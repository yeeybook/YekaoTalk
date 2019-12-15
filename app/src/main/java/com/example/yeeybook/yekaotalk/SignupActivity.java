package com.example.yeeybook.yekaotalk;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yeeybook.yekaotalk.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    private static final int PICK_FROM_ALBUM = 10;
    private EditText etName, etId, etPw, etPwChk;
    private Button btnSignup;
    private TextInputLayout idLayout, pwLayout, pwChkLayout;
    private ImageView imgProfile, imgChk;
    private Uri imageUri;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etName = (EditText)findViewById(R.id.etName);
        etId = (EditText)findViewById(R.id.etId);
        etPw = (EditText)findViewById(R.id.etPw);
        etPwChk = (EditText)findViewById(R.id.etPwChk);
        btnSignup = (Button)findViewById(R.id.btnSignup);
        idLayout = (TextInputLayout)findViewById(R.id.idLayout);
        pwLayout = (TextInputLayout)findViewById(R.id.pwLayout);
        pwChkLayout = (TextInputLayout)findViewById(R.id.pwChkLayout);
        imgProfile = (ImageView)findViewById(R.id.imgProfile);
        imgChk = (ImageView)findViewById(R.id.imgChk);

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        String splash_background = mFirebaseRemoteConfig.getString("splash_background");
        getWindow().setStatusBarColor(Color.parseColor(splash_background)); //상태바 색과 배경색 같게

//        imgProfile.setBackground(new ShapeDrawable(new OvalShape()));
//        imgProfile.setClipToOutline(true);
        imgProfile.setOnClickListener(new View.OnClickListener() {//사진을 누르면 앨범이 연결되게
            @Override
            public void onClick(View v) {
                Intent a = new Intent(Intent.ACTION_PICK);
                a.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(a,PICK_FROM_ALBUM);//여기에 모든 이벤트가 모여야 이 request code값(10)으로 내가 원하는 이벤트로 이동
            }
        });

        etPw.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    Pattern p = Pattern.compile("^[a-zA-Z0-9]+@[a-zA-Z0-9]+.[a-zA-Z]+$"); //이메일 형식
                    Matcher m = p.matcher((etId).getText().toString());
                    pwLayout.setPasswordVisibilityToggleEnabled(true);
                    pwLayout.setCounterEnabled(true);
                    pwLayout.setCounterMaxLength(20);
                    if(!m.matches()) {
                        etId.setFocusableInTouchMode(true);
                        etId.requestFocus();
                        Toast.makeText(SignupActivity.this, "Email형식으로 입력하세요", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        etPw.setFocusableInTouchMode(true);
                        etPw.requestFocus();
                    }
                }
            }
        });

        etPwChk.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    pwChkLayout.setPasswordVisibilityToggleEnabled(true);
                    pwChkLayout.setCounterEnabled(true);
                    pwChkLayout.setCounterMaxLength(20);
                    if(etPw.getText().length()<6){
                        etPw.setFocusableInTouchMode(true);
                        etPw.requestFocus();
                        Toast.makeText(SignupActivity.this, "비밀번호를 6자리 이상으로 입력하세요", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        etPwChk.addTextChangedListener(new TextWatcher() { //비밀번호가 같은 값인지 확인하기 위해
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(etPw.getText().toString().equals(etPwChk.getText().toString())){
                    imgChk.setImageResource(R.drawable.checked);
                } else{
                    imgChk.setImageResource(R.drawable.unchecked);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etName.getText().toString().equals("")) {
                    Toast.makeText(SignupActivity.this, "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                    etName.setFocusableInTouchMode(true);
                    etName.requestFocus();
                }else if(etId.getText().toString().equals("")) {
                    Toast.makeText(SignupActivity.this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
                    etId.setFocusableInTouchMode(true);
                    etId.requestFocus();
                }else if(etPw.getText().toString().equals("")) {
                    Toast.makeText(SignupActivity.this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                    etPw.setFocusableInTouchMode(true);
                    etPw.requestFocus();
                }else if(etPwChk.getText().toString().equals("")){
                    Toast.makeText(SignupActivity.this, "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                    etPwChk.setFocusableInTouchMode(true);
                    etPwChk.requestFocus();
                }
                else{
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(etId.getText().toString(), etPwChk.getText().toString())
                            .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    final String uid = task.getResult().getUser().getUid();
                                    if(imageUri == null){ //이미지를 변경하지 않았을 경우 기본 이미지로 넘기기 위해
                                        imageUri = Uri.parse("android.resource://"+getPackageName()+"/"+R.drawable.default_profile);
                                    }
                                        FirebaseStorage.getInstance().getReference().child("userImages").child(uid).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) { //이미지 업로드 성공했는지 안했는지 판단
                                                String imageUrl = task.getResult().getDownloadUrl().toString();

                                                UserModel userModel= new UserModel();
                                                userModel.userName = etName.getText().toString();
                                                userModel.profileImageUrl = imageUrl;

                                                FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(userModel)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                SignupActivity.this.finish();
                                                            }
                                                        });
                                            }
                                        });

                                    Toast.makeText(SignupActivity.this,"가입 완료되었습니다", Toast.LENGTH_SHORT).show();
                                    Intent a = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(a);
                                }
                            });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK){
            imgProfile.setImageURI(data.getData()); //가운데 뷰를 바꿈
            imageUri = data.getData(); //이미지 원본 경로 저장
        }
    }
}
