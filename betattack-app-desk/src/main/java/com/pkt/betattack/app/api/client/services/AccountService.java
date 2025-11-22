package com.pkt.betattack.app.api.client.services;

import com.pkt.betattack.app.api.pojo.account.Account;
import com.pkt.betattack.app.api.pojo.attack.AttackRequest;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface AccountService {
    @Headers("Content-Type: application/json")
    @GET("accounts/all")
    Call<List<Account>> getAll();


}
