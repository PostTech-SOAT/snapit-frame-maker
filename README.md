# snapit-frame-maker
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=PostTech-SOAT_snapit-frame-maker&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=PostTech-SOAT_snapit-frame-maker)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=PostTech-SOAT_snapit-frame-maker&metric=coverage)](https://sonarcloud.io/summary/new_code?id=PostTech-SOAT_snapit-frame-maker)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=PostTech-SOAT_snapit-frame-maker&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=PostTech-SOAT_snapit-frame-maker)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=PostTech-SOAT_snapit-frame-maker&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=PostTech-SOAT_snapit-frame-maker)

# Projeto Snapit – Grupo 46 – 7 SOAT
-	RM 354702 – Gabriel Rabello de Menezes
-	RM 356178 – Giovanna Caroline Adorno
-	RM 356181 – Guilherme Rodriguero de Souza
-	RM 355426 – Leandro Airton Bezerra
-	RM 354475 – Willian Novais Planciunas

# Links para acessar os códigos
- Snapit Media Manager: https://github.com/PostTech-SOAT/snapit-media-manager
- Snapit Frame Maker: https://github.com/PostTech-SOAT/snapit-frame-maker
- Snapit Notification: https://github.com/PostTech-SOAT/snapit-notification  
Em cada microsserviço tem o código do terraform que cria o helm deploy, configmap e secrets.
- Snapit Authentication (lambda para criar e logar usuário e o Cognito): https://github.com/PostTech-SOAT/snapit-authentication
- Snapit Authorization (lambda para a autorização de APIs): https://github.com/PostTech-SOAT/snapit-authorization
- Snapit Actions (repositório de template de actions): https://github.com/PostTech-SOAT/snapit-actions
- Snapit Database (para subir o RDS): https://github.com/PostTech-SOAT/snapit-database
- Snapit Infraestructure (cria as redes, o EKS, o Nginx, o RabbitMq e o API Gateway): https://github.com/PostTech-SOAT/snapit-infraestructure

## Tecnologias escolhidas
Nosso projeto foi desenvolvido em Java em conjunto com o framework SpingBoot. Como arquitetura, adotamos o conceito de microsserviços com Clean Architecture, para separar as responsabilidades da nossa aplicação, o que ajuda na testabilidade e independência das tecnologias escolhidas na implementação. Além de favorecer a escalabilidade e evolução do software. Nossa solução conta com cobertura de 100% de testes unitários, testes de integração e BDD.

### Banco de Dados
Decidimos utilizar a ferramenta PostgreSQL como nosso banco de dados, devido principalmente a sua fácil escalabilidade e sólida garantia de integridade de dados e suporte a transações ACID, bom para aplicações que precisam garantir a consistência dos dados.

### Testes
- Testes unitários: Contamos com mais de 95% de cobertura em todos os nossos projetos. Eles garantem que cada parte do código funcione corretamente de forma isolada, facilitando a identificação de erros precocemente. Além disso, ajudam a manter a qualidade do código à medida que o projeto cresce, promovendo maior confiança nas mudanças e facilitando a manutenção do sistema.
- Testes de integração: Todas as nossas APIs possuem testes de integração, que verificam a interação entre diferentes componentes do sistema, garantindo que funcionem corretamente em conjunto. Eles ajudam a identificar problemas de comunicação ou falhas em fluxos de dados entre módulos. Ao usar testes de integração, é possível detectar erros que não seriam encontrados em testes unitários, garantindo a integridade do sistema como um todo.
- BDD: O projeto foi construído usando essa metodologia para alinhar expectativas, melhorar a documentação e garantir que as funcionalidades atendam às necessidades reais do usuário.

### Comunicação entre microsserviços
- RabbitMq: Utilizamos como comunicação entre os microsserviços para garantir a melhor escalabilidade, desacoplamento e resiliência do sistema. Fazendo a comunicação de forma assíncrona podemos assegurar que mesmo que um dos projetos esteja indisponível, nenhuma requisição será perdida e quando eles estiverem reestabelecidos poderão ser processados corretamente.
- Apache Camel: Também usamos como comunicação o Apache Camel com o S3 para ouvir o bucket responsável por armazenar os arquivos que estão aguardando o processamento. Essa escolha foi feita para garantir a disponibilidade da aplicação, já que se houver algum problema inesperado no microsserviço de processamento de videos em frames, isso não afetará que o usuário continue consultando as informações de videos já processados anteriormente e nem que o usuário consiga inserir novos arquivos para processamento até que o microsserviço snapit-frame-maker esteja operante novamente, desacoplando essas funcionalidades. Além disso, o Camel é uma excelente ferramenta que propões fácil escalabilidade e resiliência, já que o Camel pode ser facilmente configurado para trabalhar com alta disponibilidade e de forma assíncrona, minimizando a latência.

### Separação dos microsserviços
- snapit-media-manager (Controle de Requisições): É o microsserviço responsável por expor endpoints que são responsáveis por fornecer, ao usuário, acesso às funcionalidades do sistema, como por exemplo: listar o status dos processamentos dos vídeos, fazer upload de um vídeo para obter os frames do mesmo, download de vídeo que o usuário já fez upload anteriormente e, por fim, download dos frames já processados de um vídeo.
- snapit-frame-maker (Geração dos Frames a partir de um vídeo): Responsável por efetivamente obter um vídeo e transformá-lo em frames.
- snapit-notification (Envio de notificações para o usuário): Em caso de falha no processamento, este serviço recebe um evento e, como consequência, envia um email para o usuário informando que a solicitação de processamento do mesmo, resultou em falha.

Ao fazermos o upload de um vídeo utilizando o endpoint do snapit-media-manager, o vídeo vai para os buckets snapit-history (usado para que se o usuário deseje, possa baixas os vídeos que já subiu na plataforma em sua forma original) e snapit-uploads.  
No frame-maker escutamos via Camel o bucket snapit-uploads, logo, quando é inserido um arquivo nesse bucket pelo snapit-media-manager, ele é consumido e processado em um conjunto de frames.  
Em caso de sucesso, enviamos o zip com os frames para o bucket snapit-frames, que será onde o endpoint de download buscará o arquivo a ser baixado, e enviamos uma mensagem para a fila de processamento de sucesso que será consumida no snapit-media-manager para disponibilizar para o cliente que o arquivo de frames está pronto para ser baixado.  
Em caso de erro, enviamos uma mensagem para a fila de processados com erro, essa mensagem será consumida em dois lugares, no snapit-media-manager para disponibilizar nas APIs que houve um erro no processamento das mensagens e no snapit-notification para enviar um e-mail para o usuário informando do erro.  

### Diagrama da nossa solução
![Snapit Diagrama](https://github.com/user-attachments/assets/766f9042-8ea5-4e1e-87aa-57257782c7e5)


## Tecnologias da Infraestrutura Escolhidas
Utilizamos na maioria das tecnologias os serviços em nuvem da AWS, pois oferece escalabilidade, flexibilidade. Além disso, garante alta disponibilidade, segurança robusta e acesso global, facilitando a inovação e a agilidade nos negócios. Usamos os produtos AWS Lambda, AWS Cognito, API Gateway, Classic Load Balancer, EKS, AWS S3, RDS Postgres.  
Além desses serviços da AWS, também utilizamos o SonarQube para garantir a qualidade do código, o Terraform como ferramenta de infra e o Github Actions para o CI/CD.  

### AWS Lambda
Foram criados 3 lambdas para serem responsáveis pelo gerenciamento de usuários, que se conectam ao Cognito para criar usuários na plataforma, fazer login e será responsável por validar o token do usuário no Cognito. Foi utilizado essa tecnologia para isso porque ela permite a execução de código sem gerenciamento de servidores, possui fácil integração, baixa latência e alta disponibilidade, garantindo escalabilidade automática.

### AWS Cognito
O Cognito facilita a implementação de autenticação de usuários e pode lidar com milhões de usuários sem que seja necessário se preocupar com a escalabilidade da infraestrutura. Além disso ele oferece recursos avançados de segurança.

### API Gateway
Utilizamos o API Gateway para facilitar a criação, monitoramento e segurança de APIs, com escalabilidade automática e suporte a autenticação integrada.

### CLB
Para distribuir o tráfego de rede de forma eficiente entre as instâncias, garantindo alta disponibilidade e confiabilidade.

### EKS
Foi escolhido o EKS para fazer a orquestração de contêineres Kubernetes gerenciada, permitindo escalabilidade e alta disponibilidade para aplicações baseadas em contêineres.

### AWS S3
Armazenamento de objetos altamente escalável, seguro e econômico, ideal para backup e recuperação de dados.

### SonarQube
A códigos sem cobertura, problemas de segurança ou issues de código podem acarretar grandes problemas futuros para o projeto, por isso escolhemos o Sonar para analisar a estática que ajuda a manter a qualidade do código, detectando vulnerabilidades, bugs e duplicações.

### Terraform
Automatiza a criação e gestão de infraestrutura como código, garantindo consistência e escalabilidade na infraestrutura.

### Github Actions
Permite a automação do fluxo de trabalho de CI/CD, facilitando a integração contínua e a entrega contínua com flexibilidade e integração com o GitHub.

### Diagrama da Infraestrutura da nossa solução
![snapit-infra-architecture (1)](https://github.com/user-attachments/assets/ac54b446-b94a-4309-a41c-2973078dd7f2)
