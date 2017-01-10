package com.example.tie;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.PlayerStyle;
import com.google.android.youtube.player.YouTubePlayerView;
import com.squareup.picasso.Picasso;


public class Youtube extends YouTubeBaseActivity implements
YouTubePlayer.OnInitializedListener {

	private static final int RECOVERY_DIALOG_REQUEST = 1;

	// YouTube player view
	private YouTubePlayerView youTubeView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.details);

		ImageView back_btn = (ImageView) findViewById(R.id.back_image);
		back_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent back = new Intent(Youtube.this,ReadName.class);
				startActivity(back);
				finish();
			}
		});


		// Font path
		String fontPath = "fonts/OpenSans-CondLight.ttf";

		// text view label
		TextView tv2Font = (TextView) findViewById(R.id.textView2);

		// Loading Font Face
		Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);

		// Applying font
		tv2Font.setTypeface(tf);



		// Font path
		String fontPath2 = "fonts/RobotoCondensed-Bold.ttf";

		// text view label
		TextView tv3Font = (TextView) findViewById(R.id.textView3);

		// Loading Font Face
		Typeface tf2 = Typeface.createFromAsset(getAssets(), fontPath2);

		// Applying font
		tv3Font.setTypeface(tf2);


		// Font path
		//String fontPath2 = "fonts/RobotoCondensed-Bold.ttf";

		// text view label
		/*TextView uname = (TextView) findViewById(R.id.username);

				// Loading Font Face
				//Typeface tf3 = Typeface.createFromAsset(getAssets(), fontPath2);

				// Applying font
				uname.setTypeface(tf2);*/

		Context mContext;
		mContext = Youtube.this;


		Intent i1 = getIntent();

		String youKey = i1.getStringExtra("youtube");
		Config.YOUTUBE_VIDEO_CODE= youKey;

		String name = i1.getStringExtra("name");

		tv3Font.setText(name);


		String desc = i1.getStringExtra("desc");

		tv2Font.setText(desc);

		youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);

		// Initializing video player with developer key
		youTubeView.initialize(Config.DEVELOPER_KEY, this);




		String img = i1.getStringExtra("img");
		ImageView imgview = (ImageView) findViewById(R.id.imageView2);
		Picasso.with(mContext).load(img).into(imgview);




	}

	@Override
	public void onInitializationFailure(YouTubePlayer.Provider provider,
			YouTubeInitializationResult errorReason) {
		if (errorReason.isUserRecoverableError()) {
			errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
		} else {
			String errorMessage = String.format(
					getString(R.string.error_player), errorReason.toString());
			Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onInitializationSuccess(YouTubePlayer.Provider provider,
			YouTubePlayer player, boolean wasRestored) {
		if (!wasRestored) {

			// loadVideo() will auto play video
			// Use cueVideo() method, if you don't want to play it automatically
			//player.loadVideo(Config.YOUTUBE_VIDEO_CODE);
			player.cueVideo(Config.YOUTUBE_VIDEO_CODE);

			// Hiding player controls
			//player.setPlayerStyle(PlayerStyle.CHROMELESS);
			player.setPlayerStyle(PlayerStyle.DEFAULT);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == RECOVERY_DIALOG_REQUEST) {
			// Retry initialization if user performed a recovery action
			getYouTubePlayerProvider().initialize(Config.DEVELOPER_KEY, this);
		}
	}

	private YouTubePlayer.Provider getYouTubePlayerProvider() {
		return (YouTubePlayerView) findViewById(R.id.youtube_view);
	}

}
