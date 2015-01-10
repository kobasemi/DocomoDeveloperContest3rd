
///////ここから本検索部分//////////////////////////////////////////////////////////////
class detail_CD{//検索したい本を入力することで,その本のあらすじを返す
    String uri;
	String text_artist="";	
	String text_album="";
	String[] text_track=new String[105];
	String data = ""; 
 	String scheme = "https";
    String authority = "ws.audioscrobbler.com";
    String path = "2.0/";  
    String method = "album.getinfo";
    String api_key = "自分のもの";//検索したい本の名前
    String album ;
    String artist = "";
    String format = "json";
    detail(String word,String word2){ /// クラス作成時引数として与えるword=アルバム名 word2=アーティスト名
    	album = word;
    	artist=word2;
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
		    HttpResponse httpResponse = null;
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
		        text_artist =jsonObject.getString("artist");
		        text_album =jsonObject.getString("name");
			    System.out.println("4") ; 
			    JSONObject jsonObject2 = jsonObject.getJSONObject("tracks");
			    System.out.println("5") ; 
			    JSONArray jsonObject3 = jsonObject2.getJSONArray("track");
			    System.out.println("6") ; 
			  for(int i=0;i<100;i++){
			    JSONObject jsonObject4 = jsonObject3.getJSONObject(i);
			    if(jsonObject4.getString("name")!=null){
			    	text_track[i] =jsonObject4.getString("name");
			    }
			  }
		        System.out.println("7") ;
		    }
		    catch (JSONException e){
		        // 例外
		    }    
		    httpClient.getConnectionManager().shutdown();
		
	}
}
//////検索部分ここまで/////////////////////////////////////////////////////

