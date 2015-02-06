package com.koba.myshelf;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
<<<<<<< HEAD
=======

        super.onCreate(savedInstanceState);



        SharedPreferences pref = getSharedPreferences("pref", 0);
         pretoken = pref.getString("token", null);
         String   pretokensecret = pref.getString("tokensecret", null);


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
                    	year.add(res.getString("year"));
                    	System.out.println("year" + res.getString("year"));
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




>>>>>>> 0d2bead... ver1.2.9.2
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
