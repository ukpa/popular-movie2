package me.unnikrishnanpatel.popular_movie2;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity  {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private JSONArray results = null;
    private ArrayList<HashMap<String,String>> movieList;
    private String posterUrl = "http://image.tmdb.org/t/p/w185/";
    private FetchMovieData data;
    String mostPopular = "http://api.themoviedb.org/3/movie/popular?api_key=7baf82d2c99ba2997a60d4af8b763034";
    String topRated = "http://api.themoviedb.org/3/movie/top_rated?api_key=7baf82d2c99ba2997a60d4af8b763034";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Top Rated Movies");
        mRecyclerView = (RecyclerView)findViewById(R.id.movieGrid);
        mLayoutManager = new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(mLayoutManager);




         data = (FetchMovieData) new FetchMovieData(new AsyncResponse() {
            @Override
            public void processFinish(String movieDataJson) {
                hashOutput(movieDataJson);
                mAdapter = new MovieAdapter(movieList);
                mRecyclerView.setAdapter(mAdapter);


            }
        }).execute(topRated);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sortmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.top:
                data = (FetchMovieData) new FetchMovieData(new AsyncResponse() {
                    @Override
                    public void processFinish(String movieDataJson) {
                        hashOutput(movieDataJson);
                        mAdapter = new MovieAdapter(movieList);
                        mRecyclerView.setAdapter(mAdapter);
                        MainActivity.this.setTitle("Top Rated Movies");



                    }
                }).execute(topRated);

                return true;
            case R.id.pop:
                data = (FetchMovieData) new FetchMovieData(new AsyncResponse() {
                    @Override
                    public void processFinish(String movieDataJson) {
                        hashOutput(movieDataJson);
                        mAdapter = new MovieAdapter(movieList);
                        mRecyclerView.setAdapter(mAdapter);
                        MainActivity.this.setTitle("Most Popular Movies");


                    }
                }).execute(mostPopular);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void hashOutput(String movieDataJson) {
        if(movieDataJson!=null){
            try{
                JSONObject jsonObject = new JSONObject(movieDataJson);
                movieList = new ArrayList<HashMap<String, String>>();
                results = jsonObject.getJSONArray("results");
                for(int i=0;i<results.length();i++){
                    JSONObject movieData = results.getJSONObject(i);
                    HashMap<String, String> movie = new HashMap();
                    movie.put("poster_path",posterUrl+movieData.getString("poster_path"));
                    movie.put("id",movieData.getString("id"));
                    movie.put("title",movieData.getString("title"));
                    movieList.add(movie);

                }
                Log.d("DATA:",String.valueOf(movieList.size()));


            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
    }


}

