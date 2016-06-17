package com.rohitsuratekar.NCBSinfo.common;

import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Utilities {

    public String timeStamp(){
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss a d MMM yy", Locale.getDefault());
        return formatter.format(new Date());
    }

    public String[] stringToarray(String input){
        List<String> output = new ArrayList<>();
        input = input.replace("{","");
        input = input.replace("}","");
        String[] split = input.split(",");
        for (String s: split){
            s = s.replace("\"","");
            output.add(s.replaceAll("\\s+",""));
        }
        return output.toArray(new String[output.size()]);
    }

    public boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
