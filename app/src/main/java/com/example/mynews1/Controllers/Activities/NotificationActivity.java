package com.example.mynews1.Controllers.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.mynews1.Utils.Constants;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mynews1.R;
import com.example.mynews1.Utils.NotificationReceiver;
import com.example.mynews1.Utils.Utils;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.mynews1.Utils.Constants.ARTS;
import static com.example.mynews1.Utils.Constants.BUSINESS;
import static com.example.mynews1.Utils.Constants.ENTREPRENEURS;
import static com.example.mynews1.Utils.Constants.FILTERQUERY;
import static com.example.mynews1.Utils.Constants.POLITICS;
import static com.example.mynews1.Utils.Constants.PREFERENCES;
import static com.example.mynews1.Utils.Constants.QUERY;
import static com.example.mynews1.Utils.Constants.SPORT;
import static com.example.mynews1.Utils.Constants.SWITCH;
import static com.example.mynews1.Utils.Constants.TRAVEL;

public class NotificationActivity extends AppCompatActivity {

    @BindView(R.id.query_term)
    EditText mQueryTerm;
    @BindView(R.id.begin_date)
    TextInputEditText mBeginDate;
    @BindView(R.id.end_date)
    TextInputEditText mEndDate;
    @BindView(R.id.checkbox_arts)
    CheckBox mCheckboxArts;
    @BindView(R.id.checkbox_business)
    CheckBox mCheckboxBusiness;
    @BindView(R.id.checkbox_entrepreneurs)
    CheckBox mCheckboxEntrepreneurs;
    @BindView(R.id.checkbox_politics)
    CheckBox mCheckboxPolitics;
    @BindView(R.id.checkbox_sports)
    CheckBox mCheckboxSport;
    @BindView(R.id.checkbox_travel)
    CheckBox mCheckboxTravel;
    @BindView(R.id.switch_notification)
    SwitchCompat mSwitchNotification;
    @BindView(R.id.search_query_button)
    Button mSearchQueryButton;
    @BindView(R.id.dates)
    LinearLayout mDates;

    private PendingIntent mPendingIntent;

    private int mNbChecked;
    private String mQuery;
    private String mFilterQuery;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        mSharedPreferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        ButterKnife.bind(this);
        configureToolbar();
        configureLayout();
    }

    /**
     * SearchActivity and NotificationActivity use a common layout.
     * Disables views that are no needed in this activity and update UI if the switch's state in
     * SharedPreferences is checked.
     */
    private void configureLayout() {
        mSearchQueryButton.setVisibility(View.GONE);
        mBeginDate.setVisibility(View.GONE);
        mEndDate.setVisibility(View.GONE);
        mDates.setVisibility(View.GONE);
        mSwitchNotification.setChecked(mSharedPreferences.getBoolean(SWITCH, false));
        if (mSwitchNotification.isChecked()) {
            upDateUiWithSharedPreferencesValues();
        }
    }

    /**
     * Updates UI according to booleans save in SharedPreferences when user click on the switch.
     */
    private void upDateUiWithSharedPreferencesValues() {
        mQueryTerm.setText(mSharedPreferences.getString(QUERY, ""));
        mCheckboxArts.setChecked(mSharedPreferences.getBoolean(ARTS, false));
        mCheckboxPolitics.setChecked(mSharedPreferences.getBoolean(POLITICS, false));
        mCheckboxBusiness.setChecked(mSharedPreferences.getBoolean(BUSINESS, false));
        mCheckboxSport.setChecked(mSharedPreferences.getBoolean(SPORT, false));
        mCheckboxEntrepreneurs.setChecked(mSharedPreferences.getBoolean(ENTREPRENEURS, false));
        mCheckboxTravel.setChecked(mSharedPreferences.getBoolean(TRAVEL, false));
    }

    /**
     * Configures toolbar as ActionBar and allows to display Up item
     */
    private void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.notifications_toolbar_title);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Configures all needed queries to the http request to NyTimes Search API
     */
    private void configureQueries() {
        mNbChecked = 0;
        mQuery = mQueryTerm.getText().toString();
        mFilterQuery = Utils.configureFilterQueries(mCheckboxArts, mCheckboxBusiness, mCheckboxEntrepreneurs, mCheckboxPolitics, mCheckboxSport, mCheckboxTravel);
        verifyIfOneCheckboxIsChecked();
    }

    /**
     * Verifies if one CheckBox is checked before send an intent.
     */
    private void verifyIfOneCheckboxIsChecked() {
        if (mCheckboxArts.isChecked())
            mNbChecked++;
        if (mCheckboxBusiness.isChecked())
            mNbChecked++;
        if (mCheckboxPolitics.isChecked())
            mNbChecked++;
        if (mCheckboxEntrepreneurs.isChecked())
            mNbChecked++;
        if (mCheckboxSport.isChecked())
            mNbChecked++;
        if (mCheckboxTravel.isChecked())
            mNbChecked++;
    }

    /**
     * Saves in SharedPreferences the UI's state to restore it after if mSwitchNotification is checked
     */
    private void saveInSharedPreferencesQueryParameters() {
        mSharedPreferences.edit().putBoolean(Constants.SWITCH, mSwitchNotification.isChecked()).apply();
        mSharedPreferences.edit().putString(Constants.QUERY, mQuery).apply();
        mSharedPreferences.edit().putBoolean(Constants.ARTS, mCheckboxArts.isChecked()).apply();
        mSharedPreferences.edit().putBoolean(Constants.POLITICS, mCheckboxPolitics.isChecked()).apply();
        mSharedPreferences.edit().putBoolean(Constants.BUSINESS, mCheckboxBusiness.isChecked()).apply();
        mSharedPreferences.edit().putBoolean(Constants.SPORT, mCheckboxSport.isChecked()).apply();
        mSharedPreferences.edit().putBoolean(Constants.ENTREPRENEURS, mCheckboxEntrepreneurs.isChecked()).apply();
        mSharedPreferences.edit().putBoolean(Constants.TRAVEL, mCheckboxTravel.isChecked()).apply();
    }

    /**
     * Displays Toast if EditText or Checkbox are empty or unchecked.
     * If it's OK, save the UI's state in SharedPreferences.
     */
    @OnClick(R.id.switch_notification)
    public void onViewClicked() {
        configureQueries();
        if (mQuery.isEmpty()) {
            Toast.makeText(this, R.string.query_term_empty, Toast.LENGTH_SHORT).show();
            mSwitchNotification.setChecked(false);
        } else if (mNbChecked == 0) {
            Toast.makeText(this, R.string.checkbox_selected, Toast.LENGTH_SHORT).show();
            mSwitchNotification.setChecked(false);
        }
        configureAlarmQuery();
        saveInSharedPreferencesQueryParameters();
    }

    /**
     * Starts the AlarmManager's repeating when user turn on the notification's switch and stop it when
     * user turn off the switch.
     */
    private void configureAlarmQuery() {
        configureAlarmManager();
        if (mSwitchNotification.isChecked())
            startAlarm();
        else
            stopAlarm();
    }

    /**
     * Sets mPendingIntent to use intent defined here with the NotificationReceiver.class.
     */
    private void configureAlarmManager() {
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra(QUERY, mQuery);
        intent.putExtra(FILTERQUERY, mFilterQuery);
        mPendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Configures the AlarmManager to repeat at 12:00 every day
     */
    private void startAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 00);
        //calendar.set(Calendar.SECOND, 00);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, mPendingIntent);
    }

    /**
     * Stop the AlarmManager's repeating
     */
    private void stopAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(mPendingIntent);
    }
}
