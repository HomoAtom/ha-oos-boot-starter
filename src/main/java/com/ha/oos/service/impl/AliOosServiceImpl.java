package com.ha.oos.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.OSSClient;
import com.ha.oos.autoconfigure.AliOssProperties;
import com.ha.oos.service.AliOosService;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * 阿里云文件上传service
 *
 * @author cjx
 */
public class AliOosServiceImpl implements AliOosService {

    /**
     * 阿里云文件上传属性配置文件
     */
    private AliOssProperties aliOssProperties;

    public void config(AliOssProperties aliOssProperties) {
        this.aliOssProperties = aliOssProperties;
    }

    @Override
    public String upload(byte[] data, String path) {
        return upload(new ByteArrayInputStream(data), path);
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        OSSClient client = new OSSClient(aliOssProperties.getEndPoint(), aliOssProperties.getAccessKeyId(),
                aliOssProperties.getAccessKeySecret());
        try {
            client.putObject(aliOssProperties.getBucketName(), path, inputStream);
            client.shutdown();
        } catch (Exception e) {
            throw new RuntimeException(StrUtil.format("文件上传失败：{}", e));
        }

        return aliOssProperties.getDomain() + "/" + path;
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix) {
        return upload(data, getPath(aliOssProperties.getPrefix(), suffix));
    }

    @Override
    public String uploadPrefixAndSuffix(byte[] data, String prefix, String suffix) {
        return upload(data, getPath(prefix, suffix));
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {
        return upload(inputStream, getPath(aliOssProperties.getPrefix(), suffix));
    }

    @Override
    public String uploadPrefixAndSuffix(InputStream inputStream, String prefix, String suffix) {
        return upload(inputStream, getPath(prefix, suffix));
    }
}
