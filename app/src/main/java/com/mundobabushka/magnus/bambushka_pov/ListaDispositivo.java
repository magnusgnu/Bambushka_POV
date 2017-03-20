package com.mundobabushka.magnus.bambushka_pov;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.io.OutputStream;
import java.util.Set;
import java.util.ArrayList;

import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.content.Intent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;



public class ListaDispositivo extends AppCompatActivity {

    //widgets
    Button btnParear;
    ListView  listadedispositivo;
    //bluetooth
    private BluetoothAdapter MeuBluetooth = null;
    private Set<BluetoothDevice> dispositivosPareados;
    private OutputStream FluxoSaida = null;
    private static String EXTRA_ADDRESS = "device_address";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_dispositivo);

        //Chama os widgets
        btnParear = (Button)findViewById(R.id.button_listadispositivo);
        listadedispositivo = (ListView)findViewById(R.id.listview_listadispositivo);

        //Se o dispositivo tiver bluetooth
        MeuBluetooth = BluetoothAdapter.getDefaultAdapter();
            if (MeuBluetooth == null) {
                //Mostre um mensag. Que o dispositivo não tem adaptador bluetooth
                Toast.makeText(getApplicationContext(), "@string/Device_not_found", Toast.LENGTH_LONG).show();
                //Fechar o APK
                finish();
            }
            else if (MeuBluetooth.enable())
            {
                //Peça ao usuário ligar o bluetooth
                Intent LigarBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(LigarBT,1);
            }
        btnParear.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick (View v)
            {
                Lista_dispositivosPareados (); // método que será chamado
            }
        });

    }

    private void Lista_dispositivosPareados ()
    {
        dispositivosPareados = MeuBluetooth.getBondedDevices();
        ArrayList list = new ArrayList();

        if (dispositivosPareados()>0)
        {
            for(BluetoothDevice bt: dispositivosPareados)
            {
                list.add(bt.getName() + "\n" + bt.getAddress()); // Obter o nome do dispositivo e o endereço
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),"@string/NoDevicePaired", Toast.LENGTH_LONG).show();
        }
        final ArrayAdapter adaptadorBT = new ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
        listadedispositivo.setAdapter(adaptadorBT);
        listadedispositivo.setOnClickListener(MinhaListaClicOuvinte); // Método chamado quando o dispositivo Da lista é clicado
    }
    private AdapterView.OnItemClickListener MinhaListaClicOuvinte = new AdapterView.OnItemClickListener()
    {
    public void aoClicarNoItem (AdapterView<?> av,View v, int arg2, long arg3)
        {
        //Obter o endereço MAC do dispositivo, os últimos 17 caracteres na View
            String info = ((TextView)v).getText().toString();
            String endereco = info.substring(info.length() - 17);
            // Fazer uma intenção(intent) para iniciar a próxima atividade (activity).
            Intent i = new Intent(listadedispositivo.this,ledControl.class);
            // Alterar a atividade(activity).
            i.putExtra(EXTRA_ADDRESS,endereco); // este será recebido em ledControl.(classe) Atividade(activity)
            startActivity(i);
        }
    };

}
