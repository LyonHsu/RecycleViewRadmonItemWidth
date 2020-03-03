package lyon.video.sample

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.volley.VolleyError
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import lyon.video.sample.Dialog.SearchDialog
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.locks.Lock
import kotlin.math.roundToInt


val spanCount =2;
var http = "https://api.github.com/search/users?q=keyWord&page=%p"//EX:https://api.github.com/search/users?q=lyonhsu
class MainActivity : AppCompatActivity() {
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
    val Error =1

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
        volletTools = VolletTool(this)
        var httpUrl = http.replace("keyWord",keyWord)
        httpUrl = httpUrl.replace("%p",page.toString())
        CallJsonPath(httpUrl)
        volletTools.setOnCallJSONBackListener(object : VolletTool.OnCallJsonBack{
            override fun onResponse(response: JSONObject) {
                page++
                canGetData = true
                handler.removeCallbacks(runnable)
                getJsonArray(response)
            }

        })
        volletTools.setOnErrorListener(object : VolletTool.OnError{
            override fun Error(error: VolleyError) {
                Log.e(TAG,"error http response:"+error)
                handler.sendEmptyMessage(0)
            }
        })
        val mLayoutManager = StaggeredGridLayoutManager(spanCount,StaggeredGridLayoutManager.VERTICAL)
        ryvAdapter = CellAdapter(this, jsonArray)
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.adapter = ryvAdapter
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                    lastItemPosition + 1 == jsonArray.length()
                ) {
                    if (lastItemPosition < totalCount ) {
                        if(canGetData) {
                            canGetData = false
                            var httpUrl = http.replace("keyWord", keyWord)
                            httpUrl = httpUrl.replace("%p", page.toString())
                            CallJsonPath(httpUrl)
                        }
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

    fun  findMax(lastPositions:IntArray):Int {
        var max = lastPositions[0]
        for ( i in lastPositions.indices) {
            val value = lastPositions[i]
            if (value > max) {
                max = value
            }
        }
        return max;
    }

    fun getJsonArray(response:JSONObject){
        val total_count = response.optInt("total_count")
        totalCount = total_count

        var array = response.getJSONArray("items")
        val len = array.length()
        var isisTypeOne = false
        for(i in 0..len-1){
            val jsonObject = array.optJSONObject(i)
            var name = jsonObject.optString("login")
            var imgUrl = jsonObject.optString("avatar_url")
            var type:Int = (Math.random()*3).roundToInt()
            if(isisTypeOne){
                isisTypeOne=false
                type = TYPE_QUARTER
            }else {
                if (type == TYPE_QUARTER) {
                    isisTypeOne = true
                }
            }
            val json = JSONObject()
            json.put("name",name)
            json.put("imgUrl",imgUrl)
            json.put("type",type)

            jsonArray.put(json)
        }

        if(::ryvAdapter.isInitialized) {
            ryvAdapter.setData(jsonArray)
            ryvAdapter.notifyDataSetChanged()
        }
    }

    fun CallJsonPath(path:String){
        if(::volletTools.isInitialized) {
            volletTools.CallJsonPath(path)
            canGetData = false
            handler.postDelayed(runnable, delayTime.toLong())
        }
    }

    var handler= Handler(){
        AlertDialog.Builder(this).setTitle(getString(R.string.error)).setMessage(getString(R.string.net_error)).setCancelable(true).create().show()
        true
    }
    val runnable = object :Runnable {
        override fun run() {
            canGetData = true
            Log.e(TAG,"http canGetData:"+canGetData)
        }
    }
}
