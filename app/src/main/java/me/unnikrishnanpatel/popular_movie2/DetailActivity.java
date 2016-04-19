package me.unnikrishnanpatel.popular_movie2;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class DetailActivity extends AppCompatActivity {
    FetchMovieData data,trailersFetch,reviewFetch;
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
    ArrayList<String> trailers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        DetailFragment detailFragment = new DetailFragment();
        Intent i = getIntent();
        Bundle bundle = new Bundle();
        HashMap<String,String> mov = (HashMap<String, String>) i.getSerializableExtra("data");
        bundle.putSerializable("data",mov);
        detailFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,detailFragment).
                addToBackStack(null).commit();




    }

}
