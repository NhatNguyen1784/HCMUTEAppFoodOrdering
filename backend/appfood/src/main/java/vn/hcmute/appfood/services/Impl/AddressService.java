package vn.hcmute.appfood.services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.hcmute.appfood.dto.AddressDTO;
import vn.hcmute.appfood.entity.Address;
import vn.hcmute.appfood.entity.User;
import vn.hcmute.appfood.repository.AddressRepository;
import vn.hcmute.appfood.repository.UserRepository;
import vn.hcmute.appfood.services.IAddressService;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressService implements IAddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public List<String> findAllByUserId(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        List<Address> list = addressRepository.findAllByUserId(user.getId());
        List<String> addressList = new ArrayList<>();
        for (Address address : list) {
            AddressDTO dto = new AddressDTO();
            dto.setFullAddress(address.getFullAddress());
            addressList.add(address.getFullAddress());
        }
        return addressList;
    }

    @Override
    public Long CountByUserId(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return addressRepository.countAddressesByUserId(user.getId());
    }

    @Override
    public Address addAddress(String email, String fullAddress) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        List<Address> existingAddresses = addressRepository.findAllByUserId(user.getId());
        Address address;
        if (!existingAddresses.isEmpty()) {
            // Cập nhật địa chỉ cũ
            address = existingAddresses.get(0); // chỉ lấy 1 vì chỉ có 1 địa chỉ
            address.setFullAddress(fullAddress);
        } else {
            // Tạo mới địa chỉ
            address = new Address(null, fullAddress, user);
        }

        return addressRepository.save(address);
    }


}
