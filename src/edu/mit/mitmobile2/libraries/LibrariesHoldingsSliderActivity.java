package edu.mit.mitmobile2.libraries;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import edu.mit.mitmobile2.LockingScrollView;
import edu.mit.mitmobile2.Module;
import edu.mit.mitmobile2.R;
import edu.mit.mitmobile2.SimpleArrayAdapter;
import edu.mit.mitmobile2.SliderActivity;
import edu.mit.mitmobile2.SliderInterface;
import edu.mit.mitmobile2.SmallActivityCache;
import edu.mit.mitmobile2.TwoLineActionRow;
import edu.mit.mitmobile2.libraries.BookItem.Holding.Availability;
import edu.mit.mitmobile2.libraries.BookItem.Holding.Availabilitys;

public class LibrariesHoldingsSliderActivity extends SliderActivity {
	
	private static SmallActivityCache<Map<String, Availabilitys>> sListsOfHoldingsCache = 
		new SmallActivityCache<Map<String, Availabilitys>>();
	
	private static SmallActivityCache<List<String>> sListOfLibrariesCache = 
		new SmallActivityCache<List<String>>();
	
	private static final String LIST_HOLDINGS_CACHE_KEY = "list_holdings_cache_key";
	private static final String LIST_LIBRARIES_CACHE_KEY = "list_libraries_cache_key";
	
	public static void launch(Context context, Map<String, Availabilitys> availabilitys, List<String> libraries, int position) {
		Intent intent = new Intent(context, LibrariesHoldingsSliderActivity.class);
		long listHoldingsKey = sListsOfHoldingsCache.put(availabilitys);
		long librariesKey = sListOfLibrariesCache.put(libraries);
		intent.putExtra(LIST_HOLDINGS_CACHE_KEY, listHoldingsKey);
		intent.putExtra(LIST_LIBRARIES_CACHE_KEY, librariesKey);
		intent.putExtra(KEY_POSITION, position);
		context.startActivity(intent);
	}
	
	
	Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
	
		mContext = this;
		
		Map<String, Availabilitys> listOfHoldings = 
			sListsOfHoldingsCache.getItem(getIntent().getLongExtra(LIST_HOLDINGS_CACHE_KEY, -1));
		
		List<String> listOfLibraries = 
			sListOfLibrariesCache.getItem(getIntent().getLongExtra(LIST_LIBRARIES_CACHE_KEY, -1));
		
		if (listOfHoldings == null || listOfLibraries == null) {
			finish();
			return;
		}
		
		for (String library : listOfLibraries) {
			Availabilitys availabilitys = listOfHoldings.get(library);
			addScreen(new MITLibraryBookHoldingsSliderInterface(availabilitys.getBooks()), null, library);
		}
		setPosition(getPositionValue());
		
	}
	
	
	@Override
	protected Module getModule() {
		return new LibrariesModule();
	}

	@Override
	public boolean isModuleHomeActivity() {
		return false;
	}

	@Override
	protected void prepareActivityOptionsMenu(Menu menu) { }	
	
	
	private class MITLibraryBookHoldingsSliderInterface implements SliderInterface {

		private List<Availability> mAvailabilitys;

		MITLibraryBookHoldingsSliderInterface(List<Availability> availabilitys) {
			mAvailabilitys = availabilitys;
		}
		
		@Override
		public void updateView() { } // TODO Auto-generated method stub

		@Override
		public View getView() {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
			ListView listView = (ListView) inflater.inflate(R.layout.styled_list_view, null);
			listView.setSelector(android.R.color.transparent);
			listView.setAdapter(new BookAvailibilityArrayAdapter(mContext, mAvailabilitys));
			return listView;
		}

		@Override
		public void onSelected() { } // TODO Auto-generated method stub

		@Override
		public LockingScrollView getVerticalScrollView() { return null; } // TODO Auto-generated method stub

		@Override
		public void onDestroy() { } // TODO Auto-generated method stub
	}
	
	private class BookAvailibilityArrayAdapter extends SimpleArrayAdapter<Availability> {

		public BookAvailibilityArrayAdapter(Context context, List<Availability> items) {
			super(context, items, R.layout.boring_action_row);
		}

		@Override
		public void updateView(Availability item, View view) {
			TwoLineActionRow row = (TwoLineActionRow) view;
			row.setTitle(item.callNumber);
			Spannable status = Spannable.Factory.getInstance().newSpannable(item.status);
			StyleSpan boldMarker = new StyleSpan(Typeface.BOLD);
			if (item.available) {
				status.setSpan(boldMarker, 0, status.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			}
			row.setSubtitle(status);
		}
	}
}
