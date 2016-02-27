package demo.thanhbc.com.popularmovies.service;

import com.squareup.okhttp.OkHttpClient;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;
import demo.thanhbc.com.popularmovies.pojo.DiscoverMovie;


public class TheMovieDBService {
    private static final String THE_MOVIE_SERVICE_URL = "http://api.themoviedb.org/3";
    public static final String API_KEY="cf3d601dd37998cdb8756384a969d34d";

    private MovieApi movieApi;

    public TheMovieDBService(){
        RequestInterceptor interceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Accept","application/json");
            }
        };

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setClient(new OkClient(new OkHttpClient()))
                .setEndpoint(THE_MOVIE_SERVICE_URL)
                .setRequestInterceptor(interceptor)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        movieApi=restAdapter.create(MovieApi.class);
    }

    public MovieApi getApi(){
        return movieApi;
    }


     public interface MovieApi{
        @GET("/discover/movie")
        Observable<DiscoverMovie> getMovies(@Query("sort_by") String sort,@Query("api_key") String api_key);

         @GET("/search/movie")
        Observable<DiscoverMovie> getSearchResult(@Query("query") String query, @Query("api_key") String api_key);

    }
}
