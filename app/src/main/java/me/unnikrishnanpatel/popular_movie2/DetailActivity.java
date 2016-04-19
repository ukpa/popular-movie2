package me.unnikrishnanpatel.popular_movie2;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.HashMap;

public class DetailActivity extends AppCompatActivity {

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
