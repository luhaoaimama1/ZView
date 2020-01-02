package zone.com.zview.Activity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import zone.com.zview.entity.SortModel
import zone.com.zview.R
class SortAdapter2(val context: Context, private val stuList: List<SortModel>) : BaseAdapter() {

    override fun getCount(): Int {
        return stuList.size
    }

    override fun getItem(position: Int): Any {
        return stuList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_lv_item, null)
        val selection = getSelectionByPosition(position)    // 首字母字符
        val index = getPositionBySelection(selection)
        val textView = view.findViewById<TextView>(R.id.word)
        val title = view.findViewById<TextView>(R.id.title)
        if (position == index) {
            // 说明这个条目是第一个，需要显示字母
            textView.setVisibility(View.VISIBLE)
            textView.setText(stuList[position].sortLetter)
        } else
            textView.setVisibility(View.GONE)
        title.setText(stuList[position].name)
        return view
    }

    fun getSelectionByPosition(position: Int): Int {
        return stuList[position].sortLetter[0].toInt()
    }

    /**
     * 通过首字母获取显示该首字母的姓名的人，如：C,成龙
     * @author Xubin
     */
    fun getPositionBySelection(selection: Int): Int {
        for (i in 0 until getCount()) {
            val sortStr = stuList[i].sortLetter
            val firstChar = sortStr.toUpperCase()[0]
            if (firstChar.toInt() == selection) {
                return i
            }
        }
        return -1
    }
}
