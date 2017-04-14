package com.wordpress.juniadev.newsapp;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Main app activity.
 */
public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private static final String API_URL = "http://content.guardianapis.com/search";
    private static final String API_QUERY_PARAM = "q";
    private static final String API_QUERY_VALUE = "technology";
    private static final String API_KEY_PARAM = "api-key";
    private static final String API_KEY_VALUE = "test";

    private NewsAdapter adapter;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        setSwipeRefreshListener();

        TextView emptyView = getEmptyView();
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setEmptyView(emptyView);

        setScrollListener(listView);

        adapter = new NewsAdapter(this, new ArrayList<News>());

        listView.setAdapter(adapter);

        setListOnItemListener(listView);

        if (!isConnected()) {
            emptyView.setText(R.string.no_internet_connection);
            getSpinner().setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            getSpinner().setVisibility(View.VISIBLE);
            getLoaderManager().initLoader(0, null, this);
        }
    }

    /**
     * Avoids refreshing while scrolling to top.
     * http://stackoverflow.com/a/25486615
     * @param listView
     */
    private void setScrollListener(ListView listView) {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // No need to implement.
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    swipeRefreshLayout.setEnabled(true);
                } else {
                    swipeRefreshLayout.setEnabled(false);
                }
            }
        });
    }

    private ProgressBar getSpinner() {
        return (ProgressBar) findViewById(R.id.spinner);
    }

    private TextView getEmptyView() {
        return (TextView) findViewById(R.id.empty);
    }

    private void setSwipeRefreshListener() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        refresh();
                    }
                }
        );
    }

    private void refresh() {
        if (!isConnected()) {
            TextView emptyView = getEmptyView();
            emptyView.setText(R.string.no_internet_connection);
            emptyView.setVisibility(View.VISIBLE);
            getSpinner().setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
        } else {
            getLoaderManager().restartLoader(0, null, NewsActivity.this);
        }
    }

    private void setListOnItemListener(ListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String url = adapter.getItem(position).getUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
    }

    private boolean isConnected() {
        ConnectivityManager connectivityMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityMgr.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        Uri baseUri = Uri.parse(API_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter(API_QUERY_PARAM, API_QUERY_VALUE);
        uriBuilder.appendQueryParameter(API_KEY_PARAM, API_KEY_VALUE);
        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        adapter.clear();

        getSpinner().setVisibility(View.GONE);

        swipeRefreshLayout.setRefreshing(false);

        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
        } else {
            getEmptyView().setText(R.string.no_news_found);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        adapter.clear();
    }
}
