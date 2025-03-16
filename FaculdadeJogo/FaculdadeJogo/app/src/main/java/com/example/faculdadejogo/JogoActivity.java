package com.example.faculdadejogo;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import java.util.Random;

public class JogoActivity extends AppCompatActivity {

    private TextView tvEquacao, tvResultado, tvDificuldadeSelecionada;
    private EditText etResposta;
    private Button btnVerificar;
    private String dificuldadeSelecionada;
    private int respostaCorreta;
    private Random random = new Random();
    private MediaPlayer mediaPlayer; // MediaPlayer para a música

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogo);

        Intent intent = getIntent();
        dificuldadeSelecionada = intent.getStringExtra("DIFICULDADE");

        tvEquacao = findViewById(R.id.tvEquacao);
        tvResultado = findViewById(R.id.tvResultado);
        tvDificuldadeSelecionada = findViewById(R.id.tvDificuldadeSelecionada);
        etResposta = findViewById(R.id.etResposta);
        btnVerificar = findViewById(R.id.btnVerificar);

        tvDificuldadeSelecionada.setText("Dificuldade: " + dificuldadeSelecionada);

        gerarNovaEquacao();


        mediaPlayer = MediaPlayer.create(this, R.raw.original);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        btnVerificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarResposta();
            }
        });

        Button btnVoltarJogo = findViewById(R.id.btnVoltarJogo);
        btnVoltarJogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JogoActivity.this, InicioActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void gerarNovaEquacao() {
        int limite = definirLimite(dificuldadeSelecionada);
        int num1 = random.nextInt(limite) + 1;
        int num2 = random.nextInt(limite) + 1;
        char operador;

        operador = new char[]{'+', '*', '-'}[random.nextInt(3)];

        if (operador == '-') {
            if (num1 < num2) {
                int temp = num1;
                num1 = num2;
                num2 = temp;
            }
        }

        tvEquacao.setText(num1 + " " + operador + " " + num2 + " = ?");
        respostaCorreta = calcularResultado(num1, num2, operador);
    }

    private int definirLimite(String dificuldade) {
        switch (dificuldade) {
            case "Fácil":
                return 15;
            case "Médio":
                return 50;
            case "Difícil":
                return 100;
            default:
                return 15;
        }
    }

    private int calcularResultado(int num1, int num2, char operador) {
        switch (operador) {
            case '+':
                return num1 + num2;
            case '-':
                return num1 - num2;
            case '*':
                return num1 * num2;
            default:
                return 0;
        }
    }

    private void verificarResposta() {
        String respostaUsuario = etResposta.getText().toString();

        if (!respostaUsuario.isEmpty()) {
            int resposta = Integer.parseInt(respostaUsuario);
            if (resposta == respostaCorreta) {
                tvResultado.setText("Correto!");
                tvResultado.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark));
            } else {
                tvResultado.setText("Errado! Resposta: " + respostaCorreta);
                tvResultado.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));

                LinearLayout layout = findViewById(R.id.layoutJogo);
                Animation tremor = AnimationUtils.loadAnimation(JogoActivity.this, R.anim.tremor);
                layout.startAnimation(tremor);

                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                if (vibrator != null) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        vibrator.vibrate(1000);
                    }
                }
            }
            etResposta.setText("");
            gerarNovaEquacao();
        }
    }
}
