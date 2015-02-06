package com.koba.myshelf;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;


public class CallBackActivity extends Activity {

	String DBPASS = Pass.DBPASS;
	String PATH = Pass.PATH;
	String USER = Pass.USER;
	String OAUTH = Pass.OAUTH;
	String SECRET = Pass.SECRET;
	
	
	Connection con = null;
    PreparedStatement ps = null;
    AccessToken token = null;
    twitter4j.User user = null;
    String tablename = null;
    String friendtable = null ;
	String grouptable= null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.callback);

    	Thread th;
    	getActionBar().setTitle("TwitterCallBack");

    	
    	
    	
    	
 
    	
   	 callbacksave db = new callbacksave();
		 Thread childThread = new Thread( db);
		 childThread.start();


		 try {
			childThread.join();
		} catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		   	
	    	
	        SharedPreferences pref =getSharedPreferences("pref",MODE_PRIVATE);
	        
	        Editor se = pref.edit();
	      		se.putString("token",token.getToken() );
	      		se.putString("tokensecret",token.getTokenSecret());
	      		se.putLong("uid",user.getId());
	      		se.putString("name",user.getName());
	      		se.putString("screenname",user.getScreenName() );
	      		se.putString("tablename",tablename);
	      		se.putString("frinedtable",friendtable);
	      		se.putString("grouptable",grouptable);
	      	se.commit();


		 
		 
		 Intent intent = new Intent();
           intent.setClassName(
                   "com.koba.myshelf",
                   "com.koba.myshelf.MainActivity");
     
           startActivity(intent);
           
           finish();
           

    }


    
    
    class callbacksave extends Thread{
    	
    	
    
			public void run() {

        //Twitterの認証画面から発行されるIntentからUriを取得
        Uri uri = getIntent().getData();

        if(uri != null && uri.toString().startsWith("call://back")){
            //oauth_verifierを取得する
            String verifier = uri.getQueryParameter("oauth_verifier");
            try {
                //AccessTokenオブジェクトを取得
                token = MainActivity._oauth.getOAuthAccessToken(MainActivity._req, verifier);
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        }


        System.out.println("AccessToken:"+ token.getToken() );
        System.out.println("AccessToken:"+ token.getTokenSecret());


        //認証した人のユーザ情報



     Twitter twitter = new TwitterFactory().getInstance();

        //Twitterオブジェクト作成

        twitter.setOAuthConsumer(OAUTH, SECRET);;

        //Twitterオブジェクトにアプリケーションのconsumer keyとconsumer secretをセット
        AccessToken accessToken = new AccessToken(
        		token.getToken(), token.getTokenSecret());
        
        
        twitter.setOAuthAccessToken(accessToken);
        
        

       
        Twit Twitteruptext =new Twit(token.getToken(), token.getTokenSecret());
        Twitteruptext.TweetUp("MyShelfとこのアカウントを連携しました.ようこそ.");
      
      

		try {
			user = twitter.verifyCredentials();
		} catch (TwitterException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		System.out.println(user.getId());
		System.out.println(user.getName());
		System.out.println(user.getScreenName());
		System.out.println(user.getProfileImageURL());


        user.getId();//自分のアカウントのIDの取得（数字のID）
        user.getName();//自分のアカウントの名前を取得
        user.getScreenName();//自分のアカウントのUserNameを取得（アルファベットのみの名前）
        user.getProfileImageURL(); //自分のアカウントのプロフィール画像のURLを取得

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



       String maxcntsql = "select max(id) from all_user";

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

			 System.out.println("dddddd");
			  max = rs.getInt("max(id)");


		   }
	   } catch (SQLException e1) {
		// TODO 自動生成された catch ブロック
		e1.printStackTrace();
	   }

	  int newcnt = max+1;
	  
		  tablename = "table_"+ user.getId() ;
		  friendtable = "friend_"+user.getId() ;
		  grouptable= "group_" + user.getId();
		  String favotable =  "favo_" + user.getId();

		  String searchquery = "select * from all_user where uid=" +  user.getId();
			try {

				System.out.println("1");
				ps = con.prepareStatement(searchquery);
			} catch (SQLException e1) {
				// TODO 自動生成された catch ブロック
				System.out.println("2");
				e1.printStackTrace();
			}


			  ResultSet rs2 = null;
	 			try {
	 				System.out.println("3");
	 				rs2 = ps.executeQuery();
	 			} catch (SQLException e) {
	 				// TODO 自動生成された catch ブロック
	 				System.out.println("4");
	 				e.printStackTrace();
	 			}


	 			try {
					rs2.next();
				} catch (SQLException e2) {
					// TODO 自動生成された catch ブロック
					e2.printStackTrace();
				}

	 			System.out.println("test");


				
				String tablequery = "create table "+ tablename + " Like table_2956602438 ";
				String friendquery = "create table "+ friendtable + " Like group_2956602438";
				String groupquery = "create table "+ grouptable +  " Like friend_2956602438";
				String favoquery ="create table "+ favotable  +  " Like favo_2956602438 ";
				
					
				
				System.out.println("tablequery" + tablequery);
				
				  try {
					stmt.executeUpdate(tablequery);
					  stmt.executeUpdate(friendquery);
					  stmt.executeUpdate(groupquery);
					  stmt.executeUpdate(favoquery);
					
				  
				  } catch (SQLException e1) {
					// TODO 自動生成された catch ブロック
					e1.printStackTrace();
				}
		
				  

		 	       String mxct = "select max(id) from "+tablename;

		 	     
		 	       rs = null;
		 	       try {
		 	    	   rs = stmt.executeQuery(mxct);
		 			} catch (SQLException e) {
		 				// TODO 自動生成された catch ブロック
		 		
		 				e.printStackTrace();
		 			}


		 			int mx = 0;

		 		   try {
		 			while(rs.next()) {

		 				 System.out.println("dddddd");
		 				  mx = rs.getInt("max(id)");


		 			   }
		 		   } catch (SQLException e1) {
		 			// TODO 自動生成された catch ブロック
		 			e1.printStackTrace();
		 		   }

		 		  int nwct = mx+1;
		 		  
		 			String commentquery = "create table uid"+ user.getId() +"id" +nwct +" Like commenttablesample";
				  
		 			System.out.println("comment" + commentquery);
					String deletequery = "DELETE FROM all_user where uid =" + user.getId() ;
					System.out.println("取説あり");
					
					String torisetuquery = "INSERT INTO "+tablename+"(id,uid,title,director,year,cast,music,summary,note,picture,variety,EAN8,EAN13,manufactured,artists,author,wikipedia,ISBN13,ISBN10,ECURL,commenttable,video_id,opt,nowDate)values("+ nwct +","+user.getId()+",'はじめに','NULL',20150201,'NULL','NULL','ようこそ!\nチームコバセミです。詳しい説明はStartページ(Shelf)の左上のアイコンを押すとメニューが出てきます、そこでHelpをクリック。新Itemを追加したい場合はShelfに戻ってCameraアイコンをクリック。Cameraの起動を確認し、被写体を写しましょう。なお、この説明書を消すにはShelfに戻って説明書アイコンを長押ししてください。','ここは自由記述のノートです。\n100文字以内でコメントを残せます。','http://133.242.225.109/aaa.jpg','book','null','null','kobasemi','null','kobasemi','null','null','null','http://www.yahoo.com','uid"+user.getId()+"id1','null','null',0)";
					System.out.println("torisetuquery" + torisetuquery);

					try {
						stmt.executeUpdate(deletequery);
						stmt.executeUpdate(torisetuquery);
						 stmt.executeUpdate(commentquery);
					 } catch (SQLException e1) {
						// TODO 自動生成された catch ブロック
						e1.printStackTrace();
					}
			
		  String Insertquery = "INSERT INTO all_user(id,uid,name,screenname,token,tokensecret,table_name,friend_table,group_table,favo_table,opt)"
		  		+ "VALUES(" + newcnt + "," +  user.getId()  + ",'" + user.getName() + "','" + user.getScreenName() + "','" + token.getToken() + "','" + token.getTokenSecret()
		  		+ "','" +tablename+ "','" +friendtable+ "','" +grouptable+"','" +favotable  +"','1'" +")";
		  
		  
		  


		  System.out.println(Insertquery);

          try {
				int num = stmt.executeUpdate(Insertquery);
				System.out.println(num);
          } catch (SQLException e) {
				// TODO 自動生成された catch ブロック

          }


          //設定値



    }

    }
    


}