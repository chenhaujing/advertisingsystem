package com.sztouyun.advertisingsystem.service.advertisement.material;

import com.querydsl.core.types.Projections;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.advertisement.material.AdvertisementMaterial;
import com.sztouyun.advertisingsystem.model.advertisement.material.QAdvertisementMaterial;
import com.sztouyun.advertisingsystem.model.common.MaterialTypeEnum;
import com.sztouyun.advertisingsystem.model.contract.ContractAdvertisementConfig;
import com.sztouyun.advertisingsystem.model.contract.QContract;
import com.sztouyun.advertisingsystem.repository.advertisement.material.AdvertisementMaterialRepository;
import com.sztouyun.advertisingsystem.repository.contract.ContractAdvertisementConfigRepository;
import com.sztouyun.advertisingsystem.repository.contract.ContractRepository;
import com.sztouyun.advertisingsystem.repository.customer.CustomerRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.service.file.IFileService;
import com.sztouyun.advertisingsystem.utils.FileUtils;
import com.sztouyun.advertisingsystem.viewmodel.advertisement.material.AdvertisementMaterialStatisticsViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

@Service
public class AdvertisementMaterialService extends BaseService {
    @Autowired
    private AdvertisementMaterialRepository advertisementMaterialRepository;

    @Autowired
    private ContractAdvertisementConfigRepository contractAdvertisementConfigRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private IFileService fileService;

    @Autowired
    private CustomerRepository customerRepository;

    @Value(value = "${advertisement.upload.video.size}")
    private Long vedioLimitSize;

    private static final Map<Integer, List<String>> materialExtensionMap = new HashMap<Integer, List<String>>() {
        {
            put(MaterialTypeEnum.Img.getValue(), new ArrayList<String>(Arrays.asList("png", "jpg", "jpeg", "bmp")));
            put(MaterialTypeEnum.Video.getValue(), new ArrayList<String>(Arrays.asList("flv", "avi", "mp4", "mkv", "wmv")));
        }
    };

    private final QAdvertisementMaterial qAdvertisementMaterial = QAdvertisementMaterial.advertisementMaterial;

    private final QContract qContract = QContract.contract;

    @Transactional
    public AdvertisementMaterial uploadMaterial(String contractId, String customerId, Integer materialType, MultipartFile file) {

        if (!customerRepository.exists(customerId))
            throw new BusinessException("客户信息不存在");

        boolean existsContractCustomer = contractRepository.exists(qContract.customerId.eq(customerId).and(qContract.id.eq(contractId)));
        if (!existsContractCustomer)
            throw new BusinessException("不存在合同客户信息");

        validateFile(contractId, materialType, file);
        String fileUrl;
        try {
            fileUrl = fileService.upload(file.getInputStream(),file.getSize(),file.getContentType(),file.getOriginalFilename());
        } catch (IOException e) {
            throw new BusinessException("无效文件！");
        }
        AdvertisementMaterial advertisementMaterial = new AdvertisementMaterial();
        advertisementMaterial.setCustomerId(customerId);
        advertisementMaterial.setData(fileUrl);
        advertisementMaterial.setMaterialType(materialType);
        advertisementMaterial.setFileName(file.getOriginalFilename());
        advertisementMaterial.setMaterialSize(FileUtils.convertFileSize(file.getSize()));
        if (MaterialTypeEnum.Img.getValue().equals(materialType)) {
            advertisementMaterial.setMaterialSpecification(getImgSpecification(contractId));
        }
        return advertisementMaterialRepository.save(advertisementMaterial);
    }


    public Page<AdvertisementMaterial> getAllMaterials(Pageable pageable, String customerId, Integer materialType) {

        if (!customerRepository.exists(customerId))
            throw new BusinessException("客户信息不存在");

        return advertisementMaterialRepository.findAllAuthorized(qAdvertisementMaterial.customerId.eq(customerId).and(qAdvertisementMaterial.materialType.eq(materialType)), pageable);
    }

    public String saveTextMaterial(String customerId, String data) {

        String materialId = advertisementMaterialRepository.findOne(queryFactory -> {
            return queryFactory.select(qAdvertisementMaterial.id).from(qAdvertisementMaterial).where(
                    qAdvertisementMaterial.customerId.eq(customerId).and(qAdvertisementMaterial.data.eq(data).and(qAdvertisementMaterial.materialType.eq(MaterialTypeEnum.Text.getValue()))));
        });

        if (!StringUtils.isEmpty(materialId))
            return materialId;

        AdvertisementMaterial advertisementMaterial = new AdvertisementMaterial();
        advertisementMaterial.setMaterialType(MaterialTypeEnum.Text.getValue());
        advertisementMaterial.setCustomerId(customerId);
        advertisementMaterial.setData(data);

        return advertisementMaterialRepository.save(advertisementMaterial).getId();
    }

    public List<AdvertisementMaterialStatisticsViewModel> getMaterialStatistics(String customerId) {

        List<AdvertisementMaterialStatisticsViewModel> materialStatisticsListItem = advertisementMaterialRepository.findAll(queryFactory -> queryFactory
                .select(Projections.bean(AdvertisementMaterialStatisticsViewModel.class, qAdvertisementMaterial.id.count().as("totalMaterials"), qAdvertisementMaterial.materialType))
                .from(qAdvertisementMaterial)
                .where(qAdvertisementMaterial.customerId.eq(customerId))
                .groupBy(qAdvertisementMaterial.materialType)
        );

        List<Integer> materialStatisticsTypes = new ArrayList<Integer>();
        materialStatisticsListItem.forEach(materialStatisticsItem -> {
            materialStatisticsTypes.add(materialStatisticsItem.getMaterialType());
        });

        Arrays.stream(MaterialTypeEnum.values()).forEach(materialTypeEnum -> {
            if (!materialStatisticsTypes.contains(materialTypeEnum.getValue())) {
                AdvertisementMaterialStatisticsViewModel advertisementMaterialStatisticsViewModel = new AdvertisementMaterialStatisticsViewModel();
                advertisementMaterialStatisticsViewModel.setMaterialType(materialTypeEnum.getValue());
                advertisementMaterialStatisticsViewModel.setTotalMaterials(0L);
                materialStatisticsListItem.add(advertisementMaterialStatisticsViewModel);
            }
        });
        return materialStatisticsListItem;
    }


    /**
     * 验证文件是否有效
     *
     * @param materialType
     * @param file
     */
    private void validateFile(String contractId, Integer materialType, MultipartFile file) {
        String fileExtension = FileUtils.getFileExtension(file.getOriginalFilename());

        boolean hasValidExtension = materialExtensionMap.get(materialType).contains(fileExtension);
        if (!hasValidExtension)
            throw new BusinessException("不支持该文件格式");

        if (materialType.equals(MaterialTypeEnum.Img.getValue())) {
            try {
                ContractAdvertisementConfig contractAdvertisementConfig = contractAdvertisementConfigRepository.findOne(contractId);
                BufferedImage sourceImg = ImageIO.read(file.getInputStream());

                int width = sourceImg.getWidth(); // 源图宽度
                int height = sourceImg.getHeight();// 源图高度

                if (!(contractAdvertisementConfig.getHorizontalResolution().equals(width) && contractAdvertisementConfig.getVerticalResolution().equals(height)))
                    throw new BusinessException("请选择正确规格的图片");

            } catch (IOException e) {
                logger.error("IOExcept:上传图片素材读写异常", e);
                throw new BusinessException("上传素材失败");
            }

        } else if (materialType.equals(MaterialTypeEnum.Video.getValue())) {
            if (file.getSize() > vedioLimitSize)
                throw new BusinessException("视频文件大小超过限制!");
        }
    }

    public String getImgSpecification(String contractId) {
        ContractAdvertisementConfig contractAdvertisementConfig = contractAdvertisementConfigRepository.findOne(contractId);
        if(contractAdvertisementConfig == null)
            throw new BusinessException("图片规格不存在");
        return contractAdvertisementConfig.getHorizontalResolution() + "*" + contractAdvertisementConfig.getVerticalResolution();
    }

    public Long getVedioLimitSize() {
        return vedioLimitSize;
    }

    public void setVedioLimitSize(Long vedioLimitSize) {
        this.vedioLimitSize = vedioLimitSize;
    }
}
