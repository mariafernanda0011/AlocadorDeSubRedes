/**
 * @author Maria Fernanda S. :)
 */

public class UtilitariosIp {

    /**
     * Converte um endereço IP (ex: "192.168.1.1") para um inteiro de 32 bits.
     * Essencial para realizar operações de rede (bit a bit).
     * @param ip O endereço IP em formato String.
     * @return O endereço IP como um inteiro de 32 bits.
     */
    public static int ipParaInt(String ip) {
        String[] partes = ip.split("\\.");
        if (partes.length != 4) {
            throw new IllegalArgumentException("Formato de endereço IP inválido. Use X.X.X.X");
        }
        int ipInteiro = 0;
        try {
            for (int i = 0; i < 4; i++) {
                int octeto = Integer.parseInt(partes[i]);
                if (octeto < 0 || octeto > 255) {
                    throw new IllegalArgumentException("Um octeto deve estar entre 0 e 255.");
                }
                // Desloca o octeto para a posição correta no inteiro de 32 bits.
                ipInteiro |= (octeto << (24 - (i * 8)));
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Octetos do IP devem ser números válidos.");
        }
        return ipInteiro;
    }

    /**
     * Converte um endereço IP de um inteiro de 32 bits de volta para o formato String (X.X.X.X).
     * @param ipInteiro O endereço IP como um inteiro de 32 bits.
     * @return O endereço IP em formato String.
     */
    public static String intParaIp(int ipInteiro) {
        // Extrai cada octeto por deslocamento de bits.
        return ((ipInteiro >> 24) & 0xFF) + "." +
               ((ipInteiro >> 16) & 0xFF) + "." +
               ((ipInteiro >> 8) & 0xFF) + "." +
               (ipInteiro & 0xFF);
    }

    /**
     * Calcula a máscara de sub-rede a partir de um prefixo CIDR (ex: 24).
     * @param prefixoCidr O prefixo CIDR.
     * @return A máscara de sub-rede como um inteiro de 32 bits.
     */
    public static int calcularMascaraSubrede(int prefixoCidr) {
        if (prefixoCidr < 0 || prefixoCidr > 32) {
            throw new IllegalArgumentException("Prefixo CIDR deve estar entre 0 e 32.");
        }
        // Cria a máscara.
        return (int) (0xFFFFFFFFL << (32 - prefixoCidr));
    }
}