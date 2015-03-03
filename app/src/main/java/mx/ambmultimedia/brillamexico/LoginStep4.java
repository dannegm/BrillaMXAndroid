package mx.ambmultimedia.brillamexico;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class LoginStep4 extends ActionBarActivity {

    String Nombre;
    EditText editTextHola;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_step4);

        Bundle bundle = getIntent().getExtras();
        final int CampoDeAccion = bundle.getInt("CampoDeAccion");
        final int Genero = bundle.getInt("Genero");

        editTextHola = (EditText) findViewById(R.id.editTextHola);
        Nombre = editTextHola.getText().toString();

        Button toLogin = (Button) findViewById(R.id.toLogin);
        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginStep4.this, LoginStep5.class);

                intent.putExtra("CampoDeAccion", CampoDeAccion);
                intent.putExtra("Genero", Genero);
                intent.putExtra("Nombre", editTextHola.getText().toString());

                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_step4, menu);
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
