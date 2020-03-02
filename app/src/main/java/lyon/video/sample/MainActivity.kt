package lyon.video.sample

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.StaggeredGridLayoutManager

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import lyon.video.sample.Dialog.SearchDialog
import org.json.JSONArray

val spanCount =2;
class MainActivity : AppCompatActivity() {

    var keyWord = ""
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
                    override fun determine(keyWord: String) {

                    }

                }.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun init(){
        val mLayoutManager = StaggeredGridLayoutManager(spanCount,StaggeredGridLayoutManager.VERTICAL)
        var ryvAdapter = CellAdapter(this, JSONArray())
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.adapter = ryvAdapter

    }
}
