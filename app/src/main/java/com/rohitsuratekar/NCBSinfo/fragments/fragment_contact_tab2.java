package com.rohitsuratekar.NCBSinfo.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.rohitsuratekar.NCBSinfo.DatabaseHelper;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.adapters.adapters_contact;
import com.rohitsuratekar.NCBSinfo.constants.SQLConstants;
import com.rohitsuratekar.NCBSinfo.helper.helper_contact_divideritemdecoratio;
import com.rohitsuratekar.NCBSinfo.helper.helper_contact_list;
import com.rohitsuratekar.NCBSinfo.models.models_contacts_database;
import com.rohitsuratekar.NCBSinfo.models.models_contacts_row;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Dexter on 3/17/2016.
 */

public class fragment_contact_tab2 extends Fragment implements SearchView.OnQueryTextListener {

    private View rootView2;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter2;
    private RecyclerView.LayoutManager mLayoutManager;
    EditText inputSearch;
    private ArrayList<models_contacts_row> resultsFav = new ArrayList<models_contacts_row>();
    ArrayList<models_contacts_row> mAllData = new ArrayList<models_contacts_row>();
    private ImageButton searchBt;
    private boolean toggleSearchCancel = true;
    public static final String RADIO_DATASET_CHANGED = "com.rohitsuratekar.NCBSinfo.RADIO_DATASET_CHANGED";
    private Radio radio;

    public fragment_contact_tab2() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        radio = new Radio();

        View rootView2 = inflater.inflate(R.layout.activity_contact_tab, container, false);
        mRecyclerView = (RecyclerView) rootView2.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter2 = new adapters_contact(getDataSet());
        mRecyclerView.setAdapter(mAdapter2);
        RecyclerView.ItemDecoration itemDecoration =
                new helper_contact_divideritemdecoratio(getContext(), LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);
        doSearch(rootView2);

        searchBt = (ImageButton) rootView2.findViewById(R.id.searchButton);
        searchBt.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                if (toggleSearchCancel) {
                    searchBt.setImageResource(R.drawable.icon_close);
                    toggleSearchCancel = false;
                    //inputSearch.setInputType(1);
                } else {
                    hideSoftKeyboard(getActivity());
                    searchBt.setImageResource(R.drawable.icon_search);
                    toggleSearchCancel = true;
                    resultsFav.clear();
                    resultsFav.addAll(mAllData);
                    mAdapter2.notifyDataSetChanged();
                    // inputSearch.setInputType(0);
                    inputSearch.setText("");

                }
            }
        });

        inputSearch = (EditText) rootView2.findViewById(R.id.searchTextBox);

        // Code to Add an item with default animation
        //((MyRecyclerViewAdapter) mAdapter).addItem(obj, index);

        // Code to remove an item with default animation
        //((MyRecyclerViewAdapter) mAdapter).deleteItem(index);

        return rootView2;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((adapters_contact) mAdapter2).setOnItemClickListener(new adapters_contact.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                hideSoftKeyboard(getActivity());
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + resultsFav.get(position).getmText2()));
                startActivity(intent);

            }
        });
        ((adapters_contact) mAdapter2).setOnItemClickListener2(new adapters_contact.MyClickListener2() {
            @Override
            public void onItemClick(int position, View v) {
                ImageButton img = (ImageButton) v.findViewById(R.id.fav_button);

                DatabaseHelper db2 = new DatabaseHelper(getContext());
                models_contacts_database doc = db2.getContact(resultsFav.get(position).getmID());
                if (doc.getContact_favorite().equals("0")) {
                    img.setColorFilter(Color.RED);
                    doc.setContact_favorite("1");
                    mAdapter2.notifyDataSetChanged();
                } else {
                    img.setColorFilter(null);
                    doc.setContact_favorite("0");
                    ((adapters_contact) mAdapter2).deleteItem(position);
                    mAdapter2.notifyItemRemoved(position);
                    Intent intent = new Intent(fragment_contact_tab1.RADIO_DATASET_CHANGED2); //This will update fragment
                    getActivity().getApplicationContext().sendBroadcast(intent);
                }


                db2.updateContact(doc);
            }
        });

        IntentFilter filter = new IntentFilter();
        filter.addAction(RADIO_DATASET_CHANGED);
        getActivity().getApplicationContext().registerReceiver(radio, filter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            getActivity().getApplicationContext().unregisterReceiver(radio);
        }catch (Exception e){
            //Cannot unregister receiver
        }

    }

    private ArrayList<models_contacts_row> getDataSet() {



        DatabaseHelper db2 = new DatabaseHelper(getContext());
        List<models_contacts_database> contacts = db2.getAllContacts();

        for (models_contacts_database cn : contacts) {
            if (cn.getContact_favorite().equals("1")) {
                resultsFav.add(new models_contacts_row(cn.getContact_name(), cn.getContact_extension(), cn.getContact_department(), cn.getContact_id()));
            }
        }

        mAllData.addAll(resultsFav);
        db2.close();
        return resultsFav;
    }

    private void doSearch(View v) {
        final EditText et = (EditText) v.findViewById(R.id.searchTextBox);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchBt.setImageResource(R.drawable.icon_close);
                toggleSearchCancel = false;
                String text = et.getText().toString().toLowerCase(Locale.getDefault());
                filter(text);
            }
        });
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        resultsFav.clear();

        if (charText.length() == 0) {
            resultsFav.addAll(mAllData);
        } else {
            for (models_contacts_row wp : mAllData) {
                if (wp.getmText1().toLowerCase(Locale.getDefault()).contains(charText) || wp.getmText2().toLowerCase(Locale.getDefault()).contains(charText)) {
                    resultsFav.add(wp);
                }
            }

        }
        mAdapter2.notifyDataSetChanged();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


        // Checks whether a hardware keyboard is available
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {

        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {

            searchBt.setImageResource(R.drawable.icon_search);
            toggleSearchCancel = true;
            resultsFav.clear();
            resultsFav.addAll(mAllData);
            mAdapter2.notifyDataSetChanged();
            //inputSearch.setInputType(0);
            inputSearch.setText("");
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    private class Radio extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(RADIO_DATASET_CHANGED)){
                //Change received from previous fragment
                refresh();
                mAdapter2.notifyDataSetChanged();
            }
        }
    }

    public void refresh(){
        resultsFav.clear();

        DatabaseHelper db2 = new DatabaseHelper(getContext());
        List<models_contacts_database> contacts = db2.getAllContacts();

        for (models_contacts_database cn : contacts) {
            if (cn.getContact_favorite().equals("1")) {
                resultsFav.add(new models_contacts_row(cn.getContact_name(), cn.getContact_extension(), cn.getContact_department(), cn.getContact_id()));
            }
        }
        mAllData.clear();
        mAllData.addAll(resultsFav);
        db2.close();
        mAdapter2.notifyDataSetChanged();

    }
}

