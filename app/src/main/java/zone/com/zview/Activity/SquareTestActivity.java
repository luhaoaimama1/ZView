package zone.com.zview.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import zone.com.zview.R;

/**
 * Created by fuzhipeng on 16/8/26.
 */
public class SquareTestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.square_test);
        System.out.println("----SquareTestActivity----");
        findViewById(R.id.sqv).post(new Runnable() {
            @Override
            public void run() {
                System.out.println("width:"+findViewById(R.id.sqv).getWidth()+"_height:"+findViewById(R.id.sqv).getHeight());
            }
        });
    }
}
