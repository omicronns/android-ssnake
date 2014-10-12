package com.github.omicronns.ssnake;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class SSnakeView extends View {
	
	float touchX;
	float touchY;
	float ballX = 300;
	float ballY = 300;
	Paint paint = new Paint();
	
	Button left = new Button();
	Button right = new Button();
	Button down = new Button();
	Button up = new Button();

	final int bottomMargin = 10;
	final int buttonSpacing = 10;
	final int buttonHeight = 80;
	final int buttonWidth = 100;

	public SSnakeView(Context context) {
		super(context);
	}

	public SSnakeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		invalidate(10, 10, 100, 100);
	}
	
	private void drawUI(Canvas canvas) {
		int width = getWidth();
		int height = getHeight();
		paint.setColor(0xff000000);
		
		down.setButton((width - buttonWidth)/2,
				height - bottomMargin - buttonHeight,
				buttonWidth,
				buttonHeight);
		down.draw(canvas, paint);		
		up.setButton((width - buttonWidth)/2,
				height - bottomMargin - buttonHeight*2 - buttonSpacing,
				buttonWidth,
				buttonHeight);
		up.draw(canvas, paint);		
		left.setButton((width - buttonWidth)/2 - buttonWidth - buttonSpacing,
				height - bottomMargin - (buttonHeight*2 + buttonSpacing + buttonHeight)/2,
				buttonWidth,
				buttonHeight);
		left.draw(canvas, paint);		
		right.setButton((width + buttonWidth)/2 + buttonSpacing,
				height - bottomMargin - (buttonHeight*2 + buttonSpacing + buttonHeight)/2,
				buttonWidth,
				buttonHeight);
		right.draw(canvas, paint);
	}

	boolean aaa = false;
	Rect bbb = new Rect();
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		touchX = event.getX();
		touchY = event.getY();
		if(up.isTouched(touchX, touchY)) {
			bbb.left = 10;
			bbb.top = 10;
			bbb.bottom = 100;
			bbb.right = 100;
//			invalidate(bbb);
			postInvalidate(10, 10, 100, 100);
			aaa = true;
			ballY -= 10;
		}
		if(down.isTouched(touchX, touchY))
			ballY += 10;
		if(left.isTouched(touchX, touchY))
			ballX -= 10;
		if(right.isTouched(touchX, touchY))
			ballX += 10;
		invalidate();
		return true;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(aaa) {
			paint.setColor(0xff000000);
			canvas.drawRect(canvas.getClipBounds(), paint);
			aaa = false;
		}
		else {
			drawUI(canvas);
			paint.setColor(0xff000000);
			canvas.drawCircle(ballX, ballY, 20, paint);
		}
//		paint.setColor(0xff000000);
//		Rect r = canvas.getClipBounds();
//		canvas.drawRect(r, paint);
		
	}
}

class Button {
	
	RectF button = new RectF();
	
	public Button() {
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
