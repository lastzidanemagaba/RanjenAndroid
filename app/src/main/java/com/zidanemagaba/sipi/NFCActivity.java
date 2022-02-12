package com.zidanemagaba.sipi;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmadrofiul.sipi.R;


public class NFCActivity extends Activity {

    private NfcAdapter adapter = null;
    private PendingIntent pendingIntent = null;

    private TextView currentTagView;
    private ExpandableListView expandableListView;

    private int kebutuhan; //1.Berlayar  2.Berlabuh

    @Override
    public void onCreate(final Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_nfc);
        kebutuhan = Integer.parseInt(getIntent().getStringExtra("kode"));

        currentTagView = (TextView) findViewById(R.id.currentTagView);
        currentTagView.setText("Loading...");
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        /*expandableListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final float swipeThreshold = 15;
                return false;
            }
        });*/

        adapter = NfcAdapter.getDefaultAdapter(this);
    }

    @Override
    public void onResume() {
        try{
            super.onResume();
            if (!adapter.isEnabled()) {
                Utils.showNfcSettingsDialog(this);
                return;
            }

            if (pendingIntent == null) {
                pendingIntent = PendingIntent.getActivity(this, 0,
                        new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
                if(kebutuhan==1){
                    currentTagView.setText("Tempelkan kartu ke HP untuk cek kartu nelayan");
                }else if(kebutuhan==2){
                    currentTagView.setText("Tempelkan kartu ke HP untuk berlabuh");
                }else{
                    currentTagView.setText("Tempelkan kartu ke HP untuk memblokir kartu");
                }

            }
            adapter.enableForegroundDispatch(this, pendingIntent, null, null);


        }catch (Exception e){
            Toast.makeText(NFCActivity.this,"NFC is Trouble",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onPause() {
        try{
            super.onPause();
            adapter.disableForegroundDispatch(this);
        }catch (Exception e){
            Toast.makeText(NFCActivity.this,"NFC is Trouble",Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    @Override
    public void onNewIntent(Intent intent) {
        Log.d("onNewIntent", "Discovered tag with intent " + intent);

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String tagId = Utils.bytesToHex(tag.getId());

        //currentTagView.setText("Kode NFCnya = " + tagId+"\nDi scan = "+ Utils.now());

        if(kebutuhan==1){
            Intent i = new Intent(NFCActivity.this, BerlayarActivity.class);
            i.putExtra("nfc",tagId);
            startActivity(i); finish();
        }else if(kebutuhan==2){
            Intent i = new Intent(NFCActivity.this, DataBerlabuhActivity.class);
            i.putExtra("nfc",tagId);
            startActivity(i); finish();
        }else{
            Intent i = new Intent(NFCActivity.this, BlokirActivity.class);
            i.putExtra("nfc",tagId);
            startActivity(i); finish();
        }


    }


}
