package zone.com.zview.Activity

import com.zone.lib.base.controller.activity.BaseFeatureActivity
import kotlinx.android.synthetic.main.square_test.*

import zone.com.zview.R

/**
 * Created by fuzhipeng on 16/8/26.
 */
class SquareTestActivity : BaseFeatureActivity() {


    override fun initData() {
    }

    override fun setContentView() {
        setContentView(R.layout.square_test)
    }

    override fun setListener() {
        println("----SquareTestActivity----")
        sqv.setOnClickListener {
            Runnable { println("width:" + sqv.getWidth() + "_height:" + sqv.getHeight()) }
        }
    }
}
