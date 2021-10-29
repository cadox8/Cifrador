package es.ivan.cifrador;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
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

    private final String letters = " abcdefghijklmnñopqrstuvwxyz";
    private boolean mayus = false;
    private boolean longClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.createLayout();
    }

    private void createLayout() {
        final DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        final int buttonWidth = metrics.widthPixels / 6;
        final int buttonHeight = buttonWidth;

        final int xPos = buttonWidth / 2;
        final int yPos = 300;

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
        for (int i = 1; i < this.letters.length(); i++) {
            final Button button = new Button(this);
            button.setText(letters.split("")[i]);
            button.setId(i);
            button.setAllCaps(false);

            button.setOnClickListener(this);

            layout.addView(button);

            set.setTranslationX(button.getId(), xPos + (columns * buttonWidth + 5));
            set.setTranslationY(button.getId(), yPos + (rows * buttonHeight + 5));
            set.constrainHeight(button.getId(), buttonHeight);
            set.constrainWidth(button.getId(), buttonWidth);
            set.applyTo(layout);

            columns++;
            if (i % 5 == 0) {
                rows++;
                columns = 0;
            }
        }

        // Mayus
        final ImageButton mayus = new ImageButton(this);
        mayus.setImageResource(R.drawable.mayus);
        mayus.setId(Integer.valueOf(30));
        mayus.setScaleType(ImageView.ScaleType.FIT_CENTER);

        mayus.setOnClickListener(this);
        mayus.setOnLongClickListener(this);

        layout.addView(mayus);

        set.setTranslationX(mayus.getId(), xPos + (4 * buttonWidth + 5));
        set.setTranslationY(mayus.getId(), yPos + (5 * buttonHeight + 5));
        set.constrainHeight(mayus.getId(), buttonHeight);
        set.constrainWidth(mayus.getId(), buttonWidth);
        set.applyTo(layout);

        // Delete
        final ImageButton remove = new ImageButton(this);
        remove.setImageResource(R.drawable.rem);
        remove.setId(Integer.valueOf(33));
        remove.setScaleType(ImageView.ScaleType.FIT_CENTER);

        remove.setOnClickListener(this);

        layout.addView(remove);

        set.setTranslationX(remove.getId(), xPos + (3 * buttonWidth + 5));
        set.setTranslationY(remove.getId(), yPos + (5 * buttonHeight + 5));
        set.constrainHeight(remove.getId(), buttonHeight);
        set.constrainWidth(remove.getId(), buttonWidth);
        set.applyTo(layout);

        // Encrypt
        final Button encrypt = new Button(this);
        encrypt.setText("Encriptar");
        encrypt.setId(Integer.valueOf(31));

        encrypt.setOnClickListener(this);

        layout.addView(encrypt);

        set.setTranslationX(encrypt.getId(), xPos + (buttonWidth + 5));
        set.setTranslationY(encrypt.getId(), metrics.heightPixels - (buttonHeight * 2 - 5));
        set.constrainHeight(encrypt.getId(), buttonHeight);
        set.constrainWidth(encrypt.getId(), buttonWidth + 50);
        set.applyTo(layout);

        // Decrypt
        final Button decrypt = new Button(this);
        decrypt.setText("Desencriptar");
        decrypt.setId(Integer.valueOf(32));

        decrypt.setOnClickListener(this);

        layout.addView(decrypt);

        set.setTranslationX(decrypt.getId(), xPos + (3 * buttonWidth + 5));
        set.setTranslationY(decrypt.getId(), metrics.heightPixels - (buttonHeight * 2 - 5));
        set.constrainHeight(decrypt.getId(), buttonHeight);
        set.constrainWidth(decrypt.getId(), buttonWidth + 50);
        set.applyTo(layout);
    }

    @Override
    public void onClick(View v) {
        final Caesar caesar = new Caesar();
        switch (v.getId()) {
            case 30:
                if (this.longClick) {
                    this.longClick = false;
                    v.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_ATOP);
                }
                this.mayus = !this.mayus;
                this.updateLetterButton();
                break;
            case 31:
                final TextView noCifrado = findViewById(R.id.noCifrado);
                final TextView cifrado = findViewById(R.id.cifrado);

                cifrado.setText(caesar.encrypt((String) noCifrado.getText(), 4));
                break;
            case 32:
                final TextView noCifrado2 = findViewById(R.id.noCifrado);
                final TextView cifrado2 = findViewById(R.id.cifrado);

                cifrado2.setText(caesar.decrypt((String) noCifrado2.getText(), 4));
                break;
            case 33:
                final TextView noCi = findViewById(R.id.noCifrado);
                if (noCi.getText().toString().toCharArray().length <= 0) break;
                noCi.setText(String.valueOf(Arrays.copyOf(noCi.getText().toString().toCharArray(), noCi.getText().toString().toCharArray().length - 1)));
                break;
            default:
                final TextView text = findViewById(R.id.noCifrado);
                String letter = letters.split("")[v.getId()];
                if (this.mayus) letter = letter.toUpperCase();
                text.setText(text.getText() + letter);

                if (!this.longClick) {
                    this.mayus = false;
                    this.updateLetterButton();
                }
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
        for (int i = 1; i < this.letters.length(); i++) {
            final Button button = findViewById(i);
            button.setAllCaps(this.mayus);
        }
    }
}