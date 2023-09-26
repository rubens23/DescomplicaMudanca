<h1 align="center">DescomplicaMudança</h1>

<p align="center">
  <a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
  <a href="https://android-arsenal.com/api?level=24"><img src="https://img.shields.io/badge/API-24%2B-brightgreen.svg?style=flat" border="0" alt="API"></a>
  <br>
  <a href="https://wa.me/+5511961422254"><img alt="WhatsApp" src="https://img.shields.io/badge/WhatsApp-25D366?style=for-the-badge&logo=whatsapp&logoColor=white"/></a>
  <a href="https://www.linkedin.com/in/rubens-francisco-125529162/"><img alt="Linkedin" src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white"/></a>
  <a href="mailto:rubens_assis@outlook.com.br"><img alt="Outlook" src="https://img.shields.io/badge/Microsoft_Outlook-0078D4?style=for-the-badge&logo=microsoft-outlook&logoColor=white"/></a>
</p>

<p align="center">  

⭐ Esse é um projeto para demonstrar minha capacidade no desenvolvimento Android nativo com Kotlin. Mais informações técnicas abaixo.

🏠 DescomplicaMudança é o seu guia essencial para uma transição suave e organizada para o novo lar dos seus sonhos. Com recursos que permitem controlar despesas e receitas mensais, definir metas específicas para cada custo da mudança, e oferecer dicas especializadas de planejamento, o aplicativo coloca você no comando total do planejamento da sua mudança. Acompanhe o progresso, veja quanto já economizou e transforme a mudança em uma jornada tranquila e memorável.

</p>

</br>

<p float="left" align="center">

<img alt="screenshot" width="30%" src="app/src/main/appscreenshots/WhatsApp%20Image%202023-09-25%20at%2021.52.55%20(1)_google-pixel4-clearlywhite-portrait.png"/>
<img alt="screenshot" width="30%" src="app/src/main/appscreenshots/WhatsApp%20Image%202023-09-25%20at%2021.52.55%20(2)_google-pixel4-clearlywhite-portrait.png"/>
<img alt="screenshot" width="30%" src="app/src/main/appscreenshots/WhatsApp%20Image%202023-09-25%20at%2021.52.55_google-pixel4-clearlywhite-portrait.png"/>
<img alt="screenshot" width="30%" src="app/src/main/appscreenshots/WhatsApp%20Image%202023-09-25%20at%2021.52.56%20(1)_google-pixel4-clearlywhite-portrait.png"/>
<img alt="screenshot" width="30%" src="app/src/main/appscreenshots/WhatsApp%20Image%202023-09-25%20at%2021.52.56%20(2)_google-pixel4-clearlywhite-portrait.png"/>
<img alt="screenshot" width="30%" src="app/src/main/appscreenshots/WhatsApp%20Image%202023-09-25%20at%2021.52.56%20(3)_google-pixel4-clearlywhite-portrait.png"/>
<img alt="screenshot" width="30%" src="app/src/main/appscreenshots/WhatsApp%20Image%202023-09-25%20at%2021.52.56%20(4)_google-pixel4-clearlywhite-portrait.png"/>
<img alt="screenshot" width="30%" src="app/src/main/appscreenshots/WhatsApp%20Image%202023-09-25%20at%2021.52.56_google-pixel4-clearlywhite-portrait.png"/>
<img alt="screenshot" width="30%" src="app/src/main/appscreenshots/WhatsApp%20Image%202023-09-25%20at%2021.52.57%20(1)_google-pixel4-clearlywhite-portrait.png"/>
<img alt="screenshot" width="30%" src="app/src/main/appscreenshots/WhatsApp%20Image%202023-09-25%20at%2021.52.57%20(2)_google-pixel4-clearlywhite-portrait.png"/>
<img alt="screenshot" width="30%" src="app/src/main/appscreenshots/WhatsApp%20Image%202023-09-25%20at%2021.52.57%20(3)_google-pixel4-clearlywhite-portrait.png"/>
<img alt="screenshot" width="30%" src="app/src/main/appscreenshots/WhatsApp%20Image%202023-09-25%20at%2021.52.57_google-pixel4-clearlywhite-portrait.png"/>




</p>

## Download do App

Faça o download da <a href="apk/app-debug.apk?raw=true">APK diretamente</a>. Você pode ver <a href="https://www.google.com/search?q=como+instalar+um+apk+no+android">aqui</a> como instalar uma APK no seu aparelho android.

## Tecnologias usadas

- Minimum SDK level 26
- [Linguagem Kotlin](https://kotlinlang.org/)

- Componentes da SDK do android que foram utilizados:
  - Navigation: Utilizado para simplificar a navegação das telas no meu app.
  - Room: Utilizado para persistir os dados localmente no device do usuário.
  - Dagger Hilt: Utilizado para facilitar a injeção de dependências nas minhas classes.
  - ViewModel: Utilizado para fornecer um pouco de desacoplamento entre a camada de dados e a view. Também foi utilizada para fazer a comunicação entre activity-fragment ou fragment-fragment.
  - Fragment: Os fragments foram utilizados para fornecer uma organização melhor para as telas do meu app. Além de funcionar muito bem com o NavigationComponent.
  - SharedFlow: utilizado para ficar de olho em metodos assincronos e coletar esses dados para a camada de view.
  - ViewBinding: Fornece uma maneira simples de referenciar os elementos da view nas classes que precisam manipular de alguma forma esses elementos.
 
- Arquitetura 
  - Eu estou usando a arquitetura MVVM para organizar melhor o código, aumentar o desacoplamento e facilitar a escalabilidade da aplicação.
 
## Arquitetura
**DescomplicaMudanca** utiliza a arquitetura [MVVM]
(https://developer.android.com/topic/architecture).
</br></br>
<img width="60%" src="app/src/main/appscreenshots/arquitetura.jpg"/>

<br>

## Principais Funcionalidades

### Controle ganhos e despesas no mês
<img alt="screenshot" width="40%" src="app/src/main/appscreenshots/WhatsApp%20Image%202023-09-25%20at%2021.52.56%20(3)_google-pixel4-clearlywhite-portrait.png"/>

### Estabeleça custos necessários para mudança
<img alt="screenshot" width="40%" src="app/src/main/appscreenshots/WhatsApp%20Image%202023-09-25%20at%2021.52.57%20(1)_google-pixel4-clearlywhite-portrait.png"/>

### acompanhe quanto você já conseguiu economizar
<img alt="screenshot" width="40%" src="app/src/main/appscreenshots/WhatsApp%20Image%202023-09-25%20at%2021.52.57_google-pixel4-clearlywhite-portrait.png"/>

### veja dicas para te ajudar a planejar melhor a mudança
<img alt="screenshot" width="40%" src="app/src/main/appscreenshots/WhatsApp%20Image%202023-09-25%20at%2021.52.55%20(1)_google-pixel4-clearlywhite-portrait.png"/>


# Licença



```xml
    Copyright [2023] [Rubens Francisco de Assis]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

```






