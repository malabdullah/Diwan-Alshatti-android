package com.malabdullah.alshattidiwan;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;


/**
 * A simple {@link Fragment} subclass.
 */
public class home_fragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    String url = "http://www.malabdullah.com/diwan/JSONnames.php";
    SwipeRefreshLayout swipeRefreshLayout;
    private Downloader downloader;
    private ListView listView;
    private View view;

    public home_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home_fragment, container, false);

        MobileAds.initialize(view.getContext(), "ca-app-pub-8587947849564336~3164408808");

        AdView mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //fill the list with data from webservice using json
        listView = (ListView) view.findViewById(R.id.members_lv);
        downloader = new Downloader(view.getContext(),url,listView);
        downloader.execute();

        //create refresh when pulling down the list
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_swipe);
        swipeRefreshLayout.setColorSchemeColors(android.R.color.holo_blue_bright,
                android.R.color.holo_blue_dark,
                android.R.color.holo_green_light,
                android.R.color.holo_green_dark);
        swipeRefreshLayout.setOnRefreshListener(this);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        downloader = new Downloader(view.getContext(),url,listView);
        downloader.execute();
    }
}
