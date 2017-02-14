package xyz.kandrac.kappka.dagger.component;

import javax.inject.Singleton;

import dagger.Component;
import xyz.kandrac.kappka.dagger.module.ApplicationModule;

/**
 * Created by jan on 28.10.2016.
 */
@Component(modules = ApplicationModule.class)
@Singleton
public interface ApplicationComponent {

}
