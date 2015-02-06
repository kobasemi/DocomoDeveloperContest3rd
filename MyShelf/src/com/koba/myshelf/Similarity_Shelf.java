package com.koba.myshelf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class Similarity_Shelf extends Activity {

	//camera起動　→　ページ生成　→　ページ生成と共に同じアーティストや曲（アルバム）を格納している
	//他人の本棚紹介 ListView　表示
	//とりあえず類似度とか関係なくDB検索で同じアーティストを入れている人の本棚を紹介する

	
	String DBPASS = Pass.DBPASS;
	String PATH = Pass.PATH;
	String USER = Pass.USER;

	EditText e1 = null;
	EditText e2 = null;
	ListView v1 = null;
    String vocabulary  = null;
    ArrayAdapter<String> arrayAdapter = null;
	String Artist = null;
	
	public Connection con = null;
    public PreparedStatement ps = null;

    ArrayList<Integer> tid = new ArrayList<Integer>();
    ArrayList<Long> tuid = new ArrayList<Long>();
    ArrayList<String> ArtistorAuthor = new ArrayList<String>();
    ArrayList<String> item_name= new ArrayList<String>();
    ArrayList<String> screenname= new ArrayList<String>();
    ArrayList<String> othertable_name = new ArrayList<String>();
    ArrayList<String> name= new ArrayList<String>();

	Button btn1 = null;
	Button btn2 = null;

	int hantei = 0;
	String searchtext = null;
	final Handler handler=new Handler();

	EditText Edit = null;
	String table_name = null;

		public View.OnClickListener searchar = new View.OnClickListener() {
			public void onClick(View v){


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
					 Thread childThread = new Thread( db);
					 childThread.start();


					 try {
						childThread.join();
					} catch (InterruptedException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}

			        if(hantei == 1){

			        new AlertDialog.Builder(Similarity_Shelf.this)
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



      	    		  AlertDialog.Builder dlg;
		                   dlg = new AlertDialog.Builder(Similarity_Shelf.this);
		                   dlg.setTitle("結果");
		                   dlg.setMessage("そのユーザーは存在しません");
		                   dlg.setPositiveButton("Yes", null);
		                   dlg.show();



			        }
			        }





			}
		};





	public View.OnClickListener search2 = new View.OnClickListener() {

		public void onClick(View v) {


			 SpannableStringBuilder sb = (SpannableStringBuilder)e1.getText();
			 vocabulary= sb.toString();



			search_vocabulary sr = new search_vocabulary();
			 Thread childThread = new Thread(sr);
			 childThread.start();
			 try {
					childThread.join();
				} catch (InterruptedException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
			 
			 
			 if(arrayAdapter !=null){
			 
			  v1.setAdapter(arrayAdapter);
			  
			 }else{

 	    		  AlertDialog.Builder dlg;
	                   dlg = new AlertDialog.Builder(Similarity_Shelf.this);
	                   dlg.setTitle("結果");
	                   dlg.setMessage("その作家/Artist/作家を追加している本棚は見つかりません");
	                   dlg.setPositiveButton("Yes", null);
	                   dlg.show();

				 
				 
				 
			 }
			  
		
		}
	};


	public View.OnClickListener search1 = new View.OnClickListener() {

		public void onClick(View v) {
			System.out.println("search1");


			 SpannableStringBuilder sb = (SpannableStringBuilder)e1.getText();
        	 Artist = sb.toString();


        	

			search_artist sr = new search_artist();
			 Thread childThread = new Thread(sr);
			 childThread.start();
			 System.out.println("search2");
			 
			 try {
				 System.out.println("search3");
				 
					childThread.join();
				} catch (InterruptedException e) {
					// TODO 自動生成された catch ブロック
					
					System.out.println("search1\4");
					e.printStackTrace();
					
					
				}
			 System.out.println("search5");
		 
			 
			 if(arrayAdapter !=null){
			 
			  v1.setAdapter(arrayAdapter);
			  
			 }else{

 	    		  AlertDialog.Builder dlg;
	                   dlg = new AlertDialog.Builder(Similarity_Shelf.this);
	                   dlg.setTitle("結果");
	                   dlg.setMessage("その本/曲/DVDを追加している本棚はありません");
	                   dlg.setPositiveButton("Yes", null);
	                   dlg.show();

				 
				 
				 
			 }
			  
        	 
		

		}
	};





	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.id_search);

        //
        Edit = (EditText)findViewById(R.id.searchtext);
        e1 = (EditText)findViewById(R.id.editText1);
        e2 = (EditText)findViewById(R.id.editText2);
        v1 = (ListView)findViewById(R.id.listView);

        Button  search = (Button)findViewById(R.id.search);
        search.setOnClickListener(searchar);



		btn1 = (Button) findViewById(R.id.button1); //artist
		btn2= (Button) findViewById(R.id.button2);	//voca


		btn1.setOnClickListener(search1);
		btn2.setOnClickListener(search2);

		
	    v1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View view, int pos, long id) {

                // 選択アイテムを取得
                ListView listView = (ListView)parent;
                String item = (String)listView.getItemAtPosition(pos);
                System.out.println("item");
                
                
                
              
                System.out.println(othertable_name.get(pos));
                
                
                Intent intent = new Intent();
	            intent.setClassName(
	                    "com.koba.myshelf",
	                    "com.koba.myshelf.OtherShelf");
	         
	            intent.putExtra("table_name",othertable_name.get(pos));
	            
	            System.out.println("table_name" + othertable_name.get(pos));
	            
	            intent.putExtra("name",name.get(pos));
	            startActivity(intent);
                
                finish();
                
            }
        });
		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ecsiteactionbar, menu);
		getActionBar().setTitle("各種検索");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
	
		  switch (item.getItemId()) {
	        case R.id.lobt:
	        	shelf();
	        	break;
	        }

		
		return super.onOptionsItemSelected(item);
	}

	public void shelf () {



		finish();


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
				rs = ps.executeQuery();
			} catch (SQLException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}

			try {
			

					 while(rs.next()) {
						 table_name	= rs.getString("table_name");

				    		System.out.println("rstable" + table_name);

					 }

				
					 if(table_name == null){
						 
						 hantei = 0;
						 
					 }else{
						 
						 hantei = 1;
						 
					 }
				

	       

			} catch (SQLException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}


	     }



	}


	class search_vocabulary extends Thread{


		int all = 0;
		ResultSet rs = null;
		
		
		   ArrayList<String> tuikatext = new ArrayList<String>();



         public void run() {

        	 SpannableStringBuilder sb = (SpannableStringBuilder)e2.getText();
        	  vocabulary = sb.toString();


         	  // ドライバクラスをロード
             try {
					Class.forName("com.mysql.jdbc.Driver");
				} catch (ClassNotFoundException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}

             // データベースへ接続
             try {
            	 con = DriverManager.getConnection(PATH,USER,DBPASS);
				} catch (SQLException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}



             String allitem =  " select * from all_item where item_name like '%"+ vocabulary +"%'";
             System.out.println(allitem);


             try {
					ps = con.prepareStatement(allitem);
				} catch (SQLException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
            
			
         	ResultSet rs = null;
			try {
				rs = ps.executeQuery();
			} catch (SQLException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}


		        tuid.clear();
		        tid.clear();
		        screenname.clear();
		        item_name.clear();

            	int num = 0;


            	try {
					while(rs.next()) {


						
						
					
						
					        tid.add( rs.getInt("id"));
					        System.out.println("あ" +  rs.getInt("id"));
					        tuid.add(rs.getLong("uid"));
					  
					        screenname.add("@"+rs.getString("screenname")+"さんの本棚");
					        System.out.println("い" + rs.getString("screenname"));
					        item_name.add(rs.getString("item_name"));
					        System.out.println("い" + rs.getString("item_name"));
					    
						
					        
					        
					        
					        String usr =  "select * from all_user where uid = " + rs.getLong("uid");
			            	try {
								ps = con.prepareStatement(usr);
							} catch (SQLException e) {
								// TODO 自動生成された catch ブロック
								e.printStackTrace();
							}
			            	ResultSet res = null;
							try {
								res = ps.executeQuery();
							} catch (SQLException e) {
								// TODO 自動生成された catch ブロック
								e.printStackTrace();
							}
							
							while(res.next()) {
								  othertable_name.add(res.getString("table_name"));
								  name.add(res.getString("name"));
							}
					    	num++;
					    	
					    	
					    if(rs.getString("item_name") != null){
					    	
					    	tuikatext.add("@"+rs.getString("screenname")+"さんの本棚" + "  title:" + rs.getString("item_name"));
					    	
					    }
					    
					    }

					    	
					    	
					    	
					    	

					
				} catch (SQLException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}




                
                if(item_name != null){
                

                arrayAdapter = new ArrayAdapter<String>(Similarity_Shelf.this, R.layout.list_text , R.id.textView1, tuikatext);

                }

         	}






	}






	class search_artist extends Thread{



		   ArrayList<String> tuikatext = new ArrayList<String>();


         public void run() {



        	 System.out.println("search6");


         	  // ドライバクラスをロード
             try {
					Class.forName("com.mysql.jdbc.Driver");
				} catch (ClassNotFoundException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}

             // データベースへ接続
             try {
            	 con = DriverManager.getConnection(PATH,USER,DBPASS);
				} catch (SQLException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}

            // ステートメントオブジェクトを生成


             // クエリーを実行して結果セットを取得



             System.out.println("search7");
             //Artist検索

             String allitem =  " select * from all_item where ArtistorAuthor like '%"+Artist +"%'";
            	try {
					ps = con.prepareStatement(allitem);
				} catch (SQLException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
            	ResultSet rs = null;
				try {
					rs = ps.executeQuery();
				} catch (SQLException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}


            	int num = 0;

		        tuid.clear();
		        tid.clear();
		        screenname.clear();
		        item_name.clear();

		        System.out.println("search8");
             try {
					while(rs.next()) {


						  //同じuid(同じ人)はリストに入れない
			//		    if (!Arrays.asList(tuid).contains(rs.getLong("uid"))) {

				        tid.add( rs.getInt("id"));

				        tuid.clear();
				        tid.clear();
				        screenname.clear();
				        item_name.clear();



				        System.out.println("あ" +  rs.getInt("id"));
				        tuid.add(rs.getLong("uid"));
			
				        screenname.add("@"+rs.getString("screenname")+"さんの本棚");
				        System.out.println("い" + rs.getString("screenname"));
				        item_name.add(rs.getString("item_name"));
				        System.out.println("い" + rs.getString("item_name"));

					    	num++;
					    	
					    	
					        String usr =  "select * from all_user where uid = " + rs.getLong("uid");
			            	try {
								ps = con.prepareStatement(usr);
							} catch (SQLException e) {
								// TODO 自動生成された catch ブロック
								e.printStackTrace();
							}
			            	ResultSet res = null;
							try {
								res = ps.executeQuery();
							} catch (SQLException e) {
								// TODO 自動生成された catch ブロック
								e.printStackTrace();
							}
							
							while(res.next()) {
								  othertable_name.add(res.getString("table_name"));
								  name.add(res.getString("name"));
							}
							   
							    	
							    	tuikatext.add("@"+rs.getString("screenname")+"さんの本棚" + "  title:" + rs.getString("item_name"));
							    	
							
				//	    }


					}
				} catch (SQLException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}

             
             if(item_name != null){
             

             arrayAdapter = new ArrayAdapter<String>(Similarity_Shelf.this, R.layout.list_text , R.id.textView1, tuikatext);

             }


         	}



	}




}
