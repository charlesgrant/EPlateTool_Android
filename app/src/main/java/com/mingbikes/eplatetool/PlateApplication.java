package com.mingbikes.eplatetool;

import android.app.Application;

import com.mingbikes.plate.FileUtils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by cronus-tropix on 17/8/14.
 */

public class PlateApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        final Thread.UncaughtExceptionHandler oldHandler = Thread.getDefaultUncaughtExceptionHandler();

        Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread t, Throwable e) {
                // keep this, when need to report the exception.
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                pw.close();

                FileUtils.writeTxtToFile(sw.toString(), null, null);

                //if there was another handler before
                if (oldHandler != null) {
                    //notify it also
                    oldHandler.uncaughtException(t, e);
                }
            }
        };

        Thread.setDefaultUncaughtExceptionHandler(handler);
    }
}
