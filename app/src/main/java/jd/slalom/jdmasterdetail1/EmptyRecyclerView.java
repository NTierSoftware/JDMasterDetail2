//https://gist.github.com/adelnizamutdinov/31c8f054d1af4588dc5c
package jd.slalom.jdmasterdetail1;
import android.content.*;
import android.os.*;
import android.support.v7.widget.*;
import android.util.*;
import android.view.*;


public class EmptyRecyclerView extends RecyclerView{
View emptyView;
final AdapterDataObserver observer = new AdapterDataObserver(){
	@Override public void onChanged(){
		super.onChanged();
		if (!checkIfEmpty() ) ((MovieAdapter) getAdapter()).setPosition( 0 );
	}

	@Override public void onItemRangeRemoved(int positionStart, int itemCount) {
		super.onItemRangeRemoved( positionStart, itemCount );
		checkIfEmpty();
	}
	@Override public void onItemRangeChanged(int positionStart, int itemCount){
		super.onItemRangeChanged( positionStart, itemCount );
	}
};

public EmptyRecyclerView( Context context ){ super( context ); }

public EmptyRecyclerView( Context context, AttributeSet attrs ){ super( context, attrs ); }

public EmptyRecyclerView( Context context, AttributeSet attrs, int defStyle ){
	super( context, attrs, defStyle );
}

@Override public void setAdapter( Adapter adapter ){
	final Adapter oldAdapter = getAdapter();
	if ( oldAdapter != null ){
		oldAdapter.unregisterAdapterDataObserver( observer );
	}
	super.setAdapter( adapter );
	if ( adapter != null ){ adapter.registerAdapterDataObserver( observer ); }
}

/*
public void setEmptyView( View emptyView ){
	this.emptyView = emptyView;
	checkIfEmpty();
}
*/

private boolean checkIfEmpty(){
	MovieAdapter adapter = (MovieAdapter) getAdapter();
	boolean retVal = (adapter.getItemCount() > 0);

	if ( emptyView != null ){
		if (retVal) emptyView.setVisibility( GONE );
		else{
				emptyView.setVisibility( VISIBLE );
				adapter.setPosition( RecyclerView.NO_POSITION );
		}
	}//if

	if ( adapter.mActivity.mTwoPane ){
		Bundle bundle = new Bundle();
		bundle.putInt( "pos", adapter.getCurPosition() );

		movieDetailFragment fragment = new movieDetailFragment();
		fragment.setArguments( bundle );

		adapter.mActivity.getSupportFragmentManager().beginTransaction()
		                 .replace( R.id.movie_detail_container, fragment )
		                 .commit();
	}
return retVal;
}//checkIfEmpty()

public void close(){
	try{
		setEnabled( false );
		destroyDrawingCache();
		removeAllViews();
		getAdapter().unregisterAdapterDataObserver( observer );
	}
	catch ( Exception ignore ){}
}//close

}
