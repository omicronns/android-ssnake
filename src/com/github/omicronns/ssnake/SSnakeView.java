package com.github.omicronns.ssnake;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class SSnakeView extends View {
	
	SSnakeEngine snakeEngine;
	private Handler handler = new Handler();
	
	private Runnable stepTimer = new Runnable() {
		@Override
		public void run() {
			switch(dir) {
			case UP:
				snakeEngine.moveUp();
				break;
			case DOWN:
				snakeEngine.moveDown();
				break;
			case RIGHT:
				snakeEngine.moveRight();
				break;
			case LEFT:
				snakeEngine.moveLeft();
				break;
			}
			invalidate(gameSpaceRectInner);
			handler.postDelayed(this, 100);
		}
	};
	
	enum Direction {
		UP,
		DOWN,
		LEFT,
		RIGHT
	};
	Direction dir = Direction.RIGHT;
	
	float touchX;
	float touchY;
	Paint paint = new Paint();
	
	SButton left = new SButton();
	SButton right = new SButton();
	SButton down = new SButton();
	SButton up = new SButton();

	final int gameSpaceFrameThickness = 10;
	final int gameSpaceMargin = 10;
	int gameSpaceWidth;
	int gameSpaceHeight;
	int gameSpaceXSize = 50;
	int gameSpaceYSize = 50;
	final int buttonSpacing = 10;
	final int buttonHeight = 80;
	final int buttonWidth = 100;
	final int uiBottomMargin = 20;
	int uiColor = 0xff000000;
	int gameSpaceFrameColor = 0xff000000;
	int gameSpaceColor = 0xffffffff;
	int snakeColor = 0xffff0000;
	
	Rect uiRect = new Rect(0, 0, 1, 1);
	Rect gameSpaceRectOuter = new Rect(0, 0, 1, 1);
	Rect gameSpaceRectInner = new Rect(0, 0, 1, 1);
	Rect clippingRect = new Rect();
	Rect procSegmentRect = new Rect();
	
	public SSnakeView(Context context) {
		super(context);
	}

	public SSnakeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		snakeEngine = new SSnakeEngine(gameSpaceXSize, gameSpaceYSize);
		handler.postDelayed(stepTimer, 100);
	}
	
	//TODO: Optimize unnecessary computations
	private void drawUI(Canvas canvas, float x, float y) {
		uiRect.left = (int)(x - (buttonWidth + buttonWidth/2 + buttonSpacing));
		uiRect.right = (int)(x + (buttonWidth + buttonWidth/2 + buttonSpacing));
		uiRect.top = (int)(y - (buttonHeight + buttonSpacing/2));
		uiRect.bottom = (int)(y + (buttonHeight + buttonSpacing/2));
		
		paint.setColor(uiColor);
		down.setButton(x - buttonWidth/2,
				y + buttonSpacing/2,
				buttonWidth,
				buttonHeight);
		down.draw(canvas, paint);
		up.setButton(x - buttonWidth/2,
				y - (buttonHeight + buttonSpacing/2),
				buttonWidth,
				buttonHeight);
		up.draw(canvas, paint);			
		left.setButton(x - (buttonWidth + buttonWidth/2 + buttonSpacing),
				y - buttonHeight/2,
				buttonWidth,
				buttonHeight);
		left.draw(canvas, paint);		
		right.setButton(x + buttonWidth/2 + buttonSpacing,
				y - buttonHeight/2,
				buttonWidth,
				buttonHeight);
		right.draw(canvas, paint);
	}

	private void drawGameSpace(Canvas canvas, float x, float y) {
		gameSpaceWidth = getWidth() - gameSpaceMargin*2;
		gameSpaceHeight = gameSpaceWidth;
		
		gameSpaceRectOuter.top = (int)y;
		gameSpaceRectOuter.bottom = (int)(y + gameSpaceHeight);
		gameSpaceRectOuter.left = (int)x;
		gameSpaceRectOuter.right = (int)(x + gameSpaceWidth);
		paint.setColor(gameSpaceFrameColor);
		canvas.drawRect(gameSpaceRectOuter, paint);

		gameSpaceWidth -= gameSpaceFrameThickness*2;
		gameSpaceHeight -= gameSpaceFrameThickness*2;

		gameSpaceRectInner.top = gameSpaceRectOuter.top + gameSpaceFrameThickness;
		gameSpaceRectInner.bottom = gameSpaceRectOuter.bottom - gameSpaceFrameThickness;
		gameSpaceRectInner.left = gameSpaceRectOuter.left + gameSpaceFrameThickness;
		gameSpaceRectInner.right = gameSpaceRectOuter.right - gameSpaceFrameThickness;
		paint.setColor(gameSpaceColor);
		canvas.drawRect(gameSpaceRectInner, paint);
		
		ArrayList<Point> snake = snakeEngine.getSnake();
		for(int i = 0; i < snake.size(); ++i) {
			drawSnakeSegment(canvas, snake.get(i));
		}
	}
	
	private void drawSnakeSegment(Canvas canvas, Point segment) {
		procSegmentRect.left = gameSpaceRectInner.left + (segment.x*gameSpaceWidth)/gameSpaceXSize;
		procSegmentRect.right = procSegmentRect.left + (gameSpaceWidth/gameSpaceXSize);
		procSegmentRect.top = gameSpaceRectInner.top + (segment.y*gameSpaceHeight)/gameSpaceYSize;
		procSegmentRect.bottom = procSegmentRect.top + (gameSpaceHeight/gameSpaceYSize);
		paint.setColor(snakeColor);
		canvas.drawRect(procSegmentRect, paint);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		touchX = event.getX();
		touchY = event.getY();
		if(up.isTouched(touchX, touchY)) {
			dir = Direction.UP;
		}
		if(down.isTouched(touchX, touchY)) {
			dir = Direction.DOWN;
		}
		if(left.isTouched(touchX, touchY)) {
			dir = Direction.LEFT;
		}
		if(right.isTouched(touchX, touchY)) {
			dir = Direction.RIGHT;
		}
		return true;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.getClipBounds(clippingRect);
		int width = getWidth();
		int height = getHeight();
		if(Rect.intersects(clippingRect, uiRect)) {
			drawUI(canvas, width/2, height - (uiBottomMargin + buttonHeight + buttonSpacing/2));
		}
		if(Rect.intersects(clippingRect, gameSpaceRectOuter)) {
			drawGameSpace(canvas, gameSpaceMargin, gameSpaceMargin);
		}
	}
}

class SButton {
	
	RectF button = new RectF();
	
	public SButton() {
	}	
	
	public void setButton(float x, float y, float w, float h) {
		button.set(x, y, x + w, y + h);
	}
	
	public void draw(Canvas canvas, Paint paint) {
		canvas.drawRect(button, paint);
	}
	
	public boolean isTouched(float x, float y) {
		if(x > button.left && x < button.right && y < button.bottom && y > button.top)
			return true;
		return false;
	}
}
