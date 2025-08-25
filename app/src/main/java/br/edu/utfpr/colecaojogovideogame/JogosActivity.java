package br.edu.utfpr.colecaojogovideogame;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class JogosActivity extends AppCompatActivity {

    private RecyclerView recyclerViewJogos;
    private RecyclerView.LayoutManager layoutManager;
    private JogoRecyclerViewAdapter jogoRecyclerViewAdapter;
    private JogoRecyclerViewAdapter.OnItemClickListener onItemClickListener;

    private List<Jogo> listaJogos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogos);

        setTitle(getString(R.string.controle_de_jogos));

        recyclerViewJogos = findViewById(R.id.recyclerViewJogos);

        layoutManager = new LinearLayoutManager(this);

        recyclerViewJogos.setLayoutManager(layoutManager);
        recyclerViewJogos.setHasFixedSize(true);
        recyclerViewJogos.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));

        onItemClickListener = new JogoRecyclerViewAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {

                Jogo jogo = listaJogos.get(position);

                Toast.makeText(getApplicationContext(),
                        getString(R.string.o_jogo) + jogo.getNome() + getString(R.string.foi_clicado),
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {

                Jogo jogo = listaJogos.get(position);

                Toast.makeText(getApplicationContext(),
                        getString(R.string.o_jogo) + jogo.getNome() + getString(R.string.recebeu_um_click_longo),
                        Toast.LENGTH_LONG).show();
            }
        };

        popularListaJogos();
    }

    private void popularListaJogos() {

        listaJogos = new ArrayList<>();

        Jogo jogo;
        boolean playstation;
        boolean xbox;
        boolean nintendoSwitch;
        TipoMidia tipoMidia;

        TipoMidia[] tiposMidias = TipoMidia.values();

        jogoRecyclerViewAdapter = new JogoRecyclerViewAdapter(this, listaJogos, onItemClickListener);

        recyclerViewJogos.setAdapter(jogoRecyclerViewAdapter);
    }

    /*public void abrirSobre(View view) {

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
    }*/
}