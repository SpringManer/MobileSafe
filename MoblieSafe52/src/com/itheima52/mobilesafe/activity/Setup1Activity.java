package com.itheima52.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.itheima52.mobilesafe.R;

public class Setup1Activity extends BaseSetupActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup1);
	}

	@Override
	public void showNextPage() {
		startActivity(new Intent(this, Setup2Activity.class));
		finish();
		// 两个界面的切换动画
		overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
		
	}

	@Override
	public void showPrePage() {
		
	}

	
}
