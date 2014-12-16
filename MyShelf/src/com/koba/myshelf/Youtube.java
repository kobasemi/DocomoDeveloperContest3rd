package com.koba.myshelf;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import java.io.IOException;
import java.util.List;


public class Youtube extends Activity implements View.OnClickListener {

	private Button playVideoButton;
	/** Global instance of the HTTP transport. */
	private final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	/** Global instance of the JSON factory. */
	private final JsonFactory JSON_FACTORY = new JacksonFactory();
	/** Global instance of Youtube object to make all API requests. */
	private YouTube youtube;
	public static final String DEVELOPER_KEY = ""; //APIkey，自分の入れてください

	private static final int REQ_START_STANDALONE_PLAYER = 1;
	private static final int REQ_RESOLVE_SERVICE_MISSING = 2;

	public String video_ID; //videoID
	public String queryTerm=""; //動画検索用文字列 ここに入れた文字列がyoutubeで検索される

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_youtube);

		playVideoButton = (Button) findViewById(R.id.button1);
	    playVideoButton.setOnClickListener(this);
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

	@Override
	public void onClick(View v) {
		if (v == playVideoButton) {

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

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			})).start();//←thスレッド終わり

			//ここから動画再生
			try {
				th.join(); //thスレッドが終わるまで待つ
			} catch (InterruptedException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			int startTimeMillis = 0; //動画開始位置
		    boolean autoplay = true; //自動再生するかどうか
		    boolean lightboxMode = true; //全画面の動画再生にしないかどうか

		    Intent intent = null;
		    if (v == playVideoButton) {
		      intent = YouTubeStandalonePlayer.createVideoIntent(
		          this, DEVELOPER_KEY, video_ID, startTimeMillis, autoplay, lightboxMode);
		    }

		    if (intent != null) {
		      if (canResolveIntent(intent)) {
		        startActivityForResult(intent, REQ_START_STANDALONE_PLAYER);
		      } else {
		        // Could not resolve the intent - must need to install or update the YouTube API service.
		        YouTubeInitializationResult.SERVICE_MISSING
		            .getErrorDialog(this, REQ_RESOLVE_SERVICE_MISSING).show();
		      }
		    }

		}
	}

	//ここから下はいまいち分からない，サンプルのまんま

	@Override
	  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    if (requestCode == REQ_START_STANDALONE_PLAYER && resultCode != RESULT_OK) {
	      YouTubeInitializationResult errorReason =
	          YouTubeStandalonePlayer.getReturnedInitializationResult(data);
	      if (errorReason.isUserRecoverableError()) {
	        errorReason.getErrorDialog(this, 0).show();
	      } else {
	        //String errorMessage =
	            //String.format(getString(R.string.error_player), errorReason.toString());
	        //Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
	      }
	    }
	  }

	  private boolean canResolveIntent(Intent intent) {
	    List<ResolveInfo> resolveInfo = getPackageManager().queryIntentActivities(intent, 0);
	    return resolveInfo != null && !resolveInfo.isEmpty();
	  }


}
