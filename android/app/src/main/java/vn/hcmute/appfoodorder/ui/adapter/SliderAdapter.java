package vn.hcmute.appfoodorder.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import java.util.List;
import vn.hcmute.appfoodorder.R;
import vn.hcmute.appfoodorder.model.entity.SliderItem;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {

    private List<SliderItem> sliderItems;
    private Context context;

    public SliderAdapter(List<SliderItem> sliderItems, Context context) {
        this.sliderItems = sliderItems;
        this.context = context;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.slider_item, parent, false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        SliderItem item = sliderItems.get(position);
        //Log.d("SliderItem", "Image URL: " + item.getImageUrl());
        holder.txtTitle.setText(item.getTitle());
        Glide.with(context)
                .load(item.getImageUrl())
                .placeholder(R.drawable.placeholder)
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.imgSlider)
                .onLoadFailed(ContextCompat.getDrawable(context, R.drawable.orange_background));
    }

    @Override
    public int getItemCount() {
        return (sliderItems == null)? 0 : sliderItems.size();
    }

    public static class SliderViewHolder extends RecyclerView.ViewHolder {
        ImageView imgSlider;
        TextView txtTitle;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSlider = itemView.findViewById(R.id.imgSlider);
            txtTitle = itemView.findViewById(R.id.txtSliderTitle);
        }
    }
}
