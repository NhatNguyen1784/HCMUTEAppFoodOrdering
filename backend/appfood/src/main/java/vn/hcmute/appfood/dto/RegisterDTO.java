package vn.hcmute.appfood.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {
    private String otpCode;
    private String email;
    private String password;
    private String fullName;
    private String phone;
    private String address;
    private String urlImage;
}
