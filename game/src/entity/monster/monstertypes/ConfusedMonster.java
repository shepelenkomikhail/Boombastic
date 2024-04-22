package entity.monster.monstertypes;

import java.awt.*;
import java.util.Date;
import java.util.List;
import java.util.Random;

import cell.box.BoxCell;
import cell.normalCell.NormalCell;
import cell.wall.WallCell;
import entity.Entity;
import entity.monster.Monster;
import entity.player.Player;
import map.GameMap;

import javax.swing.*;
public class ConfusedMonster extends Monster {
    private Image baseImage;
    private int direction;
    private Date lastMoveTime = new Date();

    private int speed;

    // Constructor
    public ConfusedMonster(int x, int y, GameMap gameMap, List<Player> players) {
        super(x, y, gameMap, players);
        findValidStartingPosition();
        direction = new Random().nextInt(4); // 0: left, 1: right, 2: up, 3: down
        this.speed = 450;
    }



    public String determineMonsterDirection() {
        Player p = findNearestPlayer();
        int upDistance = this.getY() > p.getY() ? this.getY() - p.getY() : Integer.MAX_VALUE;
        int downDistance = this.getY() < p.getY() ? p.getY() - this.getY() : Integer.MAX_VALUE;
        int leftDistance = this.getX() > p.getX() ? this.getX() - p.getX() : Integer.MAX_VALUE;
        int rightDistance = this.getX() < p.getX() ? p.getX() - this.getX() : Integer.MAX_VALUE;

        int minDistance = Math.min(Math.min(upDistance, downDistance), Math.min(leftDistance, rightDistance));

        if (minDistance == upDistance && upDistance != 0) {
            return "UP";
        } else if (minDistance == downDistance && downDistance != 0) {
            return "DOWN";
        } else if (minDistance == leftDistance && leftDistance != 0) {
            return "LEFT";
        } else if (minDistance == rightDistance && rightDistance != 0) {
            return "RIGHT";
        } else {
            return "sup";
        }
    }



    private void findValidStartingPosition() {
        int maxX = gameMap.getMap().length;
        int maxY = gameMap.getMap()[0].length;
        Random rand = new Random();
        do {
            this.x = rand.nextInt(maxY);
            this.y = rand.nextInt(maxX);
        } while (!(gameMap.getMap()[this.y][this.x] instanceof NormalCell));

    }





    public void moveRandomly() {

        if(lastMoveTime.getTime() + speed < new Date().getTime()) {
            lastMoveTime = new Date();

            Random rand = new Random();
            int newVal;
            int[] dx = {1, -1, 0, 0};
            int[] dy = {0, 0, 1, -1};
            int directionNew = -1;
            int newX = this.x + dx[direction];
            int newY = this.y + dy[direction];
            if (newX >= 0 && newX < gameMap.getMap()[0].length && newY >= 0 && newY < gameMap.getMap().length && gameMap.getMap()[newY][newX] instanceof NormalCell) {
                this.gameMap.getMap()[this.y][this.x].getVisitors().remove(this);

                newVal = rand.nextInt(4);
                        if(dy[newVal] == 0 && dy[direction] != 0 || dy[newVal] != 0 && dy[direction] == 0 || dx[newVal] == 0 && dx[direction] != 0 || dx[newVal] != 0 && dx[direction] == 0){
                            if(gameMap.getMap()[this.y + dy[newVal]][this.x + dx[newVal]] instanceof NormalCell){
//                                System.out.println("Turned");
                                if(rand.nextInt(7) == 2) {
//                                    System.out.println("Turned!!");

                                    this.x = this.x + dx[newVal];
                                    this.y = this.y + dy[newVal];
                                    this.gameMap.getMap()[this.y][this.x].addVisitor(this);

                                }
                            }
                        }
                else{
                    this.x = newX;
                    this.y = newY;
                    this.gameMap.getMap()[this.y][this.x].addVisitor(this);

                }
            }
            else {
                switch (determineMonsterDirection()){
                    case "UP":
                        directionNew = 3;
                        break;
                    case "DOWN":
                        directionNew = 2;
                        break;
                    case "LEFT":
                        directionNew = 1;
                        break;
                    case "RIGHT":
                        directionNew = 0;
                        break;
                }


                if(directionNew != direction){
                    direction = directionNew;
                }
                else{
                    direction = rand.nextInt(4);
                }
            }
        }
    }


    private Player findNearestPlayer() {
        Player nearestPlayer = null;
        double minDistance = Double.MAX_VALUE;  // Start with the largest possible value

        for (Player player : this.players) {
            double distance = Math.sqrt(Math.pow(this.x - player.getX(), 2) + Math.pow(this.y - player.getY(), 2));
            if (distance < minDistance) {
                minDistance = distance;
                nearestPlayer = player;
            }
        }

        return nearestPlayer;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    // Method to check if next to player
    public boolean isNextToPlayer(int px, int py) {
        return this.x == px && this.y == py;
    }
}

