package com.example.yandexpredictor;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {
    TextInputEditText inputEditText;
    Button btn;
    TextView txtV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.Btn);
        inputEditText = (TextInputEditText) findViewById(R.id.EditText);
        txtV = (TextView) findViewById(R.id.txtV);
        inputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s + "";
                if (String.valueOf(inputEditText.getText()).equals("")) {
                } else {
                    predict(str);
                }
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usersValue = String.valueOf(inputEditText.getText());
                String response =  String.valueOf(txtV.getText());
                if (position == 1)
                {
                    usersValue = usersValue + " " + response;
                }
                else
                    {
                    usersValue = usersValue.substring(0, usersValue.length() + position) + response;
                }
                inputEditText.setText(usersValue);
                inputEditText.setSelection(inputEditText.getText().length());
            }
        });
    }
    int position;
    void predict(String text) {
        final String request = "https://predictor.yandex.net/api/v1/predict.json/complete" +
                "?key=pdct.1.1.20210412T141130Z.7fe24c3464fb114c.a91bd86212d96a2ed24c9e0a44fc721cd4e118ea" +
                "&q=" + text +
                "&lang=ru";
        new Thread(new Runnable() {
            @Override
            public void run() {
                URLConnection connect = null;
                try {
                    connect = new URL(request).openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Scanner in = null;
                try {
                    in = new Scanner(connect.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                final String response = in.nextLine();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String jsonString = response;
                        JSONObject object = null;
                        try {
                            object = new JSONObject(jsonString);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JSONArray jsonArray = null;
                        try {
                            jsonArray = object.getJSONArray("text");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            txtV.setText("");
                            try {
                                txtV.append(jsonArray.get(i).toString());
                                position = object.getInt("pos");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        }).start();
    }
}

