package com.koba.myshelf;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends SimpleCursorAdapter {
            
    static class ViewHolder {
        public ImageView imageView;
        public TextView textView;
    }
    
    public CustomListAdapter(Context context, int layout, Cursor c,
            String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v= inflater.inflate(R.layout.viewline,null,true);
        
        ViewHolder holder = new ViewHolder();

        holder.imageView = (ImageView) v.findViewById(R.id.imageView);
        holder.textView = (TextView) v.findViewById(R.id.textView);

        v.setTag(holder);
        
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        
        // SQLiteのテーブルの"sample_row"という列のデータを取得してセット
        String s = cursor.getString(cursor.getColumnIndex("sample_row")); 
        holder.textView.setText(s);

        // 別途用意したアイコンをセット
        holder.imageView.setImageResource(R.drawable.ic_launcher);
    }
}