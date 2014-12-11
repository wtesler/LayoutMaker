package tesler.will.layoutmaker;

import android.util.Log;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

public class Corners implements OnCheckedChangeListener {

	ToggleButton tb_menu, tb_layers, tb_views, tb_attributes;

	Corners() {

		tb_menu = (ToggleButton) Global.cont.findViewById(R.id.tb_menu);
		tb_layers = (ToggleButton) Global.cont.findViewById(R.id.tb_layers);
		tb_views = (ToggleButton) Global.cont.findViewById(R.id.tb_views);
		tb_attributes = (ToggleButton) Global.cont
				.findViewById(R.id.tb_attributes);

		tb_menu.setOnCheckedChangeListener(this);
		tb_layers.setOnCheckedChangeListener(this);
		tb_views.setOnCheckedChangeListener(this);
		tb_attributes.setOnCheckedChangeListener(this);

	}

	@Override
	public void onCheckedChanged(CompoundButton cb, boolean checked) {
		Log.i("Corners", "Check");
		switch (cb.getId()) {
		case R.id.tb_menu:
			break;
		case R.id.tb_layers:
			break;
		case R.id.tb_views:
			Global.sideMenus.toggleLeftMenu();
			break;
		case R.id.tb_attributes:
			Global.sideMenus.toggleRightMenu();
			break;
		}
	}

}
