package algoritmosGeneticos;

//A classe ponto mapeia objetos que representam um ponto no plano
//Eles ser�o entrada para a fun��o fitness e o retorno da fun��o de mapeamento

public class Ponto {
	
	//Atributos: Posi��es x e y
	public double x;
	public double y;
	
	//Construtor - atribui automaticamente o x e o y
	public Ponto(double posX, double posY)
	{
		this.x = posX;
		this.y = posY;
	}
}
