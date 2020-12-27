package com.globals.netconnect.netconnect.helper


import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient private constructor() {
    private val retrofit: Retrofit

    val api: Api
        get() = retrofit.create(Api::class.java!!)


    init {
        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val original = chain.request()

                    val requestBuilder = original.newBuilder()
                            .method(original.method(), original.body())

                    val request = requestBuilder.build()
                    chain.proceed(request)
                }.build()

        retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
    }

    companion object {
        private const val BASE_URL = "http://10.182.5.50:8087/Referral360/"
        private var mInstance: RetrofitClient? = null

        val instance: RetrofitClient
          get() {
                if (mInstance == null) {
                    mInstance = RetrofitClient()
                }
                return mInstance as RetrofitClient
            }
    }
}
