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

public class Drugs {
    String drugname;
//    int age;
//    String sex;
    String reactions;

    public Drugs(String drugname, String reactions) {
        this.drugname = drugname;
        this.reactions = reactions;
    }
    
    public String getDrugname() {
        return drugname;
    }

    public void setDrugname(String drugname) {
        this.drugname = drugname;
    }

    public String getReactions() {
        return reactions;
    }

    public void setReactions(String reactions) {
        this.reactions = reactions;
    }
    
    
    
}
