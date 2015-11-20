package demo.thanhbc.com.popularmovies.presenters;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;
import demo.thanhbc.com.popularmovies.PopularMoviesApplication;
import demo.thanhbc.com.popularmovies.pojo.DiscoverMovie;
import demo.thanhbc.com.popularmovies.pojo.Result;
import demo.thanhbc.com.popularmovies.service.TheMovieDBService;
import demo.thanhbc.com.popularmovies.views.MainView;


public class MainFragmentPresenterImpl implements MainPresenter {


    TheMovieDBService.MovieApi service;
    MainView view;
    PopularMoviesApplication popularMoviesApplication;
    private CompositeSubscription subscription;

    public MainFragmentPresenterImpl() {
        subscription = new CompositeSubscription();
    }


    @Override
    public void discoverMovies(String sortBy) {

        subscription.add(service.getMovies(sortBy, TheMovieDBService.API_KEY)
                .subscribeOn(popularMoviesApplication.defaultSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
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
        if (subscription != null) subscription.unsubscribe();
    }
}
