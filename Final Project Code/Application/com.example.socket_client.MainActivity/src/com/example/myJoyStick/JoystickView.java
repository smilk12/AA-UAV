package com.example.myJoyStick;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;

/**
 * JoystickView is a class that implements one joystick view.
 * 
 * @author      Shmulik Melamed
 * @author      Lital Motola
 * @version     v5.0
 * @since       1.0
 */
public class JoystickView extends View {

	//.: joystick system variables
	public static final int INVALID_POINTER_ID = -1;
	private final boolean D = false;
	String TAG = "JoystickView";
	//===========================

	//.: joystick model
	private Paint bgPaint;
	private Paint handlePaint;
	private Paint stickPaint;
	private Paint basePaint;
	//===========================

	//.: joystick view numbers
	private int innerPadding;
	private int bgRadius;
	private int handleRadius;
	private int movementRadius;
	private int handleInnerBoundaries;
	//===========================

	//.: joystick listeners
	private JoystickMovedListener moveListener;
	private JoystickClickedListener clickListener;
	//===========================

	//.: # of pixels movement required between reporting to the listener
	private float moveResolution;
	//===========================

	//.: joystick check variables
	private boolean yAxisInverted;
	private boolean autoReturnToCenter;
	//===========================

	//.: Max range of movement in user coordinate system
	public final static int CONSTRAIN_BOX = 0;
	public final static int CONSTRAIN_CIRCLE = 1;
	private int movementConstraint;
	private float movementRange;
	//===========================

	//.: Regular cartesian coordinates
	public final static int COORDINATE_CARTESIAN = 0;		
	//===========================

	//.: Uses polar rotation of 45 degrees to calc differential drive paramaters
	public final static int COORDINATE_DIFFERENTIAL = 1;	
	private int userCoordinateSystem;
	//===========================

	//.: Records touch pressure for click handling
	private float touchPressure;
	private boolean clicked;
	private float clickThreshold;
	//===========================

	//.: Last touch point in view coordinates
	private int pointerId = INVALID_POINTER_ID;
	private float touchX, touchY;
	//===========================

	//.: Last reported position in view coordinates (allows different reporting sensitivities)
	private float reportX, reportY;
	//===========================

	//.: Handle center in view coordinates
	private float handleX, handleY;
	//===========================

	//.: Center of the view in view coordinates
	private int cX, cY;
	//===========================

	//.: Size of the view in view coordinates
	private int dimX;//, dimY;
	//===========================

	//.: Cartesian coordinates of last touch point - joystick center is (0,0)
	private int cartX, cartY;
	//===========================

	//.: Polar coordinates of the touch point from joystick center
	private double radial;
	private double angle;
	//===========================

	//.: User coordinates of last touch point
	private int userX, userY;
	//===========================

	//.: Offset co-ordinates (used when touch events are received from parent's coordinate origin)
	private int offsetX;
	private int offsetY;
	//===========================


	//************************ ..:: JoystickView ::.. ************************//
	/** 
	 * Added in API level 1
	 * 
	 * Constructor Called when the object first created and initialize the object variables.  	
	 * 
	 * @param context the application environment
	 * @since           1.0
	 */	
	public JoystickView(Context context) {
		super(context);
		initJoystickView();
	}
	//************************************************************************//

	//************************ ..:: JoystickView ::.. ************************//
	/** 
	 * Added in API level 1
	 * 
	 * Constructor Called when the object first created and initialize the object variables.  	
	 * 
	 * @param context the application environment
	 * @param attrs the progress bar style.
	 * @since           1.0
	 */	
	public JoystickView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initJoystickView();
	}
	//************************************************************************//

	//************************ ..:: JoystickView ::.. ************************//
	/** 
	 * Added in API level 1
	 * 
	 * Constructor Called when the object first created and initialize the object variables.  	
	 * 
	 * @param context the application environment
	 * @param attrs the progress bar style.
	 * @param defStyle the progress bar style.
	 * @since           1.0
	 */	
	public JoystickView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initJoystickView();
	}
	//************************************************************************//

	//********************* ..:: initJoystickView ::.. ***********************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when we want to initialize the object variables.  	
	 * 
	 * @since           1.0
	 */	
	private void initJoystickView() {
		setFocusable(true);

		bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		bgPaint.setColor(Color.GRAY);
		bgPaint.setStrokeWidth(1);
		bgPaint.setStyle(Paint.Style.FILL_AND_STROKE);

		handlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		handlePaint.setColor(Color.DKGRAY);
		handlePaint.setStrokeWidth(1);
		handlePaint.setStyle(Paint.Style.FILL_AND_STROKE);

		stickPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		stickPaint.setColor(Color.rgb(0x30, 0x30, 0x40));
		stickPaint.setStrokeWidth(15);
		stickPaint.setStyle(Paint.Style.FILL_AND_STROKE);

		basePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		basePaint.setColor(Color.rgb(0x40, 0x40, 0x20));
		basePaint.setStrokeWidth(1);
		basePaint.setStyle(Paint.Style.FILL_AND_STROKE);

		innerPadding = 10;

		setMovementRange(10);
		setMoveResolution(1.0f);
		setClickThreshold(0.4f);
		setYAxisInverted(true);
		setUserCoordinateSystem(COORDINATE_CARTESIAN);
		setAutoReturnToCenter(true);
	}
	//************************************************************************//

	//*********************** ..:: setHandleColor ::.. ***********************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when the to set the joystcik handle color.  	
	 * 
	 * @param color the color number.
	 * @since           1.0
	 */	
	public void setHandleColor(int color) {
		this.handlePaint.setColor(color);
	}
	//************************************************************************//

	//****************** ..:: setAutoReturnToCenter ::.. *********************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when want to set the joystick to auto return to center.  	
	 * 
	 * @param autoReturnToCenter true if auto return and false if not.
	 * @since           1.0
	 */	
	public void setAutoReturnToCenter(boolean autoReturnToCenter) {
		this.autoReturnToCenter = autoReturnToCenter;
	}
	//************************************************************************//

	//****************** ..:: isAutoReturnToCenter ::.. **********************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when want to know if the joystick is set to auto returned to center.  	
	 * 
	 * @return true if auto return to center and false if not.
	 * @since           1.0
	 */	
	public boolean isAutoReturnToCenter() {
		return autoReturnToCenter;
	}
	//************************************************************************//

	//***************** ..:: setUserCoordinateSystem ::.. ********************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when to set the user coordinate system.  	
	 * 
	 * @param userCoordinateSystem the coordinate system number.
	 * @since           1.0
	 */	
	public void setUserCoordinateSystem(int userCoordinateSystem) {
		if (userCoordinateSystem < COORDINATE_CARTESIAN || movementConstraint > COORDINATE_DIFFERENTIAL)
			Log.e(TAG, "invalid value for userCoordinateSystem");
		else
			this.userCoordinateSystem = userCoordinateSystem;
	}
	//************************************************************************//

	//***************** ..:: getUserCoordinateSystem ::.. ********************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when to knoe the user coordinate system.  	
	 * 
	 * @return  the user coordinate system.
	 * @since           1.0
	 */	
	public int getUserCoordinateSystem() {
		return userCoordinateSystem;
	}
	//************************************************************************//

	//****************** ..:: setMovementConstraint ::.. *********************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when the to set the joystick movement constraint.  	
	 * 
	 * @param movementConstraint the joystick movement constraint
	 * @since           1.0
	 */	
	public void setMovementConstraint(int movementConstraint) {
		if (movementConstraint < CONSTRAIN_BOX || movementConstraint > CONSTRAIN_CIRCLE)
			Log.e(TAG, "invalid value for movementConstraint");
		else
			this.movementConstraint = movementConstraint;
	}
	//************************************************************************//

	//******************** ..:: getMovementConstraint ::.. *******************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when know the joystick movement constraint.  	
	 * 
	 * @return the joystick movement constraint
	 * @since           1.0
	 */	
	public int getMovementConstraint() {
		return movementConstraint;
	}
	//************************************************************************//

	//*********************** ..:: isYAxisInverted ::.. **********************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when to know if the joystick YAxis inverted.  	
	 * 
	 * @return true if the joystick YAxis inverted and false if not.
	 * @since           1.0
	 */	
	public boolean isYAxisInverted() {
		return yAxisInverted;
	}
	//************************************************************************//

	//********************** ..:: setYAxisInverted ::.. **********************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when to set the joystick YAxis inverted.  	
	 * 
	 * @param yAxisInverted true to set the joystick YAxis inverted and false if not.
	 * @since           1.0
	 */	
	public void setYAxisInverted(boolean yAxisInverted) {
		this.yAxisInverted = yAxisInverted;
	}
	//************************************************************************//

	//********************* ..:: setClickThreshold ::.. **********************//
	/**
	 * Added in API level 1
	 * 
	 * Set the pressure sensitivity for registering a click
	 * 
	 * @param clickThreshold threshold 0...1.0f inclusive. 0 will cause clicks to never be reported, 1.0 is a very hard click
	 * @since           1.0
	 */
	public void setClickThreshold(float clickThreshold) {
		if (clickThreshold < 0 || clickThreshold > 1.0f)
			Log.e(TAG, "clickThreshold must range from 0...1.0f inclusive");
		else
			this.clickThreshold = clickThreshold;
	}
	//************************************************************************//

	//********************* ..:: getClickThreshold ::.. **********************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when to know the joystick click threshold.  	
	 * 
	 * @return the jotstick click threshold.
	 * @since           1.0
	 */	
	public float getClickThreshold() {
		return clickThreshold;
	}
	//************************************************************************//

	//********************** ..:: setMovementRange ::.. **********************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when to set the joystick movement range.  	
	 * 
	 * @param movementRange the joystick movement range
	 * @since           1.0
	 */	
	public void setMovementRange(float movementRange) {
		this.movementRange = movementRange;
	}
	//************************************************************************//

	//********************** ..:: getMovementRange ::.. **********************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when to know the joystick movement range.  	
	 * 
	 * @return the joystick movement range
	 * @since           1.0
	 */	
	public float getMovementRange() {
		return movementRange;
	}
	//************************************************************************//

	//********************* ..:: setMoveResolution ::.. **********************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when to set the joystick move resolution.  	
	 * 
	 * @param moveResolution the joystick move resolution.
	 * @since           1.0
	 */	
	public void setMoveResolution(float moveResolution) {
		this.moveResolution = moveResolution;
	}
	//************************************************************************//

	//********************* ..:: getMoveResolution ::.. **********************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when to know the the joystick move resolution.  	
	 * 
	 * @return the joystick move resolution.
	 * @since           1.0
	 */	
	public float getMoveResolution() {
		return moveResolution;
	}
	//************************************************************************//

	//**************** ..:: setOnJostickMovedListener ::.. *******************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when to set on joystick move listener.  	
	 * 
	 * @param listener on joystick move listener
	 * @since           1.0
	 */	
	public void setOnJostickMovedListener(JoystickMovedListener listener) {
		this.moveListener = listener;
	}
	//************************************************************************//

	//*************** ..:: setOnJostickClickedListener ::.. ******************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when to set on joystick clicked listener.  	
	 * 
	 * @param listener on joystick clicked listener
	 * @since           1.0
	 */	
	public void setOnJostickClickedListener(JoystickClickedListener listener) {
		this.clickListener = listener;
	}
	//************************************************************************//

	//************************ ..:: onMeasure ::.. ***************************//
	/**
	 * Added in API level 1
	 *
	 * Measure the view and its content to determine the measured width and the measured height.
	 * This method is invoked by measure(int, int) and should be overriden by subclasses to provide accurate and efficient measurement of their contents. 
	 *
	 * @param widthMeasureSpec  horizontal space requirements as imposed by the parent. The requirements are encoded with View.MeasureSpec. 
	 * @param heightMeasureSpec  vertical space requirements as imposed by the parent. The requirements are encoded with View.MeasureSpec. 
	 * @since           1.0
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// Here we make sure that we have a perfect circle
		int measuredWidth = measure(widthMeasureSpec);
		int measuredHeight = measure(heightMeasureSpec);
		setMeasuredDimension(measuredWidth, measuredHeight);
	}
	//************************************************************************//

	//************************* ..:: onLayout ::.. ***************************//
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
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		int d = Math.min(getMeasuredWidth(), getMeasuredHeight());

		dimX = d;
		//dimY = d;

		cX = d / 2;
		cY = d / 2;

		bgRadius = dimX/2 - innerPadding;
		//handleRadius = (int)(d * 0.25);
		handleRadius = (int)(d * 0.22);
		handleInnerBoundaries = handleRadius;
		movementRadius = Math.min(cX, cY) - handleInnerBoundaries;
	}
	//************************************************************************//

	//************************** ..:: measure ::.. ***************************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when we want to get the joystick measure.   	
	 * 
	 * @param measureSpec the measure spec we want to calculate.
	 * @since           1.0
	 */	
	private int measure(int measureSpec) {
		int result = 0;
		// Decode the measurement specifications.
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		if (specMode == MeasureSpec.UNSPECIFIED) {
			// Return a default size of 200 if no bounds are specified.
			result = 200;
		} else {
			// As you want to fill the available space
			// always return the full available bounds.
			result = specSize;
		}
		return result;
	}
	//************************************************************************//

	//*************************** ..:: onDraw ::.. ***************************//
	/**
	 * Added in API level 1
	 * 
	 * Implement this to do your drawing.
	 *
	 * @param canvas  the canvas on which the background will be drawn  
	 * @since       1.0
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.save();
		// Draw the background

		RadialGradient gradient = new android.graphics.RadialGradient(cX, cY, bgRadius+300, Color.WHITE, Color.BLUE, Shader.TileMode.CLAMP);


		bgPaint.setShader(gradient);
		canvas.drawCircle(cX, cY, bgRadius, bgPaint);

		// Draw the handle
		handleX = touchX + cX;
		handleY = touchY + cY;
		canvas.drawCircle(cX, cY, handleRadius>>1, basePaint);
		canvas.drawLine(cX, cY, handleX, handleY, stickPaint);
		canvas.drawCircle(handleX, handleY, handleRadius, handlePaint);

		if (D) {
			Log.d(TAG, String.format("(%.0f, %.0f)", touchX, touchY));
			Log.d(TAG, String.format("(%.0f, %.0f\u00B0)", radial, angle * 180.0 / Math.PI));
		}

		canvas.restore();
	}
	//************************************************************************//

	//************************ ..:: constrainBox ::.. ************************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when want to Constrain touch within a box.  	
	 * 
	 * @since           1.0
	 */	
	private void constrainBox() {
		touchX = Math.max(Math.min(touchX, movementRadius), -movementRadius);
		touchY = Math.max(Math.min(touchY, movementRadius), -movementRadius);
	}
	//************************************************************************//

	//********************** ..:: constrainCircle ::.. ***********************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when want to Constrain touch within a circle  	
	 * 
	 * @since           1.0
	 */	
	private void constrainCircle() {
		float diffX = touchX;
		float diffY = touchY;
		double radial = Math.sqrt((diffX*diffX) + (diffY*diffY));
		if ( radial > movementRadius ) {
			touchX = (int)((diffX / radial) * movementRadius);
			touchY = (int)((diffY / radial) * movementRadius);
		}
	}
	//************************************************************************//

	//************************ ..:: setPointerId ::.. ************************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when want to set pointer id.  	
	 * 
	 * @param id pointer id.
	 * @since           1.0
	 */	
	public void setPointerId(int id) {
		this.pointerId = id;
	}
	//************************************************************************//

	//************************ ..:: getPointerId ::.. ************************//
	/** 
	 * Added in API level 1
	 * 
	 * Constructor Called when to know the pointer id  	
	 * 
	 * @return the pointer id.
	 * @since           1.0
	 */	
	public int getPointerId() {
		return pointerId;
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
		final int action = ev.getAction();
		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_MOVE: {
			return processMoveEvent(ev);
		}	    
		case MotionEvent.ACTION_CANCEL: 
		case MotionEvent.ACTION_UP: {
			if ( pointerId != INVALID_POINTER_ID ) {
				returnHandleToCenter();
				setPointerId(INVALID_POINTER_ID);
			}
			break;
		}
		case MotionEvent.ACTION_POINTER_UP: {
			if ( pointerId != INVALID_POINTER_ID ) {
				final int pointerIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
				final int pointerId = ev.getPointerId(pointerIndex);
				if ( pointerId == this.pointerId ) {
					returnHandleToCenter();
					setPointerId(INVALID_POINTER_ID);
					return true;
				}
			}
			break;
		}
		case MotionEvent.ACTION_DOWN: {
			if ( pointerId == INVALID_POINTER_ID ) {
				int x = (int) ev.getX();
				if ( x >= offsetX && x < offsetX + dimX ) {
					setPointerId(ev.getPointerId(0));
					return true;
				}
			}
			break;
		}
		case MotionEvent.ACTION_POINTER_DOWN: {
			if ( pointerId == INVALID_POINTER_ID ) {
				final int pointerIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
				final int pointerId = ev.getPointerId(pointerIndex);
				int x = (int) ev.getX(pointerId);
				if ( x >= offsetX && x < offsetX + dimX ) {
					setPointerId(pointerId);
					return true;
				}
			}
			break;
		}
		}
		return false;
	}
	//************************************************************************//

	//********************** ..:: processMoveEvent ::.. **********************//
	/** 
	 * Added in API level 1
	 * 
	 * Implement this method to handle touch screen move events.
	 * 
	 * @param event  The motion event. 
	 * @return True if the event was handled, false otherwise. 
	 * @since           1.0
	 */	
	private boolean processMoveEvent(MotionEvent ev) {
		if ( pointerId != INVALID_POINTER_ID ) {
			final int pointerIndex = ev.findPointerIndex(pointerId);

			// Translate touch position to center of view
			float x = ev.getX(pointerIndex);
			touchX = x - cX - offsetX;
			float y = ev.getY(pointerIndex);
			touchY = y - cY - offsetY;

			reportOnMoved();
			invalidate();

			touchPressure = ev.getPressure(pointerIndex);
			reportOnPressure();

			return true;
		}
		return false;
	}
	//************************************************************************//

	//*********************** ..:: reportOnMoved ::.. ************************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when we want to report on joystick movement.  	
	 * 
	 * @since           1.0
	 */	
	private void reportOnMoved() {
		if ( movementConstraint == CONSTRAIN_CIRCLE )
			constrainCircle();
		else
			constrainBox();

		calcUserCoordinates();

		if (moveListener != null) {
			boolean rx = Math.abs(touchX - reportX) >= moveResolution;
			boolean ry = Math.abs(touchY - reportY) >= moveResolution;
			if (rx || ry) {
				this.reportX = touchX;
				this.reportY = touchY;

				moveListener.OnMoved(cartX, cartY);
			}
		}
	}
	//************************************************************************//

	//******************* ..:: calcUserCoordinates ::.. **********************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when we want to calculate the user coordinates.  	
	 * 
	 * @since           1.0
	 */	
	private void calcUserCoordinates() {
		//First convert to cartesian coordinates
		cartX = (int)(touchX / movementRadius * movementRange);
		cartY = (int)(touchY / movementRadius * movementRange);

		radial = Math.sqrt((cartX*cartX) + (cartY*cartY));
		angle = Math.atan2(cartY, cartX);

		//Invert Y axis if requested
		if ( !yAxisInverted )
			cartY  *= -1;

		if ( userCoordinateSystem == COORDINATE_CARTESIAN ) {
			userX = cartX;
			userY = cartY;
		}
		else if ( userCoordinateSystem == COORDINATE_DIFFERENTIAL ) {
			userX = cartY + cartX / 4;
			userY = cartY - cartX / 4;

			if ( userX < -movementRange )
				userX = (int)-movementRange;
			if ( userX > movementRange )
				userX = (int)movementRange;

			if ( userY < -movementRange )
				userY = (int)-movementRange;
			if ( userY > movementRange )
				userY = (int)movementRange;
		}

	}
	//************************************************************************//

	//********************** ..:: reportOnPressure ::.. **********************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when to report on joystick touch pressure.  	
	 * 
	 * @since           1.0
	 */	
	//Simple pressure click
	private void reportOnPressure() {
		if ( clickListener != null ) {
			if ( clicked && touchPressure < clickThreshold ) {
				clickListener.OnReleased();
				this.clicked = false;
				invalidate();
			}
			else if ( !clicked && touchPressure >= clickThreshold ) {
				clicked = true;
				clickListener.OnClicked();
				invalidate();
				performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
			}
		}
	}
	//************************************************************************//

	//******************* ..:: returnHandleToCenter ::.. *********************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when to return the joystick handle to the center.  	
	 * 
	 * @since           1.0
	 */	
	private void returnHandleToCenter() {
		if ( autoReturnToCenter ) {
			final int numberOfFrames = 5;
			final double intervalsX = (0 - touchX) / numberOfFrames;
			final double intervalsY = (0 - touchY) / numberOfFrames;

			for (int i = 0; i < numberOfFrames; i++) {
				final int j = i;
				postDelayed(new Runnable() {
					public void run() {
						touchX += intervalsX;
						touchY += intervalsY;

						reportOnMoved();
						invalidate();

						if (moveListener != null && j == numberOfFrames - 1) {
							moveListener.OnReturnedToCenter();
						}
					}
				}, i * 40);
			}

			if (moveListener != null) {
				moveListener.OnReleased();
			}
		}
	}
	//************************************************************************//

	//*********************** ..:: setTouchOffset ::.. ***********************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when we want to set the joystick touch offset.  	
	 * 
	 * @since           1.0
	 */	
	public void setTouchOffset(int x, int y) {
		offsetX = x;
		offsetY = y;
	}
	//************************************************************************//
}
