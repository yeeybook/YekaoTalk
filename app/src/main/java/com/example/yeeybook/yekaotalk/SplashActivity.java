package com.example.yeeybook.yekaotalk;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class SplashActivity extends AppCompatActivity {
    String TAG = "SplashActivity"; //TAG가 빨간 글씨 되는거 막으려고
    private LinearLayout splashLinearLayout;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        splashLinearLayout  = (LinearLayout)findViewById(R.id.splashLinearLayout);

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance(); //파이어베이스에서 원격으로 조정할 수 있음 ex)배경색, 에러메세지
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder().setDeveloperModeEnabled(BuildConfig.DEBUG).build(); //.setMinimumFetchIntervalInSeconds(3600)
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.default_config);

        mFirebaseRemoteConfig.fetch(0)//0초마다 요청
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mFirebaseRemoteConfig.activateFetched();
                        } else {
                        }
                        displayMessage();
                    }
                });
    }
    void displayMessage() { //서버 점검 중입니다 라는 메세지를 띄우고 확인을 누르면 앱이 종료되게 하려고
        String splash_background = mFirebaseRemoteConfig.getString("splash_background");
        boolean caps = mFirebaseRemoteConfig.getBoolean("splash_message_caps");
        String splash_message = mFirebaseRemoteConfig.getString("splash_message");
        splashLinearLayout.setBackgroundColor(Color.parseColor(splash_background));
        if(caps){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(splash_message).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.create().show();
        }else{
            Intent a = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(a);
            finish();
        }
    }
}
