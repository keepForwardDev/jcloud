package com.jcloud.service.impl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.jcloud.bean.FileuploadVo;
import com.jcloud.consts.Const;
import com.jcloud.core.config.SystemProperty;
import com.jcloud.core.domain.ResponseData;
import com.jcloud.utils.DateUtil;
import com.jcloud.utils.RandomUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author jiaxm
 * @date 2021/9/29
 */
@Service
@Slf4j
public class FileUploadService implements InitializingBean {

    /**
     * 阿里云用户accessKey
     */
    @Value("${aliyun.oss.accessKeyId:}")
    private String accessKeyId;

    /**
     * 阿里云用户密匙
     */
    @Value("${aliyun.oss.accessKeySecret:}")
    private String accessKeySecret;

    /**
     * 阿里云oss 上传的文件夹名称
     */
    @Value("${aliyun.oss.bucketName:}")
    private String bucketName;

    /**
     * 阿里云oss 上传url
     */
    @Value("${aliyun.oss.endpoint:}")
    private String endpoint;

    @Value("${aliyun.oss.fileUrl:}")
    private String fileUrl;

    @Autowired
    private Environment environment;

    @Autowired
    private SystemProperty systemProperty;

    private OSSClient ossClient;

    @SneakyThrows
    public ResponseData<FileuploadVo> uploadSingleFile(MultipartFile multipartFile) {
        List<String> profiles = Arrays.asList(environment.getActiveProfiles());
        // 开发环境本地存储, 线上存储到oss
        boolean isDev = profiles.contains(Const.PROFILE_DEV) || StringUtils.isBlank(accessKeyId);
        // 文件后缀
        String fileExtension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        // 去除文件后缀
        String baseName = FilenameUtils.getBaseName(multipartFile.getOriginalFilename());
        // + 上随机。虽然也有可能导致文件重名，但是较小
        String randomName = baseName + StringPool.UNDERSCORE + RandomUtil.getRandomString(6) + (org.apache.commons.lang3.StringUtils.isBlank(fileExtension) ? org.apache.commons.lang3.StringUtils.EMPTY : FilenameUtils.EXTENSION_SEPARATOR + fileExtension);
        // 组合成文件存储的位置
        String fileName = getFilePath(randomName, isDev);
        boolean storeSuccess = storeFile(multipartFile.getInputStream(), fileName, isDev);
        if (storeSuccess) {
            FileuploadVo fileUploadVo = new FileuploadVo();
            fileUploadVo.setFileName(multipartFile.getOriginalFilename());
            fileUploadVo.setFileUrl(getVisitUrl(fileName, isDev));
            fileUploadVo.setSize(multipartFile.getSize());
            ResponseData responseData = ResponseData.getSuccessInstance();
            responseData.setData(fileUploadVo);
            return responseData;
        } else {
            return new ResponseData<>();
        }
    }


    public ResponseData<List<FileuploadVo>> uploadMultiFile(List<MultipartFile> files) {
        List<FileuploadVo> fileUploadVos = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                fileUploadVos.add(uploadSingleFile(file).getData());
            }
        }
        ResponseData responseData = ResponseData.getSuccessInstance();
        responseData.setData(fileUploadVos);
        return responseData;
    }

    /**
     * 删除oss文件
     *
     * @param url
     */
    public void deleteFile(String url) {
        ossClient.deleteObject(bucketName, url.replace(fileUrl, org.apache.commons.lang3.StringUtils.EMPTY));
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        if (StringUtils.isNotBlank(accessKeyId)) {
            ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        }
    }

    private boolean storeFile(InputStream inputStream, String fileName, boolean dev) {
        try {
            if (dev) { // 开发环境本地存储
                FileUtils.copyInputStreamToFile(inputStream, new File(fileName));
            } else { // 线上环境存储到oss
                ObjectMetadata meta = new ObjectMetadata();
                Optional<MediaType> mediaType = MediaTypeFactory.getMediaType(fileName);
                meta.setContentType(mediaType.orElse(MediaType.APPLICATION_OCTET_STREAM).toString());
                ossClient.putObject(new PutObjectRequest(bucketName, fileName, inputStream, meta));
            }
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return false;
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    public String getFilePath(String fileName, boolean dev) {
        if (dev) { // 开发环境本地存储
            return systemProperty.getExtPath() + StringPool.SLASH + Const.FILE_UPLOAD_PATH + StringPool.SLASH + DateUtil.formatDate(new Date(), "yyyyMMdd") + StringPool.SLASH + fileName;
        } else { // 线上环境存储到oss
            return Const.FILE_UPLOAD_PATH + StringPool.SLASH + fileName;
        }
    }

    private String getVisitUrl(String fileName, boolean dev) {
        if (dev) {
            return getLocalBasePath() + fileName.replace(systemProperty.getExtPath(), StringUtils.EMPTY);
        } else {
            return fileUrl + fileName;
        }
    }

    /**
     * 本地路径
     *
     * @return
     */
    public String getLocalBasePath() {
        //return StringPool.SLASH + Const.FILE_UPLOAD_PATH + Const.FILE_VISIT_PREFIX;
        return Const.FILE_VISIT_PREFIX;
    }

}
