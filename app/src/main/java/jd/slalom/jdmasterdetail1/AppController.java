package jd.slalom.jdmasterdetail1;

import android.app.*;

import com.android.volley.*;
import com.android.volley.toolbox.*;
import static com.android.volley.toolbox.Volley.*;

public class AppController extends Application{
static public final String TAG = AppController.class.getSimpleName();
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
}

public void setMovieAdapter( MovieAdapter aMovieAdapter ){ mMovieAdapter = aMovieAdapter;}
public MovieAdapter getMovieAdapter(){ return mMovieAdapter; }


public RequestQueue getRequestQueue(){
	if ( mRequestQueue == null ){ mRequestQueue = newRequestQueue( getApplicationContext() ); }
	return mRequestQueue;
}

public ImageLoader getImageLoader(){
	getRequestQueue();
	if ( mImageLoader == null ){
		mImageLoader = new ImageLoader( mRequestQueue, new LruBitmapCache() );
	}
	return this.mImageLoader;
}

public < T > void addToRequestQueue( com.android.volley.Request< T > req, String tag ){
	// set the default tag if tag is empty
	req.setTag( android.text.TextUtils.isEmpty( tag ) ? TAG : tag );
	getRequestQueue().add( req );
}

public < T > void addToRequestQueue( com.android.volley.Request< T > req ){
	req.setTag( TAG );
	getRequestQueue().add( req );
}

public void cancelPendingRequests( Object tag ){
	if ( mRequestQueue != null ){ mRequestQueue.cancelAll( tag ); }
}
}//AppController

