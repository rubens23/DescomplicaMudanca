package com.rubens.appajudamudanca.data.di

import android.content.Context
import com.rubens.appajudamudanca.data.AppDatabase
import com.rubens.appajudamudanca.data.datasource.DicasApi
import com.rubens.appajudamudanca.data.datasource.DicasApi.Companion.BASE_URL
import com.rubens.appajudamudanca.data.repositories.DicasRepository
import com.rubens.appajudamudanca.data.repositories.DicasRepositoryImpl
import com.rubens.appajudamudanca.data.repositories.RoomRepository
import com.rubens.appajudamudanca.data.repositories.RoomRepositoryImpl
import com.rubens.appajudamudanca.data.room.dao.GestaoFinanceiraDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataAccessModule {

    @Provides
    @Singleton
    fun providesRetrofitInstance(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()).build()
    }

    @Provides
    @Singleton
    fun providesDicaApi(retrofitInstance: Retrofit): DicasApi{
        return retrofitInstance.create(DicasApi::class.java)
    }

    @Provides
    @Singleton
    fun providesDicasRepository(dicasApi: DicasApi): DicasRepository{
        return DicasRepositoryImpl(dicasApi)
    }

    @Provides
    @Singleton
    fun provideDB(@ApplicationContext context: Context): AppDatabase?{
        return AppDatabase.getAppDatabase(context)
    }

    @Provides
    @Singleton
    fun providesGestaoFinanceiraDao(db: AppDatabase?): GestaoFinanceiraDao{
        return db!!.gestaoFinanceiraDao
    }

    @Provides
    @Singleton
    fun providesRoomRepository(dao: GestaoFinanceiraDao): RoomRepository{
        return RoomRepositoryImpl(dao)
    }


}