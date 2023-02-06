package com.ura.ug;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {
	 protected int _splashTime = 3000; 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Handler timer = new Handler();
        
        Runnable r = new Runnable() {
      
      @Override
      public void run() {
       Intent intent = new Intent();
          intent.setClass(MainActivity.this, Control_usr.class);
          startActivity(intent);
                   
          finish();
      }
     };
     
        timer.postDelayed(r, _splashTime);
        

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    
    
}
 
    
    
    
}
