package com.mgm.amazing_volunteer.util;

import com.mgm.amazing_volunteer.exception.InvalidFileException;
import com.mgm.amazing_volunteer.exception.NotFoundException;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

@Component
public class FileUtils {

    @Value("${file.USER_IDENTITY_IMAGE_PATH}")
    private String USER_IDENTITY_IMAGE_PATH;

    public boolean checkImageType(MultipartFile file){
        final String[] validTypes = new String[]{"jpg", "png", "jpeg"};
        final String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        return Arrays.asList(validTypes).contains(fileExtension);
    }

    public byte[] getImage(final String filename) throws NotFoundException {
        byte[] image = new byte[0];
        try {
            image = org.apache.commons.io.FileUtils.readFileToByteArray(new File(USER_IDENTITY_IMAGE_PATH+"/"+filename));
        } catch (IOException e) {
            e.printStackTrace();
            throw new NotFoundException("This File could not be found");
        }
        return image;
    }

    public String uploadImage(final MultipartFile file) throws InvalidFileException {
        if(!checkImageType(file)) throw new InvalidFileException("Only accept image file");
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path path = Paths.get(new File(USER_IDENTITY_IMAGE_PATH) + "/" + fileName);
        try(InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            throw new InvalidFileException("Can't save this file, please check it");
        }
        return fileName;
    }

    public void deleteFile(String filename){
        try {
            Files.delete(Path.of(new File(USER_IDENTITY_IMAGE_PATH + "/" + filename).getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new InvalidFileException("Can not delete file");
        }
    }
}
