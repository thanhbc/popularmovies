package demo.thanhbc.com.popularmovies;

import android.app.Application;
import android.content.Context;

import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import demo.thanhbc.com.popularmovies.service.TheMovieDBService;


public class PopularMoviesApplication extends Application {

    private TheMovieDBService.MovieApi service;
    private Scheduler subscribeScheduler;

    public static PopularMoviesApplication get(Context context){
        return (PopularMoviesApplication) context.getApplicationContext();
    }

    public TheMovieDBService.MovieApi getService(){
        if(service==null){
            service=new TheMovieDBService().getApi();
        }
        return service;
    }

    final Observable.Transformer schedulersTransformer= new Observable.Transformer() {
        @Override
        public Observable call(Object o) {
            return ((Observable)o).subscribeOn(defaultSubscribeScheduler())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(defaultSubscribeScheduler()); //temp fix android.os.NetworkOnMainThreadException on okhttp lib
        }
    };


    //Reusing Transformers - Singleton
    @SuppressWarnings("unchecked")
    public <T> Observable.Transformer<T, T> applySchedulers() {
        return (Observable.Transformer<T, T>) schedulersTransformer;
    }

    public Scheduler defaultSubscribeScheduler(){
        if(subscribeScheduler==null){
            subscribeScheduler= Schedulers.io();
        }
        return subscribeScheduler;
    }

    //for testing
    public void setService(TheMovieDBService.MovieApi service) {
        this.service=service;
    }


    public void setDefaultSubscribeScheduler(Scheduler defaultSubscribeScheduler) {
        this.subscribeScheduler = defaultSubscribeScheduler;
    }
}
