// Author: John "JD" Donaldson, April 2016
// Resume: https://drive.google.com/open?id=1Pzf7oeqrpIEmzb0ZU3aRPRkRh9Us3ge0AkfsbUer3KM
package jd.slalom.jdmasterdetail1;

import android.content.*;
import android.os.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;

import com.android.volley.toolbox.*;

import org.slf4j.*;

import java.util.*;


import static org.slf4j.LoggerFactory.*;


public class MovieAdapter extends RecyclerView.Adapter< MovieAdapter.ViewHolder >{
static private final Logger mLog = getLogger( MovieAdapter.class );

private movieListActivity mActivity;
private List< Movie > mMovies;
private ImageLoader mImageLoader = AppController.getInstance().getImageLoader();

public MovieAdapter( movieListActivity activity, List< Movie > movies ){
	this.mActivity = activity;
	this.mMovies = movies;
}

public int getCount(){ return mMovies.size(); }

public Object getItem( int position ){ return mMovies.get( position ); }

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
			int pos = ( position == RecyclerView.NO_POSITION ) ? 0 : position;

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
//						.putExtra( Movie.parcelKey, holder.mItem );

				context.startActivity( intent );
			}//else
		}//onClick
	} );
}//onBindViewHolder


@Override public long getItemId( int position ){ return position; }

@Override public int getItemCount(){ return mMovies.size(); }

public class ViewHolder extends RecyclerView.ViewHolder{
//	private final Logger mLog = getLogger(ViewHolder.class);

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
