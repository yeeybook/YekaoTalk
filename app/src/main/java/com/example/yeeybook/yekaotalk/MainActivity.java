package com.example.yeeybook.yekaotalk;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.yeeybook.yekaotalk.fragment.PeopleFragment;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mainBottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainBottomNavigationView = (BottomNavigationView)findViewById(R.id.mainBottomNavigationView);

        getWindow().setStatusBarColor(Color.rgb(235,235,235)); //상태바 색과 배경색 같게
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); //상태바 글자색 변경 (true가 검정 false가 하얀색)

        getFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, new PeopleFragment()).commit();

        mainBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    //case R.id.action_people:

                }
                return true;
            }
        });
    }
}
