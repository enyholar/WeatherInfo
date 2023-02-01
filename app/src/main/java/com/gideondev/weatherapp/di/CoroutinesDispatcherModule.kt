package com.gideondev.weatherapp.di
import com.gideondev.domain.CoroutinesDispatcherProvider
import com.gideondev.domain.CoroutinesDispatcherProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CoroutinesDispatcherModule {

  @Binds
  abstract fun bindCoroutineDispatcher(coroutinesDispatcherProviderImpl: CoroutinesDispatcherProviderImpl): CoroutinesDispatcherProvider
}
