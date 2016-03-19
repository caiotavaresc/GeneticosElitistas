package algoritmosGeneticos;

//A classe ponto mapeia objetos que representam um ponto no plano
//Eles serão entrada para a função fitness e o retorno da função de mapeamento

public class Ponto {
	
	//Atributos: Posições x e y
	public double x;
	public double y;
	
	//Construtor - atribui automaticamente o x e o y
	public Ponto(double posX, double posY)
	{
		this.x = posX;
		this.y = posY;
	}
}
