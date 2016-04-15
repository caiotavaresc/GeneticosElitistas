package funcoes;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

//Classe que roda o algoritmo e muda os parametros, tudo isso pelo prompt 
public class Parametrizacao {
	public static void main(String[] args){
		
		String nomeDaFuncao = "Gold";
		
		/*Dicionario de Parametros
	     * 0 -> Num de cromossomos por genotipo
	     * 1 -> Num Individuos por geracao
	     * 2 -> Criterio de Parada - 0 [Convergencia] 1 [Num Geracoes]
	     * 3 -> Numero de Geracoes (So sera usado se o criterio de parada for 1)
	     * 4 -> Numero de Crossovers que ocorrerao por geracao
	     * 5 -> Tipo de Crossover - 0 [Um ponto] 1 [Dois pontos]
	     * 6 -> Probabilidade de crossover - entre 0.00 e 1.00
	     * 7 -> Tipo de Mutacao
	     * 8 -> Probabilidade de Mutacao - entra 0.00 e 1.00
	     * 9 -> Criterio de Troca de Populacao - 0 [com troca] 1 [sem troca]
	     * 10 -> Elitismo - 0 [nÃ£o] 1 [sim]*/
		
		String param0 = "50";
		String param1 = "60";
		String param2 = "0";
		String param3 = "0";
		String param4 = "60";
		String param5 = "0";
		String param6 = "0.9";
		String param7 = "0";
		String param8 = "0.05";
		String param9 = "0";
		String param10 = "0";
		
		//Diretorio onde estão os .class das funcoes (IDE: pasta bin - Editor de texto: mesma pasta dos .java)
		File Diretorio = new File("C:\\workspace\\EPdeIA\\bin");
		
		//Comando que eh executado no prompt
		String [] cmd = {"java funcoes." + nomeDaFuncao + " " + param0 + " " + param1 + " " + param2 + " " + param3 + " " + param4 + " " + param5 + " " + param6 + " " + param7 + " " + param8 + " " + param9 + " " + param10};
		
		try{
			run (cmd[0], Diretorio); //Chama a classe que realiza efetivamente o comando
		}catch(Exception e){
			e.printStackTrace();
		}		
	}
	
	protected static void run (String command, File path){
        try {
        	
        	//Instancia um objeto Process, classe que realiza comandos no prompt
        	Process proc = Runtime.getRuntime().exec(command, null, path);
        	
        	//Cria os leitores para poder imprimir na tela o que esta sendo executado no prompt
            BufferedReader stdPrint = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
                       
            String tmp;
            
            while((tmp = stdPrint.readLine()) != null){
            	System.out.println(" > " + tmp);  //Vai imprimindo na tela o que seria impresso no prompt           	 		           	
            }
            while((tmp = stdError.readLine()) != null){
            	System.out.println(command + " > " + tmp); //Se der erro, indica qual comando foi e qual erro que ocorreu            	
            }
			proc.waitFor();
			
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
