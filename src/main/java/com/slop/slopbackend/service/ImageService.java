package com.slop.slopbackend.service;

import com.slop.slopbackend.exception.ApiRuntimeException;
import com.slop.slopbackend.utility.FilePath;
import com.slop.slopbackend.utility.FileUploadUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ImageService {
    public byte[] getImage(String imageName) {
        try{
            return FileUploadUtil.readFileToByteArray(FilePath.IMAGE_FOLDER_PATH + "/" + imageName);
        }catch (IOException e){
            e.printStackTrace();
            throw new ApiRuntimeException("Image not found", HttpStatus.NOT_FOUND);
        }
    }
}
