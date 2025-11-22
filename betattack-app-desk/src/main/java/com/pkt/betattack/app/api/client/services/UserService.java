package com.pkt.betattack.app.api.client.services;

import com.pkt.betattack.app.api.pojo.user.LoginResponse;
import com.pkt.betattack.app.api.pojo.user.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

public interface UserService {

    @Headers("Content-Type: application/json")
    @POST("user/login")
    Call<LoginResponse> login(
        @Body
            User user);

    @Headers("Content-Type: application/json")
    @POST("user/logout")
    Call<LoginResponse> logout(
        @Header("Authorization")
            String token);

    @Headers("Content-Type: application/json")
    @POST("user/logoutAll")
    Call<LoginResponse> logoutAll(
        @Header("Authorization")
            String token);

    @Headers("Content-Type: application/json")
    @POST("user")
    Call<User> addUser(
        @Header("Authorization")
            String token,
        @Body
            User user);

    @Headers("Content-Type: application/json")
    @PATCH("user/{id}")
    Call<User> updateUser(
        @Header("Authorization")
            String token,
        @Path("id")
            String userId,
        @Body
            User user);

    @Headers("Content-Type: application/json")
    @DELETE("user")
    Call<User> deleteCurrent(
        @Header("Authorization")
            String token);

    @Headers("Content-Type: application/json")
    @DELETE("user/{id}")
    Call<User> deleteUser(
        @Header("Authorization")
            String token,
        @Path("id")
            String userId);

    @Headers("Content-Type: application/json")
    @GET("users")
    Call<List<User>> getUsers(
        @Header("Authorization")
            String token);

    @Headers("Content-Type: application/json")
    @GET("user")
    Call<User> getUser(
        @Header("Authorization")
            String token);
}
