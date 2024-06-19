package project.hansungcomputerdepartment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationFragment extends Fragment implements OnMapReadyCallback {

    private MapView labOfficeMapView;
    private MapView adminOfficeMapView;
    private GoogleMap labOfficeMap;
    private GoogleMap adminOfficeMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);

        labOfficeMapView = view.findViewById(R.id.labOfficeMapView);
        adminOfficeMapView = view.findViewById(R.id.adminOfficeMapView);

        labOfficeMapView.onCreate(savedInstanceState);
        adminOfficeMapView.onCreate(savedInstanceState);

        labOfficeMapView.getMapAsync(this::onLabOfficeMapReady);
        adminOfficeMapView.getMapAsync(this::onAdminOfficeMapReady);

        return view;
    }

    private void onLabOfficeMapReady(GoogleMap googleMap) {
        labOfficeMap = googleMap;
        setupLabOfficeMap();
    }

    private void onAdminOfficeMapReady(GoogleMap googleMap) {
        adminOfficeMap = googleMap;
        setupAdminOfficeMap();
    }

    private void setupLabOfficeMap() {
        MapsInitializer.initialize(getContext());
        LatLng labOfficeLocation = new LatLng(37.581826, 127.009860);
        labOfficeMap.addMarker(new MarkerOptions().position(labOfficeLocation).title("실습 사무실"));
        labOfficeMap.moveCamera(CameraUpdateFactory.newLatLngZoom(labOfficeLocation, 18));
    }

    private void setupAdminOfficeMap() {
        MapsInitializer.initialize(getContext());
        LatLng adminOfficeLocation = new LatLng(37.582034, 127.009868);
        adminOfficeMap.addMarker(new MarkerOptions().position(adminOfficeLocation).title("행정 사무실"));
        adminOfficeMap.moveCamera(CameraUpdateFactory.newLatLngZoom(adminOfficeLocation, 18));
    }

    @Override
    public void onResume() {
        super.onResume();
        labOfficeMapView.onResume();
        adminOfficeMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        labOfficeMapView.onPause();
        adminOfficeMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        labOfficeMapView.onDestroy();
        adminOfficeMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        labOfficeMapView.onLowMemory();
        adminOfficeMapView.onLowMemory();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }
}

/*@Override
public void onMapReady(GoogleMap googleMap) {
    // 실습 사무실 위치 설정
    LatLng labOfficeLocation = new LatLng(37.581826, 127.009860);
    googleMap.addMarker(new MarkerOptions().position(labOfficeLocation).title("실습 사무실 (공학관 A동)"));
    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(labOfficeLocation, 18));

    // 행정 사무실 위치 설정
    LatLng adminOfficeLocation = new LatLng(37.582034, 127.009868);
    googleMap.addMarker(new MarkerOptions().position(adminOfficeLocation).title("행정 사무실 (지선관)"));
    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(adminOfficeLocation, 18));
}
*/