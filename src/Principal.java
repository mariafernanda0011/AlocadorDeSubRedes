import java.util.ArrayList; 
import java.util.List; 
import java.util.Scanner; 
import java.util.regex.Matcher; 
import java.util.regex.Pattern; 

/**
 * @author Maria Fernanda S. :)
 */

public class Principal {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); 
        System.out.println("[ --- Calculadora de Sub-redes IPv4 --- ]");

        try { 
            // 1º Entrada da Rede Base
            System.out.print("Digite o endereço IP base da sua rede (ex: 192.168.10.0/24): ");
            String redeBaseCidr = scanner.nextLine(); 

            // Validação e Extração
            String[] partesBase = redeBaseCidr.split("/"); 
            if (partesBase.length != 2) { 
                throw new IllegalArgumentException("Formato da rede base inválido. Use IP/CIDR (ex: 192.168.10.0/24).");
            }
            String ipBase = partesBase[0]; 
            int prefixoCidrBase = Integer.parseInt(partesBase[1]); 

            // Endereços de Rede e Limites do Bloco Base
            int redeBaseInteiro = UtilitariosIp.ipParaInt(ipBase) & UtilitariosIp.calcularMascaraSubrede(prefixoCidrBase);
            int proximoEnderecoDisponivelInt = redeBaseInteiro;
            int broadcastMaximoPermitido = redeBaseInteiro | (~UtilitariosIp.calcularMascaraSubrede(prefixoCidrBase));

            // 2º Coleta dos Requisitos de Sub-rede
            System.out.println("\n[ --- Insira os requisitos de sub-rede --- ]");
            System.out.println("Formato: <nome>- <numero de hosts> (ex: servidores - 20)");
            System.out.println("Digite '.' para terminar.");

            List<RequisitoSubrede> requisitos = new ArrayList<>(); 
            String linha;
            // Padrão de expressão regular para "nome-  numero"
            Pattern padrao = Pattern.compile("(.+)- (\\d+)");

            // Loop para ler os requisitos
            while (!(linha = scanner.nextLine()).equalsIgnoreCase(".")) {
                Matcher verificador = padrao.matcher(linha);
                if (verificador.matches()) {
                    String nome = verificador.group(1).trim(); // Captura o nome
                    int hosts = Integer.parseInt(verificador.group(2)); // Captura os hosts
                    requisitos.add(new RequisitoSubrede(nome, hosts));
                } else {
                    System.err.println("Formato inválido. Use <nome>- <numero de hosts> (ex: administracao - 20).");
                }
            }

            // 3º Ordenação dos Requisitos (VLSM: Maior bloco primeiro)
            requisitos.sort(RequisitoSubrede.POR_PREFIXO_ASC);

            System.out.println("\n[ --- Alocações de Sub-redes --- ]");
            boolean alocacaoBemSucedida = true;

            // 4º Processo de Alocação de Sub-redes 
            for (RequisitoSubrede requisito : requisitos) {
                int prefixoRequisito = requisito.getPrefixoNecessario();
                int mascaraSubredeRequisito = UtilitariosIp.calcularMascaraSubrede(prefixoRequisito);
                int tamanhoBlocoRequisito = (int) (1L << (32 - prefixoRequisito));

                // Alinhamento da Rede: Garante que a rede comece em um múltiplo do seu tamanho de bloco.
                int enderecoRedeAlinhado = (proximoEnderecoDisponivelInt + tamanhoBlocoRequisito - 1) & mascaraSubredeRequisito;

                // Verificação dos Limites 
                int enderecoBroadcastProposto = enderecoRedeAlinhado | (~mascaraSubredeRequisito);
                
                if (enderecoBroadcastProposto > broadcastMaximoPermitido) {
                    System.err.println("ERRO: Não foi possível alocar '" + requisito.getNome() + "' (/" + prefixoRequisito + " para " + requisito.getHostsNecessarios() + " hosts)." +
                                       " Excede o bloco da rede base " + redeBaseCidr + ".");
                    alocacaoBemSucedida = false;
                    break;
                }

                requisito.setEnderecoRedeAlocadoInt(enderecoRedeAlinhado);

                // Exibindo os Detalhes da Sub-rede Alocada 
                System.out.println("\n --- " + requisito.getNome() + " --- ");
                System.out.println("  Hosts requeridos: " + requisito.getHostsNecessarios());
                System.out.println("  Prefixo alocado: /" + prefixoRequisito);

                int enderecoBroadcastInt = requisito.getEnderecoRedeAlocadoInt() | (~mascaraSubredeRequisito);
                long totalHosts = (1L << (32 - prefixoRequisito));
                long hostsUtilizaveis = totalHosts >= 2 ? totalHosts - 2 : 0;

                System.out.println("  Endereço de Rede: " + UtilitariosIp.intParaIp(requisito.getEnderecoRedeAlocadoInt()));
                System.out.println("  Máscara de Sub-rede: " + UtilitariosIp.intParaIp(mascaraSubredeRequisito));
                System.out.println("  Endereço de Broadcast: " + UtilitariosIp.intParaIp(enderecoBroadcastInt));
                System.out.println("  Faixa de Hosts: " + (hostsUtilizaveis > 0 ? UtilitariosIp.intParaIp(requisito.getEnderecoRedeAlocadoInt() + 1) + " - " + UtilitariosIp.intParaIp(enderecoBroadcastInt - 1) : "Nenhum"));
                System.out.println("  Hosts disponíveis: " + hostsUtilizaveis + " (total: " + totalHosts + ")");

                // Atualiza o próximo endereço disponível
                proximoEnderecoDisponivelInt = enderecoBroadcastInt + 1;
            }

            // 5º Resumo Final da alocação
            if (alocacaoBemSucedida) {
                System.out.println("\n[ --- Resumo Final --- ]");
                System.out.println("Rede Base Utilizada: " + UtilitariosIp.intParaIp(redeBaseInteiro) + "/" + prefixoCidrBase);
                System.out.println("Próximo endereço disponível após alocações: " + UtilitariosIp.intParaIp(proximoEnderecoDisponivelInt));
            }

        } catch (NumberFormatException e) {
            System.err.println("Erro: O prefixo CIDR ou o número de hosts devem ser números inteiros válidos.");
        } catch (IllegalArgumentException e) {
            System.err.println("Erro: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ocorreu um erro inesperado: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}