package vn.hcmute.appfoodorder.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

import vn.hcmute.appfoodorder.R;
import vn.hcmute.appfoodorder.model.dto.ApiResponse;
import vn.hcmute.appfoodorder.model.entity.Category;
import vn.hcmute.appfoodorder.model.entity.SliderItem;
import vn.hcmute.appfoodorder.ui.adapter.CategoryAdapter;
import vn.hcmute.appfoodorder.ui.adapter.SliderAdapter;
import vn.hcmute.appfoodorder.viewmodel.CategoryViewModel;
import vn.hcmute.appfoodorder.viewmodel.SliderViewModel;

public class HomeFragment extends Fragment {
    private CategoryViewModel categoryViewModel;
    private RecyclerView rcvCategory;
    private CategoryAdapter categoryAdapter;
    private ViewPager2 viewPager2;
    private SliderViewModel sliderViewModel;
    private List<SliderItem> sliderItems;
    private SliderAdapter sliderAdapter;

    private Handler sliderHandler = new Handler();

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            if (viewPager2 != null && sliderItems != null && !sliderItems.isEmpty()) {
                int current = viewPager2.getCurrentItem();
                int next = (current + 1) % sliderItems.size();
                viewPager2.setCurrentItem(next, true);
                sliderHandler.postDelayed(this, 4000); // Slide mỗi 4s
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);// Inflate layout for fragment
        mappingAndInit(view);
        setupRecyclerView();
        fetchAllCategories();
        setupSlider();
        return view;
    }

    private void mappingAndInit(View view) {
        //Mapping
        rcvCategory = view.findViewById(R.id.rcvCategory);
        viewPager2 = view.findViewById(R.id.viewPagerSlider);
        //Init
        categoryViewModel = new CategoryViewModel();
        sliderViewModel = new ViewModelProvider(this).get(SliderViewModel.class); // Khởi tạo SliderViewModel
    }

    private void setupRecyclerView() {
        categoryAdapter = new CategoryAdapter(getContext());
        rcvCategory.setAdapter(categoryAdapter);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false);
        rcvCategory.setLayoutManager(layoutManager);
    }

    private void fetchAllCategories() {
        categoryViewModel.fetchCategories();

        categoryViewModel.getCategoryList().observe(getViewLifecycleOwner(), new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                categoryAdapter.setData(categories);
            }
        });

        categoryViewModel.getMessageError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                if (errorMessage != null && !errorMessage.isEmpty()) {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    Log.d("Error load List category", errorMessage);
                }
            }
        });
    }

    private void setupSlider() {
        if (sliderViewModel != null) {
            sliderViewModel.getSliders().observe(getViewLifecycleOwner(), new Observer<ApiResponse<List<SliderItem>>>() {
                @Override
                public void onChanged(ApiResponse<List<SliderItem>> response) {
                    if (response != null && response.getResult() != null && !response.getResult().isEmpty()) {
                        sliderItems = response.getResult();
                        if (sliderAdapter == null) {
                            // Nếu sliderAdapter chưa được khởi tạo, tạo mới
                            sliderAdapter = new SliderAdapter(sliderItems, getContext());
                            viewPager2.setAdapter(sliderAdapter);
                        } else {
                            // Nếu đã khởi tạo rồi, chỉ cần cập nhật dữ liệu
                            sliderAdapter.notifyDataSetChanged();
                        }

                        // Chỉ cần gọi một lần duy nhất
                        sliderHandler.postDelayed(sliderRunnable, 3000);

                        //Tăng trải nghiệm scroll mượt
                        viewPager2.setOffscreenPageLimit(3);
                        viewPager2.setClipToPadding(false);
                        viewPager2.setClipChildren(false);

                        viewPager2.setPageTransformer((page, position) -> {
                            page.setAlpha(0.5f + (1 - Math.abs(position)) * 0.5f);
                            page.setScaleX(0.85f + (1 - Math.abs(position)) * 0.15f);
                            page.setScaleY(0.85f + (1 - Math.abs(position)) * 0.15f);
                        });

                        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                            @Override
                            public void onPageSelected(int position) {
                                super.onPageSelected(position);
                                // Mỗi lần người dùng lướt, reset thời gian chờ auto-slide
                                sliderHandler.removeCallbacks(sliderRunnable);
                                sliderHandler.postDelayed(sliderRunnable, 4000); // Reset lại 4s
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sliderHandler.removeCallbacks(sliderRunnable); // dừng khi view bị hủy
    }
}

