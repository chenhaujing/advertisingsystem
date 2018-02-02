package com.sztouyun.advertisingsystem.service.contract;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.DocumentException;
import com.sztouyun.advertisingsystem.config.PreviewConfig;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.advertisement.AdvertisementPriceConfig;
import com.sztouyun.advertisingsystem.model.contract.*;
import com.sztouyun.advertisingsystem.model.store.StoreInfo;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementPriceConfigRepository;
import com.sztouyun.advertisingsystem.repository.contract.*;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.service.file.IFileService;
import com.sztouyun.advertisingsystem.utils.FileUtils;
import com.sztouyun.advertisingsystem.utils.PdfUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Service
public class PreviewService extends BaseService {
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private PreviewRepository previewRepository;

    @Autowired
    private IFileService fileService;

    @Autowired
    private PreviewConfig previewConfig;

    @Autowired
    private ContractOperationLogRepository contractOperationLogRepository;

    @Autowired
    private AdvertisementPriceConfigRepository advertisementPriceConfigRepository;

    @Autowired
    private ContractStoreRepository contractStoreRepository;

    @Autowired
    private ContractAdvertisementConfigRepository contractAdvertisementConfigRepository;


    /**
     * 根据合同ID查询preview记录，URL有效直接返回，否则重新生成
     * @param contractId
     * @return
     */
    @Transactional
    public String getOrCreatePreview(String contractId){
        Contract contract=contractRepository.findOne(contractId);
        if(null==contract)
            throw new BusinessException("该合同不存在");
        Preview preview=previewRepository.findByContractId(contractId);
        if(preview==null){
            preview=new Preview();
        }else{
            if(contract.getContractStatus()>ContractStatusEnum.PendingAuditing.getValue()){
                Date auditingDate=getOperationAuditingTime(contractId);
                if(auditingDate !=null && preview.getUpdatedTime().after(auditingDate)){
                    return preview.getFilePath();
                }
            }
        }
        ContractForm contractForm = getContractForm(contract);
        String filePath=createPdf(contractForm,contract.getContractName());
        preview.setUpdatedTime(new Date());
        preview.setContractId(contractId);
        preview.setFilePath(filePath);
        previewRepository.save(preview);
        return filePath;
    }

    /**
     * 准备表单和表格数据
     * @param contract
     * @return
     */
    private ContractForm getContractForm(Contract contract) {
        String contractId=contract.getId();
        ContractExtension contractExtension=contract.getContractExtension();
        ContractForm contractForm=new ContractForm();
        BeanUtils.copyProperties(contract,contractForm);
        BeanUtils.copyProperties(contractExtension,contractForm);
        contractForm.setStoreCount(contractExtension.getStoreACount(),contractExtension.getStoreBCount(),contractExtension.getStoreCCount());
        contractForm.setDateOfStart(contractExtension.getStartTime());
        contractForm.setDateOfEnd(contractExtension.getEndTime());
        final List<StoreForm> storeFormList=new ArrayList<>();
        List<StoreInfo> storeInfos=contractRepository.getStoreInfoListByContractId(contractId);
        if(storeInfos!=null && !storeInfos.isEmpty()){
            final Map<Integer,AdvertisementPriceConfig> configMap=getAdvertisementPriceConfigMap(contractId);
            storeInfos.stream().forEach(storeInfo -> {
                StoreForm storeForm=new StoreForm();
                BeanUtils.copyProperties(storeInfo,storeForm);
                Integer storeType=storeInfo.getStoreType();
                if(storeType<1 || storeType>3){
                    logger.info("预览失败,门店类型不匹配");
                    throw new BusinessException("预览失败");
                }
                AdvertisementPriceConfig advertisementPriceConfig=configMap.get(storeType);
                if(advertisementPriceConfig==null) {
                    logger.info("预览失败,找不到该类型的门店配置");
                    throw new BusinessException("预览失败");
                }
                storeForm.setStoreCost(advertisementPriceConfig.getPrice());
                storeForm.setRemark("");
                storeFormList.add(storeForm);
            });
        }
        contractForm.setStoreList(storeFormList);
        contractForm.setContractId(contractId);
        ContractAdvertisementConfig contractAdvertisementConfig=contractAdvertisementConfigRepository.findOne(contractId);
        if(contractAdvertisementConfig==null){
            contractForm.setPlaysize(previewConfig.getDefaultSize());
        }else{
            contractForm.setConfigInfo(contractAdvertisementConfig);
        }
        contractForm.setTotalCost();
        return contractForm;
    }

    /**
     * 分两个步骤生成文件
     * 模板-》临时文件-》最终文件
     * @param contractForm
     * @return
     */
    private String createPdf(ContractForm contractForm,String contractName){
        String contractId=contractForm.getContractId();
        String jsonStr=JSON.toJSONString(contractForm);
        System.out.println(jsonStr);
        JSONObject jsonObject=JSON.parseObject(jsonStr);
        String tempPath= FileUtils.getTmpPath(previewConfig.getPkgtype())+previewConfig.getTempFilePath(contractId);
        String destPath= FileUtils.getTmpPath(previewConfig.getPkgtype())+previewConfig.getDestFilePath(contractId);
        try {
            PdfUtils.createPdfFromTemplate(previewConfig,jsonObject);
            PdfUtils.mergeTableToPdf(previewConfig,jsonObject);
            File file =new File(destPath);
            String filePath= fileService.upload(new FileInputStream(file),file.length(),"application/pdf",contractName+".pdf");
            logger.info("url:"+filePath);
            FileUtils.clearFile(tempPath);
            FileUtils.clearFile(destPath);
            return filePath;
        } catch (IOException e) {
            logger.error("IOExcept:createPdf 文件读写异常",e);
            throw new BusinessException("预览失败！");
        } catch (DocumentException e) {
            logger.error("DocumentException:createPdf  Document操作异常 ",e);
            throw new BusinessException("预览失败！");
        }
    }

    /** 1.若审核通过后，根据审核时间所在区间查到
     * 2.未审核通过使用激活的配置
     * @param contractId
     * @return
     */
    private Map<Integer,AdvertisementPriceConfig> getAdvertisementPriceConfigMap(String contractId){
        Date date=getOperationAuditingTime(contractId);
        List<AdvertisementPriceConfig> list=null;
        if(date!=null){
            list=advertisementPriceConfigRepository.findAllByCreatedTimeBeforeAndUpdatedTimeAfter(date,date);
        }else{
            list=advertisementPriceConfigRepository.findAllByActived(true);
        }
        if(list==null || list.isEmpty()){
            logger.info("预览失败,找不到对应的价格配置");
            throw new BusinessException("预览失败,找不到对应的价格配置！");
        }
        Map<Integer,AdvertisementPriceConfig> configMap=new HashMap<>();
        list.stream().forEach(advertisementPriceConfig -> {
            configMap.put(advertisementPriceConfig.getStoreType(),advertisementPriceConfig);
        });
        return configMap;
    }

    private Date getOperationAuditingTime(String contractId){
       Date date=contractOperationLogRepository.findByLastestPassAuditingTime(contractId);
       return date;
    }
}
