package com.example.mypicture;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    public HomeAdapter(Context context,int resourceId,List<Share> data){
        this.mContext = context;
        this.mShareData = data;
        this.resourceId = resourceId;
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
        holder.img.setImageResource(share.getmImageId());
        holder.title.setText(share.getmTitle());
        holder.head.setImageResource(share.getmHeadsId());
        holder.username.setText(share.getmAuthor());

        if(share.getmImageId() != -1){
            holder.img.setImageResource(share.getmImageId());
        }

        holder.btnCount.setOnClickListener(new View.OnClickListener(){
            int count = 0;
            @Override
            public void onClick(View v) {
                holder.likeCount.setText(Integer.toString(count++));
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
        TextView likeCount;
        ImageView img,head;
        TextView title,username;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.home_item_img);
            title=itemView.findViewById(R.id.home_item_title);
            head=itemView.findViewById(R.id.home_item_head);
            username=itemView.findViewById(R.id.home_item_username);
            btnCount=itemView.findViewById(R.id.home_item_btnCount);
            likeCount=itemView.findViewById(R.id.home_item_like);
        }
    }
}
