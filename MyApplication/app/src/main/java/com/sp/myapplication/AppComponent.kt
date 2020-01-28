package com.sp.myapplication

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import loylap.core.sdk.di.ApplicationContext
import javax.inject.Singleton


@Component(modules = [AndroidInjectionModule::class, AppModule::class, ActivityBuilder::class])
@Singleton
interface AppComponent  : AndroidInjector<MyApp> {
    @Component.Builder
    abstract class Builder: AndroidInjector.Builder<MyApp>()  {

        @BindsInstance
        abstract fun application(@ApplicationContext context: Context): Builder

        override fun seedInstance(instance: MyApp) {
            application(instance)
        }
    }
}