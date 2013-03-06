package com.umair.templerun2patch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mobclix.android.sdk.MobclixAdView;
import com.mobclix.android.sdk.MobclixMMABannerXLAdView;

public class MainActivity extends FragmentActivity {
	
	SharedPreferences settings ;
	SharedPreferences.Editor editor;
	public static final String PREFS_NAME = "data";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		settings = getSharedPreferences(PREFS_NAME, 0);
		editor = settings.edit();
		
		LinearLayout adLayout = (LinearLayout) findViewById(R.id.adLayout);
		MobclixAdView adview = new MobclixMMABannerXLAdView(this);
		
		adLayout.addView(adview);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.settings_about:
	            DialogFragment aboutDialog = new about();
	            aboutDialog.show(getSupportFragmentManager(), "missiles");
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void patch(View v){
		
		boolean mainRun = settings.getBoolean("mainRun", false);
		
		
		if(mainRun == false){
			InputStream in = getResources().openRawResource(R.raw.gamedata);
		    
		    byte[] buff = new byte[1024];
		    int read = 0;

		    try {
		       FileOutputStream out = new FileOutputStream(Environment.getExternalStorageDirectory().getPath() + "/Android/data/com.imangi.templerun2/files/gamedata.txt");
		       while ((read = in.read(buff)) > 0) {
		          out.write(buff, 0, read);
		       }

		       out.flush();
		       out.close();
		       in.close();
		       
		       editor.putBoolean("mainRun", true);
		       editor.commit();
		         
		         Toast.makeText(MainActivity.this , "Patched ...", Toast.LENGTH_SHORT).show();
		    } catch(Exception e) {
		    	editor.putBoolean("mainRun", false);
		    	editor.commit();
		    	Toast.makeText(MainActivity.this , "Shit Happened ! :(", Toast.LENGTH_SHORT).show();
		    }
		}
		else {
			Toast.makeText(MainActivity.this , "Your Temple Run 2 is already patched. ;)", Toast.LENGTH_SHORT).show();
		}
		
		
	}
	
	public void backup (View v){
		//*Don't* hardcode "/sdcard"
		File sdcard = Environment.getExternalStorageDirectory();

		//Get the text file
		File sourceFile = new File(sdcard,"/Android/data/com.imangi.templerun2/files/gamedata.txt");
		File backupFolder = new File(sdcard, "/Android/data/com.imangi.templerun2/files/backup");
		
		if(!backupFolder.exists()){
			backupFolder.mkdirs();
		}
		
		File outFile = new File (sdcard, "/Android/data/com.imangi.templerun2/files/backup/gamedata.txt");

		try {
			InputStream in = new FileInputStream(sourceFile);
	        OutputStream out = new FileOutputStream(outFile);

	        // Copy the bits from instream to outstream 
	        byte[] buf = new byte[1024];
	        int len;
	        while ((len = in.read(buf)) > 0) {
	            out.write(buf, 0, len);
	        }
	        in.close();
	        out.close();
	        editor.putBoolean("backedUp", true);
	        editor.commit();
	        Toast.makeText(MainActivity.this , "Backed Up ...", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {Toast.makeText(MainActivity.this , "Something went wrong. Please contact omerjerk@gmail.com and submit this bug.", Toast.LENGTH_LONG).show();}
		
	}
	
	public void restore(View v){
		boolean backedUp = settings.getBoolean("backedUp", false);
		if( backedUp == true){
			File sdcard = Environment.getExternalStorageDirectory();
			File sourceFile = new File(sdcard,"/Android/data/com.imangi.templerun2/files/backup/gamedata.txt");
			File outFile = new File (sdcard, "/Android/data/com.imangi.templerun2/files/gamedata.txt");
			
			try {
				InputStream in = new FileInputStream(sourceFile);
		        OutputStream out = new FileOutputStream(outFile);

		        // Copy the bits from instream to outstream 
		        byte[] buf = new byte[1024];
		        int len;
		        while ((len = in.read(buf)) > 0) {
		            out.write(buf, 0, len);
		        }
		        in.close();
		        out.close();
		        Toast.makeText(MainActivity.this , "Restored !!!", Toast.LENGTH_SHORT).show();
			} catch (Exception e) {Toast.makeText(MainActivity.this , "Something went wrong. Please contact omerjerk@gmail.com and submit this bug.", Toast.LENGTH_LONG).show();}
			
		}
		
		else {
			Toast.makeText(MainActivity.this , "You don't have any backup of your scores. :P", Toast.LENGTH_SHORT).show();
		}
	}

}
