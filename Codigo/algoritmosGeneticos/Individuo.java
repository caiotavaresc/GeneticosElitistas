
package algoritmosGeneticos;

import java.util.Arrays;
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
