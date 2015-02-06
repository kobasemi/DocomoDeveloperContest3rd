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

import twitter4j.auth.OAuthAuthorization;
import twitter4j.auth.RequestToken;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.GridView;
import android.widget.LinearLayout;

import com.mysql.jdbc.Statement;

public class OtherShelf extends Activity {



	String DBPASS = Pass.DBPASS;
	String PATH = Pass.PATH;
	String USER = Pass.USER;




	private ActionBarDrawerToggle mDrawerToggle;
	private DrawerLayout mDrawer;

	String showscreenname = null;
	public Connection con = null;
    public PreparedStatement ps = null;


    String favo_table = null;



    final Handler handler=new Handler();

    String token =null;
    String tokensecret = null;
    Long getuid = null;
    String name = null;
    String screenname = null;
    String tablename = null;
    String friendname = null;
    String groupname =null;


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
     Long myuid = null;
     String myscreenname = null;

     String table_name = null;

    Bitmap  bitmaplist[] = new Bitmap[0];
    String  titles[] = new String[0];

	String header[] ={"user1","user2","user3","user4","user5","user6","user7","user8","user9","user10","user11"};


	String pretoken = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.othershelf);

        Intent intent = getIntent();
		table_name = intent.getStringExtra("table_name");



		System.out.println("table" + table_name);
		name = intent.getStringExtra("name");

        SharedPreferences pref = getSharedPreferences("pref", 0);
        pretoken = pref.getString("token", null);

        (new Thread(new Runnable() {
                @Override
                public void run() {

                	try{
                	  // ドライバクラスをロード
                    Class.forName("com.mysql.jdbc.Driver");


                    // データベースへ接続
                    con = DriverManager.getConnection(PATH,USER,DBPASS);
                   // ステートメントオブジェクトを生成

                    //preferenceに保存されていたtokenをDB問い合わせ。
                    String sql = "select * from all_user where token = '" + pretoken + "'";

                    // ステートメントオブジェクトを生成
                    ps = con.prepareStatement(sql);

                    // クエリーを実行して結果セットを取得
                    ResultSet rs = ps.executeQuery();

                    while(rs.next()) {
                        // データを取得

                         myuid = rs.getLong("uid");
                         myscreenname = rs.getString("screenname");


                    }

                    // クエリーを実行して結果セットを取得
                    String  selectuid =  "select * from all_user where table_name = '" + table_name +"'";



                   	ps = con.prepareStatement(selectuid);
                   	ResultSet uidsel = ps.executeQuery();


                    while(uidsel.next()) {

                    	getuid = uidsel.getLong("uid");
                 //   	showscreenname =  uidsel.getString("screenname");
                    	System.out.println("getuid :" + getuid);


                    }


                    String counts =  " select count(*) as cnt from "+ table_name;
                    System.out.println(counts);

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

            	    	                	    	    System.out.println("猫");
            	    	                      //   creategrid(picture);
/*
            	    	                	    	    GridView gridView = new GridView(getApplicationContext());
            	    	                	            gridView.setNumColumns(4);
            	    	                	            gridView.setVerticalSpacing(10);
            	    	                	            gridView.setHorizontalSpacing(10);
            	    	                	            setContentView(gridView);*/





            	    	                	    	    GridView gv = (GridView)findViewById(R.id.gridView1);
            	    	                	    	    
                       	    	                	 //   	    gv.setGravity(Gravity.CENTER);
               	    	                	    	            // 縦幅に合わせる
               	    	                	    	           int height = gv.getHeight();
               	    	                	    	           int width = gv.getWidth();
               	    	                	    	            System.out.println("height1" + height);

                       	    	                	        System.out.println("height2" + height);
                             	    	                      //   creategrid(picture);

            	    	                	            
            	               		 			titles = titles2;
            	               		 			bitmaplist = bitmaplist2;

            	               		 		
            	               		 			titles = titles2;
            	               		 			bitmaplist = bitmaplist2;

            	               		 			gv.setAdapter(new SimpleAdapter(getApplicationContext(),titles,bitmaplist,height,width));




            	               		 			gv.setOnItemClickListener(new GridOnItemClick());


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




    	//TextView usertext=(TextView)findViewById(R.id.textView10);
    	//usertext.setText("@" + name + "Shelf");
        getActionBar().setTitle("This is "+" @" + name + " Shelf");

	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.otheractionbar, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		  switch (item.getItemId()) {
	        case R.id.star:
	        	addstarbtr();
	            break;
	        case R.id.button1:
	        	finishactivity();
	        	break;
	        }


		return super.onOptionsItemSelected(item);
	}






	//paperに渡す
	class Paperbrige {

		//タイトル
		private String btitle;
		private String bdirector;

		//作成日
		private String byear;


		//出演者です。ノージがどのような値を持ってくるか分かりませんが

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


	public void finishactivity() {


			finish();


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
		            intents.putExtra("c_screen_name",name);
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
		            intents.putExtra("c_screen_name",name);
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
		            intents.putExtra("c_screen_name",name);
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
		            
		            System.out.println("BookPub" + tuid.get(position));
		            intents.putExtra("uid",tuid.get(position));
		            startActivity(intents);



			}else{


		 		AlertDialog.Builder dlg;
                dlg = new AlertDialog.Builder(OtherShelf.this);
                dlg.setTitle("読み込めませんでした");
                dlg.setMessage("データベース異常です");
                dlg.setPositiveButton("Yes", null);
                dlg.show();


			}











		}
	}

	private LinearLayout.LayoutParams createParam(int w, int h){
        return new LinearLayout.LayoutParams(w, h);
    }




		class addstar extends Thread{



		       public void run() {


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

		        String favquery = "select * from all_user where uid = '"+  myuid +"'";

		    	System.out.println(favquery);
		        try {
		        	  ps = con.prepareStatement(favquery);
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
							 favo_table	= rs.getString("favo_table");


						 }




		          java.sql.Statement stmt = null;
					try {
						stmt = con.createStatement();
					} catch (SQLException e3) {
						// TODO 自動生成された catch ブロック
						e3.printStackTrace();
					}


				String 	searchfav = "select * from " + favo_table +" where screenname='"+ name +"'";

				System.out.println(searchfav);



				      rs = null;
		          try {
		       	   rs = stmt.executeQuery(searchfav);
		   		} catch (SQLException e) {
		   			// TODO 自動生成された catch ブロック
		   			 System.out.println("aaa");
		   			e.printStackTrace();
		   		}

		          int a =0;
		          while(rs.next())
		          {

		           a = rs.getInt("id");
		          }

		          if(a == 0)	{


					  String maxcntsql = "select max(id) from " + favo_table ;

			          System.out.println(maxcntsql);
			           rs = null;
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

					int newcnt = max +1;



			        rs =null;

			        String favins = "insert into " + favo_table +"(id,uid,table_name,screenname)Values("+newcnt +","+getuid + ",'"+table_name+"','"+name+"')";


			         Statement  stmt1 = null;

			         try {
						int num = stmt.executeUpdate(favins);


			         } catch (SQLException e) {

						e.printStackTrace();
					}





	            	    handler.post(new Runnable() {
	            	    	                	    	@Override
	            	    	                            public void run() {



	            	    	                	            SharedPreferences pref = getSharedPreferences("pref", 0);
	            	    	                	            String   pretoken = pref.getString("token", null);
	            	    	                	            String   pretokensecret = pref.getString("tokensecret", null);

	            	    	                	            Twit Twitteruptext =new Twit(pretoken,pretokensecret);

	            	    	                	            Twitteruptext.TweetUp("@"+name+" さんの本棚をお気に入りに追加しました.");

	            	    	                	    		  AlertDialog.Builder dlg;
	            	    					                   dlg = new AlertDialog.Builder(OtherShelf.this);
	            	    					                   dlg.setTitle("完了");
	            	    					                   dlg.setMessage("お気に入りに追加しました");
	            	    					                   dlg.setPositiveButton("Yes", null);
	            	    					                   dlg.show();




	            	    	                            }
	            	    	                        });





		          }else{




	            	    handler.post(new Runnable() {
	            	    	                	    	@Override
	            	    	                            public void run() {


	            	    	                	    		  AlertDialog.Builder dlg;
	            	    					                   dlg = new AlertDialog.Builder(OtherShelf.this);
	            	    					                   dlg.setTitle("通知");
	            	    					                   dlg.setMessage("すでに登録済です");
	            	    					                   dlg.setPositiveButton("Yes", null);
	            	    					                   dlg.show();






	            	    	                            }
	            	    	                        });







		          }



				} catch (SQLException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}



		       }


		}





		public void addstarbtr() {




			    addstar as = new addstar();
					 Thread childThread = new Thread(as);
					 childThread.start();


					 try {
						childThread.join();
					} catch (InterruptedException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}



		}

}
