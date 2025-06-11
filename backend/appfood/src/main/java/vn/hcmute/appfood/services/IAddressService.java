package vn.hcmute.appfood.services;

import vn.hcmute.appfood.entity.Address;

import java.util.List;

public interface IAddressService {
    List<String> findAllByUserId(String email);

    Long CountByUserId(String email);

    Address addAddress(String email, String fullAddress);
}
