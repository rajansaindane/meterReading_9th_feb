package com.embeltech.meterreading.injection.module

import android.content.Context
import android.net.ConnectivityManager
import com.embeltech.meterreading.config.Application
import com.embeltech.meterreading.data.Config
import com.embeltech.meterreading.data.api.BIService
import com.embeltech.meterreading.data.api.HeaderInterceptor
import com.embeltech.meterreading.exception.NoNetworkException
import com.embeltech.meterreading.extensions.isConnected
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton
import javax.net.ssl.SSLPeerUnverifiedException


@Module
class ApiServiceModule {

    @Provides
    @Named(BASE_URL)
    internal fun provideBaseUrl(): String {
        return Config.API_HOST
    }

    @Provides
    @Singleton
    internal fun provideHeaderInterceptor(): HeaderInterceptor {
        return HeaderInterceptor()
    }

    @Provides
    @Singleton
    internal fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
    }

    @Provides
    @Singleton
    internal fun provideHttpClient(
        headerInterceptor: HeaderInterceptor,
        httpInterceptor: HttpLoggingInterceptor, app: Application
    ): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(headerInterceptor)
            .addInterceptor(httpInterceptor.apply {
                level =
                    HttpLoggingInterceptor.Level.BODY
            })

        val connectivityManager =
            app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


        return okHttpClientBuilder
            .addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                //logToFile("URL: ${chain.request().url()}, Body: ${chain.request().body().bodyToString()}, Header: ${chain.request().headers()}")
                if (!connectivityManager.isConnected) {
                    throw NoNetworkException
                }
                try {
                    chain.proceed(requestBuilder.build()).apply {
                        /*val bodyString = String(this.body()!!.bytes())
                        logToFile("Response: $bodyString")
                        this.newBuilder().body(ResponseBody.create(this!!.body()!!.contentType(), bodyString)).build()*/
                    }
                } catch (e: SocketTimeoutException) {
                    throw NoNetworkException
                } catch (e: UnknownHostException) {
                    throw NoNetworkException
                } catch (e: SSLPeerUnverifiedException) {
                    throw NoNetworkException
                }
            }
            .build()
    }

    @Provides
    @Singleton
    internal fun provideGsonConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    internal fun provideRxJavaAdapterFactory(): CallAdapter.Factory {
        return RxJava2CallAdapterFactory.create()
    }

    @Provides
    @Singleton
    internal fun provideRetrofit(
        @Named(BASE_URL) baseUrl: String, converterFactory: Converter.Factory,
        callAdapterFactory: CallAdapter.Factory, client: OkHttpClient
    ): Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(callAdapterFactory)
            .client(client)
            .build()
    }

    companion object {
        private const val BASE_URL = "http://65.1.68.67:8080/iotmeter/"
    }

    /* Specific services */
    @Provides
    @Singleton
    fun provideService(retrofit: Retrofit): BIService {
        return retrofit.create(BIService::class.java)
    }

}
