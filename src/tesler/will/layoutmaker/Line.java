package tesler.will.layoutmaker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class Line extends View {
	Paint paint = new Paint();
	float position;
	boolean isHorizontal;

	static int darkgreen = Global.cont.getResources().getColor(
			R.color.darkgreen);
	static int green = Color.GREEN;

	static int color = green;

	public Line(Context context, float position, boolean isHorizontal) {

		super(context);

		this.position = position;
		this.isHorizontal = isHorizontal;

		paint.setColor(color);
		paint.setStrokeWidth(9);
		paint.setAlpha(130);

	}

	@Override
	public void onDraw(Canvas canvas) {
		if (isHorizontal)
			canvas.drawLine(0, position, Global.screenWidth, position, paint);

		else
			canvas.drawLine(position, 0, position, Global.screenHeight, paint);
	}

	// Satisfy Android tools requirement
	Line(Context c) {
		super(c);
	}

}
