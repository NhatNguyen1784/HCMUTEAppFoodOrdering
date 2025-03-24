package vn.hcmute.appfood.services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.hcmute.appfood.dto.UserDTO;
import vn.hcmute.appfood.entity.Role;
import vn.hcmute.appfood.entity.User;
import vn.hcmute.appfood.repository.RoleRepository;
import vn.hcmute.appfood.repository.UserRepository;
import vn.hcmute.appfood.services.IUserService;

import java.util.Optional;


@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    //Check login
    public boolean login(String email, String password) {
        try{
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isPresent()) {
                return user.get().getPassword().equals(password);
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
            userDTO.setPassword(user.get().getPassword());
            userDTO.setAddress(user.get().getAddress());
            userDTO.setFullName(user.get().getFullName());
            userDTO.setPhone(user.get().getPhone());
            userDTO.setRoleId(user.get().getRole().getId());
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
        user.setPassword(userDTO.getPassword());
        user.setAddress(userDTO.getAddress());
        user.setFullName(userDTO.getFullName());
        user.setPhone(userDTO.getPhone());
        user.setRole(role);
        userRepository.save(user);
    }
}
