package edu.pitt.kjg46_finalproject;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
/**
 * @author Kevin J. Grande
 * Public activity class that controls the trackerActivity
 */
public class trackerActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {
    private RadioButton radDem;
    private RadioButton radGop;
    private Spinner cboCandidateList;
    private EditText txtPollAverage;
    private EditText txtParty;
    private EditText txtPlace;
    private String url = null;
    Election e;
    private boolean dataParsed = false;
    /**
     * Required method that tells activity what to do when it starts up, initializes variables for ui controls
     * to be used in this activity class
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);
        radDem = (RadioButton) findViewById(R.id.radDem);
        radGop = (RadioButton) findViewById(R.id.radGop);
        cboCandidateList = (Spinner) findViewById(R.id.cboCandidateList);
        txtPollAverage = (EditText) findViewById(R.id.txtPollAverage);
        txtParty = (EditText) findViewById(R.id.txtParty);
        txtPlace = (EditText) findViewById(R.id.txtPlace);
    }
    /**
     * Required method that tells activity what to do when options menu is created
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tracker, menu);
        return true;
    }
    /**
     * Required method that tells activity what to do when a menu item is selected
     *
     * @param item
     * @return
     */
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
    /**
     * onClick method when "Show Candidates" button is clicked, loads candidates for selected party into
     * cboCandidateList spinner
     *
     * @param view
     */
    public void populateCandidates(View view) {
        if (radDem.isChecked() || radGop.isChecked()) {
            if (radDem.isChecked()) {
                Toast toast = Toast.makeText(this, "Party Selected: Democratic", Toast.LENGTH_SHORT);
                toast.show();
                url = "http://elections.huffingtonpost.com/pollster/api/charts?topic=2016-president-dem-primary&state=us";
                e = new Election(url);
                e.fetchJSON();
                while(e.parsingComplete);
                String[] names = e.getCandidateNames();
                ArrayAdapter<Candidate> adapter = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, names);
                cboCandidateList.setAdapter(adapter);
                cboCandidateList.setOnItemSelectedListener(this);
            } else {
                Toast toast = Toast.makeText(this, "Party Selected: Republican", Toast.LENGTH_SHORT);
                toast.show();
                url = "http://elections.huffingtonpost.com/pollster/api/charts?topic=2016-president-gop-primary&state=us";
                e = new Election(url);
                e.fetchJSON();
                while(e.parsingComplete);
                String[] names = e.getCandidateNames();
                ArrayAdapter<Candidate> adapter = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, names);
                cboCandidateList.setAdapter(adapter);
                cboCandidateList.setOnItemSelectedListener(this);
            }
        } else {
            Toast toast = Toast.makeText(this, "You Must Select a Party", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    /**
     * Required method that populates text fields when a Candidate is selected
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        if (radDem.isChecked()) {
            url = "http://elections.huffingtonpost.com/pollster/api/charts?topic=2016-president-dem-primary&state=us";
            e = new Election(url);
            e.fetchJSON();
        } else {
            url = "http://elections.huffingtonpost.com/pollster/api/charts?topic=2016-president-gop-primary&state=us";
            e = new Election(url);
            e.fetchJSON();
        }
        while (e.parsingComplete) ;
        {
            ArrayList<Candidate> candidates = e.getCandidateList();
            String choice = cboCandidateList.getSelectedItem().toString();
            Candidate c = e.matchCandidate(choice);
            if (c != null) {
                txtPollAverage.setText(String.valueOf(c.getNatlAvg()));
                txtParty.setText(c.getParty());
                txtPlace.setText(String.valueOf(c.getPosition()));
            } else {
                Toast t = Toast.makeText(this, "Unable to Access Web Service", Toast.LENGTH_SHORT);
                t.show();
            }
        }
    }
    /**
     * required method that tells activity what to do when no item in the cboCandidateList spinner is selected
     *
     * @param parent
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

