package com.space.search.ViewModel;

import com.space.search.Model.Response;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ApiResponse {

    public enum Status {
        LOADING,
        SUCCESS,
        EMPTY,
        ERROR,
    }

    public final Status status;

    @Nullable
    public final List<Response.Item> data;

    @Nullable
    public final Throwable error;

    private ApiResponse(Status status, @Nullable List<Response.Item> data, @Nullable Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static ApiResponse loading() {
        return new ApiResponse(Status.LOADING, null, null);
    }

    public static ApiResponse success(@NonNull List<Response.Item> data) {
        return new ApiResponse(Status.SUCCESS, data, null);
    }

    public static ApiResponse empty() {
        return new ApiResponse(Status.EMPTY, null, null);
    }

    public static ApiResponse error(@NonNull Throwable error) {
        return new ApiResponse(Status.ERROR, null, error);
    }
}
