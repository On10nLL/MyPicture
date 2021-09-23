package com.example.mypicture;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String [] titles = null;
    private String [] authors = null;
    private int space=5;//设置RecyclerView控件的item的上下间距
    private List<Share> shareList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);//设置刚写的xml文件页面

        initData();

        HomeAdapter homeAdapter=new HomeAdapter(
                MainActivity.this,
                R.layout.home_item,
                shareList
        );//创建适配器对象

        RecyclerView recyclerView1=findViewById(R.id.home_item);//创建recyclerView对象，并初始化其xml文件
        recyclerView1.setLayoutManager(new GridLayoutManager(this,2));//设置为表格布局，列数为2（这个是最主要的，就是这个来展示陈列式布局）
        recyclerView1.addItemDecoration(new space_item(space));//给recycleView添加item的间距
        Intent intent = new Intent(MainActivity.this,Issue.class);
        ImageButton btn_share = findViewById(R.id.btn_share);
            btn_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(intent);
                }
            });
        recyclerView1.setAdapter(homeAdapter);//将适配器添加到recyclerView
    }

    class space_item extends RecyclerView.ItemDecoration{
        //设置item的间距
        private int space=5;
        public space_item(int space){
            this.space=space;
        }
        public void getItemOffsets(Rect outRect,View view,RecyclerView parent,RecyclerView.State state){
            outRect.bottom=space;
            outRect.top=space;
        }
    }

    private void initData() {
        int length;
        String[] titles = getResources().getStringArray(R.array.titles);
        String[] authors = getResources().getStringArray(R.array.username);
        TypedArray images = getResources().obtainTypedArray(R.array.images);
        TypedArray heads = getResources().obtainTypedArray(R.array.headsIcon);

        if (titles.length > authors.length) {
            length = authors.length;
        } else {
            length = titles.length;
        }

        for (int i = 0; i < length; i++) {
            Share share = new Share();
            share.setmTitle(titles[i]);
            share.setmAuthor(authors[i]);
            share.setmImageId(images.getResourceId(i,0));
            share.setmHeadsId(heads.getResourceId(i,0));
            shareList.add(share);
        }
    }
}
