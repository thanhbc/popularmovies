package demo.thanhbc.com.popularmovies.presenters;

import demo.thanhbc.com.popularmovies.views.MainView;


public interface MainPresenter extends Presenter<MainView> {
    void discoverMovies(String sortBy);
    void searchMovies(String queryChar);
}
