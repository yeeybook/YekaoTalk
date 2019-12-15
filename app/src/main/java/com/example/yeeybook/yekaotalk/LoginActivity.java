package com.example.yeeybook.yekaotalk;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class LoginActivity extends AppCompatActivity {
    private Button btnLogin;
    private TextView tvSignup;
    private EditText etId, etPw;
    private TextInputLayout idLayout, pwLayout;
    private ProgressBar pbLogin;
    private FirebaseRemoteConfig FirebaseRemoteConfig;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener; //로그인이 됐는지 안됐는지 체크해주는 부분
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        String splash_background = FirebaseRemoteConfig.getString("splash_background");
        getWindow().setStatusBarColor(Color.parseColor(splash_background)); //상태바 색과 배경색 같게
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut(); //자동적으로 로그인 된 상태라서 로그아웃 시켜주는 코드 임시로 넣음

        etId = (EditText)findViewById(R.id.etId);
        etPw = (EditText)findViewById(R.id.etPw);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        tvSignup = (TextView)findViewById(R.id.tvSignup);
        idLayout = (TextInputLayout)findViewById(R.id.idLayout);
        pwLayout = (TextInputLayout)findViewById(R.id.pwLayout);
        pbLogin = (ProgressBar)findViewById(R.id.pbLogin);

        tvSignup.setPaintFlags(tvSignup.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG); //밑줄 표시

        etPw.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) pwLayout.setPasswordVisibilityToggleEnabled(true);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginEvent();
            }
        });

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(a);
            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() { //로그인 인터페이스 리스너(로그인 됐는지를 확인해주는 부분)
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){ //유저가 있을때(로그인)
                    Intent a = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(a);
                    finish();
                    pbLogin.setVisibility(View.GONE);
                } else{ //유저가 없을때(로그아웃)

                }
            }
        };
    }
    void loginEvent(){
        firebaseAuth.signInWithEmailAndPassword(etId.getText().toString(), etPw.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) { //로그인이 완료되면 이게 정상적으로 성공이 됐는지 안됐는지를 판단해줌(알려주기만함)
                pbLogin.setVisibility(View.VISIBLE);
                if(!task.isSuccessful()){ //로그인이 실패했을때만 작동
                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
}
