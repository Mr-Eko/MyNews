package com.example.mynews1.Utils;

import com.example.mynews1.Models.ArticleSearch;
import com.example.mynews1.Models.MostPopular;
import com.example.mynews1.Models.TopStories;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NyTimesApiStreams {

    private static NyTimesApiService mNyTimesServiceAPI = NyTimesApiService.retrofit.create(NyTimesApiService.class);

    public static Observable<TopStories> streamTopStoriesArticles(String subject) {
        return mNyTimesServiceAPI.getTopStoriesArticles(subject,NyTimesApiService.api_key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    public static Observable<MostPopular> streamMostPopularArticles() {
        return mNyTimesServiceAPI.getMostPopularArticles(NyTimesApiService.api_key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10,TimeUnit.SECONDS);
    }

    public static Observable<ArticleSearch> streamSearchArticles(Map<String,String> map) {
        return mNyTimesServiceAPI.getSearchArticles(map,NyTimesApiService.api_key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10,TimeUnit.SECONDS);
    }
}

