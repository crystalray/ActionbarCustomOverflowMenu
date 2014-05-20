package com.ray.actionbarcustomoverflowmenu;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class ActionItem {
	public int mImgRes;
	public CharSequence mTitle;
	
	
	public ActionItem(Context context, int titleId, int drawableId){
		this.mTitle = context.getResources().getText(titleId);
		this.mImgRes = drawableId;
	}
	
}
