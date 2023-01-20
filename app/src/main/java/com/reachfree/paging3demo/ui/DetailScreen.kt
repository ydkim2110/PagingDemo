package com.reachfree.paging3demo

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.util.Log.i
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Face
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import androidx.navigation.NavHostController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.reachfree.paging3demo.ui.MainViewModel
import com.reachfree.paging3demo.utils.HyperLinkText
import de.charlex.compose.HtmlText

@Composable
fun DetailScreen(
    navController: NavHostController,
    seq: String?,
    viewModel: MainViewModel
) {
    val deviceCurrentWidth = LocalConfiguration.current.screenWidthDp
    val context = LocalContext.current

    Scaffold(
        bottomBar = { BannersAd(deviceCurrentWidth) }
    ) { innerPadding ->
        if (seq != null) {
            LaunchedEffect(true) {
                viewModel.getJobDetail(seq = seq.toInt())
            }
            val state by viewModel.state.collectAsState()

            Log.d("DEBUG: ", "STATE : ${state.values}")

            if (state.isNotEmpty()) {
                val scrollState = rememberScrollState()
                Box(modifier = Modifier.padding(innerPadding)) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                            .verticalScroll(scrollState),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))
                        state.entries.forEach {
                            DetailRow(title = it.key, content = it.value, context = context)
                        }
                        HyperLinkText(
                            fullText = "금융투자협회 바로가기",
                            linkText = listOf("금융투자협회 바로가기"),
                            hyperlinks = listOf("https://www.kofia.or.kr/brd/m_96/view.do?seq=$seq")
                        )
                        Button(
                            onClick = { /*TODO*/ },
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .align(CenterHorizontally)
                        ) {
                            Image(imageVector = Icons.Rounded.Face, contentDescription = null)
                        }
                    }
                }
            }
        } else {
            Text(text = "NOTHING")
        }
    }
}

@Composable
fun BannersAd(deviceCurrentWidth: Int) {
    AndroidView(
        modifier = Modifier
            .fillMaxWidth(),
        factory = { context ->
            AdView(context).apply {
                adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                    context,
                    deviceCurrentWidth
                )
                adUnitId = context.getString(R.string.ad_id_banner)
                loadAd(AdRequest.Builder().build())
            }
        })
}

@Composable
fun Html(text: String) {
    AndroidView(factory = { context ->
        TextView(context).apply {
            setText(HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY))
        }
    })
}

@Composable
fun DetailRow(
    title: String,
    content: String,
    context: Context
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = title,
            fontSize = MaterialTheme.typography.subtitle1.fontSize,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(8.dp))


        if (title == "내용") {
            Html(text = content)
        } else {
            if (title == "사이트바로가기") {
                Text(
                    text = content,
                    modifier = Modifier.clickable {
                        try {
                            val url = if (content.contains("https://")) content
                                else "https://$content"

                            val urlIntent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(url)
                            )
                            context.startActivity(urlIntent)
                        } catch (e: ActivityNotFoundException) {
                            Log.d("DEBUG", "ERROR + ${e.message}")
                        }
                    },
                    color = Color.Gray,
                    textDecoration = TextDecoration.Underline
                )
            } else {
                Text(
                    text = content,
                    color = Color.Gray
                )
            }

        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
@Preview(showBackground = true)
@Composable
fun DetailRowPreview() {
    DetailRow(title = "제목", content = "[토마토투자자문] 운용지원팀 신입/경력 채용", LocalContext.current)
}