package com.example.faculdadejogo;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView tvEquacao, tvResultado, tvDificuldadeSelecionada;
    private EditText etResposta;
    private Button btnVerificar, btnIniciar;
    private Spinner spinnerDificuldade;
    private String dificuldadeSelecionada = "Fácil";
    private int respostaCorreta;
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvEquacao = findViewById(R.id.tvEquacao);
        tvResultado = findViewById(R.id.tvResultado);
        tvDificuldadeSelecionada = findViewById(R.id.tvDificuldadeSelecionada);
        etResposta = findViewById(R.id.etResposta);
        btnVerificar = findViewById(R.id.btnVerificar);
        btnIniciar = findViewById(R.id.btnIniciar);
        spinnerDificuldade = findViewById(R.id.spinnerDificuldade);

        spinnerDificuldade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dificuldadeSelecionada = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDificuldadeSelecionada.setText("Dificuldade: " + dificuldadeSelecionada);
                gerarNovaEquacao();
            }
        });

        btnVerificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarResposta();
            }
        });

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
            case "Fácil": return 15;
            case "Médio": return 50;
            case "Difícil": return 100;
            default: return 15;
        }
    }

    private int calcularResultado(int num1, int num2, char operador) {
        switch (operador) {
            case '+': return num1 + num2;
            case '-': return num1 - num2;
            case '*': return num1 * num2;
            case '/': return (num2 != 0) ? num1 / num2 : 0;
            default: return 0;
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
            }
            etResposta.setText("");
            gerarNovaEquacao();
        }
    }
}
