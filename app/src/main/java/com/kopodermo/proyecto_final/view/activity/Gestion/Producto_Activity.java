package com.kopodermo.proyecto_final.view.activity.Gestion;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kopodermo.proyecto_final.R;
import com.kopodermo.proyecto_final.Tratar_Imagen;
import com.kopodermo.proyecto_final.adapter.GProducto_Adapter;
import com.kopodermo.proyecto_final.model.Producto;
import com.kopodermo.proyecto_final.sqlite.BDAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class Producto_Activity extends AppCompatActivity implements GProducto_Adapter.OnFoodListener{
    private static final String TAG = GCliente_Activity.class.getSimpleName();
    private ColorGenerator generator = ColorGenerator.MATERIAL;
    private GProducto_Adapter gProductoAdapter;
    private EditText et_name, et_price, et_exist;
    private String mCurrentPhotoPath="";
    private Tratar_Imagen tratarImagen;

    private int Exist, Type;
    private String Nombre;
    private float Precio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        TextView title = findViewById(R.id.title);
        title.setText(getResources().getString(R.string.tb_editmenu));

        RecyclerView productoRecycler = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        productoRecycler.setLayoutManager(linearLayoutManager);
        //------------
        gProductoAdapter = new GProducto_Adapter(CargarProducto(),this, this);
        productoRecycler.setAdapter(gProductoAdapter);

        //Agregar linea de división
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(productoRecycler.getContext(),
                ((LinearLayoutManager) Objects.requireNonNull(productoRecycler.getLayoutManager())).getOrientation());
        productoRecycler.addItemDecoration(dividerItemDecoration);
        //------------

        FloatingActionButton fab = findViewById(R.id.fabPlus);
        fab.setOnClickListener(v -> CustomDialog(null, "Agregar Producto"));
    }

    private ArrayList<Producto> CargarProducto() {
        ArrayList<Producto> producto = new ArrayList<>();
        BDAdapter db = new BDAdapter(this);
        db.openDB();

        try
        {
            Producto p;
            Cursor c = db.obtenerProductos();
            while (c.moveToNext()) {
                p = new Producto(c.getString(1), c.getString(2),
                        c.getFloat(3),c.getInt(4),c.getInt(5),
                        c.getString(6));
                producto.add(p);
            }
            db.closeDB();
            return producto;

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            return producto;
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
                gProductoAdapter.getFilter().filter(query);
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


    @SuppressLint("SetTextI18n")
    private void CustomDialog(Producto producto, String Tipo){
        final Dialog dialog = new Dialog(this);
        BDAdapter BD = new BDAdapter(this);
        BD.openDB();

        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_producto);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        layoutParams.width = -1;
        layoutParams.height = -2;

        ((TextView) dialog.findViewById(R.id.Title)).setText(Tipo);
        Button EraseButton = dialog.findViewById(R.id.bt_erase);

        et_name = dialog.findViewById(R.id.Name);
        et_price = dialog.findViewById(R.id.Price);
        et_exist = dialog.findViewById(R.id.Exist);
        Spinner spnType = dialog.findViewById(R.id.spnType);
        ImageView item_image = dialog.findViewById(R.id.item_image);

        float pixelDensity = getResources().getDisplayMetrics().density;
        LinearLayout revealView = dialog.findViewById(R.id.linearView);
        LinearLayout layoutButtons = dialog.findViewById(R.id.layoutButtons);
        FloatingActionButton fab = dialog.findViewById(R.id.fbCamera);

        tratarImagen = new Tratar_Imagen(this, fab, item_image, pixelDensity,
                revealView,layoutButtons, TAG);

        if(producto != null){
            et_name.setText(producto.getNombre());
            et_price.setText("" + producto.getPrecio());
            et_exist.setText("" + producto.getExistencias());
            spnType.setSelection(producto.getTipo()-1);
            EraseButton.setText("ACTUALIZAR EL MENU");

            String letter = String.valueOf(producto.getNombre().charAt(0));

            TextDrawable drawable = TextDrawable.builder()
                    .buildRect(letter, generator.getRandomColor());

            //Picasso.get().load(food.getImagen()).into(item_image);
            Glide.with(this)
                    .load(producto.getImagen())
                    .apply(new RequestOptions().placeholder(drawable))
                    .into(item_image);
        }else{
            EraseButton.setText("AGREGAR AL MENU");
            EraseButton.setBackgroundColor(getResources().getColor(R.color.BotonAgregar));
        }

        spnType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id)
            {
                Type = pos+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        fab.setOnClickListener(v -> tratarImagen.launchTwitter());

        dialog.findViewById(R.id.btnCamera).setOnClickListener(v -> tratarImagen.onProfileImageClick(0));

        dialog.findViewById(R.id.btnGallery).setOnClickListener(v -> tratarImagen.onProfileImageClick(1));

        dialog.findViewById(R.id.bt_save).setOnClickListener(view -> {
            if(getValue()){
                if(producto == null){
                    BD.insertarProducto(new Producto(null,Nombre,Precio,Exist,Type,mCurrentPhotoPath));
                }else{
                    producto.setNombre(Nombre);
                    producto.setExistencias(Exist);
                    producto.setPrecio(Precio);
                    producto.setTipo(Type);
                    if(!mCurrentPhotoPath.isEmpty()) producto.setImagen(mCurrentPhotoPath);
                    BD.actualizarProducto(producto);
                }
                gProductoAdapter.setData(CargarProducto());
                BD.closeDB();
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.bt_close).setOnClickListener(view -> {
            BD.closeDB();
            dialog.dismiss();
        });

        dialog.findViewById(R.id.bt_erase).setOnClickListener(view -> {
            if(getValue()){
                if(producto == null){
                    BD.insertarProducto(new Producto(null,Nombre,Precio,Exist,Type,mCurrentPhotoPath));
                }else{
                    producto.setNombre(Nombre);
                    producto.setExistencias(Exist);
                    producto.setPrecio(Precio);
                    producto.setTipo(Type);
                    if(!mCurrentPhotoPath.isEmpty()) producto.setImagen(mCurrentPhotoPath);
                    BD.actualizarProducto(producto);
                }
                BD.closeDB();
                gProductoAdapter.setData(CargarProducto());
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(layoutParams);
    }

    private boolean getValue(){
        try {
            Nombre = Objects.requireNonNull(et_name.getText()).toString().trim();
            Precio = Float.parseFloat(et_price.getText().toString());
            Exist  = Integer.parseInt(et_exist.getText().toString());
            //Type   = Integer.parseInt(et_type.getText().toString());
            return true;
        }catch (Exception e){
            Toast.makeText(Producto_Activity.this, "Rellene todos los campos", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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

    @Override
    public void onNoteClick(Producto producto) {
        CustomDialog(producto, "Editar Platillo");
    }
}
