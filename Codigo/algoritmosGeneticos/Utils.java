package algoritmoGenetico;

/*-----------------|Classe Utils|-----------------/
Essa classe implementa funcao que lida com os binarios
/-----------------|            |-----------------*/

public class Utils {
        /*-----------------|
        Esse metodo traduz o genotipo em fenotipo, ou seja, recebe um vetor binario e retorna o x e o y
        Conforme os textos binario_real page 1 e binario_real page 2 disponibilizado pela professora Sara.
        Os parametros desse metodo sao o vetor binario, o min e o max das funcoes
        O retorno eh um vetor com x na posicao 0 e y na posicao 1
        |-----------------*/
	public static double[] binarioPraDecimal(int[] entrada, int min, int max)
	{
            double x = 0, y = 0;
            for(int i =0; i < entrada.length/2; i++)
            {   
                x+= Math.pow(2, i)*(entrada[entrada.length/2-1-i]);
                y+= Math.pow(2, i)*(entrada[entrada.length-1-i]);
            }
            
            x = min + x*((max-min)/(Math.pow(2, entrada.length/2)-1));
            y = min + y*((max-min)/(Math.pow(2, entrada.length/2)-1));
            
            return new double[]{x,y};
	}
}