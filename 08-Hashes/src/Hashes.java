import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import java.util.HexFormat;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Hashes {

        //Creamos un contador para el password
        public int npass = 0;

        public String getSHA512AmbSalt(String pw, String salt){

            try {
                
                //1.-Combinamos el password con el salt
                String combined = pw + salt;

                //2.- Obtenemos la instancia de algoritmo SHA-512
                MessageDigest digest = MessageDigest.getInstance("SHA-512");

                //3.-Convertimos el string combinado en byte
                byte[] inputBytes = combined.getBytes();

                //4.-Calcular el hash
                byte[] hashBytes = digest.digest(inputBytes);

                //5.-Convertim bytes a String hexadecimal
                HexFormat hex = HexFormat.of();
                String hashResult = hex.formatHex(hashBytes);

                return hashResult;


            } catch (NoSuchAlgorithmException e) {
                System.out.println("Error:Algoritme SHA-512 no disponible");
                
                e.printStackTrace();
                return null;
            }

        }
        public String getPBKDF2AmbSalt(String pw, String salt){
           
            try {
                byte[] saltByte = salt.getBytes();

                KeySpec spec = new  PBEKeySpec(
                    pw.toCharArray(),
                    saltByte,
                    65536,
                    512
                );

               // 3. Obtener la instancia de PBKDF2
                SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        
                // 4. Generar el hash
                byte[] hashBytes = factory.generateSecret(spec).getEncoded();
                
                // 5. Convertir a hexadecimal (igual que antes)
                HexFormat hex = HexFormat.of();
                String hashResult = hex.formatHex(hashBytes);
                
                return hashResult;
                
            } catch (Exception e) {
                System.err.println("Error en PBKDF2: " + e.getMessage());
                return null;
            }
        }
        

        public String forcaBruta(String alg, String hash, String salt){

        }
        public String getInterval(long t1, long t2){

        }

        public static void main(String[] args) throws Exception {
        String salt = "qpowetruañalxdfjz";
        String pw = "aaabF1";
        Hashes h = new Hashes();
        String[] aliashes = { h.getSHAS12AmbSalt(pw, salt), h.getPBKDF2AmbSalt(pw, salt) };
        String pwTrobat = null;
        String[] algorismes = {"SHA-512", "PBKDF2"};
        for(int i=0; i< aliashes.length; i++) {
            System.out.printf("---\n");
            System.out.printf("Algorisme: %s\n", algorismes[i]);
            System.out.printf("Hash: %s\n",aliashes[i]);
            System.out.printf("---\n");
            System.out.printf("-- Inici de força bruta ---\n");

            long t1 = System.currentTimeMillis();
            pwTrobat = h.forcaBruta(algorismes[i], aliashes[i], salt);
            long t2 = System.currentTimeMillis();

            System.out.printf("Pass : %s\n", pwTrobat);
            System.out.printf("Provats: %d\n", h.npass);
            System.out.printf("Temps : %s\n", h.getInterval(t1, t2));
            System.out.printf("---\n\n");
        }
    }   
}
