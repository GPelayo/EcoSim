/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.demonmoose.games.EcoSim;

import java.awt.*;
/**
 *
 * @author Geryl
 */
public class Personality {
    private Color identityColor;
    private String identityString;

    static Personality Glutton  = new Personality(Color.RED, "Glutton");
    static Personality Lax  = new Personality(Color.MAGENTA, "Lax");
    static Personality Boring  = new Personality(Color.LIGHT_GRAY, "Boring");

    public Personality(Color idColor, String idString){
        identityColor = idColor;
        identityString = idString;
    }

    public Color getColor(){
        return identityColor;
    }

    @Override
    public String toString(){
        return identityString;
    }
}
