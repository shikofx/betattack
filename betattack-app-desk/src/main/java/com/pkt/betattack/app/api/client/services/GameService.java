package com.pkt.betattack.app.api.client.services;

import com.pkt.betattack.app.api.pojo.game.Game;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

import java.util.List;

public interface GameService {
    @Headers("Content-Type: application/json")
    @GET("game/all/{hours}")
    Call<List<Game>> getAll(@Path("hours") Integer hours);
}

