
	
///////ここから本検索部分//////////////////////////////////////////////////////////////
	String detail(String word){//検索したい本を入力することで,その本のあらすじを返す
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
        	//System.out.println("1") ;
        	JSONObject rootObject = new JSONObject(data);
          //  System.out.println("2") ;
            JSONArray jsonObject = rootObject.getJSONArray("items");       
         //   System.out.println("3") ; 
            JSONObject jsonObject2 = jsonObject.getJSONObject(0);
         //   System.out.println("4") ;
            JSONObject jsonObject3 = jsonObject2.getJSONObject("volumeInfo");
           // System.out.println("5") ;
            detail_text =jsonObject3.getString("description");//概要がdetail_textにはいる
           // System.out.println("6") ;
           
        }
        catch (JSONException e){
        	return detail_text="該当情報なし";
        }    
        httpClient.getConnectionManager().shutdown();
        return detail_text;
	}
//////検索部分ここまで/////////////////////////////////////////////////////
	
	