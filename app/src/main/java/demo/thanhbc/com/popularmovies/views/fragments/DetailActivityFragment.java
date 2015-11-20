package demo.thanhbc.com.popularmovies.views.fragments;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import demo.thanhbc.com.popularmovies.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private static final String TITLE="title";
    private static final String THUMBNAIL="thumbnail";
    private static final String OVERVIEW="overview";
    private static final String USER_RATING="user_rating";
    private static final String RELEASE_DATE="release_date";

    final String BASE_IMG_URL = "http://image.tmdb.org/t/p/w342";

    @Bind(R.id.title_text)
    TextView titleText;
    @Bind(R.id.thumbnail_image)
    ImageView thumbnailImage;

    @Bind(R.id.overview_text)
    TextView overviewText;

    @Bind(R.id.user_rating_text)
    TextView userRatinText;

    @Bind(R.id.release_date_text)
    TextView releaseDateText;

    public DetailActivityFragment() {

    }

    public static Fragment getInstance(Bundle extras) {
        DetailActivityFragment fragment =new DetailActivityFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String title = getArguments().getString(TITLE);
        String thumbnail = BASE_IMG_URL+getArguments().getString(THUMBNAIL);
        String overview = getArguments().getString(OVERVIEW);
        String userRating = getArguments().getString(USER_RATING);
        String releaseDate = getArguments().getString(RELEASE_DATE);

        titleText.setText(title);
        Picasso.with(getContext()).load(thumbnail).into(thumbnailImage);
        overviewText.setText(overview);
        userRatinText.setText(userRating);
        releaseDateText.setText(releaseDate);
    }


}
