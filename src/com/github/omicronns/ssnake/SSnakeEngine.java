package com.github.omicronns.ssnake;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Point;


public class SSnakeEngine {
	
	public enum Direction {
		UP,
		LEFT,
		DOWN,
		RIGHT
	};
	private Direction dir = Direction.RIGHT;
	
	private Random rand = new Random();
	private ArrayList<Point> snake;
	private ArrayList<Point> apples;
	private ArrayList<Point> transparentApples;
	private int xSize;
	private int ySize;
	private boolean died;
	private boolean removeTail;
	private boolean transparentWalls;

	private int movesToAppleCount;
	private int movesToNextApple;
	private int transparentOffCount;
	private int movesToTransparentOff;
	
	public SSnakeEngine(int sizeX, int sizeY) {
		xSize = sizeX;
		ySize = sizeY;
		restartEngine();
	}
	
	public void restartEngine() {
		dir = Direction.RIGHT;
		snake = new ArrayList<Point>();
		apples = new ArrayList<Point>();
		transparentApples = new ArrayList<Point>();
		died = false;
		removeTail = false;
		transparentWalls = false;
		movesToAppleCount = 0;
		snake.add(new Point(5, 5));
		for(int i = 0; i < 10; ++i) {
			removeTail = false;
			move();
		}
		removeTail = true;
		movesToAppleCount = 0;
		movesToNextApple = rand.nextInt(10) + 20;
		movesToTransparentOff = 50;
	}
	
	public Direction getDirection() {
		return dir;
	}
	
	public void setDirection(Direction direction) {
		if(Math.abs((dir.ordinal() - direction.ordinal())) % 2 == 1)
			dir = direction;
	}
	
	public ArrayList<Point> getSnake() {
		return new ArrayList<Point>(snake);
	}
	
	public ArrayList<Point> getApples() {
		return new ArrayList<Point>(apples);
	}
	
	public ArrayList<Point> getTransparentApples() {
		return new ArrayList<Point>(transparentApples);
	}
	
	public boolean isDead() {
		return died;
	}
	
	public boolean areWallsTransparent() {
		return transparentWalls;
	}
	
	private void genApple() {
		Point apple = new Point();
		while(true) {
			apple.x = rand.nextInt(xSize);
			apple.y = rand.nextInt(ySize);
			if(!snake.contains(apple) && !apples.contains(apple) &&
			   !transparentApples.contains(apple)) {
				apples.add(apple);
				break;
			}
		} 
	}
	
	private void genTransparentApple() {
		Point apple = new Point();
		while(true) {
			apple.x = rand.nextInt(xSize);
			apple.y = rand.nextInt(ySize);
			if(!snake.contains(apple) && !apples.contains(apple) &&
			   !transparentApples.contains(apple)) {
				transparentApples.add(apple);
				break;
			}
		} 
	}

	public boolean move() {
		if(died)
			return false;
		
		if(++movesToAppleCount == movesToNextApple) {
			if(rand.nextInt(100) > 10)
				genApple();
			else
				genTransparentApple();
			movesToAppleCount = 0;
			movesToNextApple = rand.nextInt(50) + 40;
		}
		
		if(transparentWalls) {
			if(++transparentOffCount == movesToTransparentOff) {
				transparentWalls = false;
				movesToTransparentOff = rand.nextInt(50) + 120;
			}
		}
		
		Point head = snake.get(snake.size() - 1);
		Point ahead = getAhead(dir, head);
		if(snake.contains(ahead)) {
			died = true;
			return false;
		}
		if(apples.contains(ahead)) {
			apples.remove(ahead);
			removeTail = false;
		}
		if(transparentApples.contains(ahead)) {
			transparentApples.remove(ahead);
			transparentOffCount = 0;
			removeTail = false;
			transparentWalls = true;
		}
		
		switch(dir) {
		case UP:
			if(head.y == 0 && !transparentWalls) {
				died = true;
				return false;
			}
			break;
		case DOWN:
			if(head.y == ySize - 1 && !transparentWalls) {
				died = true;
				return false;
			}
			break;
		case RIGHT:
			if(head.x == xSize - 1 && !transparentWalls) {
				died = true;
				return false;
			}
			break;
		case LEFT:
			if(head.x == 0 && !transparentWalls) {
				died = true;
				return false;
			}
			break;
		default:
			break;
		}
		snake.add(ahead);
		if(removeTail)
			snake.remove(0);
		removeTail = true;
		return true;
	}
	
	private Point getAhead(Direction direction, Point head) {
		Point ahead;
		switch (dir) {
		case UP:
			ahead = new Point(head.x, head.y - 1);
			if(ahead.y < 0)
				ahead.y = ySize - 1;
			return ahead;
		case DOWN:
			ahead = new Point(head.x, head.y + 1);
			if(ahead.y >= ySize)
				ahead.y = 0;
			return ahead;
		case RIGHT:
			ahead = new Point(head.x + 1, head.y);
			if(ahead.x >= xSize)
				ahead.x = 0;
			return ahead;
		case LEFT:
			ahead = new Point(head.x - 1, head.y);
			if(ahead.x < 0)
				ahead.x = xSize - 1;
			return ahead;
		default:
			return new Point(head);
		}
	}
}
