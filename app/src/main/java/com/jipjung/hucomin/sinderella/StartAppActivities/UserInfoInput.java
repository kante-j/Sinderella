package com.jipjung.hucomin.sinderella.StartAppActivities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.jipjung.hucomin.sinderella.HomeActivities.HomeFeed;
import com.jipjung.hucomin.sinderella.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserInfoInput extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;
    private TextView textview_nickname;
    private ImageButton submit_btn;
    private Spinner spinner_foot_size;
    private RadioGroup sex_group;
    private RadioGroup radio_foot_width;
    private int foot_size;
    private String foot_width;
    private String birth_date;
    private String nickname;
    private String sex;
    private String year_to_age;

    private Button birthbtn;
    private DatePickerDialog.OnDateSetListener callbackMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_information);

        textview_nickname = findViewById(R.id.info_nickname);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        sex_group = findViewById(R.id.sex_group);
        submit_btn = (ImageButton)findViewById(R.id.information_button);
        birthbtn = findViewById(R.id.birth_date);
        spinner_foot_size = findViewById(R.id.foot_size);
        radio_foot_width = findViewById(R.id.foot_width_group);

        radio_foot_width.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.small_foot:
                        foot_width = "small";
                        break;
                    case R.id.normal_foot:
                        foot_width = "normal";
                        break;
                    case R.id.bigger_foot:
                        foot_width = "big";
                        break;
                }
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateForm()){
                    return;
                }

                WriteBatch batch = firebaseFirestore.batch();
                DocumentReference users = firebaseFirestore.collection("users").document(firebaseUser.getUid());
                Map<String, Object> docData = new HashMap<>();
                docData.put("nickname", textview_nickname.getText().toString());
                docData.put("user_id",firebaseUser.getUid());
                docData.put("sex", sex);
                docData.put("birth_date", birth_date);
                docData.put("foot_size", foot_size);
                docData.put("foot_width", foot_width);
                docData.put("age",2019-Integer.valueOf(year_to_age)+1);

//                SimpleDateFormat s = new SimpleDateFormat("yyyyMMddkkmmss");
//                String format = s.format(new Date());
////                Timestamp t = new Timestamp(new Date());
//
//                docData.put("created_at",format);

                batch.set(users, docData);
                batch.commit();

                Intent intent = new Intent(UserInfoInput.this, HomeFeed.class);
                startActivity(intent);


                finish();
            }
        });

        sex_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.man:
                        sex="male";
                        break;
                    case R.id.woman:
                        sex="female";
                        break;
                }
            }
        });

        callbackMethod = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
//                Log.d("qweqwe",String.valueOf(monthOfYear));
                String moy;
                monthOfYear = monthOfYear+1;
                year_to_age = String.valueOf(year);
//                Log.d("qweqwe",String.valueOf(monthOfYear/10));
                if(monthOfYear/10==0){
                    moy = "0"+String.valueOf(monthOfYear);
                }else if(monthOfYear == 10){
                    moy = String.valueOf(monthOfYear);
                }else{
                    moy = String.valueOf(monthOfYear);
                }

                String doy;
//                Log.d("qweqwe",String.valueOf(dayOfMonth/10));
                if(dayOfMonth/10 == 0){
                    doy = "0"+String.valueOf(dayOfMonth);
                }else{
                    doy = String.valueOf(dayOfMonth);
                }
                birth_date = String.valueOf(year)+moy+doy;
//                Log.d("qweqwe",birth_date);
//                textView_Date.setText(year + "년" + monthOfYear + "월" + dayOfMonth + "일");
            }
        };
        birthbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(UserInfoInput.this, callbackMethod, 2019, 5, 24);

                dialog.show();
            }
        });

        spinner_foot_size.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String size;
                size = parent.getItemAtPosition(position).toString();
                size = size.substring(0,3);
                foot_size = Integer.valueOf(size);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

    private boolean validateForm() {
        boolean valid = true;

        nickname = textview_nickname.getText().toString();
        if (TextUtils.isEmpty(nickname)) {
            textview_nickname.setError("Required.");
            valid = false;
        } else {
            textview_nickname.setError(null);
        }

        if(radio_foot_width.getCheckedRadioButtonId() == -1){
            valid = false;
        }
        if(sex_group.getCheckedRadioButtonId() == -1){
            Log.d("qweqwe","asdasd");
            valid = false;
        }
//        school = eschoolName.getText().toString();
//        if (TextUtils.isEmpty(school)) {
//            eschoolName.setError("Required.");
//            valid = false;
//        } else {
//            eschoolName.setError(null);
//        }

        return valid;
    }
}
