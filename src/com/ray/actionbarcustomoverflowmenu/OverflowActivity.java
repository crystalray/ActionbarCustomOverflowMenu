package com.ray.actionbarcustomoverflowmenu;



import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.ray.actionbarcustomoverflowmenu.CustomOverflowMenu.ActionMenu;
import com.ray.actionbarcustomoverflowmenu.CustomOverflowMenu.OnItemOnClickListener;

public class OverflowActivity extends Activity implements OnItemOnClickListener{

	private CustomOverflowMenu mOverflowMenu;
	private TextView mText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blank);
		mText = (TextView) findViewById(R.id.text);
		
		mOverflowMenu = new CustomOverflowMenu(this, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mOverflowMenu.initHomeActions();
		mOverflowMenu.setItemOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.blank, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_more:
			mOverflowMenu.show(findViewById(R.id.action_more));
			return true;
		case R.id.action_search:
			mText.setText(R.string.action_search);
			return true;
		case R.id.action_compose:
			mText.setText(R.string.action_compose);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onItemClick(ActionMenu item, int position) {
		mText.setText(item.mTitle);		
	}


}
