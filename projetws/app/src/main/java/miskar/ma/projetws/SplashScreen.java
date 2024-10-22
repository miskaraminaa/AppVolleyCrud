package miskar.ma.projetws;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    private ImageView logo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logo = findViewById(R.id.logo);

        logo.animate()
                .alpha(0f) // Animation pour faire disparaître le logo
                .setDuration(3000)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        // Après l'animation, passer à l'activité principale
                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(intent);
                        SplashScreen.this.finish();
                    }
                })
                .start();


    }

}