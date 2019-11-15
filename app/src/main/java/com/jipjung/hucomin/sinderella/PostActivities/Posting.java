package com.jipjung.hucomin.sinderella.PostActivities;

import android.app.ProgressDialog;
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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapLabel;
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
//    private TextView text_reason;
//    private TextView text_inf;
//    private TextView text_product;
//    private TextView text_cost;
    private String foodFrom;
    private String uid;
    private String email;
    private String nickname;
    private FirebaseFirestore mFirestore;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DocumentReference users;
    private String imagePath;
    private Uri filePath;
    private BootstrapButton btnChoose;
    private ImageView imageView;
    private BootstrapLabel postingTitle;


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

        btnChoose = (BootstrapButton) findViewById(R.id.btnChoose);
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

//        text_reason = findViewById(R.id.text_reason);
//        text_product = findViewById(R.id.text_pname);
//        text_cost = findViewById(R.id.text_cost);
//        text_inf = findViewById(R.id.inf);

        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        users = mFirestore.collection("users").document(fbUser.getUid());
        postingTitle = findViewById(R.id.posting_title);
        changePostingTitle();
        getUserInfo(fbUser);
        postingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eatoutBody = null;
                String transBody;
                if(imageView.getDrawable()==null) {
//                    if (getIntent().getStringExtra("Category").equals("FEatout")) {
//                        if (foodFrom != null)
//                            eatoutBody = "음식 종류 : " + foodFrom + "\n\n추천 이유 : " + text_reason.getText() + "\n\n기타 정보 : " + text_inf.getText();
//                        writeNewPost(uid, nickname, text_title.getText().toString(), eatoutBody);
//                    } else if (getIntent().getStringExtra("Category").equals("FTrans")) {
//                        transBody = "판매품 이름 : " + text_product.getText() + "\n\n가격 : " + text_cost.getText() + "원\n\n기타 정보 : " + text_inf.getText();
//                        writeNewPost(uid, nickname, text_title.getText().toString(), transBody);
//                    }
//                    else
                        writeNewPost(uid, nickname, text_title.getText().toString(), text_context.getText().toString());
                    if(validatePost())
                        finish();
                }else{
                    if(validatePost())
                        uploadImage();
//                    if(getIntent().getStringExtra("Category").equals("FEatout")){
//                        if(foodFrom != null)
//                            eatoutBody = "음식 종류 : "+foodFrom+"\n\n추천 이유 : "+text_reason.getText()+"\n\n기타 정보 : "+text_inf.getText();
//                        writeNewPost(uid, nickname, text_title.getText().toString(), eatoutBody);
//                    }
//                    else if(getIntent().getStringExtra("Category").equals("FTrans")){
//                        transBody = "판매품 이름 : "+text_product.getText()+"\n\n가격 : "+text_cost.getText()+"원\n\n기타 정보 : "+text_inf.getText();
//                        writeNewPost(uid, nickname, text_title.getText().toString(), transBody);
//                    }
//                    else
                        writeNewPost(uid, nickname, text_title.getText().toString(), text_context.getText().toString());
                }
            }
        });

        ImageButton xButton = findViewById(R.id.xButtoninPosting);
        xButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



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
            docData.put("category",getIntent().getStringExtra("Category"));

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

    public void changePostingTitle(){
        String category = getIntent().getStringExtra("Category");
        if(category.equals("FCook")){
            postingTitle.setText("꿀 레시피 게시글 작성하기");
        }else if(category.equals("FActivities")){
            postingTitle.setText("교내/대외 활동 게시글 작성하기");
        }else if(category.equals("FEatout")){
            postingTitle.setText("음식점 추천 게시글 작성하기");
        }else if(category.equals("FRoom")){
            postingTitle.setText("방값 정보 게시글 작성하기");
        }else if(category.equals("FTips")){
            postingTitle.setText("생활 꿀팁 게시글 작성하기");
        }else if(category.equals("FTrans")){
            postingTitle.setText("중고품 거래 게시글 작성하기");
        }else if(category.equals("FChat")){
            postingTitle.setText("자유 게시판 게시글 작성하기");
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

    public void onRadioButtonClicked(View v){
        foodFrom = (String)((RadioButton) v).getText();
    }

    public boolean validatePost(){
        boolean valid = true;

        if(TextUtils.isEmpty(text_title.getText())){
            text_title.setError("제목을 입력하세요!");
            valid = false;
        } else
            text_title.setError(null);

//        if(getIntent().getStringExtra("Category").equals("FTrans")){
//            if(TextUtils.isEmpty(text_product.getText())){
//                text_product.setError("판매할 상품 이름을 입력하세요!");
//                valid = false;
//            } else
//                text_product.setError(null);
//            if(TextUtils.isEmpty(text_cost.getText())){
//                text_cost.setError("판매 가격을 입력하세요!");
//                valid = false;
//            } else
//                text_cost.setError(null);
//        }
        return valid;
    }
}
