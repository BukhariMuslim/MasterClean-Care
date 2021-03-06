package com.TA.MVP.appmobilemember.View.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.TA.MVP.appmobilemember.MasterCleanApplication;
import com.TA.MVP.appmobilemember.Model.Basic.Place;
import com.TA.MVP.appmobilemember.Model.Basic.User;
import com.TA.MVP.appmobilemember.Model.Basic.UserContact;
import com.TA.MVP.appmobilemember.R;
import com.TA.MVP.appmobilemember.RoundedTransformation;
import com.TA.MVP.appmobilemember.Route.Repositories.UserRepo;
import com.TA.MVP.appmobilemember.lib.api.APICallback;
import com.TA.MVP.appmobilemember.lib.api.APIManager;
import com.TA.MVP.appmobilemember.lib.database.SharedPref;
import com.TA.MVP.appmobilemember.lib.utils.ConstClass;
import com.TA.MVP.appmobilemember.lib.utils.GsonUtils;
import com.TA.MVP.appmobilemember.lib.utils.Settings;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Zackzack on 09/06/2017.
 */

public class ProfileActivity extends ParentActivity {
    public final static int REQUEST_EDIT = 1;
    public final static int REQUEST_EDITPASS = 2;
    public final static int REQUEST_EDITFOTO = 3;
    public final static int REQUEST_LOG = 4;
    public final static int RESULT_SUCCESS = 1;
    public final static int RESULT_CANCEL = 2;
    private User user = new User();
    private UserContact userContact = new UserContact();
    private Toolbar toolbar;
    private ImageView imgfoto;
    private TextView nama, alamat, notelp, notelp2, email, nominal, kota;
    private Button btnlog;
    private Intent intent = new Intent();
    private NumberFormat numberFormat = NumberFormat.getNumberInstance();
    private List<Place> places;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        user = GsonUtils.getObjectFromJson(SharedPref.getValueString(ConstClass.USER), User.class);
        places = ((MasterCleanApplication)getApplication()).getGlobalStaticData().getPlaces();
        getallinfo(user.getId());

        imgfoto = (ImageView) findViewById(R.id.prof_iv_foto);
        nama = (TextView) findViewById(R.id.prof_tv_nama);
        alamat = (TextView) findViewById(R.id.prof_tv_alamat);
        notelp = (TextView) findViewById(R.id.prof_tv_notelp);
        notelp2 = (TextView) findViewById(R.id.prof_tv_notelp2);
        email = (TextView) findViewById(R.id.prof_tv_email);
        nominal = (TextView) findViewById(R.id.prof_tv_nominal);
        kota = (TextView) findViewById(R.id.prof_tv_kota);
        btnlog =(Button) findViewById(R.id.prof_btn_isi);



        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.toolbar_profile);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(R.color.toolbartitle));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.prof_menu_edit:
                intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                ProfileActivity.this.startActivityForResult(intent, REQUEST_EDIT);
                break;
            case R.id.prof_menu_editks:
                doStartActivity(ProfileActivity.this, EditPassActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setdata(){
        user = GsonUtils.getObjectFromJson(SharedPref.getValueString(ConstClass.USER), User.class);
        nama.setText(user.getName());
        kota.setText(places.get(user.getContact().getCity()-1).getName());
        try{
            alamat.setText(user.getContact().getAddress());
        }
        catch (NullPointerException e){
            alamat.setText("-");
        }
        try{
            notelp.setText(user.getContact().getPhone());
        }
        catch (NullPointerException e){
            notelp.setText("-");
        }
        try{
            notelp2.setText(user.getContact().getEmergency_numb());
        }
        catch (NullPointerException e){
            notelp2.setText("-");
        }
        email.setText(user.getEmail());
        try{
            nominal.setText(setRP(user.getUser_wallet().getAmt()));
        }
        catch (NullPointerException e){
            nominal.setText("Rp. 0");
        }

        btnlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LogWalletActivity.class);
                startActivityForResult(i, REQUEST_LOG);
//                startActivity(i);
            }
        });

        Picasso.with(getApplicationContext())
                .load(Settings.getRetrofitAPIUrl()+"image/small/"+user.getAvatar())
                .placeholder(R.drawable.default_profile)
                .error(R.drawable.default_profile)
                .fit().centerCrop()
                .transform(new RoundedTransformation(1000, 0))
                .into(imgfoto);

        dismissDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_EDIT:
                if (resultCode == RESULT_SUCCESS) {
                    getallinfo(user.getId());
                }
                break;
            case REQUEST_LOG:
                getallinfo(user.getId());
                break;
        }
    }
    public void getallinfo(Integer id){
        initProgressDialog("Memuat data...");
        showDialog();
        Call<User> caller = APIManager.getRepository(UserRepo.class).getuser(id.toString());
        caller.enqueue(new APICallback<User>() {
            @Override
            public void onSuccess(Call<User> call, Response<User> response) {
                super.onSuccess(call, response);
                user = response.body();
                SharedPref.save(ConstClass.USER, GsonUtils.getJsonFromObject(user));
                setdata();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                super.onFailure(call, t);
                dismissDialog();
                Toast.makeText(getApplicationContext(),"Koneksi bermasalah", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    public String setRP(Integer number){
        String tempp = "Rp. ";
        tempp = tempp + numberFormat.format(number);
        return tempp;
    }
}
