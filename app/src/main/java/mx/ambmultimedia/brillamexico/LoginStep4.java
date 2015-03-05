package mx.ambmultimedia.brillamexico;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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

        editTextHola = (EditText) findViewById(R.id.editTextHola);
        Nombre = editTextHola.getText().toString();

        Button toLogin = (Button) findViewById(R.id.toLogin);
        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginStep4.this, LoginStep5.class);

                intent.putExtra("CampoDeAccion", CampoDeAccion);
                intent.putExtra("Nombre", editTextHola.getText().toString());

                startActivity(intent);
            }
        });
    }
}
