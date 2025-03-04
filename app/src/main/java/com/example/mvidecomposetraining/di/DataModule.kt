package com.example.mvidecomposetraining.di

import android.content.Context
import com.example.mvidecomposetraining.data.FavouriteRepositoryImpl
import com.example.mvidecomposetraining.data.SearchRepositoryImpl
import com.example.mvidecomposetraining.data.WeatherRepositoryImpl
import com.example.mvidecomposetraining.data.local.db.FavouriteCitiesDao
import com.example.mvidecomposetraining.data.local.db.FavouriteDatabase
import com.example.mvidecomposetraining.data.remote.api.ApiFactory
import com.example.mvidecomposetraining.data.remote.api.ApiService
import com.example.mvidecomposetraining.di.annotations.ApplicationScope
import com.example.mvidecomposetraining.domain.FavouriteRepository
import com.example.mvidecomposetraining.domain.SearchRepository
import com.example.mvidecomposetraining.domain.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @Binds
    @ApplicationScope
    fun bindFavouriteRepository(impl: FavouriteRepositoryImpl) : FavouriteRepository

    @Binds
    @ApplicationScope
    fun bindWeatherRepository(impl: WeatherRepositoryImpl) : WeatherRepository

    @Binds
    @ApplicationScope
    fun bindSearchRepository(impl: SearchRepositoryImpl) : SearchRepository

    companion object {

        @Provides
        @ApplicationScope
        fun provideApiService() : ApiService = ApiFactory.apiService

        @Provides
        @ApplicationScope
        fun provideDatabase(context: Context) : FavouriteDatabase {
            return FavouriteDatabase.getInstance(context)
        }

        @Provides
        @ApplicationScope
        fun provideCitiesDao(database: FavouriteDatabase) : FavouriteCitiesDao {
            return database.favouriteCitiesDao()
        }
    }
}