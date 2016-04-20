// Author: John "JD" Donaldson, April 2016
// Resume: https://drive.google.com/open?id=1Pzf7oeqrpIEmzb0ZU3aRPRkRh9Us3ge0AkfsbUer3KM
package jd.slalom.jdmasterdetail1;

import android.os.*;
import android.support.design.widget.*;
import android.support.v4.app.*;
import android.view.*;
import android.widget.*;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;


// A fragment representing a single movie detail screen. This fragment is either contained in a {@link movieListActivity}
// in two-pane mode (on tablets) or a {@link movieDetailActivity} on handsets.
public class movieDetailFragment extends Fragment{
// The fragment argument representing the item ID that this fragment represents.

//The movie content this fragment is presenting.
private Movie mMovie;

//Mandatory empty constructor for the fragment manager to instantiate the fragment (e.g. upon screen orientation changes).
public movieDetailFragment(){}

@Override public void onCreate( Bundle savedInstanceState ){
	super.onCreate( savedInstanceState );

	Bundle bundle = getActivity().getIntent().getExtras();
	if ( bundle == null ) bundle = getArguments();

	int pos = bundle.getInt( "pos", 0 );
	MovieAdapter adapter = AppController.getInstance().getMovieAdapter();
	mMovie = (Movie) adapter.getItem( pos );
	adapter.setPosition(pos);

	CollapsingToolbarLayout appBarLayout =
	(CollapsingToolbarLayout) getActivity().findViewById( R.id.toolbar_layout );

	if ( appBarLayout != null ){ appBarLayout.setTitle( mMovie.movie_name ); }
}//onCreate

public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ){
	View rootView = inflater.inflate( R.layout.movie_detail, container, false );

	// Show the movie content as text in a TextView.
	if ( mMovie != null ){
		ImageLoader mImageLoader = AppController.getInstance()
		                                                                   .getImageLoader();
		( (NetworkImageView) rootView.findViewById( R.id.image_url ) )
				.setImageUrl( mMovie.image_url, mImageLoader );

		( (TextView) rootView.findViewById( R.id.rating ) ).setText( "Rating: " + Double.toString( mMovie.rating ) );
		( (TextView) rootView.findViewById( R.id.description ) ).setText( mMovie.description );
		//( (TextView) rootView.findViewById( R.id.movie_id ) ).setText( "id: " + mMovie.id );
	}

return rootView;
}//onCreateView

}//class movieDetailFragment
