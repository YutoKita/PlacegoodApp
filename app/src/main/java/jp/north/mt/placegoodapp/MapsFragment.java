package jp.north.mt.placegoodapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsFragment extends Fragment {

    private GoogleMap mMap;

    public static MapsFragment newInstance() {
        MapsFragment fragment = new MapsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MapsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_maps, container, false);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_maps))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.getUiSettings().setZoomControlsEnabled(true);//拡大縮小ボタン表示

        LatLng morioka = new LatLng(39.703531, 141.152667);//盛岡
        LatLng takizawa = new LatLng(39.734694, 141.077056);//滝沢

        CameraPosition.Builder camerapos = new CameraPosition.Builder();//表示位置の作成
        camerapos.target(morioka);//カメラの表示位置の指定
        camerapos.zoom(13.0f);//ズームレベル
        camerapos.bearing(0);//カメラの向きの指定(北向きなので０）
        camerapos.tilt(25.0f);//カメラの傾き設定
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(camerapos.build()));//マップの表示位置変更

        MarkerOptions options = new MarkerOptions();//ピンの設定
        options.position(morioka);//ピンの場所を指定
        options.title("盛岡");//マーカーの吹き出しの設定
        mMap.addMarker(options);//ピンの設置

        options.position(takizawa);
        options.title("滝沢");
        mMap.addMarker(options);
    }
}
