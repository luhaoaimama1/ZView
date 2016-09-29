package zone.com.zview;

import java.util.ArrayList;
import java.util.List;

public class MainMenu {
    public static List<MenuEntity> menu = new ArrayList<MenuEntity>();

    static {
        menu.add(new MenuEntity("等宽ImageView", SquareTestActivity.class));
//        menu.add(new MenuEntity("流式布局", FlowTestAcitivity.class));
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
