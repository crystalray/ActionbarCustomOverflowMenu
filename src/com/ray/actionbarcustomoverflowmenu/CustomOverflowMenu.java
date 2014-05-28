package com.ray.actionbarcustomoverflowmenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


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
	private static final int RIGHT_PADDING = 12;
	private static final int TOP_PADDING = 6;
	
	private Context mContext;
	private Rect mRect = new Rect();
	private final int[] mLocation = new int[2];
	
	private int mScreenWidth;
	private int mScreenHeight;
	private int mPopupGravity = Gravity.NO_GRAVITY;
	private int mDirection = TITLE_RIGHT;
	
	private OnItemOnClickListener mItemOnClickListener;
	private ListView mListView;
	private List<ActionMenu> mActionItems = new ArrayList<ActionMenu>();
    private ActionsAdapter mActionsAdapter;
	
	public static interface OnItemOnClickListener{
		public void onItemClick(ActionMenu item , int position);
	}
	
	public static class ActionMenu {
	    public int mImgRes;
	    public int mTitle;
	    public int mItemId;
	    
	    public ActionMenu(int titleId, int drawableId, int itemId){
	        this.mTitle = titleId;
	        this.mImgRes = drawableId;
	        this.mItemId = itemId;
	    }

        @Override
        public boolean equals(Object o) {
            if (o instanceof ActionMenu) {
                ActionMenu actionItem = (ActionMenu) o;
                if(actionItem.mItemId == this.mItemId)
                    return true;
            }
            return false;
        }
	    
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
		setAnimationStyle(R.style.PopupAnimation);
		
		setContentView(LayoutInflater.from(mContext).inflate(R.layout.custom_overflow, null));
		mListView = (ListView) getContentView().findViewById(R.id.actions_list);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {
				dismiss();
				if(mItemOnClickListener != null)
					mItemOnClickListener.onItemClick(mActionItems.get(index), index);
			}
		});
		mActionsAdapter = new ActionsAdapter(mContext);
		mListView.setAdapter(mActionsAdapter);
	}
	
	public ActionMenu getAction(int position){
		if(position < 0 || position > mActionItems.size())
			return null;
		return mActionItems.get(position);
	}
	
	public void setDirection(int direction){
		mDirection = direction;
	}
	
	public void setActions(List<ActionMenu> actions){
		mActionItems = actions;
		mActionsAdapter.notifyDataSetChanged();
	}
	
	public void addAction(ActionMenu action){
	    mActionItems.add(action);
	    mActionsAdapter.notifyDataSetChanged();
	}
	
	public void removeAction(ActionMenu action){
	    mActionItems.remove(action);
        mActionsAdapter.notifyDataSetChanged();
	}
	
	public static final int CUSTOM_MENU_ADD_ACCOUNT = 0;
	public static final int CUSTOM_MENU_SETTING = 1;
	public static final int CUSTOM_MENU_FEEDBACK = 2;
	
	public static final int CUSTOM_MENU_RESEND = 3;
	
	 private static final HashMap<Integer,ActionMenu> sMenus = new HashMap<Integer,ActionMenu>();
	    static {
	        sMenus.put(CUSTOM_MENU_ADD_ACCOUNT,new ActionMenu(R.string.action_add, R.drawable.ic_add, CUSTOM_MENU_ADD_ACCOUNT));
	        sMenus.put(CUSTOM_MENU_SETTING,new ActionMenu(R.string.action_setting, R.drawable.ic_setting, CUSTOM_MENU_SETTING));
	        sMenus.put(CUSTOM_MENU_FEEDBACK,new ActionMenu(R.string.action_feedback, R.drawable.ic_feedback, CUSTOM_MENU_FEEDBACK));
	        sMenus.put(CUSTOM_MENU_RESEND,new ActionMenu(R.string.action_mail, R.drawable.ic_resend, CUSTOM_MENU_RESEND));
	    }
	
	public void initHomeActions(){
	    List<ActionMenu> actions = new ArrayList<ActionMenu>();
	    actions.add(sMenus.get(CUSTOM_MENU_ADD_ACCOUNT));
	    actions.add(sMenus.get(CUSTOM_MENU_SETTING));
	    actions.add(sMenus.get(CUSTOM_MENU_FEEDBACK));
	    setActions(actions);
	}
	
	public void refreshResendAction(boolean isVisiable){
	    if(isVisiable){
	        if(!mActionItems.contains(sMenus.get(CUSTOM_MENU_RESEND)))
	            addAction(sMenus.get(CUSTOM_MENU_RESEND));
	    }
	    else{
	        if(mActionItems.contains(sMenus.get(CUSTOM_MENU_RESEND)))
	            removeAction(sMenus.get(CUSTOM_MENU_RESEND));
	    }
	}
	
	public void setItemOnClickListener(OnItemOnClickListener onItemOnClickListener){
		this.mItemOnClickListener = onItemOnClickListener;
	}
	
	public void show(View view){
		view.getLocationOnScreen(mLocation);
		mRect.set(mLocation[0], mLocation[1], mLocation[0] + view.getWidth(),
				mLocation[1] + view.getHeight());
		showAtLocation(view, mPopupGravity, mScreenWidth - RIGHT_PADDING - (getWidth()/2), mRect.bottom - dip2px(mContext, TOP_PADDING));
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
			return mActionItems.get(position);
		}
		
		@Override
		public int getCount() {
			return mActionItems.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ActionMenu item = mActionItems.get(position);
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
	
	public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
	
}
