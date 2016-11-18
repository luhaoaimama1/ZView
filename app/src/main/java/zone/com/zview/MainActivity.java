package zone.com.zview;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.zone.adapter.QuickAdapter;
import com.zone.adapter.callback.Helper;

import and.base.activity.BaseActivity;
import and.base.activity.kinds.ScreenSettingKind;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @Bind(R.id.listView)
    ListView listView;

    @Override
    public void updateKinds() {
        super.updateKinds();
        mKindControl.get(ScreenSettingKind.class).setNoTitle();
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.a_main);
        ButterKnife.bind(this);

    }

    @Override
    public void findIDs() {

    }

    @Override
    public void initData() {
        final int[] colorArry = {Color.WHITE, Color.GREEN, Color.YELLOW, Color.CYAN};
        listView.setAdapter(new QuickAdapter<MainMenu.MenuEntity>(this, MainMenu.menu) {
            @Override
            public void fillData(Helper<MainMenu.MenuEntity> helper, final MainMenu.MenuEntity menuEntity, boolean b, int i) {
                helper.setText(R.id.tv, menuEntity.content)
                        .setBackgroundColor(R.id.tv, colorArry[helper.getPosition() % colorArry.length])
                        .setOnClickListener(R.id.tv, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(MainActivity.this, menuEntity.cls));
                            }
                        });
            }

            @Override
            public int getItemLayoutId(MainMenu.MenuEntity menuEntity, int i) {
                return R.layout.item_menu;
            }
        });
    }

    @Override
    public void setListener() {

    }
}
