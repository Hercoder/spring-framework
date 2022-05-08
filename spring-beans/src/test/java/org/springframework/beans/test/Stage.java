package org.springframework.beans.test;

/**
 * @description:
 * @author: hkai
 * @date: 2022/5/8 16:11
 * @version: 1.0
 */
public class Stage {

	private Stage(){

	}

	private static class StageSingletonHolder{
		static Stage instance = new Stage();
	}

	public static Stage getInstance(){
		return StageSingletonHolder.instance;
	}
}
