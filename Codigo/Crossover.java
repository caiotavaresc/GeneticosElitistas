package Operadores;


import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sousa
 */
public abstract class Crossover {
    
    Random rand;
    
    public abstract int[][] executar(int[] pai, int[] mae);
    
    public Crossover()
    {
        rand = new Random();
    }
}
