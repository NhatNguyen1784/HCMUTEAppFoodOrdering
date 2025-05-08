package vn.hcmute.appfoodorder.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class OnBoardingAdapter extends RecyclerView.Adapter<OnBoardingAdapter.OnBoardingViewHolder> {

    private final List<Integer> layoutIds;

    public OnBoardingAdapter(List<Integer> layoutIds) {
        this.layoutIds = layoutIds;
    }

    @NonNull
    @Override
    public OnBoardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutIds.get(viewType), parent, false);
        return new OnBoardingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnBoardingViewHolder holder, int position) {
        // nothing to bind
    }

    @Override
    public int getItemCount() {
        return layoutIds.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class OnBoardingViewHolder extends RecyclerView.ViewHolder {
        public OnBoardingViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
