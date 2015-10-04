package edu.pitt.kjg46_finalproject;
import android.annotation.SuppressLint;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
/**
 * @author Kevin J. Grande
 * Public class that represents a primary election, it takes data from a JSON web service, parses it and creates
 * Candidate objects
 */
public class Election {
    private String urlString;
    public volatile boolean parsingComplete = true;
    private ArrayList<Candidate> candidateList = new ArrayList<Candidate>();
    private String[] candidateNames;
    /**
     * Class constructor takes a url and saves it to the urlString property to be used later in order to
     * parse JSON from that url
     * @param url = url string of JSON web service
     */
    public Election(String url) {
        this.urlString = url;
    }
    /**
     * Method parses JSON string, creates candidate objects and saves candidates to CandidateList
     * @param in
     */
    @SuppressLint("NewApi")
    public void readAndParseJSON(String in) {
        try {
            JSONArray reader = new JSONArray(in);
            JSONObject o = reader.getJSONObject(0);
            JSONArray candidates = o.getJSONArray("estimates");
            for (int i = 0; i < candidates.length(); i++) {
                JSONObject candidate = candidates.getJSONObject(i);
                String firstName = candidate.getString("first_name");
                String lastName = candidate.getString("last_name");
                String party = candidate.getString("party");
                int position = i + 1;
                String fullName = firstName + " " + lastName;
                Double natlAvg = candidate.getDouble("value");
                if(!firstName.equalsIgnoreCase("null")){
                    Candidate c = new Candidate(fullName, party, natlAvg, position);
                    this.candidateList.add(c);
                }
            }
            setCandidateNames(candidateList);
            parsingComplete = false;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /**
     * Method takes a candidate's full name as a string and matches the name to the corresponding Candidate object
     * @param candidate =  full candidate name
     * @return
     */
    public Candidate matchCandidate(String candidate) {
        ArrayList<String> names = new ArrayList<String>();
        Candidate cand = null;
        for (Candidate c : getCandidateList()) {
            if (c.getCandidateName().equalsIgnoreCase(candidate)) {
                cand = c;
            }
        }
        return cand;
    }
    /**
     * Method takes a url from the class constructor, pulls data from JSON web service, converts it to a String
     * using the streamToString() method and parses the data into Candidate objects using the
     * readAndParseJSON() method
     */
    public void fetchJSON() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    // Starts the query
                    conn.connect();
                    InputStream stream = conn.getInputStream();
                    //converts data stream to string
                    String data = convertStreamToString(stream);
                    //parses JSON
                    readAndParseJSON(data);
                    stream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
    /**
     * Method converts JSON Stream to a String so that the data can be parsed
     * @param is
     * @return
     */
    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
    public ArrayList<Candidate> getCandidateList() {
        return candidateList;
    }
    /**
     * Method used to set Candidate Names array, called when parsing JSON string
     * @param candidates
     * @return
     */
    private String[] setCandidateNames(ArrayList<Candidate> candidates){
        candidateNames = new String[candidates.size()];
        for(Candidate c: candidates){
            candidateNames[candidates.indexOf(c)] = c.getCandidateName();
        }
        return candidateNames;
    }
    /**
     * getter for Candidate Names Array used to populate Candidate List spinner
     * @return
     */
    public String[] getCandidateNames(){
        return this.candidateNames;
    }
}
