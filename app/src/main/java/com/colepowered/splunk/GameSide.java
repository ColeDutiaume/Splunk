package com.colepowered.splunk;


public class GameSide {


    private static int[][] board;

    public static void popMatrix(){
        board = new int[MainGame.getY_value()][MainGame.getX_value()];
        int counter = 0;
        int i = 0;
        while(i<MainGame.getY_value()){
            for(int j = 0; j < MainGame.getX_value(); j++){
                board[i][j] = counter;
                counter++;
            }
            i++;
        }
    }

    public static boolean randBool(){
        boolean rand = Math.random() < 0.5;
        return rand;
    }

    public static void setShip(int[] size){
        for(int i = 0; i < size.length; i++){
            MainGame.tiles[size[i]] = 1;
        }
    }

    public static boolean statusCheck(int[] check){
        boolean pass = true;
        for(int i = 0; i < check.length; i++){
            if(MainGame.tiles[check[i]] == 1){
                pass = false;
                break;
            }
        }
        return pass;
    }

    public static int[] buildShip(int shipLength){
        boolean looping;
        int[] ship = new int[shipLength];
        do{
            int[] store = new int[shipLength];
            boolean plusMinus = randBool();
            boolean dimension = randBool();

            looping = false;
            try {
                int init = (int)(Math.random()*MainGame.tiles.length);
                store[0] = init;
                for(int i = 0; i < shipLength-1; i++) {
                    if (plusMinus) {
                        store[i+1] = init + i + 1;
                    }
                    else {
                        store[i+1] = init - i - 1;
                    }
                    if(dimension) {
                        for(int j = 0; j < store.length; j++) {
                            ship[j] = board[store[j]][init];
                        }
                    }
                    else {
                        for(int j = 0; j < store.length; j++) {
                            ship[j] = board[init][store[j]];
                        }
                    }
                }
            }
            catch (Exception ArrayIndexOutOfBoundsException){
                looping = true;
            }
        }while(looping || !statusCheck(ship));
        return ship;
    }

}
