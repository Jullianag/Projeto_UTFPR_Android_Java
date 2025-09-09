package br.edu.utfpr.colecaojogovideogame;

import android.content.Context;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.edu.utfpr.colecaojogovideogame.modelo.Jogo;

public class JogoRecyclerViewAdapter extends RecyclerView.Adapter<JogoRecyclerViewAdapter.JogoHolder> {

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    private OnCreateContextMenu onCreateContextMenu;
    private OnContextMenuClickListener onContextMenuClickListener;

    private Context context;

    private List<Jogo> listaJogos;

    private String[] generos;

    interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    interface OnItemLongClickListener {
        boolean onItemLongClick(View view, int position);
    }

    interface OnCreateContextMenu {

        void onCreateContextMenu(ContextMenu menu,
                                 View v,
                                 ContextMenu.ContextMenuInfo menuInfo,
                                 int position,
                                 MenuItem.OnMenuItemClickListener meuItemClickListener);
    }

    interface OnContextMenuClickListener {

        boolean onContextMenuItemClick(MenuItem menuItem, int position);
    }

    public JogoRecyclerViewAdapter(Context context, List<Jogo> listaJogos) {
        this.context = context;
        this.listaJogos = listaJogos;

        generos = context.getResources().getStringArray(R.array.generos);
    }

    public class JogoHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
                                                                       View.OnLongClickListener,
                                                                       View.OnCreateContextMenuListener {

        public TextView textViewValorNome;
        public TextView textViewValorAno;
        public TextView textViewValorValorConsole;
        public TextView textViewValorGenero;
        public TextView textViewValorTipoMidia;

        public JogoHolder(@NonNull View itemView) {
            super(itemView);

            textViewValorNome = itemView.findViewById(R.id.textViewValorNome);
            textViewValorAno = itemView.findViewById(R.id.textViewValorAno);
            textViewValorValorConsole = itemView.findViewById(R.id.textViewValorConsole);
            textViewValorGenero = itemView.findViewById(R.id.textViewValorGenero);
            textViewValorTipoMidia = itemView.findViewById(R.id.textViewValorTipoMidia);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {

            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, getAbsoluteAdapterPosition());
                }
            }

        @Override
        public boolean onLongClick(View v) {

            if (onItemLongClickListener != null) {
                onItemLongClickListener.onItemLongClick(v, getAbsoluteAdapterPosition());
                return true;
            }
            return false;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            if (onCreateContextMenu != null) {
                onCreateContextMenu.onCreateContextMenu(
                        menu,
                        v,
                        menuInfo,
                        getAbsoluteAdapterPosition(),
                        onMenuItemClickListener);
            }
        }

        MenuItem.OnMenuItemClickListener onMenuItemClickListener = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {

                if (onContextMenuClickListener != null) {
                    onContextMenuClickListener.onContextMenuItemClick(item, getAbsoluteAdapterPosition());
                    return true;
                }

                return false;
            }
        };
    }

    @NonNull
    @Override
    public JogoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.linha_lista_jogos, parent, false);

        return new JogoHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull JogoHolder holder, int position) {

        Jogo jogo = listaJogos.get(position);

        holder.textViewValorNome.setText(jogo.getNome());
        holder.textViewValorAno.setText(String.valueOf(jogo.getAno()));

        List<String> valorConsoles = new ArrayList<>();

        if (jogo.isPlaystation()) {
            valorConsoles.add(context.getString(R.string.playstation));
        }
        if (jogo.isXbox()) {
            valorConsoles.add(context.getString(R.string.xbox));
        }
        if (jogo.isNintendoSwitch()) {
            valorConsoles.add(context.getString(R.string.nintendo_switch));
        }

        holder.textViewValorValorConsole.setText(TextUtils.join(" ", valorConsoles));


        holder.textViewValorGenero.setText(generos[jogo.getGenero()]);

        switch (jogo.getTipoMidia()) {

            case Fisica:
                holder.textViewValorTipoMidia.setText(R.string.fisica);
                break;

            case Digital:
                holder.textViewValorTipoMidia.setText(R.string.digital);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return listaJogos.size();
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public OnItemLongClickListener getOnItemLongClickListener() {
        return onItemLongClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public OnCreateContextMenu getOnCreateContextMenu() {
        return onCreateContextMenu;
    }

    public void setOnCreateContextMenu(OnCreateContextMenu onCreateContextMenu) {
        this.onCreateContextMenu = onCreateContextMenu;
    }

    public OnContextMenuClickListener getOnContextMenuClickListener() {
        return onContextMenuClickListener;
    }

    public void setOnContextMenuClickListener(OnContextMenuClickListener onContextMenuClickListener) {
        this.onContextMenuClickListener = onContextMenuClickListener;
    }
}
