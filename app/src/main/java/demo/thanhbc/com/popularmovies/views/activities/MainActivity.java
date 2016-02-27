package demo.thanhbc.com.popularmovies.views.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import demo.thanhbc.com.popularmovies.R;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rxsampleJust();
        rxsampleFrom();
        rxSchedulerSampleSync();
        rxSchedulerSampleASync();
    }

    private void rxsampleJust(){
        Observable.just("Hello Guys!")
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d(TAG, ">>>>>>>" + s + "<<<<<<<"); // Synchronous
                    }
                });

        Observable.just("Hello Guys!")
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d(TAG, ">>>>>>>" + s + "<<<<<<<");
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.d(TAG, ">>>>>>> Error, cant say hi to your guys <<<<<<<");
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        Log.d(TAG, ">>>>>>> Completed <<<<<<<");
                    }
                });

    }

private void rxsampleFrom(){
    List<Integer> integers = new ArrayList<>();
    integers.add(1);
    integers.add(2);
    integers.add(3);
    integers.add(4);
    integers.add(5);

    Observable.from(integers).subscribe(new Action1<Integer>() {
        @Override
        public void call(Integer integer) {
            Log.d(TAG, ">>>>>>> integer: " + integer);
        }
    });
}

    private void rxSchedulerSampleSync(){
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String threadID = String.valueOf(Thread.currentThread().getId());
                Log.d(TAG, "working on thread: " + threadID);
                subscriber.onNext("Android");
                subscriber.onNext("Meetup #3");
                subscriber.onCompleted();
            }
        })
        .subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                String threadID = String.valueOf(Thread.currentThread().getId());
                Log.d(TAG, "onNext on thread: " + threadID + " -> " + s);
            }
        });
    }
    private void rxSchedulerSampleASync(){
                Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        String threadID = String.valueOf(Thread.currentThread().getId());
                        Log.d(TAG,"working on thread: " + threadID);
                        subscriber.onNext("Android");
                        subscriber.onNext("Meetup #3");
                        subscriber.onCompleted();
                    }
                })
                .observeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        String threadID = String.valueOf(Thread.currentThread().getId());
                        Log.d(TAG, "onNext on thread: " + threadID + " -> " + s);
                    }
                });
    }


}
