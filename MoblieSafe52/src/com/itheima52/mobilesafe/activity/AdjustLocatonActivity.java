package com.itheima52.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itheima52.mobilesafe.R;

/**
 * 修改归属地显示位置
 * 
 * @author Kevin
 * 
 */
public class AdjustLocatonActivity extends Activity {

	private TextView tvTop;
	private TextView tvBottom;

	private ImageView ivDrag;

	private int startX;
	private int startY;
	private SharedPreferences mPref;

	private int height;
	private int width;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adjust_location);

		mPref = getSharedPreferences("config", MODE_PRIVATE);

		tvTop = (TextView) findViewById(R.id.tv_up_adjust_location);
		tvBottom = (TextView) findViewById(R.id.tv_down_adjust_location);
		ivDrag = (ImageView) findViewById(R.id.iv_drag);

		int lastX = mPref.getInt("lastX", 0);
		int lastY = mPref.getInt("lastY", 0);

		width = getWindowManager().getDefaultDisplay().getWidth();
		height = getWindowManager().getDefaultDisplay().getHeight();

		// 记录显示上下对话框的逻辑
		if (lastY > height / 2) {
			tvTop.setVisibility(View.VISIBLE);
			tvBottom.setVisibility(View.INVISIBLE);
		} else {
			tvTop.setVisibility(View.INVISIBLE);
			tvBottom.setVisibility(View.VISIBLE);
		}

		// onMeasure(测量view), onLayout(安放位置), onDraw(绘制)
		// ivDrag.layout(lastX, lastY, lastX + ivDrag.getWidth(),
		// lastY + ivDrag.getHeight());//不能用这个方法,因为还没有测量完成,就不能安放位置

		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivDrag
				.getLayoutParams();// 获取布局对象
		layoutParams.leftMargin = lastX;// 设置左边距
		layoutParams.topMargin = lastY;// 设置top边距

		ivDrag.setLayoutParams(layoutParams);// 重新设置位置

		// 设置触摸监听
		ivDrag.setOnTouchListener(new OnTouchListener() {

			@SuppressWarnings("deprecation")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// 初始化起点坐标
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					int endX = (int) event.getRawX();
					int endY = (int) event.getRawY();

					// 计算移动偏移量
					int dx = endX - startX;
					int dy = endY - startY;

					// 更新左上右下距离
					int l = ivDrag.getLeft() + dx;
					int r = ivDrag.getRight() + dx;

					int t = ivDrag.getTop() + dy;
					int b = ivDrag.getBottom() + dy;

					if (l < 0 || r > width || t < 0 || b > height - 20) {
						break;
					}

					// 判断界限,显示上下对话框
					if (t > height / 2) {
						tvTop.setVisibility(View.VISIBLE);
						tvBottom.setVisibility(View.INVISIBLE);
					} else {
						tvTop.setVisibility(View.INVISIBLE);
						tvBottom.setVisibility(View.VISIBLE);
					}

					// 更新界面
					ivDrag.layout(l, t, r, b);

					// 重新初始化起点坐标
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_UP:
					// 记录坐标点
					Editor edit = mPref.edit();
					edit.putInt("lastX", ivDrag.getLeft());
					edit.putInt("lastY", ivDrag.getTop());
					edit.commit();
					break;

				default:
					break;
				}

				//事件要往下传递，让双击事件可以响应
				return false;
			}
		});
	}

	/**
	 * 双击居中
	 * 
	 * @param v
	 */

	long[] mHit = new long[2];

	public void doubleClick(View v) {

		System.arraycopy(mHit, 1, mHit, 0, mHit.length - 1);
		mHit[mHit.length - 1] = SystemClock.uptimeMillis();
		// 判断
		if (mHit[0] > (SystemClock.uptimeMillis() - 500)) {
			// 居中
			// height;
			// private int width;
			int l = width / 2 - ivDrag.getWidth() / 2;
			int r = width / 2 + ivDrag.getWidth() / 2;

			int t = height / 2 - ivDrag.getHeight() / 2;
			int b = height / 2 + ivDrag.getHeight() / 2;

			ivDrag.layout(l, t, r, b);

		}

	}
}
