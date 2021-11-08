package es.ivan.cifrador;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.Arrays;

import es.ivan.cifrador.caesar.Caesar;
import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener, Spinner.OnItemSelectedListener {

    // Algunas variables útiles para la realización de la APP
    private final String letters = "abcdefghijklmnñopqrstuvwxyz";
    private boolean mayus = false;
    private boolean longClick = false;
    private int enDeMoves = 0;
    private final Caesar caesar = new Caesar();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final int orientation = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getRotation();

        // Dependiendo de la orientación (girado) carga un layout u otro
        if (orientation == Surface.ROTATION_90 || orientation == Surface.ROTATION_270) {
            this.rotationLayout();
        } else {
            this.normalLayout();
        }
    }

    /**
     * Método interno para la creacción de los botones del cifrado
     */
    private void normalLayout() {
        final DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        final int buttonSize = metrics.widthPixels / 6;

        final int xPos = buttonSize / 2;
        final int yPos = (buttonSize * 2) - (buttonSize / 4);

        int columns = 0;
        int rows = 0;

        final ConstraintLayout layout = findViewById(R.id.root);
        final ConstraintSet set = new ConstraintSet();
        set.clone(layout);

        /*
        A-Z
        i = 1 / 0 porque la API de Android es muy fancy y dependiendo de la versión coge las letras en un slot u otro

        'letters.split("")[1].equalsIgnoreCase("b")' sirve para comprobar cual es la letra que primero coge y así actuar en conveniencia.
        API 21-22 = a
        API 22-30 = b
        */
        final boolean newAPI = letters.split("")[1].equalsIgnoreCase("b");
        for (int i = newAPI ? 0 : 1; i <= (newAPI ? this.letters.length() - 1 : this.letters.length()); i++) {
            final Button button = new Button(this);
            button.setText(letters.split("")[i]);
            button.setId(i);
            button.setAllCaps(false);

            button.setOnClickListener(this);

            layout.addView(button);

            set.setTranslationX(button.getId(), xPos + (columns * buttonSize + 5));
            set.setTranslationY(button.getId(), yPos + (rows * buttonSize + 5));
            set.constrainHeight(button.getId(), buttonSize);
            set.constrainWidth(button.getId(), buttonSize);
            set.applyTo(layout);

            /*
            Simplemente se usa esto para saber donde tiene que ir cada botón dependiendo
             */
            columns++;
            if ((newAPI ? i + 1 : i) % 5 == 0 && i != 0) {
                rows++;
                columns = 0;
            }
        }

        // Space
        final ImageButton space = new ImageButton(this);
        space.setImageResource(R.drawable.spacebar);
        space.setId(Integer.valueOf(34));
        space.setScaleType(ImageView.ScaleType.FIT_CENTER);

        space.setOnClickListener(this);
        space.setOnLongClickListener(this);

        layout.addView(space);

        set.setTranslationX(space.getId(), xPos + (columns++ * buttonSize + 5));
        set.setTranslationY(space.getId(), yPos + (rows * buttonSize + 5));
        set.constrainHeight(space.getId(), buttonSize);
        set.constrainWidth(space.getId(), buttonSize);
        set.applyTo(layout);

        // Mayus
        final ImageButton mayus = new ImageButton(this);
        mayus.setImageResource(R.drawable.mayus);
        mayus.setId(Integer.valueOf(30));
        mayus.setScaleType(ImageView.ScaleType.FIT_CENTER);

        mayus.setOnClickListener(this);
        mayus.setOnLongClickListener(this);

        layout.addView(mayus);

        set.setTranslationX(mayus.getId(), xPos + (columns++ * buttonSize + 5));
        set.setTranslationY(mayus.getId(), yPos + (rows * buttonSize + 5));
        set.constrainHeight(mayus.getId(), buttonSize);
        set.constrainWidth(mayus.getId(), buttonSize);
        set.applyTo(layout);

        // Delete
        final ImageButton remove = new ImageButton(this);
        remove.setImageResource(R.drawable.rem);
        remove.setId(Integer.valueOf(33));
        remove.setScaleType(ImageView.ScaleType.FIT_CENTER);

        remove.setOnClickListener(this);

        layout.addView(remove);

        set.setTranslationX(remove.getId(), xPos + (columns * buttonSize + 5));
        set.setTranslationY(remove.getId(), yPos + (rows * buttonSize + 5));
        set.constrainHeight(remove.getId(), buttonSize);
        set.constrainWidth(remove.getId(), buttonSize);
        set.applyTo(layout);

        // Encrypt
        final Button encrypt = new Button(this);
        encrypt.setText("Encriptar");
        encrypt.setId(Integer.valueOf(31));

        encrypt.setOnClickListener(this);

        layout.addView(encrypt);

        set.setTranslationX(encrypt.getId(), 5);
        set.setTranslationY(encrypt.getId(), metrics.heightPixels - (buttonSize * 2 - 5));
        set.constrainHeight(encrypt.getId(), buttonSize);
        set.constrainWidth(encrypt.getId(), buttonSize * 2);
        set.applyTo(layout);

        // Decrypt
        final Button decrypt = new Button(this);
        decrypt.setText("Desencriptar");
        decrypt.setId(Integer.valueOf(32));

        decrypt.setOnClickListener(this);

        layout.addView(decrypt);

        set.setTranslationX(decrypt.getId(), metrics.widthPixels - 5 - buttonSize * 2);
        set.setTranslationY(decrypt.getId(), metrics.heightPixels - (buttonSize * 2 - 5));
        set.constrainHeight(decrypt.getId(), buttonSize);
        set.constrainWidth(decrypt.getId(), buttonSize * 2);
        set.applyTo(layout);

        // Spinner
        final Spinner enDeMoves = new Spinner(this);
        enDeMoves.setId(Integer.valueOf(22100));
        final ArrayAdapter<Integer> moveAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        enDeMoves.setAdapter(moveAdapter);
        enDeMoves.setSelection(1);

        enDeMoves.setOnItemSelectedListener(this);

        layout.addView(enDeMoves);

        set.setTranslationX(enDeMoves.getId(), metrics.widthPixels - 5 - buttonSize * 3);
        set.setTranslationY(enDeMoves.getId(), metrics.heightPixels - (buttonSize * 2 - 5));
        set.constrainHeight(enDeMoves.getId(), buttonSize);
        set.constrainWidth(enDeMoves.getId(), buttonSize * 2);
        set.applyTo(layout);
    }

    // Import methods

    @Override
    public void onClick(View v) {
        final TextView noCifrado = findViewById(R.id.noCifrado);
        final TextView cifrado = findViewById(R.id.cifrado);

        switch (v.getId()) {
            case 30:
                if (this.longClick) {
                    this.longClick = false;
                    v.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_ATOP);
                }
                this.mayus = !this.mayus;
                v.getBackground().setColorFilter(this.mayus ? Color.RED : Color.LTGRAY, PorterDuff.Mode.SRC_ATOP);
                this.updateLetterButton();
                break;
            case 31:
                if (noCifrado.getText().length() <= 0) {
                    this.error("No puedes encriptar si no hay nada, cruck", 5);
                    break;
                }
                cifrado.setText(this.caesar.encrypt((String) noCifrado.getText(), this.enDeMoves));
                break;
            case 32:
                if (noCifrado.getText().length() <= 0) {
                    this.error("No puedes desencriptar si no hay nada, cruck", 5);
                    break;
                }
                cifrado.setText(this.caesar.decrypt((String) noCifrado.getText(), this.enDeMoves));
                break;
            case 33:
                if (noCifrado.getText().length() <= 0) {
                    this.error("No puedes borrar si no hay nada, cruck", 5);
                    break;
                }
                noCifrado.setText(String.valueOf(Arrays.copyOf(noCifrado.getText().toString().toCharArray(), noCifrado.getText().toString().toCharArray().length - 1)));
                break;
            case 34:
                // Sólo pone un espacio...
                noCifrado.setText(noCifrado.getText() + " ");
                break;
            default:
                if (noCifrado.getText().length() > 30) {
                    this.error("No puedes poner más letras", 5);
                    return;
                }

                String letter = letters.split("")[v.getId()];
                if (this.mayus) letter = letter.toUpperCase();
                noCifrado.setText(noCifrado.getText() + letter);
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        this.mayus = true;
        this.longClick = true;
        this.updateLetterButton();
        v.getBackground().setColorFilter(Color.MAGENTA, PorterDuff.Mode.SRC_ATOP);
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        this.enDeMoves = ((Spinner) findViewById(Integer.valueOf(22100))).getSelectedItemPosition();
        System.out.println(this.enDeMoves);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    //

    /**
     * Método usado para actualizar las letras del teclado dependiendo si hay que ponerlas en mayusculas o no.
     */
    private void updateLetterButton() {
        final boolean newAPI = letters.split("")[1].equalsIgnoreCase("b");
        for (int i = newAPI ? 0 : 1; i <= (newAPI ? this.letters.length() - 1 : this.letters.length()); i++) {
            final Button button = findViewById(i);
            button.setAllCaps(this.mayus);
        }
    }

    /**
     * El layout para cuando se rota la pantalla :peepoClap:
     */
    private void rotationLayout() {
        final DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final ConstraintLayout layout = findViewById(R.id.root);
        final ConstraintSet set = new ConstraintSet();
        set.clone(layout);

        final GifImageView gif = new GifImageView(this);
        gif.setImageResource(R.drawable.nope);
        gif.setId(Integer.valueOf(55100));

        layout.addView(gif);

        set.setTranslationX(gif.getId(), (metrics.widthPixels / 2) - 300);
        set.setTranslationY(gif.getId(), (metrics.heightPixels / 2) - 400);
        set.constrainHeight(gif.getId(), 600);
        set.constrainWidth(gif.getId(), 400);
        set.applyTo(layout);

        final TextView text = new TextView(this);
        text.setId(Integer.valueOf(55101));
        text.setText("Hola Juan, no me gusta esta vista :c");
        layout.addView(text);

        set.setTranslationX(text.getId(), (metrics.widthPixels / 2) - 220);
        set.setTranslationY(text.getId(), (metrics.heightPixels / 2) + 150);
        set.constrainHeight(text.getId(), 220);
        set.constrainWidth(text.getId(), metrics.widthPixels);
        set.applyTo(layout);
    }

    /**
     * Método para crear Toast con colores!
     *
     * Referencia: https://github.com/pranavpandey/dynamic-toasts
     *
     * @param text El texto a mostrar
     * @param duration La duraciíon del mensaje
     */
    private void error(String text, int duration) {
        DynamicToast.makeError(this, text, duration).show();
    }
}