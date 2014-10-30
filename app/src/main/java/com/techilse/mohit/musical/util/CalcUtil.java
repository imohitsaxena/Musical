package com.techilse.mohit.musical.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by Mohit on 30-10-2014.
 */
public class CalcUtil {

    static DecimalFormat df = new DecimalFormat("00");

    public static String getFormattedTime(int milliSecond) {
        int seconds = milliSecond / 1000;
        int minutes = seconds / 60;
        seconds = seconds - minutes * 60;
        return df.format(minutes) + ":" + df.format(seconds);
    }

    public static int getPercentage(int currentTime, int totalTime) {
        return (int) ((currentTime * 100.0) / (double) (totalTime));
    }

    public static int getCurrentTimeFromPercentage(int percent, int totalTime) {
        return percent * totalTime / 100;
    }
}
