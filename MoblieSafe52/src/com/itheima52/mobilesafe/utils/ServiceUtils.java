package com.itheima52.mobilesafe.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

/**
 * 查询服务是否已在运行
 * 
 * @author spring
 * @param cnt
 *            上下文
 * @param servicename
 *            服务名称
 * 
 * 
 */
public class ServiceUtils {

	public  static boolean isRunning(Context cnt, String servicename) {

		ActivityManager service = (ActivityManager) cnt
				.getSystemService(Context.ACTIVITY_SERVICE);

		List<RunningServiceInfo> runningServices = service
				.getRunningServices(1000);

		// 遍历集合、
		for (RunningServiceInfo runningServiceInfo : runningServices) {

			// 判断
			if (servicename.equals(runningServiceInfo.service.getClassName())) {

				return true;
			}

		}

		return false;

	}

}
