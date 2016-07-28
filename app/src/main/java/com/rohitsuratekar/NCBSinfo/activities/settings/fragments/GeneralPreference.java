package com.rohitsuratekar.NCBSinfo.activities.settings.fragments;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.rohitsuratekar.NCBSinfo.Home;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.settings.SettingsCommon;
import com.rohitsuratekar.NCBSinfo.background.ServiceCentre;
import com.rohitsuratekar.NCBSinfo.ui.BaseParameters;
import com.rohitsuratekar.NCBSinfo.utilities.CurrentMode;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 11-07-16.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class GeneralPreference extends PreferenceFragment {

    Preference changeMode, clearPref, faq;
    Context context;
    BaseParameters baseParameters;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        setHasOptionsMenu(true);
        baseParameters = new BaseParameters(getActivity().getBaseContext());

        context = getActivity();
        getActivity().setTitle(R.string.settings_general);

        changeMode = findPreference("change_mode_settings");
        clearPref = findPreference("clear_settings");
        faq = findPreference("settings_knownBugs");


        changeMode.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                new AlertDialog.Builder(context)
                        .setTitle(getString(R.string.warning_mode_change))
                        .setMessage(new CurrentMode(context).getWarningMessage())
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent service = new Intent(context, ServiceCentre.class);
                                service.putExtra(ServiceCentre.INTENT, ServiceCentre.RESET_APP_DATA);
                                getActivity().startService(service);
                                Intent intent = new Intent(context, Home.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                getActivity().overridePendingTransition(baseParameters.startTransition(), baseParameters.stopTransition());
                            }
                        })
                        .setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Do nothing
                            }
                        })
                        .setIcon(new CurrentMode(context).getIcon())
                        .show();
                return false;
            }
        });

        clearPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Warning!")
                        .setMessage("Want to clear all customizations and preferences ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent service = new Intent(context, ServiceCentre.class);
                                service.putExtra(ServiceCentre.INTENT, ServiceCentre.RESET_PREFERENCES);
                                getActivity().startService(service);
                                Toast.makeText(getActivity(), "User Preferences Cleared", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(R.drawable.icon_warning)
                        .show();
                return false;
            }
        });

        faq.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getActivity(), SettingsCommon.class);
                intent.putExtra(SettingsCommon.INTENT, SettingsCommon.FAQ);
                startActivity(intent);
                getActivity().overridePendingTransition(baseParameters.startTransition(), baseParameters.stopTransition());
                return true;
            }
        });


    }

}