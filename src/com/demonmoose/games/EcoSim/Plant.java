package com.demonmoose.games.EcoSim;

import java.awt.*;
/**
 *
 * @author Geryl
 */
public class Plant extends EcoObject{
    public int NourishmentRating;

    Plant(Point startPosition){
        Position = startPosition;
        InitializeDefaultValues();
    }

    Plant(int startPositionX, int startPositionY){
        Position = new Point(startPositionX, startPositionY);
        InitializeDefaultValues();
    }

    private void InitializeDefaultValues(){
        size = 10;
        bodyColor = Color.GREEN;
        ObjType = "plant";
        NourishmentRating = 5;
    }
    
    @Override
    public void draw(Graphics2D pen){
        int[] xPoints = {Position.x, Position.x + size/4, Position.x + size/2, Position.x + (size*3)/4, Position.x + size,
                             Position.x + (size*5)/4, Position.x + (size*3)/2, Position.x + (size*7)/4, Position.x + size*2};
        int[] yPoints = {Position.y, Position.y - size, Position.y - size/2, Position.y - size, Position.y - size/2,
                             Position.y - size, Position.y - size/2, Position.y - size, Position.y};

        pen.setColor(bodyColor);
        pen.fillPolygon(xPoints, yPoints, yPoints.length);
    }  
}
