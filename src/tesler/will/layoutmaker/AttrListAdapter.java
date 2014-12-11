package tesler.will.layoutmaker;

import java.util.ArrayList;

import tesler.will.layoutmaker.Attributes.Attr;
import tesler.will.layoutmaker.Layers.HierObj;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class AttrListAdapter extends BaseAdapter {

	Handler h = new Handler();
	Runnable r;
	private ArrayList<Attr> items;
	Context cont;
	int selectedPos;
	LinearLayout selectedRow;

	public AttrListAdapter(Context context, ArrayList<Attr> items) {

		this.items = items;

		cont = context;

	}

	public int getCount() {
		return items.size();
	}

	public Object getItem(int arg0) {
		return null;
	}

	public long getItemId(int arg0) {
		return 0;
	}

	public View getView(final int position, View v, ViewGroup vg) {

		final Attr obj = items.get(position);

		final String type = obj.getType();

		if (type.contentEquals("background")) {

			// Inflate the Row in the ListView
			LayoutInflater vi = (LayoutInflater) cont
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.row_attr_color, vg, false);
			final TextView color = (TextView) v.findViewById(R.id.tv_color);
			try {
				ColorDrawable cd = (ColorDrawable) obj.v.getBackground();
				color.setBackgroundColor(cd.getColor());
			} catch (Exception e) {
				color.setBackgroundColor(Color.WHITE);
			}
			color.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					selectedPos = position;
					selectedRow = (LinearLayout) color.getParent();
					ColorDrawable cd = (ColorDrawable) v.getBackground();
					Global.attr.createColorDialog(cd.getColor(), type);
				}
			});
		} else if (type.contentEquals("text")) {

			// Inflate the Row in the ListView
			LayoutInflater vi = (LayoutInflater) cont
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.row_attr_text, vg, false);

			final EditText text = (EditText) v.findViewById(R.id.et_text);
			if (obj.v instanceof TextView) {
				text.setText(((TextView) (obj.v)).getText());
			} else if (obj.v instanceof Button) {
				text.setText(((Button) (obj.v)).getText());
			}
			text.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					// unused
				}

				@Override
				public void afterTextChanged(Editable s) {
					if (obj.v instanceof TextView)
						((TextView) obj.v).setText(s);
					if (obj.v instanceof Button)
						((Button) obj.v).setText(s);
				}
			});

			text.setOnEditorActionListener(getActionListener());

		} else if (type.contentEquals("id")) {

			// Inflate the Row in the ListView
			LayoutInflater vi = (LayoutInflater) cont
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.row_attr_text, vg, false);

			final EditText text = (EditText) v.findViewById(R.id.et_text);
			text.setText((CharSequence) obj.v.getTag());
			text.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					// unused
				}

				@Override
				public void afterTextChanged(Editable s) {
					obj.v.setTag(s.toString());
					for (HierObj ho : Global.layers.hierList) {
						if (ho.v.getTag().toString()
								.contentEquals(obj.v.getTag().toString())) {
							ho.v.setTag(s.toString());
							break;
						}
					}
					Global.layers.adapter.notifyDataSetChanged();
				}
			});

			text.setOnEditorActionListener(getActionListener());
		} else if (type.contentEquals("text color")) {

			// Inflate the Row in the ListView
			LayoutInflater vi = (LayoutInflater) cont
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.row_attr_color, vg, false);
			final TextView color = (TextView) v.findViewById(R.id.tv_color);
			if (obj.v instanceof TextView)
				color.setBackgroundColor(((TextView) obj.v)
						.getCurrentTextColor());
			if (obj.v instanceof Button)
				color.setBackgroundColor(((Button) obj.v).getCurrentTextColor());

			color.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					selectedPos = position;
					selectedRow = (LinearLayout) color.getParent();
					ColorDrawable cd = (ColorDrawable) v.getBackground();
					Global.attr.createColorDialog(cd.getColor(), type);
				}
			});
		} else if (type.contentEquals("text size")) {

			// Inflate the Row in the ListView
			LayoutInflater vi = (LayoutInflater) cont
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.row_attr_text, vg, false);

			final EditText text = (EditText) v.findViewById(R.id.et_text);
			text.setInputType(InputType.TYPE_CLASS_NUMBER);
			if (obj.v instanceof TextView)
				text.setText((Float.toString(((TextView) obj.v).getTextSize())));
			if (obj.v instanceof Button)
				text.setText((Float.toString(((Button) obj.v).getTextSize())));
			text.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					// unused
				}

				@Override
				public void afterTextChanged(Editable s) {
					try {
						if (obj.v instanceof TextView)
							((TextView) obj.v).setTextSize(Float.parseFloat(s
									.toString()));
						if (obj.v instanceof Button)
							((Button) obj.v).setTextSize(Float.parseFloat(s
									.toString()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

			text.setOnEditorActionListener(getActionListener());
		}

		v.setId(position);

		// NAME
		TextView name = (TextView) v.findViewById(R.id.tv_name);

		name.setText(obj.name);

		return v;
	}

	OnEditorActionListener getActionListener() {
		return new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					Global.hideSoftKeyboard();
					v.clearFocus();
				}
				return false;
			}
		};
	}
}
