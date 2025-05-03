package vn.hcmute.appfood.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import vn.hcmute.appfood.dto.*;
import vn.hcmute.appfood.entity.Address;
import vn.hcmute.appfood.services.Impl.AddressService;
import vn.hcmute.appfood.services.Impl.OtpService;
import vn.hcmute.appfood.services.Impl.UserService;

import java.util.List;
import java.util.regex.Pattern;


@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private OtpService otpService;

    @Autowired
    private AddressService addressService;

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
