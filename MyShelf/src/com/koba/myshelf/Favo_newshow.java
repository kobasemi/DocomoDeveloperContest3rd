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

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class Favo_newshow extends Activity {

	
	
	String DBPASS = Pass.DBPASS;
	String PATH = Pass.PATH;
	String USER = Pass.USER;
	String OAUTH = Pass.OAUTH;
	String SECRET = Pass.SECRET;
	
	// con = DriverManager.getConnection(PATH,USER,DBPASS);
	private ActionBarDrawerToggle mDrawerToggle;
	private DrawerLayout mDrawer;
	   ResultSet res = null;
	
	public Connection con = null;
    public PreparedStatement ps = null;


    final Handler handler=new Handler();

    String token =null;
    String tokensecret = null;
    Long getuid = null;
    String name = null;
    String screenname = null;
    String tablename = null;
    String friendname = null;
    String groupname =null;
    String favo_table =null;

    	
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
     

     ArrayList<Bitmap> bitmaplist3 = new ArrayList<Bitmap>();
     ArrayList<String> titles3= new ArrayList<String>();
     ArrayList<String> header3 = new ArrayList<String>();
    	
     ArrayList<Integer> fid = new ArrayList<Integer>();
     ArrayList<Long> fuid = new ArrayList<Long>();
     ArrayList<String> fscreen_name = new ArrayList<String>();
     ArrayList<String> ftable_name = new ArrayList<String>();
     
     
     Long uid = null;
     String table_name = null;

     Bitmap  bitmaplist[] = new Bitmap[0];
     String  titles[] = new String[0];


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		
		setContentView(R.layout.favo_newshow);

 
		  Intent intent = getIntent();
		  favo_table = intent.getStringExtra("favo_table");
		  name = intent.getStringExtra("name");


        (new Thread(new Runnable() {
                @Override
                public void run() {

                	try{

                    Class.forName("com.mysql.jdbc.Driver"); 
                    con = DriverManager.getConnection(PATH,USER,DBPASS);

                    String sql = "select * from " + favo_table;

                    ps = con.prepareStatement(sql);
                    ResultSet rs = ps.executeQuery();

                    while(rs.next()) {
                    	ftable_name.add(rs.getString("table_name"));
                        fscreen_name.add(rs.getString("screenname"));
                        fuid.add(rs.getLong("uid"));
                        fid.add(rs.getInt("id"));
                    }

        
                    
                Calendar timeSet = Calendar.getInstance();
        		    SimpleDateFormat format3 = new SimpleDateFormat("dd");
        		    String ddFormat = format3.format( timeSet.getTime() );
        		    SimpleDateFormat format4 = new SimpleDateFormat("MM");
        		    String MFormat = format4.format( timeSet.getTime() );
        		    SimpleDateFormat format5 = new SimpleDateFormat("yyyy");
        		    String yyyyFormat = format5.format( timeSet.getTime() );
        		long Now = Long.parseLong(yyyyFormat + MFormat +ddFormat  + "00" + "00" + "00") ;

        		
        		
              if(ftable_name != null && !rs.wasNull()){      
              
            
            	  for(int i = 0 ; i < ftable_name.size();i++){
        
                    String correction  = "Select * from "+ ftable_name.get(i) + " where nowDate >= "+ Now;
                    ps = con.prepareStatement(correction);
                    // クエリーを実行して結果セットを取得
                    res = ps.executeQuery();
                    Integer numi  = null;
                    numi = 0;

                    System.out.println("2");

                    
                    while(res.next()) {
                    	tid.add (res.getInt("id"));
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
            		 				bitmaplist3.add(bitmap);
            		 				titles3.add(res.getString("title"));
            		 				header3.add(fscreen_name.get(i));
            		 				 numi = numi + 1 ;

            		 				istream.close();




                    }
                    ////////////////////////////////////////////////
            	  }



            	    handler.post(new Runnable() {
            	    	                	    	@Override
            	    	                            public void run() {
            	    	      
	
            	    	                	    
            	    	                	    	    GridView gv = (GridView)findViewById(R.id.gridView1);

            	    	                	    	    /*
            	    	                	    	    Bitmap[] bitmaplist2=(Bitmap[])bitmaplist3.toArray(new Bitmap[0]);
            	    	                	    	    String[] titles2=(String[])titles3.toArray(new String[0]);
            	    	                	    	    String[] header2=(String[])header3.toArray(new String[0]);
            	    	                	    	     */
            	    	                	    	
            	    	                	    	    int cnt = titles3.size();
            	    	                	    	    
            	    	                	    	    Bitmap[] bitmaplist2 = new Bitmap[cnt];
            	    	                	    	    String[] titles2 = new String[cnt];
            	    	                	    	    String[] header2 = new String[cnt];

            	    	                	    	//arrayから配列へ
            	    	                	    	    
            	    	                	    		  for(int i = 0 ; i < titles3.size();i++){
            	    	                	    	    	
            	    	                	    			  header2[i] = header3.get(i);
            	    	                	    			  titles2[i] = titles3.get(i);
            	    	                	    			  bitmaplist2[i] = bitmaplist3.get(i);
            	    	                	    	    }
            	    	                	    		    // 縦幅に合わせる
          	    	                	    	           int height = gv.getHeight();
          	    	                	    	           int width = gv.getWidth();
            	    	                	    		  
            	    	                	    	
                	    	                	    	    
                	    	            
            	               		 			gv.setAdapter(new MultiAdapter(getApplicationContext(),titles2,header2,bitmaplist2,height,width));
            	               		 			gv.setOnItemClickListener(new GridOnItemClick());
            	               		 		


            	    	                            }
            	    	                        });


              }

              

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

	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.favo_newshowactionbar, menu);
		getActionBar().setTitle("お気に入り更新順");
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






	public class GridOnItemClick implements OnItemClickListener {
		@Override
	public void onItemClick(AdapterView parent, View v, int position, long id) {


			Paperbrige brige = new Paperbrige();

		
		
			brige.bvariety = variety.get(position);
		

			//varietyで飛ばすintentが違う
			
			
			if(brige.bvariety.equals("DVD")){
				
				
				brige.btitle =title.get(position);
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
				
				
				brige.btitle =title.get(position);
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
				
				
				
				brige.btitle =title.get(position);
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
				
				
				//null値
				
			}
			
			




		}
	}


	public void shelf () {
		
		finish();

	}


	

		
	        

}
