package com.space.search.View.Fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.space.search.R;

public class DetailFragment extends Fragment {
    private static final String TAG = "DetailFragment";
    public static final String BUNDLE_TITLE = "TITLE";
    public static final String BUNDLE_DESC = "DESC";
    public static final String BUNDLE_DATE_CREATED = "DATE_CREATED";
    public static final String BUNDLE_IMAGE_URL = "IMAGE_URL";

    private ImageView dp;
    private TextView title;
    private TextView dateCreated;
    private TextView desc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        dp = view.findViewById(R.id.dp);
        title = view.findViewById(R.id.title);
        dateCreated = view.findViewById(R.id.dateCreated);
        desc = view.findViewById(R.id.desc);

        Bundle bundle = getArguments();
        title.setText(bundle.getString(BUNDLE_TITLE));
        desc.setText(bundle.getString(BUNDLE_DESC));
        dateCreated.setText(bundle.getString(BUNDLE_DATE_CREATED));

        Glide.with(this)
                .load(bundle.getString(BUNDLE_IMAGE_URL))
                .into(dp);

        Log.i(TAG, "Created");
        return view;
    }
}
