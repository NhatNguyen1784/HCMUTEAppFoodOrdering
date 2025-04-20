package vn.hcmute.appfood.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import vn.hcmute.appfood.dto.*;
import vn.hcmute.appfood.services.Impl.OtpService;
import vn.hcmute.appfood.services.Impl.UserService;

import java.util.regex.Pattern;


@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private OtpService otpService;

    //API đăng nhập
    @PostMapping("/login")
    public ResponseEntity<?> loginAccount(@RequestBody LoginDTO loginDTO) {
        try{
            String email = loginDTO.getEmail();
            String password = loginDTO.getPassword();

            boolean isLoginSuccessful = userService.login(email, password);
            if (isLoginSuccessful) {
                UserDTO userDTO = userService.findByEmail(email);
                userDTO.setPassword("");//Tranh bi lo mat khau
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
            if (!Pattern.matches(emailRegex, emailRequest.getEmail())) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Invalid email format", null));
            }

            //Kiem tra ton tai cua email va phone
            if(!userService.checkExistEmailOrPhone(emailRequest.getEmail(), emailRequest.getPhone())) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Email or Phone Exist!", null));
            }

            //Thiet lap otp
            otp = otpService.generateOtp(emailRequest.getEmail().trim());
            otpService.sendOTP(emailRequest.getEmail().trim(), otp);
            ApiResponse<String> response = ApiResponse.success("OTP sent", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to send OTP", e.getMessage()));
        }
    }

    //API Xac thuc otp
    @PostMapping("/verifyOtp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpRequest otpRequest) {
        if(otpRequest.getInfUser()== null) return ResponseEntity.badRequest().body(ApiResponse.error("Information is null", null));
        if(otpRequest.getOtpCode() == null) return ResponseEntity.badRequest().body(ApiResponse.error("OTP is null", null));

        String email = otpRequest.getInfUser().getEmail();
        String otp = otpRequest.getOtpCode();

        //Xac thuc email va otp thanh cong
        if(otpService.validateOtp(email, otp)){
            String password = otpRequest.getInfUser().getPassword();
            String phone = otpRequest.getInfUser().getPhone();
            String address = otpRequest.getInfUser().getAddress();
            String fullName = otpRequest.getInfUser().getFullName();
            UserDTO userDTO = new UserDTO(email, password, fullName, phone, address, "https://res.cloudinary.com/demec8nev/image/upload/v1745039879/default_avatar_r7xkiv.png");
            userService.saveUser(userDTO);
            return ResponseEntity.ok(ApiResponse.success("OTP verified", null));
        }
        return ResponseEntity.badRequest().body(ApiResponse.error("OTP is incorrect",null));
    }

/*
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
 */
}
