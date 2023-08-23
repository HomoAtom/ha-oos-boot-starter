package com.ha.oos.service.impl;

import cn.hutool.core.util.StrUtil;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.IOUtils;
import com.ha.oos.autoconfigure.QiniuOssProperties;
import com.ha.oos.service.QiniuOosService;

import java.io.IOException;
import java.io.InputStream;

/**
 * 七牛云文件上传service
 *
 * @author cjx
 */
public class QiniuOosServiceImpl implements QiniuOosService {

    /**
     * 七牛云文件上传属性配置文件
     */
    private QiniuOssProperties qiniuOssProperties;

    private UploadManager uploadManager;
    private String token;

    public void config(QiniuOssProperties qiniuOssProperties) {
        this.qiniuOssProperties = qiniuOssProperties;
        //初始化
        init();
    }

    private void init() {
        uploadManager = new UploadManager(new Configuration(Region.autoRegion()));
        token = Auth.create(qiniuOssProperties.getAccessKey(), qiniuOssProperties.getSecretKey()).
                uploadToken(qiniuOssProperties.getBucketname());

    }

    @Override
    public String upload(byte[] data, String path) {
        try {
            Response res = uploadManager.put(data, path, token);
            if (!res.isOK()) {
                throw new RuntimeException(StrUtil.format("文件上传失败：{}", res.toString()));
            }
        } catch (Exception e) {
            throw new RuntimeException(StrUtil.format("文件上传失败：{}", e));
        }

        return qiniuOssProperties.getDomain() + "/" + path;
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        try {
            byte[] data = IOUtils.toByteArray(inputStream);
            return this.upload(data, path);
        } catch (IOException e) {
            throw new RuntimeException(StrUtil.format("文件上传失败：{}", e));
        }
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix) {
        return upload(data, getPath(qiniuOssProperties.getPrefix(), suffix));
    }

    @Override
    public String uploadPrefixAndSuffix(byte[] data, String prefix, String suffix) {
        return upload(data, getPath(prefix, suffix));
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {
        return upload(inputStream, getPath(qiniuOssProperties.getPrefix(), suffix));
    }

    @Override
    public String uploadPrefixAndSuffix(InputStream inputStream, String prefix, String suffix) {
        return upload(inputStream, getPath(prefix, suffix));
    }
}
