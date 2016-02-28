package net.msonic.testsyncdata.to;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manuelzegarra on 27/02/16.
 */
public class Pedido {




    public final List<PedidoItem> detalle;


    public Pedido(){
        detalle = new ArrayList<PedidoItem>();
    }




}
