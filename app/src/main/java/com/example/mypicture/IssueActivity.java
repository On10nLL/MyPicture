package com.example.mypicture;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.List;
//点击“+”添加图片的工具类

public class IssueActivity extends AppCompatActivity {

    private List<Share> list;
    private Button button_update;
    private Button button_cancel;
    private ImageButton imageButton;
    private ImageView imageView;
    private EditText editText_title;
    private EditText editText_context;


    private String imagePath = null;
    private final int CODE_PHOTO = 2;
    private ItemDataBase itemDataBase;
    private SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        itemDataBase = new ItemDataBase(this,"item.db",null,1);
        db = itemDataBase.getReadableDatabase();

        setContentView(R.layout.share);
        list = MainActivity.getShareList();
        //权限申请安卓6.0以上需要申请
        if(Build.VERSION.SDK_INT>=23){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0x01);
        }
        imageButton = (ImageButton)findViewById(R.id.btn_add);
        imageView = (ImageView)findViewById(R.id.show_image);
        button_update = (Button)findViewById(R.id.update);
        button_cancel = (Button)findViewById(R.id.btn_cancel);
        editText_title = (EditText)findViewById(R.id.title);
        editText_context = (EditText)findViewById(R.id.context);

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IssueActivity.this,MainActivity.class));
                finish();
            }
        });
        getAlbum();
        setButton_update();
        }

        //访问相册
    private void getAlbum(){
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                startActivityForResult(intent,CODE_PHOTO); //打开相册
            }
        });

    }
    private void setButton_update(){
        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = LoginActivity.getUsername();

                String title = editText_title.getText().toString();
                String context = editText_context.getText().toString();

                if(imagePath == null || imagePath.equals("")){
                    Toast.makeText(IssueActivity.this, "还没上传照片不能发布", Toast.LENGTH_SHORT).show();
                }
                else if(title.equals("")){
                    Toast.makeText(IssueActivity.this, "标题不能为空", Toast.LENGTH_SHORT).show();
                }
                else {
                    Share share = new Share();

                    share.setmAuthor("user");
                    share.setmHeadsId(0);
                    //使用路径存储
                    share.setmImageId(0);
                    share.setmImageUrl(imagePath);

                    share.setmTitle(title);
                    list.add(share);

                    db.execSQL("insert into items(author,title,imageurl,imageid)values(?,?,?,?)",new String[]{username,title,imagePath,0+""});
                    Toast.makeText(IssueActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(IssueActivity.this,MainActivity.class));
                    finish();
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case CODE_PHOTO:
                if(resultCode == RESULT_OK){
                    //判断手机系统版本号
                    if(Build.VERSION.SDK_INT>=19){
                        getPhoto(data);
                        show(imagePath);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void show(String path){
        if(path != null && !path.equals("")){
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            imageView.setImageBitmap(bitmap);
        }
    }

    //解析返回的照片数据
    private void getPhoto(Intent data){
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            //如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];  //解析出数字格式的id
                String selection = MediaStore.Images.Media._ID+"="+id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);

            }

        }else if("content".equalsIgnoreCase(uri.getScheme())){
            //如果是file类型的Uri，直接获取图片路径即可
            imagePath = getImagePath(uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            //如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }

    }

    private String getImagePath(Uri uri,String selection){
        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if(cursor!= null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

}
