package com.example.mynews1.Controllers.Fragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import io.reactivex.observers.DisposableObserver;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mynews1.Models.TopStories;
import com.example.mynews1.R;
import com.example.mynews1.Utils.NyTimesApiStreams;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArtsFragment extends BaseFragment {

    public ArtsFragment() {
        // Required empty public constructor
    }

    @Override
    protected void executeHttpRequest() {
        mDisposable = NyTimesApiStreams.streamTopStoriesArticles("arts")
                .subscribeWith(new DisposableObserver<TopStories>() {
                    @Override
                    public void onNext(TopStories article) {
                        UpdateUI();
                        createListTopStories(mResultList, article);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG","On Error"+Log.getStackTraceString(e));
                    }

                    @Override
                    public void onComplete() {
                        Log.e("TAG","On Complete Top Stories Arts");
                    }
                });
    }


    public static ArtsFragment newInstance() {
        return (new ArtsFragment());
    }
}
