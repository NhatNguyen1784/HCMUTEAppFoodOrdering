package vn.hcmute.appfood.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import vn.hcmute.appfood.dto.*;
import vn.hcmute.appfood.services.Impl.AddressService;
import vn.hcmute.appfood.services.Impl.OtpService;
import vn.hcmute.appfood.services.Impl.UserService;

import java.util.List;
import java.time.Duration;
import java.util.regex.Pattern;

@RestController

@RequestMapping("/api/auth")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private OtpService otpService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

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


    //API gửi mã xác thực về mail dang ky tai khoan
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

    @PostMapping("/reset-password/request")
    public ResponseEntity<?> requestResetPassword(@RequestBody ResetPasswordDTO request) {
        try{
            String email = request.getEmail().trim();
            String newPassword = request.getNewPassword();

            if (email.isEmpty() || newPassword.isEmpty()) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Email and new password are required", null));
            }

            UserDTO user = userService.findByEmail(email);
            if (user == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Email not found", null));
            }

            // Lưu vào Redis
            redisTemplate.opsForValue().set("resetPassword:" + email, newPassword, Duration.ofMinutes(5));

            String otp = otpService.generateOtp(email);
            otpService.sendOTP(email, otp);

            return ResponseEntity.ok(ApiResponse.success("OTP sent", new ResetPasswordResponse(email, 300)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to send OTP", e.getMessage()));
        }

    }

    // API xac minh OTP va doi mat khau
    //    http://localhost:8081/api/auth/reset-password/verify
    @PostMapping("/reset-password/verify")
    public ResponseEntity<?> resetPassword(@RequestBody VerifyResetPasswordDTO request) {
        try {
            String email = request.getEmail();
            String otp = request.getOtpCode();

            if (!otpService.validateOtp(email, otp)) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Invalid or expired OTP", null));
            }

            // lay password moi trong redis
            String newPassword = redisTemplate.opsForValue().get("resetPassword:" + email);
            if (newPassword == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("New password expired or not found", null));
            }

            // Tìm user
            UserDTO user = userService.findByEmail(email);
            if (user == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Email not found", null));
            }

            // Cập nhật mật khẩu
            user.setPassword(newPassword); // Có thể dùng BCrypt nếu muốn bảo mật
            userService.saveUser(user);

            // xoa password khoi redis
            redisTemplate.delete("resetPassword:" + email);

            return ResponseEntity.ok(ApiResponse.success("Password has been reset successfully", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to reset password", e.getMessage()));
        }
    }

    // gui lai OTP de reset mat khau
    @PostMapping("/reset-password/resend-otp")
    public ResponseEntity<?> resendResetPasswordOtp(@RequestParam("email") String email) {
        try {
            email = email.trim();

            if (email.isEmpty()) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Email is required", null));
            }

            // Kiểm tra xem new password còn trong Redis không
            String newPassword = redisTemplate.opsForValue().get("resetPassword:" + email);
            if (newPassword == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Reset password request has expired. Please submit a new request.", null));
            }

            // Gửi lại OTP
            String otp = otpService.generateOtp(email);
            otpService.sendOTP(email, otp);

            return ResponseEntity.ok(ApiResponse.success("OTP resent successfully", new ResetPasswordResponse(email, 300)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to resend OTP", e.getMessage()));
        }
    }



/*
    //API đăng kí
    @PostMapping("/register")
    public ResponseEntity<?> registerAccount(@RequestBody RegisterDTO registerDTO) {
        try {
            List<String> addresses = addressService.findAllByUserId(email);
            if (addresses.isEmpty()) {
                return ResponseEntity.ok(ApiResponse.success("No addresses found", null));
            }
            return ResponseEntity.ok(ApiResponse.success("Addresses found", addresses));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to fetch addresses", e.getMessage()));
        }
    }
*/

    //Api get all Address shipping
    @GetMapping("/user/shipping_address")
    public ResponseEntity<?> getAllShippingAddresses(@RequestParam String email) {
        try {
            List<String> addresses = addressService.findAllByUserId(email);
            if (addresses.isEmpty()) {
                return ResponseEntity.ok(ApiResponse.success("No addresses found", null));
            }
            return ResponseEntity.ok(ApiResponse.success("Addresses found", addresses));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to fetch addresses", e.getMessage()));
        }
    }

    //Api add new address
    @PostMapping("/user/add_address")
    public ResponseEntity<?> addNewAddress(@RequestBody AddressDTO addAddressDTO) {
        try {
            String fullAddress = addAddressDTO.getFullAddress().trim();
            String email = addAddressDTO.getEmail().trim();
            if(email == null || email.isEmpty()) return ResponseEntity.badRequest().body(ApiResponse.error("Email is required", null));
            if(fullAddress == null || fullAddress.isEmpty()) return ResponseEntity.badRequest().body(ApiResponse.error("Full address is required", null));
            addressService.addAddress(email, fullAddress);
            return ResponseEntity.ok(ApiResponse.success("Address added successfully"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to add address", e.getMessage()));
        }
    }

}
