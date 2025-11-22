package com.pkt.betattack.app.api.client.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pkt.betattack.app.api.client.services.GameService;
import com.pkt.betattack.app.api.config.AuthConfig;
import com.pkt.betattack.app.api.pojo.game.Game;
import com.pkt.betattack.app.desk.controllers.main.LiveController;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GameApiController {

    private GameService buildRequest() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AuthConfig.DATA_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(GameService.class);
    }

    public void getAll(LiveController controller, Integer hours) {
        GameService request = buildRequest();
        Call<List<Game>> call = request.getAll(hours);
        call.enqueue(new Callback<List<Game>>() {
            @Override
            public void onResponse(Call<List<Game>> call, Response<List<Game>> response) {
                List<Game> games = new ArrayList<>();
                if(response.body() != null)
                    games = response.body().stream().filter(game -> game.getIsFinished() == false).collect(
                        Collectors.toList());
                if (games.size() > 0)
                    games.sort(new Comparator<Game>() {
                        @Override
                        public int compare(Game o1, Game o2) {
                            return o1.getTimer().compareTo(o2.getTimer());
                        }
                    });
//                try {
                    controller.updateGames(games);
//                }
//                catch (IOException e) {
//                    controller.setHeaderText("Live" + "\nNo connection with DB");
//                }
            }

            @Override
            public void onFailure(Call<List<Game>> call, Throwable t) {
                AuthConfig.logErrorMessage = t.getMessage();
            }
        });
    }
}

