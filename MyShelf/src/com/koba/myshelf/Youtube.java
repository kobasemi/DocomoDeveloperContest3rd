package com.koba.myshelf;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

public class Youtube extends Activity  {

	  final Handler handler=new Handler();
	InputStream istream = null;
    String thumbnail = null;
    URL thumbnailURL = null;
    private Button playVideoButton;
	/** Global instance of the HTTP transport. */
	private final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	/** Global instance of the JSON factory. */
	private final JsonFactory JSON_FACTORY = new JacksonFactory();
	/** Global instance of Youtube object to make all API requests. */
	private YouTube youtube;
	public static final String DEVELOPER_KEY = "AIzaSyDyxo9Ce_dnwzpQH4dZ53sLvzBvGZDLSCg"; //APIkey，自分の入れてください

	private static final int REQ_START_STANDALONE_PLAYER = 1;
	private static final int REQ_RESOLVE_SERVICE_MISSING = 2;

	public String video_ID; //videoID
	public String queryTerm="進撃の巨人"; //動画検索用文字列 ここに入れた文字列がyoutubeで検索される
	
	Bitmap bitmap = null;

	
	
	
	public void thum(){
		
		
	
	    final ImageView imageView = (ImageView)findViewById(R.id.youtubeview);

		
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

					//動画検索条件の指定
					YouTube.Search.List search = youtube.search().list("id,snippet");
					//search.setPart("id");
					search.setKey(DEVELOPER_KEY);    // APIkey設定
					search.setQ(queryTerm);   // 検索文字列
					search.setType("video");  // 検索する対象
					search.setMaxResults((long)1); // 検索で出す数
					search.setFields("items(id/videoId)"); //取得するアイテム(videoIDだけ取得する)

					SearchListResponse searchResponse = search.execute(); //検索

					List<SearchResult> searchResultList = searchResponse.getItems();

					SearchResult singleVideo = searchResultList.get(0);

					video_ID = singleVideo.getId().getVideoId().toString(); //検索結果を動画再生のときに使用するvideoIDの変数に入れる


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


	            	            imageView.setImageBitmap(bitmap);




	                        }
	                    });



					System.out.println(video_ID);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		})).start();//←thスレッド終わり


		
		
		
	}
	
	
	
	
	
}