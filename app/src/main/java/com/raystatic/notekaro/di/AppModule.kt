package com.raystatic.notekaro.di

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.raystatic.notekaro.BuildConfig
import com.raystatic.notekaro.data.local.NotesDb
import com.raystatic.notekaro.data.remote.ApiHelper
import com.raystatic.notekaro.data.remote.ApiHelperImpl
import com.raystatic.notekaro.data.remote.ApiService
import com.raystatic.notekaro.other.Constants.BASE_URL
import com.raystatic.notekaro.other.Constants.NOTES_DATABASE_NAME
import com.raystatic.notekaro.other.PrefManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideGlide(
        @ApplicationContext context: Context
    ): RequestManager = Glide.with(context)

    @Singleton
    @Provides
    fun provideRunningDatabase(
        @ApplicationContext app:Context
    ) = Room.databaseBuilder(
        app,
        NotesDb::class.java,
        NOTES_DATABASE_NAME
    ).fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideUserDao(db:NotesDb) = db.getUserDao()

    @Singleton
    @Provides
    fun providesNotesDao(db:NotesDb) = db.getNotesDao()

    @Singleton
    @Provides
    fun providePrefManager(@ApplicationContext context:Context) = PrefManager(context)

    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG){
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }else{
        OkHttpClient
            .Builder()
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideApiHelper(apiHelper: ApiHelperImpl): ApiHelper = apiHelper

}