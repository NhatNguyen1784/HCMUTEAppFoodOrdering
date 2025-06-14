    package vn.hcmute.appfoodorder.ui.fragment;

    import androidx.lifecycle.ViewModelProvider;

    import android.annotation.SuppressLint;
    import android.content.Intent;
    import android.os.Bundle;

    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.fragment.app.Fragment;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ImageButton;
    import android.widget.ImageView;
    import android.widget.LinearLayout;
    import android.widget.TextView;
    import android.widget.Toast;
    import com.bumptech.glide.Glide;
    import vn.hcmute.appfoodorder.R;
    import vn.hcmute.appfoodorder.ui.activity.order.OrderStatusActivity;
    import vn.hcmute.appfoodorder.ui.activity.user.LoginActivity;
    import vn.hcmute.appfoodorder.ui.activity.user.UpdateProfileActivity;
    import vn.hcmute.appfoodorder.viewmodel.ProfileViewModel;

    public class ProfileFragment extends Fragment {
        private LinearLayout layoutLogout, layoutOrder, layoutEditProfile;
        private ProfileViewModel mViewModel;
        private View rootView;
        private TextView tvUNamePro, tvEmailUPro;
        private ImageView imgAUser;
        private ImageButton imageBtnCart;
        public static ProfileFragment newInstance() {
            return new ProfileFragment();
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_profile, container, false);
            return rootView;
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
            mViewModel.init(requireContext());
            mapping();
            layoutLogout.setOnClickListener(v -> {
                mViewModel.logoutAccount(requireContext());
            });
            logout();
            userInfor();
            orderStatus();

            updateProfile();

        }

        private void updateProfile() {
            layoutEditProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewModel.getUserInfor().observe(getViewLifecycleOwner(), user -> {
                        if (user != null) {
                            Intent intent = new Intent(requireContext(), UpdateProfileActivity.class);
                            // Truyền dữ liệu người dùng qua Intent
                            intent.putExtra("email", user.getEmail());
                            intent.putExtra("fullName", user.getFullName());
                            intent.putExtra("phone", user.getPhone());
                            intent.putExtra("address", user.getAddress());
                            intent.putExtra("urlImage", user.getUrlImage());
                            startActivity(intent);
                        } else {
                            Toast.makeText(requireContext(), "Không thể tải thông tin người dùng", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

        private void orderStatus() {
            layoutOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), OrderStatusActivity.class));
                }
            });
        }

        private void userInfor() {
            mViewModel.getUserInfor().observe(getViewLifecycleOwner(), u -> {
                if(u !=null){
                    tvUNamePro.setText(u.getFullName());
                    tvEmailUPro.setText(u.getEmail());
                    String urlImage = u.getUrlImage();

                    // Sử dụng Glide để tải ảnh vào ImageView
                    Glide.with(requireContext())
                            .load(urlImage)  // URL của ảnh trên Cloudinary
                            .placeholder(R.drawable.icon_default_avatar)  // Hình ảnh placeholder khi ảnh chưa tải xong
                            .into(imgAUser);  // ImageView muốn hiển thị ảnh
                }
            });
        }

        private void logout() {
            // Quan sát trạng thái logout
            mViewModel.getLogoutSuccess().observe(getViewLifecycleOwner(), isSuccess -> {
                if (isSuccess != null && isSuccess) {
                    Toast.makeText(requireContext(), "Logout successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(requireContext(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    requireActivity().finish();
                }
            });
        }

        @SuppressLint("WrongViewCast")
        private void mapping() {
            layoutLogout = rootView.findViewById(R.id.layoutLogout);
            tvEmailUPro = rootView.findViewById(R.id.txtEmailUPro);
            tvUNamePro = rootView.findViewById(R.id.txtNameUPro);
            imgAUser = rootView.findViewById(R.id.imgAUser);
            imageBtnCart = rootView.findViewById(R.id.imgBtnCart);
            layoutOrder = rootView.findViewById(R.id.layoutOrder);
            layoutEditProfile = rootView.findViewById(R.id.btnEditProfile);
        }

    }