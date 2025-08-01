package br.edu.utfpr.colecaojogovideogame;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.util.Calendar;

public class JogoActivity extends AppCompatActivity {

    private EditText editTextNome, editTextAno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogo);

        editTextNome = findViewById(R.id.editTextNome);
        editTextAno = findViewById(R.id.editTextAno);
    }

    public void limparCampos(View view) {

        editTextNome.setText(null);
        editTextAno.setText(null);

        editTextNome.requestFocus();

        Toast.makeText(this,
                R.string.entradas_apagadas,
                Toast.LENGTH_LONG).show();
    }

    public void salvarValores(View view) {

        String nome = editTextNome.getText().toString();

        if (nome == null || nome.trim().isEmpty()) {

            Toast.makeText(this,
                    R.string.o_nome_nao_pode_ser_vazio,
                    Toast.LENGTH_LONG).show();

            editTextNome.requestFocus();
            return;
        }

        nome = nome.trim();

        String anoString = editTextAno.getText().toString();

        if (anoString == null || anoString.trim().isEmpty()) {

            Toast.makeText(this,
                    R.string.o_ano_nao_pode_ser_vazio,
                    Toast.LENGTH_LONG).show();

            editTextAno.requestFocus();
            return;
        }

        int ano = 0;

        try {
            ano = Integer.parseInt(anoString);

        } catch (NumberFormatException e) {

            Toast.makeText(this,
                    R.string.o_ano_deve_ser_um_numero_inteiro,
                    Toast.LENGTH_LONG).show();

            editTextAno.requestFocus();
            editTextAno.setSelection(0, editTextAno.getText().toString().length());
            return;
        }

        int anoAtual;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            anoAtual = LocalDate.now().getYear();
        }
        else {
            // tive que usar o Calendar, pois o andoid nao aceitava o LocalDate
            anoAtual = Calendar.getInstance().get(Calendar.YEAR);
        }

        if (ano < 1972 || ano > anoAtual) {

            Toast.makeText(this,
                    R.string.o_ano_deve_estar_entre_1972_e_o_ano_atual,
                    Toast.LENGTH_LONG).show();

            editTextAno.requestFocus();
            editTextAno.setSelection(0, editTextAno.getText().toString().length());
            return;
        }

        Toast.makeText(this,
                getString(R.string.jogo_valor) + nome + "\n" +
                getString(R.string.ano_valor) + ano,
                Toast.LENGTH_LONG).show();
    }
}