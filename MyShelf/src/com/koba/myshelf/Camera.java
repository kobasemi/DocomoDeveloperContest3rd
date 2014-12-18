package com.koba.myshelf;

import java.io.File;
import java.util.List;

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
import jp.ne.docomo.smt.dev.imagerecognition.data.ImageRecognitionFoodDetailData;
import jp.ne.docomo.smt.dev.imagerecognition.data.ImageRecognitionGameDetailData;
import jp.ne.docomo.smt.dev.imagerecognition.data.ImageRecognitionRelatedContentData;
import jp.ne.docomo.smt.dev.imagerecognition.data.ImageRecognitionResultData;
import jp.ne.docomo.smt.dev.imagerecognition.data.ImageRecognitionSiteData;
import jp.ne.docomo.smt.dev.imagerecognition.data.ImageRecognitionSoftwareDetailData;
import jp.ne.docomo.smt.dev.imagerecognition.param.ImageRecognitionRequestParam;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.util.Log;

/**
 * 画像認識要求画面 画像認識要求を実行して結果を取得するサンプルページ
 */
public class Camera extends Activity implements View.OnClickListener {
	// カメラで使う変数
	private static final int REQUEST_CODE_CAMERA = 2;
	static final String APIKEY = "423577364e5936686663345234726857726a6c6e332e43737678454354727675753352594e33652f4c7744";
	private RecognitionAsyncTask task;
	// 認識ジョブの識別ID
	private ImageRecognitionResultData _resultData = null;
	// インテントキー
	static final String INTENT_SEND_KEY = "ImageRecognizeResultData";
	static final String INTENT_RECOGNITIONID_KEY = "recognitionid";
	static final String INTENT_ITEMID_KEY = "itemid";
	// 結果表示
	private EditText _resultText;

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
				sb.append("認識ジョブの識別ID：" + resultData.getRecognitionId() + "\n");
				List<ImageRecognitionCandidateData> candidateList = resultData
						.getCandidateDataList();
				if (candidateList == null)
					return;
				for (ImageRecognitionCandidateData candidateData : candidateList) {
					sb.append("　認識結果候補のスコア：" + candidateData.getScore() + "\n");
					sb.append("　認識結果候補のID：" + candidateData.getItemId() + "\n");
					sb.append("　認識結果候補のカテゴリ：" + candidateData.getCategory()
							+ "\n");
					sb.append("　認識結果候補の画像のURL：" + candidateData.getImageUrl()
							+ "\n");

					ImageRecognitionDetailData detailData = candidateData
							.getDetailData();
					sb.append(printDetailData(detailData));

					List<ImageRecognitionSiteData> siteList = candidateData
							.getSiteDataList();
					if (siteList != null) {
						sb.append("　認識結果候補の物体に関連するサイト情報：" + "\n");
						for (ImageRecognitionSiteData siteData : siteList) {
							sb.append("　　ECサイトのURL：" + siteData.getUrl() + "\n");
							sb.append("　　ECサイトのタイトル：" + siteData.getTitle()
									+ "\n");
							sb.append("　　ECサイトの画像のURL："
									+ siteData.getImageUrl() + "\n");
						}
					} else {
						sb.append("　認識結果候補の物体に関連するサイト情報：なし" + "\n");
					}

					List<ImageRecognitionRelatedContentData> relatedList = candidateData
							.getRelatedContentDataList();
					if (relatedList != null) {
						sb.append("　認識結果候補の物体に関連するコンテンツ：" + "\n");
						for (ImageRecognitionRelatedContentData relatedData : relatedList) {
							sb.append("　　関連コンテンツのURL：" + relatedData.getUrl()
									+ "\n");
							sb.append("　　関連コンテンツのタイトル："
									+ relatedData.getTitle() + "\n");
							sb.append("　　関連コンテンツの画像のURL："
									+ relatedData.getImageUrl() + "\n");
							sb.append("　　関連コンテンツの概要："
									+ relatedData.getAbstract() + "\n");
						}
					} else {
						sb.append("　認識結果候補の物体に関連するコンテンツ：なし" + "\n");
					}
				}
				_resultText.setText(sb.toString());
				// ソフトキーボードを非表示にする
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(_resultText.getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
				_resultData = resultData;
			}
		}

		/**
		 * カテゴリ毎の詳細情報を出力する
		 * 
		 * @param detailData
		 *            詳細情報
		 */
		private String printDetailData(ImageRecognitionDetailData detailData) {
			StringBuffer sb = new StringBuffer();
			if (detailData instanceof ImageRecognitionBookDetailData) {
				sb.append("　書籍カテゴリ詳細情報：" + "\n");
				ImageRecognitionBookDetailData bookData = (ImageRecognitionBookDetailData) detailData;
				sb.append("　　認識物体の名称： " + bookData.getItemName() + "\n");
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
				sb.append("　　言語： " + bookData.getLang() + "\n");
			} else if (detailData instanceof ImageRecognitionCdDetailData) {
				ImageRecognitionCdDetailData cdData = (ImageRecognitionCdDetailData) detailData;
				sb.append("　CDカテゴリ詳細情報：" + "\n");
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
			} else if (detailData instanceof ImageRecognitionDvdDetailData) {
				ImageRecognitionDvdDetailData dvdData = (ImageRecognitionDvdDetailData) detailData;
				sb.append("　DVDカテゴリ詳細情報：" + "\n");
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
			} else if (detailData instanceof ImageRecognitionGameDetailData) {
				ImageRecognitionGameDetailData gameData = (ImageRecognitionGameDetailData) detailData;
				sb.append("　TVゲームソフトカテゴリ詳細情報：" + "\n");
				sb.append("　　認識物体の名称：" + gameData.getItemName() + "\n");
				sb.append("　　発売日：" + gameData.getReleaseDate() + "\n");
				sb.append("　　年齢制限：" + gameData.getAgeRequirement() + "\n");
				sb.append("　　プラットフォーム：" + gameData.getPlatform() + "\n");
				sb.append("　　CEROレーティング：" + gameData.getCeroRating() + "\n");
				sb.append("　　メーカー：" + gameData.getMaker() + "\n");
				sb.append("　　販売元：" + gameData.getSeller() + "\n");
				sb.append("　　EAN13桁コード：" + gameData.getEan13() + "\n");
				sb.append("　　EAN8桁コード：" + gameData.getEan8() + "\n");
			} else if (detailData instanceof ImageRecognitionSoftwareDetailData) {
				ImageRecognitionSoftwareDetailData softData = (ImageRecognitionSoftwareDetailData) detailData;
				sb.append("　PCソフトカテゴリ詳細情報：" + "\n");
				sb.append("　　認識物体の名称：" + softData.getItemName() + "\n");
				sb.append("　　発売日：" + softData.getReleaseDate() + "\n");
				sb.append("　　年齢制限：" + softData.getAgeRequirement() + "\n");
				sb.append("　　プラットフォーム：" + softData.getPlatform() + "\n");
				sb.append("　　メディア：" + softData.getMedia() + "\n");
				sb.append("　　動作環境：" + softData.getSystemRequirements() + "\n");
				sb.append("　　メーカー：" + softData.getMaker() + "\n");
				sb.append("　　販売元：" + softData.getSeller() + "\n");
				sb.append("　　EAN13桁コード：" + softData.getEan13() + "\n");
				sb.append("　　EAN8桁コード：" + softData.getEan8() + "\n");
			} else if (detailData instanceof ImageRecognitionFoodDetailData) {
				ImageRecognitionFoodDetailData foodData = (ImageRecognitionFoodDetailData) detailData;
				sb.append("　食品パッケージカテゴリ詳細情報：" + "\n");
				sb.append("　　認識物体の名称：" + foodData.getItemName() + "\n");
				sb.append("　　発売日：" + foodData.getReleaseDate() + "\n");
				sb.append("　　年齢制限：" + foodData.getAgeRequirement() + "\n");
				sb.append("　　メーカー：" + foodData.getMaker() + "\n");
				sb.append("　　販売元：" + foodData.getSeller() + "\n");
				sb.append("　　ブランド：" + foodData.getBrand() + "\n");
				sb.append("　　商品の寸法：" + foodData.getDimension() + "\n");
				sb.append("　　商品の重量：" + foodData.getWeight() + "\n");
				sb.append("　　内容量：" + foodData.getQuantity() + "\n");
				sb.append("　　保存方法：" + foodData.getPreservation() + "\n");
				sb.append("　　産地：" + foodData.getProducingArea() + "\n");
				sb.append("　　EAN13桁コード：" + foodData.getEan13() + "\n");
				sb.append("　　EAN8桁コード：" + foodData.getEan8() + "\n");
			}
			return sb.toString();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("ログ", "起動完了");
		
		setContentView(R.layout.activity_recognition);
		Button btn = (Button) findViewById(R.id.button1);
		_resultText = (EditText) findViewById(R.id.edit_result_regnize);
		btn.setOnClickListener(this);
		// API キーの登録
		AuthApiKey.initializeAuth(APIKEY);
		
		
	}
	

		public void onClick(View v) {
			Log.d("ログ", "カメラ起動");
			String path = Environment.getExternalStorageDirectory()+ "/AAA/hoge.jpg";
			// 写真のファイル名
			String filename = "hoge.jpg";
			// 写真を保存するディレクトリ
			File dir = new File(Environment.getExternalStorageDirectory()+ "/AAA/");
			dir.mkdirs();
			File file = new File(dir, filename);
			Uri mImageUri = Uri.fromFile(file);
			Intent intent = new Intent();
			intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
			startActivityForResult(intent, REQUEST_CODE_CAMERA);
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
}

