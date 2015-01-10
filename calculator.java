import java.util.Scanner;    
public class calculator {    
 	static int decimalPlaces = 2;
    public static void main (String args[]){   
   
        short operation;
        double num1, num2;    
       
        Scanner in = new Scanner(System.in);
        
        do {
             System.out.println("\nDigite o número da operação desejada:");
             System.out.println("  1. Soma");      
             System.out.println("  2. Subtracao");      
             System.out.println("  3. Multiplicacao");      
             System.out.println("  4. Divisao");   
             System.out.println("  5. Configurar Casas Decimais");      
             System.out.println("  0. Sair");
             System.out.print("Operação: ");
             operation = in.nextShort();
             
             if (operation == 0) {
            	 System.out.println("Bye bye!");
            	 break;
             }
             
             if (operation == 5) {
             	changeDecimalPlaces();
             	continue;
             }
             
             if (!isOperationValid(operation)) {
            	 continue;
             }
             
             if (!isOperationValid(operation)) {
            	 continue;
             }
             
             System.out.print("Digite o primeiro valor: ");
             num1 = in.nextDouble();
             
             System.out.print("Digite o segundo valor: ");
             num2 = in.nextDouble();
             
             if (!isInputDataValid(operation, num1, num2)) {
            	 continue;
             }
        
        	System.out.printf("\nOperação: %s \nResultado %."+decimalPlaces+"f \n",getOperationName(operation), calculate(operation, num1, num2));	
        
        } while (operation != 0);
    }
    
    static void changeDecimalPlaces () {
       	try{
    			Scanner in = new Scanner(System.in);
       			System.out.println("Digite a quantidade de casas decimais desejada");
       			decimalPlaces = in.nextInt();
       			System.out.println("mudança efetuada com sucesso!");
       	}
       	catch(Exception ex)
       	{
       		System.out.println("Erro ao informar casas decimais. Verifique se informou um valor inteiro.");
       	}
    }
    
    static double calculate (short operation, double num1, double num2) {
    	double result = 0;
    	switch (operation) {
    		case 1: //soma
    			result = num1 + num2;
    			break;
    		case 2: //substração
    			result = num1 - num2;
    			break;
    		case 3: //multiplicação
    			result = num1 * num2;
    			break;
    		case 4: //divisão
    			result = num1 / num2;
    			break;
    	}
    	return result;
    }
    
    static boolean isOperationValid (short operation) {
       	boolean retorno = true;
    	if (operation > 4) {
       		System.out.println("ERRO: operação invalida.\n");
       		retorno = false;
       	}
    	return retorno;
    }
    
    static boolean isInputDataValid (short operation, double num1, double num2) {
    	boolean retorno = true; 
    	if (operation == 4 & num2 == 0) {
        	 System.out.println("ERRO: Divisor nao pode ser zero.\n");
        	 retorno = false;
        }
    	return retorno;
    }
    
    static String getOperationName (short operation) {
    	
    	switch (operation) {
		case 1:
			return "soma";
		case 2:
			return "subtracao";
		case 3: 
			return "multiplicacao";
		case 4: 
			return "divisao";
    	default:
    		return "undefined";
    	}
    	
    }
} 
