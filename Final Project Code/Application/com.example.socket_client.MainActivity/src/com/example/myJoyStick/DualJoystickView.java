package com.example.myJoyStick;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * DualJoystickView is a class that implements us a linear layout with two joystick view.
 * 
 * @author      Shmulik Melamed
 * @author      Lital Motola
 * @version     v5.0
 * @since       1.0
 */
public class DualJoystickView extends LinearLayout {
	@SuppressWarnings("unused")

	//.: DualJoystickView variables.
	private static final String TAG = DualJoystickView.class.getSimpleName();

	private final boolean D = false;
	private Paint dbgPaint1;

	private JoystickView stickL;
	private JoystickView stickR;

	private View pad;
	//===========================

	//********************* ..:: DualJoystickView ::.. **********************//
	/** 
	 * Added in API level 1
	 * 
	 * Constructor Called when the object first created and initialize the object variables.  	
	 * 
	 * @param context the application environment
	 * @since           1.0
	 */	
	public DualJoystickView(Context context) {
		super(context);
		stickL = new JoystickView(context);
		stickR = new JoystickView(context);
		initDualJoystickView();
	}
	//************************************************************************//

	//********************* ..:: DualJoystickView ::.. ***********************//
	/** 
	 * Added in API level 1
	 * 
	 * Constructor Called when the object first created and initialize the object variables.  	
	 * 
	 * @param context the application environment
	 * @param attrs the joystick style.
	 * @since           1.0
	 */	
	public DualJoystickView(Context context, AttributeSet attrs) {
		super(context, attrs);
		stickL = new JoystickView(context, attrs);
		stickR = new JoystickView(context, attrs);
		initDualJoystickView();
	}
	//************************************************************************//

	//******************* ..:: initDualJoystickView ::.. *********************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when we want to initialize the dual joystick view Orientation and colors.  	
	 * 
	 * @since           1.0
	 */	
	private void initDualJoystickView() {
		setOrientation(LinearLayout.HORIZONTAL);
		stickL.setHandleColor(Color.rgb(0x00, 0x00, 0x00));
		stickR.setHandleColor(Color.rgb(0x00, 0x00, 0x00));

		if ( D ) {			
			dbgPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
			dbgPaint1.setColor(Color.CYAN);
			dbgPaint1.setStrokeWidth(1);
			dbgPaint1.setStyle(Paint.Style.STROKE);

		}

		pad = new View(getContext());
	}
	//************************************************************************//

	//************************** ..:: onMeasure ::.. *************************//
	/**
	 * Added in API level 1
	 * Measure the view and its content to determine the measured width and the measured height. This method is invoked by measure(int, int) and should be overriden by subclasses to provide accurate and efficient measurement of their contents. 
	 * CONTRACT: When overriding this method, you must call setMeasuredDimension(int, int) to store the measured width and height of this view. Failure to do so will trigger an IllegalStateException, thrown by measure(int, int). Calling the superclass' onMeasure(int, int) is a valid use. 
	 * The base class implementation of measure defaults to the background size, unless a larger size is allowed by the MeasureSpec. Subclasses should override onMeasure(int, int) to provide better measurements of their content. 
	 * If this method is overridden, it is the subclass's responsibility to make sure the measured height and width are at least the view's minimum height and width (getSuggestedMinimumHeight() and getSuggestedMinimumWidth()). 
	 *
	 * This method measure the layout in order to calculate the left and right joystick places. 
	 *
	 * @param widthMeasureSpec  horizontal space requirements as imposed by the parent. The requirements are encoded with View.MeasureSpec. 
	 * @param heightMeasureSpec  vertical space requirements as imposed by the parent. The requirements are encoded with View.MeasureSpec. 
	 * @since           1.0
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		removeView(stickL);
		removeView(stickR);

		float padW = getMeasuredWidth()-(getMeasuredHeight()*2);
		int joyWidth = (int) ((getMeasuredWidth()-padW)/2);
		LayoutParams joyLParams = new LayoutParams(joyWidth,getMeasuredHeight());

		stickL.setLayoutParams(joyLParams);
		stickR.setLayoutParams(joyLParams);

		stickL.TAG = "L";
		stickR.TAG = "R";
		stickL.setPointerId(JoystickView.INVALID_POINTER_ID);
		stickR.setPointerId(JoystickView.INVALID_POINTER_ID);

		addView(stickL);

		ViewGroup.LayoutParams padLParams = new ViewGroup.LayoutParams((int) padW,getMeasuredHeight());
		removeView(pad);
		pad.setLayoutParams(padLParams);
		addView(pad);

		addView(stickR);
	}
	//************************************************************************//

	//************************** ..:: onLayout ::.. **************************//
	/**
	 * Added in API level 1
	 * Called from layout when this view should assign a size and position to each of its children. Derived classes with children should override this method and call layout on each of their children. 
	 *
	 * @param changed  This is a new size or position for this view 
	 * @param l  Left position, relative to parent 
	 * @param t  Top position, relative to parent 
	 * @param r  Right position, relative to parent 
	 * @param b  Bottom position, relative to parent
	 *   
	 * @since           1.0
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		stickR.setTouchOffset(stickR.getLeft(), stickR.getTop());
	}
	//************************************************************************//

	//******************* ..:: setAutoReturnToCenter ::.. ********************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when we want to return the left and right joystick to the center automatically.  	
	 * 
	 * @param left  joystick true if already in center and false if not. 
	 * @param right joystick true if already in center and false if not. 
	 * @since           1.0
	 */	
	public void setAutoReturnToCenter(boolean left, boolean right) {
		stickL.setAutoReturnToCenter(left);
		stickR.setAutoReturnToCenter(right);
	}
	//************************************************************************//

	//****************** ..:: setOnJostickMovedListener ::.. *****************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when we want to set a joystick listener to the left and right joystick.  	
	 * 
	 * @param left  joystick moved listener 
	 * @param right joystick moved listener 
	 * @since           1.0
	 */	
	public void setOnJostickMovedListener(JoystickMovedListener left, JoystickMovedListener right) {
		stickL.setOnJostickMovedListener(left);
		stickR.setOnJostickMovedListener(right);
	}
	//************************************************************************//

	//**************** ..:: setOnJostickClickedListener ::.. *****************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when we want to set a joystick clicked listener to the left and right joystick.  	
	 * 
	 * @param left  joystick clicked listener 
	 * @param right joystick clicked listener 
	 * @since           1.0
	 */	
	public void setOnJostickClickedListener(JoystickClickedListener left, JoystickClickedListener right) {
		stickL.setOnJostickClickedListener(left);
		stickR.setOnJostickClickedListener(right);
	}
	//************************************************************************//

	//******************** ..:: setYAxisInverted ::.. ************************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when we want to set YAxis Inverted to the left and right joystick.  	
	 * 
	 * @param leftYAxisInverted  joystick true if not inverted and false if is inverted.
	 * @param rightYAxisInverted joystick true if not inverted and false if is inverted.
	 * @since           1.0
	 */	
	public void setYAxisInverted(boolean leftYAxisInverted, boolean rightYAxisInverted) {
		stickL.setYAxisInverted(leftYAxisInverted);
		stickR.setYAxisInverted(rightYAxisInverted);
	}
	//************************************************************************//

	//******************* ..:: setMovementConstraint ::.. ********************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when we want to set movement constraint to the left and right joystick.  	
	 * 
	 * @param movementConstraint the constraint number from the joystik center.
	 * @since           1.0
	 */	
	public void setMovementConstraint(int movementConstraint) {
		stickL.setMovementConstraint(movementConstraint);
		stickR.setMovementConstraint(movementConstraint);
	}
	//************************************************************************//

	//********************* ..:: setMovementRange ::.. ***********************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when we want to set movment range to the left and right joystick.  	
	 * 
	 * @param movementRangeLeft  the range from the center point.
	 * @param movementRangeRight the range from the center point.
	 * @since           1.0
	 */	
	public void setMovementRange(float movementRangeLeft, float movementRangeRight) {
		stickL.setMovementRange(movementRangeLeft);
		stickR.setMovementRange(movementRangeRight);
	}
	//************************************************************************//

	//******************** ..:: setMoveResolution ::.. ***********************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when we want to set move resolution to the left and right joystick.  	
	 * 
	 * @param leftMoveResolution  the move resolution from the center point.
	 * @param rightMoveResolution the move resolution from the center point.
	 * @since           1.0
	 */	
	public void setMoveResolution(float leftMoveResolution, float rightMoveResolution) {
		stickL.setMoveResolution(leftMoveResolution);
		stickR.setMoveResolution(rightMoveResolution);
	}
	//************************************************************************//

	//****************** ..:: setUserCoordinateSystem ::.. *******************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when we want to set user coordinate system to the left and right joystick.  	
	 * 
	 * @param leftCoordinateSystem  the user coordinateS system from the center point.
	 * @param rightCoordinateSystem the user coordinateS system from the center point.
	 * @since           1.0
	 */	
	public void setUserCoordinateSystem(int leftCoordinateSystem, int rightCoordinateSystem) {
		stickL.setUserCoordinateSystem(leftCoordinateSystem);
		stickR.setUserCoordinateSystem(rightCoordinateSystem);
	}
	//************************************************************************//

	//*********************** ..:: dispatchDraw ::.. *************************//
	/**
	 * Added in API level 1
	 * Called by draw to draw the child views. This may be overridden by derived classes to gain control just before its children are drawn (but after its own view has been drawn). 
	 *
	 * @param canvas  the canvas on which to draw the view  
	 * @since           1.0
	 */
	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (D) {
			canvas.drawRect(1, 1, getMeasuredWidth()-1, getMeasuredHeight()-1, dbgPaint1);
		}
	}
	//************************************************************************//

	//********************* ..:: dispatchTouchEvent ::.. *********************//
	/**
	 * Added in API level 1
	 * Pass the touch screen motion event down to the target view, or this view if it is the target. 
	 *
	 * @param ev  The motion event to be dispatched. 
	 * @return True if the event was handled by the view, false otherwise. 
	 * @since           1.0
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		boolean l = stickL.dispatchTouchEvent(ev);
		boolean r = stickR.dispatchTouchEvent(ev);
		return l || r;
	}
	//************************************************************************//

	//*********************** ..:: onTouchEvent ::.. *************************//
	/**
	 * Added in API level 1
	 * Implement this method to handle touch screen motion events.
	 *
	 * @param ev  The motion event. 
	 * @return True if the event was handled, false otherwise. 
	 * @since           1.0
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		boolean l = stickL.onTouchEvent(ev);
		boolean r = stickR.onTouchEvent(ev);
		return l || r;
	}
	//************************************************************************//
}
