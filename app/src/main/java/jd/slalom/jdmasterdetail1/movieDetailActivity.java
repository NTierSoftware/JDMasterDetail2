// Author: John "JD" Donaldson, April 2016
// Resume: https://drive.google.com/open?id=1Pzf7oeqrpIEmzb0ZU3aRPRkRh9Us3ge0AkfsbUer3KM
// modified from Android Studio Master Detail template

package jd.slalom.jdmasterdetail1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

/**
 * An activity representing a single movie detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link movieListActivity}.
 */
public class movieDetailActivity extends AppCompatActivity{

@Override protected void onCreate(Bundle savedInstanceState){
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_movie_detail);
	Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
	setSupportActionBar(toolbar);

	FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
	fab.setOnClickListener(new View.OnClickListener(){
		@Override
		public void onClick(View view){
			Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
					.setAction("Action", null).show();
		}
	});

	// Show the Up button in the action bar.
	ActionBar actionBar = getSupportActionBar();
	if (actionBar != null) { actionBar.setDisplayHomeAsUpEnabled(true); }

// savedInstanceState is non-null when there is fragment state saved from previous configurations of this activity
// (e.g. when rotating the screen from portrait to landscape). In this case, the fragment will automatically be re-added
// to its container so we don't need to manually add it.
// http://developer.android.com/guide/components/fragments.html
	if (savedInstanceState == null) {
		// Create the detail fragment and add it to the activity using a fragment transaction.
		movieDetailFragment fragment = new movieDetailFragment();
		fragment.setArguments( getIntent().getExtras() );//bundle
		getSupportFragmentManager().beginTransaction()
				.add(R.id.movie_detail_container, fragment)
				.commit();
	}
}

@Override public boolean onOptionsItemSelected(MenuItem item){
	int id = item.getItemId();
	if (id == android.R.id.home){
		// This ID represents the Home or Up button. In the case of this
		// activity, the Up button is shown. For
		// more details, see the Navigation pattern on Android Design:
		//
		// http://developer.android.com/design/patterns/navigation.html#up-vs-back
		//
		navigateUpTo(new Intent(this, movieListActivity.class));
		return true;
	}
return super.onOptionsItemSelected(item);
}
}
