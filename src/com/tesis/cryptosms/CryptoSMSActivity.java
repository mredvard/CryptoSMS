package com.tesis.cryptosms;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class CryptoSMSActivity extends ListActivity {
	static final ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
	private SimpleAdapter adapter;
	public final static String ENTRY_PASSWORD = "com.tesis.cryptosms.PASSWORD";
	public final static String ENTRY_MENSAJE = "com.tesis.cryptosms.MENSAJE";
	public final static String ENTRY_NUMERO = "com.tesis.cryptosms.NUMERO";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_list_view);
        adapter = new SimpleAdapter(
        		this,
        		list,
        		R.layout.custom_row_view,
        		new String[] {"remitente","mensaje"},
        		new int[] {R.id.text1,R.id.text2}
        		);
        if (list.isEmpty()) {
	    	HashMap<String,String> temp = new HashMap<String,String>();
	    	temp.put("remitente","Nuevo Mensaje");
	    	temp.put("mensaje", "Componer un nuevo mensaje");
	    	list.add(temp);
        }
        setListAdapter(adapter);

        registerReceiver(smsreceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();  // Always call the superclass
        // Stop method tracing that the activity started during onCreate()
        android.os.Debug.stopMethodTracing();
    }
    
    // Escucha de evento click en el ListView
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	super.onListItemClick(l, v, position, id);
    	
    	final HashMap<String, String> map = (HashMap<String, String>) this.getListAdapter().getItem(position); 
    	//Toast.makeText(this, map.get("remitente"), Toast.LENGTH_SHORT).show();
    	
    	if (position == 0) {
    		AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setTitle("Clave");
            alert.setMessage("Ingrese la clave a utilizar para este mensaje");

            // Establece un EditText para la entrada de password
            final EditText input = new EditText(this);
            alert.setView(input);

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
	              String value = input.getText().toString();
	              
	              // Llamar a la actividad de mensaje
	              Intent intent = new Intent(CryptoSMSActivity.this, DisplayMessageActivity.class);
				  intent.putExtra(ENTRY_PASSWORD, value);
				  intent.putExtra(ENTRY_MENSAJE, "");
				  intent.putExtra(ENTRY_NUMERO, "");
				  startActivity(intent);
              }
            });

            alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int whichButton) {
                // Accion de cancelar.
              }
            });

            alert.show();
    	} else {
    		AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setTitle("Clave");
            alert.setMessage("Ingrese la clave a utilizar para este mensaje");

            // Establece un EditText para la entrada de password
            final EditText input = new EditText(this);
            alert.setView(input);

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
	              String value = input.getText().toString();
	              String mensaje = map.get("mensaje");
	              String numero = map.get("remitente");
	              //Desencriptar
	              DesEncrypter encrypter = new DesEncrypter(value);
	        	  String dec =  encrypter.decrypt(mensaje);
	              
	              // Llamar a la actividad de mensaje	              
	              Intent intent = new Intent(CryptoSMSActivity.this, DisplayMessageActivity.class);
				  intent.putExtra(ENTRY_PASSWORD, value);
				  intent.putExtra(ENTRY_MENSAJE, dec);
				  intent.putExtra(ENTRY_NUMERO, numero);
				  startActivity(intent);
              }
            });

            alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int whichButton) {
                // Accion de cancelar.
              }
            });

            alert.show();
    	}
    }
    
    // Escucha de mensajes de texto entrantes
    private BroadcastReceiver smsreceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
    		Bundle bundle = intent.getExtras();        
    		SmsMessage[] msgs = null;
    
    		if(null != bundle)  {
    			String info = "";
    			String remitente = "";
        		Object[] pdus = (Object[]) bundle.get("pdus");
        		msgs = new SmsMessage[pdus.length];
        
        		for (int i=0; i<msgs.length; i++) {
           			msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);                
            			remitente += msgs[i].getOriginatingAddress()+"";                     
            			info += msgs[i].getMessageBody().toString();
        		}
        		
        		Toast.makeText(context, "Nuevo mensaje", Toast.LENGTH_SHORT).show();
        		
        		// Agrega un elemento nuevo
        		HashMap<String,String> temp = new HashMap<String,String>();
            	temp.put("remitente", remitente);
            	temp.put("mensaje", info);
            	list.add(temp);
            	adapter.notifyDataSetChanged();
    		}
		}
	};
}