package demo.thanhbc.com.popularmovies.views.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import demo.thanhbc.com.popularmovies.R;
import demo.thanhbc.com.popularmovies.pojo.Result;
import demo.thanhbc.com.popularmovies.views.MainView;
import demo.thanhbc.com.popularmovies.presenters.MainFragmentPresenterImpl;
import demo.thanhbc.com.popularmovies.callbacks.OnItemInteraction;
import demo.thanhbc.com.popularmovies.views.activities.DetailActivity;
import demo.thanhbc.com.popularmovies.views.adapter.MovieAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements MainView, OnItemInteraction {

    @Bind(R.id.grid_movives)
    RecyclerView gridMovies;

    MainFragmentPresenterImpl presenter;
    MovieAdapter adapter;
    StaggeredGridLayoutManager gridLayoutManager;

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
//        gridLayoutManager=new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        gridMovies.setLayoutManager(gridLayoutManager);
        gridMovies.setLayoutManager(new GridLayoutManager(getContext(), 2));
        gridMovies.setAdapter(adapter);
        return rootView;
    }

    private String getSortOptionFromPref() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortBy = preferences.getString(
                getActivity().getString(R.string.pref_sort_key),
                getActivity().getString(R.string.pref_sort_popular));
        return sortBy;
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
    public void onItemClick(Result result, int position) {
        startActivity(DetailActivity.newIntent(getViewContext(), result.original_title, result.poster_path, result.overview, String.valueOf(result.vote_average), result.release_date));
    }

    @Override
    public Context getViewContext() {
        return this.getContext();
    }
}
