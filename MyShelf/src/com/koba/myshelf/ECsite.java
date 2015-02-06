package com.koba.myshelf;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ECsite extends Activity {

private WebView webView;

public void shelf () {
	
	finish();

}


@Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.ecsite); // main.xmlをセット
//findViews(); // Viewの読み込み

Intent intent = getIntent();
		// intentから指定キーの文字列を取得する
String ECURL = intent.getStringExtra("EC");



System.out.println("nullpo!!");


WebView webView = (WebView) findViewById(R.id.webview);
webView.setVerticalScrollbarOverlay(true);
webView.setWebViewClient(new WebViewClient());

WebSettings settings = webView.getSettings();
settings.setSupportMultipleWindows(true);
settings.setLoadsImagesAutomatically(true);

settings.setBuiltInZoomControls(true);
settings.setSupportZoom(true);
settings.setLightTouchEnabled(true);
webView.loadUrl(ECURL);


}


@Override
public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.ecsiteactionbar, menu);
	getActionBar().setTitle("ECsite");
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



}	