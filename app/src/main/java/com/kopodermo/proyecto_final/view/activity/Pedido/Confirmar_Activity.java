package com.kopodermo.proyecto_final.view.activity.Pedido;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.kopodermo.proyecto_final.R;
import com.kopodermo.proyecto_final.adapter.Check_PageAdapter;
import com.kopodermo.proyecto_final.model.CabeceraPedido;
import com.kopodermo.proyecto_final.model.DetallePedido;
import com.kopodermo.proyecto_final.model.Producto;
import com.kopodermo.proyecto_final.sqlite.BDAdapter;
import com.kopodermo.proyecto_final.view.fragment.PedidoFragment;
import com.kopodermo.proyecto_final.view.fragment.RegistroFragment;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class Confirmar_Activity extends AppCompatActivity {
    private final RegistroFragment registroFragment = new RegistroFragment();
    private final PedidoFragment pedidoFragment = new PedidoFragment();
    public ArrayList<Producto> GestionarPedido = new ArrayList<>();
    public String ID_Cliente, Nombre,  Apellido, Telefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar);

        ObtenerInformation();

        TabLayout tabLayout = findViewById(R.id.tablayout);
        final ViewPager viewPager = findViewById(R.id.container);
        Check_PageAdapter pageAdapter = new Check_PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount(),
                registroFragment, pedidoFragment);

        viewPager.setAdapter(pageAdapter);
        viewPager.setCurrentItem(0, true);

        // Listeners
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(getOnTabSelectedListener(viewPager));

        ImageButton bt_back = findViewById(R.id.bt_back);
        bt_back.setOnClickListener(view -> onSupportNavigateUp());
    }

    private TabLayout.BaseOnTabSelectedListener getOnTabSelectedListener(ViewPager viewPager) {
        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // nothing now
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // nothing now
            }
        };
    }

    public void goGuardar(View view) {
        // [INSERCIONES]
        String[] Metodos = new String[3];
        String fechaActual;
        String metodoPago;
        String ID_Pedido;

        BDAdapter db = new BDAdapter(this);
        db.openDB();
        Cursor c = db.obtenerFormasPago();

        int i=0;
        while (c.moveToNext()) {
            Metodos[i] = c.getString(1);
            i++;
        }

        String entrega = registroFragment.date1;
        String pago = registroFragment.date2;

        if(entrega.isEmpty() || (pago.isEmpty() && !registroFragment.Efectivo)){
            Toast.makeText(this,"Rellene Todos los Campos", Toast.LENGTH_LONG).show();
            return;
        }

        fechaActual = DateFormat.getDateInstance(DateFormat.SHORT).format(Calendar.getInstance().getTime());

        if(registroFragment.Efectivo){
            metodoPago = Metodos[0];
            pago = fechaActual;
        }else{
            metodoPago = Metodos[1];
        }

        ID_Pedido = db.insertarCabeceraPedido(
                new CabeceraPedido(null, fechaActual,
                        entrega,pago,ID_Cliente, metodoPago));

        int Tam = GestionarPedido.size();
        //Guardar en la base de datos todos los productos solicitados por el usuario
        for(i=0; i<Tam; i++){
            Producto p = GestionarPedido.get(i);
            db.insertarDetallePedido(new DetallePedido(ID_Pedido, i+1,
                    p.getIdProducto(), p.getCantidad(), p.getPrecio()));
        }

        Intent intent = new Intent(this, Finalizado_Activity.class);
        intent.putExtra("Id_Pedido", ID_Pedido);
        intent.putExtra("fechaActual", DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(Calendar.getInstance().getTime()));
        startActivity(intent);
    }

    private void ObtenerInformation() {
        if (getIntent().hasExtra("ID_Cliente")) {
            ID_Cliente = getIntent().getStringExtra("ID_Cliente");

            //Obtener el arraylist de los pedidos
            Bundle bundle = getIntent().getExtras();
            assert bundle != null;
            ArrayList<Producto> TratarPedido = bundle.getParcelableArrayList("TratarPedido");

            assert TratarPedido != null;
            int Tam = TratarPedido.size();

            //Separar los productos seleccionados de los que no y almacenarlos en un nuevo arraylist
            for(int i=0; i<Tam; i++){
                Producto p = TratarPedido.get(i);
                if(p .getCantidad()>0){
                    GestionarPedido.add(new Producto(p .getIdProducto(),p.getNombre(),p.getPrecio(),
                            p.getExistencias(),p.getTipo(),p.getImagen(),p.getCantidad()));
                }
            }

            BDAdapter db = new BDAdapter(this);
            db.openDB();

            try
            {
                Cursor c = db.obtenerCliente(ID_Cliente);
                while (c.moveToNext()) {
                    Nombre = c.getString(2);
                    Apellido = c.getString(3);
                    Telefono = c.getString(4);
                }
                db.closeDB();
            } catch (Exception e) {
                Log.e("Error", Objects.requireNonNull(e.getMessage()));
            }

        }

        Log.d(ID_Cliente, "getIncomingIntent: revisando." + ID_Cliente);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}