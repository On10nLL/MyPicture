package com.example.mypicture;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

    private int resourceId;
    private List<Share> mShareData;
    private Context mContext;
    private ItemDataBase itemDataBase;
    private SQLiteDatabase db;
    private Cursor cursor_collect;
    private Cursor cursor_likes;
    private String username;

    private int collect[];
    private int likes[];

    public HomeAdapter(Context context,int resourceId,List<Share> data){
        this.mContext = context;
        this.mShareData = data;
        this.resourceId = resourceId;
        itemDataBase = new ItemDataBase(context,"item.db",null,1);
        db = itemDataBase.getReadableDatabase();
        username = LoginActivity.getUsername();

        collect = new int[data.size()];
        likes = new int[data.size()];

        for (int i = 0; i < collect.length; i++) {
            collect[i] = 0;
            likes[i] = 0;
        }

        for (int i = 0; i < data.size(); i++) {
            cursor_collect = db.rawQuery("select * from collect where id = ? and user = ?",new String[]{(i+1)+"",username});
            cursor_likes = db.rawQuery("select * from likes where id = ? and user = ?",new String[]{(i+1)+"",username});
            if(cursor_collect.getCount() != 0){
                collect[i] = 1;
            }
            if(cursor_likes.getCount() != 0){
                likes[i] = 1;
            }
        }
        cursor_likes.close();
        cursor_collect.close();


    }
    @NonNull
    @Override
    //加载布局文件并返回MyViewHolder对象
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //创建view对象
        View view = LayoutInflater.from(mContext).inflate(resourceId,parent,false);
        //创建MyViewHolder对象
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    //获取数据并显示到对应控件
    public void onBindViewHolder(@NonNull HomeAdapter.MyViewHolder holder, int position) {

        Share share = mShareData.get(position);


       //保持数据统一性
        if(collect[position] == 1){
            holder.button_collect.setText("已收藏");
        }
        else {
            holder.button_collect.setText("收藏");
        }

        if(likes[position] == 1){
            holder.button_like.setText("已点赞");
        }
        else {
            holder.button_like.setText("点赞");
        }

        //判断图片是静态图片还是其他来源图片
        if (share.getmHeadsId() == 0 || share.getmImageId() == 0){
            Bitmap bitmap = BitmapFactory.decodeFile(share.getmImageUrl());
            holder.img.setImageBitmap(bitmap);
            holder.head.setImageResource(share.getmHeadsId());
        }
        else {
            holder.img.setImageResource(share.getmImageId());
            holder.head.setImageResource(share.getmHeadsId());
        }

        holder.title.setText(share.getmTitle());
        holder.username.setText(share.getmAuthor());

        if(share.getmImageId() != -1 && share.getmImageId() != 0){
            holder.img.setImageResource(share.getmImageId());
        }

        //点赞
        holder.button_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = LoginActivity.getUsername();
                if (holder.button_like.getText().toString().equals("点赞")){
                    holder.button_like.setText("已点赞");
                    if(share.getmImageId() != 0){
                        db.execSQL("insert into likes(id,author,user,title,imageurl,imageid)values(?,?,?,?,?,?)",new String[]{(position+1)+"",share.getmAuthor(),username,share.getmTitle(),"",share.getmImageId()+""});
                    }
                    else {
                        db.execSQL("insert into likes(id,author,user,title,imageurl,imageid)values(?,?,?,?,?,?)",new String[]{(position+1)+"",share.getmAuthor(),username,share.getmTitle(),share.getmImageUrl(),0+""});

                    }
                }
                else {
                    holder.button_like.setText("点赞");
                    db.execSQL("delete from likes where id = ? and user = ?",new String[]{(position+1)+"",username});
                }

            }
        });
        //收藏
        holder.button_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = LoginActivity.getUsername();
                if(holder.button_collect.getText().toString().equals("收藏")){
                    holder.button_collect.setText("已收藏");

                    if(share.getmImageId() != 0){
                        db.execSQL("insert into collect(id,author,user,title,imageurl,imageid)values(?,?,?,?,?,?)",new String[]{(position+1)+"",share.getmAuthor(),username,share.getmTitle(),"",share.getmImageId()+""});
                    }
                    else {
                        db.execSQL("insert into collect(id,author,user,title,imageurl,imageid)values(?,?,?,?,?,?)",new String[]{(position+1)+"",share.getmAuthor(),username,share.getmTitle(),share.getmImageUrl(),0+""});

                    }

                }
                else {
                    holder.button_collect.setText("收藏");
                    db.execSQL("delete from collect where id = ? and user = ?",new String[]{(position+1)+"",username});

                }
            }
        });


    }

    @Override
    public int getItemCount() {
        //获取列表条目总数
        return mShareData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        //初始化控件
        ImageButton btnCount;
        ImageView img,head;
        TextView title,username;
        Button button_collect,button_like;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.home_item_img);
            title=itemView.findViewById(R.id.home_item_title);
            head=itemView.findViewById(R.id.home_item_head);
            username=itemView.findViewById(R.id.home_item_username);
            button_like=itemView.findViewById(R.id.home_item_btnlike);
            button_collect = itemView.findViewById(R.id.collect);
        }
    }

}
