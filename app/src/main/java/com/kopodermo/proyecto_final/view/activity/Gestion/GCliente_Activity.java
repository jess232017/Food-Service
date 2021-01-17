package com.kopodermo.proyecto_final.view.activity.Gestion;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kopodermo.proyecto_final.R;
import com.kopodermo.proyecto_final.Tratar_Imagen;
import com.kopodermo.proyecto_final.adapter.Clientes_Adapter;
import com.kopodermo.proyecto_final.model.Cliente;
import com.kopodermo.proyecto_final.sqlite.BDAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class GCliente_Activity extends AppCompatActivity implements Clientes_Adapter.OnClienteListener{
    private static final String TAG = GCliente_Activity.class.getSimpleName();
    private TextView et_name,et_apellido,et_phone;
    private Clientes_Adapter clientes_Adapter;
    private String mCurrentPhotoPath="";
    private String name,apellido,phone;
    private Tratar_Imagen tratarImagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gcliente);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        TextView title = findViewById(R.id.title);
        title.setText(getResources().getString(R.string.edition_cliente));

        //------------
        RecyclerView clienteRecycler = findViewById(R.id.UsuarioRecycler);
        clienteRecycler.setHasFixedSize(true);
        clienteRecycler.setLayoutManager(new GridLayoutManager(this,2));
        clientes_Adapter = new Clientes_Adapter(CargarCliente(),R.layout.raw_cliente2, this, this);
        clienteRecycler.setAdapter(clientes_Adapter);

        FloatingActionButton fab = findViewById(R.id.fabPlus);
        fab.setOnClickListener(v -> CustomDialog(null,"Agregar Cliente"));
    }

    private ArrayList<Cliente> CargarCliente() {
        ArrayList<Cliente> cliente = new ArrayList<>();
        BDAdapter db = new BDAdapter(this);
        db.openDB();
        DatabaseUtils.dumpCursor(db.obtenerClientes());
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

    @SuppressLint("SetTextI18n")
    private void CustomDialog(Cliente cliente, String Tipo){
        final Dialog dialog = new Dialog(this);
        BDAdapter BD = new BDAdapter(this);
        BD.openDB();

        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_cliente);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        layoutParams.width = -1;
        layoutParams.height = -2;

        ((TextView) dialog.findViewById(R.id.Title)).setText(Tipo);
        Button EraseButton = dialog.findViewById(R.id.bt_erase);

        et_name = dialog.findViewById(R.id.Name);
        et_apellido = dialog.findViewById(R.id.Apellido);
        et_phone = dialog.findViewById(R.id.Phone);
        ImageView item_image = dialog.findViewById(R.id.item_image);

        float pixelDensity = getResources().getDisplayMetrics().density;
        LinearLayout revealView = dialog.findViewById(R.id.linearView);
        LinearLayout layoutButtons = dialog.findViewById(R.id.layoutButtons);

        FloatingActionButton fab = dialog.findViewById(R.id.fbCamera);

        tratarImagen = new Tratar_Imagen(this, fab, item_image, pixelDensity,
                revealView,layoutButtons, TAG);

        if(cliente != null){
            et_name.setText(cliente.getNombres());
            et_apellido.setText(cliente.getApellidos());
            et_phone.setText(cliente.getTelefono());
            EraseButton.setText("ACTUALIZAR CLIENTE");

            //Picasso.get().load(cliente.getImagen()).into(item_image);
            Glide.with(this)
                    .load(cliente.getImagen())
                    .apply(new RequestOptions().placeholder(R.drawable.ic_restau))
                    .into(item_image);
        }else{
            EraseButton.setText("AGREGAR CLIENTE");
            EraseButton.setBackgroundColor(getResources().getColor(R.color.BotonAgregar));
        }

        fab.setOnClickListener(v -> tratarImagen.launchTwitter());

        dialog.findViewById(R.id.btnCamera).setOnClickListener(v -> tratarImagen.onProfileImageClick(0));

        dialog.findViewById(R.id.btnGallery).setOnClickListener(v -> tratarImagen.onProfileImageClick(1));

        dialog.findViewById(R.id.bt_save).setOnClickListener(view -> {
            if(getValue()){
                if(cliente == null){
                    BD.insertarCliente(new Cliente(name,apellido,phone,mCurrentPhotoPath));
                }else{
                    cliente.setNombres(name);
                    cliente.setApellidos(apellido);
                    cliente.setTelefono(phone);
                    if(!mCurrentPhotoPath.isEmpty()) cliente.setImagen(mCurrentPhotoPath);
                    Log.d(TAG, "Image cache path: " + mCurrentPhotoPath);
                    BD.actualizarCliente(cliente);
                }
                BD.closeDB();
                clientes_Adapter.setData(CargarCliente());
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.bt_close).setOnClickListener(view -> {
            BD.closeDB();
            dialog.dismiss();
        });

        dialog.findViewById(R.id.bt_erase).setOnClickListener(view -> {
            if(getValue()){
                if(cliente == null){
                    BD.insertarCliente(new Cliente(name,apellido,phone,mCurrentPhotoPath));
                }else{
                    cliente.setNombres(name);
                    cliente.setApellidos(apellido);
                    cliente.setTelefono(phone);
                    if(!mCurrentPhotoPath.isEmpty()) cliente.setImagen(mCurrentPhotoPath);
                    Log.d(TAG, "Image cache path: " + mCurrentPhotoPath);
                    BD.actualizarCliente(cliente);
                }
                BD.closeDB();
                clientes_Adapter.setData(CargarCliente());
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(layoutParams);
    }

    private boolean getValue(){
        try {
            name = Objects.requireNonNull(et_name.getText()).toString().trim();
            apellido = Objects.requireNonNull(et_apellido.getText()).toString().trim();
            phone = Objects.requireNonNull(et_phone.getText()).toString().trim();
        }catch (Exception e){
            Toast.makeText(GCliente_Activity.this, "Rellene todos los campos", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(name.isEmpty() || apellido.isEmpty() || phone.isEmpty())
        {
            Toast.makeText(GCliente_Activity.this, "Rellene todos los campos", Toast.LENGTH_SHORT).show();
            return false;
        }
        else return true;
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClienteClick(Cliente cliente) {
        CustomDialog(cliente,"Editar Cliente");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == tratarImagen.getRequest()) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data != null ? data.getParcelableExtra("path") : null;
                // You can update this bitmap to your server
                //Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);

                // loading profile image from local cache
                mCurrentPhotoPath = uri != null ? Objects.requireNonNull(uri).toString() : null;
                tratarImagen.loadProfile(mCurrentPhotoPath);
            }
        }
    }
}
