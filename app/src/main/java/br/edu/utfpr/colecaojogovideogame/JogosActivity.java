package br.edu.utfpr.colecaojogovideogame;

import android.os.Bundle;
import android.widget.ListView;

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

        listViewJogos = findViewById(R.id.listViewJogos);

        popularListaJogos();
    }

    private void popularListaJogos() {

        String[] jogos_nomes = getResources().getStringArray(R.array.jogos_nome);
        int[] jogos_anos = getResources().getIntArray(R.array.jogos_anos);
        int[] jogos_playstation = getResources().getIntArray(R.array.jogos_consoles_playstation);
        int[] jogos_xbox = getResources().getIntArray(R.array.jogos_consoles_xbox);
        int[] jogos_switch = getResources().getIntArray(R.array.jogos_consoles_switch);
        int[] jogos_generos = getResources().getIntArray(R.array.jogos_generos);
        int[] jogos_tipos_midias = getResources().getIntArray(R.array.jogos_tipos_midias);

        listaJogos = new ArrayList<>();

        Jogo jogo;
        boolean playstation;
        boolean xbox;
        boolean nintendoSwitch;
        TipoMidia tipoMidia;

        TipoMidia[] tiposMidias = TipoMidia.values();

        for (int i = 0; i < jogos_nomes.length; i++) {

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
        }

        jogoAdapter = new JogoAdapter(this, listaJogos);

        listViewJogos.setAdapter(jogoAdapter);
    }
}