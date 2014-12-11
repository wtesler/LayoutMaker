package tesler.will.layoutmaker;

import java.util.ArrayList;

import tesler.will.layoutmaker.Layers.HierObj;
import tesler.will.layoutmaker.Views.ViewWrapper;
import android.content.Context;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LayerListAdapter extends BaseAdapter {

	Handler h = new Handler();
	Runnable r;
	private ArrayList<HierObj> items;
	Context cont;
	String selectedRowTag = "";
	LinearLayout selectedRow;

	public LayerListAdapter(Context context, ArrayList<HierObj> items) {

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

		final HierObj obj = items.get(position);

		if (obj.isLayer) {

			// Inflate the Row in the ListView
			LayoutInflater vi = (LayoutInflater) cont
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.row_hier_layer, vg, false);

			CheckBox cb = (CheckBox) v.findViewById(R.id.cb_visible);
			if (obj.v.getVisibility() == View.VISIBLE) {
				cb.setChecked(true);
			} else {
				cb.setChecked(false);
			}
			cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					if (isChecked)
						obj.v.setVisibility(View.VISIBLE);
					else
						obj.v.setVisibility(View.INVISIBLE);
				}
			});

		} else {

			// Inflate the Row in the ListView
			LayoutInflater vi = (LayoutInflater) cont
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.row_hier_view, vg, false);

			TextView delete = (TextView) v.findViewById(R.id.tv_delete);
			delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					for (ViewWrapper vw : Global.views.viewList) {
						if (vw.v.getTag() == obj.v.getTag()) {
							Global.views.viewList.remove(vw);
							break;
						}
					}
					Global.layers.list.get(obj.layer).removeView(obj.v);
					items.remove(position);
					notifyDataSetChanged();
				}
			});

			Button duplicate = (Button) v.findViewById(R.id.bt_duplicate);
			duplicate.setOnClickListener(new OnClickListener() {
				@SuppressWarnings("deprecation")
				@Override
				public void onClick(View v) {
					View newView = null;
					if (obj.v instanceof Button) {
						Button bt = (Button) obj.v;
						Button newBt = new Button(Global.cont);
						newBt.setText(bt.getText());
						newBt.setTag("bt_" + Global.views.btCounter++);
						newBt.setTextSize(bt.getTextSize());
						newBt.setTextColor(bt.getCurrentTextColor());
						newView = newBt;
					} else if (obj.v instanceof TextView) {
						TextView newTv = new TextView(Global.cont);
						TextView tv = (TextView) obj.v;
						newTv.setText(tv.getText());
						newTv.setTag("tv_" + Global.views.tvCounter++);
						newTv.setBackgroundDrawable(tv.getBackground());
						newTv.setTextSize(tv.getTextSize());
						newTv.setTextColor(tv.getCurrentTextColor());
						newTv.setGravity(tv.getGravity());
						newView = newTv;
					}

					assignListeners(newView);

					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
							obj.v.getLayoutParams());
					params.leftMargin += obj.v.getLeft() + 10;
					params.topMargin += obj.v.getTop() + 10;
					newView.setLayoutParams(params);

					Global.views.viewList.add(new ViewWrapper(newView,
							Global.layers.active));
					Global.layers.list.get(obj.layer).addView(newView);
					Global.layers.redraw(Global.canvas);
				}
			});
		}

		v.setId(position);

		// NAME
		TextView id = (TextView) v.findViewById(R.id.tv_id);

		id.setText((CharSequence) obj.v.getTag());

		LinearLayout parent = (LinearLayout) id.getParent();

		if (selectedRow == null
				|| selectedRowTag.contentEquals((String) obj.v.getTag())) {
			selectedRowTag = (String) obj.v.getTag();
			selectedRow = parent;
			animateRow();
		}

		parent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (obj.isLayer) {
					Global.layers.enableDisable(
							Global.layers.list.get(Global.layers.active), false);
					if (selectedRow != null) {
						selectedRow.setBackgroundColor(cont.getResources()
								.getColor(R.color.gang_yellow));
					}
					h.removeCallbacks(r);
					Global.layers.active = ((RelativeLayout) obj.v).getId();
					Global.layers.enableDisable(
							Global.layers.list.get(Global.layers.active), true);
					selectedRowTag = (String) obj.v.getTag();
					selectedRow = (LinearLayout) v;
					animateRow();

					Global.attr.populate(Global.layers.list
							.get(Global.layers.active));
				}
			}
		});

		return v;
	}

	private void animateRow() {

		if (r != null)
			h.removeCallbacks(r);

		selectedRow.setBackgroundResource(R.anim.layer_anim);
		// Begin the selection animation
		final TransitionDrawable transition = (TransitionDrawable) selectedRow
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

	private void assignListeners(View v) {
		v.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Global.views.dragView(v);
				return false;
			}
		});

		v.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// This view is now selected
				Views.selectedView = v;

				Global.attr.populate(Views.selectedView);

				// Set the Border layout
				Global.views.selectionBorder
						.setBackgroundResource(R.anim.highlight_anim);

				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
						v.getWidth(), v.getHeight());

				params.leftMargin = (int) v.getX();
				params.topMargin = (int) (v.getY() - 2);

				// Resize the border to fit around this view
				Global.views.selectionBorder.setLayoutParams(params);

				Global.views.layoutDots(params);

				// Display the selection border layer
				Global.views.selectionMask.setVisibility(View.VISIBLE);

				Global.views.animateSelectionBorder();
			}
		});
	}
}
