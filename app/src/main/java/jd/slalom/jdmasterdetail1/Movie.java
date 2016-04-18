package jd.slalom.jdmasterdetail1;

import com.google.gson.*;

import org.json.*;


class Movie{
//static final Logger mLog = org.slf4j.LoggerFactory.getLogger(Movie.class);
public String id, image_url, movie_name, description;
public double rating;
static private int idcounter = 0;


public Movie(){ id = Integer.toString( idcounter++ ); }//cstr

/*
public Movie(String movie, String imageUrl, double rating, String desc){
	this();
	this.movie_name = movie;
	this.image_url = imageUrl;
	this.rating = rating;
	this.description = desc;
}//cstr
*/

public static Movie fromJson( JSONObject json )
	{ return new Gson().fromJson( json.toString(), Movie.class ); }

@Override public String toString(){ return toJson(); }

public String toJson(){ return new Gson().toJson( this ); }

}//class Movie

