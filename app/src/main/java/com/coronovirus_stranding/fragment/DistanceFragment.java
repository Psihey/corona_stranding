package com.coronovirus_stranding.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.coronovirus_stranding.MyLocationListener;
import com.coronovirus_stranding.R;
import com.coronovirus_stranding.SharedPref;
import com.coronovirus_stranding.event.DataSuccessfullyEvent;
import com.coronovirus_stranding.model.AttributeComparator;
import com.coronovirus_stranding.model.AttributesModel;
import com.coronovirus_stranding.view.CircleView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.coronovirus_stranding.activity.MainActivity.CONFIRM;
import static com.coronovirus_stranding.activity.MainActivity.DEATH;


public class DistanceFragment extends BaseFragment {


    @BindView(R.id.tv_province)
    TextView tvProvince;

    @BindView(R.id.total_confirmed)
    TextView tvTotalConfirmed;

    @BindView(R.id.total_death)
    TextView tvTotalDeath;

    @BindView(R.id.rl_container)
    ViewGroup container;

    @BindView(R.id.radar)
    LottieAnimationView lottieAnimationView;

    @BindView(R.id.circle)
    CircleView circleView;

    @BindView(R.id.tv_get_started)
    TextView tvInformation;

    private SendDataListener sendDataListener;
    private List<AttributesModel> distanceInKmWithProvince;
    private Map<String, Integer> statistic = new HashMap<>();
    private int totalDeath;
    private int totalConfirmed;
    private BottomNavigationView navBar;
    private Window window;
    private ViewGroup viewGroup;
    private long distance = -1;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof SendDataListener) {
            sendDataListener = (SendDataListener) context;
        }
    }

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
        distanceInKmWithProvince = sendDataListener.getData();
        window = getActivity().getWindow();
        navBar = getActivity().findViewById(R.id.bottom_navigation_view);
        viewGroup = getActivity().findViewById(R.id.main_activity_fragment_container);
        statistic = sendDataListener.getStatistic();
        if (distanceInKmWithProvince != null) {
            totalConfirmed = statistic.get(CONFIRM);
            totalDeath = statistic.get(DEATH);
            setDataInView();
        }
        if (distance == -1){
            tvInformation.setVisibility(View.VISIBLE);
        }
    }


    @OnClick(R.id.circle)
    void startScan() {
        sendDataListener.startScan();
        lottieAnimationView.setVisibility(View.VISIBLE);
        tvInformation.setVisibility(View.GONE);
    }

    @SuppressLint("ResourceType")
    private void setDataInView() {
        lottieAnimationView.setVisibility(View.GONE);
        Collections.sort(distanceInKmWithProvince, new AttributeComparator());
        distance = Math.round(distanceInKmWithProvince.get(0).getDistance());
        SharedPref.getInstance().saveUserDistance(distance);
//        tvDistance.setText("Your distance from coronovirus is " + distance);
        tvProvince.setText("The closest province is " + distanceInKmWithProvince.get(0).getCountryRegion());
        tvTotalDeath.setText("Total death: " + totalDeath);
        tvTotalConfirmed.setText("Total confirmed: " + totalConfirmed);

        if (distance > 400) {
            circleView.setTextRadar(distance + " km", getResources().getString(R.color.color_radar_safe), getResources().getString(R.color.color_radar_safe), getResources().getString(R.color.color_radar2_safe), getResources().getString(R.color.color_radar3_safe));
            viewGroup.setBackgroundColor(getResources().getColor(R.color.color_safe));
            navBar.setBackgroundColor(getResources().getColor(R.color.color_safe_bottom));
            window.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.color_safe_status_bar));
        } else {
            circleView.setTextRadar(distance + " km", getResources().getString(R.color.color_alarm_radar), getResources().getString(R.color.color_alarm_radar), getResources().getString(R.color.color_alarm_radar2), getResources().getString(R.color.color_alarm_radar3));
            viewGroup.setBackgroundColor(getResources().getColor(R.color.color_alarm));
            navBar.setBackgroundColor(getResources().getColor(R.color.color_alarm_bottom));
            window.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.color_alarm_status_bar));
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_distance;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataReceived(DataSuccessfullyEvent event) {
        distanceInKmWithProvince = sendDataListener.getData();
        statistic = sendDataListener.getStatistic();
        totalConfirmed = statistic.get(CONFIRM);
        totalDeath = statistic.get(DEATH);
        setDataInView();
    }

}
