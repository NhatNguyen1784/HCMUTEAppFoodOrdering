package vn.hcmute.appfoodorder.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.hcmute.appfoodorder.R;
import vn.hcmute.appfoodorder.model.dto.response.ReviewListResponse;
import vn.hcmute.appfoodorder.model.dto.response.ReviewResponse;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private Context context;
    private List<ReviewResponse> reviewList;

    public ReviewAdapter(Context context) {
        this.context = context;
    }


    public void setData(List<ReviewResponse> reviews) {
        this.reviewList = reviews;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        ReviewResponse review = reviewList.get(position);
        holder.tvReviewerName.setText(review.getUserEmail()); // Nếu có tên người dùng thì dùng tên
        holder.ratingBar.setRating(review.getRating());
        if (!review.getComment().isEmpty()){ // neu co comment thi hien khong co thi an
            holder.tvReviewContent.setText(review.getComment());
            holder.tvReviewContent.setVisibility(View.VISIBLE);
        }
        else {
            holder.tvReviewContent.setVisibility(View.GONE);
        }
        // Setup RecyclerView ảnh trong từng đánh giá
        if (review.getImageUrls() != null && !review.getImageUrls().isEmpty()) {
            ReviewImageAdapter imageAdapter = new ReviewImageAdapter(context, review.getImageUrls());
            holder.rvImages.setAdapter(imageAdapter);
            holder.rvImages.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            holder.rvImages.setVisibility(View.VISIBLE);
        } else {
            holder.rvImages.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return reviewList != null ? reviewList.size() : 0;
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView tvReviewerName, tvReviewContent;
        RatingBar ratingBar;

        RecyclerView rvImages;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReviewerName = itemView.findViewById(R.id.tvReviewerName);
            tvReviewContent = itemView.findViewById(R.id.tvReviewContent);
            ratingBar = itemView.findViewById(R.id.ratingBarReview);
            rvImages = itemView.findViewById(R.id.rcvListImageReviewer);
        }
    }
}
