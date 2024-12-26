package com.example.cityquest.utils;

import com.google.firebase.Timestamp;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;

public class AndroidUtils {


    public static String formatTimeAgo(Timestamp timestamp) {
        PrettyTime prettyTime = new PrettyTime();
        Date date = timestamp.toDate();
        return prettyTime.format(date);  // This will give results like "3 days ago", "a month ago"
    }

}
