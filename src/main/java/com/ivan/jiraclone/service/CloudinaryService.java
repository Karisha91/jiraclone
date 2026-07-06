package com.ivan.jiraclone.service;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Slf4j
@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }


    public String upload(MultipartFile file) {

       String result = null;
        try {
            result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("secure_url").toString();

        } catch (Exception e) {
            e.printStackTrace();

        }

        return result;
    }
}
