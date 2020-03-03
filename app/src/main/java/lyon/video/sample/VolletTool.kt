package lyon.video.sample

import android.content.Context
import android.widget.Toast
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

val MY_SOCKET_TIMEOUT_MS = 30*1000


class VolletTool {
    val TAG = "VolletTool"
    internal var context: Context
    lateinit  var onCallStringBack: OnCallStringBack
    lateinit  var onCallJsonBack: OnCallJsonBack
    lateinit var onError: OnError
    lateinit var queue :RequestQueue
    constructor(context: Context){
        this.context = context
        if(!::queue.isInitialized) {
            queue = Volley.newRequestQueue(this.context)
        }
    }

    fun CallStringPath(httpUrl:String)
    {
        if(!::queue.isInitialized){
            queue = Volley.newRequestQueue(context)
        }

        val stringRequest = StringRequest(
            Request.Method.GET, httpUrl,
            Response.Listener<String> { response ->
                if(::onCallStringBack.isInitialized){
                    onCallStringBack.onResponse(response)
                }
            },
            Response.ErrorListener {
                if(::onError.isInitialized){
                    onError.Error(it)
                }
            }
        )

        stringRequest.setRetryPolicy(
            DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        )
        // Add the request to the RequestQueue.
        if(!::queue.isInitialized)
            queue = Volley.newRequestQueue(context)
        queue.add(stringRequest)
    }

    fun CallJsonPath(httpUrl:String){
        if(!::queue.isInitialized){
            queue = Volley.newRequestQueue(context)
        }

        val jsonRequest = JsonObjectRequest(
            Request.Method.GET, httpUrl,
             Response.Listener<JSONObject> {
                 if(::onCallJsonBack.isInitialized){
                     onCallJsonBack.onResponse(it)
                 }
             },  Response.ErrorListener {
                if(::onError.isInitialized){
                    onError.Error(it)
                }
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