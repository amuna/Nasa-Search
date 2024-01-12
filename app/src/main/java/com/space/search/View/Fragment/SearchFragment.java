package com.space.search.View.Fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.space.search.Model.Response;
import com.space.search.R;
import com.space.search.View.Adapter.OnItemClickListener;
import com.space.search.View.Adapter.SearchItemAdapter;
import com.space.search.ViewModel.ApiResponse;
import com.space.search.ViewModel.NasaViewModel;
import com.google.android.material.snackbar.Snackbar;

import static androidx.navigation.Navigation.findNavController;

public class SearchFragment extends Fragment implements OnItemClickListener {
    private static final String TAG = "SearchFragment";
    private NasaViewModel nasaViewModel;

    private SearchItemAdapter adapter;
    private GridLayoutManager layoutManager;
    private RecyclerView recyclerView;

    private Snackbar loadingSnackbar;
    private Snackbar msgSnackbar;

    //Pagination
    private int pastVisibleItems, visibleItemCount, totalItemCount, previousTotal = 0;
    private int view_threshold = 0;
    private String searchQuery;
    private int page = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        nasaViewModel = ViewModelProviders.of(this).get(NasaViewModel.class);

        initViews(view);

        initObservers();

        Log.i(TAG, "Created");
        return view;
    }

    private void initViews(View view) {
        setHasOptionsMenu(true);
        layoutManager = new GridLayoutManager(getContext(), 1);
        adapter = new SearchItemAdapter(this);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(adapter);

        loadingSnackbar = Snackbar.make(view, "Loading...", Snackbar.LENGTH_INDEFINITE);
        msgSnackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG);
        //show progress bar in loadingSnackbar
        ViewGroup contentLay = (ViewGroup) loadingSnackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text).getParent();
        ProgressBar progBar = new ProgressBar(getContext());
        contentLay.addView(progBar,0);
    }

    private void initObservers() {
        //pagination listener
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                //User is scrolling up
                if (dy > 0 && totalItemCount % 100 == 0) {
                    if (loadingSnackbar.isShown()) {
                        if (totalItemCount > previousTotal) {
                            loadingSnackbar.dismiss();
                            previousTotal = totalItemCount;
                        }
                    }
                    //user is at bottom of list, load more
                    if (!loadingSnackbar.isShown() && (totalItemCount-visibleItemCount) <= (pastVisibleItems+view_threshold)) {
                        page++;
                        Log.i(TAG, "Loading page " + page + " for query " + searchQuery);
                        nasaViewModel.getQuery(searchQuery, page);
                    }
                }
            }
        });

        //query observer
        nasaViewModel.searchResponse().observe(this, new Observer<ApiResponse>() {
            @Override
            public void onChanged(ApiResponse apiResponse) {
                Log.i(TAG, "ApiResponse status: " + apiResponse.status);
                consumeResponse(apiResponse);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = query;
                page = 1;
                nasaViewModel.getQuery(query, page);
                Log.i(TAG, "User Search: " + query);
                searchView.clearFocus();

                //closing searchView
                menu.findItem(R.id.action_search).collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void consumeResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case LOADING:
                loadingSnackbar.show();
                break;

            case SUCCESS:
                loadingSnackbar.dismiss();
                adapter.setItems(apiResponse.data);
                break;

            case EMPTY:
                loadingSnackbar.dismiss();
                msgSnackbar.setText("No results found. Try another query");
                msgSnackbar.show();
                break;

            case ERROR:
                loadingSnackbar.dismiss();
                msgSnackbar.setText("Error: " + apiResponse.error.getMessage());
                msgSnackbar.show();
                break;
        }
    }

    //Navigating to detail fragment
    @Override
    public void onClick(Response.Item item, View view) {
        //Could've also made object parcelable and sent object in bundle
        Bundle args = new Bundle();

        String imageURL = item.getLinks().get(0).getHref();
        String title = item.getData().get(0).getTitle();
        String desc = item.getData().get(0).getDescription();
        String dateCreated = item.getData().get(0).getDate_created();

        args.putString(DetailFragment.BUNDLE_IMAGE_URL, imageURL);
        args.putString(DetailFragment.BUNDLE_TITLE, title);
        args.putString(DetailFragment.BUNDLE_DESC, desc);
        args.putString(DetailFragment.BUNDLE_DATE_CREATED, dateCreated);

        Log.i(TAG, "Navigating to DetailFragment");
        findNavController(view)
                .navigate(R.id.detailFragment, args);
    }
}
