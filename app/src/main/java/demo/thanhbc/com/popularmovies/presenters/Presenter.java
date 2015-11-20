package demo.thanhbc.com.popularmovies.presenters;


public interface Presenter<V> {

    void attachView(V view);
    void detachView();
}
