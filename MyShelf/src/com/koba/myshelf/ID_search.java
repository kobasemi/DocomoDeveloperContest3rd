package com.koba.myshelf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ID_search extends Activity  {

	
	String DBPASS = Pass.DBPASS;
	String PATH = Pass.PATH;
	String USER = Pass.USER;
	
	
	
	int hantei = 0; 
		String searchtext = null;
	 final Handler handler=new Handler();
	 public Connection con = null;
	    public PreparedStatement ps = null;
	    EditText Edit = null;
	    String table_name = null;
	
	    
	    

		public View.OnClickListener searchar = new View.OnClickListener() {

			public void onClick(View v) {


				   SpannableStringBuilder sb = (SpannableStringBuilder)Edit.getText();
			       
			        if(sb.toString() == null){
			        	
			        	
			        }else{
			        
			        searchtext = sb.toString();
			        
			        String regex = "@";
			        Pattern p = Pattern.compile(regex);

			        Matcher m = p.matcher(searchtext);
			        searchtext = m.replaceAll("");
			        
			        
			     
			        System.out.println("result :" + searchtext);
			        
			        
			   	 
					 IDsearch db = new IDsearch();
					 Thread childThread = new Thread(db);
					 childThread.start();
					
			
					 try {
						childThread.join();
					} catch (InterruptedException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}
					 System.out.println("hantei" + hantei);
			        if(hantei == 1){
			        
			        new AlertDialog.Builder(ID_search.this)
			        .setTitle("探索結果")   
			        .setMessage("@" + searchtext + "さんの本棚に飛びますか?")
			        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			     
			        public void onClick(DialogInterface dialog, int whichButton) {
			     
			        	 Intent intentother = new Intent();
				            intentother.setClassName(
				                    "com.koba.myshelf",
				                    "com.koba.myshelf.OtherShelf");

				            intentother.putExtra("table_name",table_name);
				            intentother.putExtra("name", searchtext);
				           

				    		System.out.println("table" + table_name);
				    	
				    		System.out.println("name" + searchtext);
				            

				            startActivity(intentother);
				            finish();
			        	
			        	
			        }
			      
			        })
		
			        .setNegativeButton("No", new DialogInterface.OnClickListener() {
			   
			        public void onClick(DialogInterface dialog, int whichButton) {
			       
			        	
			        	
			        	
			        }
			    
			        })
			
			        .show();
			        
			        
			        }else{
			        	
			        
			        //街頭なし
			        
			        
			        }
			        }
			        
			        
			        


			}
		};

	    
	    
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.id_search);
	     
	        
	        //
	        Edit = (EditText)findViewById(R.id.searchtext);
	        
	        Button  search = (Button)findViewById(R.id.search);
	        search.setOnClickListener(searchar);
	        
	        
	     
	        
	        
		}
class IDsearch extends Thread{


		
       public void run() {

           hantei = 0;
         Connection con = null;
         // データベースへ接続
        try {
        	  con = DriverManager.getConnection(PATH,USER,DBPASS);
        } catch (SQLException e) {
 		// TODO 自動生成された catch ブロック
 		e.printStackTrace();
        }
   

   
        // クエリーを実行して結果セットを取得
    

        ResultSet rs =null;
        
        String searchquery = "select * from all_user where screenname = '"+  searchtext +"'";
        
        try {
        	  ps = con.prepareStatement(searchquery);
        	   rs = ps.executeQuery();
        	  
        } catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
        }
  
        
		try {
		
				 while(rs.next()) {
					 table_name	= rs.getString("table_name");

			    		System.out.println("rstable" + table_name);
			    		
			    		if(table_name == null) {
			    			
			    			hantei = 0;
			    			
			    			
			    		}else{
			    			
			    			hantei = 1;
			    			
			    		}
			    		
			    	
			    		System.out.println("hantei" + hantei);
			    		
			    		
				 }
			
			
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	    		
		
     }
		
             

}
}


