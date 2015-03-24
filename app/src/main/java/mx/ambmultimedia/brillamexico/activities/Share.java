package mx.ambmultimedia.brillamexico.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;

import mx.ambmultimedia.brillamexico.R;
import mx.ambmultimedia.brillamexico.utils.Config;

public class Share extends ActionBarActivity {
    // Referencias de la actvididad actual
    private Context ctx;
    private Activity atx;
    private Config config;

    // Referencias del compromiso previamente seleccionado
    int CampoDeAccion;
    int compromisoID;
    String compID = "0";

    // Cache de la imagen tomada por el Intent de la cámara
    private ImageView preview;
    private Bitmap previewFoto;

    // Datos auxiliares para el Intent de la cámara
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private String selectedImagePath = "";
    private String imgPath;

    // Datos para el REST Api
    private String fbID;
    private String hostname;

    // Creamos el Activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        // Almacenamos la instancia actual
        ctx = this;
        atx = this;
        // Generamos la configuraciones
        config = new Config(ctx);
        // Obtenemos datos para el REST
        fbID = config.get("fbID", "0");
        hostname = getString(R.string.hostname);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Obtenemos datos del Activity anterior
        Bundle bundle = getIntent().getExtras();
        CampoDeAccion = bundle.getInt("CampoDeAccion");
        compromisoID = bundle.getInt("compromisoID");

        // Generamos el Intent de la cámara
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

        // Obtenemos elementos de la interfaz

        // Imagen donde se mostrará la captura
        preview = (ImageView) findViewById(R.id.imageSelfie);

        // Botón que compartirá la foto
        final ActionProcessButton sendFoto = (ActionProcessButton) findViewById(R.id.sendPhoto);

        // Al hacer click ...
        sendFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creamos un archivo en memoria
                File photo = new File(selectedImagePath);
                // Obtenemos el cuadro de texto
                EditText pieDeFoto = (EditText) findViewById(R.id.pieDeFoto);

                // Mode Infinity
                sendFoto.setMode(ActionProcessButton.Mode.ENDLESS);
                sendFoto.setProgress(99);
                sendFoto.setEnabled(false);

                // Creamos los parámetros a enviar por POST
                RequestParams params = new RequestParams();
                params.put("engagement_id", compID); // Copromiso
                params.put("description", pieDeFoto.getText().toString() + " #MiSelfieBrilla"); // Descripción -> se agregó #MiSelfieBrila
                try {
                    // Agregamos binario de la foto a los parámetros
                    params.put("picture", photo);
                } catch(FileNotFoundException e) {
                    // Si algo sale mal, lo mostramos en un Toast
                    Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT);
                }
                // Creamos instancia del cliente HTTP
                final AsyncHttpClient client = new AsyncHttpClient();
                // Hacemos la petición
                client.post(hostname + "/user/selfie/" + fbID, params, new JsonHttpResponseHandler() {
                    // Si todo sale bien
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            // Obtenemos el ID de la selfie nueva y ...
                            String selfieID = response.getString("id");
                            // Le damos 10 puntos al usuario
                            addPuntos(client, 10);
                            // Seguimos con la acplicación
                            NextStep(selfieID);
                        } catch (JSONException e) {}
                    }

                    // Si algo falla
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String response, Throwable e) {
                        // Mostramos un mensaje de error
                        String msg = "[" + statusCode + "|u/selfie/upload] " + e.getMessage();
                        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
                        Log.i("[Client]", msg);

                        sendFoto.setEnabled(true);
                        sendFoto.setProgress(0);
                        sendFoto.setMode(ActionProcessButton.Mode.PROGRESS);
                        sendFoto.setText("Vuelve a intentarlo");
                    }
                });
            }
        });
    }

    /**
     * Método para dar puntos al usuario
     *
     * @param client Insntancia HTTP
     * @param pp Puntos que se le dan
     */
    private void addPuntos (AsyncHttpClient client, int pp) {
        // Generamos los parámetros POST y hacemos la petición
        RequestParams points = new RequestParams();
        points.put("points", pp);
        client.post(hostname + "/user/points/" + fbID, points, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Mostramos notificación que se le han dado 10 puntos
                Toast.makeText(ctx, "Has ganado 10 puntos", Toast.LENGTH_LONG).show();

                // Actualizamos la caché para evitar que se vuelva a subir una selfie con el mismo compromiso
                String compID = "comp_" + CampoDeAccion + "_" + compromisoID;
                config.set(compID, "true");
            }

            // SI algo falla, no me interesa saberlo...
            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable e) { }
        });
    }

    /**
     * Método para continuar la navegación
     * @param selfieID ID de la nueva selfie
     */
    private void NextStep (String selfieID) {
        // Obtenemos cuantas selfies ha subido el usuario hasta ahora, le sumamos una
        Integer nselfies = Integer.valueOf(config.get("NSelfies", "0")) + 1;
        config.set("NSelfies", nselfies.toString());

        // Si el usuario ha subido 5 selfies
        if (nselfies == 5) {
            // El usuario ha ganado un logro, lo mandamos a la pantalla de logros
            Intent intent = new Intent(Share.this, Logro.class);
            intent.putExtra("selfieID", selfieID); // Se agregó esta línea, no estaba definida y por lo tanto no regresaba la selfie al final de la pantalla de logro
            intent.putExtra("Reference", "Selfie");
            intent.putExtra("LogroID", "3");
            config.set("Refer", "ShareActivity");
            startActivity(intent);
            atx.finish();
        // Si el usuario ha subido 10 selfies
        } else if (nselfies == 10) {
            // El usuario ha ganado un logro, lo mandamos a la pantalla de logros
            Intent intent = new Intent(Share.this, Logro.class);
            intent.putExtra("selfieID", selfieID); // Se agregó esta línea, no estaba definida y por lo tanto no regresaba la selfie al final de la pantalla de logro
            intent.putExtra("Reference", "Selfie");
            intent.putExtra("LogroID", "4");
            config.set("Refer", "ShareActivity");
            startActivity(intent);
            atx.finish();
        // Si toodo continua normal
        } else {
            // Lo mandamos a la pantalla de la selfie que tomó
            Intent intent = new Intent(Share.this, Selfie.class);
            intent.putExtra("selfieID", selfieID);
            config.set("Refer", "ShareActivity");
            startActivity(intent);
            atx.finish();
        }
    }

    /**
     * Método para crear el marco del compromiso
     */
    private void CreateFrame () {
        Drawable frameDraw = getResources().getDrawable(R.drawable.marco_1);

        if (CampoDeAccion == 1) {
            switch (compromisoID) {
                case 0: frameDraw = getResources().getDrawable(R.drawable.marco_1); compID = "1"; break;
                case 1: frameDraw = getResources().getDrawable(R.drawable.marco_2); compID = "2"; break;
                case 2: frameDraw = getResources().getDrawable(R.drawable.marco_3); compID = "3"; break;
                case 3: frameDraw = getResources().getDrawable(R.drawable.marco_4); compID = "4"; break;
                case 4: frameDraw = getResources().getDrawable(R.drawable.marco_5); compID = "5"; break;
                case 5: frameDraw = getResources().getDrawable(R.drawable.marco_6); compID = "6"; break;
                case 6: frameDraw = getResources().getDrawable(R.drawable.marco_7); compID = "7"; break;
                case 7: frameDraw = getResources().getDrawable(R.drawable.marco_8); compID = "8"; break;
                case 8: frameDraw = getResources().getDrawable(R.drawable.marco_9); compID = "9"; break;
                case 9: frameDraw = getResources().getDrawable(R.drawable.marco_10); compID = "10"; break;
                case 10: frameDraw = getResources().getDrawable(R.drawable.marco_11); compID = "11"; break;
                case 11: frameDraw = getResources().getDrawable(R.drawable.marco_12); compID = "12"; break;
                case 12: frameDraw = getResources().getDrawable(R.drawable.marco_13); compID = "13"; break;
                case 13: frameDraw = getResources().getDrawable(R.drawable.marco_14); compID = "14"; break;
            }
        }
        else if (CampoDeAccion == 2) {
            switch (compromisoID) {
                case 0: frameDraw = getResources().getDrawable(R.drawable.marco_15); compID = "15"; break;
                case 1: frameDraw = getResources().getDrawable(R.drawable.marco_16); compID = "16"; break;
                case 2: frameDraw = getResources().getDrawable(R.drawable.marco_17); compID = "17"; break;
                case 3: frameDraw = getResources().getDrawable(R.drawable.marco_18); compID = "18"; break;
            }
        }
        else if (CampoDeAccion == 3) {
            switch (compromisoID) {
                case 0: frameDraw = getResources().getDrawable(R.drawable.marco_19); compID = "19"; break;
                case 1: frameDraw = getResources().getDrawable(R.drawable.marco_20); compID = "20"; break;
                case 2: frameDraw = getResources().getDrawable(R.drawable.marco_21); compID = "21"; break;
                case 3: frameDraw = getResources().getDrawable(R.drawable.marco_22); compID = "22"; break;
                case 4: frameDraw = getResources().getDrawable(R.drawable.marco_23); compID = "23"; break;
                case 5: frameDraw = getResources().getDrawable(R.drawable.marco_24); compID = "24"; break;
                case 6: frameDraw = getResources().getDrawable(R.drawable.marco_25); compID = "25"; break;
                case 7: frameDraw = getResources().getDrawable(R.drawable.marco_26); compID = "26"; break;
                case 8: frameDraw = getResources().getDrawable(R.drawable.marco_27); compID = "27"; break;
            }
        }

        ImageView imageFrame = (ImageView) findViewById(R.id.imageMarco);
        imageFrame.setImageDrawable(frameDraw);
    }

    /**
     * Método para cortar la imagen de la cámara
     * @param _bMap Bitmap regresado por la cámara
     * @return Bitmap ya recortado
     */
    public Bitmap cropImage (Bitmap _bMap) {
        Bitmap bMap = _bMap;
        int nwidth, nheight;

        if (bMap.getWidth() > bMap.getHeight()) {
            nwidth = bMap.getHeight();
            nheight = bMap.getHeight();
        } else if (bMap.getWidth() < bMap.getHeight()) {
            nwidth = bMap.getWidth();
            nheight = bMap.getWidth();
        } else {
            nwidth = bMap.getWidth();
            nheight = bMap.getHeight();
        }

        Bitmap croppedBitmap = Bitmap.createBitmap(bMap, 0, 0, nwidth, nheight);
        return croppedBitmap;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Evento ejecutado después de tomar la selfie
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            // Si sale bien
            if (resultCode == RESULT_OK) {

                // Creamos el marco de la selfie
                CreateFrame();

                // Obtenemos el path de la foto tomada y la convertimos en Bitmap
                selectedImagePath = getImagePath();
                previewFoto = decodeFile(selectedImagePath);

                // Cortamos el btimap y lo mostramos en el cuadro de imagen
                previewFoto = cropImage(previewFoto);
                preview.setImageBitmap(previewFoto);
            } else if (resultCode == RESULT_CANCELED) {
                // Si se canceló la captura, regresamos al activity anterior
                Intent intent = new Intent(Share.this, Compromisos.class);
                startActivity(intent);
                atx.finish();
            } else {
                // Si algo sale mal, mostramos un mensaje y regresamos al activity anterior
                Toast.makeText(ctx, "Algo ha salido mal", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Share.this, Compromisos.class);
                startActivity(intent);
                atx.finish();
            }
        }
    }

    // Guarda imagen en la memoria del teléfono
    public Uri setImageUri() {
        File file = new File(Environment.getExternalStorageDirectory(), "bmx_" + new Date().getTime() + ".png");
        Uri imgUri = Uri.fromFile(file);
        this.imgPath = file.getAbsolutePath();
        return imgUri;
    }

    // Obtiene la rita de imgPath
    public String getImagePath() {
        return imgPath;
    }

    // Convierte la imagen tomada de la memoria del teléfono a un bitmap
    public Bitmap decodeFile(String path) {
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE = 512;
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE
                    && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeFile(path, o2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
}
