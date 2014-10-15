package com.github.omicronns.ssnake;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.github.omicronns.ssnake.SSnakeEngine.Direction;

public class SSnakeView extends View {
	
	private SSnakeEngine snakeEngine;
	private boolean runSnake = false;
	
	private float touchX;
	private float touchY;
	private Paint paint = new Paint();
	
	private SButton left = new SButton();
	private SButton right = new SButton();
	private SButton down = new SButton();
	private SButton up = new SButton();

	private final int gameSpaceFrameThickness = 10;
	private final int gameSpaceMargin = 10;
	private int gameSpaceWidth;
	private int gameSpaceHeight;
	private int gameSpaceXSize = 50;
	private int gameSpaceYSize = 50;
	private int snakeSegmentXSize;
	private int snakeSegmentYSize;
	private final int buttonSpacing = 10;
	private final int buttonHeight = 90;
	private final int buttonWidth = 130;
	private final int uiBottomMargin = 20;
	private int uiColor = 0xff000000;
	private int gameSpaceFrameColor = 0xff000000;
	private int gameSpaceColor = 0xffffffff;
	private int snakeColor = 0xffff0000;
	private int appleColor = 0xff00ff00;
	private int transparentAppleColor = 0xff0000ff;
	
	private Rect uiRect = new Rect(0, 0, 1, 1);
	private Rect gameSpaceRectOuter = new Rect(0, 0, 1, 1);
	private Rect gameSpaceRectInner = new Rect(0, 0, 1, 1);
	private Rect clippingRect = new Rect();
	private Rect procSegmentRect = new Rect();
	
	public SSnakeView(Context context) {
		super(context);
		snakeEngine = SSnakeEngineSingleton.getInstance(gameSpaceXSize, gameSpaceYSize);
		SnakeTimer.start(this);
	}

	public SSnakeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		snakeEngine = SSnakeEngineSingleton.getInstance(gameSpaceXSize, gameSpaceYSize);
		SnakeTimer.start(this);
	}
	
	public void snakeStop() {
		runSnake = false;
	}
	
	public void snakeStart() {
		runSnake = true;
	}
	
	public void snakeRestart() {
		snakeEngine.restartEngine();
		invalidate(gameSpaceRectOuter);
	}
	
	public void snakeStep() {
		if(runSnake) {
			snakeEngine.move();
			invalidate(gameSpaceRectOuter);
			if(snakeEngine.areWallsTransparent()) {
				gameSpaceFrameColor += 100;
				gameSpaceFrameColor |= 0xff000000;
			}
		}
		else {
			gameSpaceFrameColor = 0xff000000;
		}
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

	//TODO: Optimize unnecessary computations
	private void drawGameSpace(Canvas canvas, float x, float y) {
		drawGameSpaceFrame(canvas, x, y);
		
		ArrayList<Point> snake = snakeEngine.getSnake();
		for(int i = 0; i < snake.size(); ++i) {
			drawSegment(canvas, snake.get(i), snakeColor);
		}
		
		ArrayList<Point> apples = snakeEngine.getApples();
		for(int i = 0; i < apples.size(); ++i) {
			drawSegment(canvas, apples.get(i), appleColor);
		}
		
		ArrayList<Point> transparentApples = snakeEngine.getTransparentApples();
		for(int i = 0; i < transparentApples.size(); ++i) {
			drawSegment(canvas, transparentApples.get(i), transparentAppleColor);
		}
	}

	//TODO: Optimize unnecessary computations
	private void drawGameSpaceFrame(Canvas canvas, float x, float y) {
		int size = getWidth();
		if(size > getHeight())
			size = getHeight();
		gameSpaceWidth = size - gameSpaceMargin*2;
		gameSpaceHeight = gameSpaceWidth;
		
		gameSpaceRectOuter.top = (int)y;
		gameSpaceRectOuter.bottom = (int)(y + gameSpaceHeight);
		gameSpaceRectOuter.left = (int)x;
		gameSpaceRectOuter.right = (int)(x + gameSpaceWidth);
		paint.setColor(gameSpaceFrameColor);
		canvas.drawRect(gameSpaceRectOuter, paint);

		gameSpaceWidth -= gameSpaceFrameThickness*2;
		gameSpaceHeight -= gameSpaceFrameThickness*2;

		int frameHeight = (gameSpaceRectOuter.bottom - gameSpaceRectOuter.top - gameSpaceHeight)/2;
		int frameWidth = (gameSpaceRectOuter.right - gameSpaceRectOuter.left - gameSpaceWidth)/2;

		gameSpaceRectInner.top = gameSpaceRectOuter.top + frameHeight;
		gameSpaceRectInner.bottom = gameSpaceRectOuter.bottom - frameHeight;
		gameSpaceRectInner.left = gameSpaceRectOuter.left + frameWidth;
		gameSpaceRectInner.right = gameSpaceRectOuter.right - frameWidth;
		paint.setColor(gameSpaceColor);
		canvas.drawRect(gameSpaceRectInner, paint);
	}
	
	private void drawSegment(Canvas canvas, Point segment, int color) {
		procSegmentRect.left = gameSpaceRectInner.left + (segment.x*gameSpaceWidth)/gameSpaceXSize;
		procSegmentRect.right = gameSpaceRectInner.left + ((segment.x + 1)*gameSpaceWidth)/gameSpaceXSize;
		procSegmentRect.top = gameSpaceRectInner.top + (segment.y*gameSpaceHeight)/gameSpaceYSize;
		procSegmentRect.bottom = gameSpaceRectInner.top + ((segment.y + 1)*gameSpaceHeight)/gameSpaceYSize;
		paint.setColor(color);
		canvas.drawRect(procSegmentRect, paint);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		touchX = event.getX();
		touchY = event.getY();
		if(event.getActionMasked() == MotionEvent.ACTION_DOWN) {
			if(up.isTouched(touchX, touchY)) {
				snakeEngine.setDirection(Direction.UP);
			}
			if(down.isTouched(touchX, touchY)) {
				snakeEngine.setDirection(Direction.DOWN);
			}
			if(left.isTouched(touchX, touchY)) {
				snakeEngine.setDirection(Direction.LEFT);
			}
			if(right.isTouched(touchX, touchY)) {
				snakeEngine.setDirection(Direction.RIGHT);
			}
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
			if(getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
				drawUI(canvas, width/2, height - (uiBottomMargin + buttonHeight + buttonSpacing/2));
			}
			else {
				drawUI(canvas, width - (uiBottomMargin + buttonWidth + buttonWidth/2 + buttonSpacing), height/2);
			}
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

class SSnakeEngineSingleton {
	private static SSnakeEngine selfEngine = null;
	
	public static SSnakeEngine getInstance(int x, int y) {
		if(selfEngine == null) {
			selfEngine = new SSnakeEngine(x, y);
		}
		return selfEngine; 
	}
}

class SnakeTimer {
	private static boolean isStarted = false;
	private static Handler handler = new Handler();
	private static SSnakeView snakeView;

	private static Runnable stepTimer = new Runnable() { 
		@Override
		public void run() {
			snakeView.snakeStep();
			handler.postDelayed(this, 100);
		}
	};
	
	public static void start(SSnakeView view) {
		if(!isStarted) {
			handler.postDelayed(stepTimer, 0);
			isStarted = true;
		}
		snakeView = view;
	}
}
