package vn.hcmute.appfoodorder.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import vn.hcmute.appfoodorder.model.dto.request.CartRequest;
import vn.hcmute.appfoodorder.model.dto.request.DeleteCartRequest;
import vn.hcmute.appfoodorder.model.entity.Cart;
import vn.hcmute.appfoodorder.repository.CartRepository;
import vn.hcmute.appfoodorder.utils.Resource;

public class CartViewModel extends ViewModel {
    private final CartRepository cartRepository;
    private final MutableLiveData<Cart> cartLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> messageError = new MutableLiveData<>();

    public CartViewModel() {
        cartRepository = CartRepository.getInstance();
    }

    public void addItemToCart(String token, CartRequest request){
        cartRepository.addItemToCart(token, request).observeForever(new Observer<Resource<Cart>>() {
            @Override
            public void onChanged(Resource<Cart> cartResource) {
                cartLiveData.setValue(cartResource.getData());
            }
        });
    }

    public void updateCartItem(String token, CartRequest request){
        cartRepository.updateCartItem(token, request).observeForever(new Observer<Resource<Cart>>() {
            @Override
            public void onChanged(Resource<Cart> cartResource) {
                cartLiveData.setValue(cartResource.getData());
            }
        });
    }

    public void deleteCartItem(String token, DeleteCartRequest request){
        cartRepository.deleteCartItem(token, request).observeForever(new Observer<Resource<Cart>>() {
            @Override
            public void onChanged(Resource<Cart> cartResource) {
                cartLiveData.setValue(cartResource.getData());
            }
        });
    }

    public void getMyCart(String authHeader){
        cartRepository.getMyCart(authHeader).observeForever(new Observer<Resource<Cart>>() {
            @Override
            public void onChanged(Resource<Cart> cartResource) {
                cartLiveData.setValue(cartResource.getData());
            }
        });
    }

    public LiveData<Cart> getCartLiveData() {
        return cartLiveData;
    }

    public LiveData<String> getMessageError() {
        return messageError;
    }

}
