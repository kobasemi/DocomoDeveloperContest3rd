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
//参考サイトhttp://rakuishi.com/archives/6676/


public class MultiAdapter extends BaseAdapter {

  private Context mContext;
  private Bitmap    bitmapper[] = null;
  private String[] bbb = null; 
  private LayoutInflater mLayoutInflater;
  private String[]  header = null; 
  
  int heights = 0;
  int widths = 0;
  
  private static class ViewHolder {
	public TextView  TextView2;
	public ImageView hueImageView;
    public TextView  hueTextView;
 
  }

  public MultiAdapter(Context context,String[] aaa,String[] head,Bitmap[] bitmap,int height,int width) {
    mContext = context;
    mLayoutInflater = LayoutInflater.from(context);
    bbb = aaa;
    bitmapper = bitmap;
    header = head;
    
    heights = (height-935)/3;
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
      convertView = mLayoutInflater.inflate(R.layout.aaaaaaa2, null);
      holder = new ViewHolder();
     
      holder.hueImageView = (ImageView)convertView.findViewById(R.id.hue_imageview);
      holder.hueTextView = (TextView)convertView.findViewById(R.id.hue_textview);
      holder.TextView2 = (TextView)convertView.findViewById(R.id.TextView2);

      convertView.setTag(holder);
    } else {
      holder = (ViewHolder)convertView.getTag();
    }
    
    System.out.println(header[position]);
    holder.TextView2.setText(header[position]);
    holder.hueImageView.setImageBitmap(bitmapper[position]);
    holder.hueTextView.setText(bbb[position]);
    
    

    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(280,280);
    layoutParams.topMargin = (heights);
    layoutParams.leftMargin = (widths);
    holder.hueImageView.setLayoutParams(layoutParams);
      
    
    return convertView;
  }
  
  
}