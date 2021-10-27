package es.ivan.cifrador;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.createLayout();
    }

    private void createLayout() {
        final String letters = "abcdefghijklmnñopqrstuvwxyz";
        final int xPos = 1;
        final int yPos = 150;

        int rows = 0;

        final ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.root);

        for (int i = 0; i <= 27; i++) {
            final Button button = new Button(this);
            button.setText(letters.split("")[i]);
            //button.setPadding(xPos, yPos + (rows * 50 + 2), 0, 0);
            button.layout(xPos, yPos + (rows * 50 + 2), 0, 0);
            button.setVisibility(View.VISIBLE);
            button.setEnabled(true);
            button.setId(i);

            layout.addView(button);

            if (i % 8 == 0) rows++;
        }
    }
}