/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.ha.oos.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

/**
 * 云存储service
 *
 * @author cjx
 */
public interface BaseOosService {

    /**
     * 文件路径
     *
     * @param prefix 前缀
     * @param suffix 后缀
     * @return 返回上传路径
     */
    default String getPath(String prefix, String suffix) {
        //生成uuid
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //文件路径
        String path = DateUtil.format(new Date(), "yyyyMMdd") + "/" + uuid;

        if (StrUtil.isNotBlank(prefix)) {
            path = prefix + "/" + path;
        }

        return path + "." + suffix;
    }

    /**
     * 文件上传
     *
     * @param data 文件字节数组
     * @param path 文件路径，包含文件名
     * @return 返回http地址
     */
    String upload(byte[] data, String path);

    /**
     * 文件上传
     *
     * @param data   文件字节数组
     * @param suffix 后缀
     * @return 返回http地址
     */
    String uploadSuffix(byte[] data, String suffix);

    /**
     * 文件上传
     *
     * @param data   文件字节数组
     * @param prefix 前缀
     * @param suffix 后缀
     * @return 返回http地址
     */
    String uploadPrefixAndSuffix(byte[] data, String prefix, String suffix);

    /**
     * 文件上传
     *
     * @param inputStream 字节流
     * @param path        文件路径，包含文件名
     * @return 返回http地址
     */
    String upload(InputStream inputStream, String path);

    /**
     * 文件上传
     *
     * @param inputStream 字节流
     * @param suffix      后缀
     * @return 返回http地址
     */
    String uploadSuffix(InputStream inputStream, String suffix);

    /**
     * 文件上传
     *
     * @param inputStream 字节流
     * @param prefix      前缀
     * @param suffix      后缀
     * @return 返回http地址
     */
    String uploadPrefixAndSuffix(InputStream inputStream, String prefix, String suffix);

}
