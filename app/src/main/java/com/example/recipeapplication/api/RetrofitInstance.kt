package com.example.recipeapplication.api

import com.example.recipeapplication.util.Constants.Companion.API_HOST
import com.example.recipeapplication.util.Constants.Companion.API_KEY
import com.example.recipeapplication.util.Constants.Companion.BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    companion object{

        private val retrofit by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

//            val authInterceptor = Interceptor{ chain ->
//
//                val original = chain.request()
//                val url = original.url.newBuilder().build()
//
//                val request = original.newBuilder()
//                    .url(url)
//                    .header("X-RapidAPI-Key", API_KEY)
//                    .header("X-RapidAPI-Host", API_HOST)
//                    .build()
//
//                chain.proceed(request)
//            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
//                .addInterceptor(authInterceptor)
                .build()

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val api by lazy {
            retrofit.create(RecipesAPI::class.java)
        }

    }

}