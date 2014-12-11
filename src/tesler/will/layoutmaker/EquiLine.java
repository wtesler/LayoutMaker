package tesler.will.layoutmaker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class EquiLine extends View {
	Paint paint = new Paint();
	float startX, startY, endX, endY;
	boolean isHorizontal;

	static int darkgreen = Global.cont.getResources().getColor(
			R.color.darkgreen);
	static int green = Color.BLUE;

	static int color = green;

	public EquiLine(Context context, float startX, float startY, float endX,
			float endY) {

		super(context);

		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;

		paint.setColor(color);
		paint.setStrokeWidth(18);
		paint.setAlpha(130);
	}

	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawLine(startX, startY, endX, endY, paint);
	}

	// Satisfy Android tools requirement
	EquiLine(Context c) {
		super(c);
	}

}
