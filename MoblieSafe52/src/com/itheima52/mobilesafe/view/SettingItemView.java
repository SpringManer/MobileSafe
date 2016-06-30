package com.itheima52.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itheima52.mobilesafe.R;

public class SettingItemView extends RelativeLayout {

	private static final String NAMESPACE = "http://schemas.android.com/apk/res/com.itheima52.mobilesafe";
	private TextView tvTitle;
	private TextView tvDesc;
	private CheckBox cbState;
	private String mDescOn;
	private String mDescOf;
	private String title;

	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mDescOn = attrs.getAttributeValue(NAMESPACE, "desc_on");
		mDescOf = attrs.getAttributeValue(NAMESPACE, "desc_of");
		title = attrs.getAttributeValue(NAMESPACE, "title");

		initView();
	}

	public SettingItemView(Context context) {
		super(context);
		initView();
	}

	// 初始化布局

	private void initView() {
		View view = View.inflate(getContext(), R.layout.view_setting_item, this);
		tvTitle = (TextView) view.findViewById(R.id.tv_title);
		tvDesc = (TextView) view.findViewById(R.id.tv_desc);
		cbState = (CheckBox) view.findViewById(R.id.cb_status);
		
		tvTitle.setText(title);
	}

	public void setTitle(String title) {
		tvTitle.setText(title);
	}

	public void setDesc(String title) {
		tvDesc.setText(title);
	}

	// 判断并返回当前的勾选状态
	public boolean isChecked() {
		return cbState.isChecked();
	}

	// 设置checkBox勾选，动态完成状态勾选
	public void setChecked(Boolean checked) {
		cbState.setChecked(checked);
		if(checked){
			tvDesc.setText(mDescOn);
		}else{
			tvDesc.setText(mDescOf);
		}
		
	}

}
