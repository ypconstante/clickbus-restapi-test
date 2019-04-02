# Clickbus Backend Test

## Para começar
* Faça um Fork desse projeto (Não clone ele, por favor)
* Ao terminar, abra um PR para que possamos avaliar ;)

## Proposta
Considerando a seguinte estrutura de dados:

![](/Users/danilopereira/Desktop/Screen Shot 2019-04-02 at 10.46.05.png)

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

*OBS: Na tabela de Place, a coluna ​slug​​ deve ter uma constraint ​UNIQUE​​ representada na Entity.*

### Para isso, 
* Crie as Entities que representem a estrutura de dados desenhada acima, com os relacionamentos representados por anotações:
    * Preferencialmente utilize as anotações do ​Lombok​​ dentro das Entities;
    * Utilize um padrão de ​Lazy​​ ​Load​​ nos relacionamentos entre as Entities.
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

