package com.learn.jetpackcomposepokedex.repository

import com.learn.jetpackcomposepokedex.data.remote.api.PokeApi
import com.learn.jetpackcomposepokedex.data.remote.response.Pokemon
import com.learn.jetpackcomposepokedex.data.remote.response.PokemonList
import com.learn.jetpackcomposepokedex.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class PokemonRepository @Inject constructor(
    private val api: PokeApi
) {
    suspend fun getPokemonList(limit:Int,offset:Int):Resource<PokemonList>{
        val response=try{
            api.getPokemonList(limit,offset)
        }catch (e:Exception){
            return Resource.Error("An unknown error occured.")
        }



        return Resource.Success(response)
    }

    suspend fun getPokemonInfo(pokemonName:String):Resource<Pokemon>{
        val response=try{
            api.getPokemonInfo(pokemonName)
        }catch (e:Exception){
            return Resource.Error("An unknown error occured.")
        }

        return Resource.Success(response)
    }
}