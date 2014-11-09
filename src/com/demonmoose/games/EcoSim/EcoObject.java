package com.demonmoose.games.EcoSim;

import java.awt.*;

/**
 *
 * @author Geryl
 */
public abstract class EcoObject {
    public int size;
    public Color bodyColor;
    public Point Position;
    public String ObjType;
    
    public abstract void draw(Graphics2D drawingTool);
}
