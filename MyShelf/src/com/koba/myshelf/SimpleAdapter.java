package com.koba.myshelf;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;



public class SimpleAdapter extends BaseAdapter {

  private Context mContext;
  private Bitmap    bitmapper[] = null;
  private String[] bbb = null; 
  private LayoutInflater mLayoutInflater;
  int heights = 0;
  int widths = 0;

  private static class ViewHolder {
	
	public ImageView hueImageView;
    public TextView  hueTextView;
 
  }

  public SimpleAdapter(Context context,String[] aaa,Bitmap[] bitmap,int height,int width) {
    mContext = context;
    mLayoutInflater = LayoutInflater.from(context);
    bbb = aaa;
    bitmapper = bitmap;
  
    heights = (height-900)/3;
    widths = ((width/3)-280)/2;
    
    
    
  }

  public int getCount() {
    return bbb.length;
  }

  public Object getItem(int position) {
    return bbb[position];
  }

  public long getItemId(int position) {
    return position;
  }

  public View getView(int position, View convertView, ViewGroup parent) {

    ViewHolder holder;
    if (convertView == null) {
      convertView = mLayoutInflater.inflate(R.layout.aaaaaaa, null);
      holder = new ViewHolder();
     
      
      
      
      holder.hueImageView = (ImageView)convertView.findViewById(R.id.hue_imageview);
      holder.hueTextView = (TextView)convertView.findViewById(R.id.hue_textview);

      convertView.setTag(holder);
    } else {
      holder = (ViewHolder)convertView.getTag();
    }
  
    holder.hueImageView.setImageBitmap(bitmapper[position]);
    holder.hueTextView.setText(bbb[position]);
 
    
    
  RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(280,280);
  layoutParams.topMargin = (heights);
  layoutParams.leftMargin = (widths);
  holder.hueImageView.setLayoutParams(layoutParams);
    
    return convertView;
  }
  
  
}