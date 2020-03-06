package lyon.video.sample

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.volley.VolleyError
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import lyon.video.sample.Dialog.SearchDialog
import lyon.video.sample.Mode.ListenerMode
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.locks.Lock
import kotlin.math.roundToInt


val spanCount =2;
var http = "https://api.github.com/search/users?q=keyWord&page=%p"//EX:https://api.github.com/search/users?q=lyonhsu
class MainActivity : AppCompatActivity() , ListenerMode {
    val TAG = MainActivity::class.java.simpleName
    lateinit var volletTools:VolletTool
    var keyWord = "Lyon"
    var jsonArray=JSONArray()
    lateinit var ryvAdapter:CellAdapter
    var lastItemPosition =0;
    var page =1
    var totalCount =0
    var canGetData = true
    val delayTime = 3*1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        init()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_search ->{
                object : SearchDialog(this){
                    override fun determine(kw: String) {
                        jsonArray = JSONArray()
                        page = 1
                        keyWord=kw
                        var httpUrl = http.replace("keyWord",keyWord)
                        httpUrl = httpUrl.replace("%p",page.toString())
                        CallJsonPath(httpUrl)
                    }
                }.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun init(){
        volletTools = VolletTool(this,this)
        var httpUrl = http.replace("keyWord",keyWord)
        httpUrl = httpUrl.replace("%p",page.toString())
        CallJsonPath(httpUrl)

        val mLayoutManager = StaggeredGridLayoutManager(spanCount,StaggeredGridLayoutManager.VERTICAL)
        ryvAdapter = CellAdapter(this, jsonArray)
        var context = this
        ryvAdapter.setOnItemClickListener(object :CellAdapter.ItemClick{
            override fun onCLick(v: View, position: Int) {
                val intent = Intent(context, WebViewActivity::class.java)
                val html_url = jsonArray.getJSONObject(position).optString("html_url")
                intent.putExtra("html_url",html_url.toString())
                startActivity(intent)
            }

        })
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.adapter = ryvAdapter
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                    lastItemPosition + 1 >= ryvAdapter.itemCount
                ) {
                    if (lastItemPosition < totalCount ) {
                            var httpUrl = http.replace("keyWord", keyWord)
                            httpUrl = httpUrl.replace("%p", page.toString())
                            CallJsonPath(httpUrl)
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val staggeredGridLayoutManager = recyclerView.layoutManager as StaggeredGridLayoutManager
                val lastPositions = IntArray(staggeredGridLayoutManager.spanCount)
                staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions)
                lastItemPosition = findMax(lastPositions)
            }

        })
    }

    fun findMax(lastPositions:IntArray):Int {
        var max = lastPositions[0]
        for ( i in lastPositions.indices) {
            val value = lastPositions[i]
            if (value > max) {
                max = value
            }
        }
        return max;
    }

    fun CallJsonPath(path:String){
        if(::volletTools.isInitialized) {
            volletTools.CallJsonPath(path)
        }
    }

    var handler= Handler(){
        AlertDialog.Builder(this).setTitle(getString(R.string.error)).setMessage(getString(R.string.net_error)).setCancelable(true).create().show()
        true
    }


    override fun getJsonArray(jsonArray: JSONArray,totalCount:Int) {
        this.totalCount=totalCount
        this.jsonArray=jsonArray
        page++
        if(::ryvAdapter.isInitialized) {
            ryvAdapter.setData(jsonArray)
            ryvAdapter.notifyDataSetChanged()
        }
    }

    override fun setOnErrorListener(error: VolleyError) {
        Log.e(TAG,"error http response:"+error)
        handler.sendEmptyMessage(0)
    }


}
