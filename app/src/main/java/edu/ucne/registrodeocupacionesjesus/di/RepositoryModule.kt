package edu.ucne.registrodeocupacionesjesus.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.ucne.registrodeocupacionesjesus.data.empleados.repository.EmpleadoRepositoryImpl
import edu.ucne.registrodeocupacionesjesus.data.ocupaciones.repository.OcupacionRepositoryImpl
import edu.ucne.registrodeocupacionesjesus.domain.empleados.repository.EmpleadoRepository
import edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.repository.OcupacionRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent :: class)
abstract class RepositoryModule{
    @Binds
    @Singleton
    abstract fun bindOcupacionRepository(
        impl: OcupacionRepositoryImpl
    ): OcupacionRepository

    @Binds
    @Singleton
    abstract fun bindEmpleadoRepository(
        impl: EmpleadoRepositoryImpl
    ): EmpleadoRepository
}

