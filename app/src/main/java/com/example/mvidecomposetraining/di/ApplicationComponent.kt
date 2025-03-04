package com.example.mvidecomposetraining.di

import android.content.Context
import com.example.mvidecomposetraining.di.annotations.ApplicationScope
import dagger.BindsInstance
import dagger.Component

@Component(modules = [DataModule::class])
@ApplicationScope
interface ApplicationComponent {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance context: Context
        ) : ApplicationComponent
    }
}