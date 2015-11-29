package fieldwire.willsays.com.fieldwireimgur;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import fieldwire.willsays.com.fieldwireimgur.Adapter.RecyclerImageAdapter;
import fieldwire.willsays.com.fieldwireimgur.Data.Image;
import fieldwire.willsays.com.fieldwireimgur.Data.ImgurConstants;
import fieldwire.willsays.com.fieldwireimgur.Task.ImageAsyncTask;
import fieldwire.willsays.com.fieldwireimgur.Util.EndlessRecyclerOnScrollListener;

public class ScrollingActivity extends AppCompatActivity {

    // page for current search query
    private int page = 0;
    // current search query
    private String searchQuery = "test";
    // card creator
    private RecyclerImageAdapter imageAdapter;
    // recycliable scroll view
    private RecyclerView recyclerView;
    private List<Image> img = new ArrayList<Image>();
    private List<String> history = new ArrayList<String>();


    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;
    private AutoCompleteTextView edtSeach;
    private ArrayAdapter<String> dataAdapter;

    private String urlConstruct() {
        try {
            return ImgurConstants.MY_IMGUR_URL + Integer.toString(page) + "?q=" + URLEncoder.encode(searchQuery, "UTF-8") ;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return ImgurConstants.MY_IMGUR_URL + Integer.toString(page);
        }
    }

    /**
     * initialing ImageLoader and Image Cache
     */
    private void initial() {
        File cacheDir = StorageUtils.getCacheDirectory(this);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024)) // initial memory size
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .diskCache(new UnlimitedDiskCache(cacheDir)) // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100).threadPoolSize(3).build();

        ImageLoader.getInstance().init(config);
    }

    private void doSearch() {

        history.add(searchQuery);

        dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, history);
        edtSeach.setAdapter(dataAdapter);

        img.clear();
        imageAdapter.notifyDataSetChanged();
        page = 0;
        String[] url = {urlConstruct()};
        new ImageAsyncTask(imageAdapter).execute(url);
    }


    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        mSearchAction = menu.findItem(R.id.action_search);
        return super.onPrepareOptionsPanel(view, menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initial();

        setContentView(R.layout.activity_scrolling);

        edtSeach = (AutoCompleteTextView) findViewById(R.id.search);


        View doSearch = findViewById(R.id.action);

        doSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchQuery = edtSeach.getText().toString();
                doSearch();
            }
        });



        dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, history);
        edtSeach.setAdapter(dataAdapter);

        // Specify the layout to use when the list of choices appears
        //dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner

//        spinner.setAdapter(dataAdapter);
//
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                searchQuery = history.get(position);
//                doSearch();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });




        imageAdapter = new RecyclerImageAdapter(img, this);
        RecyclerView.LayoutManager layoutManager = null;
        int layoutOrientation = OrientationHelper.VERTICAL;
        layoutManager = new LinearLayoutManager(this, layoutOrientation, false);
        recyclerView = (RecyclerView) findViewById(R.id.abs_list_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(imageAdapter);

        String[] url = {urlConstruct()};
        new ImageAsyncTask(imageAdapter).execute(url);
        // on scroll load more
        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener((LinearLayoutManager) layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                page++;
                String[] url = {urlConstruct()};
                new ImageAsyncTask(imageAdapter).execute(url);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_search:
                doSearch();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
