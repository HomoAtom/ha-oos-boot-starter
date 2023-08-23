package com.ha.oos.service.impl;


import cn.hutool.core.util.StrUtil;
import com.ha.oos.autoconfigure.LocalOssProperties;
import com.ha.oos.service.LocalOssService;
import org.apache.commons.io.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 本地文件上传service
 *
 * @author cjx
 */

public class LocalOssServiceImpl implements LocalOssService {

    /**
     * 本地上传属性配置文件
     */
    private LocalOssProperties localOssProperties;

    public void config(LocalOssProperties localOssProperties) {
        this.localOssProperties = localOssProperties;
    }

    @Override
    public String upload(byte[] data, String path) {
        return upload(new ByteArrayInputStream(data), path);
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        File file = new File(localOssProperties.getPath() + File.separator + path);
        try {
            FileUtils.copyToFile(inputStream, file);
        } catch (IOException e) {
            throw new RuntimeException(StrUtil.format("文件上传失败：{}", e));
        }
        return localOssProperties.getDomain() + "/" + path;
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix) {
        return upload(data, getPath(localOssProperties.getPrefix(), suffix));
    }

    @Override
    public String uploadPrefixAndSuffix(byte[] data, String prefix, String suffix) {
        return upload(data, getPath(prefix, suffix));
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {
        return upload(inputStream, getPath(localOssProperties.getPrefix(), suffix));
    }

    @Override
    public String uploadPrefixAndSuffix(InputStream inputStream, String prefix, String suffix) {
        return upload(inputStream, getPath(prefix, suffix));
    }
}
