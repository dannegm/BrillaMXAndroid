package mx.ambmultimedia.brillamexico;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class LoginStep5 extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_step5);

        Bundle bundle = getIntent().getExtras();
        int CampoDeAccion = bundle.getInt("CampoDeAccion");
        int Genero = bundle.getInt("Genero");
        String Nombre = bundle.getString("Nombre");

        User usuario = new User();
        usuario.setUser(0, Nombre, "", CampoDeAccion);

        String pHiText = getString(R.string.l_text_11);
        pHiText = pHiText.replaceAll("__username__", Nombre);

        TextView hiText = (TextView) findViewById(R.id.hiText);
        hiText.setText(Html.fromHtml(pHiText));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_step5, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
