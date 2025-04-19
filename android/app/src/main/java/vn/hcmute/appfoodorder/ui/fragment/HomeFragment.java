package vn.hcmute.appfoodorder.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.hcmute.appfoodorder.R;
import vn.hcmute.appfoodorder.model.entity.Category;
import vn.hcmute.appfoodorder.ui.adapter.CategoryAdapter;
import vn.hcmute.appfoodorder.viewmodel.CategoryViewModel;

public class HomeFragment extends Fragment {
    private CategoryViewModel categoryViewModel;
    private RecyclerView rcvCategory;
    private CategoryAdapter categoryAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // inflate layout for fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // get data
        categoryViewModel = new CategoryViewModel();

        // lay danh sach category
        getAllCategory(view);

        return view;
    }

    private void getAllCategory(View view) {
        rcvCategory = view.findViewById(R.id.rcvCategory);
        categoryAdapter = new CategoryAdapter(getContext());

        //get data from viewmodel
        categoryViewModel.fetchCategories();

        //observe
        categoryViewModel.getCategoryList().observe(getViewLifecycleOwner(), new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                categoryAdapter.setData(categories);
            }
        });

        categoryViewModel.getMessageError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                Log.d("Error load category: ", s);
            }
        });

        categoryViewModel.getGetError().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Toast.makeText(getContext(), "Error: ", Toast.LENGTH_SHORT).show();
            }
        });

        //  RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),  LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false);
        rcvCategory.setLayoutManager(layoutManager);
        rcvCategory.setAdapter(categoryAdapter);
    }


}
