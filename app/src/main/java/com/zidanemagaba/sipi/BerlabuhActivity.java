package com.zidanemagaba.sipi;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmadrofiul.sipi.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BerlabuhActivity extends AppCompatActivity {

    private String nfc, hasil;
    private String result, pemilik, no_sipi, nm_kapal, jml_abk, tgl_berangkat;
    private String [] id_ikan, nm_ikan;
    private int jumlahnya;
    private List jumlah;
    private ProgressDialog mProgressDialog;
    private AlertDialog.Builder builder;
    private ListView listview;
    private TextView t_pemilik, t_no_sipi, t_nm_kapal, t_jml_abk, t_tgl_berangkat;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berlabuh);


        result = getIntent().getStringExtra("result");
        pemilik = getIntent().getStringExtra("pemilik");
        no_sipi = getIntent().getStringExtra("no_sipi");
        nm_kapal = getIntent().getStringExtra("nm_kapal");
        jml_abk = getIntent().getStringExtra("jml_abk");
        tgl_berangkat = getIntent().getStringExtra("tgl_berangkat");

        jumlahnya = getIntent().getIntExtra("jumlahnya",0);
        id_ikan = new String[jumlahnya];
        id_ikan = getIntent().getStringArrayExtra("id_ikan");
        nm_ikan = new String[jumlahnya];
        nm_ikan = getIntent().getStringArrayExtra("nm_ikan");

        mProgressDialog = new ProgressDialog(this);
        builder = new AlertDialog.Builder(this);

        jumlah = new ArrayList<Integer>();

        /// L I S T V I E W N Y A ////
        listview = findViewById(R.id.listView);
        listview.setItemsCanFocus(true);
        View header = getLayoutInflater().inflate(R.layout.activity_berlabuh_header,null);
        View footer = getLayoutInflater().inflate(R.layout.activity_berlabuh_footer,null);
        listview.addHeaderView(header);
        listview.addFooterView(footer);

        t_pemilik = header.findViewById(R.id.pemilik);
        t_no_sipi = header.findViewById(R.id.no_sipi);
        t_nm_kapal = header.findViewById(R.id.nm_kapal);
        t_jml_abk = header.findViewById(R.id.jml_abk);
        t_tgl_berangkat = header.findViewById(R.id.tgl_berangkat);

        t_pemilik.setText(pemilik);
        t_no_sipi.setText(no_sipi);
        t_nm_kapal.setText(nm_kapal);
        t_jml_abk.setText(jml_abk);
        t_tgl_berangkat.setText(tgl_berangkat);

        for(int i=0;i<jumlahnya;i++){
            jumlah.add(0);
        }

        CustomAdapter customAdapter = new CustomAdapter(this, nm_ikan,jumlah);
        listview.setAdapter(customAdapter);

        button = footer.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setTitle("Perhatian");
                builder.setMessage("Data hasil tangkapan sudah benar?");
                builder.setCancelable(false);
                builder.setNegativeButton("Kembali", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int j) {
                        dialogInterface.dismiss();
                    }
                });

                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int j) {


                        hasil = "";
                        for (int i = 0; i < CustomAdapter.jumlahIkan.size(); i++) {
                            int a = Integer.parseInt(CustomAdapter.jumlahIkan.get(i));
                            hasil = hasil+id_ikan[i]+";"+a+",";
                        }
                        //Toast.makeText(BerlabuhActivity.this, ""+hasil, Toast.LENGTH_SHORT).show();

                        StringRequest stringrequest = new StringRequest(Request.Method.POST, Server.SEND_BERLABUH,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        AlertDialog.Builder send = new AlertDialog.Builder(BerlabuhActivity.this);
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
                                Toast.makeText(BerlabuhActivity.this, "EROR :" + error, Toast.LENGTH_SHORT).show();
                                error.printStackTrace();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                SharedPreferences share = getSharedPreferences("user", MODE_PRIVATE);
                                String id = share.getString("id", "");
                                String id_kota = share.getString("id_kota", "");

                                Map<String, String> map = new HashMap<String, String>();
                                map.put("id_kapal", result);
                                map.put("no_sipi", no_sipi);
                                map.put("id_admin", id);
                                map.put("id_kota", id_kota);
                                map.put("hasil", hasil);

                                return map;
                            }
                        };
                        AppController.getInstance(BerlabuhActivity.this).addTorequestque(stringrequest);

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }




}
