package com.coronovirus_stranding.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.coronovirus_stranding.R;
import com.coronovirus_stranding.SharedPref;
import com.coronovirus_stranding.event.DataSuccessfullyEvent;
import com.coronovirus_stranding.model.AttributeComparator;
import com.coronovirus_stranding.model.AttributesModel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.coronovirus_stranding.activity.MainActivity.CONFIRM;
import static com.coronovirus_stranding.activity.MainActivity.DEATH;


public class DistanceFragment extends BaseFragment {

    @BindView(R.id.tv_distance)
    TextView tvDistance;

    @BindView(R.id.tv_province)
    TextView tvProvince;

    @BindView(R.id.total_confirmed)
    TextView tvTotalConfirmed;

    @BindView(R.id.total_death)
    TextView tvTotalDeath;

    @BindView(R.id.rl_container)
    ViewGroup container;

    private SendDataListener sendDataListener;
    private List<AttributesModel> distanceInKmWithProvince;
    private Map<String, Integer> statistic = new HashMap<>();
    private int totalDeath;
    private int totalConfirmed;

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
        statistic = sendDataListener.getStatistic();
        if (distanceInKmWithProvince != null) {
            totalConfirmed = statistic.get(CONFIRM);
            totalDeath = statistic.get(DEATH);
            setDataInView();
        }
    }

    private void setDataInView() {
        Collections.sort(distanceInKmWithProvince, new AttributeComparator());
        long distance = Math.round(distanceInKmWithProvince.get(0).getDistance());
        SharedPref.getInstance().saveUserDistance(distance);
        tvDistance.setText("Your distance from coronovirus is " + distance);
        tvProvince.setText("The closest province is " + distanceInKmWithProvince.get(0).getCountryRegion());
        tvTotalDeath.setText("Total death: " + totalDeath);
        tvTotalConfirmed.setText("Total confirmed: " + totalConfirmed);
        if (distance > 1000) {
            container.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        } else if (distance > 500) {
            container.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
        } else if (distance > 200) {
            container.setBackgroundColor(getResources().getColor(android.R.color.holo_purple));
        } else {
            container.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
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
