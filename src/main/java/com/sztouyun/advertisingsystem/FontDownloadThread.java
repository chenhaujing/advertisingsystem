package com.sztouyun.advertisingsystem;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.config.PreviewConfig;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Created by wenfeng on 2017/8/28.
 */
@Component
public class FontDownloadThread implements  Runnable {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    PreviewConfig previewConfig;
    public FontDownloadThread(PreviewConfig previewConfig){
        this.previewConfig=previewConfig;
    }

    public PreviewConfig getPreviewConfig() {
        return previewConfig;
    }

    public void setPreviewConfig(PreviewConfig previewConfig) {
        this.previewConfig = previewConfig;
    }

    @Override

    public void run() {
        initTempDirAndFont(previewConfig);
    }

    /**
     * 当以JAR运行时，在JAR所在目录创建临时目录，并且从七牛下载字体文件
     * @param previewConfig
     */
    private   void initTempDirAndFont(PreviewConfig previewConfig){
        if(previewConfig.getPkgtype().equals(Constant.JAR)){
            File tempFile=new File(FileUtils.getJarPath()+previewConfig.getTempdir());
            FileUtils.mkDir(tempFile);
            FileUtils.mkDir(new File(FileUtils.getJarPath()+previewConfig.getFiledir()));
            String localFont=previewConfig.getFullFontPath();
            localFont=localFont.substring(0,localFont.length()-2);
            File fontFile=new File(localFont);
            if(tempFile.exists() && !fontFile.exists()){
                try {
                    FileUtils.downloadFile(previewConfig.getRemotefont(),localFont);
                } catch (Exception e) {
                    logger.error("下载字体文件失败",e);
                    throw new BusinessException("下载字体文件失败");
                }
            }
        }
    }
}
