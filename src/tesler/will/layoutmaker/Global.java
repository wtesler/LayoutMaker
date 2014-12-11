package tesler.will.layoutmaker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Animation.AnimationListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class Global {

	public static SlidingFragmentActivity cont;

	public static SideMenus sideMenus;

	public static Attributes attr;

	// public static Corners corners;

	public static Views views;

	public static Layers layers;

	public static RelativeLayout canvas;

	// WARNING: THIS MUST BE CHANGED IF YOU CHANGE THE PACKAGE NAME OF THE APP
	public final static String BROADCAST_STATUS = "tesler.will.layoutmaker.STATUS";
	public final static String BROADCAST_ACTION = "tesler.will.layoutmaker.BROADCAST";

	public static Boolean hasExited = false;

	// Device Configs
	public static Integer screenWidth;
	public static Integer screenHeight;
	public static Boolean inForeground = true;
	public static Integer screenDensity;

	// Debug
	private static Boolean Debug = true;

	Global(SlidingFragmentActivity cont) {

		// Be careful about the ordering of initializations here!

		Global.cont = cont;

		getDeviceConfig();

		canvas = (RelativeLayout) cont.findViewById(R.id.rl_canvas);

		getPreferences(cont);

		sideMenus = new SideMenus();

		// corners = new Corners();

		attr = new Attributes();

		layers = new Layers();

		views = new Views();
	}

	public static void getPreferences(SlidingFragmentActivity cont) {

		// preferences = cont.getSharedPreferences("SETTINGS",
		// Context.MODE_PRIVATE);
		//
		// self.id = ObjectId.massageToObjectId(preferences.getString(Keys.ID,
		// null));
		//
		// self.member.color = preferences.getString(Keys.COLOR, "orange");
		// self.member.username = preferences.getString(Keys.NAME, null);
		// self.email = preferences.getString(Keys.EMAIL, null);
		//
		// self.member.profilePic = preferences.getString(Keys.PIC, "D0000");
		//
		// if (self.id != null) {
		// users.loadUsersFromPreferences();
		// }
		//
		// try {
		// Double latitude = Double.parseDouble(preferences.getString(
		// "lastLat", null));
		// Double longitude = Double.parseDouble(preferences.getString(
		// "lastLong", null));
		//
		// map.initialLatLng = new LatLng(latitude, longitude);
		//
		// map.startWandering();
		//
		// } catch (Exception e) {
		// W("Last LatLong was null");
		// }
		//
		// I("Preferences restored.");
	}

	public static void savePreferences() {

		// SharedPreferences.Editor e = preferences.edit();
		//
		// try {
		// e.putString(Keys.ID, self.id.toString());
		// } catch (NullPointerException npe) {
		// }
		// try {
		// e.putString(Keys.COLOR, self.member.color);
		// } catch (NullPointerException npe) {
		// }
		// try {
		// e.putString(Keys.NAME, self.member.username);
		// } catch (NullPointerException npe) {
		// }
		// try {
		// e.putString(Keys.EMAIL, self.email);
		// } catch (NullPointerException npe) {
		// }
		// try {
		// e.putString("lastLat", self.member.latitude.toString());
		// } catch (NullPointerException npe) {
		// try {
		// e.putString("lastLat",
		// Double.toString(map.initialLatLng.latitude));
		// } catch (Exception exp) {
		// e.putString("lastLat", "");
		// }
		// }
		// try {
		// e.putString("lastLong", self.member.longitude.toString());
		// } catch (NullPointerException npe) {
		// try {
		// e.putString("lastLong",
		// Double.toString(map.initialLatLng.longitude));
		// } catch (Exception exp) {
		// e.putString("lastLong", "");
		// }
		// }
		// try {
		// e.putString(Keys.PIC, self.member.profilePic);
		// } catch (NullPointerException npe) {
		// e.putString(Keys.PIC, null);
		// }
		//
		// e.commit();

		// Log.i(Global.LOG, "Preferences saved.");
	}

	public static void clearPreferences() {

		// SharedPreferences.Editor e = preferences.edit();
		//
		// e.putString(Keys.ID, null);
		//
		// e.putString(Keys.COLOR, "orange");
		//
		// e.putString(Keys.NAME, null);
		//
		// e.putString(Keys.PASS, null);
		//
		// e.putString(Keys.EMAIL, null);
		//
		// e.putString(Keys.MESSAGES, null);
		//
		// e.putString("gang", null);
		//
		// e.putString(Keys.USERS, null);
		//
		// e.putString(Keys.PIC, null);
		//
		// e.putString(Keys.TASKS, null);
		//
		// e.putString(Keys.GCMID, null);
		//
		// e.commit();

	}

	public static void fadeIn(final View view) {

		view.setVisibility(View.VISIBLE);

		AnimationSet anim = new AnimationSet(true);

		anim.setDuration(800);

		AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);

		anim.addAnimation(aa);
		anim.setFillAfter(true);

		view.startAnimation(anim);
	}

	public static void fadeOut(final View view) {

		AnimationSet anim = new AnimationSet(true);
		anim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				view.setVisibility(View.GONE);

			}
		});

		AlphaAnimation aa = new AlphaAnimation(1f, 0f);

		aa.setDuration(600);

		anim.addAnimation(aa);
		anim.setFillAfter(false);

		view.startAnimation(anim);
	}

	public static String getTime() {

		try {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa",
					Locale.getDefault());

			String time = sdf.format(cal.getTime());
			if (time.length() > 0 && time.indexOf(0) == '0') {
				time = time.substring(1, time.length() - 1);
			}

			return time;

		} catch (Exception e) {
			W("Couldn't display time. Maybe the user is in another country");
			return "";
		}
	}

	public static double euclidian_length(int x1, int y1, int x2, int y2) {

		return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));

	}

	// These Surpressions are handled with conditionals
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void getDeviceConfig() {

		if (android.os.Build.VERSION.SDK_INT >= 13) {
			Point p = new Point();
			Global.cont.getWindowManager().getDefaultDisplay().getSize(p);
			screenWidth = p.x;
			screenHeight = p.y;
		} else {
			Display display = Global.cont.getWindowManager()
					.getDefaultDisplay();
			screenWidth = display.getWidth();
			screenHeight = display.getHeight();
		}

		screenDensity = Global.cont.getResources().getDisplayMetrics().densityDpi;

		I("Screen Dimensions are " + screenWidth.toString() + " "
				+ screenHeight.toString() + " with a Density of "
				+ screenDensity.toString());
	}

	public static void hideSoftKeyboard() {

		try {
			InputMethodManager inputMethodManager = (InputMethodManager) Global.cont
					.getSystemService(Activity.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(Global.cont
					.getCurrentFocus().getWindowToken(), 0);
		} catch (NullPointerException npe) {
			I("Could not hide the soft keyboard");
		}
	}

	public static void I(String text) {

		String tag = "Default";

		try {
			StackTraceElement[] stackTraceElements = Thread.currentThread()
					.getStackTrace();

			tag = stackTraceElements[stackTraceElements.length - 1]
					.getClassName();

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (Debug) {
			Log.i(tag, text);
		}
	}

	public static void W(String text) {

		String tag = "Default";

		try {
			StackTraceElement[] stackTraceElements = Thread.currentThread()
					.getStackTrace();

			tag = stackTraceElements[stackTraceElements.length - 1]
					.getClassName();

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (Debug) {
			Log.w(tag, text);
		}
	}
}