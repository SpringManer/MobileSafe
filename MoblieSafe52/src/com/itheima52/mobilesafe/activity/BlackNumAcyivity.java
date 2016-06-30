package com.itheima52.mobilesafe.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima52.mobilesafe.R;
import com.itheima52.mobilesafe.db.bean.BlackNumInfo;
import com.itheima52.mobilesafe.db.dao.BlackNumDao;

public class BlackNumAcyivity extends Activity {

	private ListView ls_blacknum;
	private Button btn_add;
	private ArrayList<BlackNumInfo> arrayList;
	private int mode = 1;
	private MyAdapter mAdptor;
	boolean mIsLoad = false;

	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {

			if (mAdptor == null) {

				mAdptor = new MyAdapter();

				ls_blacknum.setAdapter(mAdptor);
			} else {
				mAdptor.notifyDataSetChanged();
				mIsLoad = false;
			}

		};
	};

	/**
	 * 点击删除条目 1.删除数据库条目 2.删除数字条目，更新适配器 3.将viewHold设置为静态，不会去反复创建对象
	 * 4.listView分页加载，一次20，逆序返回。(1.listＶiew设置滚动监听 2.滚动到最底部，最后一个条目可见 3.滚动状态发生改变)
	 * 
	 */
	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return arrayList.size();
		}

		@Override
		public Object getItem(int position) {
			return arrayList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			ViewHold viewHold = null;

			if (convertView == null) {

				convertView = View.inflate(getApplicationContext(),
						R.layout.listview_blacknum_item, null);
				// 创建ViewHold实例，并将参数存入
				viewHold = new ViewHold();

				viewHold.tv_item_num = (TextView) convertView
						.findViewById(R.id.tv_item_num);
				viewHold.tv_item_mode = (TextView) convertView
						.findViewById(R.id.tv_item_mode);
				viewHold.iv_item_delete = (ImageView) convertView
						.findViewById(R.id.iv_item_delete);
				// 存入convertView
				convertView.setTag(viewHold);

			} else {

				viewHold = (ViewHold) convertView.getTag();
			}

			// 显示数据

			final String phone = arrayList.get(position).phone;
			String mode = arrayList.get(position).mode;

			viewHold.tv_item_num.setText(phone);
			// 拦截的类型 1.短信 2.电话 3.拦截所有
			switch (Integer.parseInt(mode)) {
			case 1:
				viewHold.tv_item_mode.setText("短信拦截");
				break;
			case 2:
				viewHold.tv_item_mode.setText("电话拦截");
				break;
			case 3:
				viewHold.tv_item_mode.setText("拦截所有");
				break;

			default:
				break;
			}

			viewHold.iv_item_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					// 1.删除数据库条目
					BlackNumDao.getInstance(getApplicationContext()).delete(
							phone);
					// 删除数字条目，更新适配器
					arrayList.remove(position);

					handler.sendEmptyMessage(0);

					Toast.makeText(getApplicationContext(), "删除成功", 0).show();

				}
			});

			return convertView;
		}

	}

	private static class ViewHold {
		public TextView tv_item_num;
		public TextView tv_item_mode;
		public ImageView iv_item_delete;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_black_num);

		iniUI();
		iniData();

	}

	/**
	 * 拿到数据库所有数据
	 */
	private void iniData() {

		new Thread() {
			@Override
			public void run() {
				// 调用查询方法
				arrayList = BlackNumDao.getInstance(getApplicationContext()).find(0);
				// 发送消息
				handler.sendEmptyMessage(0);
				super.run();
			}
		};

	}

	private void iniUI() {

		ls_blacknum = (ListView) findViewById(R.id.lv_blacknum);
		btn_add = (Button) findViewById(R.id.btn_add_blacknum);

		btn_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showAddBlackDialog();

			}
		});

		// 设置滚动监听,(加载下一页的逻辑)
		ls_blacknum.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

				switch (scrollState) {
				// 没滚动
				case OnScrollListener.SCROLL_STATE_IDLE:

					// 判断是数据库的总数是不是大于现在集合的个数
					int num = BlackNumDao.getInstance(getApplicationContext())
							.getCount();
					System.out.println("==========================" + num);

					if (num > arrayList.size()) {

						// 判断是不是到了底部
						if (ls_blacknum.getLastVisiblePosition() >= arrayList
								.size() - 1 && !mIsLoad) {
							// 加载下面20个条目
							ArrayList<BlackNumInfo> moreArrayList = BlackNumDao
									.getInstance(getApplicationContext()).find(
											arrayList.size());
							// 将新加载的条目放入之前的集合、
							arrayList.addAll(moreArrayList);
							// 发送消息
							handler.sendEmptyMessage(0);
							// 更改mIsLoad值
							mIsLoad = true;
						}
					}

					break;
				// 飞速滚动
				case OnScrollListener.SCROLL_STATE_FLING:

					break;
				// 触摸滚动
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:

					break;

				default:
					break;
				}

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});

	}

	/**
	 * 点击添加黑名单
	 */
	protected void showAddBlackDialog() {

		Builder builder = new AlertDialog.Builder(this);

		final AlertDialog alertDialog = builder.create();
		View view = View.inflate(this, R.layout.dialog_add_blacknum, null);

		Button btn_cancel = (Button) view
				.findViewById(R.id.btn_dialog_add_blacknum_cancel);
		Button btn_confirm = (Button) view
				.findViewById(R.id.btn_dialog_add_blacknum_confirm);
		final EditText balcknum_add = (EditText) view
				.findViewById(R.id.et_dialog_balcknum_add);
		final RadioGroup radioGroup = (RadioGroup) view
				.findViewById(R.id.rg_balck_num_RadioGroup);

		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			// radio group 监听 拦截的类型 1.短信 2.电话 3.拦截所有
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				switch (checkedId) {
				case R.id.rb_all:
					mode = 3;
					break;
				case R.id.rb_call:
					mode = 2;
					break;
				case R.id.rb_sms:
					mode = 1;
					break;

				default:
					break;
				}

			}
		});

		btn_confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// [1] 拿到输入的数据和单选框的选择状态,
				String input_num = balcknum_add.getText().toString().trim();
				if (!TextUtils.isEmpty(input_num)) {
					// [2] 存到数据库
					BlackNumDao.getInstance(BlackNumAcyivity.this).insert(
							input_num, mode + "");

					// [3]将新数据插入到集合
					BlackNumInfo blackNumInfo = new BlackNumInfo();
					blackNumInfo.phone = input_num;
					blackNumInfo.mode = mode + "";
					arrayList.add(0, blackNumInfo);

					// [4]更新适配器
					if (mAdptor != null) {

						mAdptor.notifyDataSetChanged();
					}

					// [5]给handle发消息，更新适配器
					handler.sendEmptyMessage(0);
					// [6]关闭对话框
					alertDialog.dismiss();

				} else {
					Toast.makeText(getApplicationContext(), "号码不能为空", 0).show();
				}

			}
		});

		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alertDialog.dismiss();

			}
		});

		alertDialog.setView(view, 0, 0, 0, 0);

		alertDialog.show();

	}
}
