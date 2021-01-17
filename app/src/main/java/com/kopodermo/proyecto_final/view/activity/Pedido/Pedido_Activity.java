package com.kopodermo.proyecto_final.view.activity.Pedido;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.transition.Explode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kopodermo.proyecto_final.R;
import com.kopodermo.proyecto_final.adapter.Menu_PageAdapter;
import com.kopodermo.proyecto_final.model.Menú;
import com.kopodermo.proyecto_final.model.Producto;
import com.kopodermo.proyecto_final.sqlite.BDAdapter;
import com.kopodermo.proyecto_final.view.fragment.BebidaFragment;
import com.kopodermo.proyecto_final.view.fragment.ComidaFragment;
import com.kopodermo.proyecto_final.view.fragment.ExtraFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Objects;

public class Pedido_Activity extends AppCompatActivity {
    private final ComidaFragment comidaFragment = new ComidaFragment();
    private final BebidaFragment bebidaFragment = new BebidaFragment();
    private final ExtraFragment extraFragment = new ExtraFragment();
    public ArrayList<Producto> Main_Producto = new ArrayList<>();
    private EventBus bus = EventBus.getDefault();
    private TextView TVTotal, TVItems;
    private int TabPosition = 0;
    private String ID_Cliente;
    public Float Total = .0f;
    public int Items = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);

        getIncomingIntent();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        TextView title = findViewById(R.id.title);
        title.setText(getResources().getString(R.string.tb_menu));

        TabLayout tabLayout = findViewById(R.id.tablayout);

        CargarProducto();

        final ViewPager viewPager = findViewById(R.id.container);

        Menu_PageAdapter pageAdapter = new Menu_PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount(),
                comidaFragment,bebidaFragment,extraFragment);

        viewPager.setAdapter(pageAdapter);
        viewPager.setCurrentItem(0, true);

        // Listeners
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(getOnTabSelectedListener(viewPager));

        TVTotal = findViewById(R.id.price_total);
        TVItems = findViewById(R.id.item_total);
    }

    private void CargarProducto() {

        if(!Main_Producto.isEmpty()) return ;

        Main_Producto = new ArrayList<>();
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
                Main_Producto.add(p);
            }
            db.closeDB();
            bus.post(new Menú(.0f,Main_Producto,-1, "Act1"));

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
    }

    public void goCheckout(View view){
        if(Items <1){
            Toast.makeText(this,"Ingrese el pedido primero",Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(this, Confirmar_Activity.class);
        intent.putExtra("ID_Cliente", ID_Cliente);

        //Pasar el Arraylist de los productos
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("TratarPedido",Main_Producto);
        intent.putExtras(bundle);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Explode explode = new Explode();
            explode.setDuration(1000);
            getWindow().setExitTransition(explode);
            startActivity(intent,
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, this.getString(R.string.transitionname_picture)).toBundle());
        }else {
            startActivity(intent);
        }
    }

    private TabLayout.BaseOnTabSelectedListener getOnTabSelectedListener(ViewPager viewPager) {
        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TabPosition = tab.getPosition();
                viewPager.setCurrentItem(TabPosition);
                bus.post(new Menú(Total, Main_Producto, -1, "Act2"));
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
                switch(TabPosition) {
                    case 0:
                        comidaFragment.MostrarQuery(query);
                        break;
                    case 1:
                        bebidaFragment.MostrarQuery(query);
                        break;
                    case 2:
                        extraFragment.MostrarQuery(query);
                        break;
                    default:
                        // code block
                }
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

    private void getIncomingIntent() {
        if (getIntent().hasExtra("ID_Cliente")) {
            ID_Cliente = getIntent().getStringExtra("ID_Cliente");
        }
        Log.d(ID_Cliente, "getIncomingIntent: revisando." + ID_Cliente);
    }

    @Override
    public void onPause() {
        super.onPause();
        bus.unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        bus.register(this);
    }

    @SuppressLint("SetTextI18n")
    @Subscribe
    public void ActualizarScreen(Menú minus){
        if(minus.getIncrementar() == 1){
            Total = Total + minus.getPrecio();
            Items = Items + 1;
        }else if(minus.getIncrementar() == 2){
            Total = Total - minus.getPrecio();
            Items = Items - 1;
        }

        Main_Producto = minus.getProductos();

        TVTotal.setText("C$ " + Total);
        TVItems.setText(" - " + Items + " Items");
    }
}
