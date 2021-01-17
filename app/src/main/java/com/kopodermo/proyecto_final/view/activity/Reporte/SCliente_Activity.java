package com.kopodermo.proyecto_final.view.activity.Reporte;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.kopodermo.proyecto_final.R;
import com.kopodermo.proyecto_final.adapter.Clientes_Adapter;
import com.kopodermo.proyecto_final.model.Cliente;
import com.kopodermo.proyecto_final.sqlite.BDAdapter;
import com.kopodermo.proyecto_final.view.activity.Pedido.Pedido_Activity;

import java.util.ArrayList;
import java.util.Objects;

public class SCliente_Activity extends AppCompatActivity implements Clientes_Adapter.OnClienteListener{
    private ArrayList<Cliente> cliente = new ArrayList<>();
    private Clientes_Adapter clientes_Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scliente_);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        TextView title = findViewById(R.id.title);
        title.setText(getResources().getString(R.string.choose_client));

        RecyclerView clienteRecycler = findViewById(R.id.UsuarioRecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        clienteRecycler.setLayoutManager(linearLayoutManager);
        //------------
        clientes_Adapter = new Clientes_Adapter(CargarCliente(),R.layout.raw_cliente, this, this);
        clienteRecycler.setAdapter(clientes_Adapter);
    }

    private ArrayList<Cliente> CargarCliente() {
        BDAdapter db = new BDAdapter(this);
        db.openDB();
        try
        {
            Cliente p;
            int num;
            Cursor c = db.obtenerClientes();
            while (c.moveToNext()) {
                p = new Cliente(c.getString(2),c.getString(3),
                        c.getString(4),c.getString(5));

                p.setIdCliente(c.getString(1));
                num = db.obtenerPedidosPorUsuario(c.getString(1));
                p.setPedidos(num);
                cliente.add(p);
            }
            db.closeDB();
            return cliente;

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            return cliente;
        }
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        getMenuInflater().inflate( R.menu.search, menu);

        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) search.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String query) {
                //FILTER AS YOU TYPE
                clientes_Adapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClienteClick(Cliente cliente) {
        Intent intent = new Intent(this, RCliente_Activity.class);
        intent.putExtra("ID_Cliente", cliente.getIdCliente());
        intent.putExtra("Nombres", cliente.getNombres() + " " + cliente.getApellidos());
        intent.putExtra("Phone", cliente.getTelefono());
        intent.putExtra("Image", cliente.getImagen());
        this.startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
