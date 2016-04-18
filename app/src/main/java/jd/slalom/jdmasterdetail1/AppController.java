package jd.slalom.jdmasterdetail1;

import android.app.*;

import com.android.volley.*;
import com.android.volley.toolbox.*;


import static com.android.volley.toolbox.Volley.*;


public class AppController extends Application{
static private final org.slf4j.Logger mLog = org.slf4j.LoggerFactory.getLogger(
		AppController.class );
static private AppController mInstance;
private RequestQueue mRequestQueue;
private ImageLoader mImageLoader;
private MovieAdapter mMovieAdapter;

public static synchronized AppController getInstance(){ return mInstance; }

@Override public void onCreate(){
	super.onCreate();
	mInstance = this;
	mRequestQueue = newRequestQueue( getApplicationContext());
}

public void setMovieAdapter( MovieAdapter aMovieAdapter ){ mMovieAdapter = aMovieAdapter;}
public MovieAdapter getMovieAdapter(){ return mMovieAdapter; }

public RequestQueue getRequestQueue(){ return mRequestQueue; }

public ImageLoader getImageLoader(){
	if ( mImageLoader == null ){
		mImageLoader = new ImageLoader( mRequestQueue, new LruBitmapCache() );
	}
	return this.mImageLoader;
}

public < T > void addToRequestQueue( Request< T > req, String tag ){
	// set the default tag if tag is empty
	req.setTag( android.text.TextUtils.isEmpty( tag ) ? TAG : tag );
	mRequestQueue.add( req );
}


static private final String TAG = AppController.class.getSimpleName();
public < T > void addToRequestQueue( Request< T > req ){
	req.setTag( TAG );
	mRequestQueue.add( req );
}


public void cancelPendingRequests(){ mRequestQueue.cancelAll( TAG ); }

}//AppController
