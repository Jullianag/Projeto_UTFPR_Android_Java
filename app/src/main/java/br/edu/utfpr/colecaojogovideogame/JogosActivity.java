package br.edu.utfpr.colecaojogovideogame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class JogosActivity extends AppCompatActivity {

    private ListView listViewJogos;

    private List<Jogo> listaJogos;

    private JogoAdapter jogoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogos);

        setTitle(getString(R.string.controle_de_jogos));

        listViewJogos = findViewById(R.id.listViewJogos);

        listViewJogos.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Jogo jogo = (Jogo) listViewJogos.getItemAtPosition(position);

                Toast.makeText(getApplicationContext(),
                        getString(R.string.o_jogo) + jogo.getNome() + getString(R.string.foi_clicado),
                        Toast.LENGTH_LONG).show();
            }
        });

        popularListaJogos();
    }

    private void popularListaJogos() {

        /*String[] jogos_nomes = getResources().getStringArray(R.array.jogos_nome);
        int[] jogos_anos = getResources().getIntArray(R.array.jogos_anos);
        int[] jogos_playstation = getResources().getIntArray(R.array.jogos_consoles_playstation);
        int[] jogos_xbox = getResources().getIntArray(R.array.jogos_consoles_xbox);
        int[] jogos_switch = getResources().getIntArray(R.array.jogos_consoles_switch);
        int[] jogos_generos = getResources().getIntArray(R.array.jogos_generos);
        int[] jogos_tipos_midias = getResources().getIntArray(R.array.jogos_tipos_midias);*/

        listaJogos = new ArrayList<>();

        Jogo jogo;
        boolean playstation;
        boolean xbox;
        boolean nintendoSwitch;
        TipoMidia tipoMidia;

        TipoMidia[] tiposMidias = TipoMidia.values();

        /*for (int i = 0; i < jogos_nomes.length; i++) {

            playstation = jogos_playstation[i] == 1 ? true : false;
            xbox = jogos_xbox[i] == 1 ? true : false;
            nintendoSwitch = jogos_switch[i] == 1 ? true : false;

            tipoMidia = tiposMidias[jogos_tipos_midias[i]];

            jogo = new Jogo(jogos_nomes[i],
                    jogos_anos[i],
                    playstation,
                    xbox,
                    nintendoSwitch,
                    jogos_generos[i],
                    tipoMidia);

            listaJogos.add(jogo);
        }*/

        jogoAdapter = new JogoAdapter(this, listaJogos);

        listViewJogos.setAdapter(jogoAdapter);
    }

    public void abrirSobre(View view) {

        Intent intentAbertura = new Intent(this, SobreActivity.class);

        startActivity(intentAbertura);

    }

    ActivityResultLauncher<Intent> launcherNovoJogo = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == JogosActivity.RESULT_OK) {

                        Intent intent = result.getData();

                        Bundle bundle = intent.getExtras();

                        if (bundle != null) {

                            String nome = bundle.getString(JogoActivity.KEY_NOME);
                            int ano = bundle.getInt(JogoActivity.KEY_ANO);
                            ArrayList<String> consoles = bundle.getStringArrayList(JogoActivity.KEY_CONSOLES);
                            int genero = bundle.getInt(JogoActivity.KEY_GENERO);
                            String tipoMidiaTexto = bundle.getString(JogoActivity.KEY_TIPO_MIDIA);

                            boolean playstation = consoles != null && consoles.contains(getString(R.string.playstation));
                            boolean xBox = consoles != null && consoles.contains(getString(R.string.xbox));
                            boolean nintendoSwitch = consoles != null && consoles.contains(getString(R.string.nintendo_switch));

                            Jogo jogo = new Jogo(nome, ano, playstation, xBox, nintendoSwitch,
                                    genero, TipoMidia.valueOf(tipoMidiaTexto));

                            listaJogos.add(jogo);

                            jogoAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });

    public void abrirNovoJogo(View view) {

        Intent intentAbertura = new Intent(this, JogoActivity.class);

        launcherNovoJogo.launch(intentAbertura);
    }
}