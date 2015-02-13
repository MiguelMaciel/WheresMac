package miguelmaciel.play.wheresmac;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class SpecialAdapter extends BaseAdapter {
    private List<macExiste> listEstado;
    // Classe utilizada para instanciar os objetos do XML
    private LayoutInflater mInflater;

    public SpecialAdapter(Context context, List<macExiste> plistEstado) {
        this.listEstado = plistEstado;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final macExiste item) {
        this.listEstado.add(item);
        // Atualizar a lista caso seja adicionado algum item
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return listEstado.size();
    }

    @Override
    public Object getItem(int position) {
        return listEstado.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        // Pega o registro da lista e trasnfere para o objeto estadoVO
        macExiste estadoVO = listEstado.get(position);

        // Utiliza o XML estado_row para apresentar na tela
        convertView = mInflater.inflate(R.layout.estadorow, null);

        // Instância os objetos do XML
        TextView tvNome = (TextView) convertView
                .findViewById(R.id.textViewNome);
        TextView tvLocal = (TextView) convertView
                .findViewById(R.id.textViewLocal);

        // pega os dados que estão no objeto estadoVO e transfere para os
        // objetos do XML
        tvNome.setText(estadoVO.getNome());
        tvLocal.setText(estadoVO.getLocal());

        return convertView;
    }
}
