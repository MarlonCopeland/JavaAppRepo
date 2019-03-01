package com.teamtreehouse.blogreader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainListActivity extends ListActivity {
	
	
	public static final int NUMBER_OF_POSTS = 20;
	public static final String TAG = MainListActivity.class.getSimpleName();
	protected JSONObject mBlogData;
	// all caps because they are constants		
	protected ProgressBar mProgressBar;
	private final String KEY_TITLE = "title";
	private final String KEY_AUTHOR = "author"; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_list);
		
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
		
		if(isNetworkAvailable()){
			mProgressBar.setVisibility(View.VISIBLE);
			GetBlogPostsTask getBlogPostsTask = new GetBlogPostsTask();
			getBlogPostsTask.execute();
		}
		else{
			Toast.makeText(this, "Network is Unavailable", Toast.LENGTH_LONG).show();
		}
		//url constructor threw an error, wrap inside try catch block
		
			//Resources resources = getResources();
		//mBlogPostTitles = resources.getStringArray(R.array.android_names);
		
		//ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, mBlogPostTitles);
		//setListAdapter(adapter);
		
		//Toast.makeText(this, getString(R.string.no_items), Toast.LENGTH_LONG).show();
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		super.onListItemClick(l, v, position, id);
		try{
		JSONArray jsonPosts = mBlogData.getJSONArray("posts");
		JSONObject jsonPost = jsonPosts.getJSONObject(position);
		String blogURL = jsonPost.getString("url");
		//Declare intent object
		//Intent intent = new Intent(Intent.ACTION_VIEW);
		Intent intent = new Intent(this, BlogWebViewActivity.class);
		
		intent.setData(Uri.parse(blogURL));
		startActivity(intent);
		
			}
		catch(JSONException e){
			logException(e);
		}
	}

	private void logException(Exception e) {
		Log.e(TAG, "Exception caught!", e);
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager manager = (ConnectivityManager)
				getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		
		boolean isAvailable = false;
		//check the network
		if(networkInfo != null && networkInfo.isConnected()){
			isAvailable = true;
			
			
		}
		return isAvailable;
	}

	
	
	public void handleBlogResponse() {
		// TODO Auto-generated method stub
		mProgressBar.setVisibility(View.INVISIBLE);
		
		if(mBlogData == null){
			updateDisplayForError();			
			
		}
		else{
			try {
				
				//Log.d(TAG,mBlogData.toString(2));
				JSONArray jsonPosts = mBlogData.getJSONArray("posts");
				ArrayList<HashMap<String,String>> blogPosts = 
						new ArrayList<HashMap<String,String>>();
				
				
				for(int i = 0; i < jsonPosts.length(); i++  ){
					JSONObject post = jsonPosts.getJSONObject(i);
					
					String title = post.getString(KEY_TITLE);
					title = Html.fromHtml(title).toString();
					
					String author = post.getString(KEY_AUTHOR);
					author = Html.fromHtml(author).toString();
					
					HashMap<String,String> blogPost = new HashMap<String, String>();
					blogPost.put(KEY_TITLE, title); 
					blogPost.put(KEY_AUTHOR, author);
					
					blogPosts.add(blogPost);
					
					
				}
				String[] keys = {KEY_TITLE,KEY_AUTHOR};
				int[] ids = {android.R.id.text1, android.R.id.text2};
				
				SimpleAdapter adapter = new SimpleAdapter(this, blogPosts, android.R.layout.simple_list_item_2, keys, ids);
				
				setListAdapter(adapter);
			} catch (JSONException e) {
				logException(e);
			}
		}
	}

	private void updateDisplayForError() {
		//create an alert dialog
		//"this" is a subclass of context
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.title));
		builder.setMessage(getString(R.string.error_message));
		builder.setPositiveButton(android.R.string.ok, null);
		AlertDialog dialog = builder.create();
		dialog.show();
		
		TextView emptyTextView = (TextView) getListView().getEmptyView();
		emptyTextView.setText(getString(R.string.no_items));
	}
	//create custom async task
	private class GetBlogPostsTask extends AsyncTask<Object, Void, JSONObject>{
		//changed object to string, going to return the data as a string
		
		@Override
		protected JSONObject doInBackground(Object... arg0) {
			int responseCode = -1;
			JSONObject jsonResponse = null;
			
			try{
				URL blogFeedUrl = new URL("http://blog.teamtreehouse.com/api/get_recent_summary/?count=" + NUMBER_OF_POSTS);
				HttpURLConnection connection = (HttpURLConnection) blogFeedUrl.openConnection();
				connection.connect();
				
				responseCode = connection.getResponseCode();
				if (responseCode == HttpURLConnection.HTTP_OK){
					InputStream inputStream = connection.getInputStream();
					Reader reader = new InputStreamReader(inputStream);
					int contentLength = connection.getContentLength();
					char[] charArray = new char[contentLength];
					reader.read(charArray);
					//the char array is in json format, we need a json object to parse it
					String responseData = new String(charArray);
					
				    jsonResponse = new JSONObject(responseData);
				}
				else{
					Log.i(TAG,"Unsuccessful HTTP Response Code: " + responseCode);
				}
			}
			catch(MalformedURLException e){
				//write exception to the log!
				logException(e);
			}
			catch(IOException e){
				logException(e);
			}
			catch(Exception e){
				logException(e);
			}
			
			return jsonResponse;
		}
	
	
		//do in bacground calls onPostExecute and return type becomes onPostExecute's input param
		@Override
		protected void onPostExecute(JSONObject result){
			mBlogData = result;
			//update the list
			handleBlogResponse();
		}
		
	}
	

}
