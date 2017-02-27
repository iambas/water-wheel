package com.darker.test.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.darker.test.R;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class BookActivity extends AppCompatActivity {

    private SharedPreferences sharedP;
    private SharedPreferences.Editor editor;
    private String photoPath;

    private DatePickerDialog mDatePicker;
    private Calendar mCalendar;
    private ImageButton mDateButton;
    private TextView mTextDate, textView;

    private EditText editText;
    private CheckBox ckGuide, ckRoom;
    private String textDate = "", bookDate = "", text;

    private boolean isGuide = false, isRoom = false;
    private String name = "", guide = "", room = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        sharedP = getSharedPreferences("book", Context.MODE_PRIVATE);
        editor = sharedP.edit();

        String photo = sharedP.getString("photoPath", "");
        if (photo != "") {
            ImageView imageView = (ImageView) findViewById(R.id.img_book_qr);
            Bitmap bitmap = BitmapFactory.decodeFile(photo);
            imageView.setImageBitmap(bitmap);

            textView = (TextView) findViewById(R.id.book_name);
            textView.setText(sharedP.getString("name", ""));

            textView = (TextView) findViewById(R.id.book_giude);
            text = sharedP.getBoolean("guide", false) ? "มัคคุเทศก์" : "ไม่มีมัคคุเทศก์";
            textView.setText(text);

            textView = (TextView) findViewById(R.id.book_room);
            text = sharedP.getBoolean("room", false) ? "จองห้องพัก" : "ไม่ได้จองห้องพัก";
            textView.setText(text);

            textView = (TextView) findViewById(R.id.book_date);
            textView.setText(sharedP.getString("date", ""));
        }
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
                String s = sharedP.getString("name", "");
                if (s != "")
                    onConfirm();
                break;
        }
        return true;
    }

    public void onConfirm(){
        AlertDialog.Builder builder = new AlertDialog.Builder(BookActivity.this);
        builder.setMessage("ต้องการยกเลิกการจองทัวร์นี้?");
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

    public void onAddBookClicked(View view){
        //startActivity(new Intent(BookActivity.this, BookTourActivity.class));

        AlertDialog.Builder builder = new AlertDialog.Builder(BookActivity.this);
        LayoutInflater inflater = getLayoutInflater();

        View vb = inflater.inflate(R.layout.book_dialog, null);
        builder.setView(vb);

        editText = (EditText) vb.findViewById(R.id.your_name);
        ckGuide = (CheckBox) vb.findViewById(R.id.ck_guide);
        ckRoom = (CheckBox) vb.findViewById(R.id.ck_room);

        mDateButton = (ImageButton) vb.findViewById(R.id.bn_getDate);
        mTextDate = (TextView) vb.findViewById(R.id.getDate);

        mCalendar = Calendar.getInstance();

        mDatePicker = DatePickerDialog.newInstance(
                onDateSetListener,
                mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH),
                false);

        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePicker.setYearRange(2000, 2020);
                mDatePicker.show(getSupportFragmentManager(), "datePicker");
            }
        });

        builder.setPositiveButton("จอง", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                name = editText.getText().toString();
                Log.d("Name", "." + name + ".");

                if (isGuide)    guide = "guide\n";
                if (isRoom)     room = "book a room\n";

                if(name == "" || name.equals("")) {
                    if (textDate == "") {
                        Toast.makeText(getApplicationContext(), "กรุณากรอกชื่อของคุณและเลือกวันที่", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "กรุณากรอกชื่อของคุณ", Toast.LENGTH_LONG).show();
                    }
                }else if (textDate == ""){
                    Toast.makeText(getApplicationContext(), "กรุณาเลือกวันที่", Toast.LENGTH_LONG).show();
                }else{

                    String text2Qr = name + "\n" + guide + room + bookDate; // bookDate = textDate

                    File photo = null;
                    try{
                        photo = createImageFile();
                        editor.putString("photoPath", photoPath);
                        editor.putString("name", name);
                        editor.putString("date", textDate); // textDate
                        editor.putBoolean("guide", isGuide);
                        editor.putBoolean("room", isRoom);
                        editor.commit();
                    }catch (IOException e){
                        Log.e("CreateImage", e.getMessage());
                    }

                    if (photo != null) {

                        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                        try {
                            BitMatrix bitMatrix = multiFormatWriter.encode(text2Qr, BarcodeFormat.QR_CODE, 200, 200);
                            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

                            try {
                                FileOutputStream out = new FileOutputStream(photoPath);
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                                out.flush();
                                out.close();
                            } catch (Exception e) {
                                Log.e("Bitmap", e.getMessage());
                            }
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }
                    }
                    finish();
                    startActivity(getIntent());
                }
            }
        });
        builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        builder.show();
    }

    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
            mCalendar.set(year, month, day);
            Date date = mCalendar.getTime();
            textDate = dateFormat.format(date);

            Log.d("Day", String.valueOf(day));
            Log.d("Month", String.valueOf(month));
            Log.d("Year", String.valueOf(year));

            String[] m = getResources().getStringArray(R.array.month);
            bookDate = day + " " + m[month] + " " + year;
            //mTextDate.setText(bookDate);
            mTextDate.setText(textDate);
        }
    };


    private File createImageFile() throws IOException {
        String time = new SimpleDateFormat("ddMMyyyy_HHmmss_").format(new Date());
        String imageFileName = "PNG_" + time + "_";
        File storage = getExternalFilesDir(null);
        File image = File.createTempFile(imageFileName, ".png", storage);
        photoPath = image.getAbsolutePath();

        return image;
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch(view.getId()) {
            case R.id.ck_guide:
                if (checked)    isGuide = true;
                else    isGuide = false;
                break;
            case R.id.ck_room:
                if (checked)    isRoom = true;
                else    isRoom = false;
                break;
        }
    }
}
