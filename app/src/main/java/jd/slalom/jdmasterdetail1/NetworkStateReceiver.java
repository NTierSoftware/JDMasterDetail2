// http://stackoverflow.com/questions/6169059/android-event-for-internet-connectivity-state-change
package jd.slalom.jdmasterdetail1;// Created by John Donaldson, NTier Software Engineering on 4/16/2016.

import android.content.*;
import android.net.*;

import java.util.*;



public class NetworkStateReceiver extends BroadcastReceiver{

protected List< NetworkStateReceiverListener > listeners;
protected Boolean connected;

public interface NetworkStateReceiverListener{
	public void onNetworkAvailable();

	public void onNetworkUnavailable();
}

public NetworkStateReceiver(){
	listeners = new ArrayList< NetworkStateReceiverListener >();
	connected = null;
}

public void onReceive( Context context, Intent intent ){

	if ( intent == null || intent.getExtras() == null ) return;

	ConnectivityManager manager = (ConnectivityManager) context.getSystemService(
			Context.CONNECTIVITY_SERVICE );
	NetworkInfo ni = manager.getActiveNetworkInfo();

	if ( ni != null && ni.getState() == NetworkInfo.State.CONNECTED ){ connected = true; }
	else if ( intent.getBooleanExtra( ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE ) )
				{ connected = false; }

	notifyStateToAll();
}

public void addListener( NetworkStateReceiverListener l ){
	listeners.add( l );
	notifyState( l );
}

public void removeListener( NetworkStateReceiverListener l ){ listeners.remove( l ); }

private void notifyStateToAll(){
	for ( NetworkStateReceiverListener listener : listeners )
		notifyState( listener );
}

private void notifyState( NetworkStateReceiverListener listener ){
	if ( connected == null || listener == null ) return;

	if ( connected ) listener.onNetworkAvailable();
	else listener.onNetworkUnavailable();
}
}//class NetworkStateReceiver


/*

public class NetworkStateReceiver extends BroadcastReceiver{

private ConnectivityManager mManager;
private List< NetworkStateReceiverListener > mListeners;
private boolean mConnected;

public interface NetworkStateReceiverListener{
	public void onNetworkAvailable();
	public void onNetworkUnavailable();
}

public NetworkStateReceiver( Context context ){
	mListeners = new ArrayList< NetworkStateReceiverListener >();
	mManager = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );
	checkStateChanged();
}

public void onReceive( Context context, Intent intent ){
	if ( intent == null || intent.getExtras() == null ) return;

	if ( checkStateChanged() ) notifyStateToAll();
}

public void addListener( NetworkStateReceiverListener l ){
	mListeners.add( l );
	notifyState( l );
}

public void removeListener( NetworkStateReceiverListener l ){
	mListeners.remove( l );
}

private boolean checkStateChanged(){
	boolean prev = mConnected;
	NetworkInfo activeNetwork = mManager.getActiveNetworkInfo();
	mConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
	return prev != mConnected;
}

private void notifyStateToAll(){
	for ( NetworkStateReceiverListener listener : mListeners ){
		notifyState( listener );
	}
}

private void notifyState( NetworkStateReceiverListener listener ){
	if ( listener != null ){
		if ( mConnected ) listener.onNetworkAvailable();
		else listener.onNetworkUnavailable();
	}
}
}//NetworkStateReceiver

*/
