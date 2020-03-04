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
import com.bumptech.glide.Glide
import org.json.JSONArray
import kotlin.math.roundToInt


val typeCount = 4
var isTypeOne =false
val TYPE_FULL = 0
val TYPE_HALF = 1
val TYPE_QUARTER = 2

class CellAdapter(private var context: Context,private var jsonArray: JSONArray) :
    RecyclerView.Adapter<CellAdapter.ViewHolder>(){
    lateinit var holder: ViewHolder

    var itemClick:ItemClick?=null;

    interface ItemClick {
        fun onCLick(v: View, position: Int)
    }

    fun setOnItemClickListener(itemClick: ItemClick) {
        this.itemClick = itemClick
    }

    inner class ViewHolder(itemView: View, var viewType: Int) : RecyclerView.ViewHolder(itemView) {
        var nameTextView: TextView? = null
        var imageView:ImageView?=null

        fun bindModel(position:Int) {
            imageView = itemView.findViewById(R.id.imageView)
            nameTextView = itemView.findViewById(R.id.nameTextView)
            val imageUrl = jsonArray.getJSONObject(position).optString("imgUrl")
            val name = jsonArray.getJSONObject(position).optString("name")
            Glide.with(context).load(imageUrl).into(imageView!!)
            nameTextView?.text=name

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
        return jsonArray?.length() ?:0
    }

    override fun getItemViewType(position: Int): Int {
        val type = jsonArray.getJSONObject(position).opt("type")
        when (type) {
            TYPE_HALF -> return TYPE_HALF
            TYPE_QUARTER -> {
                return TYPE_QUARTER
            }
        }
        return TYPE_FULL
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        jsonArray?:return
        holder?.bindModel(position)
        holder.itemView.setOnClickListener {
            if(itemClick!=null){
                itemClick!!.onCLick(holder.itemView,position)
            }
        }
    }

    fun setData(jsonArray:JSONArray){
        this.jsonArray=jsonArray
    }
}