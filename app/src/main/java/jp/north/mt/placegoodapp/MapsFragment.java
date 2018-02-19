package jp.north.mt.placegoodapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import static android.content.Context.LOCATION_SERVICE;

public class MapsFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private TextView textView;

    public static MapsFragment newInstance() {
        MapsFragment fragment = new MapsFragment();
        return fragment;
    }

    @Override
    public void onLocationChanged(Location location) {
        String longitude = String.valueOf(location.getLongitude());
//        float latitude = Double.valueOf(location.getLatitude()).floatValue();
        String latitude = String.valueOf(location.getLatitude());
        Log.d("MapsFragment", String.format("lon:%f lat:%f", longitude, latitude));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, null, false);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.fragment_maps);
        mapFragment.getMapAsync(this);

        textView = (TextView) view.findViewById(R.id.fragment_maps);
        textView.setText(String.valueOf(location.getLatitude()));

        return view;
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
        }
    }

    private LocationManager mLocationManager;
    //30秒に1度更新する
    private final int LOCATION_UPDATE_MIN_TIME = 1 * 1000;
    //10m移動する度に更新する
    private final int LOCATION_UPDATE_MIN_DISTANCE = 3;

    @Override
    public void onResume() {
        if (mLocationManager != null) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0,
                    0,
                    this);
        }

        super.onResume();
    }

    @Override
    public void onPause() {
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(this);
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