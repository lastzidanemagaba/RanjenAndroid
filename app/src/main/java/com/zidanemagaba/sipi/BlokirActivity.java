package com.zidanemagaba.sipi;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmadrofiul.sipi.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BlokirActivity extends AppCompatActivity {

    private String nfc, blokir;
    private String result, pemilik, no_sipi, nm_kapal, gross, tgl_awal, tgl_akhir, jml_abk, status;
    private ProgressDialog mProgressDialog;
    private AlertDialog.Builder builder;
    private Button button;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blokir);

        nfc = getIntent().getStringExtra("nfc");

        mProgressDialog = new ProgressDialog(this);
        builder = new AlertDialog.Builder(this);

        mProgressDialog.setMessage("Loading ...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        getData();


        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder.setTitle("Perhatian");
                builder.setMessage("Apakah ingin Memblokir Kartu?");
                builder.setCancelable(false);
                builder.setNegativeButton("Kembali", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int j) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton("Blokir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int j) {

                        EditText b = findViewById(R.id.txt_blokir);
                        blokir = b.getText().toString();

                        if(!(blokir.equals(""))){
                            StringRequest stringrequest = new StringRequest(Request.Method.POST, Server.SEND_BLOKIR,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {

                                            AlertDialog.Builder send = new AlertDialog.Builder(BlokirActivity.this);
                                            send.setTitle("Perhatian");
                                            send.setMessage("" + response);
                                            send.setCancelable(false);
                                            send.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    finish();
                                                }
                                            });
                                            AlertDialog alertDialog = send.create();
                                            alertDialog.show();
                                        }
                                    }
                                    , new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(BlokirActivity.this, "EROR :" + error, Toast.LENGTH_SHORT).show();
                                    error.printStackTrace();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    SharedPreferences share = getSharedPreferences("user", MODE_PRIVATE);
                                    String id = share.getString("id", "");

                                    Map<String, String> map = new HashMap<String, String>();
                                    map.put("id_kapal", result);
                                    map.put("no_sipi", no_sipi);
                                    map.put("id_admin", id);
                                    map.put("blokir", blokir);


                                    return map;
                                }
                            };
                            AppController.getInstance(BlokirActivity.this).addTorequestque(stringrequest);
                        }else{
                            Toast.makeText(BlokirActivity.this, "Form tidak boleh kosong", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });
        
    }


    private void getData(){
        final StringRequest request = new StringRequest(Request.Method.GET, Server.URL_SIPI+""+"&nfc="+nfc,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            iniData(response);
                            mProgressDialog.dismiss();
                        }catch (Exception e){
                            Toast.makeText(BlokirActivity.this, "Eror "+e, Toast.LENGTH_LONG).show();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void iniData(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);

            // ini utk mengambil attribute array yg ada di json (yaitu attribute data)
            result = jsonObject.getString("result");

            pemilik = jsonObject.getString("pemilik");
            no_sipi = jsonObject.getString("no_sipi");
            nm_kapal = jsonObject.getString("nm_kapal");
            gross = jsonObject.getString("gross");
            tgl_awal = jsonObject.getString("tgl_awal");
            tgl_akhir = jsonObject.getString("tgl_akhir");
            jml_abk = jsonObject.getString("jml_abk");
            status = jsonObject.getString("status");

            if(!(result.equals("null"))){

                TextView t_pemilik = findViewById(R.id.pemilik);
                TextView t_no_sipi = findViewById(R.id.no_sipi);
                TextView t_nm_kapal = findViewById(R.id.nm_kapal);
                TextView t_gross = findViewById(R.id.gross);
                TextView t_tgl_awal = findViewById(R.id.tgl_awal);
                TextView t_tgl_akhir = findViewById(R.id.tgl_akhir);
                TextView t_jml_abk = findViewById(R.id.jml_abk);
                TextView t_status = findViewById(R.id.status);

                t_pemilik.setText(pemilik);
                t_no_sipi.setText(no_sipi);
                t_nm_kapal.setText(nm_kapal);
                t_gross.setText(gross);
                t_tgl_awal.setText(tgl_awal);
                t_tgl_akhir.setText(tgl_akhir);
                t_jml_abk.setText(jml_abk);
                if(status.equals("1")){
                    t_status.setText("SIPI Aktif");
                }else if(status.equals("2")){
                    t_status.setText("Kartu SIPI Terblokir");
                }else if(status.equals("4")){
                    t_status.setText("Kapal sedang berlayar");
                }else{
                    t_status.setText("SIPI Tidak Aktif");
                }


            }else{
                Toast.makeText(BlokirActivity.this,"Data tidak tersedia",Toast.LENGTH_SHORT).show();
                finish();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
