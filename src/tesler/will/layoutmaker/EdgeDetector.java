package tesler.will.layoutmaker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import tesler.will.layoutmaker.Views.Magnet;
import tesler.will.layoutmaker.Views.ViewWrapper;
import android.view.View;

public class EdgeDetector {

	static void detectEdges(View v, int curX, int curY) {
		// Horizontal Dragee Segments
		int[] hds = new int[3];
		hds[0] = curY;
		hds[1] = curY + v.getHeight() / 2;
		hds[2] = curY + v.getHeight();

		// Vertical Matchers
		int[] vds = new int[3];
		vds[0] = curX;
		vds[1] = curX + v.getWidth() / 2;
		vds[2] = curX + v.getWidth();

		ArrayList<Pair> hViews = new ArrayList<Pair>();
		hViews.add(new Pair(curX, v.getWidth(), curY + v.getHeight() / 2, true));

		ArrayList<Pair> vViews = new ArrayList<Pair>();
		vViews.add(new Pair(curY, v.getHeight(), curX + v.getWidth() / 2, true));

		boolean hMatch = false, vMatch = false;

		for (ViewWrapper vw : Global.views.viewList) {

			if (vw.layer == Global.layers.active) {

				View widget = vw.getView();

				if (widget != v) {

					Line.color = Line.green;

					int width = widget.getWidth();
					int height = widget.getHeight();
					int x = (int) widget.getX();
					int y = (int) widget.getY();

					boolean hMatchFound = false;
					for (int i = 0; i < 3; i++) {
						int result = checkAlignment(vw, i, height / 2,
								v.getHeight(), y, hds, true, false);

						if (result == -1) {
							vw.removeLine(true, i);
						} else {
							hMatch = true;
						}
						if (result == 1) {
							hMatchFound = true;
						}
					}
					if (hMatchFound) {
						hViews.add(new Pair(widget.getLeft(),
								widget.getWidth(), widget.getTop()
										+ widget.getHeight() / 2, false));
					}

					boolean vMatchFound = false;
					for (int i = 0; i < 3; i++) {
						int result = checkAlignment(vw, i, width / 2,
								v.getWidth(), x, vds, false, false);
						if (result == -1) {
							vw.removeLine(false, i);
						} else {
							vMatch = true;
						}
						if (result == 1) {
							vMatchFound = true;
						}
					}
					if (vMatchFound) {
						vViews.add(new Pair(widget.getTop(),
								widget.getHeight(), widget.getLeft()
										+ widget.getWidth() / 2, false));
					}

				} else {

					Line.color = Line.darkgreen;

					int result = checkAlignment(vw, 1, curY, 0,
							Global.screenHeight / 2, hds, true, true);
					if (result == -1) {
						vw.removeLine(true, 1);
					} else {
						hMatch = true;
					}

					result = checkAlignment(vw, 1, curX, 0,
							Global.screenWidth / 2, vds, false, true);
					if (result == -1) {
						vw.removeLine(false, 1);
					} else {
						vMatch = true;
					}
				}
			}
		}

		Magnet.leftMargin = null;
		checkEquidistance(hViews, true);

		Magnet.topMargin = null;
		checkEquidistance(vViews, false);

		if (!hMatch) {
			// Global.I("Setting Magnet Horizontal to null");
			Magnet.horizontal = null;
		}
		if (!vMatch) {
			// Global.I("Setting Magnet Vertical to null");
			Magnet.vertical = null;
		}
	}

	private static boolean checkEquidistance(ArrayList<Pair> views,
			boolean horizontal) {

		if (views.size() <= 2)
			return false;

		Collections.sort(views, getComparator());

		Pair a = views.get(0);

		int rememberedDiff = Integer.MAX_VALUE;

		for (int i = 1; i < views.size(); i++) {
			Pair b = views.get(i);
			int diff = b.compVal - (a.compVal + a.length);
			if (rememberedDiff != Integer.MAX_VALUE) {
				int diffDifference = Math.abs(rememberedDiff - diff);
				if (diffDifference > 6) {
					return false;
				}
			}
			rememberedDiff = diff;
			a = b;
		}

		int movingIndex = drawEquiLines(views, horizontal);

		adjustMagnetMargins(views, movingIndex, rememberedDiff, horizontal);

		return true;
	}

	private static int drawEquiLines(ArrayList<Pair> views, boolean horizontal) {
		int movingIndex = 0;
		for (int j = views.size() - 1; j > 0; j--) {
			if (horizontal) {
				EquiLine l = new EquiLine(Global.cont, views.get(j).compVal,
						views.get(j).pos, views.get(j - 1).compVal
								+ views.get(j - 1).length, views.get(j).pos);
				Global.views.equilines.addView(l);
			} else {
				EquiLine l = new EquiLine(Global.cont, views.get(j).pos,
						views.get(j).compVal, views.get(j).pos,
						views.get(j - 1).compVal + views.get(j - 1).length);
				Global.views.equilines.addView(l);
			}
			if (views.get(j).moving) {
				movingIndex = j;
			}
		}
		return movingIndex;
	}

	private static void adjustMagnetMargins(ArrayList<Pair> views,
			int movingIndex, int rememberedDiff, boolean horizontal) {
		if (horizontal) {
			if (movingIndex == 0) {
				Magnet.leftMargin = views.get(1).compVal - views.get(0).length
						- rememberedDiff;
			} else {
				Magnet.leftMargin = views.get(movingIndex - 1).compVal
						+ views.get(movingIndex - 1).length + rememberedDiff;
			}
		} else {
			if (movingIndex == 0) {
				Magnet.topMargin = views.get(1).compVal - views.get(0).length
						- rememberedDiff;
			} else {
				Magnet.topMargin = views.get(movingIndex - 1).compVal
						+ views.get(movingIndex - 1).length + rememberedDiff;
			}
		}
	}

	private static int checkAlignment(ViewWrapper vw, int i, int otherHalfSize,
			int selfSize, int bound, int[] sections, Boolean horizontal,
			Boolean centerLines) {

		int otherSection = i * otherHalfSize;

		if (!centerLines)
			bound += otherSection;

		for (int j = 2; j >= 0; j--) {

			if (sections[j] > bound - 7 && sections[j] < bound + 7) {

				int offset;
				if (centerLines) {
					offset = sections[j] - otherSection;
				} else {
					offset = j * selfSize / 2 - selfSize % 2;
				}

				if (horizontal) {

					vw.addLine(new Line(Global.cont, bound, true), true, i);

					Magnet.horizontal = bound - offset;
				} else {

					vw.addLine(new Line(Global.cont, bound, false), false, i);

					Magnet.vertical = bound - offset;
				}

				return j;
			}
		}
		return -1;
	}

	static Comparator<Pair> getComparator() {
		return new Comparator<Pair>() {
			@Override
			public int compare(Pair first, Pair second) {
				if (first.compVal < second.compVal) {
					return -1;
				} else if (first.compVal == second.compVal) {
					return 0;
				} else {
					return 1;
				}
			}
		};
	}

	static class Pair {
		int compVal;
		int length;
		int pos;
		boolean moving;

		Pair(int c, int l, int p, boolean m) {
			compVal = c;
			length = l;
			pos = p;
			moving = m;
		}
	}

}
