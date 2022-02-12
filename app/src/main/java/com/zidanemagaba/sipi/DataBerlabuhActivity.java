package com.zidanemagaba.sipi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class DataBerlabuhActivity extends AppCompatActivity {

    private String nfc;
    private String result, pemilik, no_sipi, nm_kapal, jml_abk, tgl_berangkat;
    private String [] id_ikan, nm_ikan;
    private int jumlahnya;
    private ProgressDialog mProgressDialog;
    private AlertDialog.Builder builder;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_berlabuh);

        nfc = getIntent().getStringExtra("nfc");

        mProgressDialog = new ProgressDialog(this);
        builder = new AlertDialog.Builder(this);
        
        mProgressDialog.setMessage("Loading ...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        getData();
    }

    private void getData(){
        final StringRequest request = new StringRequest(Request.Method.GET, Server.URL_BERLAYAR+""+"&nfc="+nfc,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            iniData(response);
                            mProgressDialog.dismiss();
                        }catch (Exception e){
                            Toast.makeText(DataBerlabuhActivity.this, "Eror "+e, Toast.LENGTH_LONG).show();
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
            jml_abk = jsonObject.getString("jml_abk");
            tgl_berangkat = jsonObject.getString("tgl_berangkat");

            JSONArray jsonArray = jsonObject.getJSONArray("result_ikan");
            jumlahnya = jsonArray.length();

            id_ikan = new String [jumlahnya];
            nm_ikan = new String [jumlahnya];

            for(int i=0; i<jsonArray.length(); i++){
                //get json berdasarkan banyaknya data (index i)
                JSONObject data = jsonArray.getJSONObject(i);

                id_ikan[i]= data.getString("id_ikan");
                nm_ikan[i] = data.getString("nm_ikan");
            }

            if(!(result.equals("null"))){
                Intent i = new Intent(DataBerlabuhActivity.this, BerlabuhActivity.class);
                i.putExtra("result",result);
                i.putExtra("pemilik",pemilik);
                i.putExtra("no_sipi",no_sipi);
                i.putExtra("nm_kapal",nm_kapal);
                i.putExtra("jml_abk",jml_abk);
                i.putExtra("tgl_berangkat",tgl_berangkat);

                i.putExtra("jumlahnya",jumlahnya);
                i.putExtra("id_ikan",id_ikan);
                i.putExtra("nm_ikan",nm_ikan);
                startActivity(i); finish();

            }else{
                Toast.makeText(DataBerlabuhActivity.this,"Data tidak tersedia",Toast.LENGTH_SHORT).show();
                finish();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
