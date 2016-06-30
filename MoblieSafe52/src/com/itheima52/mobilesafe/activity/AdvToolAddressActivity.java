package com.itheima52.mobilesafe.activity;

import com.itheima52.mobilesafe.R;
import com.itheima52.mobilesafe.db.dao.AddressDao;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

public class AdvToolAddressActivity extends Activity {

	private EditText inpurNum;
	private TextView locationResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advtool_adress);
		// 找到输入的控件
		inpurNum = (EditText) findViewById(R.id.et_input_num);

		locationResult = (TextView) findViewById(R.id.tv_location_result);
		// 设置输入文字改变的自动监听
		inpurNum.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				// 调用查询方法
				String result = AddressDao.getAddress(s.toString());
				// 显示结果

				locationResult.setText(result);

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}

		});

	}

	// 实现点击按钮查询事件
	public void qureyNum(View v) {

		// 拿到输入的号码
		String num = inpurNum.getText().toString().trim();

		// 判断，是否为空
		if (!TextUtils.isEmpty(num)) {

			// 调用查询方法
			String result = AddressDao.getAddress(num);
			// 显示结果

			locationResult.setText(result);
		} else {
			
			// 抖动,且震动提示不能为空
			Animation animation = AnimationUtils.loadAnimation(this,
					R.anim.shake);
			
			virbrate();
			inpurNum.startAnimation(animation);

		}

	}
	/**
	 * 震动功能
	 */
	private void virbrate(){
		
		Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		vibrator.vibrate(2000);//震动两秒
		
	}
}
