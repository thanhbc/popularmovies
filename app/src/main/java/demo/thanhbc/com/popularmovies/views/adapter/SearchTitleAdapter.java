package demo.thanhbc.com.popularmovies.views.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.TextView;

import demo.thanhbc.com.popularmovies.R;

/**
 * Created by thanhbc on 2/26/16.
 */
public class SearchTitleAdapter extends SimpleCursorAdapter {

    public SearchTitleAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {


        TextView textView = (TextView) view.findViewById(R.id.tvMovieTitle);
        textView.setText(cursor.getString(0) + " : " + cursor.getString(1) + " - " + cursor.getString(2).split("-")[0].toString());
    }
}
