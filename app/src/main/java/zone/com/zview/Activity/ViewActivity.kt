package zone.com.zview.Activity

import com.zone.lib.base.controller.activity.BaseFeatureActivity

import zone.com.zview.view.SinView

/**
 * Created by fuzhipeng on 2016/11/18.
 */

class ViewActivity : BaseFeatureActivity() {

    override fun setContentView() {
        setContentView(SinView(this))
    }

    override fun initData() {

    }

    override fun setListener() {

    }
}
