package org.mickael.librarymsloan.model.enumeration;

public enum LoanStatus {

    ONGOING( "En cours"),
    OUTDATED("En retard"),
    EXTENDED( "Prolongé"),
    CLOSED( "Rendu");


    private final String label;

    LoanStatus(String label){
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

}
