package com.itheima52.mobilesafe.receive;

import java.util.ArrayList;

import com.itheima52.mobilesafe.R;
import com.itheima52.mobilesafe.activity.SettingActivity;
import com.itheima52.mobilesafe.service.LocationService;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.drm.DrmStore.Action;
import android.media.MediaPlayer;
import android.sax.StartElementListener;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {

	private SharedPreferences sharedPreferences;

	@Override
	public void onReceive(Context context, Intent intent) {

		sharedPreferences = context.getSharedPreferences("config",
				Context.MODE_PRIVATE);

		// 拿到原始短信数据
		Object[] object = (Object[]) intent.getExtras().get("pdus");

		for (Object obj : object) {

			SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);

			// 短信内容
			String messageBody = smsMessage.getMessageBody();

			// 发送的号码
			String originatingAddress = smsMessage.getOriginatingAddress();

			// System.out.println(originatingAddress + "---------------"
			// + messageBody);

			// 将收到的短信内容进行比对，一样就放报警音乐

			if ("#*alarm*#".equals(messageBody)) {
				// 播放报警音乐
				MediaPlayer mediaPlayer = MediaPlayer.create(context,
						R.raw.ylzs);

				mediaPlayer.setVolume(1f, 1f);
				mediaPlayer.setLooping(true);
				mediaPlayer.start();

				// 终止广播，不让系统提示收到短信
				abortBroadcast();
			} else if ("#*location*#".equals(messageBody)) {

				// 启动位置服务
				context.startService(new Intent(context, LocationService.class));

				// 拿到sharePreferce里面保存的位置信息，发送短信出去
				String loaction = sharedPreferences.getString("location", null);

				// 终止广播，不让系统提示收到短信
				abortBroadcast();

				// 发送经纬度到指定号码
				// System.out.println(loaction);
				// 拿到短信manager
				SmsManager smsManager = SmsManager.getDefault();

				// 切割,超过字数，分条发送
				ArrayList<String> divideMessage = smsManager
						.divideMessage(loaction);

				// smsManager.sendTextMessage("5556", null, divideMessage, null,
				// null);
				smsManager.sendMultipartTextMessage("5556", null,
						divideMessage, null, null);

			} else if ("#*lockscreen*#".equals(messageBody)) {
				// 实行锁屏逻辑同时设置密码,拿到设备管理器

				DevicePolicyManager mDevicePolicyManager = (DevicePolicyManager) context
						.getSystemService(context.DEVICE_POLICY_SERVICE);
				ComponentName componentName = new ComponentName(context,
						MyDeviceAdminReciver.class);
				// 判断是否注册过权限
				if (mDevicePolicyManager.isAdminActive(componentName)) {

					mDevicePolicyManager.lockNow();
					mDevicePolicyManager.resetPassword("123456", 0);
					// 终止广播，不让系统提示收到短信
					abortBroadcast();
				} else {
					
//					Toast.makeText(context, "请获取权限", 0).show();
					// 跳转到获取权限页面
//					 Intent intent2 = new
//					 Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
//					intent2.addFlags(intent2.FLAG_ACTIVITY_NEW_TASK);
//					intent2.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
//					intent2.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
//							"哈哈哈哈哈。请求权限，同意吧");
//					context.startActivity(intent2);
//					测试
					Intent intent2 = new Intent(context,SettingActivity.class);
					
					intent2.addFlags(intent2.FLAG_ACTIVITY_NEW_TASK);
					
					context.startActivity(intent2);
				}

			}

		}

	}
}
