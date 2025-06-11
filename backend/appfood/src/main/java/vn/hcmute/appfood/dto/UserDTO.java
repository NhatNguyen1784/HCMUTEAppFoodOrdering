package vn.hcmute.appfood.dto;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String email;
    private String password;
    private String fullName;
    private String phone;
    private String address;
    private String urlImage;
    private String token;
}
