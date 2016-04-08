/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algoritmosGeneticos;

import java.util.Arrays;

/**
 *
 * @author sousa
 */
public class Individuo implements Comparable<Individuo>{
    private int [] genotipo;
    protected double fitness;
    
    public Individuo(int[] f, double fit)
    {
        genotipo = Arrays.copyOf(f, f.length);
        fitness = fit;
    }
    
    int [] getGenotipo()
    {
        return genotipo;
    }

    @Override
    public int compareTo(Individuo o) {
        return Double.compare(this.fitness, o.fitness);
    }
}
