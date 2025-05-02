package vn.hcmute.appfood.configs;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dk8e9lwe2",
                "api_key", "585447145779693",
                "api_secret","rnN5GjxZLti0uCZv_gkHNXV3CPw"));
//        return new Cloudinary(ObjectUtils.asMap(
//                "cloud_name", "demec8nev",
//                "api_key", "172378761872566",
//                "api_secret","wbMR2pCVkfTXC2uMe9b1_6L7VgY"));
    }
}
