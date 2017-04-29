/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neu.pojo;

/**
 *
 * @author vishakha
 */
public class DrugOutcome {
    String drugname;
    String reaction;
    String doseform;
    String doseunit;
    String dosefrequency;
    String manufacturer;
    String route;
    String doseAmt;

    public DrugOutcome(String drugname, String reaction, String doseform, String doseunit, String dosefrequency, String manufacturer, String route, String doseAmt) {
        this.drugname = drugname;
        this.reaction = reaction;
        this.doseform = doseform;
        this.doseunit = doseunit;
        this.dosefrequency = dosefrequency;
        this.manufacturer = manufacturer;
        this.route = route;
        this.doseAmt = doseAmt;
    }



    public String getDrugname() {
        return drugname;
    }

    public void setDrugname(String drugname) {
        this.drugname = drugname;
    }

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }

    public String getDoseform() {
        return doseform;
    }

    public void setDoseform(String doseform) {
        this.doseform = doseform;
    }

    public String getDoseunit() {
        return doseunit;
    }

    public void setDoseunit(String doseunit) {
        this.doseunit = doseunit;
    }

    public String getDosefrequency() {
        return dosefrequency;
    }

    public void setDosefrequency(String dosefrequency) {
        this.dosefrequency = dosefrequency;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getDoseAmt() {
        return doseAmt;
    }

    public void setDoseAmt(String doseAmt) {
        this.doseAmt = doseAmt;
    }
    
    
}
