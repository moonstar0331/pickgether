package com.capstone.pick.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class FileUploadService {
    public String saveFile(MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            String path = new File("").getAbsolutePath() + File.separator + File.separator + "upload" + File.separator;
            File folder = new File(path);

            // 폴더가 존재하지 않을 경우
            if (!folder.exists()) {
                folder.mkdirs();
            }

            // 파일명 중복을 피하기 위해 이름 변경
            String fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + System.nanoTime() + file.getOriginalFilename();
            String filePath = path + fileName;
            File saveFile = new File(filePath);

            file.transferTo(saveFile);
            return saveFile.getPath();
        } else {
            return "";
        }
    }
}