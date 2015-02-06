package com.koba.myshelf;

import twitter4j.AsyncTwitter;
import twitter4j.AsyncTwitterFactory;
import twitter4j.auth.AccessToken;

public class Twit {
	
	 private AsyncTwitter mTwitter;
	String API_KEY = Pass.OAUTH;
	String API_SECRET = Pass.SECRET;
	String ACEESS_TOKEN = null;
	String ACEESS_TOKEN_SECRET = null;
	String Tweet = null;

	Twit(String a,String b){
		ACEESS_TOKEN = a;
		ACEESS_TOKEN_SECRET = b;
		
		mTwitter = new AsyncTwitterFactory().getInstance();
		mTwitter.setOAuthConsumer(API_KEY, API_SECRET);
		AccessToken accessToken = new AccessToken(ACEESS_TOKEN, ACEESS_TOKEN_SECRET);
		mTwitter.setOAuthAccessToken(accessToken);
		
	}
	
	void TweetUp(String c){
		Tweet = c;
		mTwitter.updateStatus(Tweet);
	}
	
	 
}