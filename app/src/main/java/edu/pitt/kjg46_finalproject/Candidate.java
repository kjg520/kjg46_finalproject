package edu.pitt.kjg46_finalproject;
/**
 * @author Kevin J. Grande
 * Public class that represents Primary Election Candidates
 */
public class Candidate {
    private String candidateName;
    private String party;
    private double natlAvg;
    private int position;
    /**
     * Class constructor that takes JSON data parsed in Election class and creates Candidate objects
     * @param candidateName
     * @param party
     * @param natlAvg
     * @param position
     */
    public Candidate(String candidateName, String party, double natlAvg, int position){
        this.candidateName = candidateName;
        if(party.equalsIgnoreCase("Dem")){
            this.party = "Democratic";
        }else{
            this.party = "Republican";
        }
        this.natlAvg = natlAvg;
        this.position = position;
    }
    /**
     * Override Method that makes property candidateName appear instead of memory location when
     * printing out candidate objects - used for testing
     * @return
     */
    @Override
    public String toString(){
        return this.candidateName;
    }
    /**
     * getter for candidateName property
     * @return
     */
    public String getCandidateName() {
        return candidateName;
    }
    /**
     * getter for Candidate party property
     * @return
     */
    public String getParty() {
        return party;
    }
    /**
     * getter for Candidate National Poll Average property
     * @return
     */
    public double getNatlAvg() {
        return natlAvg;
    }
    /**
     * getter for Candidate position property
     * @return
     */
    public int getPosition() {
        return position;
    }
}
