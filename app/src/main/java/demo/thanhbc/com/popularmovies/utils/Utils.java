package demo.thanhbc.com.popularmovies.utils;

import android.database.MatrixCursor;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;

import java.util.List;

import demo.thanhbc.com.popularmovies.pojo.Result;

/**
 * Created by thanhbc on 2/26/16.
 */
public class Utils {

    public static final String[] COLUMNS = new String[]{"_id", "movie_name", "release_date"};

    public static MatrixCursor convertResultsToCursor(List<Result> results) {
        MatrixCursor matrixCursor = new MatrixCursor(COLUMNS);
        if (results != null) {
            int i = 0;
            for (Result rs : results){
                String [] temp = new String [3];
                temp[0] = String.valueOf(i);
                temp[1] = rs.title;
                temp[2] = rs.release_date;
                matrixCursor.addRow(temp);
                i = i + 1;
            }
        }
        return matrixCursor;
    }

    public static void hideSoftKeyboard(AppCompatActivity activity) {
        if (activity != null && activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }
}
