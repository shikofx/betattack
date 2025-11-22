package com.pkt.betattack.app.api.client.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pkt.betattack.app.api.client.services.AccountService;
import com.pkt.betattack.app.api.config.AuthConfig;
import com.pkt.betattack.app.api.pojo.account.Account;
import com.pkt.betattack.app.desk.controllers.main.LiveController;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

public class AccountApiController {
    private AccountService buildRequest() {
        Gson gson = new GsonBuilder()
            .setLenient()
            .create();

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(AuthConfig.DATA_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

        return retrofit.create(AccountService.class);
    }

    public void getAll(LiveController controller) {
        AccountService request = buildRequest();
        Call<List<Account>> call = request.getAll();
        call.enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                AuthConfig.attackAccounts = response.body();
            }

            @Override
            public void onFailure(Call<List<Account>> call, Throwable throwable) {

            }
        });
    }

}
