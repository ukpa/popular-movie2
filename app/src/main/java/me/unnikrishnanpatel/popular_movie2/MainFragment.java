package me.unnikrishnanpatel.popular_movie2;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private JSONArray results = null;
    private ArrayList<HashMap<String,String>> movieList;
    private String posterUrl = "http://image.tmdb.org/t/p/w342/";
    private FetchMovieData data;
    String mostPopular = "http://api.themoviedb.org/3/movie/popular?api_key=7baf82d2c99ba2997a60d4af8b763034";
    String topRated = "http://api.themoviedb.org/3/movie/top_rated?api_key=7baf82d2c99ba2997a60d4af8b763034";
    SharedPreferences sharedPreferences;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        this.setHasOptionsMenu(true);



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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.sortmenu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.top:
                data = (FetchMovieData) new FetchMovieData(new AsyncResponse() {
                    @Override
                    public void processFinish(String movieDataJson) {
                        hashOutput(movieDataJson);
                        mAdapter = new MovieAdapter(movieList);
                        mRecyclerView.setAdapter(mAdapter);
                        getActivity().setTitle("Top Rated Movies");
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
                        getActivity().setTitle("Most Popular Movies");


                    }
                }).execute(mostPopular);

                return true;
            case R.id.fav:
                Map<String,?> keys = sharedPreferences.getAll();
                movieList.clear();

                for(Map.Entry<String,?> entry : keys.entrySet()){

                    HashMap<String, String> movie = new HashMap();
                    movie.put("poster_path", String.valueOf(entry.getValue()));
                    movie.put("id",String.valueOf(entry.getKey()));

                    movieList.add(movie);
                }
                mAdapter = new MovieAdapter(movieList);
                mRecyclerView.setAdapter(mAdapter);
                getActivity().setTitle("Your favourite Movies");


            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        getActivity().setTitle("Top Rated Movies");
        mRecyclerView = (RecyclerView)v.findViewById(R.id.movieGrid);
        mLayoutManager = new GridLayoutManager(getActivity(),2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        sharedPreferences = getActivity().getSharedPreferences("saved_movies",Context.MODE_PRIVATE);

        data = (FetchMovieData) new FetchMovieData(new AsyncResponse() {
            @Override
            public void processFinish(String movieDataJson) {
                hashOutput(movieDataJson);
                mAdapter = new MovieAdapter(movieList);
                mRecyclerView.setAdapter(mAdapter);


            }
        }).execute(topRated);
        return v;
    }


}
