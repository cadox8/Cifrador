package es.ivan.cifrador;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import es.ivan.cifrador.utils.Caesar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private final String letters = "abcdefghijklmnñopqrstuvwxyz";
    private boolean mayus = false;
    private boolean longClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.createLayout();
    }

    private void createLayout() {
        final int xPos = 1;
        final int yPos = 150;

        int columns = 0;
        int rows = 0;

        final ConstraintLayout layout = findViewById(R.id.root);
        final ConstraintSet set = new ConstraintSet();
        set.clone(layout);

        // A-Z
        for (int i = 0; i < 27; i++) {
            final Button button = new Button(this);
            button.setText(letters.split("")[i + 1]);
            button.setId(i + 1);
            button.setAllCaps(false);
            
            button.setOnClickListener(this);

            layout.addView(button);

            set.setTranslationX(button.getId(), xPos + (columns * 86 + 2));
            set.setTranslationY(button.getId(), yPos + (rows * 67 + 2));
            set.constrainHeight(button.getId(), 80);
            set.constrainWidth(button.getId(), 70);
            set.applyTo(layout);

            columns++;
            if (i % 5 == 0) {
                rows++;
                columns = 0;
            }
        }

        // Mayus
        final Button mayus = new Button(this);
        mayus.setText("Mayus");
        mayus.setId(Integer.valueOf(30));

        mayus.setOnClickListener(this);
        mayus.setOnLongClickListener(this);

        layout.addView(mayus);

        set.setTranslationX(mayus.getId(), xPos + (86 + 2));
        set.setTranslationY(mayus.getId(), yPos + (5 * 67 + 2));
        set.constrainHeight(mayus.getId(), 80);
        set.constrainWidth(mayus.getId(), 70);
        set.applyTo(layout);

        // Encrypt
        final Button encrypt = new Button(this);
        encrypt.setText("Encriptar");
        encrypt.setId(Integer.valueOf(31));

        encrypt.setOnClickListener(this);
        encrypt.setOnLongClickListener(this);

        layout.addView(encrypt);

        set.setTranslationX(encrypt.getId(), xPos + (86 + 2));
        set.setTranslationY(encrypt.getId(), yPos + (7 * 67 + 2));
        set.constrainHeight(encrypt.getId(), 80);
        set.constrainWidth(encrypt.getId(), 70);
        set.applyTo(layout);

        // Decrypt
        final Button decrypt = new Button(this);
        decrypt.setText("Desencriptar");
        decrypt.setId(Integer.valueOf(32));

        decrypt.setOnClickListener(this);
        decrypt.setOnLongClickListener(this);

        layout.addView(decrypt);

        set.setTranslationX(decrypt.getId(), xPos + (2 * 150 + 2));
        set.setTranslationY(decrypt.getId(), yPos + (7 * 67 + 2));
        set.constrainHeight(decrypt.getId(), 80);
        set.constrainWidth(decrypt.getId(), 150);
        set.applyTo(layout);
    }

    @Override
    public void onClick(View v) {
        final Caesar caesar = new Caesar();
        switch (v.getId()) {
            case 30:
                if (this.longClick) this.updateLetterButton();
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

                noCifrado2.setText(caesar.decrypt((String) cifrado2.getText(), 4));
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
        return true;
    }

    private void updateLetterButton() {
        for (int i = 1; i < 27; i++) {
            final Button button = findViewById(i);
            button.setAllCaps(this.mayus);
        }
    }
}