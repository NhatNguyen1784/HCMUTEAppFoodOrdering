package vn.hcmute.appfoodorder.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.List;
import vn.hcmute.appfoodorder.R;
import vn.hcmute.appfoodorder.model.entity.FoodImage;

public class ImageFoodSliderAdapter extends RecyclerView.Adapter<ImageFoodSliderAdapter.ImageViewHolder> {
    private List<FoodImage> foodImages;
    private Context context;

    public ImageFoodSliderAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<FoodImage> foodImages){
        this.foodImages = foodImages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ImageFoodSliderAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_slider, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageFoodSliderAdapter.ImageViewHolder holder, int position) {
        FoodImage foodImage = foodImages.get(position);

        Glide.with(context)
                .load(foodImage.getImageUrl())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return foodImages == null ? 0 : foodImages.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageItem);
        }
    }
}
