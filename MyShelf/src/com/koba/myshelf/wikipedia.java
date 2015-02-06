package com.koba.myshelf;
import java.io.IOException;
import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Context;
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

public class wikipedia extends Activity implements View.OnClickListener {
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
			detail_text = detail_DVD(str,"宮崎駿");//映画のタイトルと監督名を入力
			textView.setText(detail_text);
			inputMethodManager.hideSoftInputFromWindow(layout.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
			layout.requestFocus();
		}
	}
	// /////ここから映画検索部分//////////////////////////////////////////////////////////////
	String detail_DVD(String title,String kantoku) {// 検索したいDVD名を入力することで,そのDVDのあらすじを返す
		String text = "";
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
		String url;
		String encodeStr=null;
		
			try {
				encodeStr = URLEncoder.encode(title, "utf-8");// DVD名をUTF-8にエンコード
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
							if (dest_a_tensou[i].toString().indexOf("title=\"+word") != -1) {// 題名を含む場合
								dest_target_tensou=i;
							}
						}
						dest_url = dest_a_tensou[dest_target_tensou].toString().split("\"");// 検索結果をリンク部分ごとに分割
						url="http://ja.wikipedia.org"+dest_url[0];
						try {
							document = Jsoup.connect(url).get();// 上記urlからdocumentを入手
						} catch (IOException e2) {
							System.out.println("2");
							return "該当情報なし";
						}
						
					}
				} catch (IOException e2) {
					
					return "該当情報なし";
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
				//System.out.println("bbbbbbbbbbbbbbbbbbbbbbb");
				//System.out.println(url);
				try {
					document = Jsoup.connect(url).get();// 上記urlからdocumentを入手
				} catch (IOException e) {
					return "該当情報なし";
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
							if(kantoku==null){
								return "該当情報なし";
							}
							else if(kantoku==""){
								return "該当情報なし";
							}
							else{
								if (document.toString().indexOf(kantoku) != -1) {// 映画に該当する項目を検索
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
			} catch (IOException e) {
				return "該当情報なし";
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
				return "該当情報なし";
			}
			Document doc = Jsoup.parse("<h2><span class=\"mw-headline\""
					+ dest4[target]);// あらすじに該当する見出しを抽出しhtmlとして再形成
			Elements content2 = doc.getElementsByTag("p");// 見出しからp要素のみを抽出(ほぼ文章)
			text = content2.text();
			//System.out.println(content2.text());// 抽出したp要素からタグ等を除去
		
			
		
		return text;

	}
	
	//////////////////////////////////////////////////////////////////////////////////////

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
