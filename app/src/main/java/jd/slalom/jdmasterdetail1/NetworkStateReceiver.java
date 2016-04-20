// Author: John "JD" Donaldson, April 2016
// Resume: https://drive.google.com/open?id=1Pzf7oeqrpIEmzb0ZU3aRPRkRh9Us3ge0AkfsbUer3KM

// http://stackoverflow.com/questions/6169059/android-event-for-internet-connectivity-state-change
package jd.slalom.jdmasterdetail1;

import android.content.*;
import android.net.*;

import java.util.*;



public class NetworkStateReceiver extends BroadcastReceiver{

protected List< NetworkStateReceiverListener > listeners;
protected Boolean connected;

public interface NetworkStateReceiverListener{
	void onNetworkAvailable();
	void onNetworkUnavailable();
}

public NetworkStateReceiver(){
	listeners = new ArrayList<>();
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

public void notifyStateToAll(){
	for ( NetworkStateReceiverListener listener : listeners )
		notifyState( listener );
}

private void notifyState( NetworkStateReceiverListener listener ){
	if ( connected == null || listener == null ) return;

	if ( connected ) listener.onNetworkAvailable();
	else listener.onNetworkUnavailable();
}
}//class NetworkStateReceiver

