# Clickbus Backend Test

[![CircleCI](https://circleci.com/gh/ypconstante/clickbus-restapi-test.svg?style=svg)](https://circleci.com/gh/ypconstante/clickbus-restapi-test) [![codecov](https://codecov.io/gh/ypconstante/clickbus-restapi-test/branch/master/graph/badge.svg)](https://codecov.io/gh/ypconstante/clickbus-restapi-test)

## Desenvolvimento

Para iniciar a aplicação com um banco em memória, execute o comando:

```sh
./mvnw spring-boot:run -Dspring.profiles.active=development
```


## Produção

Crie um banco MySQL e execute o script de criação de tabelas em [src/main/resources/schema.sql](src/main/resources/schema.sql).

Para iniciar a aplicação execute o comando:

```
./mvnw spring-boot:run -Ddb.url={url jdbc do banco} -Ddb.user={usuario} -Ddb.password={senha}
```


## Swagger

Após iniciar a aplicação, a documentação dos endpoints fica disponível no endereço <http://localhost:8080/swagger-ui.html>

---

## Lets Code, but first...
* Faça um Fork desse projeto (Não clone ele, por favor)
* Ao terminar, abra um PR para que possamos avaliar e nos envie (***backend-devs@clickbus.com***) ;)

## Proposta
Considerando a seguinte estrutura de dados:

![](tables.png)

Criar uma API Rest onde seja possível buscar as seguintes informações:
```json
{
"id": 1,
"name": " Nome do Place",
"slug": "slug-do-place",
"city": {
  "name": "Nome da Cidade",
},
"state": {
  "name": "Nome do Estado"
},
"country": {
  "name": "Nome do Pais",
},
"clientIds": [ "1","2","3" ]
}
```

*OBS: Na tabela de Place, a coluna **slug** deve ter uma constraint **UNIQUE** representada na Entity.*

### Para isso, 
* Crie as Entities que representem a estrutura de dados desenhada acima, com os relacionamentos representados por anotações:
    * Preferencialmente utilize as anotações do ​Lombok​​ dentro das Entities;
    * Utilize um padrão de **LazyLoad** nos relacionamentos entre as Entities.
* Criar um RestController (EndPoint) onde seja possível listar todos os Places existentes
no banco de dados (sempre com os ClientIds relacionados), com a saída de acordo com o JSON demonstrado:
    * Procure seguir o padrão REST de URis.
* Criar um EndPoint onde seja possível buscar apenas um Place através do Id do Place.
* Criar um EndPoint onde seja possível buscar Places pelo slug, lembrando que o slug
deve ser um atributo único dentro da entidade.
* Acrescente o Swagger para documentar e acessar os end points criados.
* Crie 1 teste unitário para cada EndPoint desenvolvido.
* Crie um Pull Request com o seu código no repositório Bitbucket.

*OBS:
Não há restrições para a criação dos end points solicitados, ou seja, fique à vontade
para alterar o pom.xml retirando ou acrescentando qualquer dependência que achar necessária;*

Qualquer dúvida, entre em contato conosco: ***backend-devs@clickbus.com***

Boa Sorte!

