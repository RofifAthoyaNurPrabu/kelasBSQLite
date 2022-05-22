package com.example.kelasbsqlite.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kelasbsqlite.App.AppController;
import com.example.kelasbsqlite.MainActivity;
import com.example.kelasbsqlite.R;
import com.example.kelasbsqlite.database.DBController;
import com.example.kelasbsqlite.database.Teman;
import com.example.kelasbsqlite.edit_teman;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TemanAdapter extends RecyclerView.Adapter<TemanAdapter.TemanViewHolder> {
    private ArrayList<Teman> listData;
    private Context control;

    public TemanAdapter(ArrayList<Teman> listData) {
        this.listData = listData;
    }

    @NonNull
    @Override
    public TemanAdapter.TemanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInf = LayoutInflater.from(parent.getContext());
        View view = layoutInf.inflate(R.layout.row_data_teman, parent, false);
        control = parent.getContext();
        return new TemanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TemanAdapter.TemanViewHolder holder, int position) {
        String nma, tlp, id;

        id = listData.get(position).getId();
        nma = listData.get(position).getNama();
        tlp = listData.get(position).getTelpon();
        DBController db = new DBController(control);

        holder.namaTxt.setTextColor(Color.BLUE);
        holder.namaTxt.setTextSize(20);
        holder.namaTxt.setText(nma);
        holder.telponTxt.setText(tlp);

        holder.cardku.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu pm = new PopupMenu(view.getContext(), view);
                pm.inflate(R.menu.menu);
                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.mnEdit:
                                Bundle bendel = new Bundle();
                                bendel.putString("kunci1", id);
                                bendel.putString("kunci2", nma);
                                bendel.putString("kunci3", tlp);
                                Intent inten = new Intent(view.getContext(), edit_teman.class);
                                String bundle;
                                inten.putExtras(bendel);
                                view.getContext().startActivity(inten);
                                break;

                            case R.id.mnHapus:
                                AlertDialog.Builder alertdb = new AlertDialog.Builder(view.getContext());
                                alertdb.setTitle("Yakin" + nma + "akan dihapus?");
                                alertdb.setMessage("Tekan ya untuk menghapus");
                                alertdb.setCancelable(false);
                                alertdb.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        HapusData(id);
                                        Toast.makeText(view.getContext(), "Data" + id + "telah dihapus", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(view.getContext(), MainActivity.class);
                                        view.getContext().startActivity(intent);
                                    }

                                });
                                alertdb.setNegativeButton("Tidak", new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int which){
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog adlg = alertdb.create();
                                adlg.show();
                                break;
                        }
                        return true;
                    }
                });
                pm.show();
                return true;
            }
        });

    }

    private void HapusData(final String idx) {
        String url_update = "";
        final  String TAG = MainActivity.class.getSimpleName();
        final String TAG_SUCCES = "success";
        final int[] sukses = new int[1];

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_update, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Respon: " + response.toString());
                try {
                    JSONObject jobj = new JSONObject(response);
                    sukses[0] = jobj.getInt((TAG_SUCCES));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG,"Error: "+error.getMessage());
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();

                params.put("id", idx);

                return  params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    @Override
    public int getItemCount() {
        return (listData != null)?listData.size() : 0;
    }
    public class TemanViewHolder extends RecyclerView.ViewHolder {
        private CardView cardku;
        private TextView namaTxt, telponTxt;
        public TemanViewHolder(View view) {
            super(view);
            cardku = (CardView) view.findViewById(R.id.kartuku);
                    namaTxt = (TextView) view.findViewById(R.id.textNama);
                    telponTxt = (TextView) view.findViewById(R.id.textTelpon);
        }
    }
}
