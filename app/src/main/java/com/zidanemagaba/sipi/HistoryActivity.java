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

public class HistoryActivity extends AppCompatActivity {

    String [] aksi, waktu;
    int jumlah;
    private ProgressDialog mProgressDialog;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading ...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        getData();
    }

    private void getData(){
        SharedPreferences share = getSharedPreferences("user", MODE_PRIVATE);
        String id = share.getString("id","");
        final StringRequest request = new StringRequest(Request.Method.GET, Server.URL_HISTORY+""+id,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            iniData(response);
                            mProgressDialog.dismiss();
                        }catch (Exception e){
                            Toast.makeText(HistoryActivity.this, "Eror "+e,Toast.LENGTH_LONG).show();
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
                aksi = new String [jumlah];
                waktu = new String [jumlah];
                //looping utk array
                for(int i=0; i<jsonArray.length(); i++){
                    //get json berdasarkan banyaknya data (index i)
                    JSONObject data = jsonArray.getJSONObject(i);

                    aksi[i]= data.getString("aksi");
                    waktu[i]= data.getString("waktu");
                }
                tampilData();
            }else{
                builder = new AlertDialog.Builder(HistoryActivity.this);
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

        listView.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, aksi));
        listView.setSelected(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView arg0, View arg1, final int arg2, long arg3) {

                final String tampil =
                                ""+aksi[arg2];

                builder = new AlertDialog.Builder(HistoryActivity.this);
                builder.setTitle(""+waktu[arg2]);
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
        ((ArrayAdapter)listView .getAdapter()).notifyDataSetInvalidated();

    }
}
