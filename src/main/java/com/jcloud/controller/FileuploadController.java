package com.jcloud.controller;

import com.jcloud.core.domain.ResponseData;
import com.jcloud.service.impl.FileUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author jiaxm
 * @date 2021/9/29
 */
@Api(tags = "文件上传服务")
@RestController
@RequestMapping("upload")
public class FileuploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @ApiOperation(value = "单文件上传")
    @RequestMapping(value = "singleFile", method = RequestMethod.POST)
    public ResponseData uploadSingleFile(@RequestParam("file") MultipartFile multipartFile) {
        return fileUploadService.uploadSingleFile(multipartFile);
    }
}
