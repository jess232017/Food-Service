package com.kopodermo.proyecto_final.view.fragment;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.kopodermo.proyecto_final.R;
import com.kopodermo.proyecto_final.view.activity.Pedido.Confirmar_Activity;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistroFragment extends Fragment {
    private final Calendar cldr = Calendar.getInstance();
    private int day = cldr.get(Calendar.DAY_OF_MONTH);
    private int month = cldr.get(Calendar.MONTH);
    private int year = cldr.get(Calendar.YEAR);
    private DatePickerDialog picker;
    public boolean Efectivo = true;
    public String date1 = "";
    public String date2 = "";



    public RegistroFragment() {
        // Required empty public constructor
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registro, container, false);

        String Nombre = ((Confirmar_Activity)
                Objects.requireNonNull(getActivity())).Nombre;
        String Apellido = ((Confirmar_Activity)
                Objects.requireNonNull(getActivity())).Apellido;
        String Telefono = ((Confirmar_Activity)
                Objects.requireNonNull(getActivity())).Telefono;

        TextView name = view.findViewById(R.id.name);
        TextView phone = view.findViewById(R.id.phone);
        TextView given_date = view.findViewById(R.id.given_date);
        TextView paydate = view.findViewById(R.id.paydate);

        name.setText(Nombre +  " " + Apellido);
        phone.setText(Telefono);

        given_date.setInputType(InputType.TYPE_NULL);
        given_date.setOnClickListener(v -> {
            // date picker dialog
            picker = new DatePickerDialog(Objects.requireNonNull(getContext()),
                    (view1, year, monthOfYear, dayOfMonth) -> {
                        date1 = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        given_date.setText(date1);
                    }, year, month, day);
            picker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            picker.show();
        });

        MultiStateToggleButton button = view.findViewById(R.id.mstb_multi_id);
        // Resource id, position one is selected by default
        button.setElements(R.array.FormaPago, 0);

        TextInputLayout lytpay = view.findViewById(R.id.lytpay);
        View view1 = view.findViewById(R.id.view);

        button.setOnValueChangedListener(position -> {
            Log.d("bablabla", "Position: " + position);
            if(position == 0){
                Efectivo = true;
                lytpay.setVisibility(View.GONE);
                view1.setVisibility(View.GONE);
            }else{
                Efectivo = false;
                lytpay.setVisibility(View.VISIBLE);
                view1.setVisibility(View.VISIBLE);
            }
        });

        paydate.setInputType(InputType.TYPE_NULL);
        paydate.setOnClickListener(v -> {
            // date picker dialog
            picker = new DatePickerDialog(Objects.requireNonNull(getContext()),
                    (view12, year, monthOfYear, dayOfMonth) -> {
                        date2 = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        paydate.setText(date2);
                    }, year, month, day);
            picker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            picker.show();
        });

        return view;
    }

}
