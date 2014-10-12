package com.github.omicronns.ssnake;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Point;


public class SSnakeEngine {
	
	private Random rand = new Random();
	private ArrayList<Point> snake = new ArrayList<Point>();
	private int xSize;
	private int ySize;
	private boolean died = false;
	private boolean removeTail = false;
	
	public SSnakeEngine(int sizeX, int sizeY) {
		xSize = sizeX;
		ySize = sizeY;
		snake.add(new Point(5, 5));
		for(int i = 0; i < 30; ++i) {
			moveRight();
		}
		removeTail = true;
	}

	public boolean moveUp() {
		if(died)
			return false;
		
		Point head = snake.get(snake.size() - 1);
		Point ahead = new Point(head.x, head.y - 1);
		if(head.y == 0 || snake.contains(ahead)) {
			died = true;
			return false;
		}
		else {
			snake.add(ahead);
			if(removeTail)
				snake.remove(0);
			return true;
		}
	}
	
	public boolean moveDown() {
		if(died)
			return false;
		
		Point head = snake.get(snake.size() - 1);
		Point ahead = new Point(head.x, head.y + 1);
		if(head.y == ySize - 1 || snake.contains(ahead)) {
			died = true;
			return false;
		}
		else {
			snake.add(new Point(ahead));
			if(removeTail)
				snake.remove(0);
			return true;
		}
	}
	
	public boolean moveRight() {
		if(died)
			return false;
		
		Point head = snake.get(snake.size() - 1);
		Point ahead = new Point(head.x + 1, head.y);
		if(head.x == xSize - 1 || snake.contains(ahead)) {
			died = true;
			return false;
		}
		else {
			snake.add(new Point(ahead));
			if(removeTail)
				snake.remove(0);
			return true;
		}
	}
	
	public boolean moveLeft() {
		if(died)
			return false;
		
		Point head = snake.get(snake.size() - 1);
		Point ahead = new Point(head.x - 1, head.y);
		if(head.x == 0 || snake.contains(ahead)) {
			died = true;
			return false;
		}
		else {
			snake.add(new Point(ahead));
			if(removeTail)
				snake.remove(0);
			return true;
		}
	}
	
	public ArrayList<Point> getSnake() {
		return new ArrayList<Point>(snake);
	}
	
	public boolean isDead() {
		return died;
	}
}
