package demo.thanhbc.com.popularmovies.views;

import java.util.List;

import demo.thanhbc.com.popularmovies.pojo.Result;


public interface MainView extends CommonView {
    void loadData(List<Result> results);
    void loadError(String message);
}
