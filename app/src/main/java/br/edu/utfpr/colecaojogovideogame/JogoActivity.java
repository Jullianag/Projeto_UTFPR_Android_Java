package br.edu.utfpr.colecaojogovideogame;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
    public static final String KEY_MODO = "MODO";

    public static final int MODO_NOVO = 0;
    public static final int MODO_EDITAR = 1;

    private EditText editTextNome, editTextAno;
    private CheckBox checkBoxPlay, checkBoxXBox, checkBoxSwitch;
    private Spinner spinnerGenero;
    private RadioGroup radioGroupTipoMidia;
    private RadioButton radioButtonFisica, radioButtonDigital;

    private int modo;
    private Jogo jogoOriginal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogo);

        editTextNome = findViewById(R.id.editTextNome);
        editTextAno = findViewById(R.id.editTextAno);
        checkBoxPlay = findViewById(R.id.checkBoxPlay);
        checkBoxXBox = findViewById(R.id.checkBoxXBox);
        checkBoxSwitch = findViewById(R.id.checkBoxSwitch);
        radioGroupTipoMidia = findViewById(R.id.radioGroupTipoMidia);
        spinnerGenero = findViewById(R.id.spinnerGenero);
        radioButtonFisica = findViewById(R.id.radioButtonFisica);
        radioButtonDigital = findViewById(R.id.radioButtonDigital);

        Intent intentAbertuta = getIntent();

        Bundle bundle = intentAbertuta.getExtras();

        if (bundle != null) {

            modo = bundle.getInt(KEY_MODO);

            if (modo == MODO_NOVO) {
                setTitle(getString(R.string.novo_jogo));
            } else {
                setTitle(getString(R.string.editar_jogo));

                String nome = bundle.getString(JogoActivity.KEY_NOME);
                int ano = bundle.getInt(JogoActivity.KEY_ANO);
                ArrayList<String> consoles = bundle.getStringArrayList(JogoActivity.KEY_CONSOLES);
                int genero = bundle.getInt(JogoActivity.KEY_GENERO);
                String tipoMidiaTexto = bundle.getString(JogoActivity.KEY_TIPO_MIDIA);

                boolean playstation = consoles != null && consoles.contains(getString(R.string.playstation));
                boolean xBox = consoles != null && consoles.contains(getString(R.string.xbox));
                boolean nintendoSwitch = consoles != null && consoles.contains(getString(R.string.nintendo_switch));

                TipoMidia tipoMidia = TipoMidia.valueOf(tipoMidiaTexto);

                jogoOriginal = new Jogo(nome, ano, playstation, xBox, nintendoSwitch, genero, tipoMidia);

                editTextNome.setText(nome);
                editTextAno.setText(String.valueOf(ano));
                checkBoxPlay.setChecked(playstation);
                checkBoxXBox.setChecked(xBox);
                checkBoxSwitch.setChecked(nintendoSwitch);
                spinnerGenero.setSelection(genero);

                if (tipoMidia == TipoMidia.Fisica) {
                    radioButtonFisica.setChecked(true);

                } else {
                    if (tipoMidia == TipoMidia.Digital) {
                        radioButtonDigital.setChecked(true);
                    }
                }

            }
        }
    }

    public void limparCampos() {

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

    public void salvarValores() {

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

        if (modo == MODO_EDITAR &&
                nome.equalsIgnoreCase(jogoOriginal.getNome()) &&
                ano == jogoOriginal.getAno() &&
                playstationConsole == jogoOriginal.isPlaystation() &&
                xBoxConsole == jogoOriginal.isXbox() &&
                switchConsole == jogoOriginal.isNintendoSwitch() &&
                genero == jogoOriginal.getGenero() &&
                tipoMidia == jogoOriginal.getTipoMidia()) {

            setResult(JogoActivity.RESULT_CANCELED);
            finish();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.jogo_opcoes, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int idMenuItem = item.getItemId();

        if (idMenuItem == R.id.menuItemSalvar) {

            salvarValores();
            return true;
        } else {
            if (idMenuItem == R.id.menuItemLimpar) {

                limparCampos();
                return true;
            } else {
                return super.onOptionsItemSelected(item);
            }
        }
    }
}