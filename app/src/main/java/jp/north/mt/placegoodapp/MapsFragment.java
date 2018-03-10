package jp.north.mt.placegoodapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.content.Context.LOCATION_SERVICE;

public class MapsFragment extends Fragment implements OnMapReadyCallback, LocationListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private TextView textView1;
    private TextView textView2;
    private Location mlocation;
    private LocationManager locationManager = null;
    //検索ボタン機能追加のためメンバ変数追加
    Button mRegist_button;

    public static MapsFragment newInstance() {
        MapsFragment fragment = new MapsFragment();
        return fragment;
    }

    @Override
    public void onLocationChanged(Location location) {
        mlocation = location;
        String longitude = String.valueOf(location.getLongitude());
        String latitude = String.valueOf(location.getLatitude());
        Log.d("MapsFragment", String.format("lon:" + longitude + ", lat: " + latitude));
        textView1.setText(String.valueOf(location.getLatitude()));
        textView2.setText(String.valueOf(location.getLongitude()));

        //マーカーは数値が変動すると付かないため、一度変数に入れる。（できれば固定値(final)が望ましい）
        double lat = mlocation.getLatitude();
        double lng = mlocation.getLongitude();

        //現在地にマーカーをつける
        mMap.addMarker(new MarkerOptions()
//                .position(new LatLng(lat, lng))
                .position(new LatLng(10.1111, 10.1111))
                .title("Hello world"));
    }

    private void startLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationManager = (LocationManager) getActivity().getSystemService(Activity.LOCATION_SERVICE);

            //位置測位プロバイダを決定する
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
            criteria.setBearingRequired(false);
            criteria.setSpeedRequired(false);
            criteria.setAltitudeRequired(false);
            String bestProvider = locationManager.getBestProvider(criteria, true);

            //測位を開始する
            locationManager.requestLocationUpdates(bestProvider,
                    LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //LocationManagerの取得
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        //パーミッション（※Fragmentのため、thisではなく、getActivity()で親のActivityを呼び出し）
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //現在地の座標を取得
            mMap.setMyLocationEnabled(true);
            // Set a listener for marker click.
            mMap.setOnMarkerClickListener(this);

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.fragment_maps);
        mapFragment.getMapAsync(this);

        textView1 = (TextView) view.findViewById(R.id.textView_latitude);
        textView2 = (TextView) view.findViewById(R.id.textView_longitude);
        startLocation();

        //登録ボタンを押した時の処理
        Button button = (Button) view.findViewById(R.id.regist_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double lat = 35.681167;
                double lng = 139.767052;

                if(mlocation!=null) {
                    lat = mlocation.getLatitude();
                    lng = mlocation.getLongitude();
                }

                //intent.putExtraはintentに処理を渡す
                Intent intent = new Intent(getActivity(), InputTask.class);
//                intent.putExtra(EXTRA_TASK, task.getId());
                intent.putExtra("VALUE1", lat);
                intent.putExtra("VALUE2", lng);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
            Toast.makeText(getActivity(),
                    marker.getTitle() +
                            " has been clicked " + clickCount + " times.",
                    Toast.LENGTH_SHORT).show();
        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    //15秒に1度更新する
    private final int LOCATION_UPDATE_MIN_TIME = 15 * 1000;
    //5m移動する度に更新する
    private final int LOCATION_UPDATE_MIN_DISTANCE = 5;

    @Override
    public void onResume() {
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0,
                    0,
                    this);
        }

        super.onResume();
    }

    @Override
    public void onPause() {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }

        super.onPause();
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
            case LocationProvider.AVAILABLE:
                Log.d("Status", "AVAILABLE");
                break;
            case LocationProvider.OUT_OF_SERVICE:
                Log.d("Status", "OUT_OF_SERVICE");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.d("Status", "TEMPORARILY_UNAVAILABLE");
                break;
        }
    }
}