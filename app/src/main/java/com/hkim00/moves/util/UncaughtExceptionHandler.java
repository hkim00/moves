package com.hkim00.moves.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;

import com.hkim00.moves.ShowErrorActivity;

import java.io.PrintWriter;
import java.io.StringWriter;

public class UncaughtExceptionHandler implements java.lang.Thread.UncaughtExceptionHandler {
    // add the line below to Activities that will utilize this exception handler
    // Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler(this));
    private final Activity contextActivity;
    private final String LINE_SEPARATOR = "\n";

    public UncaughtExceptionHandler(Activity context) {
        contextActivity = context;
    }

    public void uncaughtException(Thread thread, Throwable exception) {
        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        StringBuilder errorReport = new StringBuilder();
        errorReport.append("************ CAUSE OF ERROR ************\n\n");
        errorReport.append(stackTrace.toString());

        errorReport.append("\n************ DEVICE INFORMATION ***********\n");
        errorReport.append("Brand: ");
        errorReport.append(Build.BRAND);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Device: ");
        errorReport.append(Build.DEVICE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Model: ");
        errorReport.append(Build.MODEL);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Id: ");
        errorReport.append(Build.ID);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Product: ");
        errorReport.append(Build.PRODUCT);
        errorReport.append(LINE_SEPARATOR);

        Intent intent = new Intent(contextActivity, ShowErrorActivity.class);
        intent.putExtra("error", errorReport.toString());
        contextActivity.startActivity(intent);

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

}