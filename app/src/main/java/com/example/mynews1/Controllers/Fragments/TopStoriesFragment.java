package com.example.mynews1.Controllers.Fragments;


import androidx.fragment.app.Fragment;
import android.util.Log;

import com.example.mynews1.Models.NYTimesNews;
import com.example.mynews1.Models.TopStories;
import com.example.mynews1.Utils.NyTimesApiStreams;
import com.example.mynews1.Utils.Utils;


import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopStoriesFragment extends BaseFragment {

    public TopStoriesFragment() {
        //  Required empty public constructor
    }

    public static TopStoriesFragment newInstance() {
        return (new TopStoriesFragment());
    }

    /**
     * Subscribes to the Observable returned by NyTimesStream method and update UI when onNext is called
     */
    protected void executeHttpRequest() {
        mDisposable = NyTimesApiStreams.streamTopStoriesArticles("home")
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
                        Log.e("TAG","On Complete Top Stories");
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
