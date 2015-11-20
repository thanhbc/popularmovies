package demo.thanhbc.com.popularmovies;

import android.app.Application;
import android.content.Context;

import rx.Scheduler;
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
