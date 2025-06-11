package vn.hcmute.appfoodorder.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImagePickerUtils {
    private static File currentPhotoFile;

    // Mở gallery để chọn ảnh
    public static void openGallery(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        activity.startActivityForResult(intent, Constants.PICK_IMAGE_REQUEST);
    }

    // Mở camera để chụp ảnh
    public static void openCamera(Activity activity) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = createImageFile(activity);
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(activity, "vn.hcmute.appfoodorder.fileprovider", photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            activity.startActivityForResult(intent, Constants.CAMERA_REQUEST_CODE);
        }
    }

    // Tạo một file ảnh tạm thời để lưu ảnh chụp
    private static File createImageFile(Activity activity) {
        // Tạo tên ảnh duy nhất dựa trên timestamp
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        // Lưu ảnh vào thư mục external của ứng dụng
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (storageDir != null) {
            try {
                // Tạo file ảnh mới
                currentPhotoFile =  File.createTempFile(imageFileName, ".jpg", storageDir);
                return currentPhotoFile;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // Lấy đường dẫn file ảnh từ URI khi chọn ảnh từ gallery
    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(proj[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        }
        return null;
    }
    public static File getCurrentPhotoFile() {
        return currentPhotoFile;
    }
}
