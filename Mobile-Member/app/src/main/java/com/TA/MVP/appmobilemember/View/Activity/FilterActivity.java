package com.TA.MVP.appmobilemember.View.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.TA.MVP.appmobilemember.MasterCleanApplication;
import com.TA.MVP.appmobilemember.Model.Adapter.RecyclerAdapterBahasa;
import com.TA.MVP.appmobilemember.Model.Adapter.SpinnerAdapter;
import com.TA.MVP.appmobilemember.Model.Array.FilterArrays;
import com.TA.MVP.appmobilemember.Model.Basic.Job;
import com.TA.MVP.appmobilemember.Model.Basic.Language;
import com.TA.MVP.appmobilemember.Model.Basic.Place;
import com.TA.MVP.appmobilemember.Model.Basic.Waktu_Kerja;
import com.TA.MVP.appmobilemember.R;
import com.TA.MVP.appmobilemember.lib.database.SharedPref;
import com.TA.MVP.appmobilemember.lib.utils.GsonUtils;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zackzack on 10/06/2017.
 */

public class FilterActivity extends ParentActivity{
    private Toolbar toolbar;
    private RecyclerView.LayoutManager rec_LayoutManager;
    private RecyclerAdapterBahasa rec_Adapter;

    private RecyclerView recviewbahasa;
    private EditText nama, gaji, usiamin, usiamax, pk, suku;
    private Spinner spinnerkota, spinneragama, spinnergender, spinnerprofesi, spinnerwaktukrj;
    private Button btncari, btnbatal, btnuminup, btnumindown, btnumaxup, btnumaxdown;
    private TextView textgaji;
    private SpinnerAdapter spinnerAdapterGender, spinnerAdapteragama;
    private FilterArrays filterArrays;
    private Integer tmp;
    private ArrayAdapter arrayAdapterJob, arrayAdapterWaktu, arrayAdapterKota;
    private List<Language> defaultlistbahasa = new ArrayList<>();
    private NumberFormat numberFormat = NumberFormat.getNumberInstance();
    private TextWatcher textWatcher = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        initAllView();
        setbtnlistener(btnuminup, usiamin, btnumindown, 20, 70);
        setbtnlistener(btnumaxup, usiamax, btnumaxdown, 20, 70);

        btncari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPref.save("searching", "yes");
                Intent i = new Intent();
                i.putExtra("nama", nama.getText().toString());
                if (spinnerkota.getSelectedItemPosition() != 0)
                    i.putExtra("kota", String.valueOf(spinnerkota.getSelectedItemPosition()));
                if (spinneragama.getSelectedItemPosition() != 0)
                    i.putExtra("agama", String.valueOf(spinneragama.getSelectedItemPosition()));
                if (spinnergender.getSelectedItemPosition() != 0)
                    i.putExtra("gender", String.valueOf(spinnergender.getSelectedItemPosition()));
                i.putExtra("suku", suku.getText().toString());
                if (spinnerprofesi.getSelectedItemPosition() != 0)
                    i.putExtra("profesi", String.valueOf(spinnerprofesi.getSelectedItemPosition()));
                if (spinnerwaktukrj.getSelectedItemPosition() != 0)
                    i.putExtra("WT", String.valueOf(spinnerwaktukrj.getSelectedItemPosition()));
                i.putExtra("gaji", Integer.valueOf(gaji.getText().toString().replace("Rp. ","").replace(",","")));
                if (Integer.valueOf(usiamin.getText().toString()) < 20)
                    i.putExtra("usiamin", "20");
                else if (Integer.valueOf(usiamin.getText().toString()) > 70)
                    i.putExtra("usiamin", "70");
                else
                    i.putExtra("usiamin", usiamin.getText().toString());
                if (Integer.valueOf(usiamax.getText().toString()) < 20)
                    i.putExtra("usiamax", "20");
                else if (Integer.valueOf(usiamax.getText().toString()) > 70)
                    i.putExtra("usiamax", "70");
                else
                    i.putExtra("usiamax", usiamax.getText().toString());
                i.putExtra("listbahasa", GsonUtils.getJsonFromObject(rec_Adapter.getselectedlist()));
                setResult(Activity.RESULT_OK, i);
                finish();
            }
        });
        btnbatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnintent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnintent);
                finish();
            }
        });

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                gaji.removeTextChangedListener(textWatcher);
                Integer total = getinttotal(gaji.getText().toString());
                if (total == null)
                    gaji.setText(setRP(0));
                else gaji.setText(setRP(total));
                gaji.setSelection(gaji.getText().length());
                gaji.addTextChangedListener(textWatcher);
            }
        };
        gaji.setText(setRP(0));
        gaji.addTextChangedListener(textWatcher);

        //set leave focus from parent function
        setupleavefocus(findViewById(R.id.filter_inner_layout), this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                hideSoftKeyboard(this);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initAllView(){
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        textgaji = (TextView) findViewById(R.id.filter_tv_gaji);
        nama = (EditText) findViewById(R.id.filter_et_nama);
        gaji = (EditText) findViewById(R.id.filter_et_gaji);
        usiamin = (EditText) findViewById(R.id.filter_et_umin);
        usiamax = (EditText) findViewById(R.id.filter_et_umax);
//        pk = (EditText) findViewById(R.id.filter_et_pk);
        suku = (EditText) findViewById(R.id.filter_et_suku);
        spinnerkota = (Spinner) findViewById(R.id.filter_spinner_kota);
        spinneragama = (Spinner) findViewById(R.id.filter_spinner_agama);
        spinnergender = (Spinner) findViewById(R.id.filter_spinner_gender);
        spinnerprofesi = (Spinner) findViewById(R.id.filter_spinner_profesi);
        spinnerwaktukrj = (Spinner) findViewById(R.id.filter_spinner_waktukrj);
        recviewbahasa = (RecyclerView) findViewById(R.id.filter_recview_bahasa);
        btncari = (Button) findViewById(R.id.filter_btn_cari);
        btnbatal = (Button) findViewById(R.id.filter_btn_batal);
        btnuminup = (Button) findViewById(R.id.filter_btn_uminup);
        btnumindown = (Button) findViewById(R.id.filter_btn_umindown);
        btnumaxup = (Button) findViewById(R.id.filter_btn_umaxup);
        btnumaxdown = (Button) findViewById(R.id.filter_btn_umaxdown);

        setAll();
    }

    private void setAll(){
        defaultlistbahasa = ((MasterCleanApplication)getApplication()).getGlobalStaticData().getLanguages();
        //toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.toolbar_filter);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(R.color.toolbartitle));

        //recviewbahasa
        rec_LayoutManager = new LinearLayoutManager(getApplicationContext());
        recviewbahasa.setLayoutManager(rec_LayoutManager);
        rec_Adapter = new RecyclerAdapterBahasa();
        recviewbahasa.setAdapter(rec_Adapter);
        rec_Adapter.setlistbahasa(defaultlistbahasa);

        //Spinner
        filterArrays = new FilterArrays();

        arrayAdapterJob = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, addalljob(((MasterCleanApplication) getApplication()).getGlobalStaticData().getJobs()));
        spinnerprofesi.setAdapter(arrayAdapterJob);

        arrayAdapterWaktu = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, addallwkt(((MasterCleanApplication) getApplication()).getGlobalStaticData().getWaktu_kerjas()));
        spinnerwaktukrj.setAdapter(arrayAdapterWaktu);
        spinnerwaktukrj.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0){
                    gaji.setText("0");
                    gaji.setEnabled(false);
                }
                else gaji.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        arrayAdapterKota = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, addallkota(((MasterCleanApplication) getApplication()).getGlobalStaticData().getPlaces()));
        spinnerkota.setAdapter(arrayAdapterKota);


        spinnerAdapteragama = new SpinnerAdapter(this, filterArrays.getArrayAgama().getArrayList2());
        spinneragama.setAdapter(spinnerAdapteragama.getArrayAdapter());
        spinnerAdapterGender = new SpinnerAdapter(this, filterArrays.getArrayGender().getArrayList2());
        spinnergender.setAdapter(spinnerAdapterGender.getArrayAdapter());

        gaji.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b){
                    String temp = gaji.getText().toString();
                    if (temp.matches(""))
                        gaji.setText("0");//gak bisa set after leave focus
                }
            }
        });
        gaji.setText("0");
        usiamin.setText(String.valueOf(20));
        usiamax.setText(String.valueOf(70));
//        pk.setText(String.valueOf(1));
    }
    private void setbtnlistener(Button btnup, final EditText editText, Button btndown, final Integer min2, final Integer max2){
        btnup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tmp = Integer.valueOf(editText.getText().toString());
                if (tmp < max2)
                    editText.setText(String.valueOf(tmp + 1));
            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    tmp = Integer.valueOf(editText.getText().toString());
                    if (tmp > max2)
                        editText.setText(String.valueOf(max2));
                    else if (tmp < min2)
                        editText.setText(String.valueOf(min2));
                }
            }
        });
        btndown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tmp = Integer.valueOf(editText.getText().toString());
                if (tmp > min2)
                    editText.setText(String.valueOf(tmp - 1));
            }
        });
    }

    public List<Job> addalljob(List<Job> jobs){
        List<Job> tmp = new ArrayList<>();
        Job alljob = new Job();
        alljob.setJob("Semua");
        tmp.add(alljob);
        for (int n=0;n<jobs.size();n++){
            tmp.add(jobs.get(n));
        }
        return tmp;

    }

    public List<Waktu_Kerja> addallwkt(List<Waktu_Kerja> wk){
        List<Waktu_Kerja> tmp = new ArrayList<>();
        Waktu_Kerja alljob = new Waktu_Kerja();
        alljob.setWork_time("Semua");
        tmp.add(alljob);
        for (int n=0;n<wk.size();n++){
            tmp.add(wk.get(n));
        }
        return tmp;

    }

    public List<Place> addallkota(List<Place> places){
        List<Place> tmp = new ArrayList<>();
        Place alljob = new Place();
        alljob.setName("Semua");
        tmp.add(alljob);
        for (int n=0;n<places.size();n++){
            tmp.add(places.get(n));
        }
        return tmp;
    }
    public String setRP(Integer number){
        String tempp = "Rp. ";
        tempp = tempp + numberFormat.format(number);
        return tempp;
    }
    public Integer getinttotal(String string){
        string = string.replace("R", "");
        string = string.replace("p", "");
        string = string.replace(".", "");
        string = string.replace(" ", "");
        string = string.replace(",", "");
        Integer result = 0;
        try{
            result = Integer.valueOf(string);
        }catch (NumberFormatException e){
            return 0;
        }
        return result;
    }

}
