package com.ha.oos.service.impl;

import cn.hutool.core.util.StrUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.UploadResult;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.TransferManagerConfiguration;
import com.qcloud.cos.transfer.Upload;
import com.ha.oos.autoconfigure.TencentOssProperties;
import com.ha.oos.service.TencentOosService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 腾讯云文件上传service
 *
 * @author cjx
 */
public class TencentOosServiceImpl implements TencentOosService {

    /**
     * 腾讯云文件上传属性配置文件
     */
    private TencentOssProperties tencentOssProperties;

    private COSCredentials credentials;

    private ClientConfig clientConfig;

    private COSClient client;

    public void config(TencentOssProperties tencentOssProperties) {
        this.tencentOssProperties = tencentOssProperties;
        //初始化
        init();
    }

    private void init() {
        //1、初始化用户身份信息(secretId, secretKey)
        credentials = new BasicCOSCredentials(tencentOssProperties.getSecretId(), tencentOssProperties.getSecretKey());

        //2、设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        clientConfig = new ClientConfig(new Region(tencentOssProperties.getRegion()));

        //3、生成 cos 客户端
        client = new COSClient(credentials, clientConfig);
    }

    @Override
    public String upload(byte[] data, String path) {
        return upload(new ByteArrayInputStream(data), path);
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        return tencentOssProperties.getBlock() ? blockUpload(inputStream, path) : simpleUpload(inputStream, path);
    }

    /**
     * 简单上传
     *
     * @param inputStream 字节流
     * @param path        文件路径，包含文件名
     * @return 返回http地址
     */
    private String simpleUpload(InputStream inputStream, String path) {
        try {

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(inputStream.available());
            String bucketName = tencentOssProperties.getBucketName() + "-" + tencentOssProperties.getAppId();
            PutObjectRequest request = new PutObjectRequest(bucketName, path, inputStream, metadata);
            PutObjectResult result = client.putObject(request);

            client.shutdown();
            if (result.getETag() == null) {
                throw new RuntimeException(StrUtil.format("文件上传失败：{}", ""));
            }
        } catch (IOException e) {
            throw new RuntimeException(StrUtil.format("文件上传失败：{}", e));
        }

        return tencentOssProperties.getDomain() + "/" + path;
    }

    /**
     * 分块上传
     *
     * @param inputStream 字节流
     * @param path        文件路径，包含文件名
     * @return 返回http地址
     */
    private String blockUpload(InputStream inputStream, String path) {
        try {
            // 线程池大小，建议在客户端与 COS 网络充足（例如使用腾讯云的 CVM，同地域上传 COS）的情况下，设置成16或32即可，可较充分的利用网络资源
            // 对于使用公网传输且网络带宽质量不高的情况，建议减小该值，避免因网速过慢，造成请求超时。
            ExecutorService threadPool = Executors.newFixedThreadPool(32);
            // 传入一个 threadpool, 若不传入线程池，默认 TransferManager 中会生成一个单线程的线程池。
            TransferManager transferManager = new TransferManager(client, threadPool);
            // 设置高级接口的分块上传阈值和分块大小为10MB
            TransferManagerConfiguration transferManagerConfiguration = new TransferManagerConfiguration();
            transferManagerConfiguration.setMultipartUploadThreshold(10 * 1024 * 1024);
            transferManagerConfiguration.setMinimumUploadPartSize(10 * 1024 * 1024);
            transferManager.setConfiguration(transferManagerConfiguration);
            String bucketName = tencentOssProperties.getBucketName() + "-" + tencentOssProperties.getAppId();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(inputStream.available());
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, path, inputStream, metadata);
            Upload upload = transferManager.upload(putObjectRequest);
            UploadResult result = upload.waitForUploadResult();
            if (result.getETag() == null) {
                throw new RuntimeException(StrUtil.format("文件上传失败：{}", ""));
            }
        } catch (Exception e) {
            throw new RuntimeException(StrUtil.format("文件上传失败：{}", e));
        }

        return tencentOssProperties.getDomain() + "/" + path;
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix) {
        return upload(data, getPath(tencentOssProperties.getPrefix(), suffix));
    }

    @Override
    public String uploadPrefixAndSuffix(byte[] data, String prefix, String suffix) {
        return upload(data, getPath(prefix, suffix));
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {
        return upload(inputStream, getPath(tencentOssProperties.getPrefix(), suffix));
    }

    @Override
    public String uploadPrefixAndSuffix(InputStream inputStream, String prefix, String suffix) {
        return upload(inputStream, getPath(prefix, suffix));
    }
}
