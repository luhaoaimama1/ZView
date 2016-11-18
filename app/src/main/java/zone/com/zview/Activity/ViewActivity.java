package zone.com.zview.Activity;

import and.base.activity.BaseActivity;
import zone.com.zview.view.SinView;

/**
 * Created by fuzhipeng on 2016/11/18.
 */

public class ViewActivity extends BaseActivity {
    @Override
    public void setContentView() {
        setContentView(new SinView(this));
    }

    @Override
    public void findIDs() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void setListener() {

    }
}
