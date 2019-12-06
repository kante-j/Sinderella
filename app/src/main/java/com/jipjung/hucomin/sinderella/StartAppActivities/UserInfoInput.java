package com.jipjung.hucomin.sinderella.StartAppActivities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.TestLooperManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class UserInfoInput extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;
    private TextView textview_nickname;
    private Button submit_btn;
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
    private LinearLayout check_foot_width;
    private RadioButton unkown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_information);

        textview_nickname = findViewById(R.id.info_nickname);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        sex_group = findViewById(R.id.sex_group);
        submit_btn = (Button) findViewById(R.id.information_button);
        birthbtn = findViewById(R.id.birth_date);
        spinner_foot_size = findViewById(R.id.foot_size);
        radio_foot_width = findViewById(R.id.foot_width_group);

        check_foot_width = findViewById(R.id.check_foot_width);
//<<<<<<< HEAD
//        TextView nike_airforce_hover;
//        AnimatorSet animatorSet;
//
//        nike_airforce_hover=(TextView)findViewById(R.id.nike_airforce_hover);
//        animatorSet=new AnimatorSet();
//=======
//>>>>>>> a5a78ad0971a49133ad73fe39ccb61e5b83fe2ce

        FrameLayout hover_nike_airforce = findViewById(R.id.nike_airforce);
        FrameLayout hover_reebok = findViewById(R.id.reebok_fury);
        FrameLayout hover_newbalance = findViewById(R.id.newbalcnce_574);
        FrameLayout hover_converse = findViewById(R.id.converse_converse);
        FrameLayout hover_vans = findViewById(R.id.vans_oidschool);
        FrameLayout hover_adidas = findViewById(R.id.adidas_superstar);
        FrameLayout hover_skechers = findViewById(R.id.skechers_dlites);
        FrameLayout hover_fila = findViewById(R.id.fila_courtdeluxe);

        TextView nike_airforce_text = findViewById(R.id.nike_airforce_hover);
        TextView reebok_text = findViewById(R.id.reebok_fury_hover);
        TextView newbalance_text = findViewById(R.id.newbalance_574_hover);
        TextView converse_text = findViewById(R.id.converse_converse_hover);
        TextView vans_text = findViewById(R.id.vans_oldschool_hover);
        TextView adidas_text = findViewById(R.id.adidas_superstar_hover);
        TextView skechers_text = findViewById(R.id.skechers_dlites_hover);
        TextView fila_text = findViewById(R.id.fila_courtdeluxe_hover);

        hover_nike_airforce.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if (nike_airforce_text.getVisibility() == view.INVISIBLE) {
                            nike_airforce_text.setVisibility(View.VISIBLE);
                        } else {
                            nike_airforce_text.setVisibility(view.INVISIBLE);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        nike_airforce_text.setVisibility(View.INVISIBLE);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        nike_airforce_text.setVisibility(View.INVISIBLE);
                        break;
                }
                return false;
            }
        });

        hover_reebok.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if (reebok_text.getVisibility() == view.INVISIBLE) {
                            reebok_text.setVisibility(View.VISIBLE);
                        } else {
                            reebok_text.setVisibility(view.INVISIBLE);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        reebok_text.setVisibility(View.INVISIBLE);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        reebok_text.setVisibility(View.INVISIBLE);
                        break;
                }
                return false;
            }
        });

        hover_newbalance.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if (newbalance_text.getVisibility() == view.INVISIBLE) {
                            newbalance_text.setVisibility(View.VISIBLE);
                        } else {
                            newbalance_text.setVisibility(view.INVISIBLE);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        newbalance_text.setVisibility(View.INVISIBLE);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        newbalance_text.setVisibility(View.INVISIBLE);
                        break;
                }
                return false;
            }
        });

        hover_converse.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if (converse_text.getVisibility() == view.INVISIBLE) {
                            converse_text.setVisibility(View.VISIBLE);
                        } else {
                            converse_text.setVisibility(view.INVISIBLE);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        converse_text.setVisibility(View.INVISIBLE);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        converse_text.setVisibility(View.INVISIBLE);
                        break;
                }
                return false;
            }
        });

        hover_vans.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if (vans_text.getVisibility() == view.INVISIBLE) {
                            vans_text.setVisibility(View.VISIBLE);
                        } else {
                            vans_text.setVisibility(view.INVISIBLE);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        vans_text.setVisibility(View.INVISIBLE);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        vans_text.setVisibility(View.INVISIBLE);
                        break;
                }
                return false;
            }
        });

        hover_adidas.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if (adidas_text.getVisibility() == view.INVISIBLE) {
                            adidas_text.setVisibility(View.VISIBLE);
                        } else {
                            adidas_text.setVisibility(view.INVISIBLE);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        adidas_text.setVisibility(View.INVISIBLE);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        adidas_text.setVisibility(View.INVISIBLE);
                        break;
                }
                return false;
            }
        });

        hover_skechers.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if (skechers_text.getVisibility() == view.INVISIBLE) {
                            skechers_text.setVisibility(View.VISIBLE);
                        } else {
                            skechers_text.setVisibility(view.INVISIBLE);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        skechers_text.setVisibility(View.INVISIBLE);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        skechers_text.setVisibility(View.INVISIBLE);
                        break;
                }
                return false;
            }
        });

        hover_fila.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if (fila_text.getVisibility() == view.INVISIBLE) {
                            fila_text.setVisibility(View.VISIBLE);
                        } else {
                            fila_text.setVisibility(view.INVISIBLE);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        fila_text.setVisibility(View.INVISIBLE);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        fila_text.setVisibility(View.INVISIBLE);
                        break;
                }
                return false;
            }
        });


        radio_foot_width.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
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
                if (!validateForm()) {
                    return;
                }

                WriteBatch batch = firebaseFirestore.batch();
                DocumentReference users = firebaseFirestore.collection("users").document(firebaseUser.getUid());
                Map<String, Object> docData = new HashMap<>();
                docData.put("nickname", textview_nickname.getText().toString());
                docData.put("user_id", firebaseUser.getUid());
                docData.put("sex", sex);
                docData.put("birth_date", birth_date);
                docData.put("foot_size", foot_size);
                docData.put("foot_width", foot_width);
                docData.put("age", 2019 - Integer.valueOf(year_to_age) + 1);

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
                switch (checkedId) {
                    case R.id.man:
                        sex = "male";
                        break;
                    case R.id.woman:
                        sex = "female";
                        break;
                }
            }
        });

        callbackMethod = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                Log.d("qweqwe",String.valueOf(monthOfYear));
                String moy;
                monthOfYear = monthOfYear + 1;
                year_to_age = String.valueOf(year);
//                Log.d("qweqwe",String.valueOf(monthOfYear/10));
                if (monthOfYear / 10 == 0) {
                    moy = "0" + String.valueOf(monthOfYear);
                } else if (monthOfYear == 10) {
                    moy = String.valueOf(monthOfYear);
                } else {
                    moy = String.valueOf(monthOfYear);
                }

                String doy;
//                Log.d("qweqwe",String.valueOf(dayOfMonth/10));
                if (dayOfMonth / 10 == 0) {
                    doy = "0" + String.valueOf(dayOfMonth);
                } else {
                    doy = String.valueOf(dayOfMonth);
                }
                birth_date = String.valueOf(year) + moy + doy;
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
                size = size.substring(0, 3);
                foot_size = Integer.valueOf(size);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //TODO: 잘모르겠다 누를시 check_foot_width_layout visible
        unkown = findViewById(R.id.unknown);

        unkown.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (unkown.isChecked()) {
                    if (check_foot_width.getVisibility() == View.GONE) {
                        check_foot_width.setVisibility(View.VISIBLE);
                        radio_foot_width.setVisibility(View.GONE);
                    } else {
                        check_foot_width.setVisibility(View.GONE);
                        radio_foot_width.setVisibility(View.VISIBLE);
                        unkown.setChecked(false);
                    }
                }
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

        // TODO : 안들어간 값들 전부 에러처리

        nickname = textview_nickname.getText().toString();
        if (TextUtils.isEmpty(nickname)) {
            textview_nickname.setError("Required.");
            valid = false;
        } else {
            textview_nickname.setError(null);
        }

        if (radio_foot_width.getCheckedRadioButtonId() == -1) {
            valid = false;
        }
        if (sex_group.getCheckedRadioButtonId() == -1) {
            Log.d("qweqwe", "asdasd");
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
