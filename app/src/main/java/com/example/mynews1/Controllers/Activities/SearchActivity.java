package com.example.mynews1.Controllers.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;

import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mynews1.R;
import com.example.mynews1.Utils.Utils;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.mynews1.Utils.Constants.BEGINDATE;
import static com.example.mynews1.Utils.Constants.ENDDATE;
import static com.example.mynews1.Utils.Constants.FILTERQUERY;
import static com.example.mynews1.Utils.Constants.QUERY;

public class SearchActivity extends AppCompatActivity {

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

    private String mQuery;
    private String mFilterQueries;
    private String mBeginQuery;
    private String mEndQuery;
    private int mNbCheckedBoxes;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        configureToolbar();
        mSwitchNotification.setVisibility(View.GONE);
    }

    /**
     * Configures toolbar as ActionBar and allows to display Up item
     */
    private void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.search_toolbar_title));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
    }


    @OnClick({R.id.begin_date, R.id.end_date, R.id.search_query_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.begin_date:
                // Displays a Date Picker when user click and updates mBeginDate when user choose a date
                displayDatePicker(0);
                break;
            case R.id.end_date:
                // Displays a Date Picker when user click and updates mEndDate when user choose a date
                displayDatePicker(1);
                break;
            case R.id.search_query_button:
                // Displays toast if needed queries missing and sends an intent if all queries are filled
                configureQueries();
                if (mQuery.isEmpty())
                    Toast.makeText(this, (R.string.query_term_empty), Toast.LENGTH_SHORT).show();
                else if (mNbCheckedBoxes == 0)
                    Toast.makeText(this, (R.string.checkbox_selected), Toast.LENGTH_SHORT).show();
                else{
                    Intent intent = new Intent(this,SearchResultActivity.class);
                    intent.putExtra(QUERY ,mQuery);
                    intent.putExtra(FILTERQUERY,mFilterQueries);
                    intent.putExtra(BEGINDATE,mBeginQuery);
                    intent.putExtra(ENDDATE,mEndQuery);
                    startActivity(intent);
                }
        }
    }

    /**
     * Creates a DatePickerDialog with different Theme according to the build version. When user set a date in the DatePicker,
     * the corresponding EditText is updated.
     *
     * @param tag A value to set the text in the correct EditText
     */

    private void displayDatePicker (final int tag) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style;

        style = R.style.Theme_AppCompat_DayNight_Dialog;
        DatePickerDialog dialog = new DatePickerDialog(this,style,mDateSetListener, year, month, day);
        dialog.show();

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = year + "-" + month + "-" + dayOfMonth;
                switch (tag) {
                    case 0:
                        mBeginDate.setText(date);
                        break;

                    case 1:
                        mEndDate.setText(date);
                        break;
                }

            }
        };
    }

    /**
     * Configures all needed queries to the http request to NyTimes Search API
     */
    private void configureQueries() {
        mNbCheckedBoxes = 0;
        mQuery = mQueryTerm.getText().toString();
        mBeginQuery = mBeginDate.getText().toString();
        mEndQuery = mEndDate.getText().toString();
        verifyIfOneCheckboxIsChecked();
        mFilterQueries = Utils.configureFilterQueries(mCheckboxArts, mCheckboxBusiness, mCheckboxPolitics, mCheckboxSport, mCheckboxEntrepreneurs, mCheckboxTravel);
    }

    /**
     * Verifies if one CheckBox is checked before send an intent.
     */
    private void verifyIfOneCheckboxIsChecked() {
        if (mCheckboxArts.isChecked())
            mNbCheckedBoxes++;
        if (mCheckboxBusiness.isChecked())
            mNbCheckedBoxes++;
        if (mCheckboxPolitics.isChecked())
            mNbCheckedBoxes++;
        if (mCheckboxEntrepreneurs.isChecked())
            mNbCheckedBoxes++;
        if (mCheckboxSport.isChecked())
            mNbCheckedBoxes++;
        if (mCheckboxTravel.isChecked())
            mNbCheckedBoxes++;
    }
}
