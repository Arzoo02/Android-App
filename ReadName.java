package com.example.tie;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


//import android.widget.ImageView;

public class ReadName extends ActionBarActivity {

	// Progress Dialog
	private ProgressDialog pDialog;

	// php read comments script

	// localhost:
	// testing on your device
	// put your local ip instead, on windows, run CMD > ipconfig
	// or in mac's terminal type ifconfig and look for the ip under en0 or en1
	// private static final String READ_COMMENTS_URL =
	// "http://xxx.xxx.x.x:1234/webservice/comments.php";

	// testing on Emulator:
	private static final String READ_COMMENTS_URL = "http://utopiansolutions.co.in/at/tie/comments.php";
	private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
	private long mBackPressed;
	// testing from a real server:
	// private static final String READ_COMMENTS_URL =
	// "http://www.mybringback.com/webservice/comments.php";

	// JSON IDS:
	//private static final String TAG_SUCCESS = "success";
	private static final String TAG_DESC = "desc";
	private static final String TAG_NAME = "name";
	private static final String TAG_ID = "id";
	private static final String TAG_IMAGE_URL = "image_url";
	private static final String TAG_YOU_KEY = "you_key";
	// it's important to note that the message is both in the parent branch of
	// our JSON tree that displays a "Post Available" or a "No Post Available"
	// message,
	// and there is also a message for each individual post, listed under the
	// "posts"
	// category, that displays what the user typed as their message.

	// An array of all of our comments
	private JSONArray mComments = null;
	// manages all of our comments in a list.
	private ArrayList<HashMap<String, String>> mCommentList;


	ListView listView;
	LazyAdapter adapter;


	//ImageView imageView;
	//String[] brands = {"adidas","nike","puma","fastrack","reebok","woodland","american tourister","lee","levis","kiler","pepe jeans","wrangler","monte carlo","peter england","being human"};

	ArrayList<HashMap<String, String>> arraylist;



	JSONParser jsonParser= new JSONParser();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// note that use read_comments.xml instead of our single_post.xml
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		//Remove title bar
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.newxml);



		// Font path
		String fontPath = "fonts/RobotoCondensed-Bold.ttf";

		// text view label
		TextView tv2Font = (TextView) findViewById(R.id.name);

		// Loading Font Face
		Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);

		// Applying font
		tv2Font.setTypeface(tf);

		new LoadComments().execute();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// loading the comments via AsyncTask

	}

	/*public void addComment(View v) {
		Intent i = new Intent(ReadName.this, Details.class);
		startActivity(i);
	}*/

	/**
	 * Retrieves recent post data from the server.
	 */
	public void updateJSONdata() {

		// Instantiate the arraylist to contain all the JSON data.
		// we are going to use a bunch of key-value pairs, referring
		// to the json element name, and the content, for example,
		// message it the tag, and "I'm awesome" as the content..

		mCommentList = new ArrayList<HashMap<String, String>>();

		// Bro, it's time to power up the J parser
		JSONParser jParser = new JSONParser();
		// Feed the beast our comments url, and it spits us
		// back a JSON object. Boo-yeah Jerome.
		JSONObject json = jParser.getJSONFromUrl(READ_COMMENTS_URL);

		// when parsing JSON stuff, we should probably
		// try to catch any exceptions:
		try {

			// I know I said we would check if "Posts were Avail." (success==1)
			// before we tried to read the individual posts, but I lied...
			// mComments will tell us how many "posts" or comments are
			// available
			mComments = json.getJSONArray("posts");

			// looping through all posts according to the json object returned
			for (int i = 0; i < mComments.length(); i++) {
				JSONObject c = mComments.getJSONObject(i);

				// gets the content of each tag
				String name = c.getString(TAG_NAME);
				String desc = c.getString(TAG_DESC);
				String image_url = c.getString(TAG_IMAGE_URL);
				String you_key = c.getString(TAG_YOU_KEY);
				String id = c.getString(TAG_ID);

				// creating new HashMap
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(TAG_ID,id);
				map.put(TAG_DESC, desc);
				map.put(TAG_NAME, name);
				map.put(TAG_YOU_KEY, you_key);
				map.put(TAG_IMAGE_URL, image_url);

				// adding HashList to ArrayList
				mCommentList.add(map);


				/*listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,int position, long id) {


						@SuppressWarnings("unchecked")
						HashMap<String, String> o = (HashMap<String, String>) listView.getItemAtPosition(position);

						Intent i = new Intent(ReadName.this,Youtube.class);
				        i.putExtra("image_url",o.get("image_url"));

				        startActivity(i);

				        Toast.makeText(ReadName.this, "ID '" + "' was clicked.", Toast.LENGTH_LONG).show();

					}
				});*/




			}

			// annndddd, our JSON data is up to date same with our array
			// list


		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Inserts the parsed data into the listview.
	 */

	public class LoadComments extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ReadName.this);
			pDialog.setMessage("#Better Connection #Faster Ties.\nPlease Wait");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			updateJSONdata();
			return null;

		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			pDialog.dismiss();


			listView = (ListView) findViewById(R.id.list);
			adapter = new LazyAdapter(ReadName.this, mCommentList);
			listView.setAdapter(adapter);



			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// getting values from selected ListItem
					String sno = ((TextView) view.findViewById(R.id.sno)).getText().toString();
					String youtube = ((TextView) view.findViewById(R.id.you_key)).getText().toString();
					String user = ((TextView) view.findViewById(R.id.username)).getText().toString();
					String desc = ((TextView) view.findViewById(R.id.desc)).getText().toString();
					String img = ((TextView) view.findViewById(R.id.img_url)).getText().toString();


					Intent in = new Intent(getApplicationContext(),Youtube.class);
					in.putExtra("sno", sno);
					in.putExtra("img", img);
					in.putExtra("youtube", youtube);
					in.putExtra("desc", desc);
					in.putExtra("name", user);
					/*in.putExtra("",shop_no);
						in.putExtra(RATING, rating);
						in.putExtra(FLOOR,floor);
					 */

					/*Intent two = new Intent(getApplicationContext(),Youtube.class);
						two.putExtra("img", img);
						startActivity(two);*/
					startActivity(in);
					finish();

				}
			});


		}
	}

	public void onBackPressed() {
		if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) 
		{ 
			super.onBackPressed(); 
			return;
		}
		else { Toast.makeText(getBaseContext(), "Double tap in order to exit", Toast.LENGTH_SHORT).show(); }

		mBackPressed = System.currentTimeMillis();
	}	
}
