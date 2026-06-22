# App de Clima ⛅

Um aplicativo desktop desenvolvido em Java para consulta de previsão do tempo. O sistema integra dados climáticos oficiais do CPTEC/INPE com a base de municípios do IBGE, oferecendo uma interface gráfica moderna com suporte a *Dark Mode*.



## 🚀 Funcionalidades

* **Integração com IBGE:** Listagem dinâmica e filtro inteligente de cidades brasileiras divididas por Estado.
* **Previsão de 7 Dias:** Consome a API XML do INPE para exibir a previsão climática de hoje e dos próximos 6 dias.
* **Temperatura Atual (Estimativa):** Cálculo ilustrativo da temperatura no momento da consulta com base na máxima e mínima do dia.
* **Ícones Dinâmicos:** Carregamento assíncrono de imagens meteorológicas diretamente do CDN do OpenWeatherMap.
* **Interface Moderna:** UI construída em Java Swing com paleta de cores focada no *Dark Mode* e tipografia de alto contraste.

## 🛠️ Tecnologias Utilizadas

* **Linguagem:** Java
* **Interface Gráfica:** Java Swing
* **Manipulação de XML:** XStream (e suas dependências como KXML2, XMLPull)
* **APIs Consumidas:**
  * [CPTEC/INPE](http://servicos.cptec.inpe.br/XML/) - Dados meteorológicos em XML.
  * [IBGE Localidades](https://servicodados.ibge.gov.br/api/docs/localidades) - Listagem de UFs e Municípios em JSON.

## ⚙️ Como Executar

1. Clone o repositório ou baixe os arquivos fonte.
2. Certifique-se de que a biblioteca **XStream** (e os arquivos `.jar` associados na pasta `/lib`) estejam adicionados ao *Build Path* do seu projeto.
3. Execute a classe principal do pacote de visualização:
   ```bash
   java view.AppClimaGUI
