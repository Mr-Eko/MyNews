package com.example.mynews1.Utils;

import com.example.mynews1.Models.ArticleSearch;
import com.example.mynews1.Models.MostPopular;
import com.example.mynews1.Models.TopStories;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface NyTimesApiService {

    String api_key = "3RcyvQUAwAjgpsQ1EuIrGml29G6bvvrS";

    String URL = "https://api.nytimes.com/svc/";

    //    Makes the http request to fetch results on TopStories API
    @GET("topstories/v2/{section}.json")
    Observable<TopStories> getTopStoriesArticles(@Path("section") String subject, @Query("api-key") String api_key);

    //    Makes the http request to fetch results on MostPopular API
    @GET("mostpopular/v2/viewed/7.json")
    Observable<MostPopular> getMostPopularArticles(@Query("api-key") String api_key);

    //    Makes the http request to fetch results on Search API
    @GET("search/v2/articlesearch.json")
    Observable<ArticleSearch> getSearchArticles(@QueryMap Map<String, String> map, @Query("api-key") String api_key);

    //    Configures Retrofit with the base url, the Gson converter and the RxJava call Adapter
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();
}
