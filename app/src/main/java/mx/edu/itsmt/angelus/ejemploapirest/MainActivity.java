package mx.edu.itsmt.angelus.ejemploapirest;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    final int AGUAS=1,VERACRUZ=4,MTZ=102;
    TextView sal;
    Spinner municipios;
    Spinner entidad;  //102 martinez, 193 veracruz, 87 xalapa
    ArrayList listamunicipios;
    String[] entidades={"Aguascalientes","Veracruz","Martinez de la Torre"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sal  = (TextView) findViewById(R.id.salida);
        entidad = (Spinner) findViewById(R.id.spentidad);
        municipios = (Spinner) findViewById(R.id.spmunicipio);
        listamunicipios = new ArrayList();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, entidades);
        entidad.setAdapter(adapter);


        entidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              listamunicipios.clear();
                getData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void getData(){

        String sql = "http://datamx.io/dataset/319a8368-416c-4fe6-b683-39cf4d58b360/resource/829a7efd-3be9-4948-aa1b-896d1ee12979/download/municipiosmunicipios.json";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        URL url = null;
        HttpURLConnection conn;

        try {
            url = new URL(sql);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            String json = "";
            while((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }

            json = response.toString();
            JSONArray jsonArr = null;
            jsonArr = new JSONArray(json);

            int identidad=0;
            switch (entidad.getSelectedItemPosition()){
                case 0:
                    identidad = AGUAS;
                    break;
                case 1:
                    identidad = VERACRUZ;
                    break;
                case 2:
                    identidad = MTZ;
                    break;
            }

            String mensaje = "";
            for(int i = 0;i<jsonArr.length();i++){
                JSONObject jsonObject = jsonArr.getJSONObject(i);
               // Log.d("SLIDA",jsonObject.optString("name"));
                mensaje = jsonObject.optInt("state_id")+" "+jsonObject.optString("name");
                System.out.println(identidad);
              if(jsonObject.optInt("state_id")==identidad) {
                  listamunicipios.add(mensaje);
              }
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listamunicipios);
            municipios.setAdapter(adapter);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
