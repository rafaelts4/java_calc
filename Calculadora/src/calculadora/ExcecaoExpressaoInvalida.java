/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package calculadora;

/**
 *
 * @author ricardo
 */
public class ExcecaoExpressaoInvalida extends Exception{

    @Override
    public String getMessage() {
        return "A expressão foi construída de maneira incorreta!";
    }
    
    
    
}
