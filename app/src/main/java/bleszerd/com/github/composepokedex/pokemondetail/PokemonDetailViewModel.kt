package bleszerd.com.github.composepokedex.pokemondetail

import androidx.lifecycle.ViewModel
import bleszerd.com.github.composepokedex.data.remote.responses.Pokemon
import bleszerd.com.github.composepokedex.repository.PokemonRepository
import bleszerd.com.github.composepokedex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val repository: PokemonRepository
): ViewModel() {
    suspend fun getPokemonInfo(pokemonName: String): Resource<Pokemon>{
        return repository.getPokemonInfo(pokemonName)
    }
}