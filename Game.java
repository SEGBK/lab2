/**
 * Game.java - lab2
 * Handles the game logic.
 */

import lib.*;

class Game {
    public static void main(String[] args) {
		Board b = new Board();
		System.out.println(b.toString());   
		b.play(0,0);
		System.out.println(b.toString());        
		b.play(0,1);
		System.out.println(b.toString());        
		b.play(0,2);
		System.out.println(b.toString());        
		b.play(1,1);
		System.out.println(b.toString());        
		b.play(1,0);
		System.out.println(b.toString());        
		b.play(2,2);
		System.out.println(b.toString());        
		b.play(2,0);
		System.out.println(b.toString());        
    }
}