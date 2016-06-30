package com.itheima52.mobilesafe.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

/**
 * 开机自动检测Sim卡序列号
 * 
 * @author spring
 * 
 */
public class BootCompleReceiver extends BroadcastReceiver {

	private TelephonyManager tele;

	@Override
	public void onReceive(Context context, Intent intent) {

		// 保存的值
		SharedPreferences sp = context.getSharedPreferences("config",
				context.MODE_PRIVATE);
		String state = sp.getString("sim", null);

		tele = (TelephonyManager) context
				.getSystemService(context.TELEPHONY_SERVICE);

		// 只有在打开保护锁的状态下才进行SIM卡判断,和后台短信的验证
		boolean protectState = sp.getBoolean("protect", false);

		if (protectState) {

			String simSerialNumber = tele.getSimSerialNumber() + "111";
			// 对比
			if (simSerialNumber.equals(state)) {
				System.out.println("------------------------------手机安全");

			} else {

				// 发送短信给当初留下的安全号码,获取messageManageer

				SmsManager smsManager = SmsManager.getDefault();

				smsManager.sendTextMessage("5556", null,
						"SmsCard changed,  dangerous!!! ", null, null);

				System.out.println("SIM卡已跟换，手机危险");
			}
			
			

		} else {
			System.out.println("--------------没有设置安全保护");
		}
	}

}
