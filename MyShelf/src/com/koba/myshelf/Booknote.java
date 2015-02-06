package com.koba.myshelf;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class Booknote extends Activity{

	
	Pass passes = new Pass();
	String DBPASS = passes.DBPASS;
	String PATH = passes.PATH;
	String USER = passes.USER;
	ListView v1 = null;
	
	String picture = null;
	 final Handler handler=new Handler();
	InputStream istream = null;

	URL url = null;

	Bitmap rtbitmap = null;
	EditText Edit = null;

	String table  = null ;
	String year= null ;
	String ECURL= null ;
	String manufacture= null ;
	String allmusic= null ;
	String alubam = null ;
	String music = null ;
	String summary= null ;
	String name = null;
	String artist = null;
	String director = null;
	String cast = null;
	String title = null;
	String wikipedia = null;
	String author = null;
	int  tablecnt = 0;
    ArrayAdapter<String> arrayAdapter = null;
	
	
	 public PreparedStatement ps = null;

	    ArrayList<Integer> tid = new ArrayList<Integer>();
	    ArrayList<Integer> tuid = new ArrayList<Integer>();
	    ArrayList<String> ArtistorAuthor = new ArrayList<String>();
	    ArrayList<String> item_name= new ArrayList<String>();
	    ArrayList<String> screenname= new ArrayList<String>();
	    ArrayList<String> othertable_name = new ArrayList<String>();
	    ArrayList<String> name2= new ArrayList<String>();
	
	
	String notetext = null;
	
		//youtube


		  Connection con = null;
		    Bitmap bitmap = null;
		    java.sql.Connection  con2 = null;
	

		//keybord操作
			InputMethodManager inputMethodManager;
		
			private LinearLayout mainLayout;
			
			
			
			
		public void notecommit()   {// 検索したいDVD名を入力することで,そのDVDのあらすじを返す

				
				   (new Thread(new Runnable() {
		                @Override
		                public void run() {

				
	                Connection con = null;
	                // データベースへ接続
	               try {
	            	    con = DriverManager.getConnection(PATH,USER,DBPASS);
	               } catch (SQLException e) {
	        		// TODO 自動生成された catch ブロック
	        		e.printStackTrace();
	               }

	               java.sql.Statement stmt = null;
	               try {
	        		stmt = con.createStatement();
	               } catch (SQLException e2) {
	        		// TODO 自動生成された catch ブロック
	        		e2.printStackTrace();
	               }

	               
	               
	               String commit = "UPDATE " +  table +" SET  note = '" + notetext + "' where id = " + tablecnt ;
	               
	               try {
					stmt.executeUpdate(commit);
	               } catch (SQLException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
	               }
	               
	               
				
	               
	               handler.post(new Runnable() {
           	    	@Override
                       public void run() {
	               
	               
           	    	 AlertDialog.Builder dlg;
                     dlg = new AlertDialog.Builder(Booknote.this);
                     dlg.setTitle("完了");
                     dlg.setMessage("書き込みました。再度書き込みで \n訂正できます");
                     dlg.setPositiveButton("Yes", null);
                     dlg.show();



                    }
                });

           	    		
           	    		
				
	            }
				
		                
		                
		                
		                
	                


	        })).start();
		                
		                
				
			}
			
			
			


			


	public View.OnClickListener notecommit = new View.OnClickListener() {

		public void onClick(View v) {



		
			
			 SpannableStringBuilder sb = (SpannableStringBuilder)Edit.getText();
		     notetext = sb.toString();

		     //入力判断
		     
		     if(notetext.equals("ノートを残せます") || notetext == null ){
		    	 
		   	  AlertDialog.Builder dlg;
              dlg = new AlertDialog.Builder(Booknote.this);
              dlg.setTitle("エラー");
              dlg.setMessage("空白、もしくは初期文字です");
              dlg.setPositiveButton("Yes", null);
              dlg.show();

		    	 
		    	 
		     }else{
		    	 
		    	 
		    	 notecommit();
		    	 
		    	 
		    	 
		    	 
		    	 
		     }
		     
		     






		}
	};







	public void EC() {

	



			 Intent intentEC = new Intent();
	            intentEC.setClassName(
	                    "com.koba.myshelf",
	                    "com.koba.myshelf.ECsite");

	            intentEC.putExtra("EC",ECURL);


	            startActivity(intentEC);



		}


	public void shelf () {

		


			 Intent intent = new Intent();
	            intent.setClassName(
	                    "com.koba.myshelf",
	                    "com.koba.myshelf.MainActivity");

	            startActivity(intent);

			finish();


		}




	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	

		  getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.booknote);
		Button btn = (Button) findViewById(R.id.notecommit);
	


		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);


		final ImageView IV = (ImageView)findViewById(R.id.imageView1);  //Bitmap


		  btn.setOnClickListener(notecommit);


		  Intent intent = getIntent();

		  table = intent.getStringExtra("table");
		  year = intent.getStringExtra("year");
		  ECURL = intent.getStringExtra("ECURL");
		  manufacture = intent.getStringExtra("manufacture");
		  picture = intent.getStringExtra("picture");
		  allmusic = intent.getStringExtra("allmusic");
		  director  = intent.getStringExtra("director");
		  //music = intent.getStringExtra("music");
		  summary = intent.getStringExtra("summary");
		  name = intent.getStringExtra("name");
		  artist = intent.getStringExtra("artist");
		  cast = intent.getStringExtra("cast");
		  title = intent.getStringExtra("title");
		  wikipedia = intent.getStringExtra("wikipedia");
		  author = intent.getStringExtra("author");
		  tablecnt = intent.getIntExtra("tablecnt",0);
		  
		  
	

		  
			Edit = (EditText) findViewById(R.id.EditText);
			   Edit.setOnClickListener(
		        		new View.OnClickListener() {

		                    public void onClick(View v) {
		                    	Edit.setMaxLines(3);
		            			Edit.setFocusable(true);
		            			Edit.setFocusableInTouchMode(true);
		            		    Edit.requestFocus();
		                    }
		            });
			
			
			   startrun start = new  startrun();
				 Thread childThread2 = new Thread(start);
				 childThread2.start();

				 try {
					childThread2.join();
				} catch (InterruptedException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
				 
		  
		
		  
			   if(rtbitmap == null){

					System.out.println("nullpo5");

				}else{

					IV.setImageBitmap(rtbitmap);

				}
            


		   TextView t1 = (TextView)findViewById(R.id.textView1);//title
		   TextView t3 = (TextView)findViewById(R.id.textView2);//年
		   TextView t4 = (TextView)findViewById(R.id.textView3);// 著者
		   TextView t5 = (TextView)findViewById(R.id.textView4);//
		  
		   TextView t7 = (TextView)findViewById(R.id.textView7);//summary
	


		    v1 = (ListView)findViewById(R.id.listView);
           
		    t1.setText(title);
		  
		    t4.setText("著者:"+ author);
		    t3.setText("出版日:"+ year);
		    t5.setText("出版元:" + manufacture);
		 
		    t7.setText(summary);
		   // t9.setText(brige.bnote);


		    search_vocabulary vo = new  search_vocabulary(title);
			 Thread childThread1 = new Thread(vo);
			 childThread1.start();

			 try {
				childThread1.join();
			} catch (InterruptedException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			 
	
			 
			 
			 
			 
			 v1.setAdapter(arrayAdapter);
			 
			 
			 int totalHeight  = 0;
			 int desiredWidth = MeasureSpec.makeMeasureSpec(v1.getWidth(), MeasureSpec.AT_MOST);
			 for (int i = 0; i < arrayAdapter.getCount(); i++) {
				   View listItem = arrayAdapter.getView(i, null, v1);
				   listItem.measure(desiredWidth , MeasureSpec.UNSPECIFIED);
				  totalHeight = totalHeight +  75;
			}

			// 実際のListViewに反映する
			 ViewGroup.LayoutParams params = v1.getLayoutParams();
			 
			 System.out.println("v1.getDividerHeight()" + v1.getDividerHeight());
			 System.out.println("totalHeight" + totalHeight);
			 params.height = totalHeight + (v1.getDividerHeight() * (arrayAdapter.getCount() - 1));
			 v1.setLayoutParams(params);
			 v1.requestLayout();
			 
			 

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.itemactionbar, menu);
		getActionBar().setTitle("Book Item");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
	
		  switch (item.getItemId()) {
	        case R.id.button2:
	        	EC();
	            break;
	        case R.id.lobt:
	        	shelf();
	        	break;
	        }

		
		return super.onOptionsItemSelected(item);
	}


	
	
	
	
	
	 @Override
	  public boolean onTouchEvent(MotionEvent event) {

	  // キーボードを隠す
	  inputMethodManager.hideSoftInputFromWindow(mainLayout.getWindowToken(),
	  InputMethodManager.HIDE_NOT_ALWAYS);
	  // 背景にフォーカスを移す
	  mainLayout.requestFocus();

	  return true;

	  }
	  

	 
	 class startrun extends Thread{
		 
		 
		 
		 
	            public void run() {


	            	try {
	            		System.out.println(picture);
						url = new URL(picture);
					} catch (MalformedURLException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}
	 				//入力ストリームを開く
					System.out.println("nullpo2");

					try {
						istream = url.openStream();
					} catch (IOException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}

	 				//画像を取得
					rtbitmap= BitmapFactory.decodeStream(istream);
					try {
						istream.close();
					} catch (IOException e1) {
						// TODO 自動生成された catch ブロック
						e1.printStackTrace();
					}

					if(rtbitmap == null){

						System.out.println("nullpo3");

					}
					try {
						istream.close();
					} catch (IOException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}




	            }



		  
		 
		 
	 }
	 

		class search_vocabulary extends Thread{


			int all = 0;
			ResultSet rs = null;
			
			String vocabulary = null;
			
			search_vocabulary(String n){
				
				vocabulary = n;
				
			}
			
			
			
			   ArrayList<String> tuikatext = new ArrayList<String>();



	         public void run() {

	        	

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
						        tuid.add(rs.getInt("uid"));
						        System.out.println("い" +  rs.getInt("uid"));
						        screenname.add("@"+rs.getString("screenname")+"さんの本棚");
						        System.out.println("い" + rs.getString("screenname"));
						        item_name.add(rs.getString("item_name"));
						        System.out.println("い" + rs.getString("item_name"));
						    
							
						        
						        
						        
						        String usr =  "select * from all_user where uid = " + rs.getInt("uid");
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
									  name2.add(res.getString("name"));
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
	                
	                    arrayAdapter = new ArrayAdapter<String>(Booknote.this, R.layout.smalltext , R.id.textView1, tuikatext);

	                }

	         	}






		}






		class search_artist extends Thread{

			String Artist = null;
			
			
			search_artist(String n){
				
				Artist = n;
				
			}

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
				//		    if (!Arrays.asList(tuid).contains(rs.getInt("uid"))) {

					        tid.add( rs.getInt("id"));

					        tuid.clear();
					        tid.clear();
					        screenname.clear();
					        item_name.clear();



					        System.out.println("あ" +  rs.getInt("id"));
					        tuid.add(rs.getInt("uid"));
					        System.out.println("い" +  rs.getInt("uid"));

					        screenname.add("@"+rs.getString("screenname")+"さんの本棚");
					        System.out.println("い" + rs.getString("screenname"));
					        item_name.add(rs.getString("item_name"));
					        System.out.println("い" + rs.getString("item_name"));

						    	num++;
						    	
						    	
						        String usr =  "select * from all_user where uid = " + rs.getInt("uid");
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
									  name2.add(res.getString("name"));
								}
								   
								    	
								    	tuikatext.add("@"+rs.getString("screenname")+"さんの本棚" + "  title:" + rs.getString("item_name"));
								    	
								
					//	    }


						}
					} catch (SQLException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}

	             
	             if(item_name != null){
	            	 
	             arrayAdapter = new ArrayAdapter<String>(Booknote.this, R.layout.smalltext , R.id.textView1, tuikatext);

	             }


	         	}



		}

	
	 
	 
	 

}
