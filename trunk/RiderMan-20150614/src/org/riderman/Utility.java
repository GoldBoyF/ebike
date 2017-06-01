package org.riderman;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;


public  class Utility {  
    public static void setListViewHeightBasedOnChildren(ListView listView) {  
        ListAdapter listAdapter = listView.getAdapter();   
        if (listAdapter == null) {  
            // pre-condition  
            return;  
        }  
  
        int totalHeight = 0;  
        for (int i = 0; i < listAdapter.getCount(); i++) {  
            View listItem = listAdapter.getView(i, null, listView);  
            listItem.measure(0, 0);  
            totalHeight += listItem.getMeasuredHeight();  
        }  
  
        ViewGroup.LayoutParams params = listView.getLayoutParams();  
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));  
        listView.setLayoutParams(params);  
   //     setLayoutY(listView, params.height);
    }  
    /* 
    * 设置控件所在的位置Y，并且不改变宽高， 
    * Y为绝对位置，此时X可能归0 
    */ 
    public static void setLayoutY(View view,int y) 
    { 
    MarginLayoutParams margin=new MarginLayoutParams(view.getLayoutParams()); 
    margin.setMargins(margin.leftMargin,margin.topMargin, margin.rightMargin, margin.height); 
    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin); 
    view.setLayoutParams(layoutParams); 
    } 
}  
