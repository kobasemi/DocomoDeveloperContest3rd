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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Pattern;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class BookPub extends Activity{



	String DBPASS = Pass.DBPASS;
	String PATH = Pass.PATH;
	String USER = Pass.USER;
	String OAUTH = Pass.OAUTH;
	String SECRET = Pass.SECRET;

    ArrayList<HashMap<String,String>> outputArray = new ArrayList<HashMap<String,String>>();

    String other_table_name = null;
	String other_name = null;
	  ListView listView = null;
    // 画像表示用に作成したCustomAdapterに、上記ArrayListを設定
   SimpleAdapter myAdapter =null;
	 twitter4j.User user = null;
	// 背景のレイアウト
	String names = null;
	String commenttable = null;

	String title = null;
	Long uid = 4l;

	InputMethodManager inputMethodManager;
  
	private LinearLayout mainLayout;

//	int  tablecnt = 0;
	String notetext = null;

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
	String  note = null;
	String author = null;
	String c_screen_name = null;
	Connection con = null;
	Bitmap bitmap = null;
	java.sql.Connection  con2 = null;



	public void EC() {

	



			 Intent intentEC = new Intent();
	            intentEC.setClassName(
	                    "com.koba.myshelf",
	                    "com.koba.myshelf.ECsite");

	            intentEC.putExtra("EC",ECURL);


	            startActivity(intentEC);



		}


	public void shelf () {
		
			finish();

		}





	public View.OnClickListener on5 = new View.OnClickListener() {

		public void onClick(View v) {


			//コメント投稿

		    commentcommit incm = new commentcommit();
				 Thread child = new Thread(incm);
				 child.start();


				 try {
					child.join();
				} catch (InterruptedException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
			    
			    

			
			
			

		    commentload cm = new commentload();
				 Thread childThread = new Thread(cm);
				 childThread.start();


				 try {
					childThread.join();
				} catch (InterruptedException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
			    
			    




		}
	};

	public void reload() {
	    Intent intent = getIntent();
	    overridePendingTransition(0, 0);
	    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	    finish();

	    overridePendingTransition(0, 0);
	    startActivity(intent);
	}


	class commentload implements Runnable{

	        public void run() {


	         ArrayList<String> comment = new ArrayList<String>();
	   		 ArrayList<String> iconURL = new ArrayList<String>();
	   		 ArrayList<String> name = new ArrayList<String>();

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
              java.sql.Statement stmt = null;

 		    PreparedStatement ps = null;


              String  commentselect= "select * from " + commenttable;
              System.out.println(commentselect);
                 // ステートメントオブジェクトを生成
                 try {
						ps = con.prepareStatement(commentselect);
					} catch (SQLException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}

                 // クエリーを実行して結果セットを取得
                 ResultSet rs = null;
					try {
						rs = ps.executeQuery();
					} catch (SQLException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}

                 // 検索された行数分ループ

					 System.out.println("getcomment");

					try {



						if(!rs.wasNull()){

							//コメントテーブルにコメントがあるかないか
							 System.out.println("get?comment");

						try {
							while(rs.next()) {

							    // nameデータを取得
							     comment.add(rs.getString("comment"));
							     iconURL.add(rs.getString("iconURL"));
							     name.add(rs.getString("name") + "さん        ");

							}
						} catch (SQLException e) {
							// TODO 自動生成された catch ブロック
							e.printStackTrace();
						}
						




 outputArray = new ArrayList<HashMap<String,String>>();

      for( int i = 0; i < comment.size(); i++ ) {
       HashMap<String, String> item = new HashMap<String, String>();
						// 画像の設定（とりあえず全ての項目に同じ画像を入れています。）
       item.put("iconKey", name.get(i));
						// 文字列の設定（とりあえず、ループのカウンタを表示させています。）
       item.put("textKey",comment.get(i));
        // 表示用のArrayListに設定
       outputArray.add(item);
      }




      // 画像表示用に作成したCustomAdapterに、上記ArrayListを設定
      myAdapter = new SimpleAdapter(
						    BookPub.this,
						    outputArray,
						     R.layout.twitline,  // ここがポイント２
						   new String[]{"iconKey","textKey"}, // ここがポイント３－１
						     new int[]{R.id.textView2,R.id.textView} // ここがポイント３－２
      );

      handler.post(new Runnable() {
	    	@Override
          public void run() {

      // ListViewにmyAdapterをセット
      ListView listView = (ListView)findViewById(R.id.listView1);
      listView.setAdapter(myAdapter);


      int totalHeight  = 0;
     int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
     for (int i = 0; i < myAdapter.getCount(); i++) {
  	   View listItem = myAdapter.getView(i, null, listView);
  	   listItem.measure(desiredWidth , MeasureSpec.UNSPECIFIED);
  	  totalHeight = totalHeight +  listItem.getMeasuredHeight();
   }

   // 実際のListViewに反映する
     ViewGroup.LayoutParams params = listView.getLayoutParams();
     params.height = totalHeight + (listView.getDividerHeight() * (myAdapter.getCount() - 1));
     listView.setLayoutParams(params);
     listView.requestLayout();




            }
        });




						}
					} catch (SQLException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					} 


			}
		

	            
	}


	  
	  class commentcommit implements Runnable  {
		  
		  


	            public void run() {

	    			EditText edit = (EditText)findViewById(R.id.EditText);
	    		    SpannableStringBuilder sb = (SpannableStringBuilder)edit.getText();
	    	        final String str = sb.toString();


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
          java.sql.Statement stmt = null;
			try {
				stmt = con.createStatement();
			} catch (SQLException e3) {
				// TODO 自動生成された catch ブロック
				e3.printStackTrace();
			}

          PreparedStatement ps = null;



          ///twitter投稿

          SharedPreferences pref = getSharedPreferences("pref",MODE_PRIVATE);
          

  			String pretoken = pref.getString("token", null);	
          
          String sql = "select * from all_user where token = '" + pretoken + "'";

          // ステートメントオブジェクトを生成
          try {
			ps = con.prepareStatement(sql);
		} catch (SQLException e2) {
			// TODO 自動生成された catch ブロック
			e2.printStackTrace();
		}
          ResultSet rs4 = null;
          // クエリーを実行して結果セットを取得
           try {
			rs4 = ps.executeQuery();
		} catch (SQLException e2) {
			// TODO 自動生成された catch ブロック
			e2.printStackTrace();
		}

          // 検索された行数分ループ


          try {
			while(rs4.next()) {
			       names = rs4.getString("screenname");
			  }
		} catch (SQLException e2) {
			// TODO 自動生成された catch ブロック
			e2.printStackTrace();
		}
          

       // 設定ファイルからoauth_tokenとoauth_token_secretを取得してTwitter投稿の準備。
       String oauthToken = pref.getString("token", null);
       String oauthTokenSecret = pref.getString("tokensecret", null);

       ConfigurationBuilder confbuilder = new ConfigurationBuilder();

       confbuilder.setOAuthAccessToken(oauthToken)
       .setOAuthAccessTokenSecret(oauthTokenSecret)
       .setOAuthConsumerKey(OAUTH)
       .setOAuthConsumerSecret(SECRET);

       
       
       

       Twit Twitteruptext =new Twit(oauthToken,oauthTokenSecret);
  
       Twitteruptext.TweetUp("@"+c_screen_name+" さんの本棚アイテム["+title+"]にコメントを付けました。"+ str);


       System.out.println(oauthToken);
       System.out.println(oauthTokenSecret);



       AccessToken at2 = new AccessToken(oauthToken, oauthTokenSecret);


       Twitter twitter = new TwitterFactory(confbuilder.build()).getInstance(at2);
       twitter.setOAuthAccessToken(at2);


       try {
			user = twitter.verifyCredentials();
			} catch (TwitterException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			}

       String geticon = user.getProfileImageURL();

  

       	  String URL = "https://www.google.co.jp/";
          String maxcntsql = "select max(id) from " + commenttable ;

          System.out.println(maxcntsql);
          ResultSet rs = null;
          try {
       	   rs = stmt.executeQuery(maxcntsql);
   		} catch (SQLException e) {
   			// TODO 自動生成された catch ブロック
   			 System.out.println("aaa");
   			e.printStackTrace();
   		}


   		int max = 0;

   	   try {
   		while(rs.next()) {


   			  max = rs.getInt("max(id)");


   		   }
   	   } catch (SQLException e1) {
   		// TODO 自動生成された catch ブロック
   		   e1.printStackTrace();
   	   }

   	   int newcnt = max+1;
   	   Long	nowDate =  Datert();

          String commentInsert = "INSERT INTO " + commenttable + "(id,uid,comment,twitter,iconURL,name,nowDate,opt)"
          		+ "VALUES("+ newcnt + "," + uid + ",'"+ str + "','"+ URL + "','"+ geticon + "','"+ names + "',"+ nowDate + ",'1')";

      		try {
					int num = stmt.executeUpdate(commentInsert);
				} catch (SQLException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}

      		
            handler.post(new Runnable() {
       	    	@Override
                   public void run() {
	               
	               
       	    	 AlertDialog.Builder dlg;
                 dlg = new AlertDialog.Builder(BookPub.this);
                 dlg.setTitle("完了");
                 dlg.setMessage("書き込みました。");
                 dlg.setPositiveButton("Yes", null);
                 dlg.show();



                }
            });

      		
      		
      		
			}
	  }
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		  getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

		setContentView(R.layout.bookpub);

		Button btn2 = (Button) findViewById(R.id.button2);
		Button btn3 = (Button) findViewById(R.id.lobt);
		Button btn4 = (Button) findViewById(R.id.commentcommit);


		 listView = (ListView)findViewById(R.id.listView1);

			
		btn4.setOnClickListener(on5);


		final ImageView IV = (ImageView)findViewById(R.id.imageView1);  //Bitmap


		   listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	            public void onItemClick(AdapterView<?> parent,
	                                    View view, int pos, long id) {

	            	
					all_user_serch aus = new all_user_serch();
					 Thread childThread = new Thread(aus);
					 childThread.start();


					 try {
						childThread.join();
					} catch (InterruptedException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}
					
					Intent intents = new Intent();
			            intents.setClassName(
			                    "com.koba.myshelf",
			                    "com.koba.myshelf.OtherShelf");

			            intents.putExtra("table_name", other_table_name);
			            intents.putExtra("name", other_name);
			            startActivity(intents);


	            }
	        });

	


		  Intent intent = getIntent();
		  uid	= intent.getLongExtra("uid",4l);
		  c_screen_name  = intent.getStringExtra("c_screen_name");
		  note = intent.getStringExtra("note");
		  title = intent.getStringExtra("title");
		  table = intent.getStringExtra("table");
		  year = intent.getStringExtra("year");
		  ECURL = intent.getStringExtra("EC");
		  manufacture = intent.getStringExtra("manufacture");
		  picture = intent.getStringExtra("picture");
		  summary = intent.getStringExtra("summary");
		 // alubam = intent.getStringExtra("alubam");
		  //music = intent.getStringExtra("music");
		  summary = intent.getStringExtra("summary");
	//	  names = intent.getStringExtra("name");
		  author = intent.getStringExtra("author");

		  commenttable = intent.getStringExtra("commenttable");

		  System.out.println("commenttable :" + commenttable);
		 // tablecnt = intent.getIntExtra("tablecnt",0);


		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);



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




		  if( allmusic != null){

		   Pattern pattern = Pattern.compile("/");
			String[] ss = pattern.split(allmusic, 0); //「:」で分割

			int counter = 0;
			for(String s:ss){

				if(counter == 0){}else{

					//分割を表示　後でlistviewに入れる


				System.out.println(s);
				}

				counter++;

			}
		  }

		   TextView t1 = (TextView)findViewById(R.id.textView1);//title
		   TextView t3 = (TextView)findViewById(R.id.textView2);//年
		   TextView t4 = (TextView)findViewById(R.id.textView3);// artist
		   TextView t5 = (TextView)findViewById(R.id.textView4);//販売元
		   TextView t01 = (TextView)findViewById(R.id.TextView01);//挿入歌
		   TextView t7 = (TextView)findViewById(R.id.textView7);//summary
		 //  TextView t9 = (TextView)findViewById(R.id.TextView01);//Note




		    t1.setText(title);
		    t3.setText("発売日:" + year );
		    t4.setText("著者" +  author );
		    t5.setText("出版社" + manufacture);
		    t7.setText(summary);
		    t01.setText(note);

			

		    commentload cm = new commentload();
				 Thread childThread = new Thread(cm);
				 childThread.start();


				 try {
					childThread.join();
				} catch (InterruptedException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
			    
			    


		   (new Thread(new Runnable() {
	            @Override
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




				    handler.post(new Runnable() {
           	    	@Override
                       public void run() {
           	    		if(rtbitmap == null){

           					System.out.println("nullpo5");

           				}

           			IV.setImageBitmap(rtbitmap);


                       }
                   });



	            }


	            })).start();



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




		public Long Datert(){

				Calendar timeSet = Calendar.getInstance();
			    int second = 0;
			    SimpleDateFormat format0 = new SimpleDateFormat("ss");
			    String ssFormat = format0.format( timeSet.getTime());
			    SimpleDateFormat format1 = new SimpleDateFormat("mm");
			    String mmFormat = format1.format( timeSet.getTime() );
			    SimpleDateFormat format2 = new SimpleDateFormat("HH");
			    String hhFormat = format2.format( timeSet.getTime() );
			    SimpleDateFormat format3 = new SimpleDateFormat("dd");
			    String ddFormat = format3.format( timeSet.getTime() );
			    SimpleDateFormat format4 = new SimpleDateFormat("MM");
			    String MFormat = format4.format( timeSet.getTime() );
			    SimpleDateFormat format5 = new SimpleDateFormat("yyyy");
			    String yyyyFormat = format5.format( timeSet.getTime() );

			long nowDate = Long.parseLong(yyyyFormat + MFormat +ddFormat  +hhFormat + mmFormat + ssFormat) ;

			return nowDate;
		}

		  class all_user_serch  implements Runnable{

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
		         java.sql.Statement stmt = null;
		       	try {
		       		stmt = con.createStatement();
		       	} catch (SQLException e3) {
		       		// TODO 自動生成された catch ブロック
		       		e3.printStackTrace();
		       	}
		       	
		       	
		        ResultSet rs =null;

		        String otherquery = "select * from all_user where uid = '"+  uid +"'";
		        System.out.println("BookPub" + uid);
		   
		        try {
		      	   rs = stmt.executeQuery(otherquery);
		      	} catch (SQLException e) {
		      		// TODO 自動生成された catch ブロック
		      		 System.out.println("aaa");
		      		e.printStackTrace();
		      	}
		        
		        
		        
					try {
						while(rs.next()) {
						        other_table_name=  rs.getString("table_name");
						        other_name =  rs.getString("screenname");
						}
					} catch (SQLException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}


		        }

	  }





}
