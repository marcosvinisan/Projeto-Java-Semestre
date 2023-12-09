create database ProjetoJava;
use ProjetoJava;

create table usuario (
nome varchar (100) not null,
email varchar (100) not null,
username varchar (20) unique not null,
senha varchar (20) not null,
primary key (username)
);

create table restaurante (
id_restaurante int primary key,
nome varchar (100) not null,
telefone varchar (15) not null,
dias_alimento varchar (15) not null
);

create table endereco (
rua varchar (100) not null,
numero int not null,
complemento varchar (50),
bairro varchar (50) not null,
cidade varchar (50) not null,
uf varchar (2),
pais varchar (100),
id_restaurante int,
foreign key (id_restaurante) references restaurante (id_restaurante)
);

select * from usuario;
select * from restaurante;
select * from endereco;