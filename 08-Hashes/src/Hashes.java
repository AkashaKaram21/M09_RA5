import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Hashes {

    
    private static final char[] CHARSET = "abcdefABCDEF1234567890!".toCharArray(); // caràcters per a l'atac de força bruta
    private static final int MAX_LENGTH = 3;  // Longitud maxima del password
    private int npass = 0;  // Variable per comptar quants passwords es proven

    /**
     * Genera un hash SHA-512 amb salt
     */
    public String getSHA512AmbSalt(String pw, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] hash = md.digest(pw.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Genera un hash PBKDF2 amb salt (més segur)
     */
    public String getPBKDF2AmbSalt(String pw, String salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(pw.toCharArray(), salt.getBytes(), 65536, 256);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = skf.generateSecret(spec).getEncoded();
            spec.clearPassword();
            return bytesToHex(hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Funcio auxiliar: converteix un array de bytes a Hexadecimal
     */
    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * Atac de força bruta per trobar el password original a partir del hash i el salt
     */
    public String forcaBruta(String algorisme, String hash, String salt) {
        npass = 0; // Reiniciar comptador
        char[] password = new char[MAX_LENGTH];

        // Cridem recursivament per totes les longituds possibles
        for (int len = 1; len <= MAX_LENGTH; len++) {
            if (bruteForceRecursive(algorisme, hash, salt, password, len, 0)) {
                return new String(password).trim();
            }
        }
        return null;
    }

    /**
     * Metode recursiu que genera totes les combinacions possibles de passwords
     */
    private boolean bruteForceRecursive(String algorisme, String hash, String salt,
                                        char[] password, int length, int position) {
        if (position == length) {
            npass++;
            String candidate = new String(password, 0, length);
            String candidateHash;

            if ("SHA-512".equals(algorisme)) {
                candidateHash = getSHA512AmbSalt(candidate, salt);
            } else if ("PBKDF2".equals(algorisme)) {
                candidateHash = getPBKDF2AmbSalt(candidate, salt);
            } else {
                return false;
            }

            if (candidateHash.equals(hash)) {
                System.out.println("✅ Password trobat: " + candidate);
                return true;
            }
            return false;
        }

        for (char c : CHARSET) {
            password[position] = c;
            if (bruteForceRecursive(algorisme, hash, salt, password, length, position + 1)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calcula l'interval de temps entre dos marques de temps
     */
    public String getInterval(long t1, long t2) {
        long diff = t2 - t1;

        long millis = diff % 1000;
        diff /= 1000;
        long segons = diff % 60;
        diff /= 60;
        long minuts = diff % 60;
        diff /= 60;
        long hores = diff % 24;
        long dies = diff / 24;

        return String.format("%d dies / %d hores / %d minuts / %d segons / %d millis",
                dies, hores, minuts, segons, millis);
    }

    /**
     * Mètode principal per provar els algorismes
     */
    public static void main(String[] args) throws Exception {
        String salt = "qpoweiruañslkdfjz";
        String pw = "aF!";
        Hashes h = new Hashes();

        String[] aHashes = {
            h.getSHA512AmbSalt(pw, salt),
            h.getPBKDF2AmbSalt(pw, salt)
        };

        String pwTrobat = null;
        String[] algorismes = {"SHA-512", "PBKDF2"};

        for (int i = 0; i < aHashes.length; i++) {
            System.out.printf("===========================\n");
            System.out.printf("Algorisme: %s\n", algorismes[i]);
            System.out.printf("Hash: %s\n", aHashes[i]);
            System.out.printf("---------------------------\n");
            System.out.printf("-- Inici de força bruta ---\n");

            long t1 = System.currentTimeMillis();
            pwTrobat = h.forcaBruta(algorismes[i], aHashes[i], salt);
            long t2 = System.currentTimeMillis();

            System.out.printf("Pass : %s\n", pwTrobat);
            System.out.printf("Provats: %d\n", h.npass);
            System.out.printf("Temps : %s\n", h.getInterval(t1, t2));
            System.out.printf("---------------------------\n\n");
        }
    }
}