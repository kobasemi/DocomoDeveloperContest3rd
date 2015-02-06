package com.koba.myshelf;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jp.ne.docomo.smt.dev.common.exception.SdkException;
import jp.ne.docomo.smt.dev.common.exception.ServerException;
import jp.ne.docomo.smt.dev.common.http.AuthApiKey;
import jp.ne.docomo.smt.dev.imagerecognition.ImageRecognition;
import jp.ne.docomo.smt.dev.imagerecognition.constants.Recog;
import jp.ne.docomo.smt.dev.imagerecognition.data.ImageRecognitionBookDetailData;
import jp.ne.docomo.smt.dev.imagerecognition.data.ImageRecognitionCandidateData;
import jp.ne.docomo.smt.dev.imagerecognition.data.ImageRecognitionCdDetailData;
import jp.ne.docomo.smt.dev.imagerecognition.data.ImageRecognitionDetailData;
import jp.ne.docomo.smt.dev.imagerecognition.data.ImageRecognitionDvdDetailData;
import jp.ne.docomo.smt.dev.imagerecognition.data.ImageRecognitionRelatedContentData;
import jp.ne.docomo.smt.dev.imagerecognition.data.ImageRecognitionResultData;
import jp.ne.docomo.smt.dev.imagerecognition.data.ImageRecognitionSiteData;
import jp.ne.docomo.smt.dev.imagerecognition.param.ImageRecognitionRequestParam;

import org.apache.commons.codec.net.URLCodec;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;

import com.mysql.jdbc.Statement;


/**
 * 画像認識要求画面 画像認識要求を実行して結果を取得するサンプルページ
 */
public class Camera extends Activity  {
	
	
	
	
	
	String DBPASS = Pass.DBPASS;
	String PATH = Pass.PATH;
	String USER = Pass.USER;
	
	ProgressBar progressBar1 = null;
	
	
	private static final int REQUEST_CODE_CAMERA = 2;
	static final String APIKEY =Pass.APIKEY;
	private RecognitionAsyncTask task;
	// 認識ジョブの識別ID
	private ImageRecognitionResultData _resultData = null;
	// インテントキー
	static final String INTENT_SEND_KEY = "ImageRecognizeResultData";
	static final String INTENT_RECOGNITIONID_KEY = "recognitionid";
	static final String INTENT_ITEMID_KEY = "itemid";
	// 結果表示
	//private EditText _resultText;

	//DVD

	int target = -1;
	String[] dest_li=null;
	String[] dest_a = null;
	String[] dest_a2 = null;
	String[] dest_a_tensou = null;
	String[] dest_link=null;
	String[] dest4 = null;
	String url_temp=null;
	String[] dest_url = null;
	Document document;
	String url = null;
	String encodeStr=null;

	String titles = null ;
	String ttitle = null;

	String DVDtext = null;
	int newcnt = 0;

	
	String googlebookid = null;
	
	//本
	String word;



	/*CD*/


	String uri;
	String text_artist="";
	String text_album="";
	String[] text_track=new String[105];
	String data = "";
	String scheme = "https";
	  String authority = "ws.audioscrobbler.com";
	  String path = "2.0/";
	  String method = "album.getinfo";
	  String api_key = Pass.API_KEY;
	  String album ;
	  String artist = "";
	  String format = "json";





	class DBcon{
		//追加はする予定

		String  dbuser_tablename = null;
		String 	dbuid = null;
		String  dbECURL= null;
		String  dbvariety = null;
		String  dbname = null;
		String	dbyear = null;
		String 	dbpicture = null;

		String dbsummary = null;

		//bookのみ
		String ISBN10 = null;
		String ISBN13 = null;
		String author = null;

		//CDのみ
		String dbartists = null;
		String dbalubam = null;

		//CD DVD
		String  dbmanufactured =null;  //発売 or 販売 元
		String  EAN13 = null;
		String 	EAN8 = null;
		String 	dbactor = null;

		String allmusic = null;

		//DVDのみ
		String director = null;



		String wikipedia = null;
		int newcount = 0;



	}



	DBcon allinfo = new DBcon();


	private class RecognitionAsyncTask
			extends
			AsyncTask<ImageRecognitionRequestParam, Integer, ImageRecognitionResultData> {
		private AlertDialog.Builder _dlg;
		private boolean isSdkException = false;
		private String exceptionMessage = null;

		public RecognitionAsyncTask(AlertDialog.Builder dlg) {
			super();
			_dlg = dlg;
		}

		@Override
		protected ImageRecognitionResultData doInBackground(
				ImageRecognitionRequestParam... params) {
			ImageRecognitionResultData resultData = null;
			ImageRecognitionRequestParam requestParam = params[0];
			try {
				// 画像認識要求のリクエスト送信
				ImageRecognition recognition = new ImageRecognition();
				resultData = recognition.request(requestParam);
			} catch (SdkException ex) {
				isSdkException = true;
				exceptionMessage = "ErrorCode: " + ex.getErrorCode()
						+ "\nMessage: " + ex.getMessage();
			} catch (ServerException ex) {
				exceptionMessage = "ErrorCode: " + ex.getErrorCode()
						+ "\nMessage: " + ex.getMessage();
			}
			return resultData;
		}

		@Override
		protected void onCancelled() {
		}

		@Override
		protected void onPostExecute(ImageRecognitionResultData resultData) {
			
			
			progressBar1.setProgress(60);
			
			if (resultData == null) {
				if (isSdkException) {
					_dlg.setTitle("SdkException 発生");
				} else {
					_dlg.setTitle("ServerException 発生");
				}
				_dlg.setMessage(exceptionMessage + " ");
				_dlg.show();
				_resultData = null;

			} else {
				// 結果表示
				_dlg.setTitle("処理結果");
				StringBuffer sb = new StringBuffer();

				//sb.append("認識ジョブの識別ID：" + resultData.getRecognitionId() + "\n");

				List<ImageRecognitionCandidateData> candidateList = resultData
						.getCandidateDataList();
				if (candidateList == null)
					return;

				Integer cnts = 0;

				for (ImageRecognitionCandidateData candidateData : candidateList) {


					/*
					sb.append("　認識結果候補のスコア：" + candidateData.getScore() + "\n");
					sb.append("　認識結果候補のID：" + candidateData.getItemId() + "\n");
					sb.append("　認識結果候補のカテゴリ：" + candidateData.getCategory()
							+ "\n");
					sb.append("　認識結果候補の画像のURL：" + candidateData.getImageUrl()
							+ "\n");
						*/
					allinfo.dbpicture =   candidateData.getImageUrl();


					ImageRecognitionDetailData detailData = candidateData
							.getDetailData();
					printDetailData(detailData);

					List<ImageRecognitionSiteData> siteList = candidateData
							.getSiteDataList();
					if (siteList != null) {
					//	sb.append("　認識結果候補の物体に関連するサイト情報：" + "\n");


						for (ImageRecognitionSiteData siteData : siteList) {

							cnts++;
				/*
							sb.append("　　ECサイトのURL：" + siteData.getUrl() + "\n");
							sb.append("　　ECサイトのタイトル：" + siteData.getTitle()
									+ "\n");
							sb.append("　　ECサイトの画像のURL："
									+ siteData.getImageUrl() + "\n");
*/

							 allinfo.dbECURL = siteData.getUrl();

						}
					} else {
				//		sb.append("　認識結果候補の物体に関連するサイト情報：なし" + "\n");
					}

					List<ImageRecognitionRelatedContentData> relatedList = candidateData
							.getRelatedContentDataList();
					if (relatedList != null) {
					//	sb.append("　認識結果候補の物体に関連するコンテンツ：" + "\n");
						for (ImageRecognitionRelatedContentData relatedData : relatedList) {



						/*	sb.append("　　関連コンテンツのURL：" + relatedData.getUrl()
									+ "\n");
							sb.append("　　関連コンテンツのタイトル："
									+ relatedData.getTitle() + "\n");
							sb.append("　　関連コンテンツの画像のURL："
									+ relatedData.getImageUrl() + "\n");
							sb.append("　　関連コンテンツの概要："
									+ relatedData.getAbstract() + "\n");
*/

						}
					} else {
				//		sb.append("　認識結果候補の物体に関連するコンテンツ：なし" + "\n");
					}



				//一回目のループで抜ける。
					break;


				}

				// ソフトキーボードを非表示にする
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			/*	imm.hideSoftInputFromWindow(_resultText.getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
				_resultData = resultData;
	*/



			//	DBinsert();


			}









			  System.out.println("variety:" + allinfo.dbvariety);



			  if (allinfo.dbvariety == null){

				  allinfo.dbvariety = "エラー";


			  }

			    SharedPreferences pref = getSharedPreferences("pref", 0);
			      String   pretoken = pref.getString("token", null);
			      String   pretokensecret = pref.getString("tokensecret", null);
			      Twit Twitteruptext =new Twit(pretoken,pretokensecret);
			  
				progressBar1.setProgress(80);
			if(allinfo.dbvariety.equals("CD")){


				 detail_CD db = new detail_CD();
				 Thread childThread = new Thread( db);
				 childThread.start();


				 try {
					childThread.join();
				} catch (InterruptedException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}

			 Intent intentmusic = new Intent();
	            intentmusic.setClassName(
	                    "com.koba.myshelf",
	                    "com.koba.myshelf.Musicnote");
	            System.out.println("CDintentStart");
	            intentmusic.putExtra("table",allinfo.dbuser_tablename);
	            intentmusic.putExtra("year",allinfo.dbyear);
	            intentmusic.putExtra("picture",allinfo.dbpicture);
	            intentmusic.putExtra("artist",allinfo.dbartists);
	            intentmusic.putExtra("alubam",allinfo.dbalubam);
	            intentmusic.putExtra("ECURL",allinfo.dbECURL);
	            intentmusic.putExtra("summary",allinfo.dbsummary);
	            intentmusic.putExtra("manufacture",allinfo.dbmanufactured);
	            intentmusic.putExtra("allmusic",allinfo.allmusic);
	            intentmusic.putExtra("name",allinfo.dbname);
	            Twitteruptext.TweetUp("お知らせ:  ["+allinfo.dbalubam+"]"+"をMyShelfに追加しました。");

	            System.out.println("allinfo.newcount" + allinfo.newcount);
	            intentmusic.putExtra("tablecnt",newcnt);

	            startActivity(intentmusic);
	            finish();

			}else if(allinfo.dbvariety.equals("DVD")){

				 System.out.println("DVDintentstart");



				 detail_DVD db = new detail_DVD();
				 Thread childThread = new Thread( db);
				 childThread.start();


				 try {
					childThread.join();
				} catch (InterruptedException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}

				  
				 
			      Twitteruptext.TweetUp("お知らせ:  ["+titles+"]"+"をMyShelfに追加しました。");
			      
				 Intent intentDVD = new Intent();
				 intentDVD.setClassName(
		                    "com.koba.myshelf",
		                    "com.koba.myshelf.DVDnote");




	            intentDVD.putExtra("table",allinfo.dbuser_tablename);
	            intentDVD.putExtra("year",allinfo.dbyear);
	            intentDVD.putExtra("picture",allinfo.dbpicture);
	            intentDVD.putExtra("director",allinfo.director);
	            intentDVD.putExtra("wikipedia",allinfo.wikipedia);
	            intentDVD.putExtra("ECURL",allinfo.dbECURL);
	            intentDVD.putExtra("summary",allinfo.dbsummary);
	            intentDVD.putExtra("manufacture",allinfo.dbmanufactured);
	            intentDVD.putExtra("allmusic",allinfo.allmusic);
	            intentDVD.putExtra("name",allinfo.dbname);
	            intentDVD.putExtra("cast",allinfo.dbactor);
	            intentDVD.putExtra("title",titles);
	            intentDVD.putExtra("tablecnt",newcnt);





	            startActivity(intentDVD);
	            finish();



			}else if(allinfo.dbvariety.equals("book")){




				System.out.println("bookintentstart");



				 detail_book db = new detail_book();
				 Thread childThread = new Thread( db);
				 childThread.start();


				 try {
					childThread.join();
				} catch (InterruptedException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}


			  
			      
			  
			 
			      Twitteruptext.TweetUp("お知らせ:  ["+titles+"]"+"をMyShelfに追加しました。");
			      
				 
				 
				 Intent intentbook = new Intent();
				 intentbook.setClassName(
		                    "com.koba.myshelf",
		                    "com.koba.myshelf.Booknote");


	            intentbook.putExtra("table",allinfo.dbuser_tablename);
	            intentbook.putExtra("year",allinfo.dbyear);
	            intentbook.putExtra("picture",allinfo.dbpicture);
	            intentbook.putExtra("director",allinfo.director);
	            intentbook.putExtra("wikipedia",allinfo.wikipedia);
	            intentbook.putExtra("ECURL",allinfo.dbECURL);
	            intentbook.putExtra("summary",allinfo.dbsummary);
	            intentbook.putExtra("manufacture",allinfo.dbmanufactured);
	            intentbook.putExtra("name",allinfo.dbname);
	            intentbook.putExtra("title",titles);



	            intentbook.putExtra("tablecnt",newcnt);
	            intentbook.putExtra("author",allinfo.author);
	            startActivity(intentbook);
	            finish();


			}else{

				  AlertDialog.Builder dlg;
				                   dlg = new AlertDialog.Builder(Camera.this);
				                   dlg.setTitle("読み込めませんでした");
				                   dlg.setMessage("これはDVD/CD/本/のうちですか？\nそうであれば、度映してください");
				                   dlg.setPositiveButton("Yes", null);
				                   dlg.show();

			}



		}

		/**
		 * カテゴリ毎の詳細情報を出力する
		 *
		 * @param detailData
		 *            詳細情報
		 */


		public Long Datert(){

			Calendar timeSet = Calendar.getInstance();

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






		private void printDetailData(ImageRecognitionDetailData detailData) {

			progressBar1.setProgress(30);

			StringBuffer sb = new StringBuffer();
			if (detailData instanceof ImageRecognitionBookDetailData) {

				allinfo.dbvariety = "book";

				ImageRecognitionBookDetailData bookData = (ImageRecognitionBookDetailData) detailData;
				allinfo.dbname = bookData.getItemName();
			/*	sb.append("　　認識物体の名称： " + bookData.getItemName()  + "\n");
				sb.append("　　発売日： " + bookData.getReleaseDate() + "\n");
				sb.append("　　年齢制限： " + bookData.getAgeRequirement() + "\n");
				sb.append("　　著者： " + bookData.getAuthor() + "\n");
				sb.append("　　訳者： " + bookData.getTranslator() + "\n");
				sb.append("　　発売元： " + bookData.getPublisher() + "\n");
				sb.append("　　販売元： " + bookData.getSeller() + "\n");
				sb.append("　　種別： " + bookData.getType() + "\n");
				sb.append("　　ページ数： " + bookData.getPages() + "\n");
				sb.append("　　ISBN10桁コード： " + bookData.getIsbn10() + "\n");
				sb.append("　　ISBN13桁コード： " + bookData.getIsbn13() + "\n");
				sb.append("　　言語： " + bookData.getLang() + "\n");*/





				allinfo.dbmanufactured = bookData.getPublisher() ;
				allinfo.dbyear = bookData.getReleaseDate();

				if(bookData.getAuthor() != null){
					List<String> authorlist = bookData.getAuthor();
					allinfo.author = (String)authorlist.get(0);
				}


				allinfo.dbname = bookData.getItemName() ;


				word = allinfo.dbname ;








			} else if (detailData instanceof ImageRecognitionCdDetailData) {
				allinfo.dbvariety = "CD";
				 System.out.println("ends");
				ImageRecognitionCdDetailData cdData = (ImageRecognitionCdDetailData) detailData;


				/*sb.append("　CDカテゴリ詳細情報：" + "\n");
				sb.append("　　認識物体の名称：" + cdData.getItemName() + "\n");
				sb.append("　　発売日：" + cdData.getReleaseDate() + "\n");
				sb.append("　　年齢制限：" + cdData.getAgeRequirement() + "\n");
				sb.append("　　アーティスト名：" + cdData.getArtist() + "\n");
				sb.append("　　作曲者：" + cdData.getComposer() + "\n");
				sb.append("　　指揮者：" + cdData.getConductor() + "\n");
				sb.append("　　レーベル：" + cdData.getLabel() + "\n");
				sb.append("　　販売元：" + cdData.getSeller() + "\n");
				sb.append("　　形式：" + cdData.getFormat() + "\n");
				sb.append("　　収録曲数：" + cdData.getSongs() + "\n");
				sb.append("　　収録時間：" + cdData.getTime() + "\n");
				sb.append("　　ディスク枚数：" + cdData.getDiscs() + "\n");
				sb.append("　　EAN13桁コード：" + cdData.getEan13() + "\n");
				sb.append("　　EAN8桁コード：" + cdData.getEan8() + "\n");
				sb.append("　　SPARSコード：" + cdData.getSparsCode() + "\n");
				*/

				allinfo.EAN13 = cdData.getEan13() ;
				allinfo.EAN13 = cdData.getEan8() ;
				allinfo.dbmanufactured = cdData.getLabel();
				allinfo.dbyear = cdData.getReleaseDate();
				List<String> artistlist = cdData.getArtist();
				allinfo.dbartists = (String)artistlist.get(0);
				allinfo.dbname = cdData.getItemName();








			} else if (detailData instanceof ImageRecognitionDvdDetailData) {
				ImageRecognitionDvdDetailData dvdData = (ImageRecognitionDvdDetailData) detailData;
				allinfo.dbvariety = "DVD";
				  System.out.println("ends");

				/*sb.append("　DVDカテゴリ詳細情報：" + "\n");
				sb.append("　　認識物体の名称：" + dvdData.getItemName() + "\n");
				sb.append("　　発売日：" + dvdData.getReleaseDate() + "\n");
				sb.append("　　年齢制限：" + dvdData.getAgeRequirement() + "\n");
				sb.append("　　出演者：" + dvdData.getActor() + "\n");
				sb.append("　　監督：" + dvdData.getDirector() + "\n");
				sb.append("　　発売元：" + dvdData.getMaker() + "\n");
				sb.append("　　販売元：" + dvdData.getSeller() + "\n");
				sb.append("　　形式：" + dvdData.getFormat() + "\n");
				sb.append("　　言語：" + dvdData.getLang() + "\n");
				sb.append("　　字幕の言語：" + dvdData.getCaptionLang() + "\n");
				sb.append("　　リージョンコード：" + dvdData.getRegionCode() + "\n");
				sb.append("　　収録時間：" + dvdData.getRunningTime() + "\n");
				sb.append("　　ディスク枚数：" + dvdData.getDiscs() + "\n");
				sb.append("　　EAN13桁コード：" + dvdData.getEan13() + "\n");
				sb.append("　　EAN8桁コード：" + dvdData.getEan8() + "\n");
*/


				allinfo.dbmanufactured  =  dvdData.getSeller();  //発売 or 販売 元
				allinfo.EAN13 = dvdData.getEan13() ;
				allinfo.EAN8 = dvdData.getEan8();

				List<String> actorlist  = null;



				if(dvdData.getActor() == null){
				 actorlist = dvdData.getActor();

				if(actorlist!=null){
					allinfo.dbactor =  (String)actorlist.get(0);
				}

				}else{


					 System.out.println("actorなかった");
				}
				allinfo.dbyear = dvdData.getReleaseDate();
				List<String> Directorlist = dvdData.getDirector();
				ttitle = dvdData.getItemName();

				if( Directorlist  == null){

					 allinfo.director = " ";

				}else{
					  allinfo.director = (String)Directorlist.get(0);
				}


				//sb.append("　　テスト：" + DVDtext + "\n");





			}
			else{

				  AlertDialog.Builder dlg;
				                   dlg = new AlertDialog.Builder(Camera.this);
				                   dlg.setTitle("読み込めませんでした");
				                   dlg.setMessage("これはDVD/CD/本/のうちですか？\nそうであれば、度映してください");

				                   dlg.setPositiveButton("Yes", null);

				                   dlg.show();

			}



		}






	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("ログ", "起動完了");

	
		
		
		
		Intent intent = getIntent();
		// intentから指定キーの文字列を取得する
		allinfo.dbuser_tablename = intent.getStringExtra("table");


		 System.out.println("allinfotablename = " + allinfo.dbuser_tablename);

		allinfo.dbuid = intent.getStringExtra("uid");
		 System.out.println("allinfouid = " + allinfo.dbuid );
		setContentView(R.layout.activity_recognition);
		Button btn = (Button) findViewById(R.id.lobt);
	//	_resultText = (EditText) findViewById(R.id.edit_result_regnize);
		progressBar1 = (ProgressBar)findViewById(R.id.ProgressBarHorizontal);
		progressBar1.setMax(100); // 水平プログレスバーの最大値を設定
		progressBar1.setProgress(10); // 水平プログレスバーの値を設定
	
		// API キーの登録
		AuthApiKey.initializeAuth(APIKEY);




		Log.d("ログ", "カメラ起動");
		String path = Environment.getExternalStorageDirectory()+ "/AAA/hoge.jpg";
		// 写真のファイル名
		String filename = "hoge.jpg";
		// 写真を保存するディレクトリ
		File dir = new File(Environment.getExternalStorageDirectory()+ "/AAA/");
		dir.mkdirs();
		File file = new File(dir, filename);
		Uri mImageUri = Uri.fromFile(file);
		Intent intents = new Intent();
		intents.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
		intents.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
		startActivityForResult(intents, REQUEST_CODE_CAMERA);
		// アンドロイドのデータベースへ登録
		registAndroidDB(path);


	}



		// 写真データベース更新
		private void registAndroidDB(String path) {
			// アンドロイドのデータベースへ登録)
			ContentValues values = new ContentValues();
			ContentResolver contentResolver = Camera.this.getContentResolver();
			values.put(Images.Media.MIME_TYPE, "image/jpeg");
			values.put("_data", path);
			contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);


			Log.d("ログ", "OK");

		}

	@Override
	public void onPause() {
		super.onPause();
		if (task != null) {
			task.cancel(true);
		}
	}
	@Override
	protected void onActivityResult(int requestCode,int resultCode,Intent data){
		if(requestCode == REQUEST_CODE_CAMERA){
			if(resultCode == RESULT_OK){
				pushExecButton();
			}
		}
	}

	private void pushExecButton() {
		AlertDialog.Builder dlg = new AlertDialog.Builder(this);
		// パラメータを設定する
		ImageRecognitionRequestParam requestParam = new ImageRecognitionRequestParam();
		requestParam.setRecog(Recog.ALL);
		// パラメータ取得 イメージパス
		String path = "/storage/emulated/0/AAA/hoge.jpg";
		requestParam.setFilePath(path);
		// 実行
		task = new RecognitionAsyncTask(dlg);
		task.execute(requestParam);
		Log.d("ログ", "実行完了");
	}






	class detail_CD extends Thread  {


	            public void run() {



	    	  		album = allinfo.dbname;
	    	  		artist= allinfo.dbartists;
	    		    Uri.Builder uriBuilder = new Uri.Builder();

	    		    uriBuilder.scheme(scheme);
	    		    uriBuilder.authority(authority);
	    		    uriBuilder.path(path);
	    		    uriBuilder.appendQueryParameter("method", method);
	    		    uriBuilder.appendQueryParameter("api_key", api_key);
	    		    uriBuilder.appendQueryParameter("album", album);
	    		    uriBuilder.appendQueryParameter("artist", artist);
	    		    uriBuilder.appendQueryParameter("format", format);

	    		    uri = uriBuilder.toString();
	    		     HttpClient httpClient = new DefaultHttpClient();
	    		    HttpParams params = httpClient.getParams();
	    		    HttpConnectionParams.setConnectionTimeout(params, 3000);
	    		    HttpConnectionParams.setSoTimeout(params, 3000);
	    		    HttpUriRequest httpRequest = new HttpGet(uri);
	    		    org.apache.http.HttpResponse httpResponse = null;



		    try {
		        httpResponse = httpClient.execute(httpRequest);
		    }
		    catch (ClientProtocolException e) {
		        //例外処理
		    }
		    catch (IOException e){
		        //例外処理
		    }

		    if (httpResponse != null && httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
		        HttpEntity httpEntity = httpResponse.getEntity();
		        try {
		            data = EntityUtils.toString(httpEntity);
		        }
		        catch (ParseException e) {
		            //例外処理
		        }
		        catch (IOException e) {
		            //例外処理
		        }
		        finally {
		            try {
		                httpEntity.consumeContent();
		            }
		            catch (IOException e) {
		                //例外処理
		            }
		        }
		    }


		    //JSON処理///////
		    try {
		    	System.out.println("1") ;
		    	JSONObject rootObject = new JSONObject(data);
		        System.out.println("2") ;
		        JSONObject jsonObject = rootObject.getJSONObject("album");

		        System.out.println("3") ;
		        allinfo.dbartists = jsonObject.getString("artist");
		        text_artist =jsonObject.getString("artist");

		        allinfo.dbalubam=jsonObject.getString("name");
			    System.out.println("4") ;
			    JSONObject jsonObject2 = jsonObject.getJSONObject("tracks");
			    System.out.println("qwert"+jsonObject2) ;
			    JSONArray jsonObject3 = jsonObject2.getJSONArray("track");
				for (int i = 0; i < 100; i++) {
					JSONObject jsonObject4 = jsonObject3.getJSONObject(i);
					if (jsonObject4.getString("name") != null) {
						allinfo.allmusic =allinfo.allmusic+jsonObject4.getString("name");// 曲名が配列に入る
						
						 System.out.println(i) ;
						    
						
						
					}
			    
			  
					 System.out.println("allinfo.allmusic:" +  allinfo.allmusic) ;
			    
			    
			    
			    
			    
			    
				}
			    
			    
			    
			    
			
		       
		    }
		    catch (JSONException e){
		        // 例外
		    }
		    httpClient.getConnectionManager().shutdown();
		    System.out.println("text_artist:"+text_artist) ;




			Connection con = null;
		    PreparedStatement ps = null;




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
			} catch (SQLException e2) {
				// TODO 自動生成された catch ブロック
				e2.printStackTrace();
			}

	         String maxcntsql = "select MAX(id) from " + allinfo.dbuser_tablename;

	         System.out.println(maxcntsql);

	         try {
					ps = con.prepareStatement(maxcntsql);
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

				int max = 0;

			   try {
			while(rs.next()) {


				  max = rs.getInt("MAX(id)");


			   }
			   } catch (SQLException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
			   }

			   newcnt = max+1;


			 if(allinfo.dbyear == null ){

				allinfo.dbyear = "0";


			 }
				Calendar timeSet = Calendar.getInstance();

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






	         String commenttable = "uid" + allinfo.dbuid + "id" + newcnt;
	         String sql = "INSERT INTO " + allinfo.dbuser_tablename +"(id,uid,title,director,year,cast,music,summary,note,picture,variety,EAN8,EAN13,manufactured,artists,author,wikipedia,ISBN13,ISBN10,ECURL,commenttable,video_id,opt,nowDate)values(" + newcnt + "," + allinfo.dbuid + ",'" + allinfo.dbalubam +"','"
	         		+ allinfo.director + "'," + allinfo.dbyear + ",'" + allinfo.dbactor + "'," +  "'なし'"+ ",'" + allinfo.dbsummary  + "','"+  allinfo.allmusic +"','"+allinfo.dbpicture+"','"+ allinfo.dbvariety + "','"+ allinfo.EAN13  +"','"+ allinfo.EAN8 +"','"+allinfo.dbmanufactured +"','"+ allinfo.dbartists+"','"
	         		+ allinfo.author +  "','"+ allinfo.wikipedia + "','"+ allinfo.ISBN13 +"','" + allinfo.ISBN10 + "','" + allinfo.dbECURL+ "','" + commenttable + "','null',"+ "'なし'," + nowDate +")";
	         System.out.println(sql);
	         String all_itemcntsql = "select MAX(id) from " + allinfo.dbuser_tablename;

	         System.out.println(all_itemcntsql);

	         try {
					ps = con.prepareStatement(all_itemcntsql);
				} catch (SQLException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}

	          // クエリーを実行して結果セットを取得
	           rs = null;
				try {
					rs = ps.executeQuery();
				} catch (SQLException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}

				int all_itemmax = 0;

			   try {
			while(rs.next()) {


				  all_itemmax = rs.getInt("MAX(id)");


			   }
			   } catch (SQLException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
			   }




			   String usrnamequery = "select screenname from all_user where uid = " + allinfo.dbuid;

		         System.out.println(usrnamequery);

		         try {
						ps = con.prepareStatement(usrnamequery);
					} catch (SQLException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}

		          // クエリーを実行して結果セットを取得
		           rs = null;
					try {
						rs = ps.executeQuery();
					} catch (SQLException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}


					String usrname = null;
				   try {
				while(rs.next()) {


					  usrname = rs.getString("screenname");


				   }
				   } catch (SQLException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
				   }


			int all_imtenewcnt = all_itemmax+1;









	         String all_itemquery = "INSERT INTO all_item(id,uid,iid,screenname,item_name,ArtistorAuthor,nowDate)VALUES("+all_imtenewcnt + "," + allinfo.dbuid +"," + newcnt +",'" +  usrname +"','" +  allinfo.dbalubam + "','" + allinfo.dbartists +"'," + nowDate  + ")";
	          System.out.println("all_item" + sql);




	         Statement  stmt1 = null;

	         try {
				int num = stmt.executeUpdate(sql);
				num = stmt.executeUpdate(all_itemquery);

	         } catch (SQLException e) {

				e.printStackTrace();
			}
	    	String commentablename = "uid" +allinfo.dbuid+"commenttable"+allinfo.dbname;
	     	String createcommenttable = "create table " +commenttable + "("
	     			+ "id int,uid int,comment text,twitter text,iconURL text,name text,nowDate bigint,opt text)";


	        try {
				int num = stmt.executeUpdate(createcommenttable);

	        } catch (SQLException e) {

				e.printStackTrace();
			}



	            }
	}

//////検索部分ここまで/////////////////////////////////////////////////////










	class detail_DVD extends Thread  {// 検索したいDVD名を入力することで,そのDVDのあらすじを返す




	            public void run() {

	            	URLCodec codec = new URLCodec();
	            	String titleencode = null;

	            	try {
					 titleencode	= codec.encode(ttitle, "UTF-8");
					 System.out.println("enco:" + titleencode);

					} catch (UnsupportedEncodingException e3) {
						// TODO 自動生成された catch ブロック
						e3.printStackTrace();
					}


	            	URL urls = null;
					try {
						urls = new URL("http://133.242.225.109/mecab.php?set="+ titleencode);

						  System.out.println("zxcv" + urls);

					} catch (MalformedURLException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}

				       HttpURLConnection urlconn = null;
					try {
						urlconn = (HttpURLConnection)urls.openConnection();
					} catch (IOException e1) {
						// TODO 自動生成された catch ブロック
						e1.printStackTrace();
					}
				        try {
							urlconn.setRequestMethod("GET");
						} catch (ProtocolException e) {
							// TODO 自動生成された catch ブロック
							e.printStackTrace();
						}
				        urlconn.setInstanceFollowRedirects(false);
				         urlconn.setRequestProperty("Accept-Language", "ja,en-US;q=0.8,en;q=0.6");

				         try {
							urlconn.connect();
						} catch (IOException e) {
							// TODO 自動生成された catch ブロック
							e.printStackTrace();
						}

				         Map headers = urlconn.getHeaderFields();
				        Iterator it = headers.keySet().iterator();
				        System.out.println("レスポンスヘッダ:");
			        while (it.hasNext()){
				            String key= (String)it.next();
				            System.out.println("  " + key + ": " + headers.get(key));
				        }
			        BufferedReader reader = null;
					try {
						reader = new BufferedReader(new InputStreamReader(urlconn.getInputStream()));
					} catch (IOException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}

			        		            while (true){
			        		                String line = null;
											try {
												line = reader.readLine();
											} catch (IOException e) {
												// TODO 自動生成された catch ブロック
												e.printStackTrace();
											}
			        		                if ( line == null ){
			        		                    break;
			        		                }


			        		                titles = line;


			        		           }

			        		            try {
											reader.close();
										} catch (IOException e) {
											// TODO 自動生成された catch ブロック
											e.printStackTrace();
										}
			        		           urlconn.disconnect();




			try {
				encodeStr = URLEncoder.encode(titles, "utf-8");// DVD名をUTF-8にエンコード
				url = "http://ja.wikipedia.org/wiki/" + encodeStr;// wikiのurl作成
				System.out.println(url);
				document = Jsoup.connect(url).get();// 上記urlからdocumentを入手
			} catch (IOException e) {
				try {
					url = "http://ja.wikipedia.org/wiki/" + encodeStr+"&redirect=no";// wikiのurl作成
					document = Jsoup.connect(url).get();// 上記urlからdocumentを入手

					if (document.toString().indexOf("転送先") != -1) {// 曖昧さ回避ページにリンクしている場合
						System.out.println("patternC");
						Elements content_a_tensou = document.getElementsByTag("a");// a要素のみを抽出
						dest_a_tensou = content_a_tensou.toString().split("<a href=\"");// 検索結果をリンク部分ごとに分割
						int dest_target_tensou=0;
						for(int i =0;i<dest_a_tensou.length;i++){
							if (dest_a_tensou[i].toString().indexOf("titles=\"+word") != -1) {// 題名を含む場合
								dest_target_tensou=i;
							}
						}
						dest_url = dest_a_tensou[dest_target_tensou].toString().split("\"");// 検索結果をリンク部分ごとに分割
						url="http://ja.wikipedia.org"+dest_url[0];
						try {
							document = Jsoup.connect(url).get();// 上記urlからdocumentを入手
						} catch (IOException e2) {
							System.out.println("2");
							return;
						}

					}
				} catch (IOException e2) {

					return;
				}
			}
			if (document.toString().indexOf("その他の用法については") != -1) {// 曖昧さ回避ページにリンクしている場合
				System.out.println("patternA");
				Elements content_tr = document.getElementsByTag("tr");// tr要素のみを抽出
				Document doc_temp = Jsoup.parse(content_tr.toString());
				Elements content_a = doc_temp.getElementsByTag("a");// a要素のみを抽出
				dest_a2 = content_a.toString().split("<a href=\"");// 検索結果をリンク部分ごとに分割
				int dest_target=0;
				for(int i =0;i<dest_a2.length;i++){
					if (dest_a2[i].toString().indexOf("class=\"mw-disambig\"") != -1) {// 題名を含む場合
						dest_target=i;
					}
				}
				dest_url = dest_a2[dest_target].toString().split("\"");// 検索結果をリンク部分ごとに分割
				url="http://ja.wikipedia.org"+dest_url[0];

				allinfo.wikipedia = url;

				//System.out.println("bbbbbbbbbbbbbbbbbbbbbbb");
				//System.out.println(url);
				try {
					document = Jsoup.connect(url).get();// 上記urlからdocumentを入手
				} catch (IOException e) {
					return;
				}

			}
			if (document.toString().indexOf("曖昧さ回避のためのページ") != -1) {// 曖昧さ回避ページの場合
				System.out.println("patternB");
				// 部分一致です
				Element body = document.getElementById("content");// document内のbody部分を抽出
				Elements content = body.getElementsByTag("li");// 見出しからli要素(曖昧さの検索結果それぞれ)のみを抽出
				dest_li = content.toString().split("<li>");// 曖昧さの検索結果をそれぞれに分割
				for (int i = 0; i < dest_li.length; i++) {
					if (dest_li[i].indexOf("映画") != -1&&dest_li[i].indexOf("<ul>") == -1) {// 映画に該当する項目を検索
						// 部分一致です
						//System.out.println(i);
						Document doc = Jsoup.parse(dest_li[i]);// 映画に該当する項目をhtmlとして再形成
						Elements a_content = doc.getElementsByTag("a");// 　a要素(リンク部分)のみ抽出
						dest_a = a_content.toString().split(">");// 検索結果をリンク部分ごとに分割
						// 最初のリンク部分(おそらく映画ページへのリンクとなっている)をターゲットにする
						dest_a[0] = dest_a[0].replaceAll("<a href=", "");// 不要部分の除去
					//	System.out.println(dest_a[0]);
					//	System.out.println(dest_li[i]);
						//System.out.println(i);
						dest_link = dest_a[0].split("\"");// 検索結果をリンク部分ごとに分割
						url_temp = "http://ja.wikipedia.org" + dest_link[1];// wikiのurl作成
						//System.out.println(url_temp);
						try {
							document = Jsoup.connect(url_temp).get();// 上記urlからdocumentを入手
							if(  allinfo.director==null){
								return;
							}
							else if(  allinfo.director==""){
								return;
							}
							else{
								if (document.toString().indexOf(allinfo.director) != -1) {// 映画に該当する項目を検索
									url=url_temp;
								}
							}
						} catch (IOException e) {
							//e.printStackTrace();
						}


					} else {
						// 部分一致ではありません
					}
				}

			}
			try {
				document = Jsoup.connect(url).get();// 上記urlからdocumentを入手
				allinfo.wikipedia = url;
			} catch (IOException e) {
				return;
			}

			Element body = document.body();// document内のbody部分を抽出
			System.out.println(body);
			//System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
			//System.out.println(body);
			dest4 = body.toString().split(
					"<h2><span class=\"mw-headline\"");// body要素を<h2><span
														// class=\"mw-headline\"で分割(見出し単位)
			for (int i = 0; i < dest4.length; i++) {
				if (dest4[i].indexOf(">あらすじ<") != -1) {// あらすじに該当する見出しを検索
					// 部分一致です
					// System.out.println(i+"// 部分一致です") ;
					target = i;
				} else if (dest4[i].indexOf(">概要<") != -1) {// あらすじに該当する見出しを検索
					// 部分一致です
					// System.out.println(i+"// 部分一致です") ;
					target = i;
				} else if (dest4[i].indexOf(">ストーリー<") != -1) {// あらすじに該当する見出しを検索
					// 部分一致です
					// System.out.println(i+"// 部分一致です") ;
					target = i;
				} else {
					// 部分一致ではありません

				}
			}
			if(target==-1){
				return;
			}
			Document doc = Jsoup.parse("<h2><span class=\"mw-headline\""
					+ dest4[target]);// あらすじに該当する見出しを抽出しhtmlとして再形成
			final Elements content2 = doc.getElementsByTag("p");// 見出しからp要素のみを抽出(ほぼ文章)	
			
		allinfo.dbsummary =  content2.text();
		
		System.out.println("allinfo.dbsummary" + allinfo.dbsummary );
		
			
		//スレッド動機がうまくいかないので
		//本専用DB



		Connection con = null;
	    PreparedStatement ps = null;





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
		} catch (SQLException e2) {
			// TODO 自動生成された catch ブロック
			e2.printStackTrace();
		}

         String maxcntsql = "select MAX(id) from " + allinfo.dbuser_tablename;

         System.out.println(maxcntsql);

         try {
				ps = con.prepareStatement(maxcntsql);
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

			int max = 0;

		   try {
		while(rs.next()) {


			  max = rs.getInt("MAX(id)");


		   }
		   } catch (SQLException e1) {
		// TODO 自動生成された catch ブロック
		e1.printStackTrace();
		   }

		 newcnt = max+1;


		 if(allinfo.dbyear == null ){

			allinfo.dbyear = "不明";


		 }
			Calendar timeSet = Calendar.getInstance();

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



		allinfo.newcount = newcnt;

		String commenttable = "uid" + allinfo.dbuid + "id" + newcnt;
         String sql = "INSERT INTO " + allinfo.dbuser_tablename +"(id,uid,title,director,year,cast,music,summary,note,picture,variety,EAN8,EAN13,manufactured,artists,author,wikipedia,ISBN13,ISBN10,ECURL,commenttable,video_id,opt,nowDate)values(" + newcnt + "," + allinfo.dbuid + ",'" + titles +"','"
         		+ allinfo.director + "'," + allinfo.dbyear + ",'" + allinfo.dbactor + "'," +  "'なし'"+ ",'" + allinfo.dbsummary  + "',"+  "'なし'"+",'"+allinfo.dbpicture+"','"+ allinfo.dbvariety + "','"+ allinfo.EAN13  +"','"+ allinfo.EAN8 +"','"+allinfo.dbmanufactured +"','"+ allinfo.dbartists+"','"
         		+ allinfo.author +  "','"+ allinfo.wikipedia + "','"+ allinfo.ISBN13 +"','" + allinfo.ISBN10 + "','" + allinfo.dbECURL+ "','" + commenttable +"','null',"+ "'なし'," + nowDate +")";
         System.out.println(sql);
         Statement  stmt1 = null;

         String all_itemcntsql = "select MAX(id) from all_item";

         System.out.println(all_itemcntsql);

         try {
				ps = con.prepareStatement(all_itemcntsql);
			} catch (SQLException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}

          // クエリーを実行して結果セットを取得
           rs = null;
			try {
				rs = ps.executeQuery();
			} catch (SQLException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}

			int all_itemmax = 0;

		   try {
		while(rs.next()) {


			  all_itemmax = rs.getInt("MAX(id)");


		   }
		   } catch (SQLException e1) {
		// TODO 自動生成された catch ブロック
		e1.printStackTrace();
		   }




		   String usrnamequery = "select screenname from all_user where uid = " + allinfo.dbuid;

	         System.out.println(usrnamequery);

	         try {
					ps = con.prepareStatement(usrnamequery);
				} catch (SQLException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}

	          // クエリーを実行して結果セットを取得
	           rs = null;
				try {
					rs = ps.executeQuery();
				} catch (SQLException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}


				String usrname = null;
			   try {
			while(rs.next()) {


				  usrname = rs.getString("screenname");


			   }
			   } catch (SQLException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
			   }


		int all_imtenewcnt = all_itemmax+1;









	    String all_itemquery = "INSERT INTO all_item(id,uid,iid,screenname,item_name,ArtistorAuthor,nowDate)VALUES("+all_imtenewcnt + "," + allinfo.dbuid +"," + newcnt +",'" +  usrname +"','" + titles + "','" + allinfo.dbartists +"'," + nowDate  + ")";
 
         try {
			int num = stmt.executeUpdate(sql);
			num = stmt.executeUpdate(all_itemquery);

         } catch (SQLException e) {

			e.printStackTrace();
		}

    	String commentablename = "uid" +allinfo.dbuid+"commenttable"+allinfo.dbname;
     	String createcommenttable = "create table " +commenttable + "("
     			+ "id int,uid int,comment text,twitter text,iconURL text,name text,nowDate bigint,opt text)";


        try {
			int num = stmt.executeUpdate(createcommenttable);

        } catch (SQLException e) {

			e.printStackTrace();
		}








				}



	}





	class detail_book extends Thread  {

	            public void run() {



	             	URLCodec codec = new URLCodec();
	            	String titleencode = null;

	            	try {
					 titleencode	= codec.encode(word, "UTF-8");
					 System.out.println("enco:" + titleencode);

					} catch (UnsupportedEncodingException e3) {
						// TODO 自動生成された catch ブロック
						e3.printStackTrace();
					}


	            	URL urls = null;
					try {
						urls = new URL("http://133.242.225.109/mecab.php?set="+ titleencode);

						  System.out.println("zxcv" + urls);

					} catch (MalformedURLException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}

				       HttpURLConnection urlconn = null;
					try {
						urlconn = (HttpURLConnection)urls.openConnection();
					} catch (IOException e1) {
						// TODO 自動生成された catch ブロック
						e1.printStackTrace();
					}
				        try {
							urlconn.setRequestMethod("GET");
						} catch (ProtocolException e) {
							// TODO 自動生成された catch ブロック
							e.printStackTrace();
						}
				        urlconn.setInstanceFollowRedirects(false);
				         urlconn.setRequestProperty("Accept-Language", "ja,en-US;q=0.8,en;q=0.6");

				         try {
							urlconn.connect();
						} catch (IOException e) {
							// TODO 自動生成された catch ブロック
							e.printStackTrace();
						}

				         Map headers = urlconn.getHeaderFields();
				        Iterator it = headers.keySet().iterator();
				        System.out.println("レスポンスヘッダ:");
			        while (it.hasNext()){
				            String key= (String)it.next();
				            System.out.println("  " + key + ": " + headers.get(key));
				        }
			        BufferedReader reader = null;
					try {
						reader = new BufferedReader(new InputStreamReader(urlconn.getInputStream()));
					} catch (IOException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}

			        		            while (true){
			        		                String line = null;
											try {
												line = reader.readLine();
											} catch (IOException e) {
												// TODO 自動生成された catch ブロック
												e.printStackTrace();
											}
			        		                if ( line == null ){
			        		                    break;
			        		                }


			        		                word = line;
			        		                System.out.println("word:" + word) ;

			        		           }

			        		            try {
											reader.close();
										} catch (IOException e) {
											// TODO 自動生成された catch ブロック
											e.printStackTrace();
										}
			        		           urlconn.disconnect();


		String detail_text="";
		String text="";
		String data = "";
	 	String scheme = "https";
        String authority = "www.googleapis.com";
        String path = "books/v1/volumes";
        String query = word;//検索したい本の名前

        Uri.Builder uriBuilder = new Uri.Builder();
      
        uriBuilder.scheme(scheme);
        uriBuilder.authority(authority);
        uriBuilder.path(path);
        uriBuilder.appendQueryParameter("q", query);

        String uri = uriBuilder.toString();
        HttpClient httpClient = new DefaultHttpClient();
        HttpParams params = httpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, 3000);
        HttpConnectionParams.setSoTimeout(params, 3000);
        HttpUriRequest httpRequest = new HttpGet(uri);
        org.apache.http.HttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpRequest);
        }
        catch (ClientProtocolException e) {
            //例外処理
        }
        catch (IOException e){
            //例外処理
        }

        if (httpResponse != null && httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
            HttpEntity httpEntity = httpResponse.getEntity();
            try {
                data = EntityUtils.toString(httpEntity);
            }
            catch (ParseException e) {
                //例外処理
            	   System.out.println("エラー1") ;
            }
            catch (IOException e) {
                //例外処理
            	 System.out.println("エラー2") ;
            }
            finally {
                try {
                    httpEntity.consumeContent();
                }
                catch (IOException e) {
                    //例外処理
                	 System.out.println("エラー3") ;
                }
            }
        }
        //JSON処理///////
        try {
        	System.out.println("あいうえ") ;
        	JSONObject rootObject = new JSONObject(data);
          //  System.out.println("2") ;
            JSONArray jsonObject = rootObject.getJSONArray("items");
         //   System.out.println("3") ;
            JSONObject jsonObject2 = jsonObject.getJSONObject(0);
         //   System.out.println("4") ;
            JSONObject jsonObject3 = jsonObject2.getJSONObject("volumeInfo");
           // System.out.println("5") ;
            detail_text =jsonObject3.getString("description");//概要がdetail_textにはいる
           System.out.println(" detail_text" +  detail_text) ;
           googlebookid=jsonObject2.getString("id");
           System.out.println("https://books.google.co.jp/books?id=" + googlebookid +"&printsec=frontcover&hl=ja&ved=#v=onepage&q&f=true");
           if(allinfo.author ==  null){


        	   allinfo.author  =jsonObject3.getString("authors");

        	   allinfo.author =   allinfo.author.replaceAll("[\\[\\]\"]","");

           }



        }
        catch (JSONException e){

        	//エラーダイアログ
        	 System.out.println("エラー4") ;


        }
        httpClient.getConnectionManager().shutdown();
        allinfo.dbsummary = detail_text;




		Connection con = null;
	    PreparedStatement ps = null;





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
		} catch (SQLException e2) {
			// TODO 自動生成された catch ブロック
			e2.printStackTrace();
		}

         String maxcntsql = "select MAX(id) from " + allinfo.dbuser_tablename;

         System.out.println(maxcntsql);

         try {
				ps = con.prepareStatement(maxcntsql);
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

			int max = 0;

		   try {
		while(rs.next()) {


			  max = rs.getInt("MAX(id)");


		   }
		   } catch (SQLException e1) {
		// TODO 自動生成された catch ブロック
		e1.printStackTrace();
		   }

		 newcnt = max+1;


		 if(allinfo.dbyear == null ){

			allinfo.dbyear = "不明";


		 }
			Calendar timeSet = Calendar.getInstance();

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




		allinfo.newcount = newcnt;

		titles = word;

		String commenttable = "uid" + allinfo.dbuid + "id" + newcnt;
         String sql = "INSERT INTO " + allinfo.dbuser_tablename +"(id,uid,title,director,year,cast,music,summary,note,picture,variety,EAN8,EAN13,manufactured,artists,author,wikipedia,ISBN13,ISBN10,ECURL,commenttable,video_id,opt,nowDate)values(" + newcnt + "," + allinfo.dbuid + ",'" + titles +"','"
         		+ allinfo.director + "'," + allinfo.dbyear + ",'" + allinfo.dbactor + "'," +  "'なし'"+ ",'" + allinfo.dbsummary  + "',"+  "'なし'"+",'"+allinfo.dbpicture+"','"+ allinfo.dbvariety + "','"+ allinfo.EAN13  +"','"+ allinfo.EAN8 +"','"+allinfo.dbmanufactured +"','"+ allinfo.dbartists+"','"
         		+ allinfo.author +  "','"+ allinfo.wikipedia + "','"+ allinfo.ISBN13 +"','" + allinfo.ISBN10 + "','" + allinfo.dbECURL+ "','" + commenttable + "','null',"+ "'なし'," + nowDate +")";
         System.out.println(sql);



         String all_itemcntsql = "select MAX(id) from " + allinfo.dbuser_tablename;

         System.out.println(all_itemcntsql);

         try {
				ps = con.prepareStatement(all_itemcntsql);
			} catch (SQLException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}

          // クエリーを実行して結果セットを取得
           rs = null;
			try {
				rs = ps.executeQuery();
			} catch (SQLException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}

			int all_itemmax = 0;

		   try {
		while(rs.next()) {


			  all_itemmax = rs.getInt("MAX(id)");


		   }
		   } catch (SQLException e1) {
		// TODO 自動生成された catch ブロック
		e1.printStackTrace();
		   }




		   String usrnamequery = "select screenname from all_user where uid = " + allinfo.dbuid;

	         System.out.println(usrnamequery);

	         try {
					ps = con.prepareStatement(usrnamequery);
				} catch (SQLException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}

	          // クエリーを実行して結果セットを取得
	           rs = null;
				try {
					rs = ps.executeQuery();
				} catch (SQLException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}


				String usrname = null;
			   try {
			while(rs.next()) {


				  usrname = rs.getString("screenname");
				  System.out.println("screenname" + usrname);

			   }
			   } catch (SQLException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
			   }


		int all_imtenewcnt = all_itemmax+1;
		System.out.println("titles = " +titles);


	    String all_itemquery = "INSERT INTO all_item(id,uid,iid,screenname,item_name,ArtistorAuthor,nowDate)VALUES("+all_imtenewcnt + "," + allinfo.dbuid +"," + newcnt +",'" +  usrname +"','" + titles + "','" + allinfo.dbartists +"'," + nowDate  + ")";

         try {
			int num = stmt.executeUpdate(sql);
			num = stmt.executeUpdate(all_itemquery);

         } catch (SQLException e) {

			e.printStackTrace();
		}


    	String commentablename = "uid" +allinfo.dbuid+"commenttable"+allinfo.dbname;
     	String createcommenttable = "create table " +commenttable + "("
     			+ "id int,uid int,comment text,twitter text,iconURL text,name text,nowDate bigint,opt text)";


        try {
			int num = stmt.executeUpdate(createcommenttable);

        } catch (SQLException e) {

			e.printStackTrace();
		}

}






	}





}


