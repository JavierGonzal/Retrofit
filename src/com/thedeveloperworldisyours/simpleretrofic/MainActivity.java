package com.thedeveloperworldisyours.simpleretrofic;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.thedeveloperworldisyours.simpleretrofic.model.Question;
import com.thedeveloperworldisyours.simpleretrofic.utils.Constants;
import com.thedeveloperworldisyours.simpleretrofic.utils.Utils;
import com.thedeveloperworldisyours.simpleretrofic.webservice.RestClient;

public class MainActivity extends ActionBarActivity {

	List<String> mListTitle = new ArrayList<String>();
	private ListView mListView;
	
		protected ListView getListView() {
		    if (mListView == null) {
		        mListView = (ListView) findViewById(android.R.id.list);
		    }
		    return mListView;
		}
	
		protected void setListAdapter(ListAdapter adapter) {
		    getListView().setAdapter(adapter);
		}
	
		protected ListAdapter getListAdapter() {
		    ListAdapter adapter = getListView().getAdapter();
		    if (adapter instanceof HeaderViewListAdapter) {
		        return ((HeaderViewListAdapter)adapter).getWrappedAdapter();
		    } else {
		        return adapter;
		    }
		}
	
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_main);
		getQuestionCheckInternet();
		
		
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return true;
    }
	@Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
         
        switch (item.getItemId())
        {
        case R.id.action_refresh:
        	refreshList();
        	if(Utils.isOnline(MainActivity.this)){
    			Toast.makeText(this, R.string.action_refresh, Toast.LENGTH_SHORT).show();
    		}
            return true;
 
        default:
            return super.onOptionsItemSelected(item);
        }
        }
	public void refreshList(){
		mListTitle.clear();
		getQuestionCheckInternet();
		createList();
		
	}
	
	public void getQuestionCheckInternet(){
		if(Utils.isOnline(MainActivity.this)){
			getQuestions();
		}else{
			Toast.makeText(this, R.string.info_offline, Toast.LENGTH_LONG).show();
		}
	}
	
	protected void onListItemClick(ListView lv, View v, int position, long id) {
	    getListView().getOnItemClickListener().onItemClick(lv, v, position, id);
	    String item = (String) getListAdapter().getItem(position);
		Toast.makeText(this, item, Toast.LENGTH_LONG).show();
	}
	

	public void getQuestions() {
		Callback<Question> callback = new Callback<Question>() {

			@Override
			public void failure(RetrofitError resp) {
				Log.v("failure", String.valueOf(resp.getMessage()));
			}

			@Override
			public void success(Question info, Response resp) {
				Log.v("success", String.valueOf(resp.getStatus()));
				getTitle(info);
	            createList();
			}
		};
		RestClient.initRestAdapter().getQuestions(Constants.SORT, Constants.SITE,
				Constants.PAGESIZE, Constants.PAGE, callback);
	}
	

	public List<String> getTitle(Question info) {
		for (int i = 0; i < info.getItems().size(); i++) {
			mListTitle.add(info.getItems().get(i).getTitle());
		}
		return mListTitle;
	}
	public void createList(){
		
		// use your custom layout
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		        android.R.layout.simple_list_item_1,mListTitle);
		setListAdapter(adapter);
	}
}
