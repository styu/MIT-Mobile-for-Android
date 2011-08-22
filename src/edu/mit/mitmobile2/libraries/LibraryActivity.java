package edu.mit.mitmobile2.libraries;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import edu.mit.mitmobile2.Module;
import edu.mit.mitmobile2.ModuleActivity;
import edu.mit.mitmobile2.R;
import edu.mit.mitmobile2.TwoLineActionRow;

public class LibraryActivity extends ModuleActivity {

    private TwoLineActionRow accountRow;
    private TwoLineActionRow locationRow;
    private TwoLineActionRow askUsRow;
    private TwoLineActionRow tellUsRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createViews();
    }

    private void createViews() {
        setContentView(R.layout.library_main);

        accountRow = (TwoLineActionRow) findViewById(R.id.libraryAccount);
        locationRow = (TwoLineActionRow) findViewById(R.id.libraryLocationHours);
        askUsRow = (TwoLineActionRow) findViewById(R.id.libraryAskUs);
        tellUsRow = (TwoLineActionRow) findViewById(R.id.libraryTellUs); // librarySearch

        accountRow.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
            }
        });
        locationRow.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(LibraryActivity.this, LibraryLocationAndHour.class));
            }
        });
        askUsRow.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
            }
        });
        tellUsRow.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    protected Module getModule() {
        return new LibraryModule();
    }

    @Override
    public boolean isModuleHomeActivity() {
        return true;
    }

    @Override
    protected void prepareActivityOptionsMenu(Menu menu) {
    }

}
