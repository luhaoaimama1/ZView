package zone.com.zviews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.zone.adapter.QuickAdapter;
import com.zone.adapter.callback.Helper;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.listView)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);
        ButterKnife.bind(this);
        listView.setAdapter(new QuickAdapter<MainMenu.MenuEntity>(this, MainMenu.menu) {
            @Override
            public void fillData(Helper<MainMenu.MenuEntity> helper, final MainMenu.MenuEntity menuEntity, boolean b, int i) {
                helper.setText(R.id.tv, menuEntity.content).setOnClickListener(R.id.tv, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, menuEntity.cls));
                    }
                });
            }

            @Override
            public int getItemLayoutId(MainMenu.MenuEntity menuEntity, int i) {
                return R.layout.item;
            }
        });

    }
}
