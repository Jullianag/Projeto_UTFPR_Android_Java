package br.edu.utfpr.colecaojogovideogame;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.edu.utfpr.colecaojogovideogame.modelo.Jogo;
import br.edu.utfpr.colecaojogovideogame.modelo.TipoMidia;
import br.edu.utfpr.colecaojogovideogame.persistencia.JogosDatabase;
import br.edu.utfpr.colecaojogovideogame.utils.UtilsAlert;
import br.edu.utfpr.colecaojogovideogame.utils.UtilsLocalDate;

public class JogoActivity extends AppCompatActivity {

    /*public static final String KEY_NOME = "KEY_NOME";
    public static final String KEY_ANO = "KEY_ANO";
    public static final String KEY_CONSOLES = "KEY_CONSOLES";
    public static final String KEY_GENERO = "KEY_GENERO";
    public static final String KEY_TIPO_MIDIA = "KEY_TIPO_MIDIA";*/

    public static final String KEY_MODO = "MODO";
    public static final String KEY_ID = "ID";

    public static final String KEY_SUGERIR_GENERO = "SUGERIR_GENERO";
    public static final String KEY_ULTIMO_GENERO = "ULTIMO_GENERO";

    public static final int MODO_NOVO = 0;
    public static final int MODO_EDITAR = 1;

    private EditText editTextNome, editTextAno, editTextDataLancamento;
    private CheckBox checkBoxPlay, checkBoxXBox, checkBoxSwitch;
    private Spinner spinnerGenero;
    private RadioGroup radioGroupTipoMidia;
    private RadioButton radioButtonFisica, radioButtonDigital;

    private int modo;
    private Jogo jogoOriginal;

    private boolean sugerirGenero = false;
    private int ultimoGenero = 0;

    private LocalDate dataLancamento;
    private int anosParaTras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogo);

        editTextNome = findViewById(R.id.editTextNome);
        editTextAno = findViewById(R.id.editTextAno);
        editTextDataLancamento = findViewById(R.id.editTextDataLancamento);
        checkBoxPlay = findViewById(R.id.checkBoxPlay);
        checkBoxXBox = findViewById(R.id.checkBoxXBox);
        checkBoxSwitch = findViewById(R.id.checkBoxSwitch);
        radioGroupTipoMidia = findViewById(R.id.radioGroupTipoMidia);
        spinnerGenero = findViewById(R.id.spinnerGenero);
        radioButtonFisica = findViewById(R.id.radioButtonFisica);
        radioButtonDigital = findViewById(R.id.radioButtonDigital);

        editTextDataLancamento.setFocusable(false);

        editTextDataLancamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mostrarDatePickerDialog();
            }
        });

        lerPreferencias();
        anosParaTras = getResources().getInteger(R.integer.anos_para_tras);

        dataLancamento = LocalDate.now().minusYears(anosParaTras);

        Intent intentAbertuta = getIntent();

        Bundle bundle = intentAbertuta.getExtras();

        if (bundle != null) {

            modo = bundle.getInt(KEY_MODO);

            if (modo == MODO_NOVO) {
                setTitle(getString(R.string.novo_jogo));

                if (sugerirGenero) {

                    spinnerGenero.setSelection(ultimoGenero);

                }

            } else {
                setTitle(getString(R.string.editar_jogo));

                long id = bundle.getLong(KEY_ID);

                JogosDatabase database = JogosDatabase.getInstance(this);

                jogoOriginal = database.getJogoDao().queryForId(id);

                /*String nome = bundle.getString(JogoActivity.KEY_NOME);
                int ano = bundle.getInt(JogoActivity.KEY_ANO);
                ArrayList<String> consoles = bundle.getStringArrayList(JogoActivity.KEY_CONSOLES);
                int genero = bundle.getInt(JogoActivity.KEY_GENERO);
                String tipoMidiaTexto = bundle.getString(JogoActivity.KEY_TIPO_MIDIA);

                boolean playstation = consoles != null && consoles.contains(getString(R.string.playstation));
                boolean xBox = consoles != null && consoles.contains(getString(R.string.xbox));
                boolean nintendoSwitch = consoles != null && consoles.contains(getString(R.string.nintendo_switch));

                jogoOriginal = new Jogo(nome, ano, playstation, xBox, nintendoSwitch, genero, tipoMidia); */

                editTextNome.setText(jogoOriginal.getNome());
                editTextAno.setText(String.valueOf(jogoOriginal.getAno()));

                if (jogoOriginal.getDataLancamento() != null) {
                    dataLancamento = jogoOriginal.getDataLancamento();
                }

                editTextDataLancamento.setText(UtilsLocalDate.formatLocalDate(dataLancamento));

                checkBoxPlay.setChecked(jogoOriginal.isPlaystation());
                checkBoxXBox.setChecked(jogoOriginal.isXbox());
                checkBoxSwitch.setChecked(jogoOriginal.isNintendoSwitch());
                spinnerGenero.setSelection(jogoOriginal.getGenero());

                TipoMidia tipoMidia = jogoOriginal.getTipoMidia();

                if (tipoMidia == TipoMidia.Fisica) {
                    radioButtonFisica.setChecked(true);

                } else {
                    if (tipoMidia == TipoMidia.Digital) {
                        radioButtonDigital.setChecked(true);
                    }
                }

                editTextNome.requestFocus();
                editTextNome.setSelection(editTextNome.getText().length());
            }
        }
    }

    private void mostrarDatePickerDialog() {

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                dataLancamento = LocalDate.of(year, month + 1, dayOfMonth);

                editTextDataLancamento.setText(UtilsLocalDate.formatLocalDate(dataLancamento));
            }
        };

        DatePickerDialog picker = new DatePickerDialog(this,
                listener,
                dataLancamento.getYear(),
                dataLancamento.getMonthValue() - 1,
                dataLancamento.getDayOfMonth());

        long dataMaximaMillis = UtilsLocalDate.toMillissegundos(LocalDate.now());
        picker.getDatePicker().setMaxDate(dataMaximaMillis);
        picker.show();
    }

    public void limparCampos() {

        final String nome = editTextNome.getText().toString();
        final String ano = editTextAno.getText().toString();
        final LocalDate dataLancamentoAnterior = dataLancamento;
        final boolean playstation = checkBoxPlay.isChecked();
        final boolean xBox = checkBoxXBox.isChecked();
        final boolean nintendoSwitch = checkBoxSwitch.isChecked();
        final int radioButtonId = radioGroupTipoMidia.getCheckedRadioButtonId();
        final int genero = spinnerGenero.getSelectedItemPosition();

        final ScrollView scrollView = findViewById(R.id.main);
        final View viewComFoco = scrollView.findFocus();

        editTextNome.setText(null);
        editTextAno.setText(null);

        editTextDataLancamento.setText(null);
        dataLancamento = LocalDate.now().minusYears(anosParaTras);

        checkBoxPlay.setChecked(false);
        checkBoxXBox.setChecked(false);
        checkBoxSwitch.setChecked(false);
        radioGroupTipoMidia.clearCheck();
        spinnerGenero.setSelection(0);

        editTextNome.requestFocus();

        Snackbar snackbar = Snackbar.make(scrollView,
                R.string.entradas_apagadas,
                Snackbar.LENGTH_LONG);

        snackbar.setAction(R.string.desfazer, new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                editTextNome.setText(nome);
                editTextAno.setText(ano);

                dataLancamento = dataLancamentoAnterior;
                editTextDataLancamento.setText(UtilsLocalDate.formatLocalDate(dataLancamento));

                checkBoxPlay.setChecked(playstation);
                checkBoxXBox.setChecked(xBox);
                checkBoxSwitch.setChecked(nintendoSwitch);

                if (radioButtonId == R.id.radioButtonFisica) {
                    radioButtonFisica.setChecked(true);

                } else {
                    if (radioButtonId == R.id.radioButtonDigital) {
                        radioButtonDigital.setChecked(true);
                    }
                }

                spinnerGenero.setSelection(genero);

                if (viewComFoco != null) {
                    viewComFoco.requestFocus();
                }
            }
        });

        snackbar.show();

    }

    public void salvarValores() {

        String nome = editTextNome.getText().toString();

        if (nome == null || nome.trim().isEmpty()) {

            UtilsAlert.mostrarAviso(this,
                    R.string.o_nome_nao_pode_ser_vazio);

            editTextNome.requestFocus();
            return;
        }

        nome = nome.trim();

        String anoString = editTextAno.getText().toString();

        if (anoString == null || anoString.trim().isEmpty()) {

            UtilsAlert.mostrarAviso(this,
                    R.string.o_ano_nao_pode_ser_vazio);

            editTextAno.requestFocus();
            return;
        }

        int ano = 0;

        try {
            ano = Integer.parseInt(anoString);

        } catch (NumberFormatException e) {

            UtilsAlert.mostrarAviso(this,
                    R.string.o_ano_deve_ser_um_numero_inteiro);

            editTextAno.requestFocus();
            editTextAno.setSelection(0, editTextAno.getText().toString().length());
            return;
        }

        int anoAtual;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            anoAtual = LocalDate.now().getYear();
        }
        else {
            // tive que usar o Calendar, pois o android nao aceitava o LocalDate
            anoAtual = Calendar.getInstance().get(Calendar.YEAR);
        }

        if (ano < 1972 || ano > anoAtual) {

            UtilsAlert.mostrarAviso(this,
                    R.string.o_ano_deve_estar_entre_1972_e_o_ano_atual);

            editTextAno.requestFocus();
            editTextAno.setSelection(0, editTextAno.getText().toString().length());
            return;
        }

        String dataLancamentoString = editTextDataLancamento.getText().toString();

        if (dataLancamentoString == null || dataLancamentoString.trim().isEmpty()) {
            UtilsAlert.mostrarAviso(this, R.string.faltou_entrar_com_data_lancamento);
            return;
        }

        int idade = UtilsLocalDate.diferencaEmAnosParaHoje(dataLancamento);

        if (idade < 0 || idade > 67) {
            UtilsAlert.mostrarAviso(this, R.string.idade_invalida);
            return;
        }

        boolean playstationConsole = checkBoxPlay.isChecked();
        boolean xBoxConsole = checkBoxXBox.isChecked();
        boolean switchConsole = checkBoxSwitch.isChecked();

        if (!playstationConsole && !xBoxConsole && !switchConsole) {

            UtilsAlert.mostrarAviso(this,
                    R.string.deve_ser_marcado_pelo_menos_um_console);

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
                UtilsAlert.mostrarAviso(this,
                        R.string.e_obrigatorio_o_tipo_de_midia);

                return;
            }
        }

        int genero = spinnerGenero.getSelectedItemPosition();

        if (genero == AdapterView.INVALID_POSITION) {

            UtilsAlert.mostrarAviso(this,
                    R.string.o_spinner_nao_possui_valores);

            return;
        }

        Jogo jogo = new Jogo(nome, ano, playstationConsole, xBoxConsole, switchConsole, genero, tipoMidia, dataLancamento);

        if (jogo.equals(jogoOriginal)) {

            setResult(JogoActivity.RESULT_CANCELED);
            finish();
            return;
        }

        Intent intentResposta = new Intent();

        JogosDatabase database = JogosDatabase.getInstance(this);

        if (modo == MODO_NOVO) {
            long novoId = database.getJogoDao().insert(jogo);

            if (novoId <= 0) {
                UtilsAlert.mostrarAviso(this, R.string.erro_ao_tentar_inserir);
                return;
            }

            jogo.setId(novoId);

        } else {

            jogo.setId(jogoOriginal.getId());

            int quantidadeAlterada = database.getJogoDao().update(jogo);

            if (quantidadeAlterada != 1) {
                UtilsAlert.mostrarAviso(this, R.string.erro_ao_tentar_alterar);
                return;
            }

        }

        salvarUltimoTipo(genero);

        intentResposta.putExtra(KEY_ID, jogo.getId());

        setResult(JogoActivity.RESULT_OK, intentResposta);

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.jogo_opcoes, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem item = menu.findItem(R.id.menuItemSugerirGenero);

        item.setChecked(sugerirGenero);

        return true;
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

                if (idMenuItem == R.id.menuItemSugerirGenero) {

                    boolean valor = !item.isChecked();

                    salvarSugerirTipo(valor);
                    item.setChecked(valor);

                    if (sugerirGenero) {
                        spinnerGenero.setSelection(ultimoGenero);
                    }

                    return true;
                } else {
                    return super.onOptionsItemSelected(item);
                }
            }
        }
    }

    private void lerPreferencias() {

        SharedPreferences shared = getSharedPreferences(JogosActivity.ARQUIVO_PREFERENCIAS,
                Context.MODE_PRIVATE);

        sugerirGenero = shared.getBoolean(KEY_SUGERIR_GENERO, sugerirGenero);
        ultimoGenero = shared.getInt(KEY_ULTIMO_GENERO, ultimoGenero);
    }

    private void salvarSugerirTipo(boolean novoValor) {

        SharedPreferences shared = getSharedPreferences(JogosActivity.ARQUIVO_PREFERENCIAS,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = shared.edit();

        editor.putBoolean(KEY_SUGERIR_GENERO, novoValor);

        editor.commit();

        sugerirGenero = novoValor;
    }

    private void salvarUltimoTipo(int novoValor) {

        SharedPreferences shared = getSharedPreferences(JogosActivity.ARQUIVO_PREFERENCIAS,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = shared.edit();

        editor.putInt(KEY_ULTIMO_GENERO, novoValor);

        editor.commit();

        ultimoGenero = novoValor;
    }
}