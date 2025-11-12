package src;

public class RotX {
    static char[] majuscules = "AÀÁÄBCÇDEÈÉËFGHIÌÍÏJKLMNÑOÒÓÖPQRSTUÙÚÜVWXYZ".toCharArray();
    static char[] minuscules = "aàáäbcçdeèéëfghiìíïjklmnñoòóöpqrstuùúüvwxyz".toCharArray();

    public static String xifraRotX(String cadena, int desplacament) {
        String resultat = "";
        
        for (int i = 0; i < cadena.length(); i++) {
            char c = cadena.charAt(i);
            boolean trobat = false;
            
            for (int j = 0; j < minuscules.length; j++) {
                if (c == minuscules[j]) {
                    resultat += minuscules[(j + desplacament) % minuscules.length];
                    trobat = true;
                    break;
                }
            }
            
            if (!trobat) {
                for (int j = 0; j < majuscules.length; j++) {
                    if (c == majuscules[j]) {
                        resultat += majuscules[(j + desplacament) % majuscules.length];
                        trobat = true;
                        break;
                    }
                }
            }
            
            if (!trobat) {
                resultat += c;
            }
        }
        
        return resultat;
    }

    public static String desxifraRotX(String cadena, int desplacament) {
        String resultat = "";

        for (int i = 0; i < cadena.length(); i++) {
            char c = cadena.charAt(i);
            boolean trobat = false;

            for (int j = 0; j < minuscules.length; j++) {
                if (c == minuscules[j]) {
                    int pos = (j - desplacament) % minuscules.length;
                    if (pos < 0) pos += minuscules.length;
                    resultat += minuscules[pos];
                    trobat = true;
                    break;
                }
            }

            if (!trobat) {
                for (int j = 0; j < majuscules.length; j++) {
                    if (c == majuscules[j]) {
                        int pos = (j - desplacament) % majuscules.length;
                        if (pos < 0) pos += majuscules.length;
                        resultat += majuscules[pos];
                        trobat = true;
                        break;
                    }
                }
            }

            if (!trobat) {
                resultat += c;
            }
        }
        
        return resultat;
    }
    
    public static void forcaBrutaRotX(String cadena) {
        int max = majuscules.length;

        for (int d = 0; d < max; d++) {
            String resultat = desxifraRotX(cadena, d);
            System.out.println("(" + d + ")->" + resultat);
        }
    }

    public static void main(String[] args) {
        System.out.println("Xifrat");
        System.out.println("------");
        System.out.println("(0)-ABC => " + xifraRotX("ABC", 0));
        System.out.println("(2)-XYZ => " + xifraRotX("XYZ", 2));
        System.out.println("(4)-Hola, Mr. calçot => " + xifraRotX("Hola, Mr. calçot", 4));
        System.out.println("(6)-Perdó, per tu què és? => " + xifraRotX("Perdó, per tu què és?", 6));

        System.out.println("Desxifrat");
        System.out.println("---------");
        System.out.println("(0)ABC => " + desxifraRotX("ABC", 0));
        System.out.println("(2)ZAÁ => " + desxifraRotX("ZAÁ", 2));
        System.out.println("(4)Ïqoc, Óú. écoèqü => " + desxifraRotX("Ïqoc, Óú. écoèqü", 4));
        System.out.println("(6)Úiüht, úiü wx ùxì ív? => " + desxifraRotX("Úiüht, úiü wx ùxì ív?", 6));

        System.out.println("Missatge xifrat: Úiüht, úiü wx ùxì ív?");
        System.out.println("----------------");
        forcaBrutaRotX("Úiüht, úiü wx ùxì ív?");
    }
}