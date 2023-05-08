package com.example.testtask

import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.testtask.ui.theme.TestTaskTheme


private var IDOBJECTLIST: MutableList<IdObject> = listOf(IdObject(1)) as MutableList<IdObject>
private var CURRENTOBJECT: ContentObject? = null
private var CURRENTID = 1

class MainActivity : ComponentActivity() {

    private val viewModel: SharedRepositoryViewModel by lazy {
        ViewModelProvider(this)[SharedRepositoryViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.dataObjectLiveData.observe(this) { response ->
            if (response == null) {
                Toast.makeText(
                    this@MainActivity,
                    "Unsuccessful network call while getting id list object",
                    Toast.LENGTH_SHORT
                ).show()
                return@observe
            } else {
                IDOBJECTLIST = response.data
                CURRENTID = IDOBJECTLIST.component1().id
                viewModel.contentObjectLiveData.observe(this) { response ->
                    if (response == null) {
                        Toast.makeText(
                            this@MainActivity,
                            "Unsuccessful network call while getting content",
                            Toast.LENGTH_SHORT
                        ).show()
                        viewModel.refreshContentObject(CURRENTID)
                        return@observe
                    } else if (!response.type.equals("text")
                        && !response.type.equals("image")
                        && !response.type.equals("webview")
                    ) {
                        CURRENTID += 1
                        if (CURRENTID > IDOBJECTLIST.size) {
                            CURRENTID = 1
                        }
                        viewModel.refreshContentObject(CURRENTID)
                    } else {
                        CURRENTOBJECT = response
                    }
                }
                viewModel.refreshContentObject(CURRENTID)
            }
        }
        viewModel.refreshDataObject()

        setContent {
            TestTaskTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: SharedRepositoryViewModel) {
    val contentObject = viewModel.contentObjectLiveData.observeAsState()

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(700.dp), Alignment.Center
        ) {
            if (contentObject.value == null) {
                CircularProgressIndicator()
            } else if (contentObject.value!!.type.equals("text")) {
                contentObject.value!!.message?.let { Text(it) }
            } else if (contentObject.value!!.type.equals("webview")) {
                WebDisplay(viewModel = viewModel)
            } else if (contentObject.value!!.type.equals("image")) {
                ImageDisplay(viewModel = viewModel)
            }
        }
        NextButton(viewModel = viewModel)
    }
}

@Composable
fun NextButton(viewModel: SharedRepositoryViewModel) {
    Button(modifier = Modifier
        .fillMaxWidth()
        .height(64.dp), onClick = {
        CURRENTID += 1
        if (CURRENTID > IDOBJECTLIST.size) {
            CURRENTID = 1
        }
        viewModel.refreshContentObject(CURRENTID)
    }) {
        Text(text = "Next", fontSize = 20.sp)
    }
}

@Composable
fun ImageDisplay(viewModel: SharedRepositoryViewModel) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data("http: //www.w3.org/2000/svg")
            .decoderFactory(SvgDecoder.Factory())
            .build(),
        contentDescription = null,
        modifier = Modifier.size(300.dp)
    )
}

@Composable
fun WebDisplay(viewModel: SharedRepositoryViewModel) {

    // Declare a string that contains a url
    val mUrl = viewModel.contentObjectLiveData.value!!.url!!

    // Adding a WebView inside AndroidView
    // with layout as full screen
    AndroidView(factory = {
        WebView(it).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            loadUrl(mUrl)
        }
    }, update = {
        it.loadUrl(mUrl)
    })
}
