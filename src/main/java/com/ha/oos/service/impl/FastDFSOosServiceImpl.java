package com.ha.oos.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.DefaultGenerateStorageClient;
import com.ha.oos.autoconfigure.FastDFSOssProperties;
import com.ha.oos.service.FastDFSOosService;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * FastDFS文件上传service
 *
 * @author cjx
 */
public class FastDFSOosServiceImpl implements FastDFSOosService {

    /**
     * FastDFS文件上传属性配置文件
     */
    private FastDFSOssProperties fastDFSOssProperties;

    private static DefaultGenerateStorageClient defaultGenerateStorageClient;

    static {
        defaultGenerateStorageClient = (DefaultGenerateStorageClient) SpringUtil.getBean("defaultGenerateStorageClient");
    }

    public void config(FastDFSOssProperties fastDFSOssProperties) {
        this.fastDFSOssProperties = fastDFSOssProperties;
    }


    @Override
    public String upload(byte[] data, String path) {
        return upload(new ByteArrayInputStream(data), path);
    }

    @Override
    public String upload(InputStream inputStream, String suffix) {
        StorePath storePath;
        try {
            storePath = defaultGenerateStorageClient.uploadFile("group1", inputStream, inputStream.available(), suffix);
        } catch (Exception ex) {
            throw new RuntimeException(StrUtil.format("文件上传失败：{}", ex));
        }

        return fastDFSOssProperties.getDomain() + "/" + storePath.getPath();
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix) {
        return upload(data, suffix);
    }

    @Override
    public String uploadPrefixAndSuffix(byte[] data, String prefix, String suffix) {
        return null;
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {
        return upload(inputStream, suffix);
    }

    @Override
    public String uploadPrefixAndSuffix(InputStream inputStream, String prefix, String suffix) {
        return null;
    }
}
