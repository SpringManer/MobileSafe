package com.itheima52.mobilesafe.activity;

import com.itheima52.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LostFindActivity extends Activity {
	private SharedPreferences mPrefer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mPrefer = getSharedPreferences("config", MODE_PRIVATE);

		boolean configed = mPrefer.getBoolean("configed", false);
		if (configed) {

			setContentView(R.layout.activity_lost_find);

			// 设置安全号码显示和状态锁

			ImageView stateLock = (ImageView) findViewById(R.id.iv_state_lock);

			TextView safeNum = (TextView) findViewById(R.id.tv_safe_num);

			boolean safeState = mPrefer.getBoolean("protect", false);

			String num = mPrefer.getString("safe_phone", null);
//			if (num != null) {
				safeNum.setText(num);
//			}

			// //判断状态
			if (safeState) {
				stateLock.setImageResource(R.drawable.lock);

			} else {
				stateLock.setImageResource(R.drawable.unlock);
			}

		} else {
			// 如果是跳转进设置向导页面
			startActivity(new Intent(this, Setup1Activity.class));

			finish();
		}

	}

	/**
	 * 重新进入设置向导页面
	 */
	public void reEnterSetup(View v) {

		startActivity(new Intent(this, Setup1Activity.class));
		finish();

	}

}
