package com.jipjung.hucomin.sinderella.MyMenuActivities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jipjung.hucomin.sinderella.R;
import com.jipjung.hucomin.sinderella.Classes.User;
import com.jipjung.hucomin.sinderella.StartAppActivities.SplashScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MyMenu extends AppCompatActivity {

    private FirebaseUser fbUser;
    private FirebaseFirestore mFirestore;
    private TextView text_created_at;
    private EditText text_email;
    private EditText text_nickname;
    private TextView birth_date;
    private Spinner foot_size;
    private ImageView btn_profilemodify;
    private RadioGroup foot_width;

    private FirebaseStorage storage;
    private StorageReference storageReference;
    private User user;
    private FirebaseAuth fbAuth;
    private Button btn_password;
    private RelativeLayout password_modify_layout;
    private Button profilemodify_check;
    private Button action_bar_back_close;
    private Button profilemodify_picture_edit_btn;
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;
    private String imagePath;
    private ImageView profilemodify_Image;
    private ImageView profilemodify_picture;

    private RadioButton profilemodify_small_foot;
    private RadioButton profilemodify_normal_foot;
    private RadioButton profilemodify_bigger_foot;
    private RadioGroup profilemodify_foot_width_group;
    private EditText profilemodify_password;
    private Spinner profilemodify_foot_size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profilemodify);
//        //get User Info
//        FirebaseFirestore.setLoggingEnabled(true);
        mFirestore = FirebaseFirestore.getInstance();
////        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
////                .setTimestampsInSnapshotsEnabled(true)
////                .build();
////        fs.setFirestoreSettings(settings);
//
//
        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://sinderella-d45a8.appspot.com");

        profilemodify_small_foot=findViewById(R.id.profilemodify_small_foot);
        profilemodify_normal_foot = findViewById(R.id.profilemodify_normal_foot);
        profilemodify_bigger_foot = findViewById(R.id.profilemodify_bigger_foot);
        user = (User)getIntent().getSerializableExtra("user");

//        //Find Text View by Id
        text_email = findViewById(R.id.profilemodify_email);
//        text_created_at = findViewById(R.id.mymenu_created_at);
        text_nickname = findViewById(R.id.profilemodify_nicname);
        birth_date = findViewById(R.id.year_month_day);

        btn_profilemodify = findViewById(R.id.btn_profilemodify);
        btn_profilemodify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Spinner set

        foot_size = findViewById(R.id.profilemodify_foot_size);
        String compareValue = String.valueOf(user.getFoot_size());
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.foot_size, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        foot_size.setAdapter(adapter);
        if (compareValue != null) {
            int spinnerPosition = adapter.getPosition(compareValue);
            foot_size.setSelection(spinnerPosition);
        }

        if(user.getFoot_width().equals("small")){
            profilemodify_small_foot.setChecked(true);
        }else if(user.getFoot_width().equals("normal")){
            profilemodify_normal_foot.setChecked(true);
        }else{
            profilemodify_bigger_foot.setChecked(true);
        }


        text_nickname.setText(user.getNickname());
        text_email.setText(fbUser.getEmail());
        birth_date.setText(user.getBirth_date());

        //MoHyeonMin


        btn_password = findViewById(R.id.btn_passward_profilemodify);
        password_modify_layout = findViewById(R.id.password_modify_layout);
        profilemodify_check = findViewById(R.id.profilemodify_check);
        action_bar_back_close = findViewById(R.id.action_bar_back_close);

        //click시 비밀번호 창 생성했다가 사라지기
        btn_password.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(btn_password.getVisibility() == View.VISIBLE){
                    btn_password.setVisibility((View.GONE));
                    password_modify_layout.setVisibility(View.VISIBLE);
                }
            }


        });



        profilemodify_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickHandler();

                if(profilemodify_check.isPressed()){
                    password_modify_layout.setVisibility(View.GONE);
                    btn_password.setVisibility(View.VISIBLE);
                    fbUser.updatePassword(profilemodify_password.getText().toString());
                }
            }
        });

        //뒤로 가기 버튼
        action_bar_back_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //TODO: 프로필 사진 변경
        profilemodify_picture= findViewById(R.id.profilemodify_picture);
        profilemodify_Image = findViewById(R.id.profilemodify_Image);
        profilemodify_picture_edit_btn = findViewById(R.id.profilemodify_picture_edit_btn);

        profilemodify_picture_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
//        StorageReference path = storageReference.child(user.getUser_id());
//        Log.d("qweqwe",path.toString());
//        if(path !=null){
//            profilemodify_picture.setVisibility(View.GONE);
//            profilemodify_Image.setVisibility(View.VISIBLE);
//            Glide.with(this).load(path).skipMemoryCache(true).into(profilemodify_Image);
//        }
        if(user.getProfile_url() !=null){
            profilemodify_picture.setVisibility(View.GONE);
            profilemodify_Image.setVisibility(View.VISIBLE);
            StorageReference path = storageReference.child(user.getProfile_url());
            Glide.with(this).load(path).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(profilemodify_Image);
        }

        //TODO: mymenu 바뀌기

        profilemodify_foot_width_group = findViewById(R.id.profilemodify_foot_width_group);
        profilemodify_password = findViewById(R.id.profilemodify_password);


        profilemodify_foot_width_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.profilemodify_small_foot:
                        mFirestore.collection("users").document(user.getUser_id()).update("foot_width", "small");
                        break;
                    case R.id.profilemodify_normal_foot:
                        mFirestore.collection("users").document(user.getUser_id()).update("foot_width", "normal");
                        break;
                    case R.id.profilemodify_bigger_foot:
                        mFirestore.collection("users").document(user.getUser_id()).update("foot_width", "big");
                        break;
                }
            }
        });

        foot_size.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("mypage_foot",foot_size.getSelectedItem().toString());
                mFirestore.collection("users").document(user.getUser_id()).update("foot_size", Integer.valueOf(foot_size.getSelectedItem().toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });














//        filterbtn =v.findViewById(R.id.btn_filter);
//        filterbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(applytext.getVisibility() == View.GONE){
//                    applytext.setVisibility(View.VISIBLE);
//                }else{
//                    applytext.setVisibility(View.GONE);
//                }
//
//                if(filterscreen.getVisibility() == View.GONE){
//                    filterscreen.setVisibility(View.VISIBLE);
//                }else{
//                    filterscreen.setVisibility(View.GONE);
//                }
//
//            }
//        });



//        text_foot_width.setText(user.getFoot_width());
//        text_foot_size.setText(user.getFoot_size());

//        String timeStamp = new SimpleDateFormat("yyyy년 MM월 dd일").format(fbUser.getMetadata().getCreationTimestamp());
//        text_email.setText(fbUser.getEmail().toString());
//        text_created_at.setText(timeStamp);
//
//
//        text_nickname.setText(getIntent().getStringExtra("nickname"));
//
//        //로그아웃
//        Button btn_logout = findViewById(R.id.button_logout);
//        btn_logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!MyMenu.this.isFinishing()){
//                    AlertDialog.Builder builder = new AlertDialog.Builder(MyMenu.this);
//                    builder.setTitle("로그아웃");
//                    builder.setMessage("로그아웃 하시겠습니까?");
//                    builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            fbAuth = FirebaseAuth.getInstance();
//                            fbAuth.signOut();
//                            Intent intent = new Intent(MyMenu.this, SplashScreen.class);
//                            finishAffinity();
////                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);
//                        }
//                    });
//                    builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            return;
//                        }
//                    });
//                    builder.show();
//                }
//            }
//        });
//
//        //내가 쓴 게시글 보기
//        Button goMyPosts = findViewById(R.id.my_posts);
//        goMyPosts.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MyMenu.this, MyPosts.class);
//                startActivity(intent);
//            }
//        });
//
//        //내가 좋아하는 게시글 보기
//        Button goLikingPosts = findViewById(R.id.go_liking_posts);
//        goLikingPosts.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MyMenu.this, LikingPosts.class);
//                intent.putExtra("nickname",text_nickname.getText().toString());
//                startActivity(intent);
//            }
//        });
    }

    public void OnClickHandler(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("비밀번호 수정").setMessage("수정되었습니다.");

        AlertDialog alertDialog =builder.create();

        alertDialog.show();
    }

    public void restartApp(Context context) {
    }

    //Image 변경
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
                profilemodify_Image.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            profilemodify_picture.setVisibility(View.INVISIBLE);
            profilemodify_Image.setVisibility(View.VISIBLE);

            uploadImage();
        }

    }


    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(MyMenu.this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("profiles/"+ user.getUser_id());
            imagePath = ref.getPath();
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(MyMenu.this, "업로드 완료", Toast.LENGTH_SHORT).show();
                            ;
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(MyMenu.this, "업로드 실패 "+e.getMessage(), Toast.LENGTH_SHORT).show();
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

//            WriteBatch batch = mFirestore.batch();
            mFirestore.collection("users").document(user.getUser_id())
                    .update("profile_url",imagePath).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
//            Map<String, Object> docData = new HashMap<>();
//            docData.put("profile_url", imagePath);
//            batch.set(profile, docData);
//            batch.commit();
        }
    }

//    private void getUser(){
//        fs.collection("users").whereEqualTo("user_id",fbUser.getUid()).get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                    }
//                })
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        if(queryDocumentSnapshots.isEmpty()){
//                            return;
//                        }else{
//                            user = queryDocumentSnapshots.toObjects(User.class).get(0);
//
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                    }
//                });
//
//    }
//    public void menuClick(View v){
//        PopupMenu popup = new PopupMenu(getApplicationContext(), v);
//        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
//        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch(item.getItemId()){
////                    case R.id.go_mymenu:
////                        Intent intent = new Intent(MyMenu.this, MyMenu.class);
////                        startActivity(intent);
////                        finish();
////                        break;
//                    case R.id.messages:
//                        Intent i = new Intent(MyMenu.this, MyMessages.class);
//                        startActivity(i);
//                        break;
//                }
//                return false;
//            }
//        });
//        popup.show();
//    }
}