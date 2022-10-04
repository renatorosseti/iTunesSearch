package com.rosseti.itunessearch.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.rosseti.domain.entity.ITunesEntity
import com.rosseti.itunessearch.R
import com.rosseti.itunessearch.ui.theme.sizeLarge128dp
import com.rosseti.itunessearch.ui.theme.sizeSmall16dp
import com.rosseti.itunessearch.ui.theme.sizeSmall4dp
import com.rosseti.itunessearch.ui.theme.sizeSmall8dp
import com.rosseti.itunessearch.ui.utils.Utils

@Composable
fun DetailsScreen(
    navController: NavController,
    song: ITunesEntity,
    searchText: String
) {
    val uriHandler = LocalUriHandler.current
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.app_name),
                    color = MaterialTheme.colors.secondary
                )
            },
            navigationIcon = {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null,
                    tint = MaterialTheme.colors.secondary,
                    modifier = Modifier.clickable {
                        navController.previousBackStackEntry?.savedStateHandle?.set(
                            Utils.SEARCH_TEXT_KEY,
                            searchText
                        )
                        navController.popBackStack()
                    })
            }
        )
    }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(MaterialTheme.colors.primary)
        ) {
            Column(
                Modifier
                    .padding(sizeSmall16dp)
                    .verticalScroll(rememberScrollState())
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(song.artworkUrl100)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = stringResource(R.string.app_name),
                    modifier = Modifier
                        .size(width = sizeLarge128dp, height = sizeLarge128dp)
                        .padding(bottom = sizeSmall16dp)
                )
                Row {
                    Text(
                        text = stringResource(R.string.artist),
                        color = MaterialTheme.colors.secondary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(start = sizeSmall16dp, top = sizeSmall16dp)
                    )
                    Text(
                        text = song.artistName,
                        color = MaterialTheme.colors.secondary,
                        modifier = Modifier.padding(
                            start = sizeSmall4dp,
                            top = sizeSmall16dp
                        )
                    )
                }
                Row {
                    Text(
                        text = stringResource(R.string.collection),
                        color = MaterialTheme.colors.secondary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(
                            start = sizeSmall16dp,
                            top = sizeSmall16dp
                        )
                    )
                    Text(
                        text = song.collectionName,
                        color = MaterialTheme.colors.secondary,
                        modifier = Modifier.padding(
                            start = sizeSmall4dp,
                            top = sizeSmall16dp
                        )
                    )
                }
                Row {
                    Text(
                        text = stringResource(R.string.genre),
                        color = MaterialTheme.colors.secondary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(
                            start = sizeSmall16dp,
                            top = sizeSmall16dp
                        )
                    )
                    Text(
                        text = song.primaryGenreName,
                        color = MaterialTheme.colors.secondary,
                        modifier = Modifier.padding(
                            start = sizeSmall4dp,
                            top = sizeSmall16dp,
                            bottom = sizeSmall8dp
                        )
                    )
                }
                if (song.longDescription.isNotEmpty()) {
                    Text(
                        text = song.longDescription,
                        color = MaterialTheme.colors.secondary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(sizeSmall16dp)
                    )
                }
                if (song.artistViewUrl.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.artist_details),
                        color = MaterialTheme.colors.primaryVariant,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(sizeSmall16dp)
                            .clickable {
                                uriHandler.openUri(song.artistViewUrl)
                            },
                    )
                }
                if (song.trackViewUrl.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.track_details),
                        color = MaterialTheme.colors.primaryVariant,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(sizeSmall16dp)
                            .clickable {
                                uriHandler.openUri(song.trackViewUrl)
                            },
                    )
                }
            }
        }
    }
}