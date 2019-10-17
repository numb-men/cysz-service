package cn.hengyumo.humor.base.system.file;

import cn.hengyumo.humor.base.system.exception.common.SystemException;
import cn.hengyumo.humor.base.system.exception.enums.SystemResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * FileService
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/19
 */
@Slf4j
@Service
public class FileService {

    @Value("${hengyumo.file.upload.path}")
    private String fileUploadPath;

    public String upload(MultipartFile file) {
        if (file.isEmpty()) {
            throw new SystemException(SystemResultEnum.FILE_IS_EMPTY);
        }
        String fileName = file.getOriginalFilename();
        String suffixName = "";
        if (fileName != null) {
            suffixName = fileName.substring(fileName.lastIndexOf("."));
        }
        long fileSize = file.getSize();
        log.info("上传文件【" + fileName + "】，" +
                "文件后缀【" + suffixName + "】，文件大小【" + fileSize + "】");
        File dest = new File(fileUploadPath + fileName);
        if (!dest.getParentFile().exists()) {
            if (!dest.getParentFile().mkdir()) {
                throw new SystemException(SystemResultEnum.MKDIR_FAIL);
            }
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
            throw new SystemException(SystemResultEnum.FILE_SAVE_FAIL);
        }

        return String.format("/media/%s", fileName);
    }

    public void uploadMany(HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Iterator<String> fileNames = multipartRequest.getFileNames();
        while (fileNames.hasNext()) {
            String fileName = fileNames.next();
            List<MultipartFile> files = multipartRequest.getFiles(fileName);
            for (MultipartFile file : files) {
                upload(file);
            }
        }
    }
}
