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

import java.util.ArrayList;
import java.util.List;

import demo.thanhbc.com.popularmovies.pojo.DiscoverMovie;
import demo.thanhbc.com.popularmovies.presenters.MainFragmentPresenterImpl;
import demo.thanhbc.com.popularmovies.presenters.MainPresenter;
import demo.thanhbc.com.popularmovies.service.TheMovieDBService;
import demo.thanhbc.com.popularmovies.views.MainView;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;


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

    @Test
    public void testPerformance_for_loop(){
        List<Integer> data = new ArrayList<>();
        for(int i =0 ; i < 100000; ++i){
            data.add(i);
        }
        List<Integer> result = new ArrayList<>();

        for(int i = 0,size  = data.size(); i < size; i++){
            if(i%2==0){
                result.add(i);
            }
        }

        assertEquals(100000 / 2, result.size());
    }

    @Test
    public void testPerformance_rx(){
        List<Integer> data = new ArrayList<>();
        for(int i=0;i < 100000; ++i){
            data.add(i);
        }

        List<Integer> result = Observable.from(data)
                .filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer % 2 ==0;
                    }
                })
                .toList()
                .toBlocking().first();

        assertEquals(100000 / 2, result.size());

    }
}
