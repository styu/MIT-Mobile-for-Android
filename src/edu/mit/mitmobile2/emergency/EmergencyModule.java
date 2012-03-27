package edu.mit.mitmobile2.emergency;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import edu.mit.mitmobile2.MITMenuItem;
import edu.mit.mitmobile2.NewModule;
import edu.mit.mitmobile2.R;

public class EmergencyModule extends NewModule {

	@Override
	public String getLongName() {
		return "Emergency Info";
	}

	@Override
	public String getShortName() {
		return "Emergency";
	}
	
	@Override
	public Class<? extends Activity> getModuleHomeActivity() {
		return EmergencyActivity.class;
	}

	@Override
	public int getMenuIconResourceId() {
		return R.drawable.menu_emergency;
	}

	@Override
	public String getMenuOptionTitle() {
		return "Emergency Info";
	}
	
	@Override
	public int getHomeIconResourceId() {
		return R.drawable.home_emergency;
	}

	@Override
	protected List<MITMenuItem> getPrimaryOptions() {
		// TODO Auto-generated method stub
		ArrayList<MITMenuItem> menuItems = new ArrayList<MITMenuItem>();
		menuItems.add(new MITMenuItem("refresh", "", R.drawable.menu_refresh));
		return menuItems;
	}

	@Override
	protected List<MITMenuItem> getSecondaryOptions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean onItemSelected(Activity activity, String id) {
		// TODO Auto-generated method stub
		return false;
	}
}
