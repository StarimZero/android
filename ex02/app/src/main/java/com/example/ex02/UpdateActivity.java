package com.example.ex02;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UpdateActivity extends AppCompatActivity {
    int id;
    DBHelper helper;
    SQLiteDatabase db;
    EditText name, price;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        getSupportActionBar().setTitle("상세정보" + id);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.name);
        price = findViewById(R.id.price);


        helper = new DBHelper(this);
        db = helper.getWritableDatabase();
        String sql = "select _id, name, price from product where _id=" + id;
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToNext()){
            name.setText(cursor.getString(1));
            price.setText(String.valueOf(cursor.getInt(2)));
        }

        Button button = findViewById(R.id.btnInsert);
        button.setText("수정");
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AlertDialog.Builder box = new AlertDialog.Builder(UpdateActivity.this);
                box.setTitle("수정");
                box.setMessage("수정합니다.");
                box.show();
                box.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String strName = name.getText().toString();
                        String strPrice = price.getText().toString();
                        String sql = "update product set name = '" + strName + "',";
                        sql+= "price = '" + strPrice + "',";
                        sql+= " where _id=" + id;
                        db.execSQL(sql);
                        finish();
                    }
                });
                box.setNegativeButton("아니오", null);
            }
        });
    }//버튼 온클릭리스너

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }




}