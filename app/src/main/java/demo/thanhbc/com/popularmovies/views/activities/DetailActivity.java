package demo.thanhbc.com.popularmovies.views.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import demo.thanhbc.com.popularmovies.R;
import demo.thanhbc.com.popularmovies.views.fragments.DetailActivityFragment;

public class DetailActivity extends AppCompatActivity {

    private static final String TITLE="title";
    private static final String THUMBNAIL="thumbnail";
    private static final String OVERVIEW="overview";
    private static final String USER_RATING="user_rating";
    private static final String RELEASE_DATE="release_date";

    FragmentManager fragmentManager;

    public static Intent newIntent(Context context,String title,String thumbnail,String overview,String userRating, String releaseDate){
        Intent intent = new Intent(context,DetailActivity.class);
        intent.putExtra(TITLE,title);
        intent.putExtra(THUMBNAIL   ,thumbnail);
        intent.putExtra(OVERVIEW,overview);
        intent.putExtra(USER_RATING,userRating);
        intent.putExtra(RELEASE_DATE,releaseDate);
       return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if(savedInstanceState==null){
            fragmentManager=getSupportFragmentManager();
            FragmentTransaction transaction =fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_container,DetailActivityFragment.getInstance(getIntent().getExtras()),"DETAIL_MOVIE").commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
