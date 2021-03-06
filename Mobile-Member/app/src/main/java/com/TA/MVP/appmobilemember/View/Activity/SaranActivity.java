package com.TA.MVP.appmobilemember.View.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.TA.MVP.appmobilemember.Model.Basic.User;
import com.TA.MVP.appmobilemember.Model.Responses.KdanSResponse;
import com.TA.MVP.appmobilemember.R;
import com.TA.MVP.appmobilemember.Route.Repositories.KdanSRepo;
import com.TA.MVP.appmobilemember.lib.api.APICallback;
import com.TA.MVP.appmobilemember.lib.api.APIManager;
import com.TA.MVP.appmobilemember.lib.database.SharedPref;
import com.TA.MVP.appmobilemember.lib.utils.ConstClass;
import com.TA.MVP.appmobilemember.lib.utils.GsonUtils;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by jcla123ns on 11/08/17.
 */

public class SaranActivity extends ParentActivity {
    private EditText content;
    private Button simpan;
    private User user = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saran);
        user = GsonUtils.getObjectFromJson(SharedPref.getValueString(ConstClass.USER),User.class);

        content = (EditText) findViewById(R.id.et_content);
        simpan = (Button) findViewById(R.id.btn_simpan);

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!content.getText().toString().equals("")){
                    abuildermessage("Kirim kritik dan saran?","Konfirmasi");
                    abuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            postsaran();
                        }
                    });
                    abuilder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    showalertdialog();
                }else Toast.makeText(getApplicationContext(),"Anda belum mengisi Kritik atau saran anda.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void postsaran(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("email", user.getEmail());
        map.put("content", content.getText().toString());
        Call<KdanSResponse> caller = APIManager.getRepository(KdanSRepo.class).postSaran(map);
        caller.enqueue(new APICallback<KdanSResponse>() {
            @Override
            public void onSuccess(Call<KdanSResponse> call, Response<KdanSResponse> response) {
                super.onSuccess(call, response);
                abuildermessage("Terima kasih sudah memberikan kritik dan saran anda.", "Pemberitahuan");
                abuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                showalertdialog();
            }

            @Override
            public void onError(Call<KdanSResponse> call, Response<KdanSResponse> response) {
                super.onError(call, response);
                Toast.makeText(getApplicationContext(),"Terjadi kesalahan.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<KdanSResponse> call, Throwable t) {
                super.onFailure(call, t);
                Toast.makeText(getApplicationContext(),"Koneksi bermasalah.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
