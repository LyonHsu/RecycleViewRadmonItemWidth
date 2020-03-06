package lyon.video.sample.Mode

import com.android.volley.VolleyError
import org.json.JSONArray

interface ListenerMode {
    fun getJsonArray(jsonArray: JSONArray,totalCount:Int)
    fun setOnErrorListener(error: VolleyError)
}