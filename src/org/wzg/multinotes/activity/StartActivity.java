package org.wzg.multinotes.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.wzg.multifuncationnotes.R;
import org.wzg.multinotes.adapter.SaveContentAdapter;
import org.wzg.multinotes.db.OperateNotesDataBase;
import org.wzg.multinotes.entity.NoteEntity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;

public class StartActivity extends Activity implements OnClickListener,
		OnItemClickListener, OnItemLongClickListener {

	private TextView mCreateNote;
	private TextView mTextShowLocation;
	private ListView mShowContentLv;
	private LinearLayout mBottomLayout;
	private Button mDeleteBtn;
	private Button mCancleBtn;
	private AdapterView<?> mParent;// 用于辅助批量删除的参数

	private Intent mIntent;
	private SaveContentAdapter mAdapter;
	private OperateNotesDataBase mDataBase;
	private List<NoteEntity> mlist;
	private Boolean isChecked;

	public AMapLocationClient mLocationClient = null;
	public AMapLocationClientOption mLocationOption = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_start);

		initViews();
		initData();
		initLocationClient();
	}

	public void initViews() {

		mCreateNote = (TextView) findViewById(R.id.btn_createnote);
		mTextShowLocation = (TextView) findViewById(R.id.tv_showlocation);
		mShowContentLv = (ListView) findViewById(R.id.lv_shownotes);

		mBottomLayout = (LinearLayout) findViewById(R.id.ll_bottom);
		mDeleteBtn = (Button) findViewById(R.id.btn_start_delete);
		mCancleBtn = (Button) findViewById(R.id.btn_start_cancle);

		mCreateNote.setOnClickListener(this);
		mShowContentLv.setOnItemClickListener(this);
		mShowContentLv.setOnItemLongClickListener(this);

		mDeleteBtn.setOnClickListener(this);
		mCancleBtn.setOnClickListener(this);

	}

	private void initData() {
		mDataBase = new OperateNotesDataBase(this);
		mlist = new ArrayList<NoteEntity>();
	}

	/**
	 * 初始化定位对象以及定位参数
	 */
	private void initLocationClient() {
		// 初始化定位
		mLocationClient = new AMapLocationClient(getApplicationContext());
		// 初始化定位参数
		mLocationOption = new AMapLocationClientOption();
		// 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
		mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
		// 设置是否返回地址信息（默认返回地址信息）
		mLocationOption.setNeedAddress(true);
		// 设置是否只定位一次,默认为false
		mLocationOption.setOnceLocation(false);
		// 设置是否强制刷新WIFI，默认为强制刷新
		mLocationOption.setWifiActiveScan(true);
		// 设置是否允许模拟位置,默认为false，不允许模拟位置
		mLocationOption.setMockEnable(false);
		// 设置定位间隔,单位毫秒,默认为2000ms
		mLocationOption.setInterval(2000);
		// 给定位客户端对象设置定位参数
		mLocationClient.setLocationOption(mLocationOption);
		// 启动定位
		mLocationClient.startLocation();
		// 设置定位回调监听
		mLocationClient.setLocationListener(mLocationListener);
	}

	/**
	 * 每次启动主页面都查询数据
	 */
	@Override
	protected void onStart() {
		super.onStart();
		mlist = mDataBase.queryDB();
		mAdapter = new SaveContentAdapter(this, mlist);
		mShowContentLv.setAdapter(mAdapter);
	}

	@Override
	public void onClick(View v) {
		mIntent = new Intent(StartActivity.this, AddContentActivity.class);
		switch (v.getId()) {

		// 创建笔记
		case R.id.btn_createnote:
			startActivity(mIntent);
			mBottomLayout.setVisibility(View.GONE);
			break;

		// 批量删除
		case R.id.btn_start_delete:
			for (int i = 0; i < mlist.size(); i++) {
				View mView = mParent.getChildAt(i);
				CheckBox checkBox = (CheckBox) mView
						.findViewById(R.id.list_ck_delete);
				if (checkBox.isChecked()) {
					mDataBase.deleteDB(mlist.get(i));
				}
			}
			mBottomLayout.setVisibility(View.GONE);
			// 删除后重新查询数据库
			mlist = mDataBase.queryDB();
			mAdapter = new SaveContentAdapter(this, mlist);
			mShowContentLv.setAdapter(mAdapter);
			break;

		// 取消删除布局
		case R.id.btn_start_cancle:

			for (int i = 0; i < mlist.size(); i++) {
				View mView = mParent.getChildAt(i);
				CheckBox checkBox = (CheckBox) mView
						.findViewById(R.id.list_ck_delete);

				checkBox.setVisibility(View.GONE);

			}
			mBottomLayout.setVisibility(View.GONE);
			break;

		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		// 判断点击时是否有删除事件
		if (mBottomLayout.getVisibility() == View.VISIBLE) {
			mBottomLayout.setVisibility(View.GONE);
			for (int i = 0; i < mlist.size(); i++) {
				View mView2 = mParent.getChildAt(i);
				CheckBox checkBox = (CheckBox) mView2
						.findViewById(R.id.list_ck_delete);
				checkBox.setVisibility(View.GONE);
			}
		} else {
			Intent intent = new Intent(StartActivity.this,
					DetailsActivity.class);
			NoteEntity item = mlist.get(position);
			intent.putExtra("item", item);
			startActivity(intent);
		}
	}

	/**
	 * 批量删除
	 */
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {

		mParent = parent;
		for (int i = 0; i < mlist.size(); i++) {
			View mView = mParent.getChildAt(i);
			CheckBox checkBox = (CheckBox) mView
					.findViewById(R.id.list_ck_delete);
			isChecked = checkBox.isChecked();
			checkBox.setVisibility(View.VISIBLE);
			checkBox.setChecked(false);
		}
		mBottomLayout.setVisibility(View.VISIBLE);
		return true;
	}

	/**
	 * 声明定位回调监听器
	 */
	public AMapLocationListener mLocationListener = new AMapLocationListener() {

		@Override
		public void onLocationChanged(AMapLocation amapLocation) {
			if (amapLocation != null) {
				if (amapLocation.getErrorCode() == 0) {
					// 定位成功回调信息，设置相关消息
					amapLocation.getLocationType();// 获取当前定位结果来源，如网络定位结果，详见定位类型表
					amapLocation.getLatitude();// 获取纬度
					amapLocation.getLongitude();// 获取经度
					amapLocation.getAccuracy();// 获取精度信息
					SimpleDateFormat df = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					Date date = new Date(amapLocation.getTime());
					df.format(date);// 定位时间
					amapLocation.getAddress();// 地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
					amapLocation.getCountry();// 国家信息
					amapLocation.getProvince();// 省信息
					amapLocation.getCity();// 城市信息
					amapLocation.getDistrict();// 城区信息
					amapLocation.getStreet();// 街道信息
					amapLocation.getStreetNum();// 街道门牌号信息
					amapLocation.getCityCode();// 城市编码
					mTextShowLocation.setText(amapLocation.getCity());
				} else {
					// 显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
					Log.e("AmapError", "location Error, ErrCode:"
							+ amapLocation.getErrorCode() + ", errInfo:"
							+ amapLocation.getErrorInfo());
				}
			}
		}
	};
}
