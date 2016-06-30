package com.itheima52.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public abstract class BaseSetupActivity extends Activity {
	private GestureDetector mGesture;
	public SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
		mGesture = new GestureDetector(this, new SimpleOnGestureListener() {

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {

				// 向右滑动,上一页
				if ((e2.getRawX() - e1.getRawX()) > 200) {
					showPrePage();

				}
				// 判断纵向的滑动
				if ((Math.abs(e1.getRawY() - e2.getRawY())) > 100) {
					Toast.makeText(BaseSetupActivity.this, "不能这样划哦", 0).show();

				}
				// 判断速度
				if (Math.abs(velocityX) < 100) {
					Toast.makeText(BaseSetupActivity.this, "滑动的太慢了", 0).show();
				}

				// 向左滑动，下一页
				if ((e1.getRawX() - e2.getRawX()) > 200) {
					showNextPage();
				}

				return super.onFling(e1, e2, velocityX, velocityY);
			}

		});
	}

	/**
	 * 跳转下一页
	 */
	public abstract void showNextPage();

	/**
	 * 跳转上一页
	 */
	public abstract void showPrePage();

	// 实现点击事件,跳转Setup3Activity页
	public void next(View v) {
		showNextPage();

	}

	// 实现点击事件,跳转上一页
	public void previous(View v) {
		showPrePage();

	}

	/**
	 * 增加手势划屏
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mGesture.onTouchEvent(event);// 事件委托给gestureDetector
		return super.onTouchEvent(event);
	}

}
