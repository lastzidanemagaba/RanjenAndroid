package com.zidanemagaba.sipi;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.ahmadrofiul.sipi.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private Button btn_login;
    private EditText txtusername, txtpassword;
    CheckBox ShowPass;
    private String result, username, password, nip, nama, no_hp, foto, id_kota ;
    private ProgressDialog mProgressDialog;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mProgressDialog = new ProgressDialog(this);
        builder = new AlertDialog.Builder(this);

        txtusername = findViewById(R.id.txt_username);
        txtpassword = findViewById(R.id.txt_password);
        ShowPass = findViewById(R.id.showPass);

        //Set onClickListener, untuk menangani kejadian saat Checkbox diklik
        ShowPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ShowPass.isChecked()){
                    //Saat Checkbox dalam keadaan Checked, maka password akan di tampilkan
                    txtpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    //Jika tidak, maka password akan di sembuyikan
                    txtpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mProgressDialog.setMessage("Loading ...");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();
                getData();
            }
        });
    }


    private void getData(){
        username = txtusername.getText().toString();
        password = txtpassword.getText().toString();
        //Toast.makeText(Login2Activity.this,Utils.URL_LOGIN+username+"&pass="+password,Toast.LENGTH_SHORT).show();

        final StringRequest request = new StringRequest(Request.Method.GET, Server.URL_LOGIN+username+"&pass="+password,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            iniData(response);
                            mProgressDialog.dismiss();
                        }catch (Exception e){
                            Toast.makeText(LoginActivity.this, "Eror "+e, Toast.LENGTH_LONG).show();
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

            nip = jsonObject.getString("nip");
            nama = jsonObject.getString("nama");
            no_hp = jsonObject.getString("no_hp");
            foto = jsonObject.getString("foto");
            id_kota  = jsonObject.getString("id_kota");

            //Toast.makeText(Login2Activity.this,""+result,Toast.LENGTH_SHORT).show();

            if(result.equals("null")){
                builder.setTitle("Login Gagal");
                builder.setMessage("Username atau Password salah");
                builder.setCancelable(false);
                builder.setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int j) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }else if(result.equals("superadmin")||result.equals("admin")){
                builder.setTitle("Login Gagal");
                builder.setMessage("Anda bukan petugas checker");
                builder.setCancelable(false);
                builder.setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int j) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }else{
                //mengubah nilai login nya jadi true
                SharedPreferences share = getSharedPreferences("user", MODE_PRIVATE);
                SharedPreferences.Editor save = share.edit();
                save.putString("id", result);
                save.putString("nip", nip);
                save.putString("nama", nama);
                save.putString("no_hp", no_hp);
                save.putString("foto", foto);
                save.putString("id_kota", id_kota);
                save.putBoolean("login", true);
                save.apply();

                builder.setTitle("SELAMAT DATANG");
                builder.setMessage(""+nama);
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int j) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
