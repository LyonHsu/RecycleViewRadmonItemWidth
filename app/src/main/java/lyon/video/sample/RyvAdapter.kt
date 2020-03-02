package lyon.video.sample

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import org.json.JSONArray


val typeCount = 4
var isTypeOne =false
val TYPE_FULL = 0
val TYPE_HALF = 1
val TYPE_QUARTER = 2

class CellAdapter(private var context: Context,private var jsonArray: JSONArray) :
    RecyclerView.Adapter<CellAdapter.ViewHolder>(){
    lateinit var holder: ViewHolder


    inner class ViewHolder(itemView: View, var viewType: Int) : RecyclerView.ViewHolder(itemView) {
        var nameTextView: TextView? = null


        fun bindModel(position:Int) {
            var imageView: ImageView? = null
            imageView = itemView.findViewById(R.id.image)
            nameTextView = itemView.findViewById(R.id.nameTextView)
//            val imageUrl = jsonArray.getJSONObject(position).optString("avatar")
//            val name = jsonArray.getJSONObject(position).optString("name")
//            Glide.with(context).load(imageUrl).into(imageView!!)
            nameTextView?.text=position.toString()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_cell, parent, false);
        view.tag = viewType
        view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.recycle_cell_3, parent, false);
        view.getViewTreeObserver()
            .addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    val lp: ViewGroup.LayoutParams = view.getLayoutParams()
                    if (lp is StaggeredGridLayoutManager.LayoutParams) {
                        val sglp = lp
                            when (viewType) {
                                TYPE_FULL -> sglp.isFullSpan = true
                                TYPE_HALF -> {
                                    sglp.isFullSpan = true
                                    sglp.height = context.resources.getDimension(R.dimen.item_h).toInt()
                                }
                                TYPE_QUARTER -> {
                                    sglp.isFullSpan = false
                                    sglp.width = context.resources.getDimension(R.dimen.item_w).toInt()
                                    sglp.height = context.resources.getDimension(R.dimen.item_h).toInt()
                                }
                            }

                        view.setLayoutParams(sglp)
                        val lm =
                            (parent as RecyclerView).layoutManager as StaggeredGridLayoutManager?
                        lm!!.invalidateSpanAssignments()
                    }
                    view.getViewTreeObserver().removeOnPreDrawListener(this)
                    return true
                }
            })

        val viewHolder = ViewHolder(view,viewType)

        this.holder=viewHolder
        return holder
    }

    override fun getItemCount(): Int {
        return 50;//jsonArray?.length() ?:0
    }

    override fun getItemViewType(position: Int): Int { // 瀑布流样式外部设置spanCount为3，在这列设置两个不同的item type，以区分不同的布局
        val modeEight = position % typeCount
        when (modeEight) {
            0 -> return TYPE_HALF
            1,2 -> return TYPE_QUARTER
        }
        return TYPE_FULL
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        jsonArray?:return
        holder?.bindModel(position)

    }
}