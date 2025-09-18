package br.edu.utfpr.colecaojogovideogame;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.edu.utfpr.colecaojogovideogame.modelo.Anotacao;
import br.edu.utfpr.colecaojogovideogame.utils.UtilsLocalDateTime;

public class AnotacaoRecyclerViewAdapter extends RecyclerView.Adapter<AnotacaoRecyclerViewAdapter.AnotacaoHolder> {

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    private OnCreateContextMenu onCreateContextMenu;
    private OnContextMenuClickListener onContextMenuClickListener;

    private Context context;

    private List<Anotacao> listaAnotacoes;

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

    public AnotacaoRecyclerViewAdapter(Context context, List<Anotacao> listaAnotacoes) {
        this.context = context;
        this.listaAnotacoes = listaAnotacoes;
    }

    public class AnotacaoHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener,
            View.OnCreateContextMenuListener {

        public TextView textViewValorDiaHoraCriacao;
        public TextView textViewValorTexto;

        public AnotacaoHolder(@NonNull View itemView) {
            super(itemView);

            textViewValorDiaHoraCriacao = itemView.findViewById(R.id.textViewValorDiaHoraCriacao);
            textViewValorTexto = itemView.findViewById(R.id.textViewValorTexto);

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
    public AnotacaoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.linha_lista_anotacoes, parent, false);

        return new AnotacaoHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull AnotacaoHolder holder, int position) {

        Anotacao anotacao = listaAnotacoes.get(position);

        holder.textViewValorDiaHoraCriacao.setText(UtilsLocalDateTime.formatLocalDateTime(anotacao.getDiaHoraCriacao()));
        holder.textViewValorTexto.setText(anotacao.getTexto());

    }

    @Override
    public int getItemCount() {
        return listaAnotacoes.size();
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


