package mx.ambmultimedia.brillamexico;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;


public class LoginStep3 extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_step3);

        Bundle bundle = getIntent().getExtras();
        final int CampoDeAccion = bundle.getInt("CampoDeAccion");

        ImageView imWoman = (ImageView) findViewById(R.id.imWoman);
        ImageView imMan = (ImageView) findViewById(R.id.imMan);

        if (CampoDeAccion == 1) {
            imWoman.setImageDrawable( getDrawable(R.drawable.personaje_adulta) );
            imMan.setImageDrawable( getDrawable(R.drawable.personaje_adulto) );
        }
        else if (CampoDeAccion == 2) {
            imWoman.setImageDrawable( getDrawable(R.drawable.personaje_emprendedora) );
            imMan.setImageDrawable( getDrawable(R.drawable.personaje_emprendedor) );
        }
        else if (CampoDeAccion == 3) {
            imWoman.setImageDrawable( getDrawable(R.drawable.personaje_empresaria) );
            imMan.setImageDrawable( getDrawable(R.drawable.personaje_empresario) );
        }

        // Jóven - Adulto
        imWoman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginStep3.this, LoginStep4.class);

                intent.putExtra("CampoDeAccion", CampoDeAccion);
                intent.putExtra("Genero", 1);

                startActivity(intent);
            }
        });
        // Emprendedor
        imMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginStep3.this, LoginStep4.class);

                intent.putExtra("CampoDeAccion", CampoDeAccion);
                intent.putExtra("Genero", 2);

                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_step3, menu);
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
