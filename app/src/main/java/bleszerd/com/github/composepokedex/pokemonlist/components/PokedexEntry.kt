package bleszerd.com.github.composepokedex.pokemonlist.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import bleszerd.com.github.composepokedex.data.model.PokedexListEntry
import bleszerd.com.github.composepokedex.pokemonlist.PokemonListViewModel
import bleszerd.com.github.composepokedex.ui.theme.RobotoCondensed
import coil.annotation.ExperimentalCoilApi
import coil.bitmap.BitmapPool
import coil.compose.rememberImagePainter
import coil.transform.Transformation

@ExperimentalCoilApi
@Composable
fun PokedexEntry(
    entry: PokedexListEntry,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    val defaultDominantColor = MaterialTheme.colors.surface.toArgb()
    var dominantColor by rememberSaveable {
        mutableStateOf(defaultDominantColor)
    }
    var isLoading by rememberSaveable { mutableStateOf(true) }

    Box(
        contentAlignment = Center,
        modifier = modifier
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(1f)
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(dominantColor),
                        Color(defaultDominantColor),
                    )
                )
            )
            .clickable {
                navController.navigate(
                    "pokemon_detail_screen/${dominantColor}/${entry.pokemonName}"
                )
            }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .align(CenterHorizontally),
            ) {
                if (isLoading)
                    CircularProgressIndicator(modifier = Modifier.align(Center))

                    Image(
                        painter = rememberImagePainter(
                            data = entry.imageUrl,
                            builder = {
                                crossfade(true)
                                transformations(
                                    object : Transformation {
                                        override fun key(): String {
                                            return entry.imageUrl
                                        }

                                        override suspend fun transform(
                                            pool: BitmapPool,
                                            input: Bitmap,
                                            size: coil.size.Size
                                        ): Bitmap {
                                            viewModel.calcDominantColor(input) { color ->
                                                dominantColor = color.toArgb()
                                                isLoading = false
                                            }
                                            return input
                                        }
                                    }
                                )
                            }
                        ),
                        contentDescription = entry.pokemonName,
                        modifier = Modifier
                            .size(120.dp)
                    )
            }

            Text(
                text = entry.pokemonName,
                fontFamily = RobotoCondensed,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}
