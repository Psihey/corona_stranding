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
import com.coronovirus_stranding.view.CoronovirusView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

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

    @BindView(R.id.circle)
    CoronovirusView circleView;

    @BindView(R.id.tv_get_started)
    TextView tvInformation;

    @BindView(R.id.scan_again)
    MaterialButton btnScanAgain;

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
    public void onStop() {
        super.onStop();
        unregisterEventBus();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        circleView.setLoading(false);
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
        if (distance == -1) {
            tvInformation.setVisibility(View.VISIBLE);
        }
    }


    @OnClick(R.id.circle)
    void startScan() {
        sendDataListener.startScan();
        tvInformation.setVisibility(View.GONE);
        circleView.setLoading(true);
    }

    @OnClick(R.id.scan_again)
    void startScanAgain() {
        sendDataListener.startScan();
        tvInformation.setVisibility(View.GONE);
        btnScanAgain.setVisibility(View.GONE);
        circleView.setLoading(true);
    }

    @SuppressLint("ResourceType")
    private void setDataInView() {
        circleView.setLoading(false);
        Collections.sort(distanceInKmWithProvince, new AttributeComparator());
        distance = Math.round(distanceInKmWithProvince.get(0).getDistance());
        SharedPref.getInstance().saveUserDistance(distance);
//        tvDistance.setText("Your distance from coronovirus is " + distance);
        tvProvince.setText("The closest province is " + distanceInKmWithProvince.get(0).getCountryRegion());
        tvTotalDeath.setText("Total death: " + totalDeath);
        tvTotalConfirmed.setText("Total confirmed: " + totalConfirmed);
        circleView.setTextRadar(String.valueOf(distance));
        if (distance > 200) {
            viewGroup.setBackgroundColor(getResources().getColor(R.color.color_safe));
            navBar.setBackgroundColor(getResources().getColor(R.color.color_safe_bottom));
            window.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.color_safe_status_bar));
        } else {
            viewGroup.setBackgroundColor(getResources().getColor(R.color.color_alarm));
            navBar.setBackgroundColor(getResources().getColor(R.color.color_alarm_bottom));
            window.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.color_alarm_status_bar));
        }
        circleView.setClickable(false);
        btnScanAgain.setVisibility(View.VISIBLE);

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
