package demo.thanhbc.com.popularmovies;

import java.util.ArrayList;
import java.util.List;

import demo.thanhbc.com.popularmovies.pojo.DiscoverMovie;
import demo.thanhbc.com.popularmovies.pojo.Result;


public class Utilities {

    public static DiscoverMovie createDummyData(){
        DiscoverMovie discoverMovie =new DiscoverMovie();

        List<Result> results = new ArrayList<>(4);
        for (int i=0;i<4;i++){
            results.add(new Result());
        }
        discoverMovie.results=results;
        discoverMovie.page=1;
        discoverMovie.total_pages=1;
        discoverMovie.total_results=4;
        return discoverMovie;
    }


}
