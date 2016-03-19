package funcoes;
import algoritmosGeneticos.Ponto;

//Classe GOLD -> Mapeia o algoritmo genético para a função Gold
//Objetivo: Minimização
public class Gold extends algoritmosGeneticos.AlgoritmosGeneticos{

	//Método construtor -> Chama o construtor da classe pai
	Gold() 
	{
		super();
	}
	
	//Método fitness -> Função de avaliação
	//O fenótipo de todas as funções é um ponto no plano
	//Na função GOLD temos que mapear os domínios positivo e negativo. A função resultará apenas no domínio positivo
	//O domínio negativo (-z) deve ser tratado no código
	protected double fitness(Ponto fenotipo)
	{
		double a, b, z, x, y;
	
		//Atribuir valores
		x = fenotipo.x;
		y = fenotipo.y;
		
		//Representação da função passada na especificação
		a = 1 + Math.pow((x + y + 1), 2) * (19-14*Math.pow(x,2) - 14*y + 6*x*y + 3*Math.pow(y,2));
		b = 30 + Math.pow((2*x - 3*y), 2) * (18 - 32*x + 12*Math.pow(x,2) + 48*y -36*x*y + 27*Math.pow(y,2));
		z = a*b;
		
		return z;
	}

}
