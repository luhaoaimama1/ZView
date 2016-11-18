package zone.com.zview;

import java.util.ArrayList;
import java.util.List;

import zone.com.zview.Activity.FlowZoneActivity;
import zone.com.zview.Activity.GridZoneActivity;
import zone.com.zview.Activity.LabelTestActivity;
import zone.com.zview.Activity.SideBarActivity;
import zone.com.zview.Activity.SquareTestActivity;
import zone.com.zview.Activity.SwtichButtonActivity;
import zone.com.zview.Activity.ViewActivity;

public class MainMenu {

    public static List<MenuEntity> menu = new ArrayList<MenuEntity>();


    static {
        menu.add(new MenuEntity("等宽ImageView", SquareTestActivity.class));
        menu.add(new MenuEntity("switchButton", SwtichButtonActivity.class));
        menu.add(new MenuEntity("ninegridview", GridZoneActivity.class));
        menu.add(new MenuEntity("Labview", LabelTestActivity.class));
        menu.add(new MenuEntity("FlowLayout", FlowZoneActivity.class));
        menu.add(new MenuEntity("SurfaceViewTemplate", ViewActivity.class));
        menu.add(new MenuEntity("SideBar", SideBarActivity.class));
    }

    public static class MenuEntity {

        public String content;
        public Class<?> cls;

        public MenuEntity(String content, Class<?> cls) {
            this.content = content;
            this.cls = cls;
        }
    }
}
