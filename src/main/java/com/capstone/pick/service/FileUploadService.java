package com.capstone.pick.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
@RequiredArgsConstructor
public class FileUploadService {
    private String uploadPath = "/tmp";

    public String saveFile(MultipartFile file) {
        File saveFile = new File(uploadPath, file.getOriginalFilename());
        try {
            file.transferTo(saveFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (file.isEmpty())
            return "";
        else
            return uploadPath + "/" + file.getOriginalFilename();
    }
}

