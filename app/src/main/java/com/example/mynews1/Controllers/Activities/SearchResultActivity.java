package com.example.mynews1.Controllers.Activities;

import android.content.Intent;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.example.mynews1.Models.ArticleSearch;
import com.example.mynews1.Models.NYTimesNews;
import com.example.mynews1.R;
import com.example.mynews1.Utils.ItemClickSupport;
import com.example.mynews1.Utils.NyTimesApiStreams;
import com.example.mynews1.Utils.Utils;
import com.example.mynews1.Views.Adapters.ArticlesAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

import static com.example.mynews1.Utils.Constants.BEGINDATE;
import static com.example.mynews1.Utils.Constants.ENDDATE;
import static com.example.mynews1.Utils.Constants.FILTERQUERY;
import static com.example.mynews1.Utils.Constants.ID;
import static com.example.mynews1.Utils.Constants.QUERY;
import static com.example.mynews1.Utils.Constants.URL;

public class SearchResultActivity extends AppCompatActivity {

    @BindView(R.id.search_result_swipe_layout)
    SwipeRefreshLayout mSearchResultSwipeLayout;
    @BindView(R.id.search_result_recycler_view)
    RecyclerView mRecyclerView;

    private Map<String, String> mQueries;
    private Disposable mDisposable;
    private List<NYTimesNews> nyTimesNewsList = new ArrayList<>();
    private ArticlesAdapter mArticleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ButterKnife.bind(this);
        configureToolBar();
        configureSwipeRefreshLayout();
        configureRecyclerView();
        configureOnClickItemRecyclerView();
        executeSearchHttpRequest();
    }

    /**
     * Configures toolbar as ActionBar and allows to display Up item
     */
    private void configureToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.search_result_toolbar_title));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Adds a divider item decoration in the recycler view between each items and set the adapter
     */
    private void configureRecyclerView() {
        mArticleAdapter = new ArticlesAdapter(nyTimesNewsList, Picasso.get());
        mRecyclerView.setAdapter(mArticleAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    /**
     * Configures OnRefresh to reload the http request when user swipe from top to down.
     */
    private void configureSwipeRefreshLayout() {
        mSearchResultSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                executeSearchHttpRequest();
            }
        });
    }

    /**
     * Fetch all the IntentExtra (from SearchActivity) to set the needed QueryMap to execute the
     * Http request on searchAPI.
     */
    private void getIntentExtra() {
        mQueries = new HashMap<>();
        Intent intent = getIntent();
        String query = intent.getStringExtra(QUERY);
        mQueries.put("q", query);
        String filterQuery = intent.getStringExtra(FILTERQUERY);
        mQueries.put("fq", filterQuery);
        String beginDate;
        String endDate;
        if (!intent.getStringExtra(BEGINDATE).isEmpty()) {
            beginDate = Utils.convertDateForQuery(intent.getStringExtra(BEGINDATE));
            mQueries.put("begin_date", beginDate);
        }
        if (!intent.getStringExtra(ENDDATE).isEmpty()) {
            endDate = Utils.convertDateForQuery(intent.getStringExtra(ENDDATE));
            mQueries.put("end_date", endDate);
        }
    }

    /**
     * Subscribes to the Observable returned by NyTimesStream method and update UI when onNext is called
     */
    private void executeSearchHttpRequest() {
        getIntentExtra();
        mDisposable = NyTimesApiStreams.streamSearchArticles(mQueries)
                .subscribeWith(new DisposableObserver<ArticleSearch>() {
                    @Override
                    public void onNext(ArticleSearch article) {
                            mSearchResultSwipeLayout.setRefreshing(false);
                            mArticleAdapter.notifyDataSetChanged();
                            createListArticleSearch(nyTimesNewsList, article);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    // Create a list of NYTimesNews with articles from Most Popular API
    private void createListArticleSearch(List<NYTimesNews> nyTimesNewsList, ArticleSearch articleSearch) {

        for (ArticleSearch.Doc mResult : articleSearch.getResponse().getDocs()) {

            // Create a news
            NYTimesNews news = new NYTimesNews();

            // Add all information to the list
            news.setTitle(mResult.getSnippet());
            news.setSection(mResult.getSectionName());
            news.setUrl(mResult.getWebUrl());

            String outputText = Utils.convertDateForDisplay(mResult.getPubDate());
            news.setPublishedDate(outputText);

            // Add static url to complete image url path
            if (mResult.getMultimedia().size() != 0){
                news.setImageURL("https://static01.nyt.com/" + mResult.getMultimedia().get(0).getUrl());
            }
            nyTimesNewsList.add(news);
        }
    }


    /**
     * Adds to the RecyclerView the support of the user's item click and send an intent to DisplayArticleActivity
     * with the article's url and title in extra.
     */
    private void configureOnClickItemRecyclerView(){
        ItemClickSupport.addTo(mRecyclerView,R.id.item_layout)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Intent intent = new Intent(SearchResultActivity.this,DisplayArticleActivity.class);
                        intent.putExtra(URL,mArticleAdapter.getURL(position));
                        intent.putExtra(ID,Utils.convertTitleToId(mArticleAdapter.getTitle(position)));
                        startActivity(intent);
                    }
                });
    }

    /**
     * Unsubscribe to the Observable when the fragment is destroyed
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDisposable != null && !mDisposable.isDisposed())
            mDisposable.dispose();
    }
}