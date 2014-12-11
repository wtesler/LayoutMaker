package tesler.will.layoutmaker;

import java.util.ArrayList;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.ColorPicker.OnColorChangedListener;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;

import android.R.attr;
import android.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Attributes {

	ArrayList<Attr> attrList = new ArrayList<Attr>();
	AttrListAdapter adapter = new AttrListAdapter(Global.cont, attrList);
	ListView lv;

	public Attributes() {

		adapter = new AttrListAdapter(Global.cont, attrList);

		lv = (ListView) Global.cont.getSlidingMenu().findViewById(R.id.lv_attr);

		lv.setAdapter(adapter);
	}

	void populate(View v) {
		attrList.clear();
		if (v instanceof RelativeLayout) {
			attrList.add(new Attr("background:", v, "background"));
		} else if (v instanceof Button) {
			attrList.add(new Attr("id:", v, "id"));
			attrList.add(new Attr("text:", v, "text"));
			attrList.add(new Attr("text color:", v, "text color"));
			attrList.add(new Attr("text size:", v, "text size"));
		} else if (v instanceof TextView) {
			attrList.add(new Attr("id:", v, "id"));
			attrList.add(new Attr("background:", v, "background"));
			attrList.add(new Attr("text:", v, "text"));
			attrList.add(new Attr("text color:", v, "text color"));
			attrList.add(new Attr("text size:", v, "text size"));
		}
		adapter.notifyDataSetChanged();
	}

	void createColorDialog(int startingColor, final String type) {
		AlertDialog.Builder builder = new AlertDialog.Builder(Global.cont);

		View view = Global.cont.getLayoutInflater().inflate(
				R.layout.dialog_color, null);

		builder.setView(view);
		final AlertDialog dialog = builder.create();

		final ColorPicker picker = (ColorPicker) view.findViewById(R.id.picker);
		picker.setColor(startingColor);
		SVBar svBar = (SVBar) view.findViewById(R.id.svbar);
		OpacityBar opacityBar = (OpacityBar) view.findViewById(R.id.opacitybar);
		SaturationBar saturationBar = (SaturationBar) view
				.findViewById(R.id.saturationbar);
		ValueBar valueBar = (ValueBar) view.findViewById(R.id.valuebar);

		picker.addSVBar(svBar);
		picker.addOpacityBar(opacityBar);
		picker.addSaturationBar(saturationBar);
		picker.addValueBar(valueBar);

		// To set the old selected color u can do it like this
		picker.setOldCenterColor(picker.getColor());
		// adds listener to the colorpicker which is implemented
		// in the activity
		picker.setOnColorChangedListener(new OnColorChangedListener() {
			@Override
			public void onColorChanged(int color) {
				// TODO Auto-generated method stub

			}
		});

		Button set = (Button) view.findViewById(R.id.bt_set);
		set.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (type.contentEquals("background")) {
					attrList.get(adapter.selectedPos).v
							.setBackgroundColor(picker.getColor());
				} else if (type.contentEquals("text color")) {
					if (attrList.get(adapter.selectedPos).v instanceof TextView) {
						((TextView) attrList.get(adapter.selectedPos).v)
								.setTextColor(picker.getColor());
					} else if (attrList.get(adapter.selectedPos).v instanceof Button) {
						((Button) attrList.get(adapter.selectedPos).v)
								.setTextColor(picker.getColor());
					}
				}
				TextView rowColor = (TextView) adapter.selectedRow
						.findViewById(R.id.tv_color);
				rowColor.setBackgroundColor(picker.getColor());
				dialog.cancel();
			}
		});

		Button cancel = (Button) view.findViewById(R.id.bt_cancel);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});

		dialog.show();

	}

	class Attr {

		String name;
		View v;
		String type;

		Attr(String name, View v, String type) {
			this.name = name;
			this.v = v;
			this.type = type;
		}

		void setType(String type) {
			this.type = type;
		}

		String getType() {
			return type;
		}

	}
}
