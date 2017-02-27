package com.darker.test.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.darker.test.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double latitude, longtitude;
    private int mType, mapType = GoogleMap.MAP_TYPE_NORMAL;

    private LocationManager locationManager ;
    private boolean GpsStatus ;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        context = getApplicationContext();
        checkGpsStatus();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt("mapType", mType);
        super.onSaveInstanceState(savedInstanceState);
    }

    //onRestoreInstanceState
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mapType = savedInstanceState.getInt("mapType");
    }

   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_normal) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mType = mMap.getMapType();
            return true;
        }else if(id == R.id.menu_hybrid){
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            mType = mMap.getMapType();
            return true;
        }else if(id == R.id.menu_satellite){
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            mType = mMap.getMapType();
            return true;
        }else if(id == R.id.menu_terrain){
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            mType = mMap.getMapType();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(mapType);
        mType = mMap.getMapType();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        double[][] r = new double[][]{
                {14.612528,101.462306},
                {14.610762,101.460076},
                {14.617507,101.444775},
                {14.617899,101.444246},
                {14.618432,101.440565},     // 5
                {14.618685,101.440441},
                {14.621886,101.436951},
                {14.6233258,101.4362327},
                {14.623742,101.435318},
                {14.6247392,101.4358916},   // 10
                {14.626147,101.433878},
                {14.6272784,101.4254034},
                {14.628633,101.423731},
                {14.6332099,101.4199346},
                {14.6398696,101.4206489},   // 15
                {14.6403029,101.4214675},
                {14.640717,101.421265},
                {14.643776,101.419359},
                {14.643643,101.418651},
                {14.646122,101.417284},     // 20
                {14.646634,101.418132},
                {14.646927,101.418599},
                {14.658459,101.411551},
        };

        int len = r.length;

        for (int i = 0; i < len; i++){
            LatLng l = new LatLng(r[i][0], r[i][1]);
            mMap.addMarker(new MarkerOptions().position(l).title("ระหัดวิดน้ำ " + (i+1)));
            if (i == 11) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(l));
            }
        }

        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(true);
    }

    public void checkGpsStatus(){

        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(GpsStatus == false){
            AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
            builder.setMessage("ในการดำเนินการต่อ ให้อุปกรณ์เปิดตำแหน่ง (GPS)");
            builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            });

            builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {}
            });

            builder.show();
        }

    }
}
