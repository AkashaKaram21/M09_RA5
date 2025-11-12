public class Rot13 {
    static char[] majuscules = "AÀÁÄBCÇDEÈÉËFGHIÌÍÏJKLMNÑOÒÓÖPQRSTUÙÚÜVWXYZ".toCharArray();
    static char[] minuscules = "aàáäbcçdeèéëfghiìíïjklmnñoòóöpqrstuùúüvwxyz".toCharArray();

    public static String xifraRot13(String cadena) {
        String resultat = "";
        
        for (int i = 0; i < cadena.length(); i++) {
            char c = cadena.charAt(i);
            boolean trobat = false;
            
            for (int j = 0; j < minuscules.length; j++) {
                if (c == minuscules[j]) {
                    resultat += minuscules[(j + 13) % minuscules.length];
                    trobat = true;
                    break;
                }
            }
            
            if (!trobat) {
                for (int j = 0; j < majuscules.length; j++) {
                    if (c == majuscules[j]) {
                        resultat += majuscules[(j + 13) % majuscules.length];
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

    public static String desxifraRot13(String cadena) {
        String resultat = "";

        for (int i = 0; i < cadena.length(); i++) {
            char c = cadena.charAt(i);
            boolean trobat = false;
            
            for (int j = 0; j < minuscules.length; j++) {
                if (c == minuscules[j]) {
                    resultat += minuscules[(j - 13 + minuscules.length) % minuscules.length];
                    trobat = true;
                    break;
                }
            }
            
            if (!trobat) {
                for (int j = 0; j < majuscules.length; j++) {
                    if (c == majuscules[j]) {
                        resultat += majuscules[(j - 13 + majuscules.length) % majuscules.length];
                        trobat = true;
                        break;
                    }
                }
            }
            
            // AQUÍ ESTABA EL ERROR - FALTABA ESTA LÍNEA:
            if (!trobat) {
                resultat += c;
            }
        }
        
        return resultat;
    }

    public static void main(String[] args) {
        String e1 = "ABC";
        String e2 = "XYZ";
        String e3 = "Hola, Mr. calçot";
        String e4 = "Perdó, per tu què és?";

        System.out.println("---------");
        System.out.println(e1 + " => " + xifraRot13(e1));
        System.out.println(e2 + " => " + xifraRot13(e2));
        System.out.println(e3 + " => " + xifraRot13(e3));
        System.out.println(e4 + " => " + xifraRot13(e4));

        System.out.println("Desxifrat");
        System.out.println("---------");

        System.out.println(xifraRot13(e1) + " => " + desxifraRot13(xifraRot13(e1)));
        System.out.println(xifraRot13(e2) + " => " + desxifraRot13(xifraRot13(e2)));
        System.out.println(xifraRot13(e3) + " => " + desxifraRot13(xifraRot13(e3)));
        System.out.println(xifraRot13(e4) + " => " + desxifraRot13(xifraRot13(e4)));
    }
}