package com.ha.oos.service.impl;

import cn.hutool.core.util.StrUtil;
import com.ha.oos.autoconfigure.MinioOssProperties;
import com.ha.oos.enums.ContentTypeEnum;
import com.ha.oos.service.MinioOosService;
import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Minio文件上传service
 *
 * @author cjx
 */
public class MinioOosServiceImpl implements MinioOosService {

    /**
     * 阿里云文件上传属性配置文件
     */
    private MinioOssProperties minioOssProperties;

    private MinioClient minioClient;

    public void config(MinioOssProperties minioOssProperties) {
        this.minioOssProperties = minioOssProperties;
        //初始化
        init();
    }

    private void init() {
        try {
            minioClient = new MinioClient(minioOssProperties.getEndPoint(), minioOssProperties.getAccessKey(), minioOssProperties.getSecretKey());
        } catch (InvalidEndpointException e) {
            e.printStackTrace();
        } catch (InvalidPortException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String upload(byte[] data, String path) {
        return upload(new ByteArrayInputStream(data), path);
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        try {
            //如果BucketName不存在，则创建
            boolean found = minioClient.bucketExists(minioOssProperties.getBucketName());
            if (!found) {
                minioClient.makeBucket(minioOssProperties.getBucketName());
            }

            minioClient.putObject(minioOssProperties.getBucketName(), path, inputStream, Long.valueOf(inputStream.available()),
                    null, null, ContentTypeEnum.getContentType(path.substring(path.lastIndexOf("."))));
        } catch (Exception e) {
            throw new RuntimeException(StrUtil.format("文件上传失败：{}", e));
        }

        return minioOssProperties.getEndPoint() + "/" + minioOssProperties.getBucketName() + "/" + path;
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix) {
        return upload(data, getPath(minioOssProperties.getPrefix(), suffix));
    }

    @Override
    public String uploadPrefixAndSuffix(byte[] data, String prefix, String suffix) {
        return upload(data, getPath(prefix, suffix));
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {
        return upload(inputStream, getPath(minioOssProperties.getPrefix(), suffix));
    }

    @Override
    public String uploadPrefixAndSuffix(InputStream inputStream, String prefix, String suffix) {
        return upload(inputStream, getPath(prefix, suffix));
    }
}
