package com.darker.test.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.darker.test.R;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        /*if (id == R.id.menu_point) {
            getPoint();
            return true;
        }else if(id == R.id.menu_clear){
            onConfirm();
            //delTable();
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    public void onButtonClicked(View view){
        int id = view.getId();

        if (id == R.id.bn_clang){
            startActivity(new Intent(this, KnowlageActivity.class));
        }else if (id == R.id.bn_book){
            startActivity(new Intent(this, BookActivity.class));
        }else if (id == R.id.bn_stamp){
            startActivity(new Intent(this, QRCodeActivity.class));
        }else if (id == R.id.bn_map){
            startActivity(new Intent(this, MapsActivity.class));
        }
    }
}
