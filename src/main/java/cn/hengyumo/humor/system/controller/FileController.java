package cn.hengyumo.humor.system.controller;

import cn.hengyumo.humor.base.annotation.SystemUserAuth;
import cn.hengyumo.humor.base.result.Result;
import cn.hengyumo.humor.system.annotation.SystemResource;
import cn.hengyumo.humor.system.annotation.SystemResourceClass;
import cn.hengyumo.humor.system.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * FileController
 * TODO
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/19
 */

@Slf4j
@RestController
@RequestMapping("/system/file")
@SystemResourceClass(resourceName = "file", comment = "文件", parentResource = "system")
public class FileController {

    @Resource
    private FileService fileService;

    @SystemUserAuth
    @PostMapping("/upload")
    @SystemResource(comment = "文件上传")
    public Result upload(@RequestParam MultipartFile file) {
        return Result.success(fileService.upload(file));
    }

    @SystemUserAuth
    @PostMapping(value = "upload/many")
    @SystemResource(comment = "多文件上传")
    public Result uploadMany(HttpServletRequest request) {
        fileService.uploadMany(request);
        return Result.success();
    }
}
