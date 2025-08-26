package br.edu.utfpr.colecaojogovideogame;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
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

    public void abrirSobre() {

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

                            jogoRecyclerViewAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });

    public void abrirNovoJogo() {

        Intent intentAbertura = new Intent(this, JogoActivity.class);

        launcherNovoJogo.launch(intentAbertura);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.jogos_opcoes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int idMenuItem = item.getItemId();

        if (idMenuItem == R.id.menuItemAdicionar) {
            abrirNovoJogo();
            return true;

        } else {
            if (idMenuItem == R.id.menuItemSobre) {
                abrirSobre();
                return  true;

            } else {
                return super.onOptionsItemSelected(item);
            }
        }
    }
}