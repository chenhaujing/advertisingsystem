package com.sztouyun.advertisingsystem.utils;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class QiNiuFileUtils {
    @Value("${access.key}")
    private String ACCESS_KEY;
    @Value("${secret.key}")
    private String SECRET_KEY;
    @Value("${bucket.name}")
    private String BUCKET_NAME;
    @Value("${bucket.host.name}")
    private String BUCKET_HOST_NAME;

    private Configuration cfg = new Configuration(Zone.autoZone());
    private UploadManager uploadManager = new UploadManager(cfg);

    /**
     * 上传
     * @param inputStream File 文件流
     * @param key 上传到七牛上的文件的名称  （同一个空间下，名称【key】是唯一的）
     * @param isCover 是否覆盖文件
     */
    public Response upload(InputStream inputStream, String key, boolean isCover) {
        Response response;
        try {
            if(isCover){
                response = uploadManager.put(inputStream, key, getUpTokenCover(key), null , null);
            }else{
                response = uploadManager.put(inputStream, key, getUpToken(), null , null);
            }
        } catch (QiniuException e) {
            response = e.response;
        }
        return response;
    }

    /**
     * 删除
     * @param key 上传到七牛上的文件的名称
     */
    public Response delete(String key) {
        Response response;
        try {
            Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
            BucketManager bucketMgr = new BucketManager(auth, cfg);
            response = bucketMgr.delete(BUCKET_NAME, key);
        } catch (QiniuException e) {
            response = e.response;
        }
        return response;
    }

    /**
     * 获取凭证
     * @return
     */
    private String getUpToken() {
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        return auth.uploadToken(BUCKET_NAME);
    }

    /**
     * 获取凭证(相同key)
     * @return
     */
    private String getUpTokenCover(String key) {
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        //insertOnly 如果希望只能上传指定key的文件，并且不允许修改，那么可以将下面的 insertOnly 属性值设为 1
        return auth.uploadToken(BUCKET_NAME, key, 3600, new StringMap().put("insertOnly", 0));
    }

    /**
     * 空间默认域名
     * @return
     */
    public String getUrl(){
        return BUCKET_HOST_NAME;
    }
}