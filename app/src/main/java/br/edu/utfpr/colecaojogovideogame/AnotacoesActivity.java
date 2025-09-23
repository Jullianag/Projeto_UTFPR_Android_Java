package br.edu.utfpr.colecaojogovideogame;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import br.edu.utfpr.colecaojogovideogame.modelo.Anotacao;
import br.edu.utfpr.colecaojogovideogame.modelo.Jogo;
import br.edu.utfpr.colecaojogovideogame.persistencia.JogosDatabase;
import br.edu.utfpr.colecaojogovideogame.utils.UtilsAlert;

public class AnotacoesActivity extends AppCompatActivity {

    public static final String KEY_ID_JOGO = "KEY_ID_JOGO";

    private RecyclerView recyclerViewAnotacoes;
    private RecyclerView.LayoutManager layoutManager;
    private AnotacaoRecyclerViewAdapter anotacaoRecyclerViewAdapter;

    private List<Anotacao> listaAnotacoes;

    private int posicaoSelecionada = -1;

    private ActionMode actionMode;

    private View viewSelecionada;
    private Drawable backgroundDrawable;

    private Jogo jogo;

    private ActionMode.Callback actionCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {

            MenuInflater inflate = mode.getMenuInflater();
            inflate.inflate(R.menu.item_selecionado, menu);
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
                editarAnotacao();
                return true;
            } else {

                if (idMenuItem == R.id.menuItemExcluir) {
                    excluirAnotacao();
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

            recyclerViewAnotacoes.setEnabled(true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anotacoes);

        Intent intentAbertura = getIntent();

        Bundle bundle = intentAbertura.getExtras();

        if (bundle != null) {

            long id = bundle.getLong(KEY_ID_JOGO);

            JogosDatabase database = JogosDatabase.getInstance(this);

            jogo = database.getJogoDao().queryForId(id);

            setTitle(getString(R.string.anotacoes_do_jogo, jogo.getNome()));

        } else {

        }

        recyclerViewAnotacoes = findViewById(R.id.recyclerViewAnotacoes);

        layoutManager = new LinearLayoutManager(this);

        recyclerViewAnotacoes.setLayoutManager(layoutManager);
        recyclerViewAnotacoes.setHasFixedSize(true);
        recyclerViewAnotacoes.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));

        popularListaAnotacoes();
    }

    private void popularListaAnotacoes() {

        JogosDatabase database = JogosDatabase.getInstance(this);

        listaAnotacoes = database.getAnotacaoDao().queryForIdJogo(jogo.getId());

        anotacaoRecyclerViewAdapter = new AnotacaoRecyclerViewAdapter(this, listaAnotacoes);

        anotacaoRecyclerViewAdapter.setOnItemClickListener(new AnotacaoRecyclerViewAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                posicaoSelecionada = position;
                editarAnotacao();
            }
        });

        anotacaoRecyclerViewAdapter.setOnItemLongClickListener(new AnotacaoRecyclerViewAdapter.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(View view, int position) {

                if (actionMode != null) {
                    return false;
                }

                posicaoSelecionada = position;

                viewSelecionada = view;
                backgroundDrawable = view.getBackground();

                view.setBackgroundColor(getColor(R.color.corSelecionado));

                recyclerViewAnotacoes.setEnabled(false);

                actionMode = startSupportActionMode(actionCallback);

                return true;
            }
        });

        recyclerViewAnotacoes.setAdapter(anotacaoRecyclerViewAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.anotacoes_opcoes, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int idMenuItem = item.getItemId();

        if (idMenuItem == R.id.menuItemAdicionar) {
            novaAnotacao();
            return true;

        } else {
            if (idMenuItem == android.R.id.home) {
                finish();
                return true;
            } else {
                return super.onOptionsItemSelected(item);

            }
        }
    }

    private void excluirAnotacao() {

        final Anotacao anotacao = listaAnotacoes.get(posicaoSelecionada);

        DialogInterface.OnClickListener listenerSim = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                JogosDatabase database = JogosDatabase.getInstance(AnotacoesActivity.this);

                int quantidadeAlterada = database.getAnotacaoDao().delete(anotacao);

                if (quantidadeAlterada != 1) {
                    UtilsAlert.mostrarAviso(AnotacoesActivity.this, R.string.erro_ao_tentar_excluir);
                    return;
                }

                listaAnotacoes.remove(posicaoSelecionada);
                anotacaoRecyclerViewAdapter.notifyItemRemoved(posicaoSelecionada);

                posicaoSelecionada = -1;

                actionMode.finish();
            }
        };

        UtilsAlert.confirmarAcao(this, R.string.deseja_apagar_anotacao, listenerSim, null);
    }

    private void novaAnotacao() {

        UtilsAlert.OnTextEnteredListener listener = new UtilsAlert.OnTextEnteredListener() {

            @Override
            public void onTextEntered(String texto) {

                if (texto == null || texto.trim().isEmpty()) {
                    UtilsAlert.mostrarAviso(AnotacoesActivity.this, R.string.to_texto_nao_pode_ser_vazio);
                    return;
                }

                Anotacao anotacao = new Anotacao(jogo.getId(), LocalDateTime.now(), texto);

                JogosDatabase database = JogosDatabase.getInstance(AnotacoesActivity.this);

                long novoId = database.getAnotacaoDao().insert(anotacao);


                if (novoId <= 0) {
                    UtilsAlert.mostrarAviso(AnotacoesActivity.this, R.string.erro_ao_tentar_inserir);
                    return;
                }

                anotacao.setId(novoId);

                listaAnotacoes.add(anotacao);

                Collections.sort(listaAnotacoes, Anotacao.ordenacaoDecrescente);
                anotacaoRecyclerViewAdapter.notifyDataSetChanged();
            }
        };

        UtilsAlert.lerTexto(this, R.string.nova_anotacao, R.layout.entrada_anotacao,
                R.id.editTextTexto, null, listener);
    }

    private void editarAnotacao() {

        final Anotacao anotacao = listaAnotacoes.get(posicaoSelecionada);

        UtilsAlert.OnTextEnteredListener listener = new UtilsAlert.OnTextEnteredListener() {

            @Override
            public void onTextEntered(String texto) {

                if (texto == null || texto.trim().isEmpty()) {
                    UtilsAlert.mostrarAviso(AnotacoesActivity.this, R.string.to_texto_nao_pode_ser_vazio);
                    return;
                }

                if (texto.equalsIgnoreCase(anotacao.getTexto())) {
                    // texto igual
                    return;
                }

                anotacao.setTexto(texto);

                JogosDatabase database = JogosDatabase.getInstance(AnotacoesActivity.this);

                long novoId = database.getAnotacaoDao().update(anotacao);

                if (novoId <= 0) {
                    UtilsAlert.mostrarAviso(AnotacoesActivity.this, R.string.erro_ao_tentar_alterar);
                    return;
                }

                anotacaoRecyclerViewAdapter.notifyItemChanged(posicaoSelecionada);

                posicaoSelecionada = -1;

                if (actionMode != null) {
                    actionMode.finish();
                }
            }
        };

        UtilsAlert.lerTexto(this, R.string.edite_texto_anotacao, R.layout.entrada_anotacao,
                R.id.editTextTexto, anotacao.getTexto(), listener);
    }
}