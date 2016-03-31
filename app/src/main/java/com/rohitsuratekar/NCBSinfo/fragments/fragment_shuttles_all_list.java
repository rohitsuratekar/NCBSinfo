package com.rohitsuratekar.NCBSinfo.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.adapters.adapters_shuttle_list;
import com.rohitsuratekar.NCBSinfo.helper.helper_shuttles;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Rohit Suratekar on 3/15/2016.
 */
public class fragment_shuttles_all_list extends Fragment {

        public static fragment_shuttles_all_list newInstance(int index) {
            fragment_shuttles_all_list fragment = new fragment_shuttles_all_list();
            // Supply index input as an argument.
            Bundle args = new Bundle();
            args.putInt("index", index);
            fragment.setArguments(args);
            return fragment;
        }
        public fragment_shuttles_all_list() {
        }
        String GlobalShuttleFrom;
        String GlobalShuttleto;
        int isBuggy;
        TextView weekTitle, sundayTitle, footnote1, footnote2;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Bundle args = getArguments();
            int index = args.getInt("index", 0);
            switch (index) {
                case 0:
                    GlobalShuttleFrom = "ncbs";
                    GlobalShuttleto = "iisc";
                    isBuggy = 0;

                    break;
                case 1:
                    GlobalShuttleFrom = "iisc";
                    GlobalShuttleto = "ncbs";
                    isBuggy = 0;
                    break;
                case 2:
                    GlobalShuttleFrom = "ncbs";
                    GlobalShuttleto = "mandara";
                    isBuggy = 0;
                    break;
                case 3:
                    GlobalShuttleFrom = "mandara";
                    GlobalShuttleto = "ncbs";
                    isBuggy = 0;
                    break;
                case 4:
                    GlobalShuttleFrom = "ncbs";
                    GlobalShuttleto = "mandara";
                    isBuggy = 1;
                    break;
                case 5:
                    GlobalShuttleFrom = "ncbs";
                    GlobalShuttleto = "icts";
                    isBuggy = 0;
                    break;
                case 6:
                    GlobalShuttleFrom = "icts";
                    GlobalShuttleto = "ncbs";
                    isBuggy = 0;
                    break;
                case 7:
                    GlobalShuttleFrom = "ncbs";
                    GlobalShuttleto = "cbl";
                    isBuggy = 0;
                    break;


            }
            View rootView = inflater.inflate(R.layout.activity_shuttles_fragment_all_lists, container, false);
            weekTitle = (TextView)rootView.findViewById(R.id.Shuttles_Weekdays_List);
            sundayTitle = (TextView)rootView.findViewById(R.id.Shuttles_Sunday_List);
            footnote1 = (TextView)rootView.findViewById(R.id.Shuttles_Footnote1);
            footnote2 = (TextView)rootView.findViewById(R.id.Shuttles_Footnote2);
            perform(rootView);
            return rootView;
        }

        public void perform(View v) {
            String WeekDate = "12/31/2015 00:00:00";
            String SundayDate = "1/31/2016 00:00:00";
            String weeksmall = "12/31/2015 ";
            String sundaysmall = "1/31/2016 ";
            String[] AllArrays = new helper_shuttles().TripsData(GlobalShuttleFrom, GlobalShuttleto, WeekDate, isBuggy);



            String[] after = new String[AllArrays.length];
            SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
            SimpleDateFormat modformat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            SimpleDateFormat onlytime = new SimpleDateFormat("HH:mm:ss",Locale.getDefault());

            String current_weekday = weeksmall+onlytime.format(Calendar.getInstance().getTime());
            String current_sunday = sundaysmall+onlytime.format(Calendar.getInstance().getTime());

            Calendar nextcal = new helper_shuttles().NextTransport(GlobalShuttleFrom, GlobalShuttleto, current_weekday, isBuggy);
            Calendar sundaynext = new helper_shuttles().NextTransport(GlobalShuttleFrom, GlobalShuttleto, current_sunday, isBuggy);

            if (isBuggy==1){
                nextcal = new helper_shuttles().NextTransport("ncbs", "mandara", current_weekday, isBuggy);
                sundaynext = new helper_shuttles().NextTransport("mandara", "ncbs", current_sunday, isBuggy);
            }

            String nextShuttleWeek = modformat.format(nextcal.getTime());
            String nextShuttleSunday = modformat.format(sundaynext.getTime());
            int selectionItem1 = 0;
            int selectionItem2 = 0;

            for (int i =0; i<AllArrays.length; i++){

                try {
                    Date tempdate = format.parse(AllArrays[i]);
                    after[i] = modformat.format(tempdate);
                    if (after[i].equals(nextShuttleWeek)){
                        after[i]="<font color=\"blue\">"+modformat.format(tempdate)+"**</font>";
                        selectionItem1 = i;

                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

            String[] AllArrays_sunday = new helper_shuttles().TripsData(GlobalShuttleFrom, GlobalShuttleto, SundayDate, isBuggy);
            if (isBuggy==1){
                AllArrays_sunday = new helper_shuttles().TripsData("mandara", "ncbs", SundayDate, isBuggy);
            }

            String[] sunday = new String[AllArrays_sunday.length];
            for (int i =0; i<AllArrays_sunday.length; i++){
                try {
                    Date tempdate = format.parse(AllArrays_sunday[i]);
                    sunday[i] = modformat.format(tempdate);

                    if (sunday[i].equals(nextShuttleSunday)){
                        sunday[i]="<font color=\"blue\">"+modformat.format(tempdate)+"**</font>";
                        selectionItem2 = i;

                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
            ListView afterList = (ListView) v.findViewById(R.id.TripList_weekday);
            ListView sundaylist = (ListView) v.findViewById(R.id.TripList_sunday);
            adapters_shuttle_list adapter5 = new adapters_shuttle_list(getActivity(), R.layout.activity_shuttles_fragment_list_view, after);
            adapters_shuttle_list adapter2 = new adapters_shuttle_list(getActivity(), R.layout.activity_shuttles_fragment_list_view, sunday);
            sundaylist.setAdapter(adapter2);
            afterList.setAdapter(adapter5);
            afterList.setSelection(selectionItem1);
            afterList.requestFocus();
            sundaylist.setSelection(selectionItem2);
            sundaylist.requestFocus();

            String currentTime = modformat.format(Calendar.getInstance().getTime());

            weekTitle.setText("Weekdays*");
            sundayTitle.setText("Sunday");
            footnote1.setText("*Including Saturday");
            footnote2.setText("**Next shuttle after "+currentTime);

            if (isBuggy==1){
                AllArrays = new helper_shuttles().TripsData("ncbs", "mandara", WeekDate, isBuggy);
                weekTitle.setText("From NCBS");
                sundayTitle.setText("From Mandara");
                footnote1.setText("");
                footnote2.setText("**Next buggy after "+currentTime);
            }

        }



    }
