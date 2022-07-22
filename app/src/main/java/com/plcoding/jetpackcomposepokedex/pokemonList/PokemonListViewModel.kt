package com.plcoding.jetpackcomposepokedex.pokemonList

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.plcoding.jetpackcomposepokedex.data.models.PokedoxListEntry
import com.plcoding.jetpackcomposepokedex.repository.PokemonRepository
import com.plcoding.jetpackcomposepokedex.util.Resource
import com.plcoding.jetpackcomposepokedex.util.constants.PAGE_SIZE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private var curPage=0

    var pokemonList= mutableStateOf<List<PokedoxListEntry>>(listOf())
    var loadError= mutableStateOf("")
    var isLoading= mutableStateOf(false)

    var endReached = mutableStateOf(false)

    init {
        loadPokemonPaginated()
    }


    fun loadPokemonPaginated() {
        viewModelScope.launch {
            val result = repository.getPokemonList(PAGE_SIZE,curPage * PAGE_SIZE)

            sequenceOf(
                when (result) {
                    is Resource.Success -> {
                        endReached.value =curPage * PAGE_SIZE >= result.data!!.count

                        val pokedoxEntries = result.data.results.mapIndexed{index,entry->
                            val number= if (entry.url.endsWith("/")){
                                entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                            }else{
                                entry.url.takeLastWhile { it.isDigit() }
                            }

                            val url="https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/${number}.png"
                            //pokemonList.value +=PokedoxListEntry(entry.name.capitalize(Locale.ROOT),
                            //url, number = number.toInt())

                            PokedoxListEntry(entry.name.capitalize(Locale.ROOT),url,number.toInt())
                        }

                        curPage++

                        loadError.value =""
                        isLoading.value =false
                        pokemonList.value += pokedoxEntries

                            }

                    is Resource.Error -> {
                        Log.d("size",result.message.toString())
                        loadError.value =result.message!!
                        isLoading.value = false
                    }
                }
            )
        }
    }

    fun calculateDominantColor(drawable:Drawable,onFinish:(Color)->Unit){
        val bitmap=(drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888,true)

        Palette.from(bitmap).generate { palette ->
            palette?.dominantSwatch?.rgb?.let {
                colorValue ->
                    onFinish(Color(colorValue))
            }
        }
    }
}