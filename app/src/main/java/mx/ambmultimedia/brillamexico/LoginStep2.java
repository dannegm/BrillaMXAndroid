package mx.ambmultimedia.brillamexico;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


public class LoginStep2 extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_step2);

        ImageView imYoungAdult = (ImageView) findViewById(R.id.imYoungAdult);
        ImageView imEntrepreneur = (ImageView) findViewById(R.id.imEntrepreneur);
        ImageView imEnterprise = (ImageView) findViewById(R.id.imEnterprise);

        // Jóven - Adulto
        imYoungAdult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginStep2.this, LoginStep3.class);
                intent.putExtra("CampoDeAccion", 1);

                startActivity(intent);
            }
        });
        // Emprendedor
        imEntrepreneur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginStep2.this, LoginStep3.class);
                intent.putExtra("CampoDeAccion", 2);

                startActivity(intent);
            }
        });
        // Empresa
        imEnterprise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginStep2.this, LoginStep3.class);
                intent.putExtra("CampoDeAccion", 3);

                startActivity(intent);
            }
        });
    }
}
