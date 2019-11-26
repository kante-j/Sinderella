package com.jipjung.hucomin.sinderella;


import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import static android.animation.Animator.*;

public class user_information_check_width_hover extends AppCompatActivity {
    TextView nike_airforce_hover;
    AnimatorSet animatorSet;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_information);

        nike_airforce_hover=(TextView)findViewById(R.id.nike_airforce_hover);
        animatorSet=new AnimatorSet();

        ObjectAnimator fadeout = ObjectAnimator.ofFloat(nike_airforce_hover,"alpha",0.5f,0.1f);
        fadeout.setDuration(500);

        ObjectAnimator fadein=ObjectAnimator.ofFloat(nike_airforce_hover,"alpha", 0.1f, 0.5f);
        fadein.setDuration(500);

        animatorSet.play(fadein).after(fadeout);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation){
                super.onAnimationEnd(animation);
                animatorSet.start();
            }
        });

        animatorSet.start();
    }

}
