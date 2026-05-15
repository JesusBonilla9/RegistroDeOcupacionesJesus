package edu.ucne.registrodeocupacionesjesus.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.ucne.registrodeocupacionesjesus.data.database.RegistroDb
import edu.ucne.registrodeocupacionesjesus.data.empleados.local.EmpleadoDao
import edu.ucne.registrodeocupacionesjesus.data.ocupaciones.local.OcupacionDao
import edu.ucne.registrodeocupacionesjesus.domain.empleados.model.Empleado
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent :: class)
object DataBaseModule {
    @Provides
    @Singleton
    fun provideOcupacionDatabase(
        @ApplicationContext context: Context
    ): RegistroDb {
        return Room.databaseBuilder(context, RegistroDb :: class.java, "registro_db").build()

    }
    @Provides
    @Singleton
    fun provideOcupacionDao(database: RegistroDb): OcupacionDao {
        return database.OcupacionDao()
    }

    @Provides
    @Singleton
    fun provideEmpleadoDao(database: RegistroDb): EmpleadoDao {
        return database.EmpleadoDao()
    }
}