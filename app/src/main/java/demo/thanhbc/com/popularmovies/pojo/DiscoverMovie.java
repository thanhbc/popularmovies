package demo.thanhbc.com.popularmovies.pojo;

import java.util.ArrayList;
import java.util.List;


public class DiscoverMovie {
    public Integer page;
    public List<Result> results = new ArrayList<Result>();
    public Integer total_pages;
    public Integer total_results;
}
