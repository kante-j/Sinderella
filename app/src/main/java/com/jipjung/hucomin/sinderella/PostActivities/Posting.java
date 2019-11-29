package com.jipjung.hucomin.sinderella.PostActivities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jipjung.hucomin.sinderella.Classes.User;
import com.jipjung.hucomin.sinderella.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Posting extends AppCompatActivity {

    private final int PICK_IMAGE_REQUEST = 71;
    private static final int RC_TAKE_PICTURE = 101;
    private FirebaseUser fbUser;
    private Button postingButton;
    private TextView text_title;
    private TextView text_context;
    private String uid;
    private String email;
    private String nickname;
    private FirebaseFirestore mFirestore;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DocumentReference users;
    private String imagePath;
    private Uri filePath;
    private ImageButton btnChoose;
    private ImageView imageView;


    //Rating bar
    private RatingBar ratingBarForShoes;
    private float rating;
    // RadioButton
    private RadioGroup shoes_weight_radiogroup;
    private RadioGroup shoe_size_radiogroup;
    private RadioGroup vantilation_radiogroup;
    private RadioGroup waterproof_radiogroup;

    private String buyURLString;
    private int price;

    private String shoes_weight;
    private String shoe_size;
    private String vantilation;
    private String waterproof;

    private Spinner categorySpinner;
    private Spinner shoesSizeSpinner;


    private TextView buyURL;

    private TextView priceTextView;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if(getIntent().getStringExtra("Category").equals("FEatout"))
//            setContentView(R.layout.activity_posting_eatout);
//        else if(getIntent().getStringExtra("Category").equals("FTrans"))
//            setContentView(R.layout.activity_posting_transaction);
//        else
        setContentView(R.layout.activity_posting);
        mFirestore = FirebaseFirestore.getInstance();

        //For storage
        storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        storageReference = storage.getReference();

        btnChoose = (ImageButton) findViewById(R.id.btnChoose);
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        imageView = (ImageView) findViewById(R.id.postImage);

        postingButton = findViewById(R.id.btn_posting);
        text_context = findViewById(R.id.text_context);
        text_title = findViewById(R.id.text_title);
        ratingBarForShoes = findViewById(R.id.ratingForShoes);
        shoes_weight_radiogroup = findViewById(R.id.shoes_weight);
        ImageButton xButton = findViewById(R.id.xButtoninPosting);
        shoesSizeSpinner = findViewById(R.id.foot_size_correction);
        categorySpinner = findViewById(R.id.foot_size_correction2);
        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        users = mFirestore.collection("users").document(fbUser.getUid());
//        postingTitle = findViewById(R.id.posting_title);
//        changePostingTitle();
        getUserInfo(fbUser);
        postingButton.setOnClickListener(new View.OnClickListener() {
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
                        writeNewPost(uid, nickname, text_title.getText().toString(), text_context.getText().toString());
//                }
            }
        });

        xButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        shoes_weight_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.shoes_weight_fit:
                        shoes_weight = "fit";
                        break;
                    case R.id.shoes_weight_heavy:
                        shoes_weight = "heavy";
                        break;
                    case R.id.shoes_weight_light:
                        shoes_weight = "light";
                            break;
                }

            }
        });

        shoe_size_radiogroup = findViewById(R.id.shoes_size);
        shoe_size_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId){
                    case R.id.shoes_size_big:
                        shoe_size = "big";
                        break;
                    case R.id.shoes_size_fit:
                        shoe_size = "fit";
                        break;
                    case R.id.shoes_size_small:
                        shoe_size = "small";
                }

            }
        });

        vantilation_radiogroup = findViewById(R.id.ventilation);
        vantilation_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.ventilation_good:
                        vantilation = "good";
                        break;
                    case R.id.ventilation_best:
                        vantilation = "best";
                        break;
                    case R.id.ventilation_bad:
                        vantilation = "bad";
                        break;
                }
            }
        });

        waterproof_radiogroup = findViewById(R.id.waterproof);
        waterproof_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.waterproof_bad:
                        waterproof = "bad";
                        break;
                    case R.id.waterproof_best:
                        waterproof= "best";
                        break;
                    case R.id.waterproof_good:
                        waterproof="good";
                        break;
                }
            }
        });

        buyURL = findViewById(R.id.buyURL);

        priceTextView = findViewById(R.id.priceTextView);




    }

    private void getUserInfo(FirebaseUser user){
        uid = user.getUid();
//        nickname = user.getDisplayName();
        email = user.getEmail();
        mFirestore.collection("users").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                nickname = user.getNickname();
            }
        });
    }

    private void writeNewPost(String userId, String username, String title, String body) {
        rating = ratingBarForShoes.getRating();
        buyURLString = buyURL.getText().toString();
        price = Integer.valueOf(priceTextView.getText().toString());
        int shoe_size_num = Integer.valueOf(shoesSizeSpinner.getSelectedItem().toString().substring(0,3));
        String category = categorySpinner.getSelectedItem().toString();
        WriteBatch batch = mFirestore.batch();
        DocumentReference posts = mFirestore.collection("posts").document();
        if(validatePost()){
            Map<String, Object> docData = new HashMap<>();
            docData.put("id",posts.getId());
            docData.put("user_id", userId);
            docData.put("user_name", username);
            docData.put("email", email);
            docData.put("title",title);
            docData.put("nickname",nickname);
            docData.put("body",body);
            docData.put("shoe_size_num",shoe_size_num);
            docData.put("waterproof",waterproof);
            docData.put("ventilation",vantilation);
            docData.put("shoes_size",shoe_size);
            docData.put("shoes_weight",shoes_weight);
            docData.put("rating",rating);
            docData.put("price",price);
            docData.put("buyURL",buyURLString);
            docData.put("category",category);

            if(imagePath!=null){
                docData.put("image_url",imagePath);
            }
            SimpleDateFormat s = new SimpleDateFormat("yyyyMMddkkmmss");
            String format = s.format(new Date());

            docData.put("created_at",format);
            batch.set(posts, docData);
            batch.commit();
        }
    }

    private void launchCamera() {
        Log.d("CAMERA", "launchCamera");

        // Pick an image from storage
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        startActivityForResult(intent, RC_TAKE_PICTURE);
    }
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
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
            btnChoose.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.VISIBLE);
        }

    }

    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(Posting.this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            imagePath = ref.getPath();
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(Posting.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Posting.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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

//    public void onRadioButtonClicked(View v){
//        foodFrom = (String)((RadioButton) v).getText();
//    }

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
    public void shoesWeightNotSelected(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("신발 무게를 입력해 주세요!");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        builder.show();
    }
    public void waterproofNotSelected(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("방수 정도를 입력해 주세요!");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        builder.show();
    }
    public void ventilationNotSelected(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("통기성 정도를 선택해 주세요!");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        builder.show();
    }
    public void shoeSizenotSelected(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("신발 사이즈를 선택해 주세요!");
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

        if(TextUtils.isEmpty(text_title.getText())){
            text_title.setError("제목을 입력하세요!");
            valid = false;
        } else
            text_title.setError(null);

        Log.d("qweqwe 1",String.valueOf(valid));
        if(TextUtils.isEmpty(priceTextView.getText())){
            priceTextView.setError("가격을 입력하세요!");
            valid = false;
        }else
            text_title.setError(null);
        if(TextUtils.isEmpty(buyURL.getText())){
            buyURL.setError("구입처를 입력하세요!");
            valid = false;
        }else
            text_title.setError(null);


        Log.d("qweqwe 2",String.valueOf(valid));
        if(filePath==null){
            shoeDialogNotImage();
            valid = false;
            return valid;
        }
        Log.d("qweqwe 3",String.valueOf(valid));
//        if(!shoes_weight_radiogroup.isActivated()){
//            shoesWeightNotSelected();
//            valid = false;
//            return valid;
//        }
//        if(!waterproof_radiogroup.isSelected()){
//            waterproofNotSelected();
//            valid = false;
//            return valid;
//        }
//        if(!vantilation_radiogroup.isSelected()){
//            ventilationNotSelected();
//            valid = false;
//            return valid;
//        }
//        if(!shoe_size_radiogroup.isSelected()){
//            shoeSizenotSelected();
//            valid = false;
//            return valid;
//        }
        return valid;
    }
}
