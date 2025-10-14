# Calculadora de Alocação de Sub-redes IPv4

![Java logo](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)

## 💻 Descrição do Projeto

Este projeto implementa uma calculadora de alocação de sub-redes (Subnet Allocator) em Java. Diferente de uma calculadora CIDR tradicional, esta ferramenta permite ao usuário fornecer uma lista de requisitos de hosts por nome (ex: servidores- 20, laboratório- 50), e a aplicação realiza a alocação de endereços de forma otimizada, seguindo o princípio **VLSM (Variable Length Subnet Masking)**.

A principal lógica é garantir a alocação eficiente de endereços IP ao processar as sub-redes **em ordem decrescente de tamanho (maior bloco primeiro)**, evitando a fragmentação e garantindo que os blocos maiores sempre tenham espaço contíguo.

## ✨ Funcionalidades

* **Cálculo Automático de CIDR:** Determina o prefixo CIDR (`/n`) mínimo necessário com base na quantidade de hosts solicitada + 2 (Rede e Broadcast).
* **Alocação Otimizada (VLSM):** Ordena os requisitos pela maior necessidade de hosts antes de iniciar a alocação.
* **Alinhamento de Bloco:** Garante que cada nova sub-rede comece no endereço IP correto (múltiplo do seu tamanho de bloco).
* **Validação de Limite:** Verifica se a alocação das sub-redes não excede o bloco da rede base fornecida pelo usuário.
* **Detalhes Completos:** Fornece Endereço de Rede, Máscara de Sub-rede, Broadcast, Faixa de Hosts e o próximo endereço IP disponível.

## ⚙️ Estrutura do Código

O projeto é dividido em três classes principais, seguindo o [princípio da responsabilidade única](https://pt.wikipedia.org/wiki/O_princ%C3%ADpio_da_responsabilidade_%C3%BAnica):

1.  **`Principal.java`:** Contém o método `main()`, lida com a entrada/saída do console, orquestra a coleta de requisitos, a ordenação e o loop de alocação.
2.  **`RequisitoSubrede.java`:** Uma classe modelo que armazena os dados de um requisito de sub-rede. Inclui a lógica para calcular o **prefixo CIDR mínimo** necessário para uma quantidade de hosts.
3.  **`UtilitariosIp.java`:** Uma classe utilitária com métodos estáticos para conversão entre endereços IP (String) e números inteiros (Int), e para o cálculo da Máscara de Sub-rede.

### Exemplo de Uso

Ao executar, o programa solicitará:

1.  **Rede Base:** `192.168.10.0/24`
2.  **Requisitos:**
    * `laboratorio - 50`
    * `servidores - 20`
    * `.` (para terminar)

**O resultado será:**

```plaintext
[ --- Alocações de Sub-redes --- ]

 --- laboratorio --- 
  Hosts requeridos: 50
  Prefixo alocado: /26
  Endereço de Rede: 192.168.10.0
  Máscara de Sub-rede: 255.255.255.192
  Endereço de Broadcast: 192.168.10.63
  Faixa de Hosts: 192.168.10.1 - 192.168.10.62
  Hosts disponíveis: 62 (total: 64)

 --- servidores ---
  Hosts requeridos: 20
  Prefixo alocado: /27
  Endereço de Rede: 192.168.10.64
  Máscara de Sub-rede: 255.255.255.224
  Endereço de Broadcast: 192.168.10.95
  Faixa de Hosts: 192.168.10.65 - 192.168.10.94
  Hosts disponíveis: 30 (total: 32)

[ --- Resumo Final --- ]
Rede Base Utilizada: 192.168.10.0/24
Próximo endereço disponível após alocações: 192.168.10.96
```

💡 Projeto criado por **Maria Fernanda S. :)**
