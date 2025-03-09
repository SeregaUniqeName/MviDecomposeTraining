package com.example.mvidecomposetraining

import android.app.Application
import com.example.mvidecomposetraining.di.ApplicationComponent
import com.example.mvidecomposetraining.di.DaggerApplicationComponent

class WeatherApp : Application() {

    lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        component = DaggerApplicationComponent.factory().create(this)
    }

}