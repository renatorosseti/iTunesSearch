package com.rosseti.itunessearch.ui.home

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

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val textState = remember { mutableStateOf(TextFieldValue("")) }

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
                SearchView(textState, viewModel = viewModel)
                SoundList(navController = navController, viewModel = viewModel)
            }
        }
    }
}

@Composable
fun SoundList(navController: NavController, viewModel: HomeViewModel) {
    when (val homeAction = viewModel.homeState.collectAsState().value) {
        is HomeViewModel.HomeAction.Successful -> {
            val list = homeAction.data
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                items(list) {
                    SongRow(
                        song = it,
                        navController = navController,
                    )
                }
            }
        }
        is HomeViewModel.HomeAction.Error -> {
        }
        is HomeViewModel.HomeAction.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
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
                    .padding(15.dp)
                    .size(24.dp)
            )
        },
        trailingIcon = {
            if (state.value != TextFieldValue("")) {
                IconButton(
                    onClick = {
                        state.value =
                            TextFieldValue("") // Remove text from TextField when you press the 'X' icon
                    }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(15.dp)
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
    song: ITunesEntity,
    navController: NavController,
) {
    Surface(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        color = MaterialTheme.colors.primary
    ) {
        Card(
            elevation = 2.dp,
            shape = MaterialTheme.shapes.medium,
            backgroundColor = MaterialTheme.colors.primary,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .height(100.dp)
                .fillMaxWidth()
                .combinedClickable(
                    onClick = {
                        navController.currentBackStackEntry?.savedStateHandle?.set("song", song)
                        navController.navigate(route = AppScreens.DetailsScreen.name)
                    },
                ),
        ) {
            Row(Modifier.padding(8.dp)) {
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
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = song.artistName,
                        color = MaterialTheme.colors.secondary
                    )
                    Text(
                        text = song.collectionName,
                        color = MaterialTheme.colors.secondary
                    )
                }
            }
        }
    }
}
