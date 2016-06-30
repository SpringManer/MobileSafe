package com.itheima52.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima52.mobilesafe.R;
import com.itheima52.mobilesafe.db.dao.AddressDao;

public class NumAddressService extends Service {

	private TelephonyManager telephonyManager;
	private myPhoneStateListener myPhoneStateListener;
	private WindowManager windowManager;
	private OutCallReceiver outCallReceiver;
	private View view;
	private SharedPreferences sharedPreferences;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

		myPhoneStateListener = new myPhoneStateListener();
		telephonyManager.listen(myPhoneStateListener,
				PhoneStateListener.LISTEN_CALL_STATE);
		outCallReceiver = new OutCallReceiver();

		IntentFilter intentFilter = new IntentFilter();

		intentFilter.addAction("android.intent.action.NEW_OUTGOING_CALL");
		//动态注册广播接受者
		registerReceiver(outCallReceiver, intentFilter);
		sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);

	}

	public class myPhoneStateListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			// 判断电话状态啊

			switch (state) {
			// 响铃
			case TelephonyManager.CALL_STATE_RINGING:

				// 查询归属地
				String address = AddressDao.getAddress(incomingNumber);
				//
				// Toast.makeText(getApplicationContext(), address,
				// Toast.LENGTH_LONG).show();
				// 调用显示窗口
				showToast(address);

				break;
			// 挂断（闲置）
			case TelephonyManager.CALL_STATE_IDLE:
				// 判断是否为空，空的话关闭显示窗口
				if (windowManager != null && view != null) {

					windowManager.removeView(view);
				}

				break;

			default:
				break;
			}

		}

	}

	/**
	 * 动态注册挂广播，因为需要随时开启关闭。
	 * 
	 * @author spring
	 * 
	 */
	public class OutCallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String resultData = getResultData();

			// 查询逻辑
			String numLocation = AddressDao.getAddress(resultData);

			showToast(numLocation);
		}

	}

	/**
	 * 在任意界面tanch
	 */
	private void showToast(String address) {

		int lastX = sharedPreferences.getInt("lastX", 0);
		int lastY = sharedPreferences.getInt("lastY", 0);

		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		// 拿到屏幕宽高
		final int width = windowManager.getDefaultDisplay().getWidth();
		final int height = windowManager.getDefaultDisplay().getHeight();

		final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_PHONE;
		params.setTitle("Toast");
		// 设置重心为左上角
		params.gravity = Gravity.LEFT + Gravity.TOP;

		// 设置窗口显示的初始化位置
		params.x = lastX;
		params.y = lastY;

		// 自定义来电显示图标背景
		// final String[] style = new String[] { "半透明", "活力橙", "卫士蓝", "金属灰",
		// "苹果绿" };
		int int1 = sharedPreferences.getInt("address_style", 0);

		int[] color = new int[] { R.drawable.call_locate_white,
				R.drawable.call_locate_orange, R.drawable.call_locate_blue,
				R.drawable.call_locate_gray, R.drawable.call_locate_green };

		view = View.inflate(this, R.layout.toast_address, null);

		TextView toastNum = (TextView) view.findViewById(R.id.tv_toast_num);
		LinearLayout toastAddress = (LinearLayout) view
				.findViewById(R.id.ll_toast_address);

		toastAddress.setBackgroundResource(color[int1]);

		toastNum.setText(address);

		windowManager.addView(view, params);

		// 设置图标位置拖动

		toastNum.setOnTouchListener(new OnTouchListener() {

			private int startX;
			private int startY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// 判断
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();

					System.out.println("-----------touch");

					break;
				case MotionEvent.ACTION_MOVE:

					int endX = (int) event.getRawX();
					int endY = (int) event.getRawY();

					// 计算移动偏移量
					int dx = endX - startX;
					int dy = endY - startY;

					// 判断偏移位置，进行归零
					if (params.x < 0) {
						params.x = 0;
					}
					if ((params.x > (width - view.getWidth()))) {
						params.x = width - view.getWidth();
					}
					if (params.y < 0) {
						params.y = 0;
					}
					if (params.y > (height - 20 - view.getHeight())) {
						params.y = height - 20 - view.getHeight();
					}

					// 更新显示
					params.x += dx;
					params.y += dy;

					startX = (int) event.getRawX();
					startY = (int) event.getRawY();

					windowManager.updateViewLayout(view, params);

					break;
				case MotionEvent.ACTION_UP:

					sharedPreferences.edit().putInt("lastX", params.x).commit();
					sharedPreferences.edit().putInt("lastY", params.y).commit();

					break;

				default:
					break;
				}

				return true;
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		// 关闭电话监听
		telephonyManager.listen(myPhoneStateListener,
				PhoneStateListener.LISTEN_NONE);
		// 关闭注册广播
		unregisterReceiver(outCallReceiver);

	}

}
