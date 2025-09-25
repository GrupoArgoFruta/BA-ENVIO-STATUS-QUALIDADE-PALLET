# Ação de Atualização de Status e Envio de E-mail (Qualidade Pallets)


![Logo da ArgoFruta](https://argofruta.com/wp-content/uploads/2021/05/Logo-text-white-1.png)

Este código implementa a lógica de ação personalizada no Sankhya para atualizar o status de pallets, registrar históricos e enviar e-mails com informações formatadas em HTML.

## ⚙️ Funcionalidades Principais

- Captura de Parâmetros da Tela

- Datas de entrada e saída (DTENTRADA, DTSAIDA)

- Quantidade de pastilhas, ativadores e pallets

- Observações (OBS)

- Código do status (CODSTATUS)

- Local de tratamento (LOCATRATAMENTO)

- Tradução do Local de Tratamento

- Mapeia códigos como A, C, CM, CA, CF para descrições amigáveis (ex: ANTECÂMARA, CONTEINER FIXO, etc.).

- Caso não seja reconhecido, usa DESCONHECIDO.

- Consulta ao Banco de Dados

- Busca o status de qualidade em AD_STATUSPLT.

- Busca o local do pallet em LocalFinanceiro.

- Construção de Relatório HTML

- Gera uma tabela contendo:

- Nro. Único Pallet

- Nro. Pallet

- Local

- Status Qualidade

- Status Geral (Aberto, Finalizado, Desmontado, Reprocessado, Desconhecido)

- Observações

- Calibre

- Qtd. Caixas Pallet

- P.A. Gerados

- Persistência de Histórico

- Registra alterações na tabela AD_HISTSTATUSPALLET via ServiceHistoricoEnvio.

- Atualiza status e observação na tabela AD_MONTPALLET.

- Grava o status anterior em histórico para auditoria.

- Envio de E-mail

- Dispara e-mail com corpo HTML (ServiceCorpoEmailQualidade) contendo os dados do pallet.

- Inclui informações do período (entrada/saída), quantidades e observações.

- Controle de Erros

- Em caso de falha, exibe mensagem no contexto da ação (ctx.mostraErro).

- email padrão : expedicao.qualidade@argofruta.com

## 📊 Fluxo Resumido

- Usuário executa a ação no Sankhya.

- Sistema lê parâmetros de entrada e registros selecionados.

- Busca descrições complementares (status e local).

- Monta relatório HTML com os pallets.

- Atualiza status na base (AD_MONTPALLET).

- Registra histórico detalhado em AD_HISTSTATUSPALLET.

- Envia e-mail automático com informações do processo.

## ✅ Benefícios do Código

- Automatização do processo de atualização de status de pallets.

- Rastreabilidade via gravação de histórico anterior.

- Comunicação eficiente por meio de envio de e-mail em formato tabular.

- Padronização dos locais e status exibidos.

- Auditoria garantida: nada é sobrescrito sem histórico.

## 🔒 Regras de Negócio

- Cada execução atualiza o status dos pallets selecionados.

- Sempre grava o status anterior antes de sobrescrever.

- O local do tratamento é traduzido para descrição legível.

- Envio de e-mail é obrigatório, contendo os dados processados.

### USUARIOS 
- USUARIO RESPONSAVEL: Joamerson
- EMAIL : expedicao@argofruta.com


