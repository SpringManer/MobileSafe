package com.itheima52.mobilesafe.test;

import java.util.Random;

import android.test.AndroidTestCase;

import com.itheima52.mobilesafe.db.dao.BlackNumDao;

public class Test extends AndroidTestCase {

	public void insert() {

		BlackNumDao instance = BlackNumDao.getInstance(getContext());
		
		for (int i = 0; i < 100; i++) {
			if(i<10){
				
				instance.insert("1529510123"+i, 1+new Random().nextInt(3)+"");
				
			}else{
				instance.insert("152951012"+i, 1+new Random().nextInt(3)+"");
			}
			
			
		}


	}

}
