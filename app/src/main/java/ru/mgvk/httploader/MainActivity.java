package ru.mgvk.httploader;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    TextView httpText;
    EditText urlEdit;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        urlEdit = (EditText) findViewById(R.id.urlEdit);
        httpText = (TextView) findViewById(R.id.httpView);
        context = this;

        urlEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {

                    correctString();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            LoadPage();
                        }
                    }).start();

                    return true;
                }
                return false;
            }
        });

    }


    void correctString() {
        String s = urlEdit.getText().toString();
        if (!s.contains("http")) {
            s = "http://" + s;
        }
        urlEdit.setText(s);
    }

    void LoadPage() {
        try {
            URL url = new URL(urlEdit.getText().toString());

            URLConnection urlConnection = url.openConnection();
            urlConnection.connect();

            byte[] data = new byte[1024];

            InputStream input = new BufferedInputStream(url.openStream());

            String s = "";
            while (input.read(data) != -1) {
                s += new String(data);
            }

            final String finalS = s;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    httpText.setText(finalS);
                }
            });

            input.close();

        } catch (MalformedURLException | UnknownHostException eurl) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, getString(R.string.incorrectUrl), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, getString(R.string.unsortedError), Toast.LENGTH_SHORT).show();
                }
            });

        }

    }


}
