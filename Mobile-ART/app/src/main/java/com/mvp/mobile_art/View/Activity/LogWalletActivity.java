package com.mvp.mobile_art.View.Activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.mvp.mobile_art.Model.Adapter.RecyclerAdapterLogWallet;
import com.mvp.mobile_art.Model.Basic.User;
import com.mvp.mobile_art.Model.Basic.WalletTransaction;
import com.mvp.mobile_art.R;
import com.mvp.mobile_art.Route.Repositories.UserRepo;
import com.mvp.mobile_art.lib.api.APICallback;
import com.mvp.mobile_art.lib.api.APIManager;
import com.mvp.mobile_art.lib.database.SharedPref;
import com.mvp.mobile_art.lib.utils.ConstClass;
import com.mvp.mobile_art.lib.utils.GsonUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Zackzack on 18/07/2017.
 */

public class LogWalletActivity extends ParentActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager rec_LayoutManager;
    private RecyclerAdapterLogWallet rec_Adapter;
    private Toolbar toolbar;
    private User user;
    private List<WalletTransaction> walletTransactions = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout layoutnolist;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logwallet);
        user = GsonUtils.getObjectFromJson(SharedPref.getValueString(ConstClass.USER), User.class);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        layoutnolist = (LinearLayout) findViewById(R.id.layout_nolist);

        //toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Status Transaksi");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(R.color.toolbartitle));

        //recyclerview
        recyclerView = (RecyclerView) findViewById(R.id.recycleview_logwallet);
        rec_LayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(rec_LayoutManager);
        rec_Adapter = new RecyclerAdapterLogWallet();
        recyclerView.setAdapter(rec_Adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reload();
            }
        });
        reload();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void reload(){
        Call<List<WalletTransaction>> caller = APIManager.getRepository(UserRepo.class).getwallettrans(user.getId());
        caller.enqueue(new APICallback<List<WalletTransaction>>() {
            @Override
            public void onSuccess(Call<List<WalletTransaction>> call, Response<List<WalletTransaction>> response) {
                super.onSuccess(call, response);
                swipeRefreshLayout.setRefreshing(false);
                walletTransactions = response.body();
                if (walletTransactions.size() < 1){
                    hidelist();
                }else {
                    showlist();
                    rec_Adapter.setLogWallets(walletTransactions);
                }
            }

            @Override
            public void onFailure(Call<List<WalletTransaction>> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }
    public void hidelist(){
        recyclerView.setVisibility(View.GONE);
        layoutnolist.setVisibility(View.VISIBLE);
    }
    public void showlist(){
        recyclerView.setVisibility(View.VISIBLE);
        layoutnolist.setVisibility(View.GONE);
    }
}
