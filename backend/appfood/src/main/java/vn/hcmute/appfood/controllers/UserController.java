package vn.hcmute.appfood.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hcmute.appfood.dto.*;
import vn.hcmute.appfood.entity.User;
import vn.hcmute.appfood.services.Impl.OtpService;
import vn.hcmute.appfood.services.Impl.UserService;


@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private OtpService otpService;

    //API gửi mã xác thực về mail
    @PostMapping("/sendOtp")
    public ResponseEntity<?> sendOTP(@RequestBody EmailRequest emailRequest) {
        String otp;
        try {
            if (emailRequest.getEmail() == null || emailRequest.getEmail().isEmpty()) {
                return ResponseEntity.badRequest().body("Email is required");
            }
            System.out.println(emailRequest.getEmail());

            // Regex kiểm tra địa chỉ email hợp lệ
            String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";


            otp = otpService.generateOtp(emailRequest.getEmail().trim());
            otpService.sendOTP(emailRequest.getEmail().trim(), otp);
            ApiResponse<String> response = ApiResponse.success("OTP sent", otp);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to send OTP", e.getMessage()));
        }
    }

    //API Xac thuc otp
    @PostMapping("/verifyOtp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpRequest otpRequest) {
        String email = otpRequest.getEmail();
        String otp = otpRequest.getOtpCode();
        return otpService.validateOtp(email, otp) ? ResponseEntity.ok(ApiResponse.success("OTP verified", null)) : ResponseEntity.badRequest().body(ApiResponse.error("OTP is incorrect",null));
    }

    //API đăng nhập
    @PostMapping("/login")
    public ResponseEntity<?> loginAccount(@RequestBody LoginDTO loginDTO) {
        try{
            String email = loginDTO.getEmail();
            String password = loginDTO.getPassword();

            boolean isLoginSuccessful = userService.login(email, password);
            if (isLoginSuccessful) {
                UserDTO userDTO = userService.findByEmail(email);
                return ResponseEntity.ok(ApiResponse.success("Login Successful", userDTO));
            } else {
                return ResponseEntity.badRequest().body(ApiResponse.error("Login failed", null));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body(ApiResponse.error("Login failed",null));
    }

    //API đăng kí
    @PostMapping("/register")
    public ResponseEntity<?> registerAccount(@RequestBody RegisterDTO registerDTO) {
        try {
            String email = registerDTO.getEmail();
            String password = registerDTO.getPassword();
            String otp = registerDTO.getOtpCode();

            if (!otpService.validateOtp(email, otp)) {
                return ResponseEntity.badRequest().body(ApiResponse.error("OTP is incorrect", null));
            }

                String fullName = registerDTO.getFullName();
                String address = registerDTO.getAddress();
                String phone = registerDTO.getPhone();

                UserDTO userDTO = new UserDTO();
                userDTO.setEmail(email);
                userDTO.setPassword(password);
                userDTO.setFullName(fullName);
                userDTO.setAddress(address);
                userDTO.setPhone(phone);

                userService.saveUser(userDTO);
                return ResponseEntity.ok(ApiResponse.success("User registered", userDTO));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(ApiResponse.error("OTP is incorrect", null));
        }
    }
}
