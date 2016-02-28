package net.msonic.testsyncdata.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.msonic.testsyncdata.CustomApplication;
import net.msonic.testsyncdata.R;
import net.msonic.testsyncdata.to.PedidoItem;
import net.msonic.testsyncdata.to.Product;
import net.msonic.testsyncdata.util.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class PedidoFragment extends Fragment implements View.OnClickListener,RecyclerView.OnItemTouchListener {


    @Bind(R.id.rvDetalle)
    RecyclerView rvDetalle;


    GestureDetectorCompat gestureDetector;

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



        rvDetalle.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvDetalle.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        rvDetalle.addItemDecoration(itemDecoration);


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


        for(int i=0;i<40;i++){
            PedidoItem p4 = new PedidoItem();
            p4.producto=new Product();
            p4.producto.name= String.format("Nicole %s",i+1);
            p4.cantidad = 0.5;
            p4.precio = 1.5;
            detalle.add(p4);
        }

        adapter =new Adapter(detalle);
        rvDetalle.setAdapter(adapter);
        rvDetalle.addOnItemTouchListener(this);




        gestureDetector =
                new GestureDetectorCompat(getActivity(), new RecyclerViewDemoOnGestureListener());


        /*String dateStr = DateUtils.formatDateTime(
                this,
                model.dateTime.getTime(),
                DateUtils.FORMAT_ABBREV_ALL);*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pedido, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onClick(View v) {

        // item click
        int idx = rvDetalle.getChildAdapterPosition(v);

        myToggleSelection(idx);

        /*if (actionMode != null) {
            myToggleSelection(idx);
            return;
        }
        DemoModel data = adapter.getItem(idx);
        View innerContainer = view.findViewById(R.id.container_inner_item);
        innerContainer.setTransitionName(Constants.NAME_INNER_CONTAINER + "_" + data.id);
        Intent startIntent = new Intent(this, CardViewDemoActivity.class);
        startIntent.putExtra(Constants.KEY_ID, data.id);
        ActivityOptions options = ActivityOptions
                .makeSceneTransitionAnimation(this, innerContainer, Constants.NAME_INNER_CONTAINER);*/
        //this.startActivity(startIntent, options.toBundle());


    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        gestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }


    private class RecyclerViewDemoOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            View view = rvDetalle.findChildViewUnder(e.getX(), e.getY());
            onClick(view);
            return super.onSingleTapConfirmed(e);
        }

        public void onLongPress(MotionEvent e) {
            View view = rvDetalle.findChildViewUnder(e.getX(), e.getY());

            /*if (actionMode != null) {
                return;
            }
            // Start the CAB using the ActionMode.Callback defined above
            actionMode = startActionMode(RecyclerViewDemoActivity.this);*/
            int idx = rvDetalle.getChildAdapterPosition(view);
            myToggleSelection(idx);
            super.onLongPress(e);
        }
    }


    private void myToggleSelection(int idx) {
        adapter.toggleSelection(idx);
        //String title = getString(R.string.selected_count, adapter.getSelectedItemCount());
        //actionMode.setTitle(title);
    }




    public class Adapter extends RecyclerView.Adapter<Adapter.PersonViewHolder>{


        private final SparseBooleanArray selectedItems;
        private final List<PedidoItem> detalle;

        public Adapter(List<PedidoItem> detalle){
            this.detalle=detalle;
            this.selectedItems = new SparseBooleanArray();
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


            holder.itemView.setActivated(selectedItems.get(position, false));

            /*
            	//This "if-else" is just an example
		if (isSelected(position)) {
			holder.mImageView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.image_round_selected));
		} else {
			holder.mImageView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.image_round_normal));
		}
            * */
        }

        @Override
        public int getItemCount() {
            return detalle.size();
        }



        public void toggleSelection(int pos) {
            if (selectedItems.get(pos, false)) {
                selectedItems.delete(pos);
            }
            else {
                selectedItems.put(pos, true);
            }
            notifyItemChanged(pos);
        }

        public void clearSelections() {
            selectedItems.clear();
            notifyDataSetChanged();
        }


        public List<Integer> getSelectedItems() {
            List<Integer> items =
                    new ArrayList<Integer>(selectedItems.size());
            for (int i = 0; i < selectedItems.size(); i++) {
                items.add(selectedItems.keyAt(i));
            }
            return items;
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
