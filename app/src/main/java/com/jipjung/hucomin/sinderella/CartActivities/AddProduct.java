package com.jipjung.hucomin.sinderella.CartActivities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.jipjung.hucomin.sinderella.PostActivities.Posting;
import com.jipjung.hucomin.sinderella.R;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

public class AddProduct extends AppCompatActivity {

    private String imagePath;
    private Uri filePath;
    private FirebaseFirestore mFirestore;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private ImageView imageView;
    private Button item_submit_btn;
    private ImageButton item_choose_btn;
    private EditText model_name;
    private EditText brand_name;
    private EditText product_url;
    private EditText price;
    private Spinner foot_size_correction2;
    private Button action_bar_back_close;
    private EditText item_option_txt;
    private File tempFile;
    private static final String TAG = "blackjin";

    private Boolean isPermission = true;

    //    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_CAMERA = 2;
    private static final int PICK_FROM_ALBUM = 1;
//    private static final int CROP_FROM_CAMERA = 2;

    private Uri mImageCaptureUri;
    private ImageView mPhotoImageView;
    private Button mButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_items_upload);
        item_choose_btn = findViewById(R.id.item_choose_btn);
        item_submit_btn = findViewById(R.id.item_submit_btn);
        model_name = findViewById(R.id.model_name);
        brand_name = findViewById(R.id.brand_name);
        product_url = findViewById(R.id.product_url);
        tedPermission();

        action_bar_back_close = findViewById(R.id.action_bar_back_close);
        item_option_txt = findViewById(R.id.item_option_txt);
        price = findViewById(R.id.price);
        foot_size_correction2 = findViewById(R.id.foot_size_correction2);
        imageView =  findViewById(R.id.postImage);
        mFirestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        item_choose_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int permissionCheck = ContextCompat.checkSelfPermission(AddProduct.this, Manifest.permission.CAMERA);
//                if(permissionCheck== PackageManager.PERMISSION_DENIED){
//                    // 권한 없음
//                    ActivityCompat.requestPermissions(AddProduct.this,new String[]{Manifest.permission.CAMERA},0);
//                    //Toast.makeText(getApplicationContext(),"권한없음",Toast.LENGTH_SHORT).show();
//                }else{
                //권한 있음
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(intent,1);

//                    doTakePhotoAction();
                DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(isPermission)  takePhoto();
                        else Toast.makeText(AddProduct.this, getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
                    }
                };

                DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(isPermission) goToAlbum();
                        else Toast.makeText(AddProduct.this, getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
                    }
                };

                DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                };

                new AlertDialog.Builder(AddProduct.this)
                        .setTitle("업로드할 이미지 선택")
                        .setNegativeButton("취소", cancelListener)
                        .setPositiveButton("사진촬영", cameraListener)
                        .setNeutralButton("앨범선택", albumListener)
                        .show();
            }
//            }
        });
        item_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validatePost()) {
                    uploadImage();
                    writeNewPost();
                }
            }
        });

        action_bar_back_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }





    private void writeNewPost() {
        WriteBatch batch = mFirestore.batch();
        DocumentReference products = mFirestore.collection("products").document();
        if (validatePost()) {
            Map<String, Object> docData = new HashMap<>();
            docData.put("id", products.getId());

            docData.put("brand", brand_name.getText().toString());
            docData.put("category", foot_size_correction2.getSelectedItem().toString());
            docData.put("name", model_name.getText().toString());
            docData.put("price", Integer.valueOf(price.getText().toString()));

            docData.put("product_url", product_url.getText().toString());
            docData.put("option",item_option_txt.getText().toString());
            if (imagePath != null) {
                docData.put("image_url", imagePath);
            }
            SimpleDateFormat s = new SimpleDateFormat("yyyyMMddkkmmss");
            String format = s.format(new Date());

            docData.put("created_at", format);
            batch.set(products, docData);
            batch.commit();
        }
    }

    private void tedPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

    }

    public void shoeDialogNotImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("사진을 넣어주세요!");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        builder.show();
    }

    public boolean validatePost() {
        boolean valid = true;
        if (filePath == null) {
            shoeDialogNotImage();
            valid = false;
            return valid;
        }
        return valid;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();

            if (tempFile != null) {
                if (tempFile.exists()) {
                    if (tempFile.delete()) {
                        Log.e(TAG, tempFile.getAbsolutePath() + " 삭제 성공");
                        tempFile = null;
                    }
                }
            }

            return;
        }

        if (requestCode == PICK_FROM_ALBUM) {

            filePath = data.getData();
            Log.d(TAG, "PICK_FROM_ALBUM photoUri : " + filePath);

            Cursor cursor = null;

            try {

                /*
                 *  Uri 스키마를
                 *  content:/// 에서 file:/// 로  변경한다.
                 */
                String[] proj = {MediaStore.Images.Media.DATA};

                assert filePath != null;
                cursor = getContentResolver().query(filePath, proj, null, null, null);

                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                cursor.moveToFirst();

                tempFile = new File(cursor.getString(column_index));

                Log.d(TAG, "tempFile Uri : " + Uri.fromFile(tempFile));

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            setImage();

        } else if (requestCode == PICK_FROM_CAMERA) {

            setImage();

        }
    }

    private void uploadImage() {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(AddProduct.this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("products/" + UUID.randomUUID().toString());
            imagePath = ref.getPath();
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(AddProduct.this, "Uploaded", Toast.LENGTH_SHORT).show();

                            tempFile = null;
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddProduct.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");

                            tempFile = null;
                        }
                    });
        }
    }
    private void goToAlbum() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    private void setImage() {

//        ImageView imageView = findViewById(R.id.dp_image);

        imageView.setVisibility(View.VISIBLE);
        item_choose_btn.setVisibility(View.GONE);
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        Log.d(TAG, "setImage : " + tempFile.getAbsolutePath());

        imageView.setImageBitmap(originalBm);


    }

    private void takePhoto() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            tempFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            finish();
            e.printStackTrace();
        }
        if (tempFile != null) {
//            String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
//            filePath = FileProvider.getUriForFile(this, "com.bignerdranch.android.test.fileprovider",
//                    new File(Environment.getExternalStorageDirectory(), url));
            filePath = FileProvider.getUriForFile(this, "com.test.android.test.fileprovider", tempFile);
//            Uri photoUri = Uri.fromFile(tempFile);
//            Uri photoUri = Uri.fromFile(tempFile);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, filePath);
            startActivityForResult(intent, PICK_FROM_CAMERA);
        }
    }

    private File createImageFile() throws IOException {

        // 이미지 파일 이름 ( blackJin_{시간}_ )
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "blackJin_" + timeStamp + "_";

        // 이미지가 저장될 폴더 이름 ( blackJin )
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/blackJin/");
        if (!storageDir.exists()) storageDir.mkdirs();

        // 빈 파일 생성
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        return image;
    }
}
