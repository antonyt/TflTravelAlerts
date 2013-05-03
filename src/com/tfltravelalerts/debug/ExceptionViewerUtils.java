
package com.tfltravelalerts.debug;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import android.os.Environment;
import android.util.Log;

import com.tfltravelalerts.TflApplication;

public class ExceptionViewerUtils {

    private static final String LOG_TAG = ExceptionViewerUtils.class.getName();

    private static final String EXCEPTIONS_LOG_FILEPATH =
            Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator
                    + "Android"
                    + File.separator
                    + "data"
                    + File.separator
                    + TflApplication.getLastInstance().getPackageName()
                    + File.separator
                    + "exceptions.log";

    public static void appendException(Throwable throwable) {
        try {
            File file = new File(EXCEPTIONS_LOG_FILEPATH);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(EXCEPTIONS_LOG_FILEPATH, true);
            String message = buildExceptionMessage(throwable);
            writer.write(message);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            Log.i(LOG_TAG, "Could not write to exceptions log!");
        }
    }
    
    public static String buildExceptionMessage(Throwable throwable) {
        StringBuilder stringBuilder = new StringBuilder();
        String exception = Log.getStackTraceString(throwable);
        stringBuilder.append("Exception date: " + new Date());
        stringBuilder.append('\n');
        stringBuilder.append("===================================================");
        stringBuilder.append('\n');
        stringBuilder.append(exception);
        stringBuilder.append("===================================================");
        stringBuilder.append('\n');
        stringBuilder.append('\n');
        return stringBuilder.toString();
    }

    public static String getExceptions() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            FileReader fileReader = new FileReader(EXCEPTIONS_LOG_FILEPATH);
            char[] buffer = new char[1024];
            while (fileReader.read(buffer) != -1) {
                stringBuilder.append(buffer);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            Log.i(LOG_TAG, "Could not read exceptions log!");
        }
        return null;
    }

    public static void clearLog() {
        File file = new File(EXCEPTIONS_LOG_FILEPATH);
        file.delete();
    }

}
