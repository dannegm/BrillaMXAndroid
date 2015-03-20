package mx.ambmultimedia.brillamexico.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import mx.ambmultimedia.brillamexico.R;
import mx.ambmultimedia.brillamexico.activities.Actividad;
import mx.ambmultimedia.brillamexico.activities.Bases;
import mx.ambmultimedia.brillamexico.activities.Emprendedores;
import mx.ambmultimedia.brillamexico.activities.Logout;
import mx.ambmultimedia.brillamexico.activities.Noticias;
import mx.ambmultimedia.brillamexico.activities.Privacy;
import mx.ambmultimedia.brillamexico.activities.UserProfile;

public class DrawerUtils {
    private Context ctx;
    private Activity atx;

    public DrawerUtils (Context _ctx, Activity _atx) {
        ctx = _ctx;
        atx = _atx;
    }

    public void Navigation (final DrawerLayout drawer) {
        final Class actualClass = ctx.getClass();
        // My Perfil
        LinearLayout toMyProfile = (LinearLayout) atx.findViewById(R.id.dw_myprofile);
        toMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actualClass != UserProfile.class) {
                    Intent intent = new Intent(ctx, UserProfile.class);
                    atx.startActivity(intent);
                } else {
                    drawer.closeDrawer(Gravity.LEFT);
                }
            }
        });

        // Actividad
        LinearLayout toActivity = (LinearLayout) atx.findViewById(R.id.dw_activity);
        toActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actualClass != Actividad.class) {
                    Intent intent = new Intent(ctx, Actividad.class);
                    atx.startActivity(intent);
                } else {
                    drawer.closeDrawer(Gravity.LEFT);
                }
            }
        });

        // Noticias
        LinearLayout toNoticias = (LinearLayout) atx.findViewById(R.id.dw_news);
        toNoticias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actualClass != Noticias.class) {
                    Intent intent = new Intent(ctx, Noticias.class);
                    atx.startActivity(intent);
                } else {
                    drawer.closeDrawer(Gravity.LEFT);
                }
            }
        });

        // Emprendedores
        LinearLayout toEmp = (LinearLayout) atx.findViewById(R.id.dw_emp);
        toEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actualClass != Emprendedores.class) {
                    Intent intent = new Intent(ctx, Emprendedores.class);
                    atx.startActivity(intent);
                } else {
                    drawer.closeDrawer(Gravity.LEFT);
                }
            }
        });

        // Otros

        // Bases
        LinearLayout toBases = (LinearLayout) atx.findViewById(R.id.dw_bases);
        toBases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actualClass != Bases.class) {
                    Intent intent = new Intent(ctx, Bases.class);
                    atx.startActivity(intent);
                } else {
                    drawer.closeDrawer(Gravity.LEFT);
                }
            }
        });

        // Privacidad
        LinearLayout toPrivacy = (LinearLayout) atx.findViewById(R.id.dw_privacy);
        toPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actualClass != Privacy.class) {
                    Intent intent = new Intent(ctx, Privacy.class);
                    atx.startActivity(intent);
                } else {
                    drawer.closeDrawer(Gravity.LEFT);
                }
            }
        });

        // Salir
        LinearLayout toSalir = (LinearLayout) atx.findViewById(R.id.dw_salir);
        toSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actualClass != Logout.class) {
                    Intent intent = new Intent(ctx, Logout.class);
                    atx.startActivity(intent);
                } else {
                    drawer.closeDrawer(Gravity.LEFT);
                }
            }
        });
    }
}
