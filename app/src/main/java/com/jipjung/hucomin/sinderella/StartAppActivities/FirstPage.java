package com.jipjung.hucomin.sinderella.StartAppActivities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jipjung.hucomin.sinderella.HomeActivities.HomeFeed;
import com.jipjung.hucomin.sinderella.R;

import androidx.appcompat.app.AppCompatActivity;

public class FirstPage extends AppCompatActivity {

    public static String TAG="FirstPage";

    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;
    private Button logoutbtn;
    private Button gohome;
    private FirebaseAuth user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        logoutbtn = findViewById(R.id.log_out_btn);
        gohome = findViewById(R.id.gohome);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        gohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(FirstPage.this, HomeFeed.class);
                startActivity(intent);

            }
        });



        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FirstPage.this);
                    builder.setTitle("로그아웃");
                    builder.setMessage("로그아웃 하시겠습니까?");
                    builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            user = FirebaseAuth.getInstance();
                            user.signOut();
                            Intent intent = new Intent(FirstPage.this, SplashScreen.class);
                            finishAffinity();
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
                    builder.show();
            }
        });
    }
}
