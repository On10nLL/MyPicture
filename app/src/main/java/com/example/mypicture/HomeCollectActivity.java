package com.example.mypicture;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class HomeCollectActivity extends AppCompatActivity {
    private RecyclerView item_collect;
    private ItemDataBase itemDataBase;
    private SQLiteDatabase database;
    private Cursor cursor;
    private ImageView imageView_re;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String username = LoginActivity.getUsername();
        setContentView(R.layout.home_collect);
        item_collect = (RecyclerView)findViewById(R.id.item_collect);
        imageView_re = (ImageView)findViewById(R.id.re);

        itemDataBase = new ItemDataBase(this,"item.db",null,1);
        database = itemDataBase.getReadableDatabase();
        cursor = database.rawQuery("select * from collect where user = ?",new String[]{username});
        List<Home_Share> list = new ArrayList<Home_Share>();
         if(cursor.moveToFirst()){
            do{
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String author = cursor.getString(cursor.getColumnIndex("author"));
                int imageid = cursor.getInt(cursor.getColumnIndex("imageid"));
                String imageurl = cursor.getString(cursor.getColumnIndex("imageurl"));
                list.add(new Home_Share(imageid,imageurl,title,author));
            }while (cursor.moveToNext());
        }
         ItemAdapter itemAdapter = new ItemAdapter(list,this);
         item_collect.setLayoutManager(new GridLayoutManager(this,2));
         item_collect.setAdapter(itemAdapter);
         imageView_re.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 cursor.close();
                 finish();
             }
         });
    }
}