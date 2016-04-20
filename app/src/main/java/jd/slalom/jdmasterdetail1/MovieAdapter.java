// Author: John "JD" Donaldson, April 2016
// Resume: https://drive.google.com/open?id=1Pzf7oeqrpIEmzb0ZU3aRPRkRh9Us3ge0AkfsbUer3KM
package jd.slalom.jdmasterdetail1;

import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;

import com.android.volley.toolbox.*;

import org.slf4j.*;

import java.util.*;


import static org.slf4j.LoggerFactory.*;


public class MovieAdapter extends EmptyRecyclerView.Adapter< MovieAdapter.ViewHolder >{
movieListActivity mActivity;
static private final Logger mLog = getLogger( MovieAdapter.class );

private List< Movie > mMovies  = new ArrayList<>();
private ImageLoader mImageLoader = AppController.getInstance().getImageLoader();
private int curPosition = 0;

public MovieAdapter( movieListActivity activity ){
	this.mActivity = activity;
}//cstr

//public int getCount(){ return mMovies.size(); }
//@Override public long getItemId( int position ){ return position; }

public Object getItem( int position ){
	return ( position <= RecyclerView.NO_POSITION || mMovies.size() <= 0 ) ? null : mMovies.get(
			chkPosition( position ) );
}

@Override public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType ){
	View view = LayoutInflater.from( mActivity )
	                          .inflate( R.layout.movie_list_content, parent, false );
	return new ViewHolder( view );
}//onCreateViewHolder


@Override public void onBindViewHolder( final ViewHolder holder, final int position ){
	holder.bindMovie( position );

	mLog.debug( "onBindViewHolder:\t" + holder.mItem.toString() );

	holder.mMovienameView.setText( mMovies.get( position ).movie_name );

	holder.mView.setOnClickListener( new View.OnClickListener(){
		@Override public void onClick( View v ){
			int pos = ( position <= RecyclerView.NO_POSITION ) ? 0 : position;

			if ( mActivity.mTwoPane ){
				Bundle bundle = new Bundle();
				bundle.putInt( "pos", pos );

				movieDetailFragment fragment = new movieDetailFragment();
				fragment.setArguments( bundle );
				mActivity.getSupportFragmentManager().beginTransaction()
				         .replace( R.id.movie_detail_container, fragment )
				         .commit();

			}//if
			else{
				Context context = v.getContext();
				Intent intent = new Intent( context, movieDetailActivity.class )
						.putExtra( "pos", pos );

				context.startActivity( intent );
			}//else
		}//onClick
	} );
}//onBindViewHolder

@Override public int getItemCount(){ return mMovies.size(); }

public int setPosition( int position ){
	curPosition = chkPosition( position );
	return curPosition;
}//setPosition


public void clear(){
	mMovies.clear();
	notifyDataSetChanged();
}//clear()

/*
public void add( Movie aMovie ){
	mMovies.add( aMovie );
	notifyDataSetChanged();
}
*/

public boolean isEmpty(){return mMovies.isEmpty();}

public int getCurPosition(){return curPosition;}

public int removeCurItem(){
	return removeItem( curPosition );
}


private int chkPosition( int position ){
	int size = mMovies.size();
	//When the size changes update the position.
	if (size > 0) position = Math.min( Math.max( position, 0 ), size -1 );
	else position = RecyclerView.NO_POSITION;

return position;
}

private int removeItem( int position ){
	if ( mActivity.mTwoPane ){
		FragmentManager mgr = mActivity.getSupportFragmentManager();
		Fragment fragment = mgr.findFragmentById( R.id.movie_detail_container );
		mgr.beginTransaction().remove( fragment ).commit();
	}
	position = chkPosition( position );
	mMovies.remove( position );
	notifyItemRemoved( position );
	notifyDataSetChanged();
return setPosition( position );
}//removeItem

public void setList(List< Movie > aMovies){
	mMovies = aMovies;
	notifyDataSetChanged();
}
public List< Movie > getList(){return mMovies;}

public class ViewHolder extends RecyclerView.ViewHolder{//implements View.OnClickListener
	public final View mView;
	public final TextView mMovienameView, mRatingView;
	public final NetworkImageView mImageurl;
	public Movie mItem;

	public ViewHolder( View view ){
		super( view );
		mView = view;
		mImageurl = (NetworkImageView) view.findViewById( R.id.image_url );
		mMovienameView = (TextView) view.findViewById( R.id.movie_name );
		mRatingView = (TextView) view.findViewById( R.id.rating );
	}

	//https://www.bignerdranch.com/blog/recyclerview-part-1-fundamentals-for-listview-experts///
	public void bindMovie( final int position ){
		mItem = mMovies.get( position );
		mImageurl.setImageUrl( mItem.image_url, mImageLoader );
		mLog.debug( "ViewHolder.bindMovie:\t" + mItem.toString() );
	}

	@Override public String toString(){ return "ViewHolder:\t" + mItem.toString(); }
}//class ViewHolder
}//MovieAdapter
