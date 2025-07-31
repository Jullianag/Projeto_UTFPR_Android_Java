package br.edu.utfpr.colecaojogovideogame;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextNome, editTextAno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        ano = Integer.parseInt(anoString);

        Toast.makeText(this,
                getString(R.string.jogo_valor) + nome + "\n" +
                getString(R.string.ano_valor) + ano,
                Toast.LENGTH_LONG).show();
    }
}