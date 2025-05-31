package vn.hcmute.appfood.services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.hcmute.appfood.dto.UserDTO;
import vn.hcmute.appfood.dto.UserUpdateDTO;
import vn.hcmute.appfood.entity.Role;
import vn.hcmute.appfood.entity.User;
import vn.hcmute.appfood.exception.ResourceNotFoundException;
import vn.hcmute.appfood.repository.RoleRepository;
import vn.hcmute.appfood.repository.UserRepository;
import vn.hcmute.appfood.services.IUserService;
import java.util.Collections;
import java.util.Optional;


@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CloudinaryService uploadService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //Check login
    public boolean login(String email, String password) {
        try{
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isPresent()) {
                return passwordEncoder.matches(password, user.get().getPassword());
            }
            return false;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //Find user by email
    public UserDTO findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            UserDTO userDTO = new UserDTO();
            userDTO.setEmail(user.get().getEmail());
            userDTO.setPassword("");
            userDTO.setAddress(user.get().getAddress());
            userDTO.setFullName(user.get().getFullName());
            userDTO.setPhone(user.get().getPhone());
            userDTO.setUrlImage(user.get().getUrlImage());
            return userDTO;
        }
        return null;
    }

    //Luu hoac update
    public void saveUser(UserDTO userDTO) {
        Optional<User> u = userRepository.findByEmail(userDTO.getEmail());
        User user = new User();
        if (u.isPresent()) {
            user = u.get();
        }
        Role role = roleRepository.findById(1);

        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setAddress(userDTO.getAddress());
        user.setFullName(userDTO.getFullName());
        user.setPhone(userDTO.getPhone());
        user.setRole(role);
        user.setUrlImage(userDTO.getUrlImage());
        userRepository.save(user);
    }

    public void updateUser(UserUpdateDTO dto, MultipartFile image) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with Email: " + dto.getEmail()));

        Role role = roleRepository.findById(1);

        user.setFullName(dto.getFullName());
        user.setPhone(dto.getPhone());
        user.setRole(role);
        user.setAddress(dto.getAddress());

        // xu ly anh upload kem theo (neu co)
        if(image != null && !image.isEmpty()){
                String imageUrl = uploadService.uploadImage(image);
                user.setUrlImage(imageUrl);
        }
        userRepository.save(user);
    }

    public boolean checkExistEmailOrPhone(String email, String phone) {
        Boolean existEmail = userRepository.existsByEmail(email);
        Boolean existPhone = userRepository.existsByPhone(phone);
        return !existEmail && !existPhone;
    }
}
