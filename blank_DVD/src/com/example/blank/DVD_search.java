package com.example.blank;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
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
import android.content.Context;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class DVD_search extends Activity implements View.OnClickListener {
	LinearLayout layout;
	Handler handler = new Handler();
	TextView textView;
	ScrollView scrollView;
	TextView tv;
	EditText edit;
	Button image_button;

	SpannableStringBuilder sb;

	String str;
	String detail_text = "";
	private InputMethodManager inputMethodManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		DisplayMetrics metrics = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(metrics);

		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);

		tv = new TextView(this);
		tv.setText("検索したい言葉を入力してください");
		tv.setHeight(100);
		tv.setWidth(200);
		tv.setPadding(0, 0, 0, 20);
		tv.setId(2);
		tv.setGravity(Gravity.CENTER);
		layout.addView(tv);

		edit = new EditText(this);
		edit.setHeight(50);
		edit.setWidth(200);
		edit.setId(1);
		edit.setGravity(Gravity.CENTER);
		layout.addView(edit);

		image_button = new Button(this);
		image_button.setText("検索");
		image_button.setOnClickListener(this);
		layout.addView(image_button);

		textView = new TextView(this);

		scrollView = new ScrollView(this);
		scrollView.addView(textView);
		layout.addView(scrollView);

		setContentView(layout);
	}

	public void onClick(View v) {
		if (v == image_button) {
			sb = (SpannableStringBuilder) edit.getText();
			str = sb.toString();
			detail_text = detail(str);
			textView.setText(detail_text);
			inputMethodManager.hideSoftInputFromWindow(layout.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
			layout.requestFocus();
		}
	}

	// /////ここから映画検索部分//////////////////////////////////////////////////////////////
	String detail(String word) {// 検索したいDVD名を入力することで,その本のあらすじを返す
		String text = "";
		String query = word;// 検索したいDVDの名前
		int target = 0;

		try {
			String encodeStr = URLEncoder.encode(word, "utf-8");// DVD名をUTF-8にエンコード
			String url = "http://ja.wikipedia.org/wiki/" + encodeStr;// wikiのurl作成
			Document document = Jsoup.connect(url).get();// 上記urlからdocumentを入手
			Element body = document.body();// document内のbody部分を抽出

			System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
			String[] dest = body.toString().split(
					"<h2><span class=\"mw-headline\"");// body要素を<h2><span
														// class=\"mw-headline\"で分割(見出し単位)
			for (int i = 0; i < dest.length; i++) {
				if (dest[i].indexOf("あらすじ") != -1) {// あらすじに該当する見出しを検索
					// 部分一致です
					// System.out.println(i+"// 部分一致です") ;
					target = i;
				} else if (dest[i].indexOf("概要") != -1) {// あらすじに該当する見出しを検索
					// 部分一致です
					// System.out.println(i+"// 部分一致です") ;
					target = i;
				} else {
					// 部分一致ではありません
				}
			}
			Document doc = Jsoup.parse("<h2><span class=\"mw-headline\""
					+ dest[target]);// あらすじに該当する見出しを抽出しhtmlとして再形成
			Elements content = doc.getElementsByTag("p");// 見出しからp要素のみを抽出(ほぼ文章)
			text = content.text();
			System.out.println(content.text());// 抽出したp要素からタグ等を除去
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;

	}

	// ////検索部分ここまで/////////////////////////////////////////////////////

	// /////曖昧さ部分　（没）//////////////////////////////////////////////////////////////
	String detail_aimai(String word) {// 検索したいDVD名を入力することで,その本のあらすじを返す
		String text = "";
		String query = word;// 検索したいDVDの名前
		int target = 0;

		//
		try {
			String encodeStr = URLEncoder.encode("ゲーム_(曖昧さ回避)", "utf-8");// DVD名をUTF-8にエンコード
			String url = "http://ja.wikipedia.org/wiki/" + encodeStr;// wikiのurl作成
			Document document = Jsoup.connect(url).get();// 上記urlからdocumentを入手
			Element body = document.getElementById("content");// document内のbody部分を抽出
			Elements content = body.getElementsByTag("li");// 見出しからli要素
															// (曖昧さの検索結果それぞれ)のみを抽出
			String[] dest = body.toString().split("<li>");// 曖昧さの検索結果をそれぞれに分割

			for (int i = 0; i < dest.length; i++) {
				if (dest[i].indexOf("映画") != -1) {// 映画に該当する項目を検索
					// 部分一致です
					System.out.println(i);
					Document doc = Jsoup.parse(dest[i]);// 映画に該当する項目をhtmlとして再形成
					Elements a_content = doc.getElementsByTag("a");// 　a要素(内容部分)のみ抽出
					String[] dest2 = a_content.toString().split(">");// 検索結果をリンク部分ごとに分割
					// 最初のリンク部分(おそらく映画ページへのリンクとなっている)をターゲットにする
					dest2[0] = dest2[0].replaceAll("<a href=", "");// 不要部分の除去
					System.out.println(dest2[0]);
					target = i;
				} else {
					// 部分一致ではありません
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;

	}

	// ////検索部分ここまで/////////////////////////////////////////////////////

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onStop() {
		super.onStop();

	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
	}

}
