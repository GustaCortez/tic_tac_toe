package com.androidavanzado.tic_tac_toe.app;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class myapp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
