package com.koba.myshelf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Favo_Shelf extends Activity {
	

	String DBPASS = Pass.DBPASS;
	String PATH = Pass.PATH;
	String USER = Pass.USER;
	
	
	
	ListView listView = null;
	ArrayAdapter<String> adapter = null;
	String favo_table = null;
	
	  ArrayList<String> screenname =     new  ArrayList<String>();
	  ArrayList<String> table_name = new ArrayList<String>();
	  ArrayList<String> tuikatext = new ArrayList<String>();



	public Connection con = null;
    public PreparedStatement ps = null;
	 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favo);
		 
		Intent intent = getIntent();
		// intentから指定キーの文字列を取得する
		favo_table = intent.getStringExtra("favo_table");

		
		listView =(ListView)findViewById(R.id.listView1);
		
		    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	            public void onItemClick(AdapterView<?> parent,
	                                    View view, int pos, long id) {

	                // 選択アイテムを取得
	                ListView listView = (ListView)parent;
	                String item = (String)listView.getItemAtPosition(pos);
	                System.out.println("item");
	                
	                
	                
	           
	                Intent intent = new Intent();
		            intent.setClassName(
		                    "com.koba.myshelf",
		                    "com.koba.myshelf.OtherShelf");
		         
		            intent.putExtra("table_name",table_name.get(pos));          
		            intent.putExtra("name",screenname.get(pos));
		            startActivity(intent);
	                finish();
	                
	            }
	        });
	
		
		

		    favo fv = new favo();
			 Thread child = new Thread(fv);
			 child.start();

		     adapter = new ArrayAdapter<String>(Favo_Shelf.this, R.layout.list_text , R.id.textView1, tuikatext);
		
			
			 listView.setAdapter(adapter);

			 try {
				child.join();
			} catch (InterruptedException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		    
		    
	 
	 
		
	}
	
	
	

class favo extends Thread{


		
       public void run() {

     
         Connection con = null;
         // データベースへ接続
        try {
        	  con = DriverManager.getConnection(PATH,USER,DBPASS);
        } catch (SQLException e) {
 		// TODO 自動生成された catch ブロック
 		e.printStackTrace();
        }
 
        ResultSet rs =null;
        
        String favoquery = "select * from " + favo_table;
        
        try {
        	  ps = con.prepareStatement(favoquery);
        	   rs = ps.executeQuery();
        	  
        } catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
        }
  
        
		try {
		
				 while(rs.next()) {

					screenname.add(rs.getString("screenname"));
					table_name.add(rs.getString("table_name"));
					tuikatext.add("@" + rs.getString("screenname")+ "さんの本棚"); 
				 
				 }
			
			
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	    		
		
     }
		
             

}
	
	
	
	
}