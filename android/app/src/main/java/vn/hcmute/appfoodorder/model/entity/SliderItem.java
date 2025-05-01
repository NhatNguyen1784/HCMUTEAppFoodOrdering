package vn.hcmute.appfoodorder.model.entity;

public class SliderItem {
    private String imageUrl;
    private String title;

    public SliderItem(String imageUrl, String title) {
        this.imageUrl = imageUrl;
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }
}
