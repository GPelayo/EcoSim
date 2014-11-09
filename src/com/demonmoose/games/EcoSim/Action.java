package com.demonmoose.games.EcoSim;

import java.awt.*;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Geryl
 */

public class Action {
    private static final String DEFAULT_NAME = "Idle";
    private static final Color DEFAULT_COLOR = Color.BLUE;

    private static final Color MAROON = new Color(100,0,0);

    private String moodName;
    private int priority;
    private Color bodyColor;

    public static  final Action Idle = new Action(DEFAULT_NAME,DEFAULT_COLOR);
    public static  final Action SearchingForFood = new Action("Searching For Food", MAROON);
    public static  final Action Eating = new Action("Eating", Color.ORANGE);
    public static  final Action Grazing = new Action("Grazing", Color.YELLOW);

    public Action(){
        moodName = DEFAULT_NAME;
        bodyColor = DEFAULT_COLOR;
    }
    private Action(String name_Arg, Color color_Arg){
        moodName = name_Arg;
        bodyColor = color_Arg;
    }

    public boolean equals(Action mood_Arg){
        return this.moodName.equals(mood_Arg.toString());
    }

    public Color getColor(){
        return bodyColor;
    }

    @Override
    public String toString(){
        return moodName;
    }
}
