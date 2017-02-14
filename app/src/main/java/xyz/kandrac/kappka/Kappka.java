package xyz.kandrac.kappka;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import xyz.kandrac.kappka.dagger.component.ApplicationComponent;
import xyz.kandrac.kappka.dagger.component.DaggerApplicationComponent;
import xyz.kandrac.kappka.dagger.module.ApplicationModule;

/**
 * Created by jan on 12.2.2017.
 */

public class Kappka extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
    }

    private ApplicationComponent mAppComponent;

    protected ApplicationModule getApplicationModule() {
        return new ApplicationModule(this);
    }

    public static ApplicationComponent getAppComponent(Context context) {
        Kappka app = (Kappka) context.getApplicationContext();
        if (app.mAppComponent == null) {
            app.mAppComponent = DaggerApplicationComponent.builder()
                    .applicationModule(app.getApplicationModule())
                    .build();
        }
        return app.mAppComponent;
    }
}
