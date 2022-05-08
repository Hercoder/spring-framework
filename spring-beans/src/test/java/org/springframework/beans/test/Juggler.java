package org.springframework.beans.test;

/**
 * @description:
 * @author: hkai
 * @date: 2022/5/8 16:09
 * @version: 1.0
 */
public class Juggler implements Performer{
	private int beanBags = 3;
	public Juggler(){

	}

	public Juggler(int beanBags){
		this.beanBags = beanBags;
	}

	@Override
	public void perform(){
		System.out.println("Juggling " + beanBags + " beanbags");
	}
}