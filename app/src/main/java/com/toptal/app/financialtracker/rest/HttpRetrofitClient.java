package com.toptal.app.financialtracker.rest;

import android.content.Context;

import com.google.gson.Gson;

import com.toptal.app.financialtracker.entities.Expense;
import com.toptal.app.financialtracker.entities.User;
import com.toptal.app.financialtracker.persistence.PrefsHelper;

import java.io.IOException;
import java.util.ArrayList;


import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Class that handle http client.
 */
public class HttpRetrofitClient {
    /**
     * Api url.
     */
//    public static final String API_URL = "http://10.100.100.155:3000"; // Tests
    public static final String API_URL = "https://financialtrackerserver.herokuapp.com"; // Tests
    /**
     * Rest Adapter.
     */
    private Retrofit mRetrofit;
    /**
     * The context.
     */
    private Context mContext;
    /**
     * BackOffice API.
     */
    public BackOfficeAPI mClient;
    /**
     * Gson.
     */
    private Gson gson = new Gson();

    /**
     * BackOfficeAPI interface.
     */
    public interface BackOfficeAPI {
        /**
         * Method to get expenses on the user role.
         *
         * @return expenses.
         */
        @GET("/expense")
        Call<ArrayList<Expense>> getExpenses();

        /**
         * Method to login on server.
         *
         * @param username to login
         * @param password to login
         */
        @FormUrlEncoded
        @POST("/login/process")
        Call<User> login(
                @Field("email") String username,
                @Field("password") String password);


        /**
         * Method to add a expense.
         *
         * @return created expense.
         */
        @POST("/expense/add")
        Call<Expense> addExpense(
                @Body Expense expense
        );

        /**
         * Method to edit a expense.
         *
         * @return edited expense.
         */
        @POST("/expense/update")
        Call<Expense> editExpense(
                @Body Expense expense
        );

        /**
         * Method to remove a expense.
         *
         * @return removed expense.
         */
        @POST("/expense/remove")
        Call<Expense> removeExpense(
                @Body Expense expense
        );

        /**
         * Method to add a user.
         *
         * @return created user.
         */
        @POST("/user/add")
        Call<User> addUser(
                @Body User user
        );

    }

    /**
     * Class constructor.
     *
     */
    public HttpRetrofitClient(final Context context) {
        mContext = context;

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new AddCookiesInterceptor())
                .addInterceptor(interceptor)
                .build();



        mRetrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mClient = mRetrofit.create(BackOfficeAPI.class);
    }


    /**
     * This interceptor put all the Cookies in Preferences in the Request.
     * Your implementation on how to get the Preferences MAY VARY.
     * <p>
     * Created by tsuharesu on 4/1/15.
     */
    public class AddCookiesInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
//            Request.Builder builder = chain.request().newBuilder();

            Request.Builder builder = chain.request().newBuilder()
                    .method(chain.request().method(), chain.request().body());


            if (!PrefsHelper.getCookie(mContext).equals("")) {
                builder.addHeader("Cookie", PrefsHelper.getCookie(mContext));
            }
            return chain.proceed(builder.build());
        }
    }


}
