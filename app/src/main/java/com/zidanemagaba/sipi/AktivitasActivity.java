package com.zidanemagaba.sipi;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ahmadrofiul.sipi.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AktivitasActivity extends AppCompatActivity {

    String [] id, pemilik, no_sipi, nm_kapal, tgl_berangkat, jml_abk;
    int jumlah;
    private ProgressDialog mProgressDialog;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aktivitas);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading ...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        getData();

    }


    private void getData(){
        SharedPreferences share = getSharedPreferences("user", MODE_PRIVATE);
        String id_kota = share.getString("id_kota","");
        final StringRequest request = new StringRequest(Request.Method.GET, Server.URL_AKTIVITAS+""+"&id_kota="+id_kota,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            iniData(response);
                            mProgressDialog.dismiss();
                        }catch (Exception e){
                            Toast.makeText(AktivitasActivity.this, "Eror "+e,Toast.LENGTH_LONG).show();
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


            JSONArray jsonArray = jsonObject.getJSONArray("result");
            jumlah = jsonArray.length();
            //Toast.makeText(StokActivity.this,""+jumlah,Toast.LENGTH_SHORT).show();

            if(jumlah>0){
                id = new String [jumlah];
                pemilik = new String [jumlah];
                no_sipi = new String [jumlah];
                nm_kapal = new String [jumlah];
                tgl_berangkat = new String [jumlah];
                jml_abk = new String [jumlah];

                //looping utk array
                for(int i=0; i<jsonArray.length(); i++){
                    //get json berdasarkan banyaknya data (index i)
                    JSONObject data = jsonArray.getJSONObject(i);

                    id[i]= data.getString("id");
                    pemilik[i]= data.getString("pemilik");
                    no_sipi[i]= data.getString("no_sipi");
                    nm_kapal[i]= data.getString("nm_kapal");
                    tgl_berangkat[i]= data.getString("tgl_berangkat");
                    jml_abk[i]= data.getString("jml_abk");
                    }
                tampilData();
            }else{
                builder = new AlertDialog.Builder(AktivitasActivity.this);
                builder.setTitle("Perhatian");
                builder.setMessage("Data tidak tersedia");
                builder.setCancelable(false);
                builder.setPositiveButton("Kembali", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dialog.dismiss();
                        finish();
                    }
                });

                builder.create().show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void tampilData(){

        ListView listView = findViewById(R.id.listView);

        listView.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, nm_kapal));
        listView.setSelected(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView arg0, View arg1, final int arg2, long arg3) {

                final String tampil =
                                "Penanggung Jawab :\n"+pemilik[arg2]+"\n\n"+
                                "Nomor Sipi :\n"+no_sipi[arg2]+"\n\n"+
                                "Berangkat :\n"+tgl_berangkat[arg2]+"\n\n"+
                                "Jml ABK :\n"+jml_abk[arg2];

                builder = new AlertDialog.Builder(AktivitasActivity.this);
                builder.setTitle(""+nm_kapal[arg2]);
                builder.setMessage(tampil);
                builder.setCancelable(true);
                builder.setPositiveButton("Kembali", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.create().show();


            }});
        ((ArrayAdapter)listView.getAdapter()).notifyDataSetInvalidated();

    }
}
