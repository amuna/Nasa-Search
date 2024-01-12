package com.space.search.ViewModel;

import android.util.Log;

import com.space.search.Model.Response;
import com.space.search.Model.Repository.ResponseRepository;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NasaViewModel extends ViewModel {
    private final String TAG = "NasaViewModel";
    private final MutableLiveData<ApiResponse> responseLiveData = new MutableLiveData<>();
    private final ResponseRepository repository;
    private List<Response.Item> pagedItems;

    public NasaViewModel() {
        repository = new ResponseRepository();
        //Initial query
        getQuery("qi", 1);
    }

    public void getQuery(String q, int page) {
        repository.getQuery(q, page).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG, "Subscribed");
                        responseLiveData.setValue(ApiResponse.loading());
                    }

                    @Override
                    public void onSuccess(Response response) {
                        Log.i(TAG, "Success");
                        List<Response.Item> items = response.getCollection().getItems();
                        if (items.size() == 0) {
                            responseLiveData.setValue(ApiResponse.empty());
                            return;
                        }
                        if (page != 1) {
                            pagedItems.addAll(items);
                            responseLiveData.setValue(ApiResponse.success(pagedItems));
                            return;
                        }
                        pagedItems = items;
                        responseLiveData.setValue(ApiResponse.success(items));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Error: " + e.getMessage());
                        responseLiveData.setValue(ApiResponse.error(e));
                    }
                });

    }

    public MutableLiveData<ApiResponse> searchResponse() {
        return responseLiveData;
    }

}
