create database test;

\c test;

create table order_ (
    order_id text not null,
    customer_id text not null,
    primary key (order_id)
);

create table order_item (
    order_id text not null,
    item_id text not null,
    quantity integer not null
);

create table item_stock (
    item_id text not null,
    quantity integer not null,
    primary key (item_id)
);

insert into item_stock (item_id, quantity) values ('item1', 100);