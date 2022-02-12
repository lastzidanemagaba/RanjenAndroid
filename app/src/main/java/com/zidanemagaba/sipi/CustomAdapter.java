package com.zidanemagaba.sipi;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.ahmadrofiul.sipi.R;

import java.util.ArrayList;
import java.util.List;


public class CustomAdapter extends BaseAdapter {
    private Context context;
    private String [] nm_ikan;
    private List list;
    private TextView textView;
    public static ArrayList<String> jumlahIkan;

    LayoutInflater mInflater;

    public CustomAdapter(Context context,String [] nm_ikan,List list){
        this.context = context;
        this.nm_ikan = nm_ikan;
        this.list  =list;
        jumlahIkan = new ArrayList<>();
        for (int i = 0; i < nm_ikan.length; i++) {
            jumlahIkan.add("0");
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        final ViewHolder holder;
        convertView=null;
        if (convertView == null) {
            holder = new ViewHolder();

            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.listview_adapter, null);
            textView = convertView.findViewById(R.id.nm_ikan);
            textView.setText(nm_ikan[position]);

            holder.caption = (EditText) convertView.findViewById(R.id.jumlah);
            holder.caption.setTag(position);
            holder.caption.setText(list.get(position).toString());
            if(holder.caption.getText().length() > 0){
                holder.caption.setSelection(holder.caption.getText().toString().length());
            }
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        int tag_position=(Integer) holder.caption.getTag();
        holder.caption.setId(tag_position);

        holder.caption.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final int position2 = holder.caption.getId();
                final EditText Caption = (EditText) holder.caption;
                if(Caption.getText().toString().length()>0){
                    list.set(position2,Integer.parseInt(Caption.getText().toString()));
                    Caption.setSelection(Caption.getText().toString().length());
                    jumlahIkan.set(position2,Caption.getText().toString());
                }else{
                    //Toast.makeText(context, "Please enter some value", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });





        return convertView;
    }

}

class ViewHolder {
    EditText caption;
}