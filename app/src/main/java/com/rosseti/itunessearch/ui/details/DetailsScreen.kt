package com.rosseti.itunessearch.ui.details

import android.util.Log
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.rosseti.domain.entity.ITunesEntity
import com.rosseti.itunessearch.R

@Composable
fun DetailsScreen(
    navController: NavController,
    song: ITunesEntity,
    searchText: String
) {
    Log.i("HomeScreen", "Search details: $searchText")
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
                        navController.previousBackStackEntry?.savedStateHandle?.set("search", searchText)
                        navController.popBackStack()
                    })
            }
        )
    }) {
        Surface(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
                .fillMaxHeight()
                .background(MaterialTheme.colors.primary)
        ) {
            Column(
                Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(song.artworkUrl100)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = Modifier
                        .width(128.dp)
                        .height(128.dp)
                )
                Text(
                    text = song.artistName,
                    color = MaterialTheme.colors.secondary,
                    modifier = Modifier
                        .padding(16.dp)
                )
                Text(
                    text = song.collectionName,
                    color = MaterialTheme.colors.secondary,
                    modifier = Modifier
                        .padding(16.dp)
                )
                Text(
                    text = song.longDescription,
                    color = MaterialTheme.colors.secondary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                Text(
                    text = "Artist details",
                    color = MaterialTheme.colors.primaryVariant,
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            uriHandler.openUri(song.artistViewUrl)
                        },
                )
                Text(
                    text = "Collection details",
                    color = MaterialTheme.colors.primaryVariant,
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            uriHandler.openUri(song.collectionViewUrl)
                        },
                )
                Text(
                    text = "Track details",
                    color = MaterialTheme.colors.primaryVariant,
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            uriHandler.openUri(song.trackViewUrl)
                        },
                )
            }
        }
    }
}