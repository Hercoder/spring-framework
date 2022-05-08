package org.springframework.beans.test;

/**
 * @description:
 * @author: hkai
 * @date: 2022/5/8 16:10
 * @version: 1.0
 */
public class PoeticJuggler extends Juggler{

	private Poem poem;

	public PoeticJuggler(Poem poem){
		super();
		this.poem = poem;
	}

	public PoeticJuggler(int beanBags, Poem poem){
		super(beanBags);
		this.poem = poem;
	}

	public void perform(){
		super.perform();
		System.out.println("PoeticJugger reciting...");
		poem.recite();
	}
}
