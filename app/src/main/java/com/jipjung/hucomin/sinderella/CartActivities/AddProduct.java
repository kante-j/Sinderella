package com.jipjung.hucomin.sinderella.CartActivities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.jipjung.hucomin.sinderella.PostActivities.Posting;
import com.jipjung.hucomin.sinderella.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
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

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_CAMERA = 2;

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

        action_bar_back_close= findViewById(R.id.action_bar_back_close);

        price = findViewById(R.id.price);
        foot_size_correction2 = findViewById(R.id.foot_size_correction2);
        imageView = (ImageView) findViewById(R.id.postImage);
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
                    DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            doTakePhotoAction();
                        }
                    };

                    DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            chooseImage();
                        }
                    };

                    DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    };

                    new AlertDialog.Builder(AddProduct.this)
                            .setTitle("업로드할 이미지 선택")
                            .setPositiveButton("사진촬영", cameraListener)
                            .setNeutralButton("앨범선택", albumListener)
                            .setNegativeButton("취소", cancelListener)
                            .show();
                }
//            }
        });
        item_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eatoutBody = null;
                String transBody;
//                writeNewPost(uid, nickname, text_title.getText().toString(), text_context.getText().toString());
//                if(imageView.getDrawable()==null) {
//                        writeNewPost(uid, nickname, text_title.getText().toString(), text_context.getText().toString());
//                    if(validatePost())
//                        finish();
//                }else{
                if(validatePost())
                    doTakePhotoAction();
                writeNewPost();
//                }
            }
        });

        action_bar_back_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    private void writeNewPost(){
        WriteBatch batch = mFirestore.batch();
        DocumentReference products = mFirestore.collection("products").document();
        if(validatePost()){
            Map<String, Object> docData = new HashMap<>();
            docData.put("id",products.getId());

            docData.put("brand",brand_name.getText().toString());
            docData.put("category",foot_size_correction2.getSelectedItem().toString());
            docData.put("name",model_name.getText().toString());
            docData.put("price",Integer.valueOf(price.getText().toString()));
            docData.put("product_url",product_url.getText().toString());

            if(imagePath!=null){
                docData.put("image_url",imagePath);
            }
            SimpleDateFormat s = new SimpleDateFormat("yyyyMMddkkmmss");
            String format = s.format(new Date());

            docData.put("created_at",format);
            batch.set(products, docData);
            batch.commit();
        }
    }
    public void shoeDialogNotImage(){
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
    public boolean validatePost(){
        boolean valid = true;
        if(filePath==null){
            shoeDialogNotImage();
            valid = false;
            return valid;
        }
        return valid;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == 71 && resultCode == RESULT_OK
//                && data != null && data.getData() != null )
//        {
//            filePath = data.getData();
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                imageView.setImageBitmap(bitmap);
//            }
//            catch (IOException e)
//            {
//                e.printStackTrace();
//            }
//            item_choose_btn.setVisibility(View.INVISIBLE);
//            imageView.setVisibility(View.VISIBLE);
//        }

        if(resultCode != RESULT_OK)
        {
            return;
        }

        switch(requestCode)
        {
            case CROP_FROM_CAMERA:
            {
                // 크롭이 된 이후의 이미지를 넘겨 받습니다.
                // 이미지뷰에 이미지를 보여준다거나 부가적인 작업 이후에
                // 임시 파일을 삭제합니다.
                final Bundle extras = data.getExtras();

                if(extras != null)
                {
                    Bitmap photo = extras.getParcelable("data");
                    item_choose_btn.setVisibility(View.INVISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageBitmap(photo);
                }

                // 임시 파일 삭제
                File f = new File(filePath.getPath());
                if(f.exists())
                {
                    f.delete();
                }

                break;
            }

            case PICK_FROM_ALBUM:
            {
                // 이후의 처리가 카메라와 같으므로 일단  break없이 진행합니다.
                // 실제 코드에서는 좀더 합리적인 방법을 선택하시기 바랍니다.

//                mImageCaptureUri = data.getData();
                filePath = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    imageView.setImageBitmap(bitmap);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                item_choose_btn.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.VISIBLE);
            }

            case PICK_FROM_CAMERA:
            {
                // 이미지를 가져온 이후의 리사이즈할 이미지 크기를 결정합니다.
                // 이후에 이미지 크롭 어플리케이션을 호출하게 됩니다.

                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(filePath, "image/*");

                intent.putExtra("outputX", 90);
                intent.putExtra("outputY", 90);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FROM_CAMERA);

                break;
            }
        }

    }
    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(AddProduct.this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("products/"+ UUID.randomUUID().toString());
            imagePath = ref.getPath();
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(AddProduct.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddProduct.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }
    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_PICK);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 71);
    }

    private void doTakePhotoAction()
    {
        /*
         * 참고 해볼곳
         * http://2009.hfoss.org/Tutorial:Camera_and_Gallery_Demo
         * http://stackoverflow.com/questions/1050297/how-to-get-the-url-of-the-captured-image
         * http://www.damonkohler.com/2009/02/android-recipes.html
         * http://www.firstclown.us/tag/android/
         */

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 임시로 사용할 파일의 경로를 생성
        String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
//        filePath = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
        filePath = FileProvider.getUriForFile(this, "com.bignerdranch.android.test.fileprovider",
                new File(Environment.getExternalStorageDirectory(), url));

        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, filePath);
//        // 특정기기에서 사진을 저장못하는 문제가 있어 다음을 주석처리 합니다.
//        //intent.putExtra("return-data", true);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }
}
