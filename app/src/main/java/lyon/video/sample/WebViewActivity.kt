package lyon.video.sample

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.webview_layout.*

class WebViewActivity : AppCompatActivity(){
    val TAG = WebViewActivity::class.java.simpleName
    lateinit var context: Context


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        setContentView(R.layout.webview_layout)
        val html_url = intent.getStringExtra("html_url")
        Log.e(TAG,"html_url:"+html_url)
        webView.loadUrl(html_url)
    }

}