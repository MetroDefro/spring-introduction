package org.sparta.springintroduction.service;

import lombok.RequiredArgsConstructor;
import org.sparta.springintroduction.entity.File;
import org.sparta.springintroduction.repository.FileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    public File createFile(MultipartFile multiFile){
        String type = multiFile.getContentType();
        int size = (int) multiFile.getSize();
        try {
            validationFile(type, size);
            byte[] bytes = multiFile.getBytes();
            String name = multiFile.getOriginalFilename();
            File file = File.builder()
                    .name(name)
                    .type(type)
                    .size(size)
                    .contents(bytes)
                    .build();

            return fileRepository.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public File updateFile(Long id, MultipartFile multiFile) {
        File file = fileRepository.findById(id).orElseThrow(() -> new NullPointerException("해당하는 파일이 없습니다."));
        String type = multiFile.getContentType();
        int size = (int) multiFile.getSize();
        try {
            validationFile(type, size);
            byte[] bytes = multiFile.getBytes();
            String name = multiFile.getOriginalFilename();
            file.update(name, type, size, bytes);

            return fileRepository.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteFile(Long id){
        fileRepository.deleteById(id);
    }

    private void validationFile(String type, int size) throws IllegalArgumentException {
        if(ObjectUtils.isEmpty(type)) {
            throw new IllegalArgumentException("파일이 없습니다.");
        }

        if(!type.equals("image/jpg") && !type.equals("image/png")) {
            throw new IllegalArgumentException("파일 형식은 png, jpg만 가능합니다.");
        }

        if(size > 5000000) {
            throw new IllegalArgumentException("용량은 최대 5MB까지만 가능합니다.");
        }
    }
}