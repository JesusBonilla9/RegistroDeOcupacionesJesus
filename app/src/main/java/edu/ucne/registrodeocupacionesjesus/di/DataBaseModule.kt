package edu.ucne.registrodeocupacionesjesus.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.ucne.registrodeocupacionesjesus.data.database.OcupacionDb
import edu.ucne.registrodeocupacionesjesus.data.local.OcupacionDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent :: class)
object DataBaseModule {
    @Provides
    @Singleton
    fun provideOcupacionDatabase(
        @ApplicationContext context: Context
    ): OcupacionDb {
        return Room.databaseBuilder(context, OcupacionDb :: class.java, "ocupacion_db").build()

    }
    @Provides
    @Singleton
    fun provideOcupacionDao(database: OcupacionDb): OcupacionDao {
        return database.OcupacionDao()
    }
}