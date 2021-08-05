package com.example.disponibilidadapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button FechaButton;
    EditText editFecha;

    private int dia, mes, ano;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FechaButton = (Button) findViewById(R.id.Fechabutton);
        editFecha = (EditText) findViewById(R.id.editTextFecha);
        FechaButton.setOnClickListener(this);

    }
        @Override
        public void onClick(View v){
            if(v==FechaButton){
            final Calendar cal= Calendar.getInstance();
            dia=cal.get(Calendar.DAY_OF_MONTH);
            mes=cal.get(Calendar.MONTH );
            ano=cal.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    editFecha.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                }
            }
            ,dia,mes,ano);
            datePickerDialog.show();
        }
    }
}