package com.itheima52.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public class LocationService extends Service {
	private LocationManager locationMan;
	private myLocationListener listener;
	private SharedPreferences sharedPreferences;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
		// 调用位置方法
		getLocation();
	}

	/**
	 * 
	 * 获取当前位置的方法
	 */
	private void getLocation() {

		locationMan = (LocationManager) getSystemService(LOCATION_SERVICE);

		// List<String> allProviders = locationMan.getAllProviders();

		// System.out.println(allProviders);

		// 获取最佳设置criteria：标准

		Criteria criteria = new Criteria();
		criteria.setCostAllowed(true);
		criteria.setAccuracy(criteria.ACCURACY_FINE);

		String bestProvider = locationMan.getBestProvider(criteria, true);

		listener = new myLocationListener();

		locationMan.requestLocationUpdates(bestProvider, 0, 0, listener);

	}

	private class myLocationListener implements LocationListener {
		// 位置改变时候调用
		@Override
		public void onLocationChanged(Location location) {

			// float accuracy = location.getAccuracy();

			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			// double altitude = location.getAltitude();

			// 将经纬度信息保存sharePreferrce
			sharedPreferences.edit()
					.putString("location", "j" + longitude + "w" + latitude)
					.commit();
			
			//为了省电，每次调用结束后关闭
			stopSelf();

		}

		// 状态改变时调用（GPS没信号）
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		// 打开Gps
		@Override
		public void onProviderEnabled(String provider) {

		}

		// 关闭Gps
		@Override
		public void onProviderDisabled(String provider) {

		}

	}

	@Override
	public void onDestroy() {
		// 当页面关闭的时候结束定位方法，结束资源
		locationMan.removeUpdates(listener);
		super.onDestroy();
	}
}
