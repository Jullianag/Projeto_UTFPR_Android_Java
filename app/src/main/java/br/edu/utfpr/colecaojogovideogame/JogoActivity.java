package br.edu.utfpr.colecaojogovideogame;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class JogoActivity extends AppCompatActivity {

    public static final String KEY_NOME = "KEY_NOME";
    public static final String KEY_ANO = "KEY_ANO";
    public static final String KEY_CONSOLES = "KEY_CONSOLES";
    public static final String KEY_GENERO = "KEY_GENERO";
    public static final String KEY_TIPO_MIDIA = "KEY_TIPO_MIDIA";

    private EditText editTextNome, editTextAno;
    private CheckBox checkBoxPlay, checkBoxXBox, checkBoxSwitch;
    private RadioGroup radioGroupTipoMidia;

    private Spinner spinnerGenero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogo);

        setTitle(getString(R.string.novo_jogo));

        editTextNome = findViewById(R.id.editTextNome);
        editTextAno = findViewById(R.id.editTextAno);
        checkBoxPlay = findViewById(R.id.checkBoxPlay);
        checkBoxXBox = findViewById(R.id.checkBoxXBox);
        checkBoxSwitch = findViewById(R.id.checkBoxSwitch);
        radioGroupTipoMidia = findViewById(R.id.radioGroupTipoMidia);
        spinnerGenero = findViewById(R.id.spinnerGenero);
    }

    public void limparCampos(View view) {

        editTextNome.setText(null);
        editTextAno.setText(null);
        checkBoxPlay.setChecked(false);
        checkBoxXBox.setChecked(false);
        checkBoxSwitch.setChecked(false);
        radioGroupTipoMidia.clearCheck();
        spinnerGenero.setSelection(0);

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

        boolean playstationConsole = checkBoxPlay.isChecked();
        boolean xBoxConsole = checkBoxXBox.isChecked();
        boolean switchConsole = checkBoxSwitch.isChecked();

        if (!playstationConsole && !xBoxConsole && !switchConsole) {
            Toast.makeText(this,
                    R.string.deve_ser_marcado_pelo_menos_um_console,
                    Toast.LENGTH_LONG).show();
            return;
        }

        List<String> consoles = new ArrayList<>();

        if (playstationConsole) {
            consoles.add(getString(R.string.playstation));
        }
        if (xBoxConsole) {
            consoles.add(getString(R.string.xbox));
        }
        if (switchConsole) {
            consoles.add(getString(R.string.nintendo_switch));
        }

        int radioButtonId = radioGroupTipoMidia.getCheckedRadioButtonId();

        TipoMidia tipoMidia;

        if (radioButtonId == R.id.radioButtonFisica) {
            tipoMidia = TipoMidia.Fisica;

        } else {

            if (radioButtonId == R.id.radioButtonDigital) {
                tipoMidia = TipoMidia.Digital;
            } else {
                Toast.makeText(this,
                        R.string.e_obrigatorio_o_tipo_de_midia,
                        Toast.LENGTH_LONG).show();

                return;
            }
        }

        int genero = spinnerGenero.getSelectedItemPosition();

        if (genero == AdapterView.INVALID_POSITION) {
            Toast.makeText(this,
                    R.string.o_spinner_nao_possui_valores,
                    Toast.LENGTH_LONG).show();

            return;
        }

        Intent intentResposta = new Intent();

        intentResposta.putExtra(KEY_NOME, nome);
        intentResposta.putExtra(KEY_ANO, ano);
        intentResposta.putExtra(KEY_CONSOLES, new ArrayList<>(consoles));
        intentResposta.putExtra(KEY_GENERO, genero);
        intentResposta.putExtra(KEY_TIPO_MIDIA, tipoMidia.toString());

        setResult(JogoActivity.RESULT_OK, intentResposta);

        finish();
    }
}