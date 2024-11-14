package com.example.trackline24.ui.profile;

import android.widget.Button;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ProfileViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Esta es la vista de Perfil ");
    }

    public LiveData<String> getText() {
        return mText;
    }
}