package com.example.components;

import com.example.socket_client.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewParent;
import android.widget.RemoteViews.RemoteView;
import android.os.Parcel;
import android.os.Parcelable;



/**
 * VerticalProgressBar is a class that implements vertical progress bar that we can add to
 * the application view.
 * 
 * @author      Shmulik Melamed
 * @author      Lital Motola
 * @version     v5.0
 * @since       1.0
 */
@RemoteView
public class VerticalProgressBar extends View {
	private static final int MAX_LEVEL = 10000;

	//.: VerticalProgressBar variables.
	int mMinWidth;
	int mMaxWidth;
	int mMinHeight;
	int mMaxHeight;

	private int mProgress;
	private int mSecondaryProgress;
	private int mMax;

	private Drawable mProgressDrawable;
	private Drawable mCurrentDrawable;
	Bitmap mSampleTile;
	private boolean mNoInvalidate;
	private RefreshProgressRunnable mRefreshProgressRunnable;
	private long mUiThreadId;

	private boolean mInDrawing;

	protected int mScrollX;
	protected int mScrollY;
	protected int mPaddingLeft;
	protected int mPaddingRight;
	protected int mPaddingTop;
	protected int mPaddingBottom;
	protected ViewParent mParent;
	//===========================

	//********************* ..:: VerticalProgressBar ::.. ********************//
	/**
	 * Create a new progress bar with range 0...100 and initial progress of 0.
	 * 
	 * @param context the application environment
	 * @since       1.0
	 */
	public VerticalProgressBar(Context context) {
		this(context, null);
	}
	//************************************************************************//    

	//********************* ..:: VerticalProgressBar ::.. ********************//
	/** 
	 * Added in API level 1
	 * 
	 * Create a new progress bar with range 0...100 and initial progress that
	 * the user decide.
	 * 
	 * @param context the application environment
	 * @param attrs the progress bar style.
	 * @since           1.0
	 */	    
	public VerticalProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.progressBarStyle);
	}
	//************************************************************************//    

	//********************* ..:: VerticalProgressBar ::.. ********************//
	/** 
	 * Added in API level 1
	 * 
	 * Create a new progress bar with range 0...100 and initial progress that
	 * the user decide, and the progress bar style.
	 * 
	 * @param context the application environment.
	 * @param attrs the progress bar style.
	 * @param defStyle the progress bar style.
	 * @since           1.0
	 */	    
	public VerticalProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mUiThreadId = Thread.currentThread().getId();
		initProgressBar();

		TypedArray a =
				context.obtainStyledAttributes(attrs, R.styleable.ProgressBar, defStyle, 0);

		mNoInvalidate = true;

		Drawable drawable = a.getDrawable(R.styleable.ProgressBar_android_progressDrawable);
		if (drawable != null) {
			drawable = tileify(drawable, false);
			// Calling this method can set mMaxHeight, make sure the corresponding
			// XML attribute for mMaxHeight is read after calling this method
			setProgressDrawable(drawable);
		}


		mMinWidth = a.getDimensionPixelSize(R.styleable.ProgressBar_android_minWidth, mMinWidth);
		mMaxWidth = a.getDimensionPixelSize(R.styleable.ProgressBar_android_maxWidth, mMaxWidth);
		mMinHeight = a.getDimensionPixelSize(R.styleable.ProgressBar_android_minHeight, mMinHeight);
		mMaxHeight = a.getDimensionPixelSize(R.styleable.ProgressBar_android_maxHeight, mMaxHeight);

		setMax(a.getInt(R.styleable.ProgressBar_android_max, mMax));

		setProgress(a.getInt(R.styleable.ProgressBar_android_progress, mProgress));

		setSecondaryProgress(
				a.getInt(R.styleable.ProgressBar_android_secondaryProgress, mSecondaryProgress));

		mNoInvalidate = false;

		a.recycle();
	}
	//************************************************************************//    

	//************************** ..:: tileify ::.. ***************************//
	/**
	 * Converts a drawable to a tiled version of itself. It will recursively
	 * traverse layer and state list drawables.
	 * 
	 * @param drawable the drawable object.
	 * @param clip true for shapeDrawable and false for creating a new ClipDrawable.
	 * @since       1.0
	 */
	private Drawable tileify(Drawable drawable, boolean clip) {

		if (drawable instanceof LayerDrawable) {
			LayerDrawable background = (LayerDrawable) drawable;
			final int N = background.getNumberOfLayers();
			Drawable[] outDrawables = new Drawable[N];

			for (int i = 0; i < N; i++) {
				int id = background.getId(i);
				outDrawables[i] = tileify(background.getDrawable(i),
						(id == android.R.id.progress || id == android.R.id.secondaryProgress));
			}

			LayerDrawable newBg = new LayerDrawable(outDrawables);

			for (int i = 0; i < N; i++) {
				newBg.setId(i, background.getId(i));
			}

			return newBg;

		} else if (drawable instanceof StateListDrawable) {
			StateListDrawable out = new StateListDrawable();
			return out;

		} else if (drawable instanceof BitmapDrawable) {
			final Bitmap tileBitmap = ((BitmapDrawable) drawable).getBitmap();
			if (mSampleTile == null) {
				mSampleTile = tileBitmap;
			}

			final ShapeDrawable shapeDrawable = new ShapeDrawable(getDrawableShape());
			return (clip) ? new ClipDrawable(shapeDrawable, Gravity.LEFT,
					ClipDrawable.HORIZONTAL) : shapeDrawable;
		}

		return drawable;
	}
	//************************************************************************//    

	//********************** ..:: getDrawableShape ::.. **********************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when we want to get the drawable round rect shape object
	 * 
	 * @since           1.0
	 */	    
	Shape getDrawableShape() {
		final float[] roundedCorners = new float[] { 5, 5, 5, 5, 5, 5, 5, 5 };
		return new RoundRectShape(roundedCorners, null, null);
	}
	//************************************************************************//    

	//********************** ..:: initProgressBar ::.. ***********************//
	/**
	 * <p>
	 * Initialize the progress bar's default values:
	 * </p>
	 * <ul>
	 * <li>progress = 0</li>
	 * <li>max = 100</li>
	 * </ul>
	 * @since           1.0
	 */
	private void initProgressBar() {
		mMax = 100;
		mProgress = 0;
		mSecondaryProgress = 0;
		mMinWidth = 24;
		mMaxWidth = 48;
		mMinHeight = 24;
		mMaxHeight = 48;
	}
	//************************************************************************//    

	//******************** ..:: getProgressDrawable ::.. *********************//
	/**
	 * <p>Get the drawable used to draw the progress bar in
	 * progress mode.</p>
	 *
	 * @return a {@link android.graphics.drawable.Drawable} instance
	 *
	 * @see #setProgressDrawable(android.graphics.drawable.Drawable)
	 * @since       1.0
	 */
	public Drawable getProgressDrawable() {
		return mProgressDrawable;
	}
	//************************************************************************//    

	//********************* ..:: setProgressDrawable ::.. ********************//
	/**
	 * <p>Define the drawable used to draw the progress bar in
	 * progress mode.</p>
	 *
	 * @param d the new drawable
	 *
	 * @see #getProgressDrawable()
	 * @since       1.0
	 */
	public void setProgressDrawable(Drawable d) {
		if (d != null) {
			d.setCallback(this);
			// Make sure the ProgressBar is always tall enough
			int drawableHeight = d.getMinimumHeight();
			if (mMaxHeight < drawableHeight) {
				mMaxHeight = drawableHeight;
				requestLayout();
			}
		}
		mProgressDrawable = d;
		mCurrentDrawable = d;
		postInvalidate();
	}
	//************************************************************************//    

	//********************* ..:: getCurrentDrawable ::.. *********************//
	/**
	 * @return The drawable currently used to draw the progress bar
	 * @since       1.0
	 */
	Drawable getCurrentDrawable() {
		return mCurrentDrawable;
	}
	//************************************************************************//    

	//********************* ..:: verifyDrawable ::.. *************************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when we want to verify the drawable object we use.
	 * 
	 * @param who the drawable object we want to verify.
	 * @return true if equal to the progress drawable and false if equal to himself.
	 * @since           1.0
	 */	    
	@Override
	protected boolean verifyDrawable(Drawable who) {
		return who == mProgressDrawable || super.verifyDrawable(who);
	}
	//************************************************************************//    

	//********************** ..:: postInvalidate ::.. ************************//
	/**
	 * Added in API level 1
	 * Cause an invalidate to happen on a subsequent cycle through the event loop. Use this to invalidate the View from a non-UI thread.
	 *
	 * This method can be invoked from outside of the UI thread only when this View is attached to a window.
	 *
	 * @see #invalidate()
	 * @see #postInvalidateDelayed(long) 
	 * @since       1.0
	 */
	@Override
	public void postInvalidate() {
		if (!mNoInvalidate) {
			super.postInvalidate();
		}
	}
	//************************************************************************//    

	//****************** ..:: RefreshProgressRunnable ::.. *******************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when we want to refersh the progress bar view.
	 * 
	 * @since           1.0
	 */	    
	private class RefreshProgressRunnable implements Runnable {

		private int mId;
		private int mProgress;
		private boolean mFromUser;

		RefreshProgressRunnable(int id, int progress, boolean fromUser) {
			mId = id;
			mProgress = progress;
			mFromUser = fromUser;
		}

		public void run() {
			doRefreshProgress(mId, mProgress, mFromUser);
			// Put ourselves back in the cache when we are done
			mRefreshProgressRunnable = this;
		}

		public void setup(int id, int progress, boolean fromUser) {
			mId = id;
			mProgress = progress;
			mFromUser = fromUser;
		}

	}
	//************************************************************************//    

	//********************* ..:: doRefreshProgress ::.. **********************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when we want to do a refersh the progress bar view.
	 * 
	 * @param id progress bar id
	 * @param progrss the progress value
	 * @param fromUser true is the user do refresh or the program.
	 * @since           1.0
	 */	    
	private synchronized void doRefreshProgress(int id, int progress, boolean fromUser) {
		float scale = mMax > 0 ? (float) progress / (float) mMax : 0;
		final Drawable d = mCurrentDrawable;
		if (d != null) {
			Drawable progressDrawable = null;

			if (d instanceof LayerDrawable) {
				progressDrawable = ((LayerDrawable) d).findDrawableByLayerId(id);
			}

			final int level = (int) (scale * MAX_LEVEL);
			(progressDrawable != null ? progressDrawable : d).setLevel(level);
		} else {
			invalidate();
		}

		if (id == android.R.id.progress) {
			onProgressRefresh(scale, fromUser);
		}
	}
	//************************************************************************//    

	//********************* ..:: onProgressRefresh ::.. **********************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when the progress bar refreshed by the program.
	 * 
	 * @param scale progress value.
	 * @param fromUser true is the user do refresh or the program.
	 * @since           1.0
	 */	    
	void onProgressRefresh(float scale, boolean fromUser) {
	}

	//********************** ..:: refreshProgress ::.. ***********************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when we want to refersh the progress bar view.
	 * 
	 * @param id progress bar id
	 * @param progrss the progress value
	 * @param fromUser true is the user do refresh or the program.
	 * @since           1.0
	 */	    
	private synchronized void refreshProgress(int id, int progress, boolean fromUser) {
		if (mUiThreadId == Thread.currentThread().getId()) {
			doRefreshProgress(id, progress, fromUser);
		} else {
			RefreshProgressRunnable r;
			if (mRefreshProgressRunnable != null) {
				// Use cached RefreshProgressRunnable if available
				r = mRefreshProgressRunnable;
				// Uncache it
				mRefreshProgressRunnable = null;
				r.setup(id, progress, fromUser);
			} else {
				// Make a new one
				r = new RefreshProgressRunnable(id, progress, fromUser);
			}
			post(r);
		}
	}
	//************************************************************************//    

	//************************ ..:: setProgress ::.. *************************//
	/**
	 * <p>Set the current progress to the specified value.</p>
	 *
	 * @param progress the new progress, between 0 and {@link #getMax()}
	 *
	 * @see #getProgress()
	 * @see #incrementProgressBy(int)
	 * @since       1.0
	 */
	public synchronized void setProgress(int progress) {
		setProgress(progress, false);
	}
	//************************************************************************//    

	//************************ ..:: setProgress ::.. *************************//
	/** 
	 * Added in API level 1
	 * 
	 * Called when we want to set the progress value.
	 * 
	 * @param progrss the progress value
	 * @param fromUser true is the user do refresh or the program.
	 * @since           1.0
	 */	    
	synchronized void setProgress(int progress, boolean fromUser) {
		if (progress < 0) {
			progress = 0;
		}

		if (progress > mMax) {
			progress = mMax;
		}

		if (progress != mProgress) {
			mProgress = progress;
			refreshProgress(android.R.id.progress, mProgress, fromUser);
		}
	}
	//************************************************************************//    

	//******************** ..:: setSecondaryProgress ::.. ********************//
	/**
	 * <p>
	 * Set the current secondary progress to the specified value.
	 * </p>
	 *
	 * @param secondaryProgress the new secondary progress, between 0 and {@link #getMax()}
	 * @see #getSecondaryProgress()
	 * @see #incrementSecondaryProgressBy(int)
	 * @since       1.0
	 */
	public synchronized void setSecondaryProgress(int secondaryProgress) {
		if (secondaryProgress < 0) {
			secondaryProgress = 0;
		}

		if (secondaryProgress > mMax) {
			secondaryProgress = mMax;
		}

		if (secondaryProgress != mSecondaryProgress) {
			mSecondaryProgress = secondaryProgress;
			refreshProgress(android.R.id.secondaryProgress, mSecondaryProgress, false);
		}
	}
	//************************************************************************//    

	//************************ ..:: getProgress ::.. *************************//
	/**
	 * <p>Get the progress bar's current level of progress.</p>
	 *
	 * @return the current progress, between 0 and {@link #getMax()}
	 *
	 * @see #setProgress(int)
	 * @see #setMax(int)
	 * @see #getMax()
	 * @since       1.0
	 */
	@ViewDebug.ExportedProperty
	public synchronized int getProgress() {
		return mProgress;
	}
	//************************************************************************//    

	//******************** ..:: getSecondaryProgress ::.. ********************//
	/**
	 * <p>Get the progress bar's current level of secondary progress.</p>
	 *
	 * @return the current secondary progress, between 0 and {@link #getMax()}
	 *
	 * @see #setSecondaryProgress(int)
	 * @see #setMax(int)
	 * @see #getMax()
	 * @since       1.0
	 */
	@ViewDebug.ExportedProperty
	public synchronized int getSecondaryProgress() {
		return mSecondaryProgress;
	}
	//************************************************************************//    

	//*************************** ..:: getMax ::.. ***************************//
	/**
	 * <p>Return the upper limit of this progress bar's range.</p>
	 *
	 * @return a positive integer
	 *
	 * @see #setMax(int)
	 * @see #getProgress()
	 * @see #getSecondaryProgress()
	 * @since       1.0
	 */
	@ViewDebug.ExportedProperty
	public synchronized int getMax() {
		return mMax;
	}
	//************************************************************************//    

	//*************************** ..:: setMax ::.. ***************************//
	/**
	 * <p>Set the range of the progress bar to 0...<tt>max</tt>.</p>
	 *
	 * @param max the upper range of this progress bar
	 *
	 * @see #getMax()
	 * @see #setProgress(int)
	 * @see #setSecondaryProgress(int)
	 * @since       1.0
	 */
	public synchronized void setMax(int max) {
		if (max < 0) {
			max = 0;
		}
		if (max != mMax) {
			mMax = max;
			postInvalidate();

			if (mProgress > max) {
				mProgress = max;
				refreshProgress(android.R.id.progress, mProgress, false);
			}
		}
	}
	//************************************************************************//    

	//********************* ..:: incrementProgressBy ::.. ********************//
	/**
	 * <p>Increase the progress bar's progress by the specified amount.</p>
	 *
	 * @param diff the amount by which the progress must be increased
	 *
	 * @see #setProgress(int)
	 * @since       1.0
	 */
	public synchronized final void incrementProgressBy(int diff) {
		setProgress(mProgress + diff);
	}
	//************************************************************************//    

	//*************** ..:: incrementSecondaryProgressBy ::.. *****************//
	/**
	 * <p>Increase the progress bar's secondary progress by the specified amount.</p>
	 *
	 * @param diff the amount by which the secondary progress must be increased
	 *
	 * @see #setSecondaryProgress(int)
	 * @since       1.0
	 */
	public synchronized final void incrementSecondaryProgressBy(int diff) {
		setSecondaryProgress(mSecondaryProgress + diff);
	}
	//************************************************************************//    

	//********************** ..:: setVisibility ::.. *************************//
	/**
	 * Added in API level 1
	 * 
	 * Set the enabled state of this view.
	 *  
	 * @param v  One of VISIBLE, INVISIBLE, or GONE. 
	 * @since       1.0
	 */
	@Override
	public void setVisibility(int v) {
		if (getVisibility() != v) {
			super.setVisibility(v);
		}
	}
	//************************************************************************//    

	//******************** ..:: invalidateDrawable ::.. **********************//
	/**
	 * Added in API level 1
	 * Invalidates the specified Drawable.
	 *
	 * @param dr  the drawable to invalidate  
	 * @since       1.0
	 */
	@Override
	public void invalidateDrawable(Drawable dr) {
		if (!mInDrawing) {
			if (verifyDrawable(dr)) {
				final Rect dirty = dr.getBounds();
				final int scrollX = mScrollX + mPaddingLeft;
				final int scrollY = mScrollY + mPaddingTop;

				invalidate(dirty.left + scrollX, dirty.top + scrollY,
						dirty.right + scrollX, dirty.bottom + scrollY);
			} else {
				super.invalidateDrawable(dr);
			}
		}
	}
	//************************************************************************//    

	//********************** ..:: onSizeChanged ::.. *************************//
	/**
	 * Added in API level 1
	 * This is called during layout when the size of this view has changed. If you were just added to the view hierarchy, you're called with the old values of 0.
	 *
	 * @param w  Current width of this view. 
	 * @param h  Current height of this view. 
	 * @param oldw  Old width of this view. 
	 * @param oldh  Old height of this view.  
	 * @since       1.0
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// onDraw will translate the canvas so we draw starting at 0,0
		int right = w - mPaddingRight - mPaddingLeft;
		int bottom = h - mPaddingBottom - mPaddingTop;

		if (mProgressDrawable != null) {
			mProgressDrawable.setBounds(0, 0, right, bottom);
		}
	}
	//************************************************************************//    

	//**************************** ..:: onDraw ::.. **************************//
	/**
	 * Added in API level 1
	 * Implement this to do your drawing.
	 *
	 * @param canvas  the canvas on which the background will be drawn  
	 * @since       1.0
	 */
	@Override
	protected synchronized void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Drawable d = mCurrentDrawable;
		if (d != null) {
			// Translate canvas so a indeterminate circular progress bar with padding
			// rotates properly in its animation
			canvas.save();
			canvas.translate(mPaddingLeft, mPaddingTop);
			d.draw(canvas);
			canvas.restore();
		}
	}
	//************************************************************************//    

	//************************** ..:: onMeasure ::.. *************************//
	/**
	 * Added in API level 1
	 *
	 * Measure the view and its content to determine the measured width and the measured height. This method is invoked by measure(int, int) and should be overriden by subclasses to provide accurate and efficient measurement of their contents. 
	 * CONTRACT: When overriding this method, you must call setMeasuredDimension(int, int) to store the measured width and height of this view. Failure to do so will trigger an IllegalStateException, thrown by measure(int, int). Calling the superclass' onMeasure(int, int) is a valid use. 
	 * The base class implementation of measure defaults to the background size, unless a larger size is allowed by the MeasureSpec. Subclasses should override onMeasure(int, int) to provide better measurements of their content. 
	 * If this method is overridden, it is the subclass's responsibility to make sure the measured height and width are at least the view's minimum height and width (getSuggestedMinimumHeight() and getSuggestedMinimumWidth()). 
	 *
	 *
	 * @param widthMeasureSpec  horizontal space requirements as imposed by the parent. The requirements are encoded with View.MeasureSpec. 
	 * @param heightMeasureSpec  vertical space requirements as imposed by the parent. The requirements are encoded with View.MeasureSpec. 
	 *
	 * @see #getMeasuredWidth()
	 * @see #getMeasuredHeight()
	 * @see #setMeasuredDimension(int, int)
	 * @see #getSuggestedMinimumHeight()
	 * @see #getSuggestedMinimumWidth()
	 * @see #getMode(int)
	 * @see #getSize(int) 
	 * @since       1.0
	 */
	@Override
	protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Drawable d = mCurrentDrawable;

		int dw = 0;
		int dh = 0;
		if (d != null) {
			dw = Math.max(mMinWidth, Math.min(mMaxWidth, d.getIntrinsicWidth()));
			dh = Math.max(mMinHeight, Math.min(mMaxHeight, d.getIntrinsicHeight()));
		}
		dw += mPaddingLeft + mPaddingRight;
		dh += mPaddingTop + mPaddingBottom;

		setMeasuredDimension(resolveSize(dw, widthMeasureSpec),
				resolveSize(dh, heightMeasureSpec));
	}
	//************************************************************************//    

	//********************* ..:: drawableStateChanged ::.. *******************//
	/**
	 * Added in API level 1
	 * This function is called whenever the state of the view changes in such a way that it impacts the state of drawables being shown. 
	 * Be sure to call through to the superclass when overriding this function.
	 *
	 * @see #setState(int[]) 
	 * @since       1.0
	 */
	@Override
	protected void drawableStateChanged() {
		super.drawableStateChanged();

		int[] state = getDrawableState();

		if (mProgressDrawable != null && mProgressDrawable.isStateful()) {
			mProgressDrawable.setState(state);
		}
	}
	//************************************************************************//    


	/**
	 * SavedState is a class that saves the progress bar state.
	 * 
	 * @author      Shmulik Melamed
	 * @author      Lital Motola
	 * @version     v5.0
	 * @since       1.0
	 */
	static class SavedState extends BaseSavedState {
		int progress;
		int secondaryProgress;

		//************************* ..:: SavedState ::.. *************************//
		/**
		 * Constructor called from {@link ProgressBar#onSaveInstanceState()}
		 * 
		 * @since       1.0
		 */
		SavedState(Parcelable superState) {
			super(superState);
		}
		//************************************************************************//    

		//************************* ..:: SavedState ::.. *************************//
		/**
		 * Constructor called from {@link #CREATOR}
		 * 
		 * @since       1.0
		 */
		private SavedState(Parcel in) {
			super(in);
			progress = in.readInt();
			secondaryProgress = in.readInt();
		}
		//************************************************************************//    

		//*********************** ..:: writeToParcel ::.. ************************//
		/** 
		 * Added in API level 1
		 * Flatten this object in to a Parcel.
		 *
		 * @param dest  The Parcel in which the object should be written. 
		 * @param flags  Additional flags about how the object should be written. May be 0 or PARCELABLE_WRITE_RETURN_VALUE.  
		 * @since       1.0
		 */	    
		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeInt(progress);
			out.writeInt(secondaryProgress);
		}
		//************************************************************************//    

		//********************* ..:: Parcelable.Creator ::.. *********************//
		/** 
		 * Added in API level 1
		 * 
		 * Called when we want to return the save state of the progress bar.
		 * 
		 * @since           1.0
		 */	    
		public static final Parcelable.Creator<SavedState> CREATOR
		= new Parcelable.Creator<SavedState>() {
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
		//************************************************************************//    
	}

	//******************** ..:: onSaveInstanceState ::.. *********************//
	/**
	 * Added in API level 1
	 * Hook allowing a view to generate a representation of its internal state that can later be used to create a new instance with that same state. This state should only contain information that is not persistent or can not be reconstructed later. For example, you will never store your current position on screen because that will be computed again when a new instance of the view is placed in its view hierarchy. 
	 * Some examples of things you may store here: the current cursor position in a text view (but usually not the text itself since that is stored in a content provider or other persistent storage), the currently selected item in a list view.
	 *
	 * @return Returns a Parcelable object containing the view's current dynamic state, or null if there is nothing interesting to save. The default implementation returns null.
	 * 
	 * @see #onRestoreInstanceState(android.os.Parcelable)
	 * @see #saveHierarchyState(android.util.SparseArray)
	 * @see #dispatchSaveInstanceState(android.util.SparseArray)
	 * @see #setSaveEnabled(boolean)  
	 * @since       1.0
	 */	    
	@Override
	public Parcelable onSaveInstanceState() {
		// Force our ancestor class to save its state
		Parcelable superState = super.onSaveInstanceState();
		SavedState ss = new SavedState(superState);

		ss.progress = mProgress;
		ss.secondaryProgress = mSecondaryProgress;

		return ss;
	}
	//************************************************************************//    

	//****************** ..:: onRestoreInstanceState ::.. ********************//
	/** 
	 * Added in API level 1
	 * Hook allowing a view to re-apply a representation of its internal state that had previously been generated by onSaveInstanceState(). This function will never be called with a null state.
	 *
	 * @param state  The frozen state that had previously been returned by onSaveInstanceState(). 
	 *
	 * @see #onSaveInstanceState()
	 * @see #restoreHierarchyState(android.util.SparseArray)
	 * @see #dispatchRestoreInstanceState(android.util.SparseArray) 
	 * @since       1.0
	 */	    
	@Override
	public void onRestoreInstanceState(Parcelable state) {
		SavedState ss = (SavedState) state;
		super.onRestoreInstanceState(ss.getSuperState());

		setProgress(ss.progress);
		setSecondaryProgress(ss.secondaryProgress);
	}
	//************************************************************************//    
}
