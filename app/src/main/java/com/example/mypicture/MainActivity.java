package com.example.mypicture;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String [] titles = null;
    private String [] authors = null;
    private int space=5;//设置RecyclerView控件的item的上下间距
    private static List<Share> shareList = new ArrayList<>();
    private SQLiteDatabase db;
    private ItemDataBase itemDataBase;
    private Cursor cursor;


    //获得全局list
    public static List<Share> getShareList() {
        return shareList;
    }
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        itemDataBase = new ItemDataBase(this,"item.db",null,1);
        db = itemDataBase.getReadableDatabase();


        setContentView(R.layout.home);//设置刚写的xml文件页面

        initData();

        HomeAdapter homeAdapter=new HomeAdapter(
                MainActivity.this,
                R.layout.home_item,
                shareList
        );//创建适配器对象

        RecyclerView recyclerView1=findViewById(R.id.home_item);//创建recyclerView对象，并初始化其xml文件
        recyclerView1.setLayoutManager(new GridLayoutManager(this,2));//设置为表格布局，列数为2
        recyclerView1.addItemDecoration(new space_item(space));//给recycleView添加item的间距

        findViewById(R.id.btn_share).setOnClickListener(this::onClick);
        findViewById(R.id.btn_me).setOnClickListener(this);

        recyclerView1.setAdapter(homeAdapter);//将适配器添加到recyclerView

        cursor.close();
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

        //加载初始数据
        cursor = db.rawQuery("select * from items",null);
        if(cursor.getCount() == 0){
            for (int i = 0; i < length; i++) {
            Share share = new Share();
            share.setmTitle(titles[i]);
            share.setmAuthor(authors[i]);
            share.setmImageId(images.getResourceId(i,0));
            share.setmHeadsId(heads.getResourceId(i,0));
            shareList.add(share);
            db.execSQL("insert into items(author,title,imageurl,imageid)values(?,?,?,?)",new String[]{authors[i],titles[i],"",images.getResourceId(i,0)+""});
           }
        }
        else {
            shareList.clear();
            if(cursor.moveToFirst()){
                do{
                    Share share = new Share();
                    String author = cursor.getString(cursor.getColumnIndex("author"));
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    int imageid = cursor.getInt(cursor.getColumnIndex("imageid"));
                    String imageurl = cursor.getString(cursor.getColumnIndex("imageurl"));
                    share.setmTitle(title);
                    share.setmImageUrl(imageurl);
                    share.setmAuthor(author);
                    share.setmImageId(imageid);
                    share.setmHeadsId(heads.getResourceId(0,0));
                    shareList.add(share);
                }while (cursor.moveToNext());

            }
        }
    }

    @Override
    public  void onClick(View view){
        switch (view.getId()){
            case R.id.btn_share:
                startActivity(new Intent(MainActivity.this,IssueActivity.class));
                break;
            case R.id.btn_me:
                startActivity(new Intent(MainActivity.this,MyHomeActivity.class));
                break;
        }
    }

    public void addVIewItem(View view){
        View Item = View.inflate(this,R.layout.home_item,null);
    }
}
