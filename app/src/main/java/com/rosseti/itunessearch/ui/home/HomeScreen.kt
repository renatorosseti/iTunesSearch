package com.rosseti.itunessearch.ui.home

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.rosseti.domain.entity.ITunesEntity
import com.rosseti.itunessearch.R
import com.rosseti.itunessearch.navigation.AppScreens
import com.rosseti.itunessearch.ui.utils.Utils

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
    searchText: String
) {
    val searchState = remember { mutableStateOf(TextFieldValue(searchText)) }

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.app_name),
                    color = MaterialTheme.colors.secondary
                )
            },
            backgroundColor = MaterialTheme.colors.primary
        )
    }) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colors.primary
        ) {
            Column {
                SearchView(searchState, viewModel = viewModel)
                SoundList(
                    searchState = searchState,
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun SoundList(
    searchState: MutableState<TextFieldValue>,
    navController: NavController,
    viewModel: HomeViewModel
) {
    when (val homeAction = viewModel.homeState.collectAsState().value) {
        is HomeViewModel.HomeAction.Successful -> {
            val list = homeAction.data
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(list) {
                    SongRow(
                        searchState = searchState,
                        song = it,
                        navController = navController,
                    )
                }
            }
        }
        is HomeViewModel.HomeAction.Error -> {
            ShowHomeInfoMessage(
                title = stringResource(R.string.error_title),
                subtitle = stringResource(R.string.error_subtitle)
            )
        }
        is HomeViewModel.HomeAction.Empty -> {
            ShowHomeInfoMessage(
                title = stringResource(R.string.empty_list_title),
                subtitle = stringResource(R.string.empty_list_subtitle)
            )
        }
        is HomeViewModel.HomeAction.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colors.primaryVariant
                )
            }
        }
    }
}

@Composable
fun SearchView(state: MutableState<TextFieldValue>, viewModel: HomeViewModel) {
    TextField(
        value = state.value,
        onValueChange = { value ->
            state.value = value
            viewModel.fetchSongs(value.text)
        },
        modifier = Modifier
            .fillMaxWidth(),
        textStyle = TextStyle(color = MaterialTheme.colors.secondary, fontSize = 18.sp),
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "",
                modifier = Modifier
                    .padding(16.dp)
                    .size(24.dp)
            )
        },
        trailingIcon = {
            if (state.value != TextFieldValue("")) {
                IconButton(
                    onClick = {
                        // Remove text from TextField when you press the 'X' icon
                        state.value = TextFieldValue("")
                        viewModel.fetchSongs(state.value.text)
                    }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(16.dp)
                            .size(24.dp)
                    )
                }
            }
        },
        singleLine = true,
        shape = RectangleShape,
        colors = TextFieldDefaults.textFieldColors(
            textColor = MaterialTheme.colors.secondary,
            cursorColor = MaterialTheme.colors.secondary,
            leadingIconColor = MaterialTheme.colors.secondary,
            trailingIconColor = MaterialTheme.colors.secondary,
            backgroundColor = MaterialTheme.colors.primaryVariant,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SongRow(
    searchState: MutableState<TextFieldValue>,
    song: ITunesEntity,
    navController: NavController,
) {
    Surface(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
    ) {
        Card(
            elevation = 2.dp,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .padding(8.dp)
                .requiredHeightIn(min = 120.dp, max = 400.dp)
                .fillMaxWidth()
                .combinedClickable(
                    onClick = {
                        setStateHandle(navController, song, searchState)
                        navController.navigate(route = AppScreens.DetailsScreen.name)
                    },
                ),
        ) {
            Row(Modifier.padding(16.dp)) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(song.artworkUrl100)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = stringResource(id = R.string.app_name),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .align(Alignment.CenterVertically)
                )
                Column {
                    Row(
                        modifier = Modifier
                            .padding(start = 8.dp, top = 4.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Artist:",
                            color = MaterialTheme.colors.secondary
                        )
                        Text(
                            text = song.artistName,
                            color = MaterialTheme.colors.secondary,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .padding(start = 8.dp, top = 4.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Collection:",
                            color = MaterialTheme.colors.secondary
                        )
                        Text(
                            text = song.collectionName,
                            color = MaterialTheme.colors.secondary,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .padding(start = 8.dp, top = 4.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Track:",
                            color = MaterialTheme.colors.secondary
                        )
                        Text(
                            text = song.trackName,
                            color = MaterialTheme.colors.secondary,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

private fun setStateHandle(
    navController: NavController,
    song: ITunesEntity,
    searchState: MutableState<TextFieldValue>
) {
    navController.currentBackStackEntry?.savedStateHandle?.set(
        Utils.SONG_KEY,
        song
    )
    navController.currentBackStackEntry?.savedStateHandle?.set(
        Utils.SEARCH_TEXT_KEY,
        searchState.value.text
    )
}

@Composable
fun ShowHomeInfoMessage(title: String, subtitle: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.secondary
        )
        Text(
            text = subtitle,
            color = MaterialTheme.colors.secondary
        )
    }
}
