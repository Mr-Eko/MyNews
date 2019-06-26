package com.example.mynews1.Controllers.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.mynews1.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.mynews1.Utils.Constants.URL;

public class DisplayArticleActivity extends AppCompatActivity {

    @BindView(R.id.webview)
    WebView mWebView;
    WebViewClient mWebViewClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_article);
        ButterKnife.bind(this);
        mWebViewClient = new WebViewClient();
        mWebView.setWebViewClient(mWebViewClient);
        String url = getIntent().getStringExtra(URL);
        mWebView.loadUrl(url);

        configureToolbar();

    }

    private void configureToolbar() {
        //Get the toolbar (Serialise)
        Toolbar toolbar = findViewById(R.id.toolbar);
        //Set the toolbar
        setSupportActionBar(toolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
    }
}