package br.edu.utfpr.colecaojogovideogame;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

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

    private List<Jogo> listaJogos;

    private int posicaoSelecionada = -1;

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

        jogoRecyclerViewAdapter = new JogoRecyclerViewAdapter(this, listaJogos);

        jogoRecyclerViewAdapter.setOnCreateContextMenu(new JogoRecyclerViewAdapter.OnCreateContextMenu() {

            @Override
            public void onCreateContextMenu(ContextMenu menu,
                                            View v, ContextMenu.ContextMenuInfo menuInfo,
                                            int position,
                                            MenuItem.OnMenuItemClickListener menuItemClickListener) {

                getMenuInflater().inflate(R.menu.jogos_item_selecionado, menu);

                for (int i = 0; i < menu.size(); i++) {
                    menu.getItem(i).setOnMenuItemClickListener(menuItemClickListener);
                }

            }
        });

        jogoRecyclerViewAdapter.setOnContextMenuClickListener(new JogoRecyclerViewAdapter.OnContextMenuClickListener() {

            @Override
            public boolean onContextMenuItemClick(MenuItem menuItem, int position) {

                int idMenuItem = menuItem.getItemId();

                if (idMenuItem == R.id.menuItemEditar) {

                    editarJogo(position);
                    return true;
                } else {

                    if (idMenuItem == R.id.menuItemExcluir) {

                        excluirJogo(position);
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        });

        jogoRecyclerViewAdapter.setOnItemClickListener(new JogoRecyclerViewAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                editarJogo(position);
            }
        });

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

        intentAbertura.putExtra(JogoActivity.KEY_MODO, JogoActivity.MODO_NOVO);

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

    private void excluirJogo(int position) {

        listaJogos.remove(position);

        jogoRecyclerViewAdapter.notifyDataSetChanged();
    }

    ActivityResultLauncher<Intent> launcherEditarJogo = registerForActivityResult(
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

                            Jogo jogo = listaJogos.get(posicaoSelecionada);

                            jogo.setNome(nome);
                            jogo.setAno(ano);
                            jogo.setGenero(genero);
                            jogo.setPlaystation(playstation);
                            jogo.setXbox(xBox);
                            jogo.setNintendoSwitch(nintendoSwitch);

                            TipoMidia tipoMidia = TipoMidia.valueOf(tipoMidiaTexto);
                            jogo.setTipoMidia(tipoMidia);

                            jogoRecyclerViewAdapter.notifyDataSetChanged();
                        }
                    }

                    posicaoSelecionada = -1;
                }
            });

    private void editarJogo (int posicao) {

        posicaoSelecionada = posicao;

        Jogo jogo = listaJogos.get(posicaoSelecionada);

        Intent intentAbertura = new Intent(this, JogoActivity.class);

        intentAbertura.putExtra(JogoActivity.KEY_MODO, JogoActivity.MODO_EDITAR);

        intentAbertura.putExtra(JogoActivity.KEY_NOME, jogo.getNome());
        intentAbertura.putExtra(JogoActivity.KEY_ANO, jogo.getAno());

        ArrayList<String> consoles = new ArrayList<>();

        if (jogo.isPlaystation()) {
            consoles.add(getString(R.string.playstation));
        }
        if (jogo.isXbox()) {
            consoles.add(getString(R.string.xbox));
        }
        if (jogo.isNintendoSwitch()) {
            consoles.add(getString(R.string.nintendo_switch));
        }

        intentAbertura.putExtra(JogoActivity.KEY_CONSOLES, consoles);
        intentAbertura.putExtra(JogoActivity.KEY_GENERO, jogo.getGenero());
        intentAbertura.putExtra(JogoActivity.KEY_TIPO_MIDIA, jogo.getTipoMidia().toString());

        launcherEditarJogo.launch(intentAbertura);

    }
}