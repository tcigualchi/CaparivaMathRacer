package com.example.faculdadejogo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.media.AudioManager;
import android.media.MediaPlayer;

public class InicioActivity extends AppCompatActivity {

    private Button btnFacil, btnMedio, btnDificil;
    MediaPlayer mediaPlayer;

    private void playMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(InicioActivity.this, R.raw.original); // Ensure mainsong.mp3 is in res/raw/
        mediaPlayer.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        btnFacil = findViewById(R.id.btnFacil);
        btnMedio = findViewById(R.id.btnMedio);
        btnDificil = findViewById(R.id.btnDificil);

        btnFacil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarJogo("Fácil");
            }
        });

        btnMedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iniciarJogo("Médio");
            }
        });

        btnDificil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iniciarJogo("Difícil");
            }
        });
    }

    private void iniciarJogo(String dificuldade) {
        Intent intent = new Intent(InicioActivity.this, JogoActivity.class);
        intent.putExtra("DIFICULDADE", dificuldade);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
