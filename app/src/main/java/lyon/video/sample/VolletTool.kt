package lyon.video.sample

import android.content.Context
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import lyon.video.sample.Mode.ListenerMode
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.roundToInt

val MY_SOCKET_TIMEOUT_MS = 30*1000


class VolletTool {
    val TAG = "VolletTool"
    internal var context: Context
    lateinit  var onCallStringBack: OnCallStringBack
    lateinit  var onCallJsonBack: OnCallJsonBack
    lateinit var onError: OnError
    lateinit var queue :RequestQueue
    var canGetData = true
    val delayTime = 3*1000
    var totalCount =0
    var jsonArray= JSONArray()
    var listenerMode: ListenerMode
    constructor(context: Context,listenerMode: ListenerMode){
        this.context = context
        this.listenerMode=listenerMode

        if(!::queue.isInitialized) {
            queue = Volley.newRequestQueue(this.context)
        }

    }

    fun CallJsonPath(httpUrl:String){
        if(!::queue.isInitialized){
            queue = Volley.newRequestQueue(context)
        }
        if(!canGetData)
            return
        canGetData = false
        handler.postDelayed(runnable, delayTime.toLong())
        val jsonRequest = JsonObjectRequest(
            Request.Method.GET, httpUrl,
             Response.Listener<JSONObject> {
                 processData(it)
             },  Response.ErrorListener {
                listenerMode.setOnErrorListener(it)
            }
        )

        jsonRequest.setRetryPolicy(
            DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        )
        // Add the request to the RequestQueue.
        if(!::queue.isInitialized)
            queue = Volley.newRequestQueue(context)
        queue.add(jsonRequest)
    }

    fun processData(response: JSONObject){
        canGetData = true
        val total_count = response.optInt("total_count")
        totalCount = total_count
        var array = response.getJSONArray("items")
        val len = array.length()
        var isisTypeOne = false
        for(i in 0..len-1){
            val jsonObject = array.optJSONObject(i)
            var name = jsonObject.optString("login")
            var imgUrl = jsonObject.optString("avatar_url")
            var html_url = jsonObject.optString("html_url")
            var type:Int = (Math.random()*3).roundToInt()
            if(isisTypeOne){
                isisTypeOne=false
                type = TYPE_QUARTER
            }else {
                if (type == TYPE_QUARTER) {
                    isisTypeOne = true
                }
            }
            when(type){
                0-> type = TYPE_FULL
                1-> type = TYPE_HALF
                2-> type = TYPE_QUARTER
            }
            val json = JSONObject()
            json.put("name",name)
            json.put("imgUrl",imgUrl)
            json.put("type",type)
            json.put("html_url",html_url)
            jsonArray.put(json)
        }
        listenerMode.getJsonArray(jsonArray,totalCount)
    }

    var handler= Handler()
    val runnable = object :Runnable {
        override fun run() {
            canGetData = true
            Log.e(TAG,"http canGetData:"+canGetData)
        }
    }

    interface OnCallStringBack {
        fun onResponse(response:String)
    }

    interface OnCallJsonBack{
        fun onResponse(response:JSONObject)
    }

    interface OnError {
        fun Error(error: VolleyError)
    }

    fun setOnCallStringBackListener(onCallStringBack: OnCallStringBack) {
        this.onCallStringBack = onCallStringBack
    }
    fun setOnCallJSONBackListener(onCallJsonBack: OnCallJsonBack) {
        this.onCallJsonBack = onCallJsonBack
    }
    fun setOnErrorListener(onError: OnError) {
        this.onError = onError
    }

}