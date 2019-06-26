package com.example.mynews1.Utils;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.widget.Toast;

import androidx.core.app.NotificationCompat;


import com.example.mynews1.Models.ArticleSearch;
import com.example.mynews1.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;


public class NotificationReceiver extends BroadcastReceiver {

    private Intent mIntent;
    private Context mContext;
    private int nbNews;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        mIntent = intent;
        executeSearchHttpRequest();
        Toast.makeText(mContext,"On Receive Ok",Toast.LENGTH_SHORT).show();
    }

    private void executeSearchHttpRequest() {
        String query = mIntent.getStringExtra(Constants.QUERY);
        String filterQuery = mIntent.getStringExtra(Constants.FILTERQUERY);
        List<ArticleSearch.Doc> docList = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("q", query);
        map.put("fq", filterQuery);
        map.put("sort","newest");
        Disposable mDisposable = NyTimesApiStreams.streamSearchArticles(map)
                .subscribeWith(new DisposableObserver<ArticleSearch>() {
                    @Override
                    public void onNext(ArticleSearch article) {
                        docList.addAll(article.getResponse().getDocs());
                        nbNews = docList.size();
                        if (nbNews != 0) {
                        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

                        int notificationId = 1;
                        String channelId = "channel-01";
                        String channelName = "Channel Name";
                        int importance = NotificationManager.IMPORTANCE_HIGH;

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            NotificationChannel mChannel = new NotificationChannel(
                                    channelId, channelName, importance);
                            notificationManager.createNotificationChannel(mChannel);
                        }

                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, channelId)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle(mContext.getString(R.string.have_a_good_news))
                                .setContentText(nbNews + " articles are available");

                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
                        stackBuilder.addNextIntent(mIntent);
                        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                        mBuilder.setContentIntent(resultPendingIntent);

                        notificationManager.notify(notificationId, mBuilder.build());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

}
