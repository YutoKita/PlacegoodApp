package jp.north.mt.placegoodapp;

import android.app.Application;

import io.realm.Realm;

public class ListdataApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
