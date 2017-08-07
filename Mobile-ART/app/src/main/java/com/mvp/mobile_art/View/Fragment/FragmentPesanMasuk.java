package com.mvp.mobile_art.View.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mvp.mobile_art.Model.Adapter.RecyclerAdapterPesanMasuk;
import com.mvp.mobile_art.Model.Basic.MyMessage;
import com.mvp.mobile_art.Model.Basic.User;
import com.mvp.mobile_art.R;
import com.mvp.mobile_art.Route.Repositories.MessageRepo;
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
 * Created by Zackzack on 06/07/2017.
 */

public class FragmentPesanMasuk extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager rec_LayoutManager;
    private RecyclerAdapterPesanMasuk rec_Adapter;
    private List<MyMessage> myMessages = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View _view = inflater.inflate(R.layout.fragment_pesan_masuk, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) _view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = (RecyclerView) _view.findViewById(R.id.recycleview_pesan);
        rec_LayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(rec_LayoutManager);
        rec_Adapter = new RecyclerAdapterPesanMasuk();
        recyclerView.setAdapter(rec_Adapter);
        rec_Adapter.setPesan(myMessages);
        rec_Adapter.setcontext(getActivity());

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadmessage();
            }
        });
        loadmessage();

        return _view;
    }
    public void loadmessage(){
        String userid = String.valueOf(GsonUtils.getObjectFromJson(SharedPref.getValueString(ConstClass.USER), User.class).getId());
        Call<List<MyMessage>> caller = APIManager.getRepository(MessageRepo.class).getallmsgfromreciverid(userid);
        caller.enqueue(new APICallback<List<MyMessage>>() {
            @Override
            public void onSuccess(Call<List<MyMessage>> call, Response<List<MyMessage>> response) {
                super.onSuccess(call, response);
                rec_Adapter.setPesan(response.body());
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onUnauthorized(Call<List<MyMessage>> call, Response<List<MyMessage>> response) {
                super.onUnauthorized(call, response);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<MyMessage>> call, Throwable t) {
                super.onFailure(call, t);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
