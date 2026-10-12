package com.example.jalicalendar.di

import com.example.jalicalendar.data.remote.AuthInterceptor
import com.example.jalicalendar.data.remote.CalendarApi
import com.example.jalicalendar.data.remote.TasksApi
import com.example.jalicalendar.data.remote.TokenAuthenticator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Two Retrofit clients (Calendar v3 and Tasks v1 have different hosts), one
 * shared OkHttp stack carrying auth + refresh. JSON is lenient about unknown
 * fields so a new server property never breaks a parse (spec §9).
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = false
        coerceInputValues = true
    }

    @Provides
    @Singleton
    fun provideOkHttp(
        authInterceptor: AuthInterceptor,
        authenticator: TokenAuthenticator,
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .authenticator(authenticator)
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideCalendarApi(client: OkHttpClient, json: Json): CalendarApi =
        Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/calendar/v3/")
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(CalendarApi::class.java)

    @Provides
    @Singleton
    fun provideTasksApi(client: OkHttpClient, json: Json): TasksApi =
        Retrofit.Builder()
            .baseUrl("https://tasks.googleapis.com/v1/")
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(TasksApi::class.java)
}
