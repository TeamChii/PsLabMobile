package com.example.pslabmobile

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import org.json.JSONObject
import java.io.BufferedReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ExecutorService

import java.util.concurrent.Executors
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var executor: ExecutorService;

    var gson = Gson();
    companion object {
        val WEBVIEW_JS = "WebViewJS"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        executor = Executors.newSingleThreadExecutor();

        val uri = intent.data
        if (uri != null && uri.host == "login") {

            Toast.makeText(baseContext, "login success", Toast.LENGTH_LONG).show();
            Log.d("team","aaa")

        } else {
            Log.d("team","bbb")
            webView.addJavascriptInterface(this, WEBVIEW_JS)
            webView.settings.apply {
                javaScriptEnabled = true
            }

            webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    view?.loadUrl(url)
                    return true

                }
            }


            webView.loadUrl("https://ps-lab-hackathon.web.app/customer");
            connectAPI();
        }
    }

    @JavascriptInterface
    fun showAlert(message: String) {
        runOnUiThread {
            Toast.makeText(baseContext, message, Toast.LENGTH_LONG).show();
        }
    }

    private fun connectAPI() {
        var runnable = Runnable {

            var url = URL("https://api.partners.scb/partners/sandbox/v2/oauth/authorize");
            val connection = url.openConnection() as HttpURLConnection

            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("accept-language", "EN");
            connection.setRequestProperty("apikey", "l779c1f7e50cfb48edb3d8cc7fc7959964");
            connection.setRequestProperty("apisecret", "bd9735c3991d43b59fbe584fcc619d8f");
            connection.setRequestProperty("endState", "mobile_app");
            connection.setRequestProperty("requestUId", "c385f890-ba04-4973-9939-98ce407ed740");
            connection.setRequestProperty("resourceOwnerId", "l779c1f7e50cfb48edb3d8cc7fc7959964");
            connection.setRequestProperty("response-channel", "mobile")
            connection.requestMethod = "GET"

            val responseCode = connection.responseCode
            Log.d("SCB", "responseCode = $responseCode")


            if (responseCode == 200) {
                val reader = BufferedReader(connection.inputStream.reader())
                var jsonString: String
                try {
                    jsonString = reader.readText()
                } finally {
                    reader.close()
                }

                val json = JSONObject(jsonString)

                runOnUiThread {
                    runJSON(json);
                }

                Log.d("SCB", "json = $json")

            }
        };

        executor.submit(runnable)
    }


    fun runJSON(json: JSONObject) {
//        json.get("")
    }

    private fun postAPI() {
        var runnable = Runnable {
            var url = URL("https://api.partners.scb/partners/sandbox/v2/oauth/authorize");
            val connection = url.openConnection() as HttpURLConnection

            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("accept-language", "EN");
            connection.setRequestProperty("apikey", "l779c1f7e50cfb48edb3d8cc7fc7959964");
            connection.setRequestProperty("apisecret", "bd9735c3991d43b59fbe584fcc619d8f");
            connection.setRequestProperty("endState", "mobile_app");
            connection.setRequestProperty("requestUId", "c385f890-ba04-4973-9939-98ce407ed740");
            connection.setRequestProperty("resourceOwnerId", "l779c1f7e50cfb48edb3d8cc7fc7959964");
            connection.setRequestProperty("response-channel", "mobile")

            connection.doOutput = true
            connection.requestMethod = "POST"

            val os = connection.outputStream
            val osw = OutputStreamWriter(os, "UTF-8")

            var jsonObject = JSONObject();

            jsonObject.put("accountNo", "1234");
            jsonObject.put("aaa", "asda")

            osw.write(jsonObject.toString())

            osw.flush()
            osw.close()
            os.close()
            connection.connect()

            val responseCode = connection.responseCode
            if (responseCode == 200) {
                val reader = BufferedReader(connection.inputStream.reader())
                var jsonString: String
                try {
                    jsonString = reader.readText()
                } finally {
                    reader.close()
                }

                val json = JSONObject(jsonString)

                Log.d("SCB", "json = $json")

            }
        }
        executor.submit(runnable)
    }

}
