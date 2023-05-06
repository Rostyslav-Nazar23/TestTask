package com.example.testtask

import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModelProvider
import com.example.testtask.ui.theme.TestTaskTheme

private var DATAOBJECT: DataObject? = null
private val OBJECTLIST: MutableList<ContentObject> = ArrayList()
private var CURRENTID = 0

class MainActivity : ComponentActivity() {

    private val viewModel: SharedRepositoryViewModel by lazy {
        ViewModelProvider(this)[SharedRepositoryViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.refreshDataObject()

        setContent {
            TestTaskTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoadingLayout(OBJECTLIST)
                }
            }
        }

        viewModel.dataObjectLiveData.observe(this) { response ->
            if (response == null) {
                Toast.makeText(
                    this@MainActivity,
                    "Unsuccessful network call while getting id list object",
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.refreshDataObject()
            }
            DATAOBJECT = response
        }
    }

    fun getContentObjectList() {
        for (item in DATAOBJECT!!.data) {
            viewModel.refreshContentObject(item.id)
            viewModel.contentObjectLiveData.observe(this) { response ->
                if (response == null) {
                    Toast.makeText(
                        this@MainActivity,
                        "Unsuccessful network call while getting content",
                        Toast.LENGTH_SHORT
                    ).show()
                    viewModel.refreshContentObject(item.id)
                }
                OBJECTLIST.add(response!!)
            }
        }
    }
}

@Composable
fun LoadingLayout(objectList: MutableList<ContentObject>?) {
    if (objectList == null) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    } else {

    }
}

@Composable
fun textDisplay(contentObject: ContentObject) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = contentObject.message!!)
    }
    Button(onClick = {

    }) {

    }
}

@Composable
fun webViewDisplay(contentObject: ContentObject) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AndroidView(factory = {
            WebView(it).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = WebViewClient()
                loadUrl(contentObject.url!!)
            }
        }, update = {
            it.loadUrl(contentObject.url!!)
        })
    }
}

@Composable
fun imageDisplay(contentObject: ContentObject) {

}

@Composable
fun nextObjectFromList() {
    var nextId = CURRENTID + 1
    if (nextId > OBJECTLIST.size) {
        nextId = 0
    }
    if (OBJECTLIST[nextId].type.equals("text")) {
        textDisplay(contentObject = OBJECTLIST[nextId])
    } else if (OBJECTLIST[nextId].type.equals("webview")) {

    } else if (OBJECTLIST[nextId].type.equals("image")) {

    } else {

    }
}
