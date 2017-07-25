package com.TA.MVP.appmobilemember.View.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.TA.MVP.appmobilemember.Model.Basic.Order;
import com.TA.MVP.appmobilemember.Model.Basic.User;
import com.TA.MVP.appmobilemember.R;
import com.TA.MVP.appmobilemember.View.Fragment.FragmentPemesanan1;
import com.TA.MVP.appmobilemember.View.Fragment.FragmentPemesanan2;
import com.TA.MVP.appmobilemember.View.Fragment.FragmentPemesanan3;
import com.TA.MVP.appmobilemember.lib.database.SharedPref;
import com.TA.MVP.appmobilemember.lib.utils.ConstClass;
import com.TA.MVP.appmobilemember.lib.utils.GsonUtils;
import com.google.android.gms.location.places.Place;

/**
 * Created by jcla123ns on 25/07/17.
 */

public class PermintaanActivity extends ParentActivity{
    private Toolbar toolbar;
    private FragmentPemesanan1 fragp1;
    private FragmentPemesanan2 fragp2;
    private FragmentPemesanan3 fragp3;
    private Bundle b = new Bundle();
    private User art = new User();
    private Order order = new Order();
    private Place place;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemesanan);
        Intent intent = getIntent();
        art = GsonUtils.getObjectFromJson(intent.getStringExtra(ConstClass.ART_EXTRA), User.class);

        fragp1 = new FragmentPemesanan1();
        fragp2 = new FragmentPemesanan2();
        fragp3 = new FragmentPemesanan3();

        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.toolbar_pemesanan);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPref.save(ConstClass.ART_EXTRA, GsonUtils.getJsonFromObject(art));
        SharedPref.save(ConstClass.ORDER_EXTRA, GsonUtils.getJsonFromObject(order));
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_pemesanan, fragp1).commit();
    }
    public void setPlace(Place place){
        this.place = place;
    }

    public void doChangeFragment(Integer nmrfrag){
        switch (nmrfrag){
            case 1:
                getSupportFragmentManager().beginTransaction().replace(R.id.layout_pemesanan, fragp1).commit();
                break;
            case 2:
                getSupportFragmentManager().beginTransaction().replace(R.id.layout_pemesanan, fragp2).commit();
                break;
            case 3:
                getSupportFragmentManager().beginTransaction().replace(R.id.layout_pemesanan, fragp3).commit();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                SharedPref.save(ConstClass.ART_EXTRA, "");
                SharedPref.save(ConstClass.ORDER_EXTRA, "");
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}