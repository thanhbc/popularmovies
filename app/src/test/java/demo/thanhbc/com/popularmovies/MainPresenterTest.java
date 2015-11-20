package demo.thanhbc.com.popularmovies;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import demo.thanhbc.com.popularmovies.pojo.DiscoverMovie;
import demo.thanhbc.com.popularmovies.presenters.MainFragmentPresenterImpl;
import demo.thanhbc.com.popularmovies.presenters.MainPresenter;
import demo.thanhbc.com.popularmovies.service.TheMovieDBService;
import demo.thanhbc.com.popularmovies.views.MainView;
import rx.Observable;
import rx.schedulers.Schedulers;


@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 16)
public class MainPresenterTest {

    MainPresenter presenter;
    @Mock
    MainView view;
    @Mock
    TheMovieDBService.MovieApi service;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        PopularMoviesApplication application = (PopularMoviesApplication) RuntimeEnvironment.application;

        application.setService(service);
        presenter = new MainFragmentPresenterImpl();
        Mockito.when(view.getViewContext()).thenReturn(application);
        // Change the default subscribe schedulers so all observables
        // will now run on the same thread
        application.setDefaultSubscribeScheduler(Schedulers.immediate());
        presenter.attachView(view);
    }

    @After
    public void tearDown() {
        presenter.detachView();
    }


    @Test
    public void discoverMovies_TestSuccess() {
        String sortBy = view.getViewContext().getString(R.string.pref_sort_popular);

        DiscoverMovie movie = Utilities.createDummyData();
        Mockito.when(service.getMovies(sortBy, TheMovieDBService.API_KEY)).thenReturn(Observable.just(movie));


        presenter.discoverMovies(sortBy);
        Mockito.verify(view, Mockito.times(1)).loadData(movie.results);

    }

    @Test
    public void discoverMovies_TestError() {
        String sortBy = view.getViewContext().getString(R.string.pref_sort_popular);
        String errorMessage = "error";
        Mockito.when(service.getMovies(sortBy, TheMovieDBService.API_KEY)).thenReturn(Observable.<DiscoverMovie>error(new RuntimeException(errorMessage)));


        presenter.discoverMovies(sortBy);
        Mockito.verify(view, Mockito.times(1)).loadError(errorMessage);

    }

}
