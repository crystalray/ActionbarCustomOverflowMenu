package com.ray.actionbarcustomoverflowmenu;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;


public class CustomOverflowMenu extends PopupWindow {
	
	public static final int TITLE_LEFT = 0;
	public static final int TITLE_RIGHT = 1;
	protected final int LIST_PADDING = 10;
	
	private Context mContext;
	private Rect mRect = new Rect();
	private final int[] mLocation = new int[2];
	
	private int mScreenWidth;
	private int mScreenHeight;
	private int mPopupGravity = Gravity.NO_GRAVITY;
	private int mDirection = TITLE_RIGHT;
	
	private OnItemOnClickListener mItemOnClickListener;
	private ListView mListView;
	private ActionItem[] mActionItems;
	
	public static interface OnItemOnClickListener{
		public void onItemClick(ActionItem item , int position);
	}
	
	
	public CustomOverflowMenu(Context context){
		this(context, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}
	
	public CustomOverflowMenu(Context context, int width, int height){
		this.mContext = context;
		setFocusable(true);
		setTouchable(true);
		setOutsideTouchable(true);
		mScreenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
		mScreenHeight = mContext.getResources().getDisplayMetrics().heightPixels;
		setWidth(width);
		setHeight(height);
		setBackgroundDrawable(new BitmapDrawable());
		
		
		setContentView(LayoutInflater.from(mContext).inflate(R.layout.custom_overflow, null));
		mListView = (ListView) getContentView().findViewById(R.id.actions_list);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {
				dismiss();
				if(mItemOnClickListener != null)
					mItemOnClickListener.onItemClick(mActionItems[index], index);
			}
		}); 
	}
	
	public ActionItem getAction(int position){
		if(position < 0 || position > mActionItems.length)
			return null;
		return mActionItems[position];
	}
	
	public void setDirection(int direction){
		this.mDirection = direction;
	}
	
	public void setActions(ActionItem[] actions){
		this.mActionItems = actions;
		mListView.setAdapter(new ActionsAdapter(mContext)) ;
	}
	
	public void setItemOnClickListener(OnItemOnClickListener onItemOnClickListener){
		this.mItemOnClickListener = onItemOnClickListener;
	}
	
	public void show(View view){
		view.getLocationOnScreen(mLocation);
		mRect.set(mLocation[0], mLocation[1], mLocation[0] + view.getWidth(),
				mLocation[1] + view.getHeight());
		showAtLocation(view, mPopupGravity, mScreenWidth - LIST_PADDING - (getWidth()/2), mRect.bottom);
	}
	
	static class ViewHolder {
		ImageView pic;
        TextView title;
    }
	
	private class ActionsAdapter extends BaseAdapter{

		private LayoutInflater inflater;

		public ActionsAdapter(Context context) {
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public Object getItem(int position) {
			return mActionItems[position];
		}
		
		@Override
		public int getCount() {
			return mActionItems.length;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ActionItem item = mActionItems[position];
			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.overflow_item, parent, false);
				holder = new ViewHolder();
				holder.title = (TextView) convertView.findViewById(R.id.actionbar_dropdown_tv);
				holder.pic = (ImageView)convertView.findViewById(R.id.actionbar_dropdown_iv);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.title.setText(item.mTitle);
			holder.pic.setImageResource(item.mImgRes);
			return convertView;
		}
		
	}
	
}
