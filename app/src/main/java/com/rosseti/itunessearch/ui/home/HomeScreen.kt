package com.rosseti.itunessearch.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.colorResource
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
                Text(text = stringResource(id = R.string.app_name))
            },
        )
    }) {
        Surface(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
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
    val homeAction = viewModel.homeState.collectAsState().value
    when (homeAction) {
        is HomeViewModel.HomeAction.Successful -> {
            val list = homeAction.data
            LazyColumn {
                items(list) {
                    SongRow(
                        song = it,
                        navController = navController,
                    )
                }
            }
        }
        is HomeViewModel.HomeAction.Error -> {}
        is HomeViewModel.HomeAction.Loading -> {
            CircularProgressIndicator(
                Modifier
                    .padding(top = 180.dp)
            )
        }

    }
}

@Composable
fun SearchView(state: MutableState<TextFieldValue>, viewModel: HomeViewModel) {
    TextField(
        value = state.value,
        onValueChange = { value ->
            state.value = value
            if (value.text.isNotEmpty()) {
                viewModel.fetchSongs(value.text)
            }
        },
        modifier = Modifier
            .fillMaxWidth(),
        textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
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
        shape = RectangleShape, // The TextFiled has rounded corners top left and right by default
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.White,
            cursorColor = Color.White,
            leadingIconColor = Color.White,
            trailingIconColor = Color.White,
            backgroundColor = colorResource(id = R.color.purple_200),
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
        Modifier
            .padding(3.dp)
            .fillMaxWidth(),
    ) {
        Card(
            elevation = 2.dp,
            shape = RoundedCornerShape(corner = CornerSize(16.dp)),
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .height(100.dp)
                .fillMaxWidth()
                .combinedClickable(
                    onClick = {
                        navController.navigate(route = AppScreens.DetailsScreen.name + "/${song.artistName}")
                    },
                ),
        ) {
            Row {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(song.artistName)
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
                        color = Color.White
                    )
                }
            }
        }
    }
}
