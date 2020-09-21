package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Interpolator;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class yuval extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me_activity);


        text1();


        // TextView text1,text2,text3,text4,text5,text6,text7,text8,text9,text10;

    }

public void text1(){


       TextView text1 = (TextView)findViewById(R.id.text1);
     Animation animation1 = AnimationUtils.loadAnimation(this,R.anim.fade);


    TextView text2 = (TextView)findViewById(R.id.text2);
      Animation animation2 = AnimationUtils.loadAnimation(this,R.anim.fade);


    TextView text3 = (TextView)findViewById(R.id.text3);
     Animation animation3 = AnimationUtils.loadAnimation(this,R.anim.fade);

    TextView text4 = (TextView)findViewById(R.id.text4);
    Animation animation4 = AnimationUtils.loadAnimation(this,R.anim.fade);

    TextView text5 = (TextView)findViewById(R.id.text5);
    Animation animation5 = AnimationUtils.loadAnimation(this,R.anim.fade);

    TextView text6 = (TextView)findViewById(R.id.text6);
    Animation animation6 = AnimationUtils.loadAnimation(this,R.anim.fade);

    TextView text7 = (TextView)findViewById(R.id.text7);
    Animation animation7 = AnimationUtils.loadAnimation(this,R.anim.fade);

    TextView text8 = (TextView)findViewById(R.id.text8);
    Animation animation8 = AnimationUtils.loadAnimation(this,R.anim.fade);

    TextView text9 = (TextView)findViewById(R.id.text9);
    Animation animation9 = AnimationUtils.loadAnimation(this,R.anim.fade);




    text1.startAnimation(animation1);

     text2.startAnimation(animation2);

     text3.startAnimation(animation3);

    text4.startAnimation(animation4);

    text5.startAnimation(animation5);

    text6.startAnimation(animation6);

    text7.startAnimation(animation7);

    text8.startAnimation(animation8);

    text9.startAnimation(animation9);
    }


}
