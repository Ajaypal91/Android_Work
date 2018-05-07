package edu.sjsu.ajay.fitnessapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class HomeActivity extends FragmentActivity implements WorkOutDetails.OnFragmentInteractionListener, WorkoutMain.OnFragmentInteractionListener {

    private WorkOutDetails workOutDetails;
    private WorkoutMain workoutMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        updateDisplay();
        //start service
        startService(new Intent(HomeActivity.this, MySensorService.class));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateDisplay();
    }

    public void updateDisplay(){
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();

        Configuration config = getResources().getConfiguration();
        if(config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            transaction.replace(R.id.my_fragment_layout, workOutDetails);
        } else {
            transaction.replace(R.id.my_fragment_layout, workoutMain);
        }
        transaction.commitAllowingStateLoss();
    }

    private void init(){
        workOutDetails = new WorkOutDetails();
        workoutMain = new WorkoutMain();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }
}
