package lyon.video.sample.Dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import kotlinx.android.synthetic.main.search_dialog.*
import lyon.video.sample.R
import org.json.JSONObject


abstract class SearchDialog(context: Context) : Dialog(context){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_dialog)
        init()
    }


    fun init(){
        cancel_btn.setOnClickListener {
            dismiss()
        }

        determine_btn.setOnClickListener {
            determine(search_key_edt.text.toString())
            dismiss()
        }
    }


    abstract fun determine(keyWord:String)

}