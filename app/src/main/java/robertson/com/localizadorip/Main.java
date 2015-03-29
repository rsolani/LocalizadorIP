package robertson.com.localizadorip;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class Main extends ActionBarActivity {

    private EditText etIP;
    private Button btLocalizar;
    private EditText etResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        etIP        = (EditText) findViewById(R.id.etIP);
        btLocalizar = (Button) findViewById(R.id.btLocalizar);
        etResult    = (EditText)findViewById(R.id.etResult);

        btLocalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = etIP.getText().toString();
                localizeIp(ip);
            }
        });
    }

    private void localizeIp(String ip){
        new Localizer(Main.this).execute("https://freegeoip.net/json/" + ip, etResult);
    }
}

class Localizer extends AsyncTask<Object,Object,Object>
{
    private EditText etRes;
    ProgressDialog myPd_ring = null;
    private Context context;

    public Localizer(Context context) {
        this.context = context;
    }

    @Override
    protected void onPostExecute(Object result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        etRes.setText(result.toString());
        myPd_ring.dismiss();
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
        myPd_ring  = new ProgressDialog(context);
        myPd_ring.setMessage("Aguarde...");
        myPd_ring.show();

    }

    @Override
    protected String doInBackground(Object... params) {

        etRes = (EditText)params[1];

        try
        {
            HttpClient httpclient = new DefaultHttpClient();

            HttpGet method = new HttpGet(params[0].toString());

            HttpResponse response = httpclient.execute(method);

            HttpEntity entity = response.getEntity();

            String _response = EntityUtils.toString(entity); // content will be consume only once

            JSONObject jObject=new JSONObject(_response);

            return jObject.toString();
        }
        catch(Exception e){
            return "Network problem";
        }
    }
}
