package tesler.will.layoutmaker;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenedListener;

public class SideMenus {

	public SlidingMenu sm;

	SideMenus() {

		sm = Global.cont.getSlidingMenu();

		sm.setMode(SlidingMenu.LEFT_RIGHT);

		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

		sm.setSecondaryMenu(R.layout.sliding_menu_right);

		sm.setSlidingEnabled(true);

		sm.setBehindScrollScale((float) 1);

		sm.setFadeDegree(0.7f);

		Global.cont.setBehindContentView(R.layout.sliding_menu_left);

		if (Global.screenWidth >= 600) {
			sm.setBehindOffset(1 * Global.screenWidth / 3);

		} else {
			sm.setBehindOffset(1 * Global.screenWidth / 3);
		}

		sm.setOnOpenedListener(new OnOpenedListener() {

			@Override
			public void onOpened() {
				// sm.setSlidingEnabled(true);
			}
		});

		sm.setOnClosedListener(new OnClosedListener() {

			@Override
			public void onClosed() {
				// if (!Global.map.auto_manual.isChecked())
				// sm.setSlidingEnabled(false);
			}
		});
	}

	public void toggleLeftMenu() {
		sm.toggle();
	}

	//
	public void toggleRightMenu() {
		if (sm.isSecondaryMenuShowing()) {
			sm.toggle();
		} else {
			sm.showSecondaryMenu();
		}
	}

}
