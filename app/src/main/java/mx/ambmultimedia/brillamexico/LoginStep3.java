package mx.ambmultimedia.brillamexico;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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
            imWoman.setImageResource( R.drawable.personaje_adulta );
            imMan.setImageResource( R.drawable.personaje_adulto );
        }
        else if (CampoDeAccion == 2) {
            imWoman.setImageResource( R.drawable.personaje_emprendedora );
            imMan.setImageResource( R.drawable.personaje_emprendedor );
        }
        else if (CampoDeAccion == 3) {
            imWoman.setImageResource( R.drawable.personaje_empresaria );
            imMan.setImageResource( R.drawable.personaje_empresario );
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
}
