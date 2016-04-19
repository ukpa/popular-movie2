package me.unnikrishnanpatel.popular_movie2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class DetailFragment extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(String param1, String param2) {
        DetailFragment fragment = new DetailFragment();
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


    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v =  inflater.inflate(R.layout.fragment_detail, container, false);
        final HashMap<String,String> movieData= (HashMap<String, String>) getArguments().getSerializable("data");
        Log.d(this.getClass().getSimpleName(),String.valueOf(movieData));
        String url = "https://api.themoviedb.org/3/movie/"+movieData.get("id")+"?api_key=7baf82d2c99ba2997a60d4af8b763034";
        sharedPreferences = getActivity().getSharedPreferences("saved_movies",Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        fav = (Button)v.findViewById(R.id.markAsFav);
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
                    poster = (ImageView)v.findViewById(R.id.moviePoster);
                    movieLength = (TextView)v.findViewById(R.id.movieLength);
                    releaseDate = (TextView)v.findViewById(R.id.release);
                    overview = (TextView)v.findViewById(R.id.overview);
                    rating = (TextView)v.findViewById(R.id.rating);
                    tagline = (TextView)v.findViewById(R.id.tagline);
                    Picasso.with(getActivity()).load(baseUrl+jsonObject.get("poster_path")).into(poster);
                    movieLength.setText("Duration: "+jsonObject.get("runtime").toString()+" Minutes");
                    releaseDate.setText("Release: "+jsonObject.get("release_date").toString());
                    rating.setText("Rating: "+jsonObject.get("vote_average").toString()+"/10");
                    overview.setText("Overview:\n\n"+jsonObject.get("overview").toString());
                    tagline.setText(jsonObject.get("tagline").toString());
                    moviedetails = jsonObject.get("title").toString();
                    getActivity().setTitle(jsonObject.get("title").toString());

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
                Context context = getActivity().getApplicationContext();
                CharSequence text = "Movie \""+movieData.get("title")+"\" added to the Favourite list :)!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                fav.setEnabled(false);
                fav.setText("Love it");
            }
        });

        trailersFetch = (FetchMovieData) new FetchMovieData(new AsyncResponse() {
            @Override
            public void processFinish(String output) {

                if (output != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(output);
                        JSONArray results = jsonObject.getJSONArray("results");
                        trailers = new ArrayList<String>();
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject xxdata= results.getJSONObject(i);
                            trailers.add(String.valueOf(xxdata.get("key")));

                        }
                        Log.d("Trailers",String.valueOf(trailers.size()));
                        LinearLayout linearLayout = (LinearLayout)v.findViewById(R.id.trailersLayout);
                        LinearLayout l;
                        for(int i =0;i<trailers.size();i++){
                            l = new LinearLayout(getActivity());
                            l.setOrientation(LinearLayout.HORIZONTAL);

                            Button t = new Button(getActivity());
                            Button s = new Button(getActivity());
                            final String url = "https://www.youtube.com/watch?v="+trailers.get(i);
                            t.setText("Watch Trailer #"+String.valueOf(i+1));
                            s.setText("Share");
                            l.addView(t);
                            l.addView(s);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.weight = 1.0f;
                            params.gravity = Gravity.CENTER;
                            t.setLayoutParams(params);
                            s.setLayoutParams(params);
                            t.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                                }
                            });
                            s.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent sendIntent = new Intent();
                                    sendIntent.setAction(Intent.ACTION_SEND);
                                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Watch "+moviedetails+"\'s trailer at "+url+" . Looks promising.");
                                    sendIntent.setType("text/plain");
                                    startActivity(Intent.createChooser(sendIntent, "Amuse your friends"));
                                }
                            });
                            linearLayout.addView(l);

                        }




                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).execute("http://api.themoviedb.org/3/movie/"+movieData.get("id")+"/videos?api_key=7baf82d2c99ba2997a60d4af8b763034");


        reviewFetch = (FetchMovieData) new FetchMovieData(new AsyncResponse() {
            @Override
            public void processFinish(String output) {

                if (output!=null){
                    try{

                        JSONObject jsonObject = new JSONObject(output);
                        JSONArray results = jsonObject.getJSONArray("results");
                        ArrayList<HashMap<String,String>> reviews= new ArrayList<>();
                        LinearLayout layout = (LinearLayout)v.findViewById(R.id.reviews);
                        for(int i=0;i<results.length();i++){
                            JSONObject reviewData = results.getJSONObject(i);
                            /*HashMap<String, String> review = new HashMap();
                            review.put("author",reviewData.getString("author"));
                            review.put("content", reviewData.getString("content"));
                            reviews.add(review);*/
                            LinearLayout l = new LinearLayout(getActivity());
                            l.setOrientation(LinearLayout.VERTICAL);
                            l.setBackgroundColor(Color.GRAY);
                            l.setPadding(10,10,10,10);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                            layoutParams.setMargins(0, 0, 0, 10);
                            l.setLayoutParams(layoutParams);
                            TextView author = new TextView(getActivity());
                            TextView content  = new TextView(getActivity());
                            author.setText("Author: "+reviewData.getString("author"));
                            content.setText(reviewData.getString("content"));
                            l.addView(author);
                            l.addView(content);
                            layout.addView(l);

                        }


                    }catch (JSONException e){}
                }
            }
        }).execute("http://api.themoviedb.org/3/movie/"+movieData.get("id")+"/reviews?api_key=7baf82d2c99ba2997a60d4af8b763034");
        return v;
    }

}
