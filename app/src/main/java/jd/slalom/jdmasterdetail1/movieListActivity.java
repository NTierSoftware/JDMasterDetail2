// Author: John "JD" Donaldson, April 2016
// Resume: https://drive.google.com/open?id=1Pzf7oeqrpIEmzb0ZU3aRPRkRh9Us3ge0AkfsbUer3KM
// modified from Android Studio Master Detail template

/* MOBILE CODE CHALLENGE
		You're free to use any resource (internet / libraries / etc), but try to use as much native code as possible (Java for Android, Swift/Objective-C for iOS).

		If you use 3rd party library/code/tools, please comment where it is from and why it was used.

		(Note that these instructions are written agnostically since the challenge is intended to be for iOS or Android).

		The purpose of this exercise is to build a simple native app (iOS or Android) that pulls down a list of movies from some static JSON on the web and displays it. There are not detailed UI instructions included. Please feel free to include as much UI flare as you feel is appropriate in order to show off any design and interaction skills you might possess.

NOTE that there are multiple opportunities to expand on the requirements of this exercise. Feel free to do so as time permits and if you feel it is appropriate. The instructions are in some cases left deliberately ambiguous in order to allow you some creative flexibility.

The JSON data can be found here: http://private-05248-rottentomatoes.apiary-mock.com/ .

INSTRUCTIONS:
1) Create an empty app.
2) Create a "LOAD" button on the screen that downloads the JSON file on tap. Log the JSON to the console.
3) Style the button as you feel appropriate.
4) Display the "movie_name" of the JSON items in a table or grid format (You can choose what you want to use) underneath the button.
5) Download and display the "image_url" images next to the name (or anywhere you feel is appropriate).
6) When the table (or grid) cell is tapped, load a new screen and display the complete details of the record tapped (movie_name, image_url, rating, description).
7) Allow for normal / proper navigation back to the list screen.
8) Update the screen layout. Next to the original load button, add a 2nd button so that the two buttons have equal widths, filling the width of the screen. Name the button "DELETE".
9) When the 2nd button is tapped, delete the 1st item in the table / grid. You should be able to continuously tap this button, until the list is empty.
10) When the table / grid is empty, display a popup with title:"Error" message: "There are no more movies to display".

BONUS POINTS:
Initially load the JSON into a data store and drive the UI from the data store.
*/

//3rd party library/code/tools used:
// Google Volley for network requests and image loading.
// Logback for Android for excellent logging to logcat console and asynch file logging.

package jd.slalom.jdmasterdetail1;

import android.app.*;
import android.content.*;
import android.net.ConnectivityManager;
import android.os.*;
import android.support.design.widget.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.*;

import com.android.volley.*;
import com.android.volley.toolbox.*;

import org.json.*;
import org.slf4j.Logger;
import org.slf4j.*;

import java.util.*;

import ch.qos.logback.classic.*;
import ch.qos.logback.classic.util.*;
import jd.slalom.jdmasterdetail1.NetworkStateReceiver.*;


import static android.widget.Toast.*;


// An activity representing a list of movies. This activity has different presentations for handset and tablet-size devices.
// On handsets, the activity presents a list of items, which when touched, lead to a {@link movieDetailActivity} representing
// item details. On tablets, the activity presents the list of items and item details side-by-side using two vertical panes.
public class movieListActivity extends AppCompatActivity implements NetworkStateReceiverListener{
//Whether or not the activity is in two-pane mode, i.e. running on a tablet device.
public boolean mTwoPane;

static private final Logger mLog = LoggerFactory.getLogger( movieListActivity.class );
static private final LoggerContext mLoggerContext = (LoggerContext) LoggerFactory
		.getILoggerFactory();
static private final ContextInitializer mContextInitializer = new ContextInitializer(
		mLoggerContext );
static private final String HTTPURL = "http://private-05248-rottentomatoes.apiary-mock.com/";
static private final JSONObject GET = null;

private List< Movie > mMovieList = new ArrayList<>();
private MovieAdapter mMovieAdapter;
private Button btnDel, btnLoad;
private android.app.ProgressDialog progress;
private RecyclerView recyclerView;
private jd.slalom.jdmasterdetail1.AppController mAppController = AppController.getInstance();
private NetworkStateReceiver networkStateReceiver;


@Override public void onCreate( Bundle savedInstanceState ){
	super.onCreate( savedInstanceState );

	networkStateReceiver = new NetworkStateReceiver();
	networkStateReceiver.addListener( this );

	setContentView( R.layout.activity_movie_list );

	Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
	setSupportActionBar( toolbar );
	toolbar.setTitle( getTitle() );

/*
	FloatingActionButton fab = (FloatingActionButton) findViewById( R.id.fab );
	fab.setOnClickListener( new View.OnClickListener(){
		@Override public void onClick( View view ){
			Snackbar.make( view, "Replace with your own action", Snackbar.LENGTH_SHORT )
			        .setAction( "Action", null ).show();
		}//onClick
	} );
*/

	recyclerView = (RecyclerView) findViewById( R.id.movie_list );

	mMovieAdapter = new MovieAdapter( this, mMovieList );
	mAppController.setMovieAdapter( mMovieAdapter );
	recyclerView.setAdapter( mMovieAdapter );

// The detail container view will be present only in the large-screen layouts (res/values-w900dp).
// If this view is present, then the activity should be in two-pane mode.
	mTwoPane = ( findViewById( R.id.movie_detail_container ) != null );

	btnLoad = (Button) findViewById( R.id.btnLoad );
	btnDel = (Button) findViewById( R.id.btnDel );
	btnDel.setOnLongClickListener( new View.OnLongClickListener(){
		@Override public boolean onLongClick( View v ){
			if ( !isEmpty() ){
				mMovieList.clear();
				mMovieAdapter.notifyDataSetChanged();
				isEmpty();
			}
			return true;
		}//onLongClick
	}//View.OnLongClickListener
	);//btnDel.setOnLongClickListener
}//onCreate

/* @Override public boolean onCreateOptionsMenu(Menu menu){
	getMenuInflater().inflate(R.menu.main, menu);
return true;
} */

@Override public void onRestart(){
	super.onRestart();
	// Reload Logback log: http://stackoverflow.com/questions/3803184/setting-logback-appender-path-programmatically/3810936#3810936
	mLoggerContext.reset();

	try{ mContextInitializer.autoConfig(); } //I prefer autoConfig() over JoranConfigurator.doConfigure() so I don't need to find the file myself.
	catch ( ch.qos.logback.core.joran.spi.JoranException X ){ X.printStackTrace(); }
}//onRestart()

@Override public void onResume(){
	super.onResume();
	registerReceiver(networkStateReceiver, new IntentFilter( ConnectivityManager.CONNECTIVITY_ACTION) );

	progress = new android.app.ProgressDialog( this );

	btnLoadClicked(null);
}//onResume


@Override public void onPause(){
	super.onPause();
	unregisterReceiver( networkStateReceiver );
}
@Override public void onStop(){
	super.onStop();
	mLog.trace( "onStop():\t" );
	clearProgress();
	progress = null;
	mLoggerContext.stop();//flush log
}// onStop()


@Override public void onDestroy(){
	super.onDestroy();
	mLog.trace( "onDestroy():\t" );
	clearProgress();
	mLoggerContext.stop();//flush log
}


public void onNetworkAvailable(){ btnLoadClicked(null); }

public void onNetworkUnavailable(){
	btnLoad.setVisibility( View.INVISIBLE );
	btnDel.setVisibility ( View.INVISIBLE );
	Toast.makeText( this, "network Unavailable!!\nPlease Enable the network to Load!", Toast.LENGTH_SHORT ).show();
	mAppController.cancelPendingRequests();
	clearProgress();
}


//http://www.androidhive.info/2014/07/android-custom-listview-with-image-and-text-using-volley/
public void btnLoadClicked(View aView){
	final Activity toastActivity = this;
//mLog.debug("btnLoadClicked");
	progress.setMessage( "Loading. . ." );
	progress.show();

	JsonObjectRequest request = new JsonObjectRequest( HTTPURL, GET,
               new Response.Listener< JSONObject >(){
                   public void onResponse(
                           JSONObject response ){
                       //mLog.debug("onResponse:\t" + response.toString());
                       int numMovies;
                       try{ //to view JSON use http://codebeautify.org/jsonviewer#
                           JSONArray moviesArr = response.getJSONArray( "movies" );

                           //"Log the JSON to the console."
                           mLog.info( "JSON:\t" + moviesArr.toString() );

                           numMovies = moviesArr.length();
                           mMovieList.clear();
                           for ( int i = 0; i < numMovies; i++ ){
                               mMovieList.add( Movie.fromJson( moviesArr.getJSONObject(i ) ) );
                           }//for
                       }//try
                       catch ( JSONException X ){
                           mLog.error( "onResponse:\t" + X.getMessage() );
                           numMovies = 0;
                           mMovieList.clear();
                       }

                       mMovieAdapter.notifyDataSetChanged();

                       if ( !isEmpty() ) btnLoad.setVisibility( View.INVISIBLE );
                       clearProgress();
                       makeText( toastActivity,
                                 Integer.toString( numMovies ) + " movies loaded.",
                                 LENGTH_SHORT ).show();

                   }//onResponse
               }//Listener

			, new Response.ErrorListener(){
		@Override public void onErrorResponse( com.android.volley.VolleyError X ){
			mLog.error( "\nonErrorResponse\n" );
			X.printStackTrace();
			clearProgress();
		}//onErrorResponse
	}//ErrorListener
	);//JsonObjectRequest


	mAppController.addToRequestQueue( request );
}//btnLoadClicked

//@Override protected void onStart(){ super.onStart(); }//onStart

public void btnDelClicked( View view ){
	btnLoad.setVisibility( View.VISIBLE );

	if ( isEmpty() ) return;

	makeText( this, getResources().getString( R.string.deleteAll ), LENGTH_SHORT ).show();

	mMovieList.remove( 0 );
	mMovieAdapter.notifyDataSetChanged();
	isEmpty();
	//recyclerView.
}//btnDelClicked

private boolean isEmpty(){
	if ( mMovieList.isEmpty() ){
		btnDel.setVisibility( View.INVISIBLE );

		makeText( this,
		          getResources().getString( R.string.emptyList ),
		          LENGTH_SHORT ).show();

		btnLoad.setVisibility( View.VISIBLE );
		return true;
	}
	btnDel.setVisibility( View.VISIBLE );
	return false;
}

private void clearProgress(){ if ( progress != null ){ progress.dismiss(); } }

}//class movieListActivity

