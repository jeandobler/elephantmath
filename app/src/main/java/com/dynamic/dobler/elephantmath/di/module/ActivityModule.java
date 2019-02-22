package com.dynamic.dobler.elephantmath.di.module;

import com.dynamic.dobler.elephantmath.activity.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;


@Module
public abstract class ActivityModule {
    abstract MainActivity contributeMainActivity();
}
