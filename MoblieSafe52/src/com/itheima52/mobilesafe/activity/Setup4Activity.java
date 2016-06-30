package com.itheima52.mobilesafe.activity;

import com.itheima52.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Setup4Activity extends BaseSetupActivity {
	private CheckBox checkbox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);

		sharedPreferences.edit().putBoolean("configed", true).commit();

		checkbox = (CheckBox) findViewById(R.id.cb_setup4_check);

		// 键入页面自动判断是否开启防盗保护
		boolean result = sharedPreferences.getBoolean("protect", false);
		if (result) {

			checkbox.setChecked(true);
			checkbox.setText("你已开启防盗保护");
		} else {
			checkbox.setChecked(false);
			checkbox.setText("你没有开启防盗保护");
		}

		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// 判断改变状态
				if (isChecked) {
					checkbox.setText("你已开启防盗保护");
					sharedPreferences.edit().putBoolean("protect", true)
							.commit();

				} else {
					checkbox.setText("你没有开启防盗保护");
					sharedPreferences.edit().putBoolean("protect", false)
							.commit();
				}

			}
		});

	}

	@Override
	public void showNextPage() {

		startActivity(new Intent(this, LostFindActivity.class));
		finish();

	}

	@Override
	public void showPrePage() {
		startActivity(new Intent(this, Setup3Activity.class));
		overridePendingTransition(R.anim.trans_previous_in,
				R.anim.trans_previous_out);

		finish();

	}

}
