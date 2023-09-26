package com.rubens.appajudamudanca.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rubens.appajudamudanca.data.room.dao.GestaoFinanceiraDao
import com.rubens.appajudamudanca.data.room.entities.CustoMudanca
import com.rubens.appajudamudanca.data.room.entities.Despesa
import com.rubens.appajudamudanca.data.room.entities.Financas
import com.rubens.appajudamudanca.data.room.entities.Ganho

@Database(
    entities = [Financas::class,
               Despesa::class,
               Ganho::class,
               CustoMudanca::class], version = 1
)
abstract class AppDatabase: RoomDatabase() {

    abstract val gestaoFinanceiraDao: GestaoFinanceiraDao

    companion object{
        private var INSTANCE: AppDatabase? = null

        fun getAppDatabase(context: Context): AppDatabase?{
            if(INSTANCE == null){
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "banco-app-ajuda-mudanca"
                ).build()
            }
            return INSTANCE
        }
    }


}