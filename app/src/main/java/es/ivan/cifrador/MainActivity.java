package es.ivan.cifrador;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.util.Arrays;

import es.ivan.cifrador.utils.Caesar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private final String letters = "abcdefghijklmnñopqrstuvwxyz";
    private boolean mayus = false;
    private boolean longClick = false;
    private final Caesar caesar = new Caesar();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final int orientation = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getRotation();

        if (orientation == Surface.ROTATION_90 || orientation == Surface.ROTATION_270) {
            this.rotationLayout();
        } else {
            this.normalLayout();
        }
    }

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

        // A-Z
        // i = 1 bc with i = 0 the A button will be alone
        //
        // [A]
        // [B] [C] [D] ...
        //
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

        set.setTranslationX(encrypt.getId(), xPos + 5);
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

        set.setTranslationX(decrypt.getId(), xPos + (3 * buttonSize + 5));
        set.setTranslationY(decrypt.getId(), metrics.heightPixels - (buttonSize * 2 - 5));
        set.constrainHeight(decrypt.getId(), buttonSize);
        set.constrainWidth(decrypt.getId(), buttonSize * 2);
        set.applyTo(layout);
    }

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
                if (this.mayus) {
                    v.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                } else {
                    v.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_ATOP);
                }
                this.updateLetterButton();
                break;
            case 31:
                cifrado.setText(this.caesar.encrypt((String) noCifrado.getText(), 4));
                break;
            case 32:
                cifrado.setText(this.caesar.decrypt((String) noCifrado.getText(), 4));
                break;
            case 33:
                if (noCifrado.getText().toString().toCharArray().length <= 0) break;
                noCifrado.setText(String.valueOf(Arrays.copyOf(noCifrado.getText().toString().toCharArray(), noCifrado.getText().toString().toCharArray().length - 1)));
                break;
            case 34:
                noCifrado.setText(noCifrado.getText() + " ");
                break;
            default:
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

    private void updateLetterButton() {
        final boolean newAPI = letters.split("")[1].equalsIgnoreCase("b");
        for (int i = newAPI ? 0 : 1; i <= (newAPI ? this.letters.length() - 1 : this.letters.length()); i++) {
            final Button button = findViewById(i);
            button.setAllCaps(this.mayus);
        }
    }

    private void rotationLayout() {
        final ConstraintLayout layout = findViewById(R.id.root);

    }
}