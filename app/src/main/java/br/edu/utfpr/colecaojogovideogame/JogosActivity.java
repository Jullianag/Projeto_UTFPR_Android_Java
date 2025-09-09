package br.edu.utfpr.colecaojogovideogame;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
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
import androidx.appcompat.view.ActionMode;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.edu.utfpr.colecaojogovideogame.utils.UtilsAlert;

public class JogosActivity extends AppCompatActivity {

    private RecyclerView recyclerViewJogos;
    private RecyclerView.LayoutManager layoutManager;
    private JogoRecyclerViewAdapter jogoRecyclerViewAdapter;

    private List<Jogo> listaJogos;

    private int posicaoSelecionada = -1;

    private ActionMode actionMode;

    private View viewSelecionada;
    private Drawable backgroundDrawable;

    public static final String ARQUIVO_PREFERENCIAS = "br.edu.utfpr.colecaojogovideogame.PREFERENCIAS";

    public static final String KEY_ORDENACAO_ASCENDENTE = "ORDENACAO_ASCENDENTE";

    public static final boolean PADRAO_INICIAL_ORDENACAO_ASCENDENTE = true;

    private boolean ordenacaoAscendente = PADRAO_INICIAL_ORDENACAO_ASCENDENTE;

    private MenuItem menuItemOrdenacao;

    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {

            MenuInflater inflate = mode.getMenuInflater();
            inflate.inflate(R.menu.jogos_item_selecionado, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            int idMenuItem = item.getItemId();

            if (idMenuItem == R.id.menuItemEditar) {
                editarJogo();
                return true;
            } else {

                if (idMenuItem == R.id.menuItemExcluir) {
                    excluirJogo();
                    return true;
                } else {
                    return false;
                }
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

            if (viewSelecionada != null) {
                viewSelecionada.setBackground(backgroundDrawable);
            }

            actionMode = null;
            viewSelecionada = null;
            backgroundDrawable = null;

            recyclerViewJogos.setEnabled(true);
        }
    };

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

        lerPreferencias();
        popularListaJogos();
    }

    private void popularListaJogos() {

        listaJogos = new ArrayList<>();

        jogoRecyclerViewAdapter = new JogoRecyclerViewAdapter(this, listaJogos);

        jogoRecyclerViewAdapter.setOnItemClickListener(new JogoRecyclerViewAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                posicaoSelecionada = position;
                editarJogo();
            }
        });

        jogoRecyclerViewAdapter.setOnItemLongClickListener(new JogoRecyclerViewAdapter.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(View view, int position) {

                if (actionMode != null) {
                    return false;
                }

                posicaoSelecionada = position;

                viewSelecionada = view;
                backgroundDrawable = view.getBackground();

                view.setBackgroundColor(getColor(R.color.corSelecionado));

                recyclerViewJogos.setEnabled(false);

                actionMode = startSupportActionMode(actionModeCallback);

                return true;
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

                            ordenarLista();
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

        menuItemOrdenacao = menu.findItem(R.id.menuItemOrdenacao);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        atualizarIconeOrdenacao();

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

                if (idMenuItem == R.id.menuItemOrdenacao) {

                    salvarPreferenciaOrdenacaoAscendente(!ordenacaoAscendente);
                    atualizarIconeOrdenacao();
                    ordenarLista();
                    return true;

                } else {

                    if (idMenuItem == R.id.menuItemRestaurar) {
                        confirmarRestaurarPadroes();
                        return true;

                    } else {
                        return super.onOptionsItemSelected(item);
                    }
                }
            }
        }
    }

    private void excluirJogo() {

        Jogo jogo = listaJogos.get(posicaoSelecionada);

        // String mensagem = getString(R.string.deseja_apagar) + jogo.getNome() + "\"";

        String mensagem = getString(R.string.deseja_apagar, jogo.getNome());

        DialogInterface.OnClickListener listenerSim = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                listaJogos.remove(posicaoSelecionada);
                // jogoRecyclerViewAdapter.notifyDataSetChanged();
                jogoRecyclerViewAdapter.notifyItemRemoved(posicaoSelecionada);
                actionMode.finish();
            }
        };

        UtilsAlert.confirmarAcao(this, mensagem, listenerSim, null);

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

                            final Jogo jogo = listaJogos.get(posicaoSelecionada);

                            final Jogo cloneJogoOriginal;

                            try {
                                cloneJogoOriginal = (Jogo) jogo.clone();

                            } catch (CloneNotSupportedException e) {
                                e.printStackTrace();
                                UtilsAlert.mostrarAviso(JogosActivity.this,
                                        R.string.erro_de_conversao_de_tipo);

                                return;
                            }

                            jogo.setNome(nome);
                            jogo.setAno(ano);
                            jogo.setGenero(genero);
                            jogo.setPlaystation(playstation);
                            jogo.setXbox(xBox);
                            jogo.setNintendoSwitch(nintendoSwitch);

                            TipoMidia tipoMidia = TipoMidia.valueOf(tipoMidiaTexto);
                            jogo.setTipoMidia(tipoMidia);

                            ordenarLista();

                            final ConstraintLayout constraintLayout = findViewById(R.id.main);

                            Snackbar snackbar = Snackbar.make(constraintLayout,
                                    R.string.alteracao_realizada,
                                    Snackbar.LENGTH_LONG);

                            snackbar.setAction(R.string.desfazer, new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {

                                    listaJogos.remove(jogo);
                                    listaJogos.add(cloneJogoOriginal);

                                    ordenarLista();
                                }
                            });

                            snackbar.show();
                        }
                    }

                    posicaoSelecionada = -1;

                    if (actionMode != null) {
                        actionMode.finish();
                    }
                }
            });

    private void editarJogo () {

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

    private void ordenarLista() {

        if (ordenacaoAscendente) {

            Collections.sort(listaJogos, Jogo.ordenacaoCrescente);
        } else {

            Collections.sort(listaJogos, Jogo.ordenacaoDecrescente);
        }

        jogoRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void atualizarIconeOrdenacao() {

        if (ordenacaoAscendente) {
            menuItemOrdenacao.setIcon(R.drawable.ic_action_ascending_order);
        } else {
            menuItemOrdenacao.setIcon(R.drawable.ic_action_descending_order);
        }
    }

    private void lerPreferencias() {

        SharedPreferences shared = getSharedPreferences(ARQUIVO_PREFERENCIAS, Context.MODE_PRIVATE);

        ordenacaoAscendente = shared.getBoolean(KEY_ORDENACAO_ASCENDENTE, ordenacaoAscendente);
    }

    private void salvarPreferenciaOrdenacaoAscendente(boolean novoValor) {

        SharedPreferences shared = getSharedPreferences(ARQUIVO_PREFERENCIAS, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = shared.edit();

        editor.putBoolean(KEY_ORDENACAO_ASCENDENTE, novoValor);

        editor.commit();

        ordenacaoAscendente = novoValor;
    }

    private void confirmarRestaurarPadroes() {

        DialogInterface.OnClickListener listenerSim = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                restaurarPadroes();
                atualizarIconeOrdenacao();
                ordenarLista();

                Toast.makeText(JogosActivity.this,
                        R.string.as_configuracoes_voltaram_para_o_padrao_de_instalacao,
                        Toast.LENGTH_LONG).show();
            }
        };

        UtilsAlert.confirmarAcao(this, R.string.deseja_voltar_padroes,
                listenerSim, null);
    }

    private void restaurarPadroes() {

        SharedPreferences shared = getSharedPreferences(ARQUIVO_PREFERENCIAS, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = shared.edit();

        editor.clear();

        editor.commit();

        ordenacaoAscendente = PADRAO_INICIAL_ORDENACAO_ASCENDENTE;
    }
}