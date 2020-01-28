package com.sp.myapplication

import android.content.Context
import dagger.Module
import dagger.Provides
import loylap.core.sdk.data.local.analytics.ACAnalyticsHelper
import loylap.core.sdk.data.local.analytics.AnalyticsHelper
import loylap.core.sdk.data.local.prefs.AppPreferencesHelper
import loylap.core.sdk.data.local.prefs.PreferencesHelper
import loylap.core.sdk.di.ApplicationContext
import loylap.core.sdk.di.PreferenceInfo
import loylap.core.sdk.utils.SdkConstants
import javax.inject.Singleton


@Module
class AppModule {

    @Provides
    internal fun provideAnalyticsHelper(@ApplicationContext context: Context, preferencesHelper: PreferencesHelper): AnalyticsHelper {
        return ACAnalyticsHelper(context, preferencesHelper)
    }

    @Provides
    @Singleton
    internal fun providePreferencesHelper(appPreferencesHelper: AppPreferencesHelper): PreferencesHelper {
        return appPreferencesHelper
    }

    @Provides
    @PreferenceInfo
    internal fun providePreferenceName(): String {
        return SdkConstants.PREF_NAME
    }

}