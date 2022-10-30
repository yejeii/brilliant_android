package com.example.mysqlconn;

import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;


public class SignupActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;

    EditText sname, semail, spass;
    Button btn;

    private static final String url = "http://10.100.104.74/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        sname = findViewById(R.id.sname);
        semail = findViewById(R.id.semail);
        spass = findViewById(R.id.spass);
        btn = findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser(sname.getText().toString(), semail.getText().toString(), spass.getText().toString());
            }
        });
    }

    private void registerUser(final String name, final String email, final String pass) {

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                sname.setText("");
                semail.setText("");
                spass.setText("");
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("mytag", error.toString());
                sname.setText("");
                semail.setText("");
                spass.setText("");
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("name", name);
                map.put("email", email);
                map.put("pass", pass);
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);


    }

}