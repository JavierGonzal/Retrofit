package com.thedeveloperworldisyours.simpleretrofic.webservice;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;
import android.os.Handler;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.thedeveloperworldisyours.simpleretrofic.model.Question;
import com.thedeveloperworldisyours.simpleretrofic.utils.Constants;

public class RestClient {

	public interface ClientInterface{
		  @GET("/questions")
		  void getQuestions( @Query("sort") String sort, @Query("site") String site,@Query("pagesize") String pagesize,@Query("page") String page, Callback<Question> callback);
		}
	
	public static ClientInterface initRestAdapter()
    {
        OkHttpClient client = new OkHttpClient();

        return (ClientInterface) new RestAdapter.Builder()
                .setClient(new OkClient(client))
                .setEndpoint(Constants.URL)
                .build()
                .create(ClientInterface.class);
    }
	public static void GetQuestions(final Handler mHandler) {
		Callback<Question> callback = new Callback<Question>() {

			@Override
			public void failure(RetrofitError resp) {
				Log.v("failure", String.valueOf(resp.getMessage()));
			}

			@Override
			public void success(Question info, Response resp) {
				Log.v("success", String.valueOf(resp.getStatus()));
			}
		};
		RestClient.initRestAdapter().getQuestions(Constants.SORT, Constants.SITE,
				Constants.PAGESIZE, Constants.PAGE, callback);
	}

}