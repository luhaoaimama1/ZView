package zone.com.zview

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.zone.LogCustomView
import com.zone.adapter3.QuickRcvAdapter
import com.zone.adapter3.bean.Holder
import com.zone.adapter3.bean.ViewDelegates
import com.zone.lib.LogZSDK
import com.zone.lib.ZLogger
import com.zone.lib.base.controller.activity.BaseFeatureActivity

import com.zone.lib.base.controller.activity.controller.SwipeBackActivityController
import kotlinx.android.synthetic.main.a_main.*

class MainActivity : BaseFeatureActivity() {
    companion object {
        //还原最开始的log配置  如果某次配置一次特殊的 打印完后的记得还原配置
        @JvmStatic
        fun initLogger() {
            ZLogger.logLevelList.clear()
            ZLogger.mayLoggerList.clear()
            ZLogger.mayLoggerList.addAll(listOf<ZLogger>(LogCustomView, LogZSDK))
        }
    }

    override fun initDefaultConifg() {
        unRegisterPrestener(SwipeBackActivityController::class.java)
    }

    override fun setContentView() {
        setContentView(R.layout.a_main)
        initLogger()

    }

    override fun onStoragePermit() {
        super.onStoragePermit()
    }

    override fun onStorageDeniedMustPermit() {
        super.onStorageDeniedMustPermit()
        finish()
    }


    override fun initData() {
        permissionCheckStorageMustPermit()
        val colorArry = intArrayOf(Color.WHITE, Color.GREEN, Color.YELLOW, Color.CYAN)
        listView.layoutManager = LinearLayoutManager(this)
        QuickRcvAdapter<MainMenu.MenuEntity>(this, MainMenu.menu)
                .addViewHolder(MenuEntityDeletates(this, colorArry))
                .relatedList(listView)

//
//        listView!!.setAdapter(object : QuickAdapter<MainMenu.MenuEntity>(this, MainMenu.menu) {
//            fun fillData(helper: Helper<MainMenu.MenuEntity>, menuEntity: MainMenu.MenuEntity, b: Boolean, i: Int) {
//                helper.setText(R.id.tv, menuEntity.content)
//                        .setBackgroundColor(R.id.tv, colorArry[helper.getPosition() % colorArry.size])
//                        .setOnClickListener(R.id.tv, View.OnClickListener { startActivity(Intent(this@MainActivity, menuEntity.cls)) })
//
//            }
//
//            fun getItemLayoutId(menuEntity: MainMenu.MenuEntity, i: Int): Int {
//                return R.layout.item_menu
//            }
//        })
    }

    override fun setListener() {

    }

    class MenuEntityDeletates(val activity: Activity, val colorArry: IntArray) : ViewDelegates<MainMenu.MenuEntity>() {
        override fun getLayoutId(): Int = R.layout.item_menu

        override fun fillData(posi: Int, entity: MainMenu.MenuEntity?, holder: Holder<out Holder<*>>?) {
            holder!!.setText(R.id.tv, entity!!.content)
                    .setBackgroundColor(R.id.tv, colorArry[holder.getAdapterPosition() % colorArry.size])
                    .setOnClickListener(View.OnClickListener {
                        activity.startActivity(Intent(activity, entity.cls))
                    }, R.id.tv)

        }

    }
}
