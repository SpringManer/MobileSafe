package com.itheima52.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itheima52.mobilesafe.R;

public class SettingSelectView extends RelativeLayout {

	private TextView tvTitle;
	private TextView tvDesc;

	public SettingSelectView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public SettingSelectView(Context context, AttributeSet attrs) {
		super(context, attrs);

		initView();
	}

	public SettingSelectView(Context context) {
		super(context);
		initView();
	}

	// 初始化布局

	private void initView() {
		View view = View.inflate(getContext(), R.layout.view_select_item, this);
		tvTitle = (TextView) view.findViewById(R.id.tv_title);
		tvDesc = (TextView) view.findViewById(R.id.tv_desc);
		
	}
	
	public void setTitle(String title) {
		tvTitle.setText(title);
	}
	
	public void setDesc(String desc) {
		tvDesc.setText(desc);
	}


	
}
