package com.zidanemagaba.sipi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahmadrofiul.sipi.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerview = navigationView.getHeaderView(0);
        SharedPreferences share = getSharedPreferences("user", MODE_PRIVATE);
        String namaa = share.getString("nama","");
        String nipp = share.getString("nip","");
        TextView t_nama = headerview.findViewById(R.id.nama);
        t_nama.setText(namaa);
        TextView t_nip = headerview.findViewById(R.id.nip);
        t_nip.setText(nipp);
        navigationView.setNavigationItemSelectedListener(this);


        ImageView berlayar = findViewById(R.id.berlayar);
        berlayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Isi koding
                Intent i = new Intent(MainActivity.this, NFCActivity.class);    i.putExtra("kode","1");
                //Intent i = new Intent(MainActivity.this, BerlayarActivity.class);    i.putExtra("nfc","D4612E1E");

                startActivity(i);
            }
        });

        ImageView berlabuh = findViewById(R.id.berlabuh);
        berlabuh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Isi koding
                Intent i = new Intent(MainActivity.this, NFCActivity.class);    i.putExtra("kode","2");
                //Intent i = new Intent(MainActivity.this, DataBerlabuhActivity.class);   i.putExtra("nfc","D4EEE21E");
                startActivity(i);
            }
        });

        ImageView blokir = findViewById(R.id.blokir);
        blokir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Isi koding
                Intent i = new Intent(MainActivity.this, NFCActivity.class);    i.putExtra("kode","3");
                //Intent i = new Intent(MainActivity.this, DataBerlabuhActivity.class);   i.putExtra("nfc","D4EEE21E");
                startActivity(i);
            }
        });

        ImageView aktivitas = findViewById(R.id.aktivitas);
        aktivitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Isi koding
                Intent i = new Intent(MainActivity.this, AktivitasActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout){
            SharedPreferences share = getSharedPreferences("user", MODE_PRIVATE);
            SharedPreferences.Editor save = share.edit();
            save.putString("id", "");
            save.putString("nip", "");
            save.putString("nama", "");
            save.putString("no_hp", "");
            save.putString("foto", "");
            save.putString("id_kota", "");
            save.putBoolean("login", false);
            save.apply();

            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_history){
            Intent i = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(i);
        }

        /*if (id == R.id.nav_komplain) {

        } else if (id == R.id.nav_kebersihan) {

        } else if (id == R.id.nav_kerusakan) {

        } else if (id == R.id.nav_kehilangan) {

        } else if (id == R.id.nav_absensi) {

        } else if (id == R.id.nav_barang) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void displayMap() {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=-6.975587,110.413428");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }
}
