package com.example.mynews1.Controllers.Fragments;


import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mynews1.Controllers.Activities.DisplayArticleActivity;
import com.example.mynews1.Models.MostPopular;
import com.example.mynews1.Models.NYTimesNews;
import com.example.mynews1.Models.TopStories;
import com.example.mynews1.Utils.ItemClickSupport;
import com.example.mynews1.Utils.Utils;
import com.example.mynews1.Views.Adapters.ArticlesAdapter;
import com.example.mynews1.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

import static com.example.mynews1.Utils.Constants.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment {

    @BindView(R.id.base_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.base_swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    private List<NYTimesNews> mResultList;
    private ArticlesAdapter mAdapter;
    protected Disposable mDisposable;



    protected abstract void executeHttpRequest();

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base, container, false);
        ButterKnife.bind(this, view);
        configureRecyclerView();
        configureOnClickRecyclerView();
        executeHttpRequest();
        configureSwipeRefreshLayout();
        return view;
    }

    private void configureRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setLayoutManager(layoutManager);
        mResultList = new ArrayList<>();
        mAdapter = new ArticlesAdapter(mResultList, Picasso.get());
        mRecyclerView.setAdapter(mAdapter);
        }



    //    Configure item click on RecyclerView
    private void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(mRecyclerView, R.id.item_layout)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        String article;
                        // 1 - Get user from adapter
                        article = mAdapter.getURL(position);
                        // 2 - Show result in a new WebView
                        Intent intent = new Intent(getActivity(), DisplayArticleActivity.class);
                        intent.putExtra(URL, article);
                        startActivity(intent);
                    }
                });
    }


    //    Configure the SwipeRefreshLayout
    private void configureSwipeRefreshLayout(){
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                executeHttpRequest();
            }
        });
    }

    protected void UpdateUI () {
        swipeRefreshLayout.setRefreshing(false);
        mAdapter.notifyDataSetChanged();
    }

    // Create a list of NYTimesNews with articles from TopStories API
    protected void createListTopStories(List<NYTimesNews> nyTimesNewsList, TopStories article) {
        mAdapter.clearNews();

        for (TopStories.Result mResult : article.getResults()){

            // Create a news
            NYTimesNews news = new NYTimesNews();

            // Add all information to the list
            news.setTitle(mResult.getTitle());
            news.setSection(mResult.getSection());
            news.setUrl(mResult.getUrl());

            // Format Date
            String outputText = Utils.convertDateForDisplay(mResult.getPublishedDate());
            news.setPublishedDate(outputText);

            // Show article thumbnail
            if (mResult.getMultimedia().size() != 0){
                news.setImageURL(mResult.getMultimedia().get(0).getUrl());
            }

            nyTimesNewsList.add(news);

        }
    }

    protected void createListMostPopular(List<NYTimesNews> nyTimesNewsList, MostPopular mostPopular) {

        // Clear the News List
        mAdapter.clearNews();

        for (MostPopular.Result mResult : mostPopular.getResults()){

            // Create a news
            NYTimesNews news = new NYTimesNews();

            // Add all information to the list
            news.setTitle(mResult.getTitle());
            news.setSection(mResult.getSection());
            news.setUrl(mResult.getUrl());

            // Date Format
            String outputText = Utils.convertDateForDisplayMostPopular(mResult.getPublishedDate());
            news.setPublishedDate(outputText);

            // Create Thumbnail

            news.setImageURL(mResult.getMedia().get(0).getMediaMetadata().get(0).getUrl());

            nyTimesNewsList.add(news);
        }
    }

}
