package vn.hcmute.appfoodorder.ui.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import vn.hcmute.appfoodorder.R;
import vn.hcmute.appfoodorder.model.entity.Food;
import vn.hcmute.appfoodorder.ui.adapter.SearchResultAdapter;
import vn.hcmute.appfoodorder.viewmodel.SearchViewModel;

public class SearchFragment extends Fragment {
    private SearchViewModel mViewModel;
    //private SearchView searchView;
    private RecyclerView rcvSearchResult;
    private SearchResultAdapter adapter;
    private TextView txtEmptyResult;
    private AutoCompleteTextView autoCompleteSearch;
    private ChipGroup chipGroupSuggest;


    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        mappingAndInit(view);
        setUpRecyclerViewSearch();
        suggestSearchText();
        return view;
    }

    private void setUpRecyclerViewSearch() {
        rcvSearchResult.setLayoutManager(new LinearLayoutManager(requireContext()));
        rcvSearchResult.setAdapter(adapter);

        // Lắng nghe thay đổi từ ViewModel
        mViewModel.getSearchResults().observe(getViewLifecycleOwner(), foodList -> {
            if (foodList != null && !foodList.isEmpty()) {
                adapter.updateList(foodList);
                rcvSearchResult.setVisibility(View.VISIBLE);//Visible hiển thị
                txtEmptyResult.setVisibility(View.GONE);//Gone ẩn
                chipGroupSuggest.setVisibility(View.VISIBLE);
            } else {
                adapter.updateList(new ArrayList<>());
                rcvSearchResult.setVisibility(View.GONE);
                txtEmptyResult.setVisibility(View.VISIBLE);
                chipGroupSuggest.setVisibility(View.GONE);
            }
        });
        /*
        // SearchView lắng nghe query
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mViewModel.searchFoods(query.trim());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Không xử lý realtime để tránh lag, cái này bắt sự kiện khi gõ text
                return false;
            }
        });
    */
    }


    private void mappingAndInit(View view) {
        // Ánh xạ view
        //searchView = view.findViewById(R.id.searchView);
        //searchView.setIconifiedByDefault(false);
        rcvSearchResult = view.findViewById(R.id.rcvSearchResult);
        txtEmptyResult = view.findViewById(R.id.txtEmptyResult);
        chipGroupSuggest = view.findViewById(R.id.chipGroupSuggest);
        autoCompleteSearch = view.findViewById(R.id.autoCompleteSearch);

        // ViewModel
        mViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        // Khởi tạo Adapter rỗng ban đầu
        adapter = new SearchResultAdapter(requireContext(), new ArrayList<>());
    }

    private void suggestSearchText() {
        // Suggestion keyword list
        List<String> suggestionList = Arrays.asList("Cơm", "Bún", "Phở", "Trà sữa", "Pizza", "Hamburger", "Bánh mì", "Hủ tiếu", "Cơm tấm", "Cơm chiên",
                "Xôi", "Bánh ngọt", "Kem", "Trà trái cây", "Sinh tố", "Sinh tố dâu", "Sinh tố bơ", "Sinh tố dừa", "Bánh mì que", "Cơm bò xào", "Cơm bắp cải",
                "Canh chua", "Canh khổ qua", "Canh chua cá Pasa", "Canh chua cá lóc");

        // Tạo Adapter gợi ý cho AutoComplete
        ArrayAdapter<String> suggestAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, suggestionList);
        autoCompleteSearch.setAdapter(suggestAdapter);

        // Gọi API khi chọn item từ gợi ý
        autoCompleteSearch.setOnItemClickListener((parent, view1, position, id) -> {
            String keyword = (String) parent.getItemAtPosition(position);
            mViewModel.searchFoods(keyword);
        });

        // Gán sự kiện chạm cho AutoCompleteTextView
        autoCompleteSearch.setOnTouchListener((v, event) -> {
            final int DRAWABLE_LEFT = 0; // Chỉ số 0 tương ứng với drawableStart (bên trái)
            // Kiểm tra nếu hành động chạm là buông tay (ACTION_UP)
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // Lấy drawable ở vị trí bên trái (drawableStart) trong AutoCompleteTextView
                Drawable drawable = autoCompleteSearch.getCompoundDrawables()[DRAWABLE_LEFT];
                // Kiểm tra nếu có icon (drawable) ở bên trái
                if (drawable != null) {
                    // Tính toán khoảng cách kết thúc của icon bên trái
                    int iconEnd = autoCompleteSearch.getPaddingStart() + drawable.getIntrinsicWidth();
                    // Kiểm tra nếu vị trí chạm nằm trong phạm vi của icon bên trái
                    if (event.getX() <= iconEnd) {
                        // Lấy từ khóa người dùng nhập
                        String keyword = autoCompleteSearch.getText().toString().trim();
                        // Nếu từ khóa không rỗng thì gọi ViewModel để tìm kiếm
                        if (!keyword.isEmpty()) {
                            mViewModel.searchFoods(keyword);
                        }
                        return true; // Trả về true để thông báo đã xử lý sự kiện
                    }
                }
            }
            return false;
        });

        // Tạo Chip cho các lựa chọn sắp xếp
        String[] options = {"Giá ↑", "Giá ↓", "Yêu thích", "Bán chạy"};
        for (String text : options) {
            Chip chip = new Chip(requireContext());
            chip.setText(text);
            chip.setCheckable(true);
            chip.setTextColor(requireContext().getColor(R.color.black));

            chip.setOnClickListener(v -> {
                // Kiểm tra nếu không có kết quả tìm kiếm thì không làm gì
                if (mViewModel.getSearchResults().getValue() == null || mViewModel.getSearchResults().getValue().isEmpty()) {
                    return;
                }

                String label = chip.getText().toString();
                List<Food> sortedList = new ArrayList<>(mViewModel.getSearchResults().getValue());

                // Xử lý loại bỏ lựa chọn giá tăng khi chọn giá giảm và ngược lại
                if (label.equals("Giá ↑")) {
                    for (int i = 0; i < chipGroupSuggest.getChildCount(); i++) {
                        Chip otherChip = (Chip) chipGroupSuggest.getChildAt(i);
                        if (otherChip.getText().equals("Giá ↓")) {
                            otherChip.setChecked(false);
                        }
                    }
                } else if (label.equals("Giá ↓")) {
                    for (int i = 0; i < chipGroupSuggest.getChildCount(); i++) {
                        Chip otherChip = (Chip) chipGroupSuggest.getChildAt(i);
                        if (otherChip.getText().equals("Giá ↑")) {
                            otherChip.setChecked(false);
                        }
                    }
                }

                // Sắp xếp kết quả theo lựa chọn
                switch (label) {
                    case "Giá ↑":
                        sortedList.sort(Comparator.comparing(Food::getFoodPrice));
                        break;
                    case "Giá ↓":
                        sortedList.sort((f1, f2) -> f2.getFoodPrice().compareTo(f1.getFoodPrice()));
                        break;
                    /*
                case "Yêu thích":
                    sortedList.sort((f1, f2) -> f2.getLikeCount() - f1.getLikeCount());
                    break;
                case "Bán chạy":
                    sortedList.sort((f1, f2) -> f2.getSoldCount() - f1.getSoldCount());
                    break;
                     */
                }
                adapter.updateList(sortedList);
            });
            chipGroupSuggest.addView(chip);
        }
    }


}