package com.itheima52.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.itheima52.mobilesafe.R;

public class Setup3Activity extends BaseSetupActivity {
	private EditText inputNmu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
		inputNmu = (EditText) findViewById(R.id.et_inputnum);

		String setedNum = sharedPreferences.getString("safe_phone", null);

		// 判断，如果有数据就显示带控件
		if (setedNum != null) {
			inputNmu.setText(setedNum);
		}

	}

	// 点击方法，显示联系人列表
	public void contact(View v) {

		// 跳转contactActivity页面

		Intent intent = new Intent(this, ContactActivity.class);
		startActivityForResult(intent, 0);

	}

	// 接收返回来的消息
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 判断一下结果码
		if (resultCode == Activity.RESULT_OK) {

			String num = data.getStringExtra("num");
			// 替换空格和-
			num = num.replaceAll("-", "").replaceAll(" ", "");
			// 显示到控件
			inputNmu.setText(num);

		}

	}

	@Override
	public void showNextPage() {
		// 强制设置安全号码
		String string = inputNmu.getText().toString().trim();

		if (TextUtils.isEmpty(string)) {
			Toast.makeText(getApplicationContext(), "安全号码不能为空", 0).show();
			return;
		}
		// 将安全号码保存到sharePu
		sharedPreferences.edit().putString("safe_phone", string).commit();

		startActivity(new Intent(this, Setup4Activity.class));
		overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
		finish();

	}

	@Override
	public void showPrePage() {
		startActivity(new Intent(this, Setup2Activity.class));
		overridePendingTransition(R.anim.trans_previous_in,
				R.anim.trans_previous_out);

		finish();

	}

}
