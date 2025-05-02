package vn.hcmute.appfoodorder.ui.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

import vn.hcmute.appfoodorder.R;
import vn.hcmute.appfoodorder.ui.adapter.SearchResultAdapter;
import vn.hcmute.appfoodorder.viewmodel.SearchViewModel;

public class SearchFragment extends Fragment {

    private SearchViewModel mViewModel;
    private SearchView searchView;
    private RecyclerView rcvSearchResult;
    private SearchResultAdapter adapter;
    private TextView txtEmptyResult;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        mappingAndInit(view);
        setUpRecyclerViewSearch();
        return view;
    }

    private void setUpRecyclerViewSearch() {
        rcvSearchResult.setLayoutManager(new LinearLayoutManager(requireContext()));
        rcvSearchResult.setAdapter(adapter);

        // Lắng nghe thay đổi từ ViewModel
        mViewModel.getSearchResults().observe(getViewLifecycleOwner(), foodList -> {
            if (foodList != null && !foodList.isEmpty()) {
                adapter.updateList(foodList);
                rcvSearchResult.setVisibility(View.VISIBLE);
                txtEmptyResult.setVisibility(View.GONE);
            } else {
                adapter.updateList(new ArrayList<>());
                rcvSearchResult.setVisibility(View.GONE);
                txtEmptyResult.setVisibility(View.VISIBLE);
            }
        });

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
    }


    private void mappingAndInit(View view) {
        // Ánh xạ view
        searchView = view.findViewById(R.id.searchView);
        searchView.setIconifiedByDefault(false);
        rcvSearchResult = view.findViewById(R.id.rcvSearchResult);
        txtEmptyResult = view.findViewById(R.id.txtEmptyResult);

        // ViewModel
        mViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        // Khởi tạo Adapter rỗng ban đầu
        adapter = new SearchResultAdapter(requireContext(), new ArrayList<>());
    }

}