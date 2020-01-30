package com.example.coron_stranding.api;

import com.example.coron_stranding.event.DataSendEvent;
import com.example.coron_stranding.model.Example;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Api {

    public static void getAttributes() {
        ApiManager.getInstance().getApiService().getAttributes().enqueue(new Callback<Example>() {
            @Override
            public void onResponse(@NotNull Call<Example> call, @NotNull Response<Example> response) {
                if (response.isSuccessful() && response.body() != null) {
                    EventBus.getDefault().post(new DataSendEvent(response.body()));
                }

            }

            @Override
            public void onFailure(@NotNull Call<Example> call, @NotNull Throwable t) {
                System.out.println(t.getLocalizedMessage());
            }
        });
    }
}
