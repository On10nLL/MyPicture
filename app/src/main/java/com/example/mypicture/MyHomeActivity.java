package com.example.mypicture;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MyHomeActivity extends AppCompatActivity {
    private RelativeLayout relativeLayout_collect;
    private RelativeLayout relativeLayout_like;
    private RelativeLayout relativeLayout_leave;
    private TextView relativeLayout_text_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_home_page);
        relativeLayout_collect = (RelativeLayout)findViewById(R.id.re_collect);
        relativeLayout_like = (RelativeLayout)findViewById(R.id.myfavorite);
        relativeLayout_leave = (RelativeLayout)findViewById(R.id.re_exit);
        relativeLayout_text_name = findViewById(R.id.text_name);

        relativeLayout_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyHomeActivity.this,HomeLikeActivity.class));
            }
        });
        relativeLayout_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyHomeActivity.this,HomeCollectActivity.class));
            }
        });
        relativeLayout_leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyHomeActivity.this,LoginActivity.class));
                finish();
            }
        });

        relativeLayout_text_name.setText(LoginActivity.getUsername());
    }
}
