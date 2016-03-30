package algoritmosGeneticos;

//A classe Utils implementará funções diversas (principalmente de manipulação de binários)
//que não fazem parte do escopo do problema mas são necessárias para tal
public class Utils {

	//binarioPraDecimal -> Recebe como entrada um array de int que representa um ponto no plano e retorna o ponto
	//Ainda precisamos definir tamanho de geração, mantissa, expoente, e etc para modelar essa função
	//Essa é a função de mapeamento que mapeiao genótipo em fenótipo
	//entrada é o genoma, min e max são as definições do intervalo (dependentes da funcao).
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
