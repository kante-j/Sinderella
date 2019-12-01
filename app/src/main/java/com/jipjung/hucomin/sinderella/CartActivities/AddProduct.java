package com.jipjung.hucomin.sinderella.CartActivities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
                chooseImage();
            }
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
                    uploadImage();
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
        if(requestCode == 71 && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
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
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 71);
    }
}
