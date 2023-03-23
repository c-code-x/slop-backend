package com.slop.slopbackend.service;

import com.slop.slopbackend.exception.ApiRuntimeException;
import com.slop.slopbackend.utility.FilePath;
import com.slop.slopbackend.utility.FileUploadUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class ImageService {
    public String saveImage(MultipartFile file){
        String fileExtension="."+ StringUtils.getFilenameExtension(file.getOriginalFilename());
        String fileName= StringUtils.cleanPath(UUID.randomUUID().toString())+fileExtension;
        try {
            FileUploadUtil.saveFile(FilePath.IMAGE_FOLDER_PATH,fileName,file);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApiRuntimeException("Could not save image", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return fileName;
    }
    public byte[] getImage(String imageName) {
        try{
            return FileUploadUtil.readFileToByteArray(FilePath.IMAGE_FOLDER_PATH + "/" + imageName);
        }catch (IOException e){
            e.printStackTrace();
            throw new ApiRuntimeException("Image not found", HttpStatus.NOT_FOUND);
        }
    }
}
