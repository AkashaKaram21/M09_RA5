import java.security.MessageDigest;
import java.security.spec.KeySpec;
import java.util.HexFormat;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Hashes {
    public int npass = 0; // Variable para contar los passwords probados
    
    // Método para obtener hash SHA-512 con salt
    public String getSHA512AmbSalt(String pw, String salt) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        String combined = pw + salt;
        byte[] hashBytes = md.digest(combined.getBytes());
        
        HexFormat hex = HexFormat.of();
        return hex.formatHex(hashBytes);
    }
    
    // Método para obtener hash PBKDF2 con salt
    public String getPBKDF2AmbSalt(String pw, String salt) throws Exception {
        int iterations = 10000;
        int keyLength = 128; // Cambiado a 128 bits para que coincida con el hash de 32 caracteres
        
        KeySpec spec = new PBEKeySpec(pw.toCharArray(), salt.getBytes(), iterations, keyLength);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        
        byte[] hashBytes = factory.generateSecret(spec).getEncoded();
        
        HexFormat hex = HexFormat.of();
        return hex.formatHex(hashBytes);
    }
    
    // Método auxiliar para verificar un password
    private String puTrobat(String alg, char[] aPw, int pos, char ch, String hash, String salt) throws Exception {
        aPw[pos] = ch;
        String currentPw = new String(aPw, 0, pos + 1);
        npass++; // Incrementar contador
        
        String testHash;
        if (alg.equals("SHA-512")) {
            testHash = getSHA512AmbSalt(currentPw, salt);
        } else { // PBKDF2
            testHash = getPBKDF2AmbSalt(currentPw, salt);
        }
        
        if (testHash.equals(hash)) {
            return currentPw;
        }
        return null;
    }
    
    // Método principal de fuerza bruta
    public String forcaBruta(String alg, String hash, String salt) throws Exception {
        String charset = "abcdefABCDEF12345678901";
        char[] aPw = new char[6];
        npass = 0; // Reiniciar contador
        
        String pw;
        
        // Longitud 1
        for (int i0 = 0; i0 < charset.length(); i0++) {
            if ((pw = puTrobat(alg, aPw, 0, charset.charAt(i0), hash, salt)) != null)
                return pw;
        }
        
        // Longitud 2
        for (int i0 = 0; i0 < charset.length(); i0++) {
            for (int i1 = 0; i1 < charset.length(); i1++) {
                if ((pw = puTrobat(alg, aPw, 1, charset.charAt(i1), hash, salt)) != null)
                    return pw;
            }
        }
        
        // Longitud 3
        for (int i0 = 0; i0 < charset.length(); i0++) {
            for (int i1 = 0; i1 < charset.length(); i1++) {
                for (int i2 = 0; i2 < charset.length(); i2++) {
                    if ((pw = puTrobat(alg, aPw, 2, charset.charAt(i2), hash, salt)) != null)
                        return pw;
                }
            }
        }
        
        // Longitud 4
        for (int i0 = 0; i0 < charset.length(); i0++) {
            for (int i1 = 0; i1 < charset.length(); i1++) {
                for (int i2 = 0; i2 < charset.length(); i2++) {
                    for (int i3 = 0; i3 < charset.length(); i3++) {
                        if ((pw = puTrobat(alg, aPw, 3, charset.charAt(i3), hash, salt)) != null)
                            return pw;
                    }
                }
            }
        }
        
        // Longitud 5
        for (int i0 = 0; i0 < charset.length(); i0++) {
            for (int i1 = 0; i1 < charset.length(); i1++) {
                for (int i2 = 0; i2 < charset.length(); i2++) {
                    for (int i3 = 0; i3 < charset.length(); i3++) {
                        for (int i4 = 0; i4 < charset.length(); i4++) {
                            if ((pw = puTrobat(alg, aPw, 4, charset.charAt(i4), hash, salt)) != null)
                                return pw;
                        }
                    }
                }
            }
        }
        
        // Longitud 6
        for (int i0 = 0; i0 < charset.length(); i0++) {
            for (int i1 = 0; i1 < charset.length(); i1++) {
                for (int i2 = 0; i2 < charset.length(); i2++) {
                    for (int i3 = 0; i3 < charset.length(); i3++) {
                        for (int i4 = 0; i4 < charset.length(); i4++) {
                            for (int i5 = 0; i5 < charset.length(); i5++) {
                                if ((pw = puTrobat(alg, aPw, 5, charset.charAt(i5), hash, salt)) != null)
                                    return pw;
                            }
                        }
                    }
                }
            }
        }
        
        return null; // No se encontró el password
    }
    
    // Método para formatear intervalos de tiempo 
    public String getInterval(long t1, long t2) {
        long diff = t2 - t1;
        
        long days = diff / (1000 * 60 * 60 * 24);
        long hours = (diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (diff % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (diff % (1000 * 60)) / 1000;
        long millis = diff % 1000;
        
        return String.format("%d dies / %d hores / %d minuts / %d segons / %d millis", 
                           days, hours, minutes, seconds, millis);
    }
    
    // Método main como se especifica
    public static void main(String[] args) throws Exception {
        String salt = "qpoweiruañs1kdfjz";
        String pw = "aaabF!";
        Hashes h = new Hashes();
        String[] aliashes = { h.getSHA512AmbSalt(pw, salt), h.getPBKDF2AmbSalt(pw, salt) };
        String pwTrobat = null;
        String[] algorismes = {"SHA-512", "PBKDF2"};
        
        for(int i=0; i< aliashes.length; i++){
            System.out.printf("---\n");
            System.out.printf("Algorisme: %s\n", algorismes[i]);
            System.out.printf("Hash: %s\n", aliashes[i]);
            System.out.printf("---\n");
            System.out.printf("-- Inici de força bruta ---\n");

            long tl = System.currentTimeMillis();
            pwTrobat = h.forcaBruta(algorismes[i], aliashes[i], salt);
            long t2 = System.currentTimeMillis();

            System.out.printf("Pass : %s\n", pwTrobat);
            System.out.printf("Provats: %d\n", h.npass);
            System.out.printf("Temps : %s\n", h.getInterval(tl, t2));
            System.out.printf("---\n\n");
        }
    }
}