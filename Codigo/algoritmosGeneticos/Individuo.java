
package algoritmoGenetico;

import java.util.Arrays;
/*-----------------|Classe Individuo|-----------------/
Essa classe contem o genotipo e o fitness de cada individuo
/-----------------|                 |-----------------*/
public class Individuo implements Comparable<Individuo>{
    private int [] genotipo;
    protected double fitness;
    
    /*-----------------|
    O metodo construcao recebe o vetor genotipo e o fitness como parametros
    |-----------------*/
    public Individuo(int[] f, double fit)
    {
        genotipo = Arrays.copyOf(f, f.length);
        fitness = fit;
    }
    
    int [] getGenotipo()
    {
        return genotipo;
    }

    /*-----------------|
    Metodo compara individuos baseado em seu fitness
    |-----------------*/
    @Override
    public int compareTo(Individuo o) {
        return Double.compare(this.fitness, o.fitness);
    }
}
