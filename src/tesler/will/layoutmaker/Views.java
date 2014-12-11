package tesler.will.layoutmaker;

import java.util.ArrayList;

import android.content.ClipData;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class Views implements OnDragListener, OnTouchListener {

	TextView textView;
	Button button;
	EditText editText;
	ImageView imageView;

	RelativeLayout selectionMask, dots, equilines;

	TextView selectionBorder;

	Handler h = new Handler();

	Runnable r;

	static View selectedView;

	ArrayList<ViewWrapper> viewList = new ArrayList<ViewWrapper>();

	int layerCounter = 1;

	int tvCounter = 0;

	int btCounter = 0;

	int etCounter = 0;

	int ivCounter = 0;

	@SuppressWarnings("deprecation")
	Views() {

		selectedView = Global.layers.list.get(Global.layers.active);

		selectedView.setOnDragListener(this);

		selectionMask = (RelativeLayout) Global.cont
				.findViewById(R.id.rl_selection);

		selectionMask.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Global.attr.populate(Global.layers.list
						.get(Global.layers.active));
				v.setVisibility(View.GONE);
				h.removeCallbacks(r);
			}
		});

		selectionBorder = new TextView(Global.cont);

		// selectionBorder.setOnTouchListener(this);

		selectionMask.addView(selectionBorder);

		equilines = (RelativeLayout) Global.cont
				.findViewById(R.id.rl_equilines);

		dots = new RelativeLayout(Global.cont);
		// dots.setVisibility(View.GONE);
		int i = 0;
		while (i < 8) {

			TextView dot = new TextView(Global.cont);

			Bitmap bmp = Bitmap.createBitmap(40, 40, Bitmap.Config.ARGB_8888);
			Canvas c = new Canvas(bmp);
			Paint p = new Paint();
			p.setAntiAlias(true);
			p.setAlpha(100);
			p.setColor(Color.BLACK);
			c.drawCircle(20, 20, (float) 8, p);

			dot.setBackgroundDrawable(new BitmapDrawable(Global.cont
					.getResources(), bmp));

			dot.setId(i);

			dot.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					dragDot(v);
					return false;
				}
			});

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					40, 40);

			switch (i) {
			case 0:
				params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				params.addRule(RelativeLayout.CENTER_VERTICAL);
				break;
			case 1:
				params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
				break;
			case 2:
				params.addRule(RelativeLayout.CENTER_HORIZONTAL);
				params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
				break;
			case 3:
				params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
				break;
			case 4:
				params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				params.addRule(RelativeLayout.CENTER_VERTICAL);
				break;
			case 5:
				params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
				break;
			case 6:
				params.addRule(RelativeLayout.CENTER_HORIZONTAL);
				params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
				break;
			case 7:
				params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
				break;
			}

			dot.setLayoutParams(params);

			dots.addView(dot);

			i++;
		}

		selectionMask.addView(dots);

		button = (Button) Global.sideMenus.sm.findViewById(R.id.bt_button);
		button.setOnTouchListener(this);

		textView = (TextView) Global.sideMenus.sm
				.findViewById(R.id.tv_textView);
		textView.setOnTouchListener(this);

		editText = (EditText) Global.sideMenus.sm
				.findViewById(R.id.et_editText);
		editText.setOnTouchListener(this);

		imageView = (ImageView) Global.sideMenus.sm
				.findViewById(R.id.iv_imageView);
		imageView.setOnTouchListener(this);
	}

	@Override
	public boolean onDrag(View view, DragEvent event) {
		View v = (View) event.getLocalState();

		if (v != null) {

			switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				Log.i("OnDrag", "STARTED");
				return true;

			case DragEvent.ACTION_DRAG_ENTERED:
				Log.i("OnDrag", "ENTERED");
				return false;

			case DragEvent.ACTION_DRAG_EXITED:
				Log.i("OnDrag", "EXITED");
				return false;

			case DragEvent.ACTION_DROP:
				Log.i("OnDrag", "DROP");

				if ((v.getId() >= 0 && v.getId() <= 7)) {

					int newHeight = selectedView.getHeight();
					int newWidth = selectedView.getWidth();
					if (Magnet.horizontal != null)
						newHeight += Magnet.horizontal - selectedView.getY();
					if (Magnet.vertical != null) {
						newWidth += Magnet.vertical - selectedView.getX();
					}

					int width_height_ratio = newWidth - newHeight;
					if (width_height_ratio > -5 && width_height_ratio < 5)
						newWidth = newHeight;

					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
							newWidth, newHeight);
					params.leftMargin = selectedView.getLeft();
					params.topMargin = selectedView.getTop();
					if (Magnet.leftMargin != null) {
						Global.I("Adjusting to left margin...");
						params.leftMargin = Magnet.leftMargin;
					}
					if (Magnet.topMargin != null) {
						params.topMargin = Magnet.topMargin;
					}

					selectionBorder.setLayoutParams(params);
					selectedView.setLayoutParams(params);

					layoutDots(params);

					selectionBorder
							.setBackgroundResource(R.anim.highlight_anim);
					animateSelectionBorder();

				} else {
					drop_view(v, event);
				}
				return true;

			case DragEvent.ACTION_DRAG_ENDED:
				Log.i("OnDrag", "ENDED");

				v.setVisibility(View.VISIBLE);

				Resizer.reset();

				Magnet.reset();

				for (ViewWrapper vw : viewList) {
					vw.removeLine(true, 0);
					vw.removeLine(true, 1);
					vw.removeLine(true, 2);
					vw.removeLine(false, 0);
					vw.removeLine(false, 1);
					vw.removeLine(false, 2);
				}

				equilines.removeAllViews();

				return true;

			default:
				// Picking up a "mid-drag" event

				// Where your finger is
				int curX = (int) event.getX();
				int curY = (int) event.getY();

				if (v.getId() >= 0 && v.getId() <= 7) {

					performResize(v, curX, curY);

					curX = selectedView.getLeft() + selectedView.getWidth() / 2;
					curY = selectedView.getTop() + selectedView.getHeight() / 2;

					v = selectedView;
				}

				curX -= v.getWidth() / 2;
				curY -= v.getHeight() / 2;

				equilines.removeAllViews();

				EdgeDetector.detectEdges(v, curX, curY);

				break;
			}
		}
		return false;
	}

	private void performResize(View v, int curX, int curY) {

		h.removeCallbacks(r);

		int width_height_ratio = selectedView.getWidth()
				- selectedView.getHeight();
		if (width_height_ratio > -5 && width_height_ratio < 5)
			selectionBorder.setBackgroundResource(R.drawable.border_purp);
		else
			selectionBorder.setBackgroundResource(R.drawable.border_blue);

		LayoutParams params = Resizer.stretch(v, selectedView, curX, curY);

		selectionBorder.setLayoutParams(params);

		selectedView.setLayoutParams(params);

		layoutDots(params);

	}

	private void drop_view(View v, DragEvent event) {
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				v.getWidth(), v.getHeight());

		if (Magnet.horizontal == null)
			params.topMargin = (int) (event.getY() - v.getHeight() / 2);
		else {
			Global.I("Magnet Horizontal was set");
			params.topMargin = Magnet.horizontal.intValue();
		}
		if (params.topMargin < 0)
			params.topMargin = 0;
		if (params.topMargin + v.getHeight() > Global.screenHeight)
			params.topMargin = Global.screenHeight - v.getHeight();

		if (Magnet.vertical == null)
			params.leftMargin = (int) (event.getX() - v.getWidth() / 2);
		else {
			Global.I("Magnet Vertical was set");
			params.leftMargin = Magnet.vertical.intValue();
		}

		if (Magnet.leftMargin != null) {
			params.leftMargin = Magnet.leftMargin;
		}
		if (Magnet.topMargin != null) {
			params.topMargin = Magnet.topMargin;
		}

		if (params.leftMargin < 0)
			params.leftMargin = 0;
		if (params.leftMargin + v.getWidth() > Global.screenWidth)
			params.leftMargin = Global.screenWidth - v.getWidth();

		v.setLayoutParams(params);

		Toast.makeText(
				Global.cont,
				Integer.toString(params.leftMargin) + ", "
						+ Integer.toString(params.topMargin),
				Toast.LENGTH_SHORT).show();
	}

	static class Magnet {

		public static Integer horizontal, vertical;
		public static Integer leftMargin, topMargin;

		public static void reset() {
			horizontal = null;
			vertical = null;
			leftMargin = null;
			topMargin = null;
		}
	}

	static class ViewWrapper {
		View v;
		int layer;
		Line[] hors = new Line[3];
		Line[] verts = new Line[3];

		ViewWrapper(View v, int layerNum) {
			this.v = v;
			this.layer = layerNum;
		}

		void addLine(Line l, boolean horizontal, int i) {
			if (horizontal) {
				if (hors[i] != null)
					removeLine(true, i);
				hors[i] = l;
				// Global.I("Added Horizontal Line");
				Global.layers.list.get(Global.layers.active).addView(l);
			} else {
				if (verts[i] != null)
					removeLine(false, i);
				verts[i] = l;
				// Global.I("Added Vertical Line");
				Global.layers.list.get(Global.layers.active).addView(l);
			}
		}

		void removeLine(boolean horizontal, int i) {
			if (horizontal && hors[i] != null) {
				// Global.I("Removed Horizontal Line");
				Global.layers.list.get(Global.layers.active)
						.removeView(hors[i]);
				hors[i] = null;
			} else if (!horizontal && verts[i] != null) {
				// Global.I("Removed Vertical Line");
				Global.layers.list.get(Global.layers.active).removeView(
						verts[i]);
				verts[i] = null;
			}
		}

		View getView() {
			return v;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			switch (v.getId()) {
			case R.id.bt_button:

				Button newButton = new Button(Global.cont);
				newButton.setText(R.string.button);
				newButton.setTextSize(30);
				newButton.setTag("bt_" + btCounter++);

				setup_and_display_view(newButton);

				viewList.add(new ViewWrapper(newButton, Global.layers.active));

				Global.layers.redraw(Global.canvas);

				return true;
			case R.id.tv_textView:

				TextView newTextView = new TextView(Global.cont);
				newTextView.setText("TextView");
				newTextView.setTextSize(30);
				newTextView.setGravity(Gravity.CENTER);
				newTextView.setPadding(5, 5, 5, 5);
				newTextView.setBackgroundResource(R.drawable.border_gray);
				newTextView.setTag("tv_" + tvCounter++);

				setup_and_display_view(newTextView);

				viewList.add(new ViewWrapper(newTextView, Global.layers.active));

				Global.layers.redraw(Global.canvas);

				return true;
			case R.id.et_editText:

				EditText newEditText = new EditText(Global.cont);
				newEditText.setText(R.string.edittext);
				newEditText.setTextSize(30);
				newEditText.setKeyListener(null);
				newEditText.setGravity(Gravity.CENTER);
				newEditText.setTag("et_" + etCounter++);

				setup_and_display_view(newEditText);

				viewList.add(new ViewWrapper(newEditText, Global.layers.active));

				Global.layers.redraw(Global.canvas);

				return true;
			case R.id.iv_imageView:
				ImageView newImageView = new ImageView(Global.cont);
				newImageView.setTag("iv_" + ivCounter++);
				newImageView.setBackgroundResource(R.drawable.imageview);

				setup_and_display_view(newImageView);

				viewList.add(new ViewWrapper(newImageView, Global.layers.active));

				Global.layers.redraw(Global.canvas);

				return true;
			}
		}

		return false;
	}

	private void setup_and_display_view(View v) {

		v.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				dragView(v);
				return false;
			}
		});

		v.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// This view is now selected
				selectedView = v;

				Global.attr.populate(selectedView);

				// Set the Border layout
				selectionBorder.setBackgroundResource(R.anim.highlight_anim);

				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
						v.getWidth(), v.getHeight());

				params.leftMargin = (int) v.getX();
				params.topMargin = (int) (v.getY() - 2);

				// Resize the border to fit around this view
				selectionBorder.setLayoutParams(params);

				layoutDots(params);

				// Display the selection border layer
				selectionMask.setVisibility(View.VISIBLE);

				animateSelectionBorder();
			}
		});

		// Add the view to the canvas
		Global.layers.list.get(Global.layers.active).addView(v);

		// Draw the View immediately
		Global.layers.list.get(Global.layers.active)
				.measure(
						MeasureSpec.makeMeasureSpec(
								Global.layers.list.get(Global.layers.active)
										.getLayoutParams().width,
								MeasureSpec.EXACTLY),
						MeasureSpec.makeMeasureSpec(
								Global.layers.list.get(Global.layers.active)
										.getLayoutParams().height,
								MeasureSpec.EXACTLY));
		Global.layers.list.get(Global.layers.active).layout(0, 0,
				Global.layers.list.get(Global.layers.active).getWidth(),
				Global.layers.list.get(Global.layers.active).getHeight());

		ClipData data = ClipData.newPlainText("", "");
		DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
		v.startDrag(data, shadowBuilder, v, 0);
		v.setVisibility(View.INVISIBLE);
		Global.sideMenus.sm.toggle();
	}

	protected void layoutDots(LayoutParams params) {
		LayoutParams p2 = new LayoutParams(params);
		p2.leftMargin -= 25;
		p2.topMargin -= 25;
		p2.width += 50;
		p2.height += 50;
		dots.setLayoutParams(p2);
	}

	void dragDot(View view) {
		ClipData data = ClipData.newPlainText("", "");
		DragShadowBuilder shadowBuilder = new View.DragShadowBuilder();
		view.startDrag(data, shadowBuilder, view, 0);
	}

	void dragView(View view) {
		ClipData data = ClipData.newPlainText("", "");
		DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
		view.startDrag(data, shadowBuilder, view, 0);
		view.setVisibility(View.INVISIBLE);
	}

	void animateSelectionBorder() {
		// Begin the selection animation
		final TransitionDrawable transition = (TransitionDrawable) selectionBorder
				.getBackground();
		transition.startTransition(1000);
		r = new Runnable() {
			@Override
			public void run() {
				transition.reverseTransition(1000);
				h.postDelayed(r, 1000);
			}
		};
		h.postDelayed(r, 1000);
	}
}
