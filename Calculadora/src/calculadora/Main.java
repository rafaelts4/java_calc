/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculadora;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 *
 * @author ricardo
 */
public class Main {

    private Properties props;
    private String casasDecimais;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Main main = new Main();

        try {
            main.getProp();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        while (true) {
            String resposta = main.mostrarMenu();

            switch (resposta) {
                case "1":
                    try {
                        main.resolveExpressao(main.obtemExpressao());
                    } catch (ExcecaoExpressaoInvalida e) {
                        JOptionPane.showMessageDialog(null, e.getMessage());
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "A expressão contém valores que não são operadores numéricos!");
                    }
                    break;
                case "2":
                    main.novaConfCasaDecimal();
                    break;
                case "3":
                    main.mostrarAjuda();
                    break;
                case "4":
                    System.exit(0);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opção Inválida!");
            }
        }
    }

    public void novaConfCasaDecimal() {
        String casas = JOptionPane.showInputDialog("Digite a nova quantidade de casas decimais."
                + "\nO valor atual é " + casasDecimais);

        try {
            System.out.println("casas: " + casas);
            int nrCasas = Integer.parseInt(casas);

            casasDecimais = casas;
            Properties prop = new Properties();
            OutputStream output = null;

            try {
                output = new FileOutputStream("./properties/conf.properties");

                // set the properties value
                prop.setProperty("prop.nr.casas.decimais", casasDecimais);

                // save properties to project root folder
                prop.store(output, null);
            } catch (IOException io) {
                io.printStackTrace();
            } finally {
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Por favor, informe um valor válido!");
        }
    }

    public void resolveExpressao(String expressao) throws ExcecaoExpressaoInvalida, NumberFormatException {
        if (expressao == null || expressao.trim().length() == 0
                || expressao.indexOf(" ") < 0) {
            throw new ExcecaoExpressaoInvalida();
        } else {
            String[] partes = expressao.split(" ");

            float operador1 = 0;
            float operador2 = 0;
            String operando = null;
            for (int i = 0; i < partes.length; i++) {
                if (i % 2 == 0) {
                    if (operando == null) {
                        operador1 = Float.parseFloat(partes[i]);
                    } else {
                        operador2 = Float.parseFloat(partes[i]);

                        switch (operando) {
                            case "+":
                                operador1 = operador1 + operador2;
                                break;
                            case "-":
                                operador1 = operador1 - operador2;
                                break;
                            case "/":
                                operador1 = operador1 / operador2;
                                break;
                            case "*":
                                operador1 = operador1 * operador2;
                        }

                        operando = null;
                    }
                } else {
                    operando = partes[i];
                }
            }

            JOptionPane.showMessageDialog(null, String.format("%." + casasDecimais + "f", operador1).replace(',', '.'));
            try {
                salvaNoHistorico(expressao);
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void salvaNoHistorico(String expressao) throws FileNotFoundException, IOException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int dia = cal.get(Calendar.DAY_OF_MONTH);
        int mes = cal.get(Calendar.MONTH) + 1;
        int ano = cal.get(Calendar.YEAR);
        int hora = cal.get(Calendar.HOUR_OF_DAY);
        int minuto = cal.get(Calendar.MINUTE);
        int segundo = cal.get(Calendar.SECOND);
        int semana = cal.get(Calendar.DAY_OF_WEEK);
        String semanaTxt = "";
        switch (semana) {
            case 1:
                semanaTxt = "Domingo";
                break;
            case 2:
                semanaTxt = "Segunda-Feira";
                break;
            case 3:
                semanaTxt = "Terca-Feira";
                break;
            case 4:
                semanaTxt = "Quarta-Feira";
                break;
            case 5:
                semanaTxt = "Quinta-Feira";
                break;
            case 6:
                semanaTxt = "Sexta-Feira";
                break;
            case 7:
                semanaTxt = "Sábado";
        }
        String dataStr = (dia < 10 ? "0" + dia : "" + dia) + "/"
                + (mes < 10 ? "0" + mes : "" + mes) + "/"
                + ano + " " + semanaTxt;
        String horaStr = (hora < 10 ? "0" + hora : "" + hora) + ":"
                + (minuto < 10 ? "0" + minuto : "" + minuto) + ":"
                + (segundo < 10 ? "0" + segundo : "" + segundo);

        Vector<String> vector = new Vector<String>();
        File file = new File("historico.txt");
        if (file.exists()) {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String linha = br.readLine();

            do {
                vector.add(linha);
            } while ((linha = br.readLine()) != null);
        }
        
        vector.add(dataStr + " - " + horaStr + ": " + expressao);

        FileWriter fw = new FileWriter("historico.txt");
        PrintWriter out = new PrintWriter(fw);
        for (String s : vector){
            out.println(s);
        }
        out.close();
    }

    public String obtemExpressao() {
        String mensagem = JOptionPane.showInputDialog("Informe a expressão desejada:"
                + "\nAs operações possíveis são (+, -, *, /)."
                + "\nSepare os operandor e operadores com espaço."
                + "\nExemplo: 5 + 5");

        return mensagem;
    }

    public void mostrarAjuda() {
        JOptionPane.showMessageDialog(null, "Texto de ajuda.");
    }

    public String mostrarMenu() {
        String mensagem = JOptionPane.showInputDialog("Menu:"
                + "\n1) Resolver uma expressão."
                + "\n2) Definir quantidade de casas decimais."
                + "\n3) Ajuda."
                + "\n4) Sair.");

        return mensagem;
    }

    public void getProp() throws IOException {
        props = new Properties();
        FileInputStream file = new FileInputStream("./properties/conf.properties");
        props.load(file);

        casasDecimais = props.getProperty("prop.nr.casas.decimais");
    }

}
