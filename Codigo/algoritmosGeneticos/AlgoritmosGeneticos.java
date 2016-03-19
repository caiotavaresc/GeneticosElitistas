package algoritmosGeneticos;
import java.util.*;

//A classe AlgoritmosGeneticos implementar� a l�gica geral dos algoritmos gen�ticos
//Ser� utilizada como superclasse para especializa��o dos problemas
public abstract class AlgoritmosGeneticos {

	/*-----------------| Espa�o dos Atributos |-----------------*/
	
	//geracao -> Lista de elementos da gera��o atual da popula��o
	//Cada elemento dever� mapear o valor x e o valor y do ponto em bin�rio
	//Cada fun��o fitness ter� seu pr�prio modelo de indiv�duo, portanto eles ser�o mapeados nas classes filhas
	List<int[]> geracao;
	
	//indGeracao -> Indicador da gera��o em que a popula��o est�
	int indGeracao;
	
	//rand -> operador aleat�rio
	Random rand;
	
	/*-----------------| Espa�o dos M�todos |-----------------*/	
	
	//Construtor -> Por enquanto s� inicializa rand
	protected AlgoritmosGeneticos()
	{
		this.rand = new Random();
	}
	
	//geradorInicial -> Cria a primeira gera��o - Sup�e que o alfabeto � {0,1}
	//Recebe como par�metro o n�mero de indiv�duos que ser�o criados na gera��o
	void geradorInicial(int n)
	{
		//La�o que cria todos os indiv�duos
		for(int i = 0; i < n; i++)
			//Iterar por cada gene do cromossomo dando um valor aleat�rio
			for(int j = 0; j < this.geracao.get(i).length; j++)
				this.geracao.get(i)[j] = rand.nextInt(2);
	}
	
	//imprimeGeracao -> imprime os elementos da gera��o atual
	//Depois podemos definir algo para separar mantissa e expoente na representa��o decimal.
	void imprimeGeracao()
	{
		Iterator<int[]> i;
		int[] temp;
		int j;
		
		i = this.geracao.iterator();
		
		//Navegar nos elementos
		while(i.hasNext())
		{
			temp = i.next();
			
			System.out.print("Indiv�duo " + i + ": ");
			
			//Imprimir o indiv�duo
			for(j = 0; j < temp.length; j++)
				System.out.print(temp[j]);
			
			System.out.println("");
		}
	}
	
	//Fitness � abstrato porque cada filho definir� o seu fitness
	//Receber� como entrada um fen�tipo e devolver� uma avalia��o
	//O fen�tipo de todas as fun��es � um ponto no plano
	protected abstract double fitness(Ponto fenotipo);
}
