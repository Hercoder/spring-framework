package org.springframework.beans.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @description:
 * @author: hkai
 * @date: 2022/5/8 16:16
 * @version: 1.0
 */
public class Test {

	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				"springAction.xml");
		try{
			Performer performer = (Performer)ctx.getBean("duke");
			performer.perform();
			performer = (Performer)ctx.getBean("poeticDuke");
			performer.perform();
			performer = (Performer)ctx.getBean("kenny");
			performer.perform();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			((ClassPathXmlApplicationContext)ctx).close();
		}
	}
}
