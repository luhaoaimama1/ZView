package zone.com.zview.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import com.zone.view.SideBar;
import com.zone.view.SideBar.OnLetterSelectedListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import and.utils.data.convert.pinyin.PinYin;
import zone.com.zview.Activity.adapter.SortAdapter2;
import zone.com.zview.entity.SortModel;
import zone.com.zview.R;

public class SideBarActivity extends Activity {

	private ListView lv;
	private SideBar sideBar;
	private TextView mTextView;
	private List<SortModel> data;
	private SortAdapter2 adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_side_bar);
		initView();
	}

	/**
	 * @author Xubin Single QQ:215298766
	 */
	private void initView() {
		lv = (ListView) findViewById(R.id.main_lv);
		sideBar = (SideBar) findViewById(R.id.side_bar);
		mTextView = (TextView) findViewById(R.id.dialog);
		sideBar.setmTextView(mTextView);
		sideBar.setLetterSelectedListener(letterSelectedListener);
		// 获取姓名数据
		data=fillData(getResources().getStringArray(R.array.data));
		// 按字母排序
		Collections.sort(data, new PinYinComparator());
		// 创建适配器，显示在ListView上
		adapter=new SortAdapter2(this, data);
		lv.setAdapter(adapter);
		adapter.relatedList(lv);
	}
	public List<SortModel> fillData(String[] names){
		List<SortModel> sortModels=new ArrayList<SortModel>();
		for (int i = 0; i < names.length; i++) {
			SortModel model=new SortModel();
			model.setName(names[i]);
			String py=PinYin.getPinYin(names[i]);
			String sortLetter=py.substring(0,1).toUpperCase();	// 获取名字拼音的首字母大写
			model.setSortLetter(sortLetter);
			sortModels.add(model);
		}
		
		return sortModels;
	}
	
	private OnLetterSelectedListener letterSelectedListener=new OnLetterSelectedListener() {
		
		@Override
		public void onLetterSelected(String s) {
			int position=adapter.getPositionBySelection(s.charAt(0));
			lv.setSelection(position);
		}
	};


	public class PinYinComparator implements Comparator<SortModel> {

		/**
		 * 排序比较
		 */
		@Override
		public int compare(SortModel lhs, SortModel rhs) {
			return lhs.getSortLetter().compareTo(rhs.getSortLetter());
		}

	}

}
