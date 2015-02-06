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

import twitter4j.TwitterException;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationContext;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

	
	
	String DBPASS = Pass.DBPASS;
	String PATH = Pass.PATH;
	String USER = Pass.USER;
	String OAUTH = Pass.OAUTH;
	String SECRET = Pass.SECRET;
	
	
	
	// con = DriverManager.getConnection(PATH,USER,DBPASS);
	private ActionBarDrawerToggle mDrawerToggle;
	private DrawerLayout mDrawer;
	
	
	public Connection con = null;
    public PreparedStatement ps = null;


    final Handler handler=new Handler();
<<<<<<< HEAD

    int  height = 0;
    int width = 0;
=======
    
    
>>>>>>> parent of 80fff1a... ver 1.2.9
    String screen_name= null;
    String token =null;
    String tokensecret = null;
    Long getuid = null;
    String name = null;
    String screenname = null;
    String tablename = null;
    String friendname = null;
    String groupname =null;
    String favo_table =null;
    String pretoken = null;

    public static RequestToken _req = null;
    public static OAuthAuthorization _oauth = null;


    	
     ArrayList<Integer> tid = new ArrayList<Integer>();
     ArrayList<Long> tuid = new ArrayList<Long>();
     ArrayList<String> title = new ArrayList<String>();
     ArrayList<String> director = new ArrayList<String>();
     ArrayList<Integer> year = new ArrayList<Integer>();
     ArrayList<String> cast = new ArrayList<String>();
     ArrayList<String> music = new ArrayList<String>();
     ArrayList<String> summary = new ArrayList<String>();
     ArrayList<String> note = new ArrayList<String>();
     ArrayList<String> picture = new ArrayList<String>();
     ArrayList<String> variety = new ArrayList<String>();
     ArrayList<String> opt = new ArrayList<String>();
     ArrayList<String> EC = new ArrayList<String>();
     ArrayList<String> commenttable = new ArrayList<String>();
     ArrayList<String> manufactured = new ArrayList<String>();
     ArrayList<String> artist = new ArrayList<String>();
     ArrayList<String> author = new ArrayList<String>();
     ArrayList<String> wikipedia = new ArrayList<String>();
     ArrayList<String> video_id = new ArrayList<String>();
     
     Long uid = null;
     String table_name = null;

     Bitmap  bitmaplist[] = new Bitmap[0];
     String  titles[] = new String[0];

	
	
	

	public void loginpref(){

	 SharedPreferences pref = getSharedPreferences("pref", 0);



		String pretoken = pref.getString("token", null);


		System.out.println("asdfg");
		System.out.println("asdfg"+pretoken);
		if(pretoken == null){

			System.out.println("lkjh");
			 Oauth();

		}

	}



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		  getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_main);

        super.onCreate(savedInstanceState);
        
    	

        SharedPreferences pref = getSharedPreferences("pref", 0);
         pretoken = pref.getString("token", null);
<<<<<<< HEAD
      String   pretokensecret = pref.getString("tokensecret", null);
      
      Twit Twitteruptext =new Twit(pretoken,pretokensecret);
 
      Twitteruptext.TweetUp("upstatuテストです");
      
      
=======
         String   pretokensecret = pref.getString("tokensecret", null);


<<<<<<< HEAD
>>>>>>> 0d2bead... ver1.2.9.2
=======
>>>>>>> origin/master
>>>>>>> origin/master
        /*login施し*/

        System.out.println("lkjhg");



        loginpref();
        
     

    

        (new Thread(new Runnable() {
                @Override
                public void run() {

                	try{
                	  // ドライバクラスをロード
                    Class.forName("com.mysql.jdbc.Driver");

                    // データベースへ接続
         
                    con = DriverManager.getConnection(PATH,USER,DBPASS);

                    String sql = "select * from all_user where token = '" + pretoken + "'";

                    // ステートメントオブジェクトを生成
                    ps = con.prepareStatement(sql);

                    // クエリーを実行して結果セットを取得
                    ResultSet rs = ps.executeQuery();

                    // 検索された行数分ループ

                    // nameデータを取得
                    	Integer iid = null;

                    	String pass = null;

                    	String friend_table = null;
                    	String group_table = null;

                    while(rs.next()) {

                        // nameデータを取得
                         iid = rs.getInt("id");
                         uid = rs.getLong("uid");
                         name = rs.getString("name");
                         screen_name = rs.getString("screenname");

                         table_name = rs.getString("table_name");
                         friend_table = rs.getString("friend_table");
                         group_table = rs.getString("group_table");
                         favo_table = rs.getString("favo_table");
                         System.out.println("favo_table" + favo_table);
                         

                    }

                    String counts =  " select count(*) as cnt from "+ table_name;
                   	ps = con.prepareStatement(counts);
                   	ResultSet countres = ps.executeQuery();
                   	countres.next();
       				Integer count = countres.getInt("cnt");
       				System.out.println("aaaa"+ Integer.toString(count));

       	           final Bitmap  bitmaplist2[] = new Bitmap[count];
                   final String  titles2[] = new String[count];
                   final String  header2[] = new String[count];

                    String correction = "select * from "+ table_name;
                    ps = con.prepareStatement(correction);



                    // クエリーを実行して結果セットを取得
                    ResultSet res = ps.executeQuery();
                    Integer numi  = null;
                    numi = 0;

                    while(res.next()) {
                    	tid.add (res.getInt("id"));
                    	 System.out.println("tid = " +  res.getInt("id"));


                    	tuid.add(res.getLong("uid"));



                    	title.add(res.getString("title"));
                    	EC.add(res.getString("ECURL"));
                    	director.add(res.getString("director"));
                    	year.add(res.getInt("year"));
                    	cast.add(res.getString("cast"));
                    	music.add(res.getString("music"));
                    	summary.add(res.getString("summary"));
                    	note.add(res.getString("note"));
                    	picture.add(res.getString("picture"));
                    	variety.add(res.getString("variety"));
                    	opt.add(res.getString("opt"));
                    	manufactured.add(res.getString("manufactured"));
                    	author.add(res.getString("author"));
                    	wikipedia.add(res.getString("wikipedia"));
                    	commenttable.add(res.getString("commenttable"));
                    	System.out.println(res.getString("commenttable"));
                    	artist.add(res.getString("artists"));
                    	video_id.add(res.getString("video_id"));
                    	

            		 				URL url = null;
            		 			 	Bitmap bitmap = null;
            		 				System.out.println("nullpo1 " +res.getString("picture"));
            		 				url = new URL(res.getString("picture"));
            		 				//入力ストリームを開く
            						System.out.println("nullpo2");
            		 				InputStream istream = url.openStream();
            						System.out.println("nullpo3");
            		 				//画像を取得
            		 				bitmap= BitmapFactory.decodeStream(istream);
            		 				bitmaplist2[numi] =bitmap;
            		 				titles2[numi] =res.getString("title");
            		 				System.out.println(titles2[numi]);
            		 				 numi = numi + 1 ;
            		 				istream.close();

                    }
                    ////////////////////////////////////////////////




            	    handler.post(new Runnable() {
            	    	                	    	@Override
            	    	                            public void run() {
            	    	                   //         	TextView textView1=(TextView)findViewById(R.id.aaa);
            	    	                   //         	textView1.setText(iid);

            	    	                         /*ここにグリッドのメソッド呼び出し*/

            	    	                	    	    
/*
            	    	                	    	    GridView gridView = new GridView(getApplicationContext());
            	    	                	            gridView.setNumColumns(4);
            	    	                	            gridView.setVerticalSpacing(10);
            	    	                	            gridView.setHorizontalSpacing(10);
            	    	                	            setContentView(gridView);*/
<<<<<<< HEAD

            	    	                	    	    final GridView gv = (GridView)findViewById(R.id.gridView1);
            	    	                	    	    
            	    	                	   
            	    	                	    	    
            	    	                	 //   	    gv.setGravity(Gravity.CENTER);
    	    	                	    	            // 縦幅に合わせる
    	    	                	    	            height = gv.getHeight();
    	    	                	    	            width = gv.getWidth();
    	    	                	    	            System.out.println("height1" + height);

            	    	                	        System.out.println("height2" + height);
                  	    	                      //   creategrid(picture);
            	    	                	            
            	               		 			titles = titles2;
            	               		 			bitmaplist = bitmaplist2;
            	               		 			
            	               		 			
            	               		 		 
            	               		 	 
            	               		 			
            	               		 			
            	               		 			gv.setAdapter(new SimpleAdapter(getApplicationContext(),titles,bitmaplist,height, width));

            	               		 		
=======
            	    	                	    	    
            	    	                	    	    GridView gv = (GridView)findViewById(R.id.gridView1);



            	               		 	
            	               		 			titles = titles2;
            	               		 			bitmaplist = bitmaplist2;

            	               		 			gv.setAdapter(new SimpleAdapter(getApplicationContext(),titles,bitmaplist));
            	               		
            	               		 			
>>>>>>> parent of 80fff1a... ver 1.2.9
            	               		 			gv.setOnItemClickListener(new GridOnItemClick());
            	               		 			gv.setOnItemLongClickListener(new GridOnItemLongClick());
            	               		 			
            	               		 			
            	               		 	

            	    	                            }
            	    	                        });




                    }catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("55555555555555" );
                    } catch (ClassNotFoundException e) {
                    	  System.out.println("6666666666666666" );
                    e.printStackTrace();
                    } catch (MalformedURLException e1) {
						// TODO 自動生成された catch ブロック
                    	System.out.println("nullpo7");
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO 自動生成された catch ブロック
						System.out.println("nullpo8");
						e1.printStackTrace();
					} finally {
                        try {

                            // close処理
                            if(ps != null){
                                ps.close();
                            }

                            // close処理
                            if(con != null){
                                con.close();
                            }
                        } catch(SQLException e){
                            e.printStackTrace();
                        }
                    }

                }


        })).start();



        ((Button)findViewById(R.id.drawer_button)).setOnClickListener(opensearch);
        ((Button)findViewById(R.id.fv)).setOnClickListener(favoopen);
        ((Button)findViewById(R.id.fvshow)).setOnClickListener(favoshowopen);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer,R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {

	            System.out.println("Drawclose");

            }
     
            @Override
            public void onDrawerOpened(View drawerView) {
                System.out.println("Drawopen");
            }
     
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                // ActionBarDrawerToggleクラス内の同メソッドにてアイコンのアニメーションの処理をしている。
                // overrideするときは気を付けること。
                super.onDrawerSlide(drawerView, slideOffset);
                System.out.println("Drawslide"); 
            }
     
            @Override
            public void onDrawerStateChanged(int newState) {
                // 表示済み、閉じ済みの状態：0
                // ドラッグ中状態:1
                // ドラッグを放した後のアニメーション中：2
                System.out.println("Draw :" +newState);
            }
        };
     
        mDrawer.setDrawerListener(mDrawerToggle);
     
        // UpNavigationアイコン(アイコン横の<の部分)を有効に
        // NavigationDrawerではR.drawable.drawerで上書き
        getActionBar().setDisplayHomeAsUpEnabled(true);
        // UpNavigationを有効に
        getActionBar().setHomeButtonEnabled(true);




	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mainactionbar, menu);
		
		
		getActionBar().setTitle("This is Your Shelf");
		return super.onCreateOptionsMenu(menu);
	}



	//paperに渡す
	class Paperbrige {

		//タイトル
		private String btitle;
		private String bdirector;

		//作成日
		private String byear;



		private String bcast;

		//音楽です
		private String bmusic;

		//サマリーです
		private String bsummary;

		//ノートです。
		private String bnote;

		//bitmapイメージにて注意してください
		private String bpicture;

		//CD DVD book
		private String bvariety;

		//将来拡張
		private String bopt;

		// amazonやyahoo,rakutenなどのECURLです
		private String bECURL;
		
		private String wikipedia;
		private String manufactured;
		private String artists;
		private String author;
		private String  EAN8;
		private String  EAN13 ;
		private String  commenttable ;
		// private int nowDate;
	
		
		
		}



	public void camerabt() {

		

			 Intent intent = new Intent();
	            intent.setClassName(
	                    "com.koba.myshelf",
	                    "com.koba.myshelf.Camera");

	            intent.putExtra("table",table_name);


	            System.out.println("tablenamename=" + table_name);

	            intent.putExtra("uid",Long.toString(uid));
	            startActivity(intent);

	}



	public class GridOnItemLongClick implements OnItemLongClickListener{
		@Override
		 public boolean onItemLongClick(AdapterView<?> parent, View v, final int pos, long id) {
			
			
			//長押し削除機能
			
			new AlertDialog.Builder(MainActivity.this)
	        .setTitle("長押しが実行されました")
	        .setMessage("この項目を削除しますか？")
	        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                
	            	
	            	//削除クラスの呼び出し
	            	
	            	
	            	 ItemClear db = new ItemClear(table_name,tid.get(pos));
					 Thread childThread = new Thread( db);
					 childThread.start();


					 try {
						childThread.join();
					} catch (InterruptedException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}
	            	
					 Intent refresh = new Intent(MainActivity.this, MainActivity.class);
					 startActivity(refresh);
					
					 finish();
					 
	            	
	            	
	            }
	        })
	        .setNegativeButton("Cancel", null)
	        .show();
			
			return true;
		}
	}
	
	
	
	
	
	
	
	
	


	public class GridOnItemClick implements OnItemClickListener {
		@Override
	public void onItemClick(AdapterView parent, View v, int position, long id) {



			/*呼び出すぞ！！！！*/

			Paperbrige brige = new Paperbrige();

			System.out.println(titles[position]);
		
			brige.bvariety = variety.get(position);
		

			//varietyで飛ばすintentが違う
			
			
			if(brige.bvariety.equals("DVD")){
				
				
				brige.btitle =titles[position];
				brige.byear = Integer.toString(year.get(position));
				brige.bdirector = director.get(position);
				brige.bcast = cast.get(position);
				brige.bmusic = music.get(position);
				brige.bsummary = summary.get(position);
				brige.bnote = note.get(position);
				brige.bpicture =  picture.get(position);
				brige.bvariety = variety.get(position);
				brige.bECURL = EC.get(position);
				brige.wikipedia = wikipedia.get(position);
				brige.manufactured = manufactured.get(position);
				brige.commenttable = commenttable.get(position);
				
				
				
				 Intent intents = new Intent();
		            intents.setClassName(
		                    "com.koba.myshelf",
		                    "com.koba.myshelf.Pub");
		            intents.putExtra("c_screen_name",screen_name);
		            intents.putExtra("name",name);
		            intents.putExtra("title",brige.btitle);
		            intents.putExtra("year",brige.byear);
		            intents.putExtra("director",brige.bdirector);
		            intents.putExtra("cast",brige.bcast );
		            intents.putExtra("music",brige.bmusic);
		            intents.putExtra("summary",brige.bsummary);
		            intents.putExtra("note",brige.bnote);
		            intents.putExtra("variety",brige.bvariety);
		            intents.putExtra("picture",brige.bpicture);
		            intents.putExtra("EC",brige.bECURL);
		            intents.putExtra("commenttable",brige.commenttable);
		            intents.putExtra("uid",tuid.get(position));
		            intents.putExtra("video_ID", video_id.get(position));
		            startActivity(intents);
				
				
			}else if(brige.bvariety.equals("CD")){
				
				
				brige.btitle =titles[position];
				brige.byear = Integer.toString(year.get(position));
			//	brige.bdirector = director.get(position);
			//	brige.bcast = cast.get(position);
				brige.bmusic = music.get(position);
			//	brige.bsummary = summary.get(position);
				brige.bnote = note.get(position);
				brige.bpicture =  picture.get(position);
				brige.bvariety = variety.get(position);
				brige.bECURL = EC.get(position);
				brige.manufactured = manufactured.get(position);
				brige.artists= artist.get(position);
				
				
				 Intent intents = new Intent();
		            intents.setClassName(
		                    "com.koba.myshelf",
		                    "com.koba.myshelf.CDPub");
		            intents.putExtra("c_screen_name",screen_name);
		            intents.putExtra("name",name);
		            
		            intents.putExtra("title",brige.btitle);
		            intents.putExtra("year",brige.byear);
		           
		            intents.putExtra("music",brige.bmusic);
		            intents.putExtra("note",brige.bnote);
		            intents.putExtra("variety",brige.bvariety);
		            intents.putExtra("picture",brige.bpicture);
		            intents.putExtra("EC",brige.bECURL);
		            System.out.println("commenttable :" + commenttable.get(position));
		            intents.putExtra("commenttable",commenttable.get(position));
		            intents.putExtra("uid",tuid.get(position));
		            
		            intents.putExtra("artists",brige.artists);
		            intents.putExtra("manufacture", brige.manufactured);
		            intents.putExtra("video_ID", video_id.get(position));
		            startActivity(intents);
				
				
				
				
				
			}else if(brige.bvariety.equals("book")){
				
				
				
				brige.btitle =titles[position];
				brige.byear = Integer.toString(year.get(position));
			//	brige.bcast = cast.get(position);
			//	brige.bmusic = music.get(position);
				brige.bsummary = summary.get(position);
				brige.bnote = note.get(position);
				brige.bpicture =  picture.get(position);
				brige.bvariety = variety.get(position);
				brige.bECURL = EC.get(position);
				brige.manufactured = manufactured.get(position);
				brige.author = author.get(position);
				
				
				 Intent intents = new Intent();
		            intents.setClassName(
		                    "com.koba.myshelf",
		                    "com.koba.myshelf.BookPub");
		            intents.putExtra("c_screen_name",screen_name);
		            intents.putExtra("name",name);
		            intents.putExtra("title",brige.btitle);
		            intents.putExtra("year",brige.byear);
		            
		            intents.putExtra("summary",brige.bsummary);
		            intents.putExtra("note",brige.bnote);
		            intents.putExtra("variety",brige.bvariety);
		            intents.putExtra("picture",brige.bpicture);
		            intents.putExtra("author",brige.author);
		            intents.putExtra("manufacture", brige.manufactured);
		            
		            intents.putExtra("EC",brige.bECURL);
		            intents.putExtra("commenttable",commenttable.get(position));
		            intents.putExtra("uid",tuid.get(position));
		            startActivity(intents);

				
				
			}else{
				
			
		 		  AlertDialog.Builder dlg;
                  dlg = new AlertDialog.Builder(MainActivity.this);
                  dlg.setTitle("読み込めませんでした");
                  dlg.setMessage("Item削除をオススメします");
                  dlg.setPositiveButton("Yes", null);
                  dlg.show();


			}
			
			
			


		





		}
	}

	private LinearLayout.LayoutParams createParam(int w, int h){
        return new LinearLayout.LayoutParams(w, h);
    }


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
	
		  switch (item.getItemId()) {
	        case R.id.camerabt:
	        	camerabt();
	            break;
	        }
		
		
		
		   
	      // ActionBarDrawerToggleにandroid.id.home(up ナビゲーション)を渡す。
	      if (mDrawerToggle.onOptionsItemSelected(item)) {
	          return true;
	      }
	   

		
		return super.onOptionsItemSelected(item);
	}



	  private void Oauth(){

	    	Thread th;
			(th = new Thread(new Runnable() { //新しいスレッド(thスレッド)の作成
				@Override
				public void run() {

	        //Twitetr4Jの設定を読み込む
	        twitter4j.conf.Configuration conf = ConfigurationContext.getInstance();




	        //Oauth認証オブジェクト作成
	        _oauth = new OAuthAuthorization(conf);
	        //Oauth認証オブジェクトにconsumerKeyとconsumerSecretを設定
	        _oauth.setOAuthConsumer(OAUTH, SECRET);
	        //アプリの認証オブジェクト作成
	        try {

	            _req = _oauth.getOAuthRequestToken("call://back");
	        } catch (TwitterException e) {

	        	System.out.println("asdfgh");


	            e.printStackTrace();
	        }
	        String _uri;
	        _uri = _req.getAuthorizationURL();

	        System.out.println("_uri:"+_uri);



	        startActivityForResult(new Intent(Intent.ACTION_VIEW , Uri.parse(_uri)), 0);
	    }


			})).start();//←thスレッド終わり
	    }



	  @Override
	  protected void onPostCreate(Bundle savedInstanceState) {
	      super.onPostCreate(savedInstanceState);
	      mDrawerToggle.syncState();
	  }
	   
	  @Override
	  public void onConfigurationChanged(Configuration newConfig) {
	      super.onConfigurationChanged(newConfig);
	      mDrawerToggle.onConfigurationChanged(newConfig);
	  }
	   

	   

		public View.OnClickListener draw = new View.OnClickListener() {

			public void onClick(View v) {
				  mDrawer.closeDrawers();
				
			}
		};
	  
	  
	  
		public View.OnClickListener opensearch = new View.OnClickListener() {

			public void onClick(View v) {

				

				 Intent intent = new Intent();
		            intent.setClassName(
		                    "com.koba.myshelf",
		                    "com.koba.myshelf.Similarity_Shelf");

		            
		            startActivity(intent);

				
				


			}
		};

	    
		 
		public View.OnClickListener favoopen = new View.OnClickListener() {

			public void onClick(View v) {

				

				 Intent intent = new Intent();
		            intent.setClassName(
		                    "com.koba.myshelf",
		                    "com.koba.myshelf.Favo_Shelf");

		            
		  		  System.out.println("favo_table" + favo_table);
		            intent.putExtra("favo_table",favo_table);

		            
		            startActivity(intent);

				
				


			}
		};

		
		
		
		 
		public View.OnClickListener favoshowopen = new View.OnClickListener() {

			public void onClick(View v) {

				

				 Intent intent = new Intent();
		            intent.setClassName(
		                    "com.koba.myshelf",
		                    "com.koba.myshelf.Favo_newshow");
		            
		            intent.putExtra("favo_table",favo_table);
		            intent.putExtra("name",name);
		      	  System.out.println("favo_table" + favo_table);
		        
		            startActivity(intent);

			}
		};

		
		
		
		  @Override
		  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		    super.onActivityResult(requestCode, resultCode, data);
		    Bundle bundle = data.getExtras();
		
		    
		    
		    if (resultCode == RESULT_OK) {
		    	Intent refresh = new Intent(this, MainActivity.class);
		    	startActivity(refresh);//Start the same Activity
		    	finish(); //finish Activity
		    }
		    
		    
		    
		  }

		
	        

}
