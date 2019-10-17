package cn.hengyumo.humor.base.system.file;

import cn.hengyumo.humor.base.annotation.SystemUserAuth;
import cn.hengyumo.humor.base.result.Result;
import cn.hengyumo.humor.base.system.config.SystemResource;
import cn.hengyumo.humor.base.system.config.SystemResourceClass;
import cn.hengyumo.humor.base.system.exception.common.SystemException;
import cn.hengyumo.humor.base.system.exception.enums.SystemResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

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
