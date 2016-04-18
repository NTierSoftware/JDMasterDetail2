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

