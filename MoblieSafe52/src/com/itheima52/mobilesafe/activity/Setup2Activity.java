package com.itheima52.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.itheima52.mobilesafe.R;
import com.itheima52.mobilesafe.view.SettingItemView;

public class Setup2Activity extends BaseSetupActivity {

	private SettingItemView mSimCard;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
		mSimCard = (SettingItemView) findViewById(R.id.siv_sim);
		mSimCard.setTitle("点击绑定SIM卡");
		// 判断状态
		String state = sharedPreferences.getString("sim", null);
		if (TextUtils.isEmpty(state)) {
			mSimCard.setChecked(false);
		} else {
			mSimCard.setChecked(true);
		}

		mSimCard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 判断状态
				if (mSimCard.isChecked()) {
					mSimCard.setChecked(false);
					// 取消绑定
					sharedPreferences.edit().remove("sim");
				} else {
					mSimCard.setChecked(true);
					// 读取手机SIM卡的序列号，保存到sharePrefer

					TelephonyManager mTeleman = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
					String simSerialNumber = mTeleman.getSimSerialNumber();

					// System.out.println("-------------------" +
					// simSerialNumber);

					sharedPreferences.edit().putString("sim", simSerialNumber)
							.commit();
				}

			}

		});

	}

	@Override
	public void showNextPage() {
		
		
		//强制绑定SIM卡，判断checkBox状态
		boolean checked = mSimCard.isChecked();
		if(checked){
			
			startActivity(new Intent(this, Setup3Activity.class));
			overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
			finish();
			
		}else{
			Toast.makeText(getApplicationContext(), "请绑定SIM卡", 0).show();
			
		}

		
		
		
		

	}

	@Override
	public void showPrePage() {
		startActivity(new Intent(this, Setup1Activity.class));
		overridePendingTransition(R.anim.trans_previous_in,
				R.anim.trans_previous_out);
		finish();

	}
}
