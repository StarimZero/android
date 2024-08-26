package com.example.ex01;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    TextView hello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        hello = findViewById(R.id.hello);

        EditText edit1 = findViewById(R.id.edit1);


        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = edit1.getText().toString();
                Toast.makeText(MainActivity.this,
                        "그의 이름은" + str, Toast.LENGTH_SHORT).show();
            }
        });
    }//ONcreate
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main2, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.red){
            hello.setTextColor(Color.RED);
        }else if(item.getItemId()==R.id.blue){
            hello.setTextColor(Color.BLUE);
        }else if(item.getItemId()==R.id.green) {
            hello.setTextColor(Color.GREEN);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        int color = hello.getTextColors().getDefaultColor();
        if(color == Color.BLUE){
            menu.findItem(R.id.blue).setChecked(true);
        } else if (color == Color.RED) {
            menu.findItem(R.id.red).setChecked(true);
        } else if (color ==Color.GREEN) {
            menu.findItem(R.id.green).setChecked(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }
}//ACtivity