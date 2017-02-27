package com.darker.test.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.darker.test.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QRCodeActivity extends AppCompatActivity {

    private IntentIntegrator qrScan;
    private TextView txtResult;
    private String[] stamp;
    private String newStamp = "";

    private SharedPreferences sharedP;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        sharedP = getSharedPreferences("stampShared", Context.MODE_PRIVATE);
        editor = sharedP.edit();

        txtResult = (TextView) findViewById(R.id.textResult);
        stamp = getResources().getStringArray(R.array.water_code);

        String txtStamp = sharedP.getString("stamp", "");
        if (!txtStamp.equals("")){
            String[] sTxt = txtStamp.split(",");

            for (int i = 0; i < 23; i++){
                if (sTxt[i].equals("1")){
                    newStamp += stamp[i] + " : เรียบร้อย\n";
                }else{
                    newStamp += stamp[i] + " : \n";
                }
            }
        }else{
            for (int i = 0; i < 23; i++){
                newStamp += stamp[i] + " : \n";
            }
        }

        txtResult.setText(newStamp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_book, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_clear:
                String s = sharedP.getString("stamp", "");
                if (s != "")
                    onConfirm();
                break;
        }
        return true;
    }

    public void onConfirm(){
        AlertDialog.Builder builder = new AlertDialog.Builder(QRCodeActivity.this);
        builder.setMessage("ต้องการลบประวัติการปั๊มแสตมป์?");
        builder.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                editor.clear();
                editor.commit();
                finish();
                startActivity(getIntent());
            }
        });

        builder.setNegativeButton("ไม่ใช่", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        builder.show();
    }

    public void onAddStampClicked(View view){
        qrScan = new IntentIntegrator(this);
        qrScan.initiateScan();
    }

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                String text = result.getContents();

                for (int i = 0; i < 23; i++){
                    if (text.equals(stamp[i])){
                        putStamp(i);
                        break;
                    }
                }

                Toast.makeText(this, text, Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void putStamp(int i){
        String txtStamp = sharedP.getString("stamp", "");
        newStamp = "";

        if(txtStamp.equals("")) {
            for (int x = 0; x < 22; x++) {
                if (x == i) newStamp += "1,";
                else    newStamp += "0,";
            }

            if (i == 22)    newStamp += "1";
            else    newStamp += "0";

        }else {
            String[] sTxt = txtStamp.split(",");
            for (int x = 0; x < 22; x++) {
                if (x == i) newStamp += "1,";
                else    newStamp += sTxt[x] + ",";
            }


            if (i == 22)    newStamp += "1";
            else    newStamp += sTxt[22];
        }

        editor.putString("stamp", newStamp);
        editor.commit();

        finish();
        startActivity(getIntent());
    }
}
