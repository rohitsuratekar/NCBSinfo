package com.rohitsuratekar.NCBSinfo.fragments.home;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.BaseActivity;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.common.CommonTasks;
import com.rohitsuratekar.NCBSinfo.common.Helper;
import com.rohitsuratekar.NCBSinfo.fragments.transport.TransportDetails;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Home extends Fragment {

    public Home() {
    }

    @BindView(R.id.hm_sun)
    ImageView sun;
    @BindView(R.id.hm_graphics)
    ImageView scene;
    @BindView(R.id.hm_stars)
    ImageView stars;
    @BindView(R.id.hm_type)
    TextView type;
    @BindView(R.id.hm_origin)
    TextView origin;
    @BindView(R.id.hm_destination)
    TextView destination;
    @BindView(R.id.hm_time)
    TextView time;
    @BindView(R.id.hm_fav)
    ImageView fav;

    private int randRotation = 0;
    private List<TransportDetails> transportList;
    private TransportDetails transport;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        randRotation = Helper.randomInt(0, 360);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home, container, false);
        ButterKnife.bind(this, rootView);
        if (getActivity() != null) {
            transportList = ((BaseActivity) getActivity()).getTransportList();
            transport = ((BaseActivity) getActivity()).getCurrentTransport();
            updateRoute();
        }
        adjustGraphics();
        return rootView;
    }

    private void updateRoute() {
        origin.setText(transport.getOrigin().toUpperCase());
        destination.setText(transport.getDestination().toUpperCase());
        type.setText(getString(R.string.hm_next, transport.getType()));
        try {
            time.setText(Helper.displayTime(transport.getNextTripDetails(Calendar.getInstance())[0]));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (getContext() != null) {
            if (transport.getRouteData().getFavorite().equals("yes")) {
                fav.setImageResource(R.drawable.icon_fav);
                fav.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorAccent));

            } else {
                fav.setImageResource(R.drawable.icon_fav_empty);
                fav.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary));

            }
        }
    }

    private void adjustGraphics() {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (hour > 5 && hour < 19) {
            stars.setVisibility(View.GONE);
        } else {
            sun.setImageResource(R.drawable.graphics_moon);
            stars.setVisibility(View.VISIBLE);
            stars.setRotation(randRotation);
        }
        float hour_factor = getResources().getDimensionPixelSize(R.dimen.home_image_height) / 12;
        hour_factor = hour_factor * hour;
        if (hour > 12) {
            hour_factor = hour_factor / 2;
        }
        sun.animate()
                .translationX(hour_factor)
                .setDuration(10).start();
    }

    @OnClick({R.id.hm_change_route, R.id.hm_origin, R.id.hm_destination, R.id.hm_type})
    public void showAllRoutes() {
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).showRouteList();
        }
    }

    @OnClick({R.id.hm_see_all, R.id.hm_time})
    public void seeAll() {
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).attachTransport();
        }
    }

    public void changeRoute(int routeNo) {
        for (TransportDetails t : transportList) {
            if (t.getRouteID() == routeNo) {
                transport = t;
                updateRoute();
                break;
            }
        }
    }

    @OnClick(R.id.hm_fav)
    public void changeFavorite(ImageView imageView) {
        if (getContext() != null) {
            CommonTasks.sendFavoriteRoute(getContext(), transport.getRouteID());
            transport.getRouteData().setFavorite("yes");
            for (TransportDetails t : transportList) {
                if (t != transport) {
                    t.getRouteData().setFavorite("no");
                }
            }
            fav.setImageResource(R.drawable.icon_fav);
            fav.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorAccent));
            Snackbar snackbar = Snackbar.make(scene, "Default route changed!", BaseTransientBottomBar.LENGTH_SHORT);
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green));
            snackbar.show();

        }
    }


}