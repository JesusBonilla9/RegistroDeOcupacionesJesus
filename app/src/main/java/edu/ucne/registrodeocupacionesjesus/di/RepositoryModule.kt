package edu.ucne.registrodeocupacionesjesus.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.ucne.registrodeocupacionesjesus.data.repository.OcupacionRepositoryImpl
import edu.ucne.registrodeocupacionesjesus.domain.repository.OcupacionRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent :: class)
abstract class RepositoryModule{
    @Binds
    @Singleton
    abstract fun bindOcupacionRepository(
        impl: OcupacionRepositoryImpl
    ): OcupacionRepository
}