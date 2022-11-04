package com.example.pet_pc.controledefaltas;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by PET-PC on 06/08/2018.
 */

public class Configuracao extends Application {

    @Override
    public void onCreate()  {
        super.onCreate();
        Realm.init(getApplicationContext());
        RealmConfiguration config = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);
    }
}
