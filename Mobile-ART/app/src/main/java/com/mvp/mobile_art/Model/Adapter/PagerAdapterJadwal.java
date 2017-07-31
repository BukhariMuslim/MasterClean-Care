package com.mvp.mobile_art.Model.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mvp.mobile_art.R;
import com.mvp.mobile_art.View.Fragment.FragmentListOffer;
import com.mvp.mobile_art.View.Fragment.FragmentPesananDiterima;
import com.mvp.mobile_art.View.Fragment.FragmentPesananMasuk;

/**
 * Created by jcla123ns on 22/07/17.
 */

public class PagerAdapterJadwal extends FragmentPagerAdapter {
    private Context thiscontext;
    public PagerAdapterJadwal(FragmentManager fm, Context context){
        super(fm);
        thiscontext = context;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new FragmentListOffer();
            case 1:
                return new FragmentPesananMasuk();
            case 2:
                return new FragmentPesananDiterima();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return thiscontext.getResources().getString(R.string.pager_penawaran);
            case 1:
                return thiscontext.getResources().getString(R.string.pager_pesanan_masuk);
            case 2:
                return thiscontext.getResources().getString(R.string.pager_pesanan_diterima);
            default:
                return null;
        }
    }
}
