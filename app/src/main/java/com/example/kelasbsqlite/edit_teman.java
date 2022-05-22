package com.example.kelasbsqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kelasbsqlite.database.DBController;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class edit_teman extends AppCompatActivity {
    TextView idText;
    Button Save;
    EditText edNama, edTelpon;
    String nma, tlp, id, namaEd, telponEd;
    int sukses;
    DBController controller = new DBController(this);

    private static String url_update = "http://10.0.2.2/umyTI/updatetm.php";
    private static String TAG = edit_teman.class.getSimpleName();
    private static  final String TAG_SUCCES = "success";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_teman);

        setContentView(R.layout.activity_edit_teman);

        idText = findViewById(R.id.textId);
        edNama = findViewById(R.id.edNama);
        edTelpon = findViewById(R.id.edTelp);
        Save = findViewById(R.id.buttonSave);

        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("kunci1");
        nma = bundle.getString("kunci2");
        tlp = bundle.getString("kunci3");

        idText.setText("id: "+id);
        edNama.setText(nma);
        edTelpon.setText(tlp);

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditData();
            }
        });
    }
    public  void callHome(){
        Intent i = new Intent(edit_teman.this, MainActivity.class);
        startActivity(i);
        finish();
    }
    public  void  EditData()
    {
        namaEd = edNama.getText().toString();
        telponEd =  edTelpon.getText().toString();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringReq = new StringRequest(Request.Method.POST, url_update, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Respon: " + response.toString());
                try {
                    JSONObject jobj = new JSONObject(response);
                    sukses = jobj.getInt(TAG_SUCCES);
                    if (sukses == 1) {
                        Toast.makeText(edit_teman.this, "Sukses mengedit data", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(edit_teman.this, "gagal", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG,"Error: " + error.getMessage());
                Toast.makeText(edit_teman.this,"Gagal Edit data", Toast.LENGTH_SHORT).show();
            }
        })
        {
         @Override
            protected Map<String, String> getParams(){
             Map<String,String> params = new HashMap<>();

             params.put("id",id);
             params.put("nama", namaEd);
             params.put("telpon",telponEd);

             return  params;
         }
        };
        requestQueue.add(stringReq);;
        CallHomeActivity();
    }

    private void CallHomeActivity() {
        Intent inten = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(inten);
        finish();
    }
}