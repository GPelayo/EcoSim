package com.demonmoose.games.EcoSim;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 *
 * @author Geryl
 */
public class Herbivore extends Animal{
    final Point DEFAULT_START_POSITION = new Point(300,300);
    
    final int SPEED = 3;

    //final int MIN_HUNGER_FOR_GRAZING = 1;
    final double THINKING_HUNGER_COST = 0.02;
    final double MOVING_HUNGER_COST = 0.05;

    public Point currentPosition;
    public int sizeOfCreature, MaxX, MaxY;
    

    private char gender;
    private double hungerRating, maxHungerRating, minHungerForSearching;
    private Action currentAction;
    private Personality personality;
    private Point locOfTargetedFood, locOfWanderingPoint;
    private Random dice;
    List<EcoObject> knownObjects;

    Herbivore(int startPosX, int startPosY){
       currentPosition = new Point(startPosX,startPosY);
       dice = new Random();

       InitializeRandomTraits();
       InitializeDisplayData();
       InitializeStatuses();
       InitializeMoveData();

       FindWanderingPoint();
    }

    private void InitializeDisplayData(){
        sizeOfCreature = 30;
        MaxX = Integer.MAX_VALUE;
        MaxY = Integer.MAX_VALUE;
    }

    private void InitializeStatuses(){
        hungerRating = 0;
        currentAction = Action.Idle;
    }

    private void InitializeRandomTraits(){
       if(dice.nextInt()%2 == 0)
           gender = 'm';
       else
           gender = 'f';

       switch(dice.nextInt(2)){
           case 0:
               personality = Personality.Glutton;
               break;
           case 1:
               personality = Personality.Boring;
       }

       if(personality.equals(Personality.Glutton)){
            maxHungerRating = 50.0;
            minHungerForSearching = 30.0;
       }
       else{
           maxHungerRating = 100.0;
           minHungerForSearching = 60.0;
       }
    }

    private void InitializeMoveData(){
       locOfTargetedFood = null;
       locOfWanderingPoint = null;
    }

    public Point GetTargetPreyLocation(){
        return locOfTargetedFood;
    }

    public Color GetHungerColor(){
        int red = (int)(255.0 *(hungerRating/maxHungerRating));
        int green = (int)(255.0 *(1.0 - (hungerRating/maxHungerRating)));
        return new Color(red, green, 0);
    }

    public double GetDistanceFromPrey(){
        //if(No prey)
        return locOfTargetedFood.distance(currentPosition);
        /*else
         * throw a no prey exception
         */
    }

    public char GetGender(){
        return gender;
    }

    public Action GetCurrentAction(){
        return currentAction;
    }

    public void SetSteppingBoundaries(int xBoundary, int yBoundary){
       MaxX = xBoundary;
       MaxY = yBoundary;
    }

    public void Act(int time){
        if(hungerRating >= minHungerForSearching)
            currentAction = Action.SearchingForFood;
        else if(hungerRating < 5){
            currentAction = Action.Idle;
            locOfTargetedFood = null;
        }

        if(currentAction.equals(Action.Idle)){
            if((time+dice.nextInt())%100 == 0)
                FindWanderingPoint();
        }
        else if(currentAction.equals(Action.SearchingForFood)){
            FindLocationOfClosestFood();
        }

        offsetHungerRating(THINKING_HUNGER_COST);

        if(currentAction.equals(Action.SearchingForFood)){
            Graze();
        }
        else if(currentAction.equals(Action.Idle)){
            Wander();
        }
    }

    public void Graze(){
        if(locOfTargetedFood != null){
            stepTowardsObject(locOfTargetedFood, SPEED);
        }
    }

    public void SearchSurroundings(List<EcoObject> ObjectLocations){
        knownObjects = ObjectLocations;
    }

    public void Wander(){
        if((locOfWanderingPoint != null)&&
            (currentPosition.distance(locOfWanderingPoint) > SPEED))
            stepTowardsObject(locOfWanderingPoint, SPEED);

    }

    public void Eat(int nourishmentArg){
        offsetHungerRating(-1 * nourishmentArg);
    }

    private void FindWanderingPoint(){
        int maxRandDist = 200;
        int randX = dice.nextInt(maxRandDist*2) - maxRandDist;
        int randY = dice.nextInt(maxRandDist*2) - maxRandDist;

        locOfWanderingPoint = new Point(currentPosition.x + randX,currentPosition.y + randY);
    }

    public void FindLocationOfClosestFood(){
        double smllstHyp, thisHyp;
        List<Point> foodLocations = new ArrayList();
        if((knownObjects.size() > 0)){
            for (int i = 0; i < knownObjects.size();i++)
                foodLocations.add(knownObjects.get(i).Position);

            smllstHyp = Double.MAX_VALUE;
        
            for (int i = 0; i < foodLocations.size(); i++) {
                thisHyp = foodLocations.get(i).distance(currentPosition);
                if (smllstHyp > thisHyp) {
                    locOfTargetedFood = foodLocations.get(i);
                    smllstHyp = thisHyp;
                }
            }
        }
    }

    private void offsetHungerRating(double offset_Arg){
        hungerRating+=offset_Arg;

        if(hungerRating < 0)
            hungerRating = 0;
        if(hungerRating > maxHungerRating)
            hungerRating = maxHungerRating;
    }

    private void stepTowardsObject(Point destination, int maxStepDistance){
        double angleBetCursorAndTangle = Math.abs(Math.atan((double)(currentPosition.y - destination.y)
                                               /(double)(currentPosition.x - destination.x)));

        if (currentPosition.y <= destination.y)
            currentPosition.y += (int)(maxStepDistance*(Math.sin(angleBetCursorAndTangle)));
        else if (currentPosition.y >=  destination.y)
            currentPosition.y -= (int)(maxStepDistance*(Math.sin(angleBetCursorAndTangle)));

        if (currentPosition.x >= destination.x)
            currentPosition.x -= (int)(maxStepDistance*Math.cos(angleBetCursorAndTangle));
        else if (currentPosition.x <= destination.x)
            currentPosition.x += (int)(maxStepDistance*Math.cos(angleBetCursorAndTangle));

        if(currentPosition.x  < 0){
           currentPosition.x += MaxX;
        }
        else if(currentPosition.x > MaxX){
            currentPosition.x -= MaxX;
        }

        if (currentPosition.y > MaxY){
            currentPosition.y -= MaxY - 10;
        }
        else if (currentPosition.y < 0){
            currentPosition.y += MaxY;
        }

        offsetHungerRating(MOVING_HUNGER_COST);
    }

    //@Override
    public void draw(Graphics2D pen){
        int PosOfGrassEaterX = currentPosition.x;
        int PosOfGrassEaterY = currentPosition.y;

        pen.setColor(GetHungerColor());
        pen.fillRect(PosOfGrassEaterX, PosOfGrassEaterY, sizeOfCreature, sizeOfCreature);
        pen.setColor(currentAction.getColor());
        pen.fillOval(PosOfGrassEaterX + 5, PosOfGrassEaterY + 5, 7, 7);
        pen.fillOval(PosOfGrassEaterX + 17, PosOfGrassEaterY + 5,7 , 7);

        pen.setColor(personality.getColor());

        if(gender == 'm'){
            pen.fillRect(PosOfGrassEaterX + 19, PosOfGrassEaterY-10, 5, 10);
            pen.fillRect(PosOfGrassEaterX + 5, PosOfGrassEaterY-10, 5, 10);
        }
        else
            pen.fillOval(PosOfGrassEaterX + 12, PosOfGrassEaterY-10, 5, 10);
    }
}