package demo.thanhbc.com.popularmovies.views.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import demo.thanhbc.com.popularmovies.R;
import demo.thanhbc.com.popularmovies.callbacks.OnItemInteraction;
import demo.thanhbc.com.popularmovies.pojo.Result;
import demo.thanhbc.com.popularmovies.presenters.MainFragmentPresenterImpl;
import demo.thanhbc.com.popularmovies.presenters.MainPresenter;
import demo.thanhbc.com.popularmovies.utils.Utils;
import demo.thanhbc.com.popularmovies.views.MainView;
import demo.thanhbc.com.popularmovies.views.activities.DetailActivity;
import demo.thanhbc.com.popularmovies.views.activities.SettingsActivity;
import demo.thanhbc.com.popularmovies.views.adapter.MovieAdapter;
import demo.thanhbc.com.popularmovies.views.adapter.SearchTitleAdapter;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements MainView, OnItemInteraction, SearchView.OnQueryTextListener,SearchView.OnSuggestionListener {


    private static final String TAG = MainActivityFragment.class.getSimpleName();
    @Bind(R.id.grid_movives)
    RecyclerView gridMovies;

    MainPresenter presenter;
    MovieAdapter adapter;
    StaggeredGridLayoutManager gridLayoutManager;

    private SearchView searchView;
    private MatrixCursor matrixCursor;

    private SearchTitleAdapter searchViewAdapter;
    public MainActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);
        presenter = new MainFragmentPresenterImpl();
        presenter.attachView(this);
        adapter = new MovieAdapter(getActivity(), new ArrayList<Result>());
        adapter.setOnItemInteractionListen(this);
        gridMovies.setLayoutManager(new GridLayoutManager(getContext(), 2));
        gridMovies.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }

    private String getSortOptionFromPref() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortBy = preferences.getString(
                getActivity().getString(R.string.pref_sort_key),
                getActivity().getString(R.string.pref_sort_popular));
        return sortBy;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        try {
            searchView = (SearchView) MenuItemCompat.getActionView(item);
            searchView.setQueryHint("Search by");
            searchView.setOnQueryTextListener(this);
            searchView.setOnSuggestionListener(this);
            matrixCursor = new MatrixCursor(Utils.COLUMNS);

            searchViewAdapter = new SearchTitleAdapter(getActivity(), R.layout.search_auto_complete_item_layout, matrixCursor, Utils.COLUMNS, null, -1100);
            searchView.setSuggestionsAdapter(searchViewAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            SettingsActivity.launch(getActivity());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.discoverMovies(getSortOptionFromPref());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        if (!matrixCursor.isClosed())
            matrixCursor.close();
    }

    @Override
    public void loadData(List<Result> results) {
        adapter.fill(results);
    }

    @Override
    public void loadError(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void loadSearchResults(List<Result> results) {

            matrixCursor = Utils.convertResultsToCursor(results);
            searchViewAdapter.changeCursor(matrixCursor);
    }

    @Override
    public void onItemClick(Result result, int position) {
        startActivity(DetailActivity.newIntent(getViewContext(), result.original_title, result.poster_path, result.overview, String.valueOf(result.vote_average), result.release_date));
    }

    @Override
    public Context getViewContext() {
        return this.getContext();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if(query.length() > 2){
            try{
                String encodedQuery = URLEncoder.encode(query, "UTF-8");
                searchView.clearFocus();
                Utils.hideSoftKeyboard((AppCompatActivity) getActivity());
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return true;
    }


private Subscription searchObservableSubscription;
    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d("MainActivityFragment", newText);
        if (newText.length() > 2) {
            if (searchObservableSubscription != null && !searchObservableSubscription.isUnsubscribed()) {
                //Cancel all ongoing requests and change cursor
                searchObservableSubscription.unsubscribe();
                matrixCursor = Utils.convertResultsToCursor(new ArrayList<Result>());
                searchViewAdapter.changeCursor(matrixCursor);
            }
            searchObservableSubscription =  getASearchObservableFor(newText)
                    .debounce(2000, TimeUnit.MILLISECONDS)
                    .subscribe(searchStringObserver());
        }
        return true;
    }

    private Observer<String> searchStringObserver(){
        return  new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.d(TAG,"Search for : " + s);
                presenter.searchMovies(s);

            }
        };
    }

    private Observable<String> getASearchObservableFor(final String searchText) {
        return Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(searchText);
            }
        });
    }

    @Override
    public boolean onSuggestionSelect(int position) {
        return false;
    }

    @Override
    public boolean onSuggestionClick(int position) {
        Cursor cursor = (Cursor) searchView.getSuggestionsAdapter().getItem(position);
        String movieTitle = cursor.getString(1);
        searchView.setQuery(movieTitle, false);
        searchView.clearFocus();
        //do something with result here
        return true;
    }
}
