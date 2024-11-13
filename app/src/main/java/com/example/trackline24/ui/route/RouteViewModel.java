package com.example.trackline24.ui.route;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RouteViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public RouteViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Esta es la vista de Rutas ");
    }

    public LiveData<String> getText() {
        return mText;
    }
}