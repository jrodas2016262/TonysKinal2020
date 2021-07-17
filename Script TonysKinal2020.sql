#Drop database if exists  TonysKinal2020;
create database TonysKinal2020;
use  TonysKinal2020;

create table TipoEmpleado(CodigoTipoEmpleado int not null,
Descripcion varchar(50) not null,
Primary Key(CodigoTipoEmpleado));

create table TipoPlato(CodigoTipoPlato int not null,
Descripcion varchar(50) not null,
Primary Key(CodigoTipoPlato));

create table Empleados(CodigoEmpleados int not null,
ApellidoEmpleados varchar(50) not null,
NombreEmpleados varchar(50) not null,
DireccionEmpleados varchar(50) not null,
TelefonoEmpleados int not null,
F_TipoEmpleados int not null,
Foreign Key(F_TipoEmpleados) references tipoempleado(CodigoTipoEmpleado),
Primary Key(CodigoEmpleados));

create table Platos(CodigoPlato int not null,
Cantidad int(15) not null,
NombrePlato varchar(50) not null,
PrecioPlato decimal(10,0) not null,
F_TipoPlato int not null,
Foreign Key(F_TipoPlato) references TipoPlato(CodigoTipoPlato),
Primary Key(CodigoPlato));

create table Empresas(CodigoEmpresa int not null,
NombreEmpresa varchar(50) not null,
DireccionEmpresa varchar(50) not null,
Telefono int not null,
Primary key(CodigoEmpresa));

create table Presupuesto(CodigoPresupuesto int not null,
FechaSolicitud date not null,
CantidadPresupuesto decimal(10,0) not null,
F_CodigoEmpresa int not null,
Foreign Key(F_CodigoEmpresa) references Empresas(CodigoEmpresa),
Primary Key(CodigoPresupuesto));

create table Productos(CodigoProducto int not null,
NombreProducto varchar(50) not null,
Cantidad int (15) not null,
Primary Key(CodigoProducto));

create table Servicios(CodigoServicios int not null,
FechaServicios date not null,
DescripcionServicios varchar(50) not null,
HoraServicios time not null,
DireccionServicios varchar(50) not null,
Telefono int not null,
F_CodigoEmpresa int not null,
Foreign key(F_CodigoEmpresa) references Empresas(CodigoEmpresa),
Primary key(CodigoServicios));

create table Servicios_has_platos(servicios_codigoServicio int not null,
plato_codigoPlato int not null,
Foreign key(servicios_codigoServicio) references servicios (CodigoServicios),
Foreign key(plato_codigoPlato) references platos (CodigoPlato));

create table Productos_has_platos(productos_CodigoProducto int not null,
platos_CodigoPlato int not null,
foreign key(productos_CodigoProducto) references productos(CodigoProducto),
foreign key(platos_CodigoPlato) references platos(CodigoPlato));

create table Servicios_has_empleados(codigoSHE int not null,
servicios_CodigoServicios int not null,
empleados_CodigoEmpleados int not null,
fechaEvento date not null,
horaEvento time not null,
lugarEvento varchar(50) not null,
foreign key(servicios_CodigoServicios) references servicios(CodigoServicios),
foreign key(empleados_CodigoEmpleados) references empleados(CodigoEmpleados),
primary key(codigoSHE));

insert into servicios_has_platos(servicios_codigoServicio,plato_codigoPlato) values(1,2);
insert into servicios_has_platos(servicios_codigoServicio,plato_codigoPlato) values(1,1);
insert into servicios_has_platos(servicios_codigoServicio,plato_codigoPlato) values(1,3);
insert into servicios_has_platos(servicios_codigoServicio,plato_codigoPlato) values(1,4);
insert into servicios_has_platos(servicios_codigoServicio,plato_codigoPlato) values(2,1);
insert into servicios_has_platos(servicios_codigoServicio,plato_codigoPlato) values(2,3);
insert into servicios_has_platos(servicios_codigoServicio,plato_codigoPlato) values(2,5);
insert into servicios_has_platos(servicios_codigoServicio,plato_codigoPlato) values(3,2);
insert into servicios_has_platos(servicios_codigoServicio,plato_codigoPlato) values(3,4);
insert into servicios_has_platos(servicios_codigoServicio,plato_codigoPlato) values(3,6);
insert into servicios_has_platos(servicios_codigoServicio,plato_codigoPlato) values(4,2);
insert into servicios_has_platos(servicios_codigoServicio,plato_codigoPlato) values(4,3);
insert into servicios_has_platos(servicios_codigoServicio,plato_codigoPlato) values(4,1);
insert into servicios_has_platos(servicios_codigoServicio,plato_codigoPlato) values(4,6);
insert into servicios_has_platos(servicios_codigoServicio,plato_codigoPlato) values(5,6);
insert into servicios_has_platos(servicios_codigoServicio,plato_codigoPlato) values(5,5);
insert into servicios_has_platos(servicios_codigoServicio,plato_codigoPlato) values(5,1);
insert into servicios_has_platos(servicios_codigoServicio,plato_codigoPlato) values(6,1);
insert into servicios_has_platos(servicios_codigoServicio,plato_codigoPlato) values(6,2);
insert into servicios_has_platos(servicios_codigoServicio,plato_codigoPlato) values(6,5);

insert into servicios_has_empleados(codigoSHE,servicios_CodigoServicios,empleados_CodigoEmpleados,
fechaEvento,horaEvento,lugarEvento) values(1,1,1,"2020-02-15","12:00:00","18 av. 14-42");
insert into servicios_has_empleados(codigoSHE,servicios_CodigoServicios,empleados_CodigoEmpleados,
fechaEvento,horaEvento,lugarEvento) values(2,1,2,"2020-02-15","07:00:00","18 av. 14-42");
insert into servicios_has_empleados(codigoSHE,servicios_CodigoServicios,empleados_CodigoEmpleados,
fechaEvento,horaEvento,lugarEvento) values(3,1,3,"2020-02-15","09:00:00","18 av. 14-42");
insert into servicios_has_empleados(codigoSHE,servicios_CodigoServicios,empleados_CodigoEmpleados,
fechaEvento,horaEvento,lugarEvento) values(4,1,4,"2020-02-15","09:15:00","18 av. 14-42");
insert into servicios_has_empleados(codigoSHE,servicios_CodigoServicios,empleados_CodigoEmpleados,
fechaEvento,horaEvento,lugarEvento) values(5,1,5,"2020-02-15","12:00:00","18 av. 14-42");
insert into servicios_has_empleados(codigoSHE,servicios_CodigoServicios,empleados_CodigoEmpleados,
fechaEvento,horaEvento,lugarEvento) values(6,2,1,"2020-01-01","03:00:00","17av. 7-42 calle b");
select * from platos;

insert into productos_has_platos(productos_CodigoProducto,platos_CodigoPlato) values(1,1);
insert into productos_has_platos(productos_CodigoProducto,platos_CodigoPlato) values(2,1);
insert into productos_has_platos(productos_CodigoProducto,platos_CodigoPlato) values(5,1);
insert into productos_has_platos(productos_CodigoProducto,platos_CodigoPlato) values(8,1);
insert into productos_has_platos(productos_CodigoProducto,platos_CodigoPlato) values(16,1);
insert into productos_has_platos(productos_CodigoProducto,platos_CodigoPlato) values(2,2);
insert into productos_has_platos(productos_CodigoProducto,platos_CodigoPlato) values(8,2);
insert into productos_has_platos(productos_CodigoProducto,platos_CodigoPlato) values(19,2);
insert into productos_has_platos(productos_CodigoProducto,platos_CodigoPlato) values(20,2);
insert into productos_has_platos(productos_CodigoProducto,platos_CodigoPlato) values(21,2);

select * from productos;
select * from platos;
select * from tipoplato;
select * from productos_has_platos