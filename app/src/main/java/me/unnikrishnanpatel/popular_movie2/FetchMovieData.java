package me.unnikrishnanpatel.popular_movie2;
import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by unnikrishnanpatel on 18/04/16.
 */
public class FetchMovieData extends AsyncTask<String,Void,String> {

    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    String movieDataJson = null;
    public AsyncResponse delegate = null;

    public FetchMovieData(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            movieDataJson = buffer.toString();


        } catch (IOException e) {

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(this.getClass().getSimpleName(), "Error closing stream", e);
                }
            }
        }
        return movieDataJson;
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }
}

