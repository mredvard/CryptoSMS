package com.tesis.cryptosms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class DisplayMessageActivity extends Activity {
	private String password;
	private String texto;
	private String telefono;
	private EditText txtTelefono;
	private EditText txtMensaje;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message);
		Intent intent = getIntent();
		
		txtTelefono = (EditText) findViewById(R.id.telefono);
		txtMensaje = (EditText) findViewById(R.id.mensaje);
		
		// Obtiene el password del dialogo anterior
		password = intent.getStringExtra(CryptoSMSActivity.ENTRY_PASSWORD);
		texto = intent.getStringExtra(CryptoSMSActivity.ENTRY_MENSAJE);
		telefono = intent.getStringExtra(CryptoSMSActivity.ENTRY_NUMERO);
		txtMensaje.setText(texto);
		
	}
	
	public void enviarMensaje(View view) {
	    // Do something in response to button
		
		String numero = txtTelefono.getText().toString();
		String mensaje = txtMensaje.getText().toString();
		
		DesEncrypter encrypter = new DesEncrypter(password);
    	String encMensaje = encrypter.encrypt(mensaje);
    	SmsManager sms = SmsManager.getDefault();
    	sms.sendTextMessage(numero, null, encMensaje, null, null);
    	Toast.makeText(this, "Mensaje enviado", Toast.LENGTH_SHORT).show();
    	finish();
	}
}
