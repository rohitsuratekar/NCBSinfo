package com.rohitsuratekar.NCBSinfo.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.transport.Routes;
import com.rohitsuratekar.NCBSinfo.activities.transport.routebuilder.RouteInformation;
import com.rohitsuratekar.NCBSinfo.constants.AppConstants;
import com.rohitsuratekar.NCBSinfo.utilities.Converters;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 04-07-16.
 */
public class TransportPref {

    private final String TAG = getClass().getSimpleName();
    SharedPreferences pref;
    Context context;

    String LAST_UPDATE = "transportLastUpdated";

    protected TransportPref(SharedPreferences pref, Context context) {
        this.pref = pref;
        this.context = context;
    }

    public String[] getWeekdayTrips(Routes routes) {
        return new Converters().stringToarray(
                pref.getString(new RouteInformation(routes).get().getWeekDayKey(), context.getString(getDefault(routes, false))));
    }

    public String[] getSundayTrips(Routes routes) {
        return new Converters().stringToarray(
                pref.getString(new RouteInformation(routes).get().getSundayKey(), context.getString(getDefault(routes, true))));
    }


    public void setRoute(Routes route, String weekValue, String sundayValue) {
        pref.edit().putString(new RouteInformation(route).get().getWeekDayKey(), weekValue).apply();
        pref.edit().putString(new RouteInformation(route).get().getSundayKey(), sundayValue).apply();
        Log.i(TAG, route.toString() + " value changed");
    }


    public void resetRoute(Routes routes) {
        Log.i(TAG, routes.toString() + " reset");
        pref.edit().putString(new RouteInformation(routes).get().getWeekDayKey(), context.getString(getDefault(routes, false))).apply();
        pref.edit().putString(new RouteInformation(routes).get().getSundayKey(), context.getString(getDefault(routes, true))).apply();
    }


    private int getDefault(Routes routes, boolean isSunday) {
        switch (routes) {
            case NCBS_IISC:
                if (!isSunday) {
                    return R.string.def_ncbs_iisc_week;
                } else {
                    return R.string.def_ncbs_iisc_sunday;
                }
            case IISC_NCBS:
                if (!isSunday) {
                    return R.string.def_iisc_ncbs_week;
                } else {
                    return R.string.def_iisc_ncbs_sunday;
                }
            case NCBS_MANDARA:
                if (!isSunday) {
                    return R.string.def_ncbs_mandara_week;
                } else {
                    return R.string.def_ncbs_mandara_sunday;
                }
            case MANDARA_NCBS:
                if (!isSunday) {
                    return R.string.def_mandara_ncbs_week;
                } else {
                    return R.string.def_mandara_ncbs_sunday;
                }
            case BUGGY_FROM_NCBS:
                return R.string.def_buggy_from_ncbs;
            case BUGGY_FROM_MANDARA:
                return R.string.def_buggy_from_mandara;
            case NCBS_ICTS:
                if (!isSunday) {
                    return R.string.def_ncbs_icts_week;
                } else {
                    return R.string.def_ncbs_icts_sunday;
                }
            case ICTS_NCBS:
                if (!isSunday) {
                    return R.string.def_icts_ncbs_week;
                } else {
                    return R.string.def_icts_ncbs_sunday;
                }
            case NCBS_CBL:
                return R.string.def_ncbs_cbl;
            default:
                return R.string.transport_default_trip;
        }
    }

    public String getLastUpdate() {
        if (new App(pref, context).getMode().equals(AppConstants.modes.OFFLINE)) {
            return "N/A (offline mode)";
        } else {
            return pref.getString(LAST_UPDATE, "Never");
        }
    }

    public void setLastUpdate(String timestamp) {
        pref.edit().putString(LAST_UPDATE, timestamp).apply();
    }

    /**
     * Clear past preferences and clean up space for others
     */
    public void cleanPastPreferences() {

        pref.edit().remove("route1_ncbs_iisc_week").apply();
        pref.edit().remove("route2_iisc_ncbs_week").apply();
        pref.edit().remove("route3_ncbs_mandara_week").apply();
        pref.edit().remove("route4_mandara_ncbs_week").apply();
        pref.edit().remove("route5_ncbs_icts_week").apply();
        pref.edit().remove("route6_icts_ncbs_week").apply();
        pref.edit().remove("route1_ncbs_iisc_sunday").apply();
        pref.edit().remove("route2_iisc_ncbs_sunday").apply();
        pref.edit().remove("route3_ncbs_mandara_sunday").apply();
        pref.edit().remove("route4_mandara_ncbs_sunday").apply();
        pref.edit().remove("route5_ncbs_icts_sunday").apply();
        pref.edit().remove("route6_icts_ncbs_sunday").apply();
        pref.edit().remove("route7_ncbs_cbl").apply();
        pref.edit().remove("buggy_from_ncbs").apply();
        pref.edit().remove("buggy_from_mandara").apply();
        pref.edit().remove("camp_buggy_ncbs").apply();
        pref.edit().remove("camp_buggy_mandara").apply();
        pref.edit().remove("camp_shuttle_ncbs").apply();
        pref.edit().remove("camp_shuttle_mandara").apply();

        Log.i(TAG, "Cleared Past Preferences");

    }


}
