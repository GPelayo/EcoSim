package com.demonmoose.games.EcoSim;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Ecosystem extends JPanel{
    protected int VERT_WINDOW_SIZE = 600;
    protected int HORZ_WINDOW_SIZE = 800;
    protected int TANGLE_SIZE = 30;
    protected int FOOD_SIZE = 10;
    protected int NUMBER_OF_GRASSEATERS = 5;
    protected int NUMBER_OF_GRASS_PER_CLICK = 5;
    protected int OBJECT_LIMIT = 200;

    Timer timer;
    Graphics field;
    JFrame frame;
    List<Herbivore> PlantEaters = new ArrayList();
    List<Plant> PlantLife = new ArrayList();
    int lastMousePosOfX, lastMousePosOfY;
    int mousePosOfX, mousePosOfY, timeCounter;

    public Ecosystem() {
        InitializeFrame();
        InitializeData();
        InitializeTimer();
        InitializeWildlife();
    }

    private void InitializeFrame(){
        frame = new JFrame("Tangle Ecosystem");

        frame.add(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(HORZ_WINDOW_SIZE, VERT_WINDOW_SIZE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setBackground(Color.BLACK);
        
        addMouseListener(new MouseWatchman());
    }

    private void InitializeData(){
        lastMousePosOfX = HORZ_WINDOW_SIZE/2;
        lastMousePosOfY = VERT_WINDOW_SIZE/2;
        timeCounter = 0;
    }

    private void InitializeTimer(){
        timer = new Timer(10, new ClockWatchman());
        timer.setInitialDelay(190);
        timer.start();
    }

    private void InitializeWildlife(){
        Random randPos = new Random();
        for(int i = 0; i < NUMBER_OF_GRASSEATERS;i++){
            PlantEaters.add(new Herbivore(randPos.nextInt(HORZ_WINDOW_SIZE),
                                randPos.nextInt(VERT_WINDOW_SIZE)));
            PlantEaters.get(i).SetSteppingBoundaries(HORZ_WINDOW_SIZE, VERT_WINDOW_SIZE);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 1000, 1000);
        
        for(int j = 0; j < PlantLife.size(); j++)
            PlantLife.get(j).draw((Graphics2D) g);
        
        if(PlantEaters.size() > 0){
            for(int i = 0; i < NUMBER_OF_GRASSEATERS;i++)
               PlantEaters.get(i).draw((Graphics2D) g);
        }
    }

    private void InitializeMouse(){
        try{
            mousePosOfX = frame.getMousePosition().x - (TANGLE_SIZE/4)*3;
            mousePosOfY = frame.getMousePosition().y - (TANGLE_SIZE/2)*3;
        }
        catch(java.lang.NullPointerException error){
            mousePosOfX = lastMousePosOfX;
            mousePosOfY = lastMousePosOfY;
        }
    }

    private List<EcoObject> gatherWildLifeData(){
        List<EcoObject> CompleteCreatureList = new ArrayList();

        for(int i = 0; i < PlantLife.size(); i++){
                CompleteCreatureList.add(PlantLife.get(i));
        }

        return CompleteCreatureList;
    }

    private void growPlants(int growPosX, int growPosY){
        Random randPos = new Random();
        int offsetX, offsetY;

        for(int i = 0; i < NUMBER_OF_GRASS_PER_CLICK; i++){
            offsetX = 15 * randPos.nextInt(8) - 16;
            offsetY = 15 * randPos.nextInt(8) - 16;
            PlantLife.add(new Plant(growPosX + offsetX,  growPosY + offsetY));
        }
    }

    public class ClockWatchman implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            Random randNum = new Random();
            timeCounter++;
            if(((timeCounter% 2+randNum.nextInt(10)) == 0)&&(PlantLife.size() < OBJECT_LIMIT)){
                growPlants(randNum.nextInt(HORZ_WINDOW_SIZE), randNum.nextInt(VERT_WINDOW_SIZE));
            }
            
            for(int i = 0; i < NUMBER_OF_GRASSEATERS;i++){
                InitializeMouse();
                PlantEaters.get(i).SearchSurroundings(gatherWildLifeData());
                PlantEaters.get(i).Act(timeCounter);

                for(int j = 0; j < PlantLife.size(); j++){
                    if(PlantEaters.get(i).GetTargetPreyLocation() == PlantLife.get(j).Position){
                        if(PlantEaters.get(i).GetDistanceFromPrey() < 20){
                            PlantEaters.get(i).Eat(PlantLife.get(j).NourishmentRating);
                            PlantLife.remove(j);
                        }
                    }
                }
            }

            repaint();
        }
    }

    public class MouseWatchman implements MouseListener{
        public void mouseClicked(MouseEvent e) {
            growPlants(mousePosOfX, mousePosOfY);
            /*for(int i = 0; i < NUMBER_OF_GRASSEATERS;i++)
                PlantEaters.get(i).FindLocationOfClosestFood(gatherWildLifeData());
            */
        }

        public void mousePressed(MouseEvent e) {

        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }
    }
}

class NoWildLifeException extends Exception{
    
}
