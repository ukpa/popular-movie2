package me.unnikrishnanpatel.popular_movie2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    Button fav;
    String baseUrl = "http://image.tmdb.org/t/p/w342/";
    SharedPreferences sharedPreferences;
    String moviedetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent i = getIntent();
        final HashMap<String,String> movieData = (HashMap<String,String>) i.getSerializableExtra("data");
        String url = "https://api.themoviedb.org/3/movie/"+movieData.get("id")+"?api_key=7baf82d2c99ba2997a60d4af8b763034";
        sharedPreferences = this.getSharedPreferences("saved_movies",Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        fav = (Button)findViewById(R.id.markAsFav);
        if(sharedPreferences.contains(movieData.get("id"))){
            fav.setEnabled(false);
            fav.setText("Love it");
        }
        else{
            fav.setEnabled(true);
        }
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
                    moviedetails = output;
                    DetailActivity.this.setTitle(jsonObject.get("title").toString());

                }
                catch (JSONException e){
                    e.printStackTrace();
                }
                catch (NullPointerException e){
                    e.printStackTrace();

                }
            }
        }).execute(url);

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString(movieData.get("id"),movieData.get("poster_path"));
                editor.commit();
                Context context = getApplicationContext();
                CharSequence text = "Movie \""+movieData.get("title")+"\" added to the Favourite list :)!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                fav.setEnabled(false);
                fav.setText("Love it");
            }
        });


    }
}
