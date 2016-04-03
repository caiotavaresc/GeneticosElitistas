package algoritmosGeneticos;

//A classe Utils implementara funcoes diversas (principalmente de manipulacao de binarios)
//que nao fazem parte do escopo do problema mas sao necessarias para tal
public class Utils {

	//binarioPraDecimal -> Recebe como entrada um array de int que representa um ponto no plano e retorna o ponto
	//Essa e a funcao de mapeamento que mapeiao genotipo em fenotipo
	//entrada e o genoma, min e max sao as definicoes do intervalo (dependentes da funcao).
	static Ponto binarioPraDecimal(int[] entrada, int min, int max)
	{
            double x = 0, y = 0;
            for(int i =0; i < entrada.length/2; i++)
            {   
                x+= Math.pow(2, i)*(entrada[entrada.length/2-1-i]);
                y+= Math.pow(2, i)*(entrada[entrada.length-1-i]);
            }
            
            x = min + x*((max-min)/(Math.pow(2, entrada.length/2)-1));
            y = min + y*((max-min)/(Math.pow(2, entrada.length/2)-1));
            
            return new Ponto(x, y);
	}
}