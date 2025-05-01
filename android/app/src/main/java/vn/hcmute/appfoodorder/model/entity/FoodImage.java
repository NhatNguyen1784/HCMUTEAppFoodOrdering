package vn.hcmute.appfoodorder.model.entity;

import java.io.Serializable;

public class FoodImage implements Serializable {
    private Long id;

    private String imageUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public FoodImage() {
    }

    public FoodImage(Long id, String imageUrl) {
        this.id = id;
        this.imageUrl = imageUrl;
    }
}
