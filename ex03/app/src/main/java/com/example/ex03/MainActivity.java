package com.example.ex03;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    DBHelper helper;
    SQLiteDatabase db;
    MyAdpter adpter;
    String sql="select _id, name, juso, phone, photo from address";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("주소관리");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        helper = new DBHelper(this);
        db = helper.getReadableDatabase();

        //데이터생성
        Cursor cursor=db.rawQuery(sql, null);
        adpter = new MyAdpter(this, cursor);
        ListView list=findViewById(R.id.list);
        list.setAdapter(adpter);

        findViewById(R.id.btnInsert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, InsertActivity.class);
                startActivity(intent);
            }
        });
    }//onCreate

    class MyAdpter extends CursorAdapter{
        public MyAdpter(Context context, Cursor c) {
            super(context, c);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            View view=getLayoutInflater().inflate(R.layout.item,viewGroup,false);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));

            // List item 클릭 시 UpdateActivity로 이동
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
            });

            // CircleImageView 설정
            CircleImageView photo = view.findViewById(R.id.photo);
            String strPhoto = cursor.getString(cursor.getColumnIndexOrThrow("photo"));

            // null 또는 빈 문자열인지 확인
            if (strPhoto == null || strPhoto.isEmpty()) {
                photo.setImageResource(R.drawable.person); // 기본 이미지 설정
            } else {
                Bitmap bitmap = BitmapFactory.decodeFile(strPhoto);
                if (bitmap != null) {
                    photo.setImageBitmap(bitmap);
                } else {
                    photo.setImageResource(R.drawable.person); // 파일이 유효하지 않을 경우 기본 이미지 설정
                }
            }

            // TextView 설정
            TextView name = view.findViewById(R.id.name);
            name.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")));

            TextView phone = view.findViewById(R.id.phone);
            phone.setText(cursor.getString(cursor.getColumnIndexOrThrow("phone")));

            TextView juso = view.findViewById(R.id.juso);
            juso.setText(cursor.getString(cursor.getColumnIndexOrThrow("juso")));

            // 삭제 버튼 클릭 처리
            view.findViewById(R.id.btnDelete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder box = new AlertDialog.Builder(MainActivity.this);
                    box.setTitle("질의");
                    box.setMessage(id + "번 주소를 삭제하실래요?");
                    box.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String sqlDelete = "DELETE FROM address WHERE _id=" + id;
                            db.execSQL(sqlDelete);
                            onRestart(); // 데이터 새로 고침
                        }
                    });
                    box.setNegativeButton("아니오", null);
                    box.show();
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Cursor cursor = db.rawQuery(sql, null);
        adpter.changeCursor(cursor);
    }
}//Activity