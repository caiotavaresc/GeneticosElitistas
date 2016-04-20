package funcoes;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;

//Classe que roda o algoritmo e muda os parametros, tudo isso pelo prompt 
public class Parametrizacao {
	public static void main(String[] args){
		
		String nomeDaFuncao = "Gold1";
		
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
		
		String[] param0 = {"20", "50", "80"};
		String[] param1 = {"50", "200", "1000"};
		String[] param2 = {"0", "1"};
		String[] param3 = {"50", "100"};//SOH SE O DE CIMA FOR 1
		String[] param4 = {"0.75", "1", "1.5"}; //MULTIPLICA PARAM 1
		String[] param5 = {"0", "1"};
		String[] param6 = {"0.5", "0.7", "0.9", "1"};
		String[] param7 = {"0", "1"};
		String[] param8 = {"0.001", "0.005", "0.01", "0.025", "0.05"};
		String[] param9 = {"0", "1"};
		String[] param10 = {"false", "true"};
                String param11 = "5";
                
                //Diretorio onde estão os .class das funcoes (IDE: pasta bin - Editor de texto: mesma pasta dos .java)
		File Diretorio = new File("C:\\Users\\sousa\\OneDrive\\Documentos\\NetBeansProjects\\EP1IA\\build\\classes");
                ArrayList<String> comandos = new ArrayList();
                
                for(int i = 0; i < param0.length; i++)
                    for(int j = 0; j < param1.length; j++)
                        for(int k = 0; k < param2.length; k++)
                            if(param2[k].equals("1"))
                                for(int l = 0; l < param3.length; l++)
                                    for(int m = 0; m < param4.length; m++)
                                        for(int n = 0; n < param5.length; n++)
                                            for(int o = 0; o < param6.length; o++)
                                                for(int p = 0; p < param7.length; p++)
                                                    for(int q = 0; q < param8.length; q++)
                                                        for(int r = 0; r < param9.length; r++)
                                                            if(param9[r].equals("1"))                                                            
                                                                comandos.add("java funcoes." + nomeDaFuncao + " " + param0[i] + " " + param1[j] + " " + param2[k] + " " + param3[l] + " " + (param4[m]) + " " + param5[n] + " " + param6[o] + " " + param7[p] + " " + param8[q] + " " + param9[r] + " " + param10[0] + " " + param11);
                                                            else
                                                                for(int s = 0; s < param10.length; s++)
                                                                    comandos.add("java funcoes." + nomeDaFuncao + " " + param0[i] + " " + param1[j] + " " + param2[k] + " " + param3[l] + " " + (param4[m]) + " " + param5[n] + " " + param6[o] + " " + param7[p] + " " + param8[q] + " " + param9[r] + " " + param10[s] + " " + param11);
                            else
                                for(int m = 0; m < param4.length; m++)
                                        for(int n = 0; n < param5.length; n++)
                                            for(int o = 0; o < param6.length; o++)
                                                for(int p = 0; p < param7.length; p++)
                                                    for(int q = 0; q < param8.length; q++)
                                                        for(int r = 0; r < param9.length; r++)
                                                            if(param9[r].equals("1"))
                                                                comandos.add("java funcoes." + nomeDaFuncao + " " + param0[i] + " " + param1[j] + " " + param2[k] + " " + param3[0] + " " + param4[m] + " " + param5[n] + " " + param6[o] + " " + param7[p] + " " + param8[q] + " " + param9[r] + " " + param10[0] + " " + param11);
                                                            else
                                                                for(int s = 0; s < param10.length; s++)
                                                                    comandos.add("java funcoes." + nomeDaFuncao + " " + param0[i] + " " + param1[j] + " " + param2[k] + " " + param3[0] + " " + param4[m] + " " + param5[n] + " " + param6[o] + " " + param7[p] + " " + param8[q] + " " + param9[r] + " " + param10[s] + " " + param11);
		
		
		
		//Comando que eh executado no prompt
		//String [] cmd = {"java funcoes." + nomeDaFuncao + " " + param0 + " " + param1 + " " + param2 + " " + param3 + " " + param4 + " " + param5 + " " + param6 + " " + param7 + " " + param8 + " " + param9 + " " + param10 + " " + param11};
		int execucao = 0;
                for(execucao = 6480; execucao< comandos.size(); execucao++)
                    try{
                        System.out.println("EXECUCAO "+ execucao+"\n"+comandos.get(execucao));
                    	run (comandos.get(execucao), Diretorio); //Chama a classe que realiza efetivamente o comando
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
