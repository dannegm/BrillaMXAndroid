package mx.ambmultimedia.brillamexico;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

@SuppressLint("ValidFragment")
public class CompromisoFragment extends Fragment {
    Context ctx;
    Config config;
    int index;
    int action;

    public CompromisoFragment (Context _ctx, int fieldToAction, int position) {
        ctx = _ctx;
        action = fieldToAction;
        index = position;
        config = new Config(ctx);
    }

    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View layout;
        layout = inflater.inflate(R.layout.fragment_compromiso, container, false);

        String compID = "comp_" + action + "_" + index;
        final Boolean isChecked = Boolean.valueOf( config.get(compID, "false") );

        String fieldName;
        String[] textos;
        switch (action) {
            case 1:
                fieldName = getString(R.string.c_youngadults);
                textos = getResources().getStringArray(R.array.campo1);
                break;
            case 2:
                fieldName = getString(R.string.c_entrepeneur);
                textos = getResources().getStringArray(R.array.campo2);
                break;
            case 3:
                fieldName = getString(R.string.c_enterprise);
                textos = getResources().getStringArray(R.array.campo3);
                break;
            default:
                fieldName = getString(R.string.c_youngadults);
                textos = getResources().getStringArray(R.array.campo1);
                break;
        }
        String compromisoText = textos[index];

        Drawable iconDraw;
        iconDraw = ctx.getResources().getDrawable(R.drawable.ic_1_1);
        if (action == 1) {
            switch (index) {
                case 0: iconDraw = ctx.getResources().getDrawable(R.drawable.ic_1_1); break;
                case 1: iconDraw = ctx.getResources().getDrawable(R.drawable.ic_1_2); break;
                case 2: iconDraw = ctx.getResources().getDrawable(R.drawable.ic_1_3); break;
                case 3: iconDraw = ctx.getResources().getDrawable(R.drawable.ic_1_4); break;
                case 4: iconDraw = ctx.getResources().getDrawable(R.drawable.ic_1_5); break;
                case 5: iconDraw = ctx.getResources().getDrawable(R.drawable.ic_1_6); break;
                case 6: iconDraw = ctx.getResources().getDrawable(R.drawable.ic_1_7); break;
                case 7: iconDraw = ctx.getResources().getDrawable(R.drawable.ic_1_8); break;
                case 8: iconDraw = ctx.getResources().getDrawable(R.drawable.ic_1_9); break;
                case 9: iconDraw = ctx.getResources().getDrawable(R.drawable.ic_1_10); break;
                case 10: iconDraw = ctx.getResources().getDrawable(R.drawable.ic_1_11); break;
                case 11: iconDraw = ctx.getResources().getDrawable(R.drawable.ic_1_12); break;
                case 12: iconDraw = ctx.getResources().getDrawable(R.drawable.ic_1_13); break;
                case 13: iconDraw = ctx.getResources().getDrawable(R.drawable.ic_1_14); break;
            }
        }
        else if (action == 2) {
            switch (index) {
                case 0: iconDraw = ctx.getResources().getDrawable(R.drawable.ic_2_15); break;
                case 1: iconDraw = ctx.getResources().getDrawable(R.drawable.ic_2_16); break;
                case 2: iconDraw = ctx.getResources().getDrawable(R.drawable.ic_2_17); break;
                case 3: iconDraw = ctx.getResources().getDrawable(R.drawable.ic_2_18); break;
            }
        }
        else if (action == 2) {
            switch (index) {
                case 0: iconDraw = ctx.getResources().getDrawable(R.drawable.ic_3_19); break;
                case 1: iconDraw = ctx.getResources().getDrawable(R.drawable.ic_3_20); break;
                case 2: iconDraw = ctx.getResources().getDrawable(R.drawable.ic_3_21); break;
                case 3: iconDraw = ctx.getResources().getDrawable(R.drawable.ic_3_22); break;
                case 4: iconDraw = ctx.getResources().getDrawable(R.drawable.ic_3_23); break;
                case 5: iconDraw = ctx.getResources().getDrawable(R.drawable.ic_3_24); break;
                case 6: iconDraw = ctx.getResources().getDrawable(R.drawable.ic_3_25); break;
                case 7: iconDraw = ctx.getResources().getDrawable(R.drawable.ic_3_26); break;
                case 8: iconDraw = ctx.getResources().getDrawable(R.drawable.ic_3_27); break;
            }
        }

        ImageView iconAccion = (ImageView) layout.findViewById(R.id.iconAccion);
        iconAccion.setImageDrawable(iconDraw);

        TextView textAccion = (TextView) layout.findViewById(R.id.textAccion);
        textAccion.setText(compromisoText);

        TextView campoAccion = (TextView) layout.findViewById(R.id.campoAccion);
        campoAccion.setText(fieldName);


        FloatingActionButton toCamera = (FloatingActionButton) layout.findViewById(R.id.toCamera);
        toCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isChecked) {
                    Intent intent = new Intent(ctx, Share.class);
                    intent.putExtra("CampoDeAccion", action);
                    intent.putExtra("compromisoID", index);
                    startActivity(intent);
                } else {
                    Toast.makeText(ctx, "Ya has hecho este compromiso", Toast.LENGTH_LONG).show();
                }
            }
        });

        if (isChecked) {
            LinearLayout back = (LinearLayout) layout.findViewById(R.id.backCompromiso);
            back.setBackgroundColor(ctx.getResources().getColor(R.color.c_back_disable));
            toCamera.setIcon(R.drawable.ic_checked);
            TextView compromisoStatus = (TextView) layout.findViewById(R.id.compromisoStatus);
            compromisoStatus.setText("¡Ya te has comprometido!");
        }

        return layout;
    }
}
