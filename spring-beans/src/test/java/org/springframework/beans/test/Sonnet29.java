package org.springframework.beans.test;

/**
 * @description:
 * @author: hkai
 * @date: 2022/5/8 16:14
 * @version: 1.0
 */
public class Sonnet29 implements Poem {

	private static String words = "balblabla...";
	@Override
	public void recite() {
		// TODO Auto-generated method stub
		System.out.println(words);
	}


}
