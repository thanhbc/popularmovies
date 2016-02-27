package demo.thanhbc.com.popularmovies.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import demo.thanhbc.com.popularmovies.R;
import demo.thanhbc.com.popularmovies.callbacks.OnItemInteraction;
import demo.thanhbc.com.popularmovies.pojo.Result;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> implements OnGridItemClickListener {

    final String BASE_IMG_URL = "http://image.tmdb.org/t/p/w342";
    List<Result> imgList;
    OnItemInteraction onItemInteraction;
    private Context context;

    public MovieAdapter(Context context, List<Result> imgList) {
        this.context = context;
        this.imgList = imgList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        return new ViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        String imageUrl = BASE_IMG_URL + imgList.get(i).poster_path;
        Picasso.with(context).load(imageUrl).into(viewHolder.movieImg);

    }

    public void setOnItemInteractionListen(OnItemInteraction onItemInteraction) {
        this.onItemInteraction = onItemInteraction;
    }

    @Override
    public int getItemCount() {
        return imgList.size();
    }


    public void fill(List<Result> datas) {
        imgList.clear();
        imgList.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public void onClick(int position) {
        onItemInteraction.onItemClick(imgList.get(position), position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        OnGridItemClickListener itemClickListener;
        @Bind(R.id.movie_img)
        ImageView movieImg;

        public ViewHolder(View itemView, OnGridItemClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            movieImg.setOnClickListener(this);
            itemClickListener = listener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(getAdapterPosition());
        }
    }
}
