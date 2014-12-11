package tesler.will.layoutmaker;

import android.view.View;
import android.widget.RelativeLayout;

class Resizer {

	static Integer dotSpotX;
	static Integer dotSpotY;

	static RelativeLayout.LayoutParams stretch(View dot, View selectedView,
			Integer curX, Integer curY) {

		if (dotSpotX == null)
			dotSpotX = dot.getLeft() + selectedView.getLeft();
		if (dotSpotY == null)
			dotSpotY = dot.getTop() + selectedView.getTop();

		int deltaX = curX - dotSpotX;
		int deltaY = curY - dotSpotY;

		// Log.i("Drag",
		// "Was at " + Float.toString(dotSpotX) + ","
		// + Float.toString(dotSpotY) + "... Now at "
		// + Float.toString(curX) + "," + Float.toString(curY));

		dotSpotX = curX;
		dotSpotY = curY;

		int id = dot.getId();

		int newWidth = selectedView.getWidth();
		if (id <= 1 || id == 7)
			newWidth += deltaX;
		else if (id >= 3 && id <= 5)
			newWidth -= deltaX;

		int newHeight = selectedView.getHeight();
		if (id >= 1 && id <= 3)
			newHeight += deltaY;
		else if (id >= 5 && id <= 7)
			newHeight -= deltaY;

		// if (newWidth > 5 && newHeight > 5) {
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				newWidth, newHeight);
		params.leftMargin = selectedView.getLeft();
		params.topMargin = selectedView.getTop();

		if (id >= 3 && id <= 5)
			params.leftMargin += deltaX;

		if (id >= 5 && id <= 7)
			params.topMargin += deltaY;

		return params;
	}

	public static void reset() {
		dotSpotX = null;
		dotSpotY = null;
	}

	public static class Pair {
		Integer curX, curY;

		Pair(Integer x, Integer y) {
			curX = x;
			curY = y;
		}
	}
}