package com.reachfree.paging3demo.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.reachfree.paging3demo.R
import com.reachfree.paging3demo.data.network.Job

@Composable
fun UserList(
    usersList: LazyPagingItems<Job>,
    userCardClicked: (seq: String) -> Unit
) {
    val listState = rememberLazyListState()

    LazyColumn(
        contentPadding = PaddingValues(vertical = 56.dp),
        state = listState,
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        items(usersList) { item ->
            item?.let {
                UserCard(job = it) { job ->
                    userCardClicked(job.seq)
                }
            }
        }

        when (usersList.loadState.append) {
            is LoadState.NotLoading -> Unit
            LoadState.Loading -> {
                item {
                    LoadingItem()
                }
            }
            is LoadState.Error -> {
                item {
                    ErrorItem(message = "Some error occurred")
                }
            }
        }

        when (usersList.loadState.refresh) {
            is LoadState.NotLoading -> Unit
            LoadState.Loading -> {
                item { 
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
            is LoadState.Error -> {
                item {

                }
            }
        }
    }
}

@Composable
fun UserCard(
    job: Job,
    onClicked: (job: Job) -> Unit
) {
    Card(
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onClicked(job) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = job.title,
                    fontSize = 18.sp,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Text(
                        text = job.name,
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = job.regDate,
                        fontSize = 16.sp,
                        color = Color.Gray,
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Image(imageVector = Icons.Default.Call, contentDescription = null)
        }

    }
    
}

@Composable
fun LoadingItem() {
    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(),
        contentAlignment = Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .width(42.dp)
                .height(42.dp)
                .padding(8.dp),
            strokeWidth = 5.dp
        )
    }
}

@Composable
fun ErrorItem(message: String) {
    Card(
        elevation = 2.dp,
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Red)
                .padding(8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_error),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier
                    .clip(CircleShape)
                    .width(42.dp)
                    .height(42.dp)
            )
            Text(
                text = message,
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .align(CenterVertically)
            )
        }
    }
}