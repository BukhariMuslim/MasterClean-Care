package com.mvp.mobile_art.View.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mvp.mobile_art.MasterCleanApplication;
import com.mvp.mobile_art.Model.Adapter.RecyclerAdapterPenawaranHistory;
import com.mvp.mobile_art.Model.Adapter.RecyclerAdapterPermintaan;
import com.mvp.mobile_art.Model.Basic.Offer;
import com.mvp.mobile_art.Model.Basic.User;
import com.mvp.mobile_art.R;
import com.mvp.mobile_art.Route.Repositories.OfferRepo;
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
 * Created by jcla123ns on 10/08/17.
 */

public class FragmentPenawaranHistory extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager rec_LayoutManager;
    private RecyclerAdapterPenawaranHistory rec_Adapter;
    private List<Offer> offers = new ArrayList<>();
    private User user = new User();
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout layoutnolist, layoutloading, layoutnoconnection;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View _view = inflater.inflate(R.layout.fragment_penawaran_history, container, false);
        user = GsonUtils.getObjectFromJson(SharedPref.getValueString(ConstClass.USER), User.class);

        layoutnolist = (LinearLayout) _view.findViewById(R.id.layout_nolist);
        layoutloading = (LinearLayout) _view.findViewById(R.id.layout_loading);
        layoutnoconnection = (LinearLayout) _view.findViewById(R.id.layout_noconnection);
        swipeRefreshLayout = (SwipeRefreshLayout) _view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = (RecyclerView) _view.findViewById(R.id.recycleview_penawaran);
        rec_LayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(rec_LayoutManager);
        rec_Adapter = new RecyclerAdapterPenawaranHistory();
        recyclerView.setAdapter(rec_Adapter);
        rec_Adapter.setDefaultwk(((MasterCleanApplication)getActivity().getApplication()).globalStaticData.getWaktu_kerjas());
        rec_Adapter.setcontext(getContext());

        showloading();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadoffers();
            }
        });
        loadoffers();

        return _view;
    }
    public void loadoffers(){
        Call<List<Offer>> caller = APIManager.getRepository(OfferRepo.class).getoffersbyart(user.getId());
        caller.enqueue(new APICallback<List<Offer>>() {
            @Override
            public void onSuccess(Call<List<Offer>> call, Response<List<Offer>> response) {
                super.onSuccess(call, response);
                hidenoconnection();
                offers = filter(response.body());
                if (offers.size() < 1)
                    hidelist();
                else {
                    showlist();
                    rec_Adapter.setOffers(offers, user.getId());
                }
                swipeRefreshLayout.setRefreshing(false);
                hideloading();
            }

            @Override
            public void onError(Call<List<Offer>> call, Response<List<Offer>> response) {
                super.onError(call, response);
                hideloading();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Offer>> call, Throwable t) {
                super.onFailure(call, t);
                hideloading();
                shownoconnection();
                swipeRefreshLayout.setRefreshing(false);
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
    public void showloading(){
        layoutloading.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }
    public void hideloading(){
        layoutloading.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }
    public void shownoconnection(){
        layoutnoconnection.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }
    public void hidenoconnection(){
        layoutnoconnection.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }
    public List<Offer> filter(List<Offer> offers){
        List<Offer> temp = new ArrayList<>();
        for (int n=0;n<offers.size();n++){
            if (offers.get(n).getStatus() != 0) {
                temp.add(offers.get(n));
            }
        }
        return temp;
    }
}
