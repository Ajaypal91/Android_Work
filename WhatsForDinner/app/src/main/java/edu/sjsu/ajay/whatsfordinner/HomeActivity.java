package edu.sjsu.ajay.whatsfordinner;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.Set;

public class HomeActivity extends AppCompatActivity {

    private PopupWindow mPopupWindow;
    private Context mContext;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void bannerPopUp(View view){

//        Toast.makeText(this,"Banner Clicked!",Toast.LENGTH_LONG).show();

        //We need to get the instance of the LayoutInflater, use the context of this activity
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Inflate the view from a predefined XML layout (no need for root id, using entire layout)
        View layout = inflater.inflate(R.layout.popup_layout,null);

        //Get the devices screen density to calculate correct pixel sizes
        float density=this.getResources().getDisplayMetrics().density;
        // create a focusable PopupWindow with the given layout and correct size
        final PopupWindow pw = new PopupWindow(layout, (int)density*250, (int)density*300, true);

        //Button to close the pop-up
        ((Button) layout.findViewById(R.id.ok_button)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pw.dismiss();
            }
        });

//        Set up touch closing outside of pop-up
        pw.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pw.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    pw.dismiss();
                    return true;
                }
                return false;
            }
        });
        pw.setOutsideTouchable(true);

        // display the pop-up in the center
        pw.showAtLocation(layout, Gravity.CENTER, 0, 0);

//        Toast.makeText(this,"Banner done!",Toast.LENGTH_LONG).show();

    }

    public void mealsClick(View view){
        Intent intent = new Intent(this, MealsActivity.class);
        startActivity(intent);
    }

    public void receipesClick(View view){
        Intent intent = new Intent(this, RecipeActivity.class);
        startActivity(intent);
    }

    public void groceriesClick(View view){
        Intent intent = new Intent(this, GroceryActivity.class);
        startActivity(intent);
    }

    public void addNewDish(View view){
        Intent intent = new Intent(this, NewDishActivity.class);
        startActivity(intent);
    }



}
