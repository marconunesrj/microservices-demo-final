package jasypt;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;

public class TestJasypt {

    // https://github.com/ulisesbocchio/jasypt-spring-boot
    public static void main(String[] args) {
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        standardPBEStringEncryptor.setPassword("Demo_Pwd!2024");  // Temos que usar esta senha para descriptografar nossas Secrets
        standardPBEStringEncryptor.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        standardPBEStringEncryptor.setIvGenerator(new RandomIvGenerator());

        String GITHUB_TOKEN = "COLOCAR AQUI O SEU GITHUB TOKEN";
        String SPRING_CLOUD_PWD = "springCloud_PWD!";

        String result = standardPBEStringEncryptor.encrypt(GITHUB_TOKEN);

        System.out.println(result);
        System.out.println(standardPBEStringEncryptor.decrypt(result));

        result = standardPBEStringEncryptor.encrypt(SPRING_CLOUD_PWD);

        System.out.println(result);
        System.out.println(standardPBEStringEncryptor.decrypt(result));
    }
}
