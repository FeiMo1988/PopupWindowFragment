package david.demos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import david.demos.popupwindow.Demos_PopupWindowFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void testPopupFragment(View view) {
        Intent intent = new Intent(this,Demos_PopupWindowFragment.class);
        this.startActivity(intent);
    }

}
