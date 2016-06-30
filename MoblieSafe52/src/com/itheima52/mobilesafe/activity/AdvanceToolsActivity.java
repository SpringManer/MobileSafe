package com.itheima52.mobilesafe.activity;

import com.itheima52.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class AdvanceToolsActivity extends Activity {

	private TextView textview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advance_tool);

		textview = (TextView) findViewById(R.id.tv_location_query);

		textview.setOnClickListener(new OnClickListener() {
			// @Override
			public void onClick(View v) {
				// 点击跳转 AdvToolAddressActivity
				Intent intent = new Intent(AdvanceToolsActivity.this,
						AdvToolAddressActivity.class);
				startActivity(intent);

			}
		});
	}

}
