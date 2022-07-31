package com.learn.jetpackcomposepokedex.di

import com.learn.jetpackcomposepokedex.data.remote.api.PokeApi
import com.learn.jetpackcomposepokedex.repository.PokemonRepository
import com.learn.jetpackcomposepokedex.util.constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun providePokemonRepository(
        api:PokeApi
    )=PokemonRepository(api)

    @Singleton
    @Provides
    fun providePokeApi():PokeApi{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(constants.BASE_URL)
            .build()
            .create(PokeApi::class.java)
    }
}