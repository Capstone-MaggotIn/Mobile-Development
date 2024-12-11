package com.capstone.maggotin.ui.article

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.capstone.maggotin.R

class DetailArticleActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_article)
        supportActionBar?.hide()

        webView = findViewById(R.id.webView)
        progressBar = findViewById(R.id.progressBar)

        val url = intent.getStringExtra("URL")
        if (url != null) {
            progressBar.visibility = android.view.View.VISIBLE
            webView.loadUrl(url)
        }

        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.setSupportMultipleWindows(true)
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.userAgentString =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar.visibility = android.view.View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar.visibility = android.view.View.GONE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        webView.destroy()
    }
}
