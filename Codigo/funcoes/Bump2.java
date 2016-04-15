/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package funcoes;

import algoritmosGeneticos.Utils;

/**
 *
 * @author sousa
 */
public class Bump2 extends algoritmosGeneticos.AlgoritmosGeneticos{

    public double fitness(int [] gen) {
        
        double x, y, z, temp0, temp1, temp2;
        double xy[] = Utils.binarioPraDecimal(gen, min, max);
        x = xy[0];
        y = xy[1];
        
        if(x*y< 0.75)
            z = 0;
        else if((x+y)>15)
            z = 0;
        else
        {
            temp0 = Math.pow(Math.cos(x), 4)+Math.pow(Math.cos(y), 4);
            temp1 = 2*Math.cos(Math.pow(x, 2))*Math.cos(Math.pow(y, 2));
            temp2 = Math.sqrt(Math.pow(x, 2) +2*Math.pow(y, 2));
            z = Math.abs((temp0-temp1)/temp2);
            z = -z;
        }
        
        return z;
    }
    
    
}
