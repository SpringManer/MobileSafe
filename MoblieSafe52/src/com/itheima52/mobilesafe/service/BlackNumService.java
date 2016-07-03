package com.itheima52.mobilesafe.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;
import com.itheima52.mobilesafe.db.dao.BlackNumDao;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

/**
 * 短信、电话 黑名单拦截服务
 * 
 * @author spring
 * 
 */
public class BlackNumService extends Service {

	private String num;
	private BlackNumDao blackNumDao;
	private InSmsintercept mInSmsintercept;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		blackNumDao = BlackNumDao.getInstance(getApplicationContext());

		// 短信拦截
		Smsintercept();

		// 电话拦截
		TelephonyManager TelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		// 监听
		TelephonyManager.listen(new MyPhoneStateListenner(),
				PhoneStateListener.LISTEN_CALL_STATE);

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

				int mode = blackNumDao.findNum(incomingNumber);

				// 判断所属的模式，符合的话，拦截(拦截的类型 1.短信 2.电话 3.拦截所有 )

				if (mode == 2 || mode == 3) {

					// 挂断逻辑

					// 拿不到 ServiceManager类，只有用进行反射。
					// ITelephony.Stub.asInterface(ServiceManager.getService(Context.TELEPHONY_SERVICE));
					try {
						//1,获取ServiceManager字节码文件
						Class<?> clazz = Class.forName("android.os.ServiceManager");
						//2,获取方法
						Method method = clazz.getMethod("getService", String.class);
						//3,反射调用此方法
						IBinder iBinder = (IBinder) method.invoke(null, Context.TELEPHONY_SERVICE);
						//4,调用获取aidl文件对象方法
						ITelephony iTelephony = ITelephony.Stub.asInterface(iBinder);
						//5,调用在aidl中隐藏的endCall方法
						iTelephony.endCall();

					} catch (Exception e) {
						e.printStackTrace();
					}

				}

				break;

			default:
				break;
			}
		}
	}

	/**
	 * 短信拦截
	 */
	private void Smsintercept() {

		// （动态注册广播接受者）
		IntentFilter intentFilter = new IntentFilter();

		intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
		intentFilter.setPriority(1000);

		mInSmsintercept = new InSmsintercept();
		registerReceiver(mInSmsintercept, intentFilter);

		

		registerReceiver(mInSmsintercept, intentFilter);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 取消注册
		unregisterReceiver(mInSmsintercept);
	}

	private class InSmsintercept extends BroadcastReceiver {

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
