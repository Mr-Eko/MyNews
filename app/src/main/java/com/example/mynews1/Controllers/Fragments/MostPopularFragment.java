package com.example.mynews1.Controllers.Fragments;


import androidx.fragment.app.Fragment;
import android.util.Log;

import com.example.mynews1.Models.MostPopular;
import com.example.mynews1.Models.NYTimesNews;
import com.example.mynews1.Utils.NyTimesApiStreams;
import com.example.mynews1.Utils.Utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * A simple {@link Fragment} subclass.
 */
public class MostPopularFragment extends BaseFragment {

    public MostPopularFragment() {
        // Required empty public constructor
    }

    public static MostPopularFragment newInstance() {
        return (new MostPopularFragment());
    }

    /**
     * Subscribes to the Observable returned by NyTimesStream method and update UI when onNext is called
     */
    protected void executeHttpRequest() {
        mDisposable = NyTimesApiStreams.streamMostPopularArticles()
                .subscribeWith(new DisposableObserver<MostPopular>() {
                    @Override
                    public void onNext(MostPopular mostPopular) {
                        UpdateUI();
                        createListMostPopular(mResultList, mostPopular);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG","On Error"+Log.getStackTraceString(e));
                    }

                    @Override
                    public void onComplete() {
                        Log.e("TAG","On Complete Most Popular");
                    }
                });
    }

    //  Dispose subscription
    private void disposeWhenDestroy(){
        if (this.mDisposable != null && !this.mDisposable.isDisposed()) this.mDisposable.dispose();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposeWhenDestroy();
    }

}
