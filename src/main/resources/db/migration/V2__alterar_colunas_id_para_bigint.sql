-- Alterar tipos de colunas ID nas tabelas principais
ALTER TABLE categorias ALTER COLUMN id TYPE BIGINT;
ALTER TABLE livros ALTER COLUMN id TYPE BIGINT;
ALTER TABLE usuarios ALTER COLUMN id TYPE BIGINT;
ALTER TABLE emprestimos ALTER COLUMN id TYPE BIGINT;
ALTER TABLE funcionarios ALTER COLUMN id TYPE BIGINT;

-- Alterar tipos de colunas de chave estrangeira
ALTER TABLE livros ALTER COLUMN categoria_id TYPE BIGINT;
ALTER TABLE emprestimos ALTER COLUMN usuario_id TYPE BIGINT;
ALTER TABLE emprestimos ALTER COLUMN livro_id TYPE BIGINT;