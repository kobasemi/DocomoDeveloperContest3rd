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
import java.util.List;

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
import android.content.pm.ResolveInfo;
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
<<<<<<< HEAD
import android.widget.AdapterView;
=======
>>>>>>> parent of 80fff1a... ver 1.2.9
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeIntents;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;

public class Pub extends Activity {


	
	String DBPASS = Pass.DBPASS;
	String PATH = Pass.PATH;
	String USER = Pass.USER;
	String OAUTH = Pass.OAUTH;
	String SECRET = Pass.SECRET;
	
	
	
	
	twitter4j.User user = null;
	InputMethodManager inputMethodManager;
	// 背景のレイアウト
	private LinearLayout mainLayout;
	String names = null;
	String commenttable = null;
	Bitmap rtbitmap = null;
	InputStream istream = null;
	URL url = null;
    final Handler handler=new Handler();
    String ECURL = null;
    String title = null;
    int uid = 0;
	String c_screen_name = null;
    
	Connection con = null;
    Bitmap bitmap = null;
    java.sql.Connection  con2 = null;
    String thumbnail = null;
    URL thumbnailURL = null;
<<<<<<< HEAD

    String wikipedia = null;
    ListView listView = null;
    ArrayList<HashMap<String,String>> outputArray = new ArrayList<HashMap<String,String>>();

    

    // 画像表示用に作成したCustomAdapterに、上記ArrayListを設定
   SimpleAdapter myAdapter =null;
=======
>>>>>>> parent of 80fff1a... ver 1.2.9
    private Button playVideoButton;
	/** Global instance of the HTTP transport. */
	private final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	/** Global instance of the JSON factory. */
	private final JsonFactory JSON_FACTORY = new JacksonFactory();
	/** Global instance of Youtube object to make all API requests. */
	private YouTube youtube;
	public static final String DEVELOPER_KEY = Pass.DEVELOPER_KEY; //APIkey，自分の入れてください

	private static final int REQ_START_STANDALONE_PLAYER = 1;
	private static final int REQ_RESOLVE_SERVICE_MISSING = 2;

	public String video_ID; //videoID
	public String queryTerm="進撃の巨人"; //動画検索用文字列 ここに入れた文字列がyoutubeで検索される
	

	
	
	
	
	public void youtubethum(){


		Thread th;

		(th = new Thread(new Runnable() { //新しいスレッド(thスレッド)の作成
			@Override
			public void run() {
           //ここから動画検索
				try {
					//おまじない
					youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
					public void initialize(HttpRequest request) throws IOException {
					}
						}).setApplicationName("com.example.youtube").build();

				
					
					thumbnail = "http://img.youtube.com/vi/"+ video_ID + "/0.jpg";


					System.out.println(thumbnail);
					thumbnailURL = new URL(thumbnail);
					istream =thumbnailURL.openStream();

	 				//画像を取得
	 				bitmap= BitmapFactory.decodeStream(istream);
	 				if(bitmap == null){

	 					System.out.println("ccccc");

	 				}else{

	 					System.out.println("dddddddd");


	 				}

	 				istream.close();



	 				  handler.post(new Runnable() {
	            	    	@Override
	                        public void run() {

	            	    	ImageView youtubeview=(ImageView) findViewById(R.id.youtubeview);

	            	    	 youtubeview.setOnClickListener(on4);

	            	    	youtubeview.setImageBitmap(bitmap);




	                        }
	                    });

					

					System.out.println(video_ID);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		})).start();//←thスレッド終わり






	}







	public View.OnClickListener on5 = new View.OnClickListener() {

		public void onClick(View v) {



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



	public View.OnClickListener on4 = new View.OnClickListener() {

		public void onClick(View v) {
			System.out.println("xxxxxxxxxxxxxxx");


			int startTimeMillis = 0; //動画開始位置
		    boolean autoplay = true; //自動再生するかどうか
		    boolean lightboxMode = true; //全画面の動画再生にしないかどうか

		    Intent youtubeintent = null;

		    	youtubeintent = YouTubeIntents.createPlayVideoIntentWithOptions(
		    		  Pub.this ,video_ID,false,false);

		    	startActivity(youtubeintent);
		    if (youtubeintent != null) {




		      if (canResolveIntent(youtubeintent)) {
		        startActivityForResult(youtubeintent, REQ_START_STANDALONE_PLAYER);
		      } else {
		        // Could not resolve the intent - must need to install or update the YouTube API service.
		        YouTubeInitializationResult.SERVICE_MISSING
		            .getErrorDialog(Pub.this, REQ_RESOLVE_SERVICE_MISSING).show();
		      }
		    }


		}
	};

	
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
					




<<<<<<< HEAD
					myAdapter = new SimpleAdapter(
							    Pub.this,
							    outputArray,
							     R.layout.twitline,  // ここがポイント２
							   new String[]{"iconKey","textKey"}, // ここがポイント３－１
							     new int[]{R.id.textView2,R.id.textView} // ここがポイント３－２
							   );

			      for( int i = 0; i < comment.size(); i++ ) {
			       HashMap<String, String> item = new HashMap<String, String>();
									// 画像の設定（とりあえず全ての項目に同じ画像を入れています。）
			       item.put("iconKey", name.get(i));
									// 文字列の設定（とりあえず、ループのカウンタを表示させています。）
			       item.put("textKey",comment.get(i));
			        // 表示用のArrayListに設定
			       outputArray.add(item);
			      }
=======
    ArrayList<HashMap<String,String>> outputArray = new ArrayList<HashMap<String,String>>();
>>>>>>> parent of 80fff1a... ver 1.2.9

  for( int i = 0; i < comment.size(); i++ ) {
   HashMap<String, String> item = new HashMap<String, String>();
					// 画像の設定（とりあえず全ての項目に同じ画像を入れています。）
   item.put("iconKey", name.get(i));
					// 文字列の設定（とりあえず、ループのカウンタを表示させています。）
   item.put("textKey",comment.get(i));
    // 表示用のArrayListに設定
   outputArray.add(item);
  }



<<<<<<< HEAD
			      handler.post(new Runnable() {
				    	@Override
			          public void run() {

			      // ListViewにmyAdapterをセット
			    
			      listView.setAdapter(myAdapter);

			      int totalHeight  = 0;
			      int simpleline = 50;
			     int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
			     for (int i = 0; i < myAdapter.getCount(); i++) {
			  	   View listItem = myAdapter.getView(i, null, listView);
			  	   listItem.measure(desiredWidth , MeasureSpec.UNSPECIFIED);
			  	   totalHeight = totalHeight +  simpleline;
			   }
=======

  // 画像表示用に作成したCustomAdapterに、上記ArrayListを設定
 final SimpleAdapter myAdapter = new SimpleAdapter(
					    Pub.this,
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

>>>>>>> parent of 80fff1a... ver 1.2.9

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
		String pretokensecret = pref.getString("tokensecret", null);	
		  
	       Twit Twitteruptext =new Twit(pretoken,pretokensecret);
	  
	       Twitteruptext.TweetUp("@"+c_screen_name+" さんの本棚アイテム["+title+"]にコメントを付けました。"+ str);

		
		
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
         dlg = new AlertDialog.Builder(Pub.this);
         dlg.setTitle("完了");
         dlg.setMessage("書き込みました。");
         dlg.setPositiveButton("Yes", null);
         dlg.show();



        }
    });

		
		
		
	}
}




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




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		  getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

		setContentView(R.layout.pub);
<<<<<<< HEAD
		 listView = (ListView)findViewById(R.id.listView1);

=======
		
		
>>>>>>> parent of 80fff1a... ver 1.2.9
		 final EditText editer = (EditText)findViewById(R.id.EditText);
		    editer.setOnClickListener(
	        		new View.OnClickListener() {

	                    public void onClick(View v) {
	                    	 editer.setMaxLines(3);
	            			editer.setFocusable(true);
	            			editer.setFocusableInTouchMode(true);
	            		    editer.requestFocus();
	                    }
	            });
	inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	 mainLayout = (LinearLayout)findViewById(R.id.LinearLayout1);


	 

	 Button commentbt = (Button)findViewById(R.id.commentcommit);
	 commentbt.setOnClickListener(on5);


	

		final ImageView IV = (ImageView)findViewById(R.id.imageView1);  //Bitmap
	    TextView t1 = (TextView)findViewById(R.id.textView1);//title
	    TextView t3 = (TextView)findViewById(R.id.textView2);//年
	    TextView t4 = (TextView)findViewById(R.id.textView3);//監督
	    TextView t5 = (TextView)findViewById(R.id.textView4);//出演者
	    TextView t6 = (TextView)findViewById(R.id.textView5);//挿入歌
	    TextView t7 = (TextView)findViewById(R.id.textView7);//summary
	    TextView t9 = (TextView)findViewById(R.id.TextView01);//Note
	    final ImageView imageView = (ImageView)findViewById(R.id.youtubeview);

	    imageView.setOnClickListener(on4);


	    Intent intent = getIntent();
		// intentから指定キーの文字列を取得する
		 title = intent.getStringExtra("title");
		String year = intent.getStringExtra("year");
<<<<<<< HEAD
		
		wikipedia  = intent.getStringExtra("wikipedia");
		c_screen_name  = intent.getStringExtra("c_screen_name");
=======


		  c_screen_name  = intent.getStringExtra("c_screen_name");
>>>>>>> parent of 80fff1a... ver 1.2.9
		String director = intent.getStringExtra("director");
		String cast = intent.getStringExtra("cast");
		String music = intent.getStringExtra("music");
		String summary = intent.getStringExtra("summary");
		String note = intent.getStringExtra("note");
		String variery = intent.getStringExtra("variety");
		commenttable	 = intent.getStringExtra("commenttable");
<<<<<<< HEAD
		uid	= intent.getLongExtra("uid",4l);
	//	names = intent.getStringExtra("name");
		video_ID = intent.getStringExtra("video_ID");
		
	  	
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
	      

=======
		uid	= intent.getIntExtra("uid",0);
	//	names = intent.getStringExtra("name");
		video_ID = intent.getStringExtra("video_ID");
		
		
		
>>>>>>> parent of 80fff1a... ver 1.2.9
		youtubethum();

		ECURL = intent.getStringExtra("EC");
		final String picture = intent.getStringExtra("picture");
		System.out.println(picture);


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


	    t1.setText(title);
	    t3.setText("公開年度:" + year + "年");
	    t4.setText("監督:" + director);
	    t5.setText("出演者" + cast);
	    t6.setText("挿入歌" + music);
	    t7.setText(summary);
	    t9.setText(note);

		

	    commentload cm = new commentload();
			 Thread childThread = new Thread(cm);
			 childThread.start();


			 try {
				childThread.join();
			} catch (InterruptedException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		    
		
	



			Thread th;

			(th = new Thread(new Runnable() { //新しいスレッド(thスレッド)の作成
				@Override
				public void run() {
	           //ここから動画検索
					try {
						//おまじない
						youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
						public void initialize(HttpRequest request) throws IOException {
						}
							}).setApplicationName("com.example.youtube").build();

					
						
						thumbnail = "http://img.youtube.com/vi/"+ video_ID + "/0.jpg";


						System.out.println(thumbnail);
						thumbnailURL = new URL(thumbnail);
						istream =thumbnailURL.openStream();

		 				//画像を取得
		 				bitmap= BitmapFactory.decodeStream(istream);
		 				if(bitmap == null){

		 					System.out.println("ccccc");

		 				}else{

		 					System.out.println("dddddddd");


		 				}

		 				istream.close();



		 				  handler.post(new Runnable() {
		            	    	@Override
		                        public void run() {

		            	    	ImageView youtubeview=(ImageView) findViewById(R.id.youtubeview);

		            	    	 youtubeview.setOnClickListener(on4);

		            	    	youtubeview.setImageBitmap(bitmap);




		                        }
		                    });

						

						System.out.println(video_ID);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			})).start();//←thスレッド終わり






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



	  private boolean canResolveIntent(Intent youtubeintent) {
	    List<ResolveInfo> resolveInfo = getPackageManager().queryIntentActivities(youtubeintent, 0);
	    return resolveInfo != null && !resolveInfo.isEmpty();
	  }



<<<<<<< HEAD
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




=======
>>>>>>> parent of 80fff1a... ver 1.2.9
	  @Override
	  public boolean onTouchEvent(MotionEvent event) {

	  // キーボードを隠す
	  inputMethodManager.hideSoftInputFromWindow(mainLayout.getWindowToken(),
	  InputMethodManager.HIDE_NOT_ALWAYS);
	  // 背景にフォーカスを移す
	  mainLayout.requestFocus();

	  return true;

	  }
	  

	
	  
	  
	  

}
