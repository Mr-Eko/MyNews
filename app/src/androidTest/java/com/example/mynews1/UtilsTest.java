package com.example.mynews1;

import android.widget.CheckBox;

import com.example.mynews1.Utils.Utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UtilsTest {

    @Mock
    CheckBox cb1,cb2,cb3,cb4,cb5,cb6;

    @Test
    public void convertDateForDisplayTest(){
        String date = "2019-03-28T06:00:06-04:00";

        assertEquals("28/03/2019",Utils.convertDateForDisplay(date));

    }

    @Test
    public void convertDateForQuery(){
        String date = "2019-03-28T06:00:06-04:00";

        assertEquals("28/03/2019",Utils.convertDateForQuery(date));

    }


    @Test
    public void convertTitleToIdTest(){
        String initialtitle = "Trump Administration Could Blacklist China's Hikvision, a Surveillance Firm";
        assertEquals("Trump Administration", Utils.convertTitle(initialtitle));
        initialtitle = "Trump Administration";
        assertEquals("Trump Administration",Utils.convertTitle(initialtitle));
    }

    @Test
    public void configureQueries(){
        MockitoAnnotations.initMocks(this);

        assertEquals("news_desk:()",Utils.configureFilterQueries(cb1,cb2,cb3,cb4,cb5,cb6));
        when(cb1.isChecked()).thenReturn(true);
        assertEquals("news_desk:(\"Arts\" )",Utils.configureFilterQueries(cb1,cb2,cb3,cb4,cb5,cb6));

        when(cb2.isChecked()).thenReturn(true);
        assertEquals("news_desk:(\"Arts\" \"Business\" )",Utils.configureFilterQueries(cb1,cb2,cb3,cb4,cb5,cb6));

        when(cb3.isChecked()).thenReturn(true);
        when(cb4.isChecked()).thenReturn(true);
        assertEquals("news_desk:(\"Arts\" \"Business\" \"Politics\" \"Sports\" )",Utils.configureFilterQueries(cb1,cb2,cb3,cb4,cb5,cb6));

        when(cb5.isChecked()).thenReturn(true);
        when(cb6.isChecked()).thenReturn(true);
        assertEquals("news_desk:(\"Arts\" \"Business\" \"Politics\" \"Sports\" \"Entrepreneurs\" \"Travel\" )",Utils.configureFilterQueries(cb1,cb2,cb3,cb4,cb5,cb6));
    }


}
