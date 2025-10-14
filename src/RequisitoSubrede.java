import java.util.Comparator;

/**
 * @author Maria Fernanda S. :)
 */

public class RequisitoSubrede {
    private String nome;
    private int hostsNecessarios;
    private int prefixoNecessario; 
    private int enderecoRedeAlocadoInt;

    /**
     * Construtor da classe RequisitoSubrede.
     * @param nome O nome da sub-rede.
     * @param hostsNecessarios O número de hosts necessários.
     */
    public RequisitoSubrede(String nome, int hostsNecessarios) {
        this.nome = nome;
        this.hostsNecessarios = hostsNecessarios;
        // Calcula qual prefixo CIDR será necessário para acomodar os hosts.
        this.prefixoNecessario = calcularPrefixoNecessario(hostsNecessarios);
    }

    /**
     * Calcula o menor prefixo CIDR que pode acomodar o número de hosts especificado.
     * Necessita de 2 endereços extras (Rede e Broadcast).
     * @param hosts O número de hosts desejados.
     * @return O prefixo CIDR necessário (ex: 27 para 30 hosts).
     */
    private int calcularPrefixoNecessario(int hosts) {
        if (hosts < 0) {
            throw new IllegalArgumentException("Número de hosts deve ser não negativo.");
        }
        if (hosts == 0) {
            return 32;
        }

        // Soma 2 para os endereços de Rede e Broadcast.
        int totalEnderecosNecessarios = hosts + 2; 
        int bits = 0; // Bits disponíveis para hosts.

        // Encontra o menor 'bits' tal que 2^bits seja suficiente.
        while ((1 << bits) < totalEnderecosNecessarios) {
            bits++;
        }
        // O prefixo CIDR é 32 (total de bits de um IPv4) menos os bits de hosts.
        return 32 - bits;
    }

    // Métodos Getters 
    public String getNome() {
        return nome;
    }
    public int getHostsNecessarios() {
        return hostsNecessarios;
    }
    public int getPrefixoNecessario() {
        return prefixoNecessario;
    }
    public int getEnderecoRedeAlocadoInt() {
        return enderecoRedeAlocadoInt;
    }

    // Método Setter
    public void setEnderecoRedeAlocadoInt(int enderecoRedeAlocadoInt) {
        this.enderecoRedeAlocadoInt = enderecoRedeAlocadoInt;
    }

    /**
     * Comparator para ordenar as sub-redes pelo prefixo CIDR de forma ASCENDENTE.
     * O prefixo menor (bloco maior) é priorizado, seguindo a regra VLSM.
     */
    public static Comparator<RequisitoSubrede> POR_PREFIXO_ASC = new Comparator<RequisitoSubrede>() {
        @Override
        public int compare(RequisitoSubrede s1, RequisitoSubrede s2) {
            // Ordena pelo prefixo.
            return Integer.compare(s1.getPrefixoNecessario(), s2.getPrefixoNecessario());
        }
    };
}