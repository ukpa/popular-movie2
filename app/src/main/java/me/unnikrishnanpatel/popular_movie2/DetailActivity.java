package me.unnikrishnanpatel.popular_movie2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class DetailActivity extends AppCompatActivity {
    FetchMovieData data;
    ImageView poster;
    TextView releaseDate;
    TextView movieLength;
    TextView rating;
    TextView overview;
    TextView tagline;
    String baseUrl = "http://image.tmdb.org/t/p/w185/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent i = getIntent();
        HashMap<String,String> movieData = (HashMap<String,String>) i.getSerializableExtra("data");
        String url = "https://api.themoviedb.org/3/movie/"+movieData.get("id")+"?api_key=7baf82d2c99ba2997a60d4af8b763034";
        this.setTitle(movieData.get("title"));
        data = (FetchMovieData) new FetchMovieData(new AsyncResponse() {
            @Override
            public void processFinish(String output) {
                try{
                    JSONObject jsonObject = new JSONObject(output);
                    poster = (ImageView)findViewById(R.id.moviePoster);
                    movieLength = (TextView)findViewById(R.id.movieLength);
                    releaseDate = (TextView)findViewById(R.id.release);
                    overview = (TextView)findViewById(R.id.overview);
                    rating = (TextView)findViewById(R.id.rating);
                    tagline = (TextView)findViewById(R.id.tagline);
                    Picasso.with(getBaseContext()).load(baseUrl+jsonObject.get("poster_path")).into(poster);
                    movieLength.setText("Duration: "+jsonObject.get("runtime").toString()+" Minutes");
                    releaseDate.setText("Release: "+jsonObject.get("release_date").toString());
                    rating.setText("Rating: "+jsonObject.get("vote_average").toString()+"/10");
                    overview.setText("Overview:\n\n"+jsonObject.get("overview").toString());
                    tagline.setText(jsonObject.get("tagline").toString());

                }
                catch (JSONException e){
                    e.printStackTrace();
                }
                catch (NullPointerException e){
                    e.printStackTrace();

                }
            }
        }).execute(url);
    }
}
