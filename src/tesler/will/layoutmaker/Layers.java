package tesler.will.layoutmaker;

import java.util.ArrayList;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class Layers {

	// The Layer Hierarchy as expressed in the right sliding menu
	ArrayList<HierObj> hierList = new ArrayList<Layers.HierObj>();
	ListView lv;
	LayerListAdapter adapter;

	// Adds new Layer
	Button addLayer;

	// RelativeLayout == Layer. This is a list of Layers.
	ArrayList<RelativeLayout> list = new ArrayList<RelativeLayout>();

	// Denotes the position in list of the active layer
	int active = 0;

	public Layers() {

		// Get the first layer from the canvas (this layer is autobuilt in
		// layout xml)
		RelativeLayout initialLayer = (RelativeLayout) Global.cont
				.findViewById(R.id.rl_layer);
		initialLayer.setTag("Layer 1");

		initialLayer.setId(0);

		list.add(initialLayer);

		Global.attr.populate(initialLayer);

		adapter = new LayerListAdapter(Global.cont, hierList);

		lv = (ListView) Global.cont.getSlidingMenu().findViewById(R.id.lv_hier);

		lv.setAdapter(adapter);

		redraw(Global.canvas);

		addLayer = (Button) Global.cont.getSlidingMenu().findViewById(
				R.id.bt_add_layer);
		addLayer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				RelativeLayout newLayer = new RelativeLayout(Global.cont);
				String tag = "Layer " + Integer.toString(list.size() + 1);
				newLayer.setTag(tag);
				newLayer.setOnDragListener(Global.views);

				RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT,
						RelativeLayout.LayoutParams.MATCH_PARENT);

				newLayer.setLayoutParams(rlp);

				enableDisable(list.get(active), false);

				// index of the element at the back of list
				active = list.size();

				// This must happen after active is reassigned
				newLayer.setId(active);

				Global.canvas.addView(newLayer);

				list.add(newLayer);

				Global.attr.populate(newLayer);

				adapter.selectedRowTag = tag;

				redraw(Global.canvas);

			}
		});
	}

	public void redraw(ViewGroup parent) {

		hierList.clear();

		generateViewHierarchy(parent, -1);

		adapter.notifyDataSetChanged();

	}

	public void generateViewHierarchy(ViewGroup parent, int layerCounter) {

		for (int i = 0; i < parent.getChildCount(); i++) {
			View v = parent.getChildAt(i);
			if (v instanceof ViewGroup) {
				hierList.add(new HierObj(v, true, ++layerCounter, i));
				generateViewHierarchy((ViewGroup) v, layerCounter);
			} else {
				hierList.add(new HierObj(v, false, layerCounter, i));
			}
		}
	}

	public void enableDisable(View view, boolean enabled) {

		view.setEnabled(enabled);

		if (view instanceof ViewGroup) {
			ViewGroup group = (ViewGroup) view;

			for (int idx = 0; idx < group.getChildCount(); idx++) {
				enableDisable(group.getChildAt(idx), enabled);
			}
		}
	}

	class HierObj {
		View v;
		Boolean isLayer;
		int layer;
		int position;

		public HierObj(View v, Boolean isLayer, int l, int p) {
			this.v = v;
			this.isLayer = isLayer;
			layer = l;
			position = p;
		}
	}
}
