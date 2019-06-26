package com.example.mynews1;

import com.example.mynews1.Models.ArticleSearch;
import com.example.mynews1.Models.MostPopular;
import com.example.mynews1.Models.TopStories;
import com.example.mynews1.Utils.NyTimesApiStreams;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.observers.TestObserver;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class NyTimesApiStreamsTest {

    // Strings for test queries in http request in Search API
    private String beginDate = "20180101";
    private String endDate = "20190301";

    @BeforeClass
    public static void setUpRxSchedulers() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> Schedulers.trampoline());
    }

    @Test
    public void streamTopStoriesArticle() {
        String path = "home";
        Observable<TopStories> articleTopObservable = NyTimesApiStreams.streamTopStoriesArticles(path);
        TestObserver<TopStories> articleTopTestObserver = new TestObserver<>();

        articleTopObservable.subscribeWith(articleTopTestObserver)
                .assertNoErrors()
                .assertNoTimeout()
                .awaitTerminalEvent();

        assertNotEquals(0, articleTopTestObserver.values().size());

        TopStories article = articleTopTestObserver.values().get(0);
        TopStories.Result result = article.getResults().get(0);

        assertEquals("OK", article.getStatus());
        assertEquals(path, article.getSection());

        assertNotNull(result.getTitle());
        assertNotNull(result.getUrl());
        assertNotNull(result.getSection());
        assertNotNull(result.getSubsection());
        assertNotNull(result.getPublishedDate());
    }

    @Test
    public void streamMostPopularArticle() {

        Observable<MostPopular> articleTopObservable = NyTimesApiStreams.streamMostPopularArticles();
        TestObserver<MostPopular> articleTopTestObserver = new TestObserver<>();

        articleTopObservable.subscribeWith(articleTopTestObserver)
                .assertNoErrors()
                .assertNoTimeout()
                .awaitTerminalEvent();

        assertNotEquals(0, articleTopTestObserver.values().size());

        MostPopular article = articleTopTestObserver.values().get(0);
        MostPopular.Result result = article.getResults().get(0);

        assertEquals("OK", article.getStatus());

        assertNotNull(result.getTitle());
        assertNotNull(result.getUrl());
        assertNotNull(result.getSection());
        assertNotNull(result.getPublishedDate());
    }


    @Test
    public void streamFetchSearchArticles() {
        Map<String, String> map = new HashMap<>();
        map.put("q", "nba");
        map.put("fq", "( \"sport\")");
        map.put("beginDate", beginDate);
        map.put("endDate", endDate);
        Observable<ArticleSearch> articleSearchObservable = NyTimesApiStreams.streamSearchArticles(map);
        TestObserver<ArticleSearch> articleSearchTestObserver = new TestObserver<>();

        articleSearchObservable.subscribeWith(articleSearchTestObserver)
                .assertNoErrors()
                .assertNoTimeout()
                .awaitTerminalEvent();

        assertNotEquals(0, articleSearchTestObserver.values().size());

        ArticleSearch articleSearch = articleSearchTestObserver.values().get(0);
        List<ArticleSearch.Doc> docList = articleSearch.getResponse().getDocs();
        ArticleSearch.Doc doc = articleSearch.getResponse().getDocs().get(0);

        assertEquals("OK", articleSearch.getStatus());

        assertNotNull(doc.getPubDate());
        assertNotNull(doc.getHeadline().getMain());
        assertNotNull(doc.getSectionName());
        assertNotNull(doc.getWebUrl());
        assertNotNull(doc.getSource());

    }
}
