package com.itheima52.mobilesafe.service;

import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.itheima52.mobilesafe.db.dao.BlackNumDao;
import com.itheima52.mobilesafe.service.BlackNumService.MyPhoneStateListenner.MContentObserver;

/**
 * 短信、电话 黑名单拦截服务
 * 
 * @author spring
 * 
 */
public class BlackNumService extends Service {

	private String num;
	private BlackNumDao blackNumDao;
	private InSmsinterceptReceiver mInSmsintercept;
	private TelephonyManager telephonyManager;
	private MyPhoneStateListenner myPhoneStateListenner;
	private Uri uriCallRecord;
	private MContentObserver mContentObserver;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		blackNumDao = BlackNumDao.getInstance(getApplicationContext());

		// 短信拦截
		smsIntercept();

		// 监听电话
		telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		myPhoneStateListenner = new MyPhoneStateListenner();
		telephonyManager.listen(myPhoneStateListenner,
				PhoneStateListener.LISTEN_CALL_STATE);

		// 拦截电话

	}

	public class MyPhoneStateListenner extends PhoneStateListener {

		

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);

			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:

				break;

			case TelephonyManager.CALL_STATE_OFFHOOK:
				break;
			// 接听之前直接挂断
			case TelephonyManager.CALL_STATE_RINGING:

				// 拦截电话
				phoneIntercepy(incomingNumber);

				// 删除通话记录，利用广播监听者

				uriCallRecord = Uri.parse("content://call_log/calls");

				mContentObserver = new MContentObserver(
						new Handler(), incomingNumber);
				getContentResolver().registerContentObserver(uriCallRecord,
						true, mContentObserver);

				break;

			default:
				break;
			}
		}

		public class MContentObserver extends ContentObserver {

			private String incomingNumber;

			public MContentObserver(Handler handler, String incomingNumber) {
				super(handler);
				this.incomingNumber = incomingNumber;
			}

			// 改变的时候要实现的逻辑
			@Override
			public void onChange(boolean selfChange) {
				super.onChange(selfChange);
				// 删除记录、
				getContentResolver().delete(uriCallRecord, "number = ?",
						new String[] { incomingNumber });

			}

		}
	}

	/**
	 * 电话拦截的逻辑
	 * 
	 * @param incomingNumber
	 */
	private void phoneIntercepy(String incomingNumber) {
		int mode = blackNumDao.findNum(incomingNumber);

		// 判断所属的模式，符合的话，拦截(拦截的类型 1.短信 2.电话 3.拦截所有 )

		if (mode == 2 || mode == 3) {

			// 挂断逻辑

			// 拿不到 ServiceManager类，只有用进行反射。
			// ITelephony.Stub.asInterface(ServiceManager.getService(Context.TELEPHONY_SERVICE));
			try {
				// 1,获取ServiceManager字节码文件
				Class<?> clazz = Class.forName("android.os.ServiceManager");
				// 2,获取方法
				Method method = clazz.getMethod("getService", String.class);
				// 3,反射调用此方法
				IBinder iBinder = (IBinder) method.invoke(null,
						Context.TELEPHONY_SERVICE);
				// 4,调用获取aidl文件对象方法
				ITelephony iTelephony = ITelephony.Stub.asInterface(iBinder);
				// 5,调用在aidl中隐藏的endCall方法
				iTelephony.endCall();

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * 短信拦截
	 */
	private void smsIntercept() {

		// （动态注册广播接受者）
		IntentFilter intentFilter = new IntentFilter();

		intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
		intentFilter.setPriority(1000);

		mInSmsintercept = new InSmsinterceptReceiver();
		registerReceiver(mInSmsintercept, intentFilter);

		registerReceiver(mInSmsintercept, intentFilter);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 取消拦截服务注册
		unregisterReceiver(mInSmsintercept);
		// 取消电话监听
		telephonyManager.listen(myPhoneStateListenner,
				PhoneStateListener.LISTEN_CALL_STATE);
		// 取消注册内容观察者
		getContentResolver().unregisterContentObserver(mContentObserver);

	}

	/**
	 * 拦截短信的接收者
	 * 
	 * @author spring
	 * 
	 */
	private class InSmsinterceptReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			// 拿到原始短信数据
			Object[] object = (Object[]) intent.getExtras().get("pdus");

			// 遍历
			for (Object object2 : object) {
				// 创建短信
				SmsMessage smsMessage = SmsMessage
						.createFromPdu((byte[]) object2);

				// 拿到短信信息
				String messageBody = smsMessage.getMessageBody();
				num = smsMessage.getOriginatingAddress();

				int mode = blackNumDao.findNum(num);
				System.out.println("---------------------------" + mode);

				// 判断所属的模式，符合的话，拦截(拦截的类型 1.短信 2.电话 3.拦截所有 )

				if (mode == 1 || mode == 3) {

					// 拦截逻辑
					mInSmsintercept.abortBroadcast();

				}

			}

		}
	}
}
