package com.github.omicronns.ssnake;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Point;


public class SSnakeEngine {
	
	public enum Direction {
		UP,
		DOWN,
		LEFT,
		RIGHT
	};
	private Direction dir = Direction.RIGHT;
	
	private Random rand = new Random();
	private ArrayList<Point> snake = new ArrayList<Point>();
	private ArrayList<Point> apples = new ArrayList<Point>();
	private int xSize;
	private int ySize;
	private boolean died = false;
	private boolean removeTail = false;
	
	private int movesCount;
	private int movesToNextApple;
	
	public SSnakeEngine(int sizeX, int sizeY) {
		xSize = sizeX;
		ySize = sizeY;
		snake.add(new Point(5, 5));
		for(int i = 0; i < 10; ++i) {
			removeTail = false;
			moveRight();
		}
		removeTail = true;
		movesToNextApple = rand.nextInt(10) + 20;
	}
	
	public Direction getDirection() {
		return dir;
	}
	
	public void setDirection(Direction direction) {
		dir = direction;
	}
	
	public ArrayList<Point> getSnake() {
		return new ArrayList<Point>(snake);
	}
	
	public ArrayList<Point> getApples() {
		return new ArrayList<Point>(apples);
	}
	
	public boolean isDead() {
		return died;
	}
	
	private void genApple() {
		apples.add(new Point(rand.nextInt(xSize), rand.nextInt(ySize)));
	}
	
	private void eatApple(Point appleToEat) {
		apples.remove(appleToEat);
		removeTail = false;
	}

	public boolean move() {
		++movesCount;
		if(movesCount == movesToNextApple) {
			genApple();
			movesCount = 0;
			movesToNextApple = rand.nextInt(10) + 20;
		}
		switch(dir) {
		case UP:
			return moveUp();
		case DOWN:
			return moveDown();
		case RIGHT:
			return moveRight();
		case LEFT:
			return moveLeft();
		default:
			return true;
		}
	}

	private boolean moveUp() {
		if(died)
			return false;
		
		Point head = snake.get(snake.size() - 1);
		Point ahead = new Point(head.x, head.y - 1);
		if(apples.contains(ahead)) {
			eatApple(ahead);
		}
		if(head.y == 0 || snake.contains(ahead)) {
			died = true;
			return false;
		}
		else {
			snake.add(ahead);
			if(removeTail)
				snake.remove(0);
			dir = Direction.UP;
			removeTail = true;
			return true;
		}
	}
	
	private boolean moveDown() {
		if(died)
			return false;
		
		Point head = snake.get(snake.size() - 1);
		Point ahead = new Point(head.x, head.y + 1);
		if(apples.contains(ahead)) {
			eatApple(ahead);
		}
		if(head.y == ySize - 1 || snake.contains(ahead)) {
			died = true;
			return false;
		}
		else {
			snake.add(new Point(ahead));
			if(removeTail)
				snake.remove(0);
			dir = Direction.DOWN;
			removeTail = true;
			return true;
		}
	}
	
	private boolean moveRight() {
		if(died)
			return false;
		
		Point head = snake.get(snake.size() - 1);
		Point ahead = new Point(head.x + 1, head.y);
		if(apples.contains(ahead)) {
			eatApple(ahead);
		}
		if(head.x == xSize - 1 || snake.contains(ahead)) {
			died = true;
			return false;
		}
		else {
			snake.add(new Point(ahead));
			if(removeTail)
				snake.remove(0);
			dir = Direction.RIGHT;
			removeTail = true;
			return true;
		}
	}
	
	private boolean moveLeft() {
		if(died)
			return false;
		
		Point head = snake.get(snake.size() - 1);
		Point ahead = new Point(head.x - 1, head.y);
		if(apples.contains(ahead)) {
			eatApple(ahead);
		}
		if(head.x == 0 || snake.contains(ahead)) {
			died = true;
			return false;
		}
		else {
			snake.add(new Point(ahead));
			if(removeTail)
				snake.remove(0);
			dir = Direction.LEFT;
			removeTail = true;
			return true;
		}
	}
}
