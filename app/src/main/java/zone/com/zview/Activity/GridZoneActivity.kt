package zone.com.zview.Activity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.bumptech.glide.Glide
import com.zone.lib.base.controller.activity.BaseFeatureActivity
import com.zone.view.ninegridview.TouchGreyImageView
import com.zone.view.ninegridview.ZGridView
import com.zone.view.ninegridview.ZGridViewAdapter
import com.zone.view.ninegridview.preview.ImageInfo
import com.zone.view.ninegridview.preview.ImagePreviewAdapter
import java.util.ArrayList
import java.util.Arrays
import kotlinx.android.synthetic.main.a_gridzone.*
import zone.com.zview.Images
import zone.com.zview.R

/**
 * Created by Administrator on 2016/4/12.
 */
class GridZoneActivity : BaseFeatureActivity() {

    override fun setContentView() {
        setContentView(R.layout.a_gridzone)
    }


    override fun initData() {
        val list = Arrays.asList(*Images.imageThumbUrls)
        val sub = list.subList(0, 5)
        gvz.setColumns(3)
        gvz.setGap(10)
        gvz.setAdapter(object : ZGridViewAdapter<String>(sub) {
            override fun onItemImageClick(context: Context, index: Int, data: String) {
                println("onItemImageClick index:$index")
            }

            override fun onItemImageLongClick(context: Context, index: Int, data: String) {
                println("onItemImageLongClick index:$index")
            }

            override fun getView(context: Context, index: Int): View {
                val item = LayoutInflater.from(context).inflate(R.layout.grid_item_pic, null)
                val tgiv = item.findViewById<View>(R.id.tgiv) as TouchGreyImageView
                //                tgiv.setClickable(true);
                Glide.with(context).load(sub[index]).into(tgiv)
                return item
            }
        })
    }

    override fun setListener() {

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v!!.id) {
            R.id.bt_change -> {
                val list = Arrays.asList(*Images.imageThumbUrls)
                val sub = list.subList(0, 6)
                gvz.setColumns(3)
                gvz.setGap(10)
                val listIf = ArrayList<ImageInfo>()
                for (s in sub) {
                    listIf.add(ImageInfo(s, s))
                }
                gvz.setAdapter(ImagePreviewAdapter(listIf))
            }
        }
    }
}
