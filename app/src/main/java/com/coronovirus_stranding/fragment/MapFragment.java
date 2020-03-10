package com.coronovirus_stranding.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.coronovirus_stranding.R;
import com.coronovirus_stranding.event.DataSuccessfullyEvent;
import com.coronovirus_stranding.model.AttributesModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

public class MapFragment extends BaseFragment implements OnMapReadyCallback {

    @BindView(R.id.map_view)
    MapView googleMapView;

    private List<AttributesModel> coordinatesList;

    private SendDataListener sendDataListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof SendDataListener) {
            sendDataListener = (SendDataListener) context;
        }
    }

    private GoogleMap googleMap;

    @Override
    public void onStart() {
        super.onStart();
        registerEventBus();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterEventBus();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        googleMapView.onCreate(savedInstanceState);
        googleMapView.onResume();
        googleMapView.getMapAsync(this);
        coordinatesList = sendDataListener.getCoordinates();
        if (coordinatesList != null) {
            setDataInView();
        }else {
            Snackbar snackbar = Snackbar.make(googleMapView,"You should start scan before watch the map",Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_map;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (coordinatesList != null) {
            this.googleMap.setMyLocationEnabled(true);
            setDataInView();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataReceived(DataSuccessfullyEvent event) {
        coordinatesList = sendDataListener.getCoordinates();
        setDataInView();
    }

    private void setDataInView() {
        for (AttributesModel attributesModel : coordinatesList) {
            if (googleMap != null) {
                double recovered = (double) attributesModel.getRecovered();
                MarkerOptions alarm = new MarkerOptions().position(new LatLng(attributesModel.getLat(), attributesModel.getLong())).title("Confirmed: " + attributesModel.getConfirmed()
                        + ". " + "Deaths: " + attributesModel.getDeaths() + ". " + "Recovered: " + (int) recovered);
                alarm.icon(BitmapDescriptorFactory.fromResource(R.drawable.virus));
                googleMap.addMarker(alarm);
            }
        }
    }

}
