package funcoes;
import algoritmosGeneticos.Ponto;

//Classe GOLD -> Mapeia o algoritmo gen�tico para a fun��o Gold
//Objetivo: Minimiza��o
public class Gold extends algoritmosGeneticos.AlgoritmosGeneticos{

	//M�todo construtor -> Chama o construtor da classe pai
	Gold() 
	{
		super();
	}
	
	//M�todo fitness -> Fun��o de avalia��o
	//O fen�tipo de todas as fun��es � um ponto no plano
	//Na fun��o GOLD temos que mapear os dom�nios positivo e negativo. A fun��o resultar� apenas no dom�nio positivo
	//O dom�nio negativo (-z) deve ser tratado no c�digo
	protected double fitness(Ponto fenotipo)
	{
		double a, b, z, x, y;
	
		//Atribuir valores
		x = fenotipo.x;
		y = fenotipo.y;
		
		//Representa��o da fun��o passada na especifica��o
		a = 1 + Math.pow((x + y + 1), 2) * (19-14*Math.pow(x,2) - 14*y + 6*x*y + 3*Math.pow(y,2));
		b = 30 + Math.pow((2*x - 3*y), 2) * (18 - 32*x + 12*Math.pow(x,2) + 48*y -36*x*y + 27*Math.pow(y,2));
		z = a*b;
		
		return z;
	}

}
