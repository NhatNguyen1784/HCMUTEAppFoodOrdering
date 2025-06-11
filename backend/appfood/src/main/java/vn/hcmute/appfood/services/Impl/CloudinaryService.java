package vn.hcmute.appfood.services.Impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    // up anh
    public String uploadImage(MultipartFile file) {
        try {
            Map<String, String> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Loi khi up anh: " + e.getMessage());
        }
    }

    // xoa anh bang url
    public void deleteImage(String imgUrl) {

        try {
            // lay public_id
            String publicId = getPublicIdFromUrl(imgUrl);
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("Loi khi xoa anh: " + e.getMessage());
        }

    }

    // lay publicId cua anh tren cloud
    private String getPublicIdFromUrl(String url) {
        String[] parts = url.split("/");
        String publicIdExtend = parts[parts.length - 1]; // lay ra file name co ca .jpg
        String publicId = publicIdExtend.split("\\.")[0]; // chi lay phan name bo phan duoi .jpg
        return publicId;
    }

}
