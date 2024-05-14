package org.sparta.springintroduction.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class FileService {

    @Value("${user.home}")
    private String uploadDir;

    public void fileUpload(MultipartFile file){
        String type = file.getContentType();
        if(!ObjectUtils.isEmpty(type)){
            if(type.contains("image")){
                Path path = Paths.get(
                        uploadDir +
                                File.separator + "Downloads" + File.separator +
                                StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename())));
                try {
                    file.transferTo(new File(path.toUri()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new IllegalArgumentException("이미지 파일이 아닙니다.");
            }
        } else {
            throw new IllegalArgumentException("파일이 없습니다.");
        }
    }
}