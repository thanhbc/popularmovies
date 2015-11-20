package demo.thanhbc.com.popularmovies.callbacks;

import demo.thanhbc.com.popularmovies.pojo.Result;


public interface OnItemInteraction {
   void onItemClick(Result result, int position);
}
