package com.pkt.betattack.app.api.client.controllers;

import static com.pkt.betattack.app.api.config.AuthConfig.responceError;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pkt.betattack.app.api.client.services.UserService;
import com.pkt.betattack.app.api.config.AuthConfig;
import com.pkt.betattack.app.api.pojo.user.LoginResponse;
import com.pkt.betattack.app.api.pojo.user.User;
import com.pkt.betattack.app.desk.controllers.settings.user.UserEditorController;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.List;


public class UserApiController {

    public LoginResponse login(User user) throws IOException {
        LoginResponse loginResponse = new LoginResponse();
        UserService request = buildRequest();
        Call<LoginResponse> call = request.login(user);
        Response<LoginResponse> response = call.execute();
        if (response.isSuccessful()) {
            return response.body();
        }
        responceError = response.raw();
        return loginResponse;
    }

    public void logout() {
        UserService request = buildRequest();
        Call<LoginResponse> call = request.logout(AuthConfig.currentTokenString);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });
    }

    public void logoutAll() {
        UserService request = buildRequest();
        Call call = request.logoutAll(AuthConfig.currentTokenString);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });
    }

    public User getUser() {
        UserService request = buildRequest();
        Call<User> call = request.getUser(AuthConfig.currentTokenString);
        try {
            Response<User> response = call.execute();
            return response.body();
        } catch (IOException e) {
            AuthConfig.logErrorMessage = e.getMessage();
        }
        return null;
    }

    public void getUsers(UserEditorController controller) {
        UserService request = buildRequest();
        Call<List<User>> call = request.getUsers(AuthConfig.currentTokenString);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                List<User> users = response.body();
                AuthConfig.userList = users;
                controller.fillUserList(users);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                AuthConfig.logErrorMessage = t.getMessage();
            }
        });
    }

    public void addUser(UserEditorController controller, User user) {
        UserService request = buildRequest();
        Call<User> call = request.addUser(AuthConfig.currentTokenString, user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 201) {
                    getUsers(controller);
                    controller.fillUserData(response.body());
                } else {
                    AuthConfig.logErrorMessage = response.message();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                AuthConfig.logErrorMessage = t.getMessage();
            }
        });
    }

    public void deleteUser(UserEditorController controller, User user) {
        UserService request = buildRequest();
        Call<User> call = request.deleteUser(AuthConfig.currentTokenString, user.getId());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    getUsers(controller);
                    controller.fillDefaultData();
                } else {
                    AuthConfig.logErrorMessage = response.message();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    public void updateUser(UserEditorController controller, User user) {
        UserService request = buildRequest();
        User userForUpdate = new User();
        userForUpdate.setName(user.getName());
        userForUpdate.setPassword(user.getPassword().equals("") ? null : user.getPassword());
        String _id = user.getId();
        Call<User> call = request.updateUser(AuthConfig.currentTokenString, _id, userForUpdate);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    if (user.getId().equals(AuthConfig.currentUser.getId())) {
                        AuthConfig.currentUser = response.body();
                    }
                    getUsers(controller);
                    controller.fillUserData(response.body());
                } else {
                    AuthConfig.logErrorMessage = response.message();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private UserService buildRequest() {
        Gson gson = new GsonBuilder()
            .setLenient()
            .create();

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(AuthConfig.DATA_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

        return retrofit.create(UserService.class);
    }
}

