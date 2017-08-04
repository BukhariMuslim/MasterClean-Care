package com.TA.MVP.appmobilemember.View.Fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.TA.MVP.appmobilemember.MasterCleanApplication;
import com.TA.MVP.appmobilemember.Model.Adapter.SpinnerAdapter;
import com.TA.MVP.appmobilemember.Model.Array.ArrayAgama;
import com.TA.MVP.appmobilemember.Model.Basic.OrderTime;
import com.TA.MVP.appmobilemember.Model.Basic.User;
import com.TA.MVP.appmobilemember.Model.Basic.UserContact;
import com.TA.MVP.appmobilemember.Model.Responses.LoginResponse;
import com.TA.MVP.appmobilemember.Model.Responses.UserResponse;
import com.TA.MVP.appmobilemember.R;
import com.TA.MVP.appmobilemember.Route.Repositories.UserRepo;
import com.TA.MVP.appmobilemember.View.Activity.AuthActivity;
import com.TA.MVP.appmobilemember.lib.api.APICallback;
import com.TA.MVP.appmobilemember.lib.api.APIManager;
import com.TA.MVP.appmobilemember.lib.database.SharedPref;
import com.TA.MVP.appmobilemember.lib.utils.ConstClass;
import com.TA.MVP.appmobilemember.lib.utils.GsonUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Zackzack on 08/06/2017.
 */

public class FragmentRegister extends Fragment {
    private EditText nama, email, katasandi, konfkatasandi, notelp, alamat, bplace, bdate, tgl;
    private Spinner spinnergender, spinnerkota, spinneragama;
    private Button btndaftar;
    private TextView tvlogin;
    private ArrayAdapter arrayAdaptergender, arrayAdapterkota;
    private SpinnerAdapter spinnerAdapteragama;
    private ArrayAgama arrayAgama=new ArrayAgama();
    private String[] genders = new String[]{"Pria","Wanita"};
    private DatePickerDialog datePickerDialog1;
    private OrderTime now = new OrderTime();
    private Calendar calendar = Calendar.getInstance();
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-d", Locale.ENGLISH);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View _view = inflater.inflate(R.layout.fragment_register, container, false);

        nama = (EditText) _view.findViewById(R.id.reg_et_nama);
        email = (EditText) _view.findViewById(R.id.reg_et_email);
        katasandi = (EditText) _view.findViewById(R.id.reg_et_katasandi);
        konfkatasandi = (EditText) _view.findViewById(R.id.reg_et_konfkatasandi);
        bplace = (EditText) _view.findViewById(R.id.reg_et_bornplace);
        tgl = (EditText) _view.findViewById(R.id.reg_et_tgl);
        spinnergender = (Spinner) _view.findViewById(R.id.reg_spinner_gender);
        spinneragama = (Spinner) _view.findViewById(R.id.reg_spinner_agama);
        notelp = (EditText) _view.findViewById(R.id.reg_et_notelp);
        spinnerkota = (Spinner) _view.findViewById(R.id.reg_spinner_kota);
        alamat = (EditText) _view.findViewById(R.id.reg_et_alamat);
        btndaftar = (Button) _view.findViewById(R.id.reg_btn_daftar);
        tvlogin = (TextView) _view.findViewById(R.id.reg_tv_login);

        setwaktusekarang();
        datePickerDialog1 = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                tgl.setText(i2 + " - " + i1 + " - " + i);
                calendar.set(Calendar.YEAR,i);
                calendar.set(Calendar.MONTH,i1);
                calendar.set(Calendar.DAY_OF_MONTH,i2);
            }
        }, now.year, now.month, now.day);
        tgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog1.show();
            }
        });

        arrayAdaptergender = new ArrayAdapter(getContext(), R.layout.spinner_item, genders);
        spinnergender.setAdapter(arrayAdaptergender);
        arrayAdapterkota = new ArrayAdapter(getContext(), R.layout.spinner_item, ((MasterCleanApplication) getActivity().getApplication()).getGlobalStaticData().getPlaces());
        spinnerkota.setAdapter(arrayAdapterkota);
        spinnerAdapteragama = new SpinnerAdapter(getContext(), arrayAgama.getArrayList());
        spinneragama.setAdapter(spinnerAdapteragama.getArrayAdapter());

        btndaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (valid()) {
                    try {
                        ((AuthActivity) getActivity()).showDialog("registering");
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("name", nama.getText().toString());
                        map.put("email", email.getText().toString());
                        map.put("password", katasandi.getText().toString());
                        map.put("gender", String.valueOf(spinnergender.getSelectedItemPosition() + 1));
                        map.put("born_place", bplace.getText().toString());
                        map.put("born_date", tgl.getText());
                        map.put("religion", String.valueOf(spinneragama.getSelectedItemPosition() + 1));
                        map.put("user_type", String.valueOf(1));//cek lg
                        map.put("status", String.valueOf(1));//register 0
                        UserContact userContact = new UserContact();
                        userContact.setAddress(alamat.getText().toString());
                        userContact.setCity((spinnerkota.getSelectedItemPosition() + 1));
                        userContact.setPhone(notelp.getText().toString());
                        userContact.setLocation("3.584949, 98.672400");//harusnya get location
                        map.put("contact", userContact);//cek lg
                        Call<UserResponse> caller = APIManager.getRepository(UserRepo.class).registeruser(map);
                        caller.enqueue(new APICallback<UserResponse>() {
                            @Override
                            public void onSuccess(Call<UserResponse> call, Response<UserResponse> response) {
                                super.onSuccess(call, response);
                                User user = response.body().getUser();
                                getToken(user, katasandi.getText().toString());
                            }

                            @Override
                            public void onUnprocessableEntity(Call<UserResponse> call, Response<UserResponse> response) {
                                super.onUnprocessableEntity(call, response);
                                ((AuthActivity) getActivity()).dismissDialog();
                            }

                            @Override
                            public void onError(Call<UserResponse> call, Response<UserResponse> response) {
                                super.onError(call, response);
                                ((AuthActivity) getActivity()).dismissDialog();
                            }

                            @Override
                            public void onFailure(Call<UserResponse> call, Throwable t) {
                                super.onFailure(call, t);
                                ((AuthActivity) getActivity()).dismissDialog();
                            }
                        });
                    }
                    catch (NullPointerException e){
                        Toast.makeText(getContext(), "Data tidak lengkap.", Toast.LENGTH_SHORT);
                    }
                }

            }
        });

        tvlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AuthActivity)getActivity()).doChangeFragment(new FragmentLogin());
            }
        });

        return _view;
    }
    public void getToken(final User user, String pass){
        ((AuthActivity) getActivity()).showDialog("Logging in");
        HashMap<String, Object> map = new HashMap<>();
        map.put("email", user.getEmail());
        map.put("password", pass);
        Call<LoginResponse> caller = APIManager.getRepository(UserRepo.class).loginmember(map);
        caller.enqueue(new APICallback<LoginResponse>() {
            @Override
            public void onSuccess(Call<LoginResponse> call, Response<LoginResponse> response) {
                super.onSuccess(call, response);
                SharedPref.save(ConstClass.USER, GsonUtils.getJsonFromObject(response.body().getUser()));
                SharedPref.save(SharedPref.ACCESS_TOKEN, response.body().getToken().getAccess_token());
                ((AuthActivity) getActivity()).dismissDialog();
                Intent i = new Intent();
                i.putExtra(ConstClass.USER, GsonUtils.getJsonFromObject(response.body().getUser()));
                ((AuthActivity)getActivity()).dofinishActivity(i);
            }

            @Override
            public void onUnauthorized(Call<LoginResponse> call, Response<LoginResponse> response) {
                super.onUnauthorized(call, response);
                ((AuthActivity) getActivity()).dismissDialog();
                Toast.makeText(getContext(), "Email or Password is wrong", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Call<LoginResponse> call, Response<LoginResponse> response) {
                super.onError(call, response);
                ((AuthActivity) getActivity()).dismissDialog();
                Toast.makeText(getContext(), "Terjadi kesalahan pada server", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                super.onFailure(call, t);
                ((AuthActivity) getActivity()).dismissDialog();
                Toast.makeText(getContext(), "Koneksi bermasalah silahkan coba lagi", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void setwaktusekarang(){
        calendar = Calendar.getInstance();
        now.year = calendar.get(Calendar.YEAR);
        now.month = calendar.get(Calendar.MONTH);
        now.day = calendar.get(Calendar.DAY_OF_MONTH);
        now.hour = calendar.get(Calendar.HOUR);
        now.minute = calendar.get(Calendar.MINUTE);
    }
    public boolean valid(){
        if (katasandi.getText().toString() != konfkatasandi.getText().toString()){
            Toast.makeText(getContext(), "Konfirmasi katasandi tidak sesuai", Toast.LENGTH_SHORT);
            return false;
        }
        return true;
    }
}
