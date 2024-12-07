package com.tu.mall.template;

import cn.hutool.core.lang.UUID;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.tu.mall.common.exception.CustomException;
import com.tu.mall.common.result.ResultCodeEnum;
import com.tu.mall.properties.OSSProperties;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author JiFeiYe
 * @since 2024/10/17
 */
public class OSSTemplate {
    private final OSSProperties ossProperties;

    public OSSTemplate(OSSProperties ossProperties) {
        this.ossProperties = ossProperties;
    }

    /**
     * 文件上传
     *
     * @param file 文件流
     * @return {@code String}
     */
    public String upload(MultipartFile file) {
        String endpoint = ossProperties.getEndpoint();
        String accessKeyId = ossProperties.getAccessKeyId();
        String accessKeySecret = ossProperties.getAccessKeySecret();
        String bucketName = ossProperties.getBucketName();
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 上传进bucket位置，例如2024/10/17/fileName.jpg
        String fileName = file.getOriginalFilename();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String s = today.format(formatter);
        String objectName = s + "/" + UUID.fastUUID() + fileName.substring(fileName.lastIndexOf(".")); // 改名以防重名
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, file.getInputStream());
            ossClient.putObject(putObjectRequest);
        } catch (IOException e) {
            throw new CustomException(ResultCodeEnum.UPLOAD_FAIL);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        // 返回存入数据库的地址
        return ossProperties.getUrl() + objectName;
    }

    /**
     * 文件删除
     *
     * @param filename 文件名
     */
    public void delete(String filename) {
        String endpoint = ossProperties.getEndpoint();
        String accessKeyId = ossProperties.getAccessKeyId();
        String accessKeySecret = ossProperties.getAccessKeySecret();
        String bucketName = ossProperties.getBucketName();
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 删除文件或目录。如果要删除目录, 目录必须为空。
        ossClient.deleteObject(bucketName, filename);
        // 关闭OSSClient实例。
        ossClient.shutdown();
    }
}
