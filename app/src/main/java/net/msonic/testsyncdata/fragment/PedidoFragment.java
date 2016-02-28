package net.msonic.testsyncdata.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.msonic.testsyncdata.CustomApplication;
import net.msonic.testsyncdata.R;
import net.msonic.testsyncdata.to.PedidoItem;
import net.msonic.testsyncdata.to.Product;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class PedidoFragment extends Fragment {


    @Bind(R.id.rvDetalle)
    RecyclerView rvDetalle;


    public PedidoFragment() {
        // Required empty public constructor
    }

    public static PedidoFragment newInstance(String param1, String param2) {
        PedidoFragment fragment = new PedidoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ((CustomApplication) getActivity().getApplication()).getDiComponent().inject(this);


    }


    Adapter adapter;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle(R.string.pedido_fragment_title);


        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rvDetalle.setLayoutManager(llm);

        List<PedidoItem> detalle = new ArrayList<PedidoItem>();

        PedidoItem p1 = new PedidoItem();
        p1.producto=new Product();
        p1.producto.name="Producto 1";
        p1.cantidad = 0.5;
        p1.precio = 1.5;
        detalle.add(p1);


        PedidoItem p2 = new PedidoItem();
        p2.producto=new Product();
        p2.producto.name="Producto 2";
        p2.cantidad = 0.5;
        p2.precio = 1.5;
        detalle.add(p2);

        PedidoItem p3 = new PedidoItem();
        p3.producto=new Product();
        p3.producto.name="Producto 3";
        p3.cantidad = 0.5;
        p3.precio = 1.5;
        detalle.add(p3);

        adapter =new Adapter(detalle);

        rvDetalle.setAdapter(adapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pedido, container, false);
        ButterKnife.bind(this, view);

        return view;
    }




    public class Adapter extends RecyclerView.Adapter<Adapter.PersonViewHolder>{


        final List<PedidoItem> detalle;

        public Adapter(List<PedidoItem> detalle){
            this.detalle=detalle;
        }


        @Override
        public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_pedido_content, viewGroup, false);
            PersonViewHolder pvh = new PersonViewHolder(v);
            return pvh;

        }

        @Override
        public void onBindViewHolder(PersonViewHolder holder, int position) {

            PedidoItem pedidoItem = detalle.get(position);

            holder.txtProducto.setText(pedidoItem.producto.name);
            holder.txtPrecio.setText(String.format("%s", pedidoItem.precio));
            holder.txtCantidad.setText(String.format("%s", pedidoItem.cantidad));


        }

        @Override
        public int getItemCount() {
            return detalle.size();
        }

        public class PersonViewHolder extends RecyclerView.ViewHolder {



            @Bind(R.id.txtProducto)
            TextView txtProducto;

            @Bind(R.id.txtCantidad)
            TextView txtCantidad;

            @Bind(R.id.txtPrecio)
            TextView txtPrecio;

            PersonViewHolder(View itemView) {
                super(itemView);

                ButterKnife.bind(this, itemView);

            }
        }

    }





}
