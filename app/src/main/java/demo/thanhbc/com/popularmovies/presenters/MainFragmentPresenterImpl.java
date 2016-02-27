package demo.thanhbc.com.popularmovies.presenters;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import demo.thanhbc.com.popularmovies.PopularMoviesApplication;
import demo.thanhbc.com.popularmovies.pojo.DiscoverMovie;
import demo.thanhbc.com.popularmovies.pojo.Result;
import demo.thanhbc.com.popularmovies.service.TheMovieDBService;
import demo.thanhbc.com.popularmovies.views.MainView;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class MainFragmentPresenterImpl implements MainPresenter {


    TheMovieDBService.MovieApi service;
    MainView view;
    PopularMoviesApplication popularMoviesApplication;
    private CompositeSubscription subscription;

    private Subscription searchResultSubscription;

    public MainFragmentPresenterImpl() {
        subscription = new CompositeSubscription();
    }


    @Override
    public void discoverMovies(String sortBy) {

        subscription.add(service.getMovies(sortBy, TheMovieDBService.API_KEY)
                .compose(popularMoviesApplication.<DiscoverMovie>applySchedulers())
                .cache()
                .map(new Func1<DiscoverMovie, List<Result>>() {
                    @Override
                    public List<Result> call(DiscoverMovie discoverMovie) {
                        return discoverMovie.results;
                    }
                }).subscribe(new Observer<List<Result>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.loadError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<Result> results) {
                        view.loadData(results);
                    }
                }));
//                .map(d->d.results)
//                .subscribe(results -> view.loadData(results),
//                           e->view.loadError(e.getMessage()),
//                           ()->{}));
    }

    @Override
    public void searchMovies(String queryChar) {

        try {

            String encodedQuery = URLEncoder.encode(queryChar, "UTF-8");
            if (searchResultSubscription != null && !searchResultSubscription.isUnsubscribed()) {
                //Cancel all ongoing requests
                searchResultSubscription.unsubscribe();
            }
            Observable<DiscoverMovie> observable = service.getSearchResult(encodedQuery, TheMovieDBService.API_KEY);
            searchResultSubscription = observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(new Func1<DiscoverMovie, List<Result>>() {
                        @Override
                        public List<Result> call(DiscoverMovie searchResult) {
                            return searchResult.results; // list of movies info
                        }
                    })
                    .subscribe(searchResultsSubscriber());

            //                    .compose(popularMoviesApplication.<DiscoverMovie>applySchedulers())
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }


    private Subscriber<List<Result>> searchResultsSubscriber(){

        return new Subscriber<List<Result>>() {
            @Override
            public void onCompleted() {
                Log.d("MovieSearch", "Completed");
            }

            @Override
            public void onError(Throwable e) {
                view.loadError(e.getMessage().toString());
            }

            @Override
            public void onNext(List<Result> results) {
                view.loadSearchResults(results);
            }
        };
    }
    @Override
    public void attachView(MainView view) {
        this.view = view;
        setUpService();
    }

    private void setUpService() {
        popularMoviesApplication = PopularMoviesApplication.get(view.getViewContext());
        this.service = popularMoviesApplication.getService();

    }

    @Override
    public void detachView() {
        this.view = null;
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
        if(searchResultSubscription != null && !searchResultSubscription.isUnsubscribed())
            searchResultSubscription.unsubscribe();
    }
}
