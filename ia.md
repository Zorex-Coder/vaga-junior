# Instruções para a IA
## Linguagem a ser utilizada
- Backend Java.
- Frontend Html, Css, Js.
- Banco de dados Postgres rodando em Docker.
- Comunicação via api http: `GET`, `POST`, `PUT`, `DELETE`.

## Estilo de código
- Funções: 4 a 20 linhas. Divida se forem mais longas.
- Arquivos: menos de 500 linhas. Divida por responsabilidade.
- Uma coisa por função, uma responsabilidade por módulo (Princípio da Responsabilidade Única).
- Nomes: específicos e únicos. Evite `data`, `handler`, `Manager`.
Prefira nomes que retornem menos de 5 resultados de busca no código-fonte.
- Tipos: explícitos. Sem `any`, sem `Dict`, sem funções sem tipo definido.
- Sem duplicação de código. Extraia a lógica compartilhada para uma função/módulo.
- Retornos antecipados em vez de ifs aninhados. Máximo de 2 níveis de indentação.
- Mensagens de exceção devem incluir o valor que causou o erro e o formato esperado.

## Comentários
- Mantenha seus próprios comentários. Não os remova na refatoração — eles carregam
intenção e procedência.
- Escreva PORQUÊ, não O QUÊ. Ignore `// incrementar contador` acima de `i++`.
- Docstrings em funções públicas: intenção + um exemplo de uso. - Referencie os números de issue/SHAs de commit quando uma linha existir devido a
um bug específico ou restrição upstream.

## Testes
- Os testes são executados com um único comando: `<específico do projeto>`.
- Cada nova função recebe um teste. Correções de bugs recebem um teste de regressão.
- Simule E/S externa (API, banco de dados, sistema de arquivos) com classes falsas nomeadas,
não stubs inline.
- Os testes devem ser rápidos, independentes, repetíveis,
autovalidáveis ​​e oportunos.

## Dependências
- Injete dependências por meio de construtor/parâmetro, não globalmente/importadas.
- Encapsule bibliotecas de terceiros por trás de uma interface simples pertencente a este projeto.

## Estrutura
- Siga a convenção do framework (Rails, Django, Next.js, etc.).
- Prefira módulos pequenos e focados a arquivos genéricos.
- Caminhos previsíveis: controller/model/view, src/lib/test, etc.

## Formatação
- Use o formatador padrão da linguagem (`cargo fmt`, `gofmt`, `prettier`,
'black`, `rubocop -A`). Não discuta estilos além disso.

## Registro de logs
- JSON estruturado para registro de logs para depuração/observabilidade.
- Somente texto simples para a saída da linha de comando voltada para o usuário.