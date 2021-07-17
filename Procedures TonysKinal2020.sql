delimiter $$
create procedure sp_EliminarEmpresas(in codEmp int)
	begin
        delete from servicios
        where servicios.F_CodigoEmpresa = codEmp;
        delete from presupuesto
        where presupuesto.F_CodigoEmpresa = codEmp;
		delete from empresas
		where empresas.codigoEmpresa = codEmp;
	end$$

delimiter $$
create procedure sp_BuscarEmpresas(in codEmp int)
	begin
		select empresas.CodigoEmpresa,
        empresas.NombreEmpresa,
		empresas.DireccionEmpresa,
        empresas.Telefono 
		from empresas 
            where empresas.codigoEmpresa = codEmp;
	end$$

delimiter $$
create procedure sp_AgregarEmpresas(in codEmp int,in nomEmp varchar(50),in dirEmp varchar(50),in telEmp int(15))
	begin
		insert into empresas(CodigoEmpresa,NombreEmpresa,DireccionEmpresa,Telefono) values(codEmp,
        nomEmp,dirEmp,telEmp);
	end$$

delimiter $$
create procedure sp_ModificarEmpresas(in codEmp int, in nomEmp varchar(50), in dirEmp varchar(50),in telEmp int)
	begin
		update empresas
			set empresas.CodigoEmpresa=codEmp,
            empresas.NombreEmpresa=nomEmp,
            empresas.DireccionEmpresa=dirEmp,
            empresas.Telefono=telEmp
            where empresas.CodigoEmpresa=codEmp;
	end$$

delimiter $$
create procedure sp_ListarEmpresas()
	begin
		select empresas.codigoEmpresa,
        empresas.NombreEmpresa,
        empresas.DireccionEmpresa,
        empresas.Telefono
        from empresas;
	end$$
call sp_AgregarEmpresas(1,"The World","Antigua Guatemala",54203010);
call sp_AgregarEmpresas(2,"Made In Heaven","CC. Portales",41803636);
call sp_AgregarEmpresas(3,"Jump Jack Flash","Mixco",40536);
call sp_AgregarEmpresas(4,"Bohemian Rhapsody","Zona 7",12457812);
call sp_AgregarEmpresas(5,"Citrus","El naranjo",41526341);
################################################################
delimiter $$
create procedure sp_ListarServicios()
	begin
		select servicios.CodigoServicios,
        servicios.FechaServicios,
        servicios.DescripcionServicios,
        servicios.HoraServicios,
        servicios.DireccionServicios,
        servicios.Telefono,
        servicios.F_CodigoEmpresa
        from servicios;
	end$$

delimiter $$
create procedure sp_EliminarServicios(in codSer int)
	begin
		delete from servicios_has_platos 
        where servicios_has_platos.servicios_codigoServicio = codSer;
        delete from servicios_has_empleados
        where servicios_has_empleados.servicios_CodigoServicios = codSer;
		delete from servicios
		where servicios.CodigoServicios = codSer;
		commit;
	end$$

delimiter $$
create procedure sp_ModificarServicios(in codSer int, in fechaSer date, in descSer varchar(50),in horaSer time,
in dirSer varchar(50),in telSer int)
	begin
		update servicios 
			set servicios.CodigoServicios=codSer,
            servicios.FechaServicios=fechaSer,
            servicios.DescripcionServicios=descSer,
            servicios.HoraServicios=horaSer,
            servicios.DireccionServicios=dirSer,
            servicios.Telefono=telSer
            where servicios.CodigoServicios=codSer;
	end$$

delimiter $$
create procedure sp_BuscarServicios(in codSer int)
	begin
		select servicios.CodigoServicios,
        servicios.FechaServicios,
		servicios.DescripcionServicios,
        servicios.HoraServicios,
        servicios.DireccionServicios,
        servicios.Telefono,
        servicios.F_CodigoEmpresa
		from servicios 
            where servicios.CodigoServicios = codSer;
	end$$
    
delimiter $$
create procedure sp_AgregarServicios(in codSer int,in fechaSer date,in desSer varchar(50),in horaSer time,
in dirSer varchar(50),in telSer int, in codEmp int)
	begin
		insert into servicios(CodigoServicios,FechaServicios,DescripcionServicios,HoraServicios,DireccionServicios,
        Telefono,F_CodigoEmpresa) values(codSer,fechaSer,desSer,horaSer,dirSer,telSer,codEmp);
	end$$
call sp_AgregarServicios(1,"2020-1-5","Buffete",15:00:00,"Zona 1",10101010,1);
call sp_AgregarServicios(2,"2020-12-31","Capacitación",12:00:00,"Zona 2",30303030,1);
call sp_AgregarServicios(3,"2020-2-14","Boda",07:00:00,"Zona 3",20202020,1);
call sp_AgregarServicios(4,"2020-9-2","Baby Shower",11:12:37,"Zona 4",50142630,2);
call sp_AgregarServicios(5,"2020-4-19","Carnaval",19:00:00,"Zona 5",1792,2);
call sp_AgregarServicios(6,"2020-12-31","15 años",08:00:00,"Zona 6",60606060,2);
################################################################
delimiter $$
create procedure sp_ListarTipoEmpleado()
	begin
		select tipoempleado.CodigoTipoEmpleado,
        tipoempleado.Descripcion
        from tipoempleado;
	end$$
    
delimiter $$
create procedure sp_EliminarTipoEmpleado(in codTEmpl int)
	begin
		delete from empleados
		where empleados.CodigoEmpleados = codTEmpl;
		delete from tipoempleado
		where tipoempleado.CodigoTipoEmpleado = codTEmpl;
	end$$

delimiter $$
create procedure sp_ModificarTipoEmpleado(in codT int, in descT varchar(50))
	begin
		update tipoempleado 
			set tipoempleado.CodigoTipoEmpleado=codT,
            tipoempleado.Descripcion=descT
            where tipoempleado.CodigoTipoEmpleado=codT;
	end$$

    
delimiter $$
create procedure sp_BuscarTipoEmpleado(in codTEmpl int)
	begin
		select tipoempleado.CodigoTipoEmpleado,
        tipoempleado.Descripcion
        from tipoempleado 
            where tipoempleado.CodigoTipoEmpleado = codTEmpl;
	end$$
    
delimiter $$
create procedure sp_AgregarTipoEmpleado(in codTEmpl int, in descTEmpl varchar(50))
	begin
		insert into tipoempleado(CodigoTipoEmpleado,Descripcion) values(codTEmpl,descTEmpl);
	end$$
call sp_AgregarTipoEmpleado(1,"En entrenamiento");
call sp_AgregarTipoEmpleado(2,"Chef");
call sp_AgregarTipoEmpleado(2,"Jefe de cocina");
################################################################
delimiter $$
create procedure sp_ListarEmpleados()
	begin
		select empleados.CodigoEmpleados,
        empleados.ApellidoEmpleados,
        empleados.NombreEmpleados,
        empleados.DireccionEmpleados,
        empleados.TelefonoEmpleados,
        empleados.F_TipoEmpleados
        from empleados;
	end$$

delimiter $$
create procedure sp_EliminarEmpleados(in codEmpl int)
	begin
		delete from servicios_has_empleados
        where servicios_has_empleados.empleados_CodigoEmpleados = codEmpl;
		delete from empleados
		where empleados.CodigoEmpleados = codEmpl;
	end$$

delimiter $$
create procedure sp_ModificarEmpleados(in codEm int, in apeEm varchar(50),in nomEm varchar(50),in dirEm varchar(50),
in telEm int)
	begin
		update empleados 
			set empleados.CodigoEmpleados=codEm,
            empleados.ApellidoEmpleados=apeEm,
            empleados.NombreEmpleados=nomEm,
            empleados.DireccionEmpleados=dirEm,
            empleados.TelefonoEmpleados=telEm
            where empleados.CodigoEmpleados=codEm;
	end$$


delimiter $$
create procedure sp_BuscarEmpleados(in codEmpl int)
	begin
		select empleados.CodigoEmpleados,
        empleados.ApellidoEmpleados,
		empleados.NombreEmpleados,
        empleados.DireccionEmpleados,
        empleados.TelefonoEmpleados,
        empleados.F_TipoEmpleados
        from empleados 
            where empleados.CodigoEmpleados = codEmpl;
	end$$

delimiter $$
create procedure sp_AgregarEmpleados(in codEmpl int,in apeEmpl varchar(50),in nomEmpl varchar(50),
in dirEmpl varchar(50),in telEmpl int,in FTipoEmpl int)
	begin
		insert into empleados(CodigoEmpleados,ApellidoEmpleados,NombreEmpleados,DireccionEmpleados,TelefonoEmpleados,
        F_TipoEmpleados) values(codEmpl,apeEmpl,nomEmpl,dirEmpl,telEmpl,FTipoEmpl);
	end$$
    
call sp_AgregarEmpleados(1,"Sanchez","Carlos","Canada",15253545,1);
call sp_AgregarEmpleados(2,"Rodas","Juan","México",21304050,2);
call sp_AgregarEmpleados(3,"Lopez","Manuel","Brasil",37908070,1);
call sp_AgregarEmpleados(4,"Aragón","Samuel","Egipto",40253647,2);
call sp_AgregarEmpleados(5,"Jenner","Sarah","U.S.A",59142536,1);
################################################################
delimiter $$
create procedure sp_ListarPresupuesto()
	begin
		select presupuesto.CodigoPresupuesto,
        presupuesto.FechaSolicitud,
        presupuesto.CantidadPresupuesto,
        presupuesto.F_CodigoEmpresa
        from presupuesto;
	end$$

delimiter $$
create procedure sp_ModificarPresupuesto(in codPres int, in fechaPres date, in cantPres decimal(10.0))
	begin
		update presupuesto 
			set presupuesto.CodigoPresupuesto=codPres,
            presupuesto.FechaSolicitud=fechaPres,
            presupuesto.CantidadPresupuesto=cantPres
            where presupuesto.CodigoPresupuesto=codPres;
	end$$
    
delimiter $$
create procedure sp_EliminarPresupuesto(in codPres int)
	begin
		delete from presupuesto
		where presupuesto.CodigoPresupuesto = codPres;
	end$$
    
delimiter $$
create procedure sp_BuscarPresupuesto(in codPres int)
	begin
		select presupuesto.CodigoPresupuesto,
        presupuesto.FechaSolicitud,
		presupuesto.CantidadPresupuesto,
        presupuesto.F_CodigoEmpresa
        from presupuesto 
            where presupuesto.CodigoPresupuesto = codPres;
	end$$    

delimiter $$
create procedure sp_AgregarPresupuesto(in codPres int,in fechaPres date,in cantPres decimal(10.0),in F_CodPres int)
	begin
		insert into presupuesto(CodigoPresupuesto,FechaSolicitud,CantidadPresupuesto,F_CodigoEmpresa) 
        values(codPres,fechaPres,cantPres,F_CodPres);
	end$$

call sp_AgregarPresupuesto(1,"2020-6-6",540200,1);
call sp_AgregarPresupuesto(2,"2020-2-28",25000,2);
call sp_AgregarPresupuesto(3,"2020-7-8",75000,3);
call sp_AgregarPresupuesto(4,"2020-7-14",50000,4);
################################################################
delimiter $$
create procedure sp_ListarProductos()
	begin
		select productos.CodigoProducto,
        productos.NombreProducto,
        productos.Cantidad
        from productos;
	end$$
    
delimiter $$
create procedure sp_ModificarProductos(in codProd int, in nomProd varchar(50), in cantProd int)
	begin
		update productos
			set productos.CodigoProducto=codProd,
            productos.NombreProducto=nomProd,
            productos.Cantidad=cantProd
            where productos.CodigoProducto=codProd;
	end$$
    
delimiter $$
create procedure sp_EliminarProductos(in codProd int)
	begin
		delete from productos_has_platos
        where productos_has_platos.productos_CodigoProducto = codProd;
		delete from productos
		where productos.CodigoProducto = codProd;
	end$$
    
delimiter $$
create procedure sp_BuscarProductos(in codProd int)
	begin
		select productos.CodigoProducto,
        productos.NombreProducto,
		productos.Cantidad
        from productos
            where productos.CodigoProducto= codProd;
	end$$  

delimiter $$
create procedure sp_AgregarProductos(in codProd int,in nomProd varchar(50),in cantProd int)
	begin
		insert into productos(CodigoProducto,NombreProducto,Cantidad) 
        values(codProd,nomProd,cantProd);
	end$$

call sp_AgregarProductos(1,"Ague Gaseosa",250);
call sp_AgregarProductos(2,"Aceite De Oliva",130);
call sp_AgregarProductos(3,"Camarones libra",23);
call sp_AgregarProductos(4,"Agia Mineral",8);
call sp_AgregarProductos(5,"Tomate libra",4);
call sp_AgregarProductos(6,"Lechuga",7);
call sp_AgregarProductos(7,"Sal libra",1);
call sp_AgregarProductos(8,"Pimiento Negra Recien Molida",17);
call sp_AgregarProductos(9,"Salmon",81);
call sp_AgregarProductos(10,"Atún",24);
call sp_AgregarProductos(11,"Alitas de pollo",34);
call sp_AgregarProductos(12,"Salsa marinara",32);
call sp_AgregarProductos(13,"Naranaja y citricos",12);
call sp_AgregarProductos(14,"Brisket",57);
call sp_AgregarProductos(15,"Cilantro",1);
call sp_AgregarProductos(16,"Apazote",7);
call sp_AgregarProductos(17,"Arroz blanco",10);
call sp_AgregarProductos(18,"Especies",5);
call sp_AgregarProductos(19,"Pan parmesano",15);
call sp_AgregarProductos(20,"Carne para hamburguesas",45);
call sp_AgregarProductos(21,"Salsa de tomate",18);
################################################################
delimiter $$
create procedure sp_ListarTipoPlato()
	begin
		select tipoplato.CodigoTipoPlato,
        tipoplato.Descripcion
        from tipoplato;
	end$$

delimiter $$
create procedure sp_EliminarTipoPLato(in codTPlato int)
	begin
		delete from platos
		where platos.F_TipoPlato = codTPlato;
        delete from tipoplato
        where tipoplato.CodigoTipoPlato = codTPlato;
	end$$

delimiter $$
create procedure sp_ModificarTipoPlato(in codTP int, in DescTP varchar(50))
	begin
		update tipoplato
			set tipoplato.CodigoTipoPlato=codTP,
            tipoplato.Descripcion=descTP
            where tipoplato.CodigoTipoPlato=codTP;
	end$$

delimiter $$
create procedure sp_BuscarTipoPLato(in codTPlato int)
	begin
		select tipoplato.CodigoTipoPlato,
        tipoplato.Descripcion
        from tipoplato
            where tipoplato.CodigoTipoPlato= codTPlato;
	end$$  

delimiter $$
create procedure sp_AgregarTipoPlato(in codTPlato int,in descTPlato varchar(50))
	begin
		insert into tipoplato(CodigoTipoPlato,Descripcion) 
        values(codTPlato,descTPlato);
	end$$
    
call sp_AgregarTipoPlato(1,"Plato fuerte");
call sp_AgregarTipoPlato(2,"Entrada");
call sp_AgregarTipoPlato(3,"Postre");
call sp_AgregarTipoPlato(4,"Ensalada");
################################################################
delimiter $$
create procedure sp_ListarPlatos()
	begin
		select platos.CodigoPlato,
        platos.Cantidad,
        platos.NombrePlato,
        platos.PrecioPlato,
        platos.F_TipoPlato
        from platos;
	end$$

delimiter $$
create procedure sp_AgregarPlatos(in codPlat int,in cantPlat int(15), in nomPlat varchar(50),in presPlat decimal(10.0),
in FCodEmpPlat int)
	begin
		insert into platos(CodigoPlato,Cantidad,NombrePlato,PrecioPlato,F_TipoPlato) 
        values(codPlat,cantPlat,nomPlat,presPlat,FCodEmpPlat);
	end$$

delimiter $$
create procedure sp_ModificarPlatos(in codP int, in cP int(15),in nP varchar(50),in pP decimal(10.0))
	begin
		update platos
			set platos.CodigoPlato=codP,
            platos.Cantidad=cP,
            platos.NombrePlato=nP,
            platos.PrecioPlato=pP
            where platos.CodigoPlato=codP;
	end$$

delimiter $$
create procedure sp_BuscarPLatos(in codPlato int)
	begin
		select platos.CodigoPlato,
        platos.Cantidad,
        platos.NombrePlato,
        platos.PrecioPlato,
        platos.F_TipoPlato
        from platos
            where platos.CodigoPlato= codPlato;
	end$$  

delimiter $$
create procedure sp_EliminarPlatos(in codPlato int)
	begin
		delete from productos_has_platos
        where productos_has_platos.platos_CodigoPlato = codPlato;
        delete from servicios_has_platos
        where servicios_has_platos.plato_codigoPlato = codPLato;
		delete from platos
		where platos.CodigoPlato = codPlato;
	end$$
    
call sp_AgregarPlatos(1,220,"Ceviche",35.20,1);
call sp_AgregarPlatos(2,15,"Sopa de pollo",27.90,2);
call sp_AgregarPlatos(3,50,"Spaghetti",46.30,1);
call sp_AgregarPlatos(4,30,"Pizza",78,2);
call sp_AgregarPlatos(5,42,"Papas fritas",18,1);
call sp_AgregarPlatos(6,14,"Helado",28,2);
################################################################
delimiter $$
create procedure sp_ListarReportePresupuesto(in conEm int)
	begin
		select * from empresas e inner join servicios s on 
        e.CodigoEmpresa = s.F_CodigoEmpresa  where e.CodigoEmpresa = conEm
        group by s.DescripcionServicios; 
	end$$

delimiter $$
create procedure sp_ListarSubReportePresupuesto(in codEm int)
	begin
		select * from empresas e inner join presupuesto p on e.CodigoEmpresa = p.F_CodigoEmpresa
        where e.CodigoEmpresa = codEm group by p.CantidadPresupuesto;
	end$$
################################################################
delimiter $$
create procedure sp_ListarReportePlatos(in codServicio int)
	begin
		select * from tipoplato tp inner join platos p on tp.CodigoTipoPlato = p.F_TipoPlato
		inner join servicios_has_platos shp on 
        p.CodigoPlato = shp.plato_codigoPlato
        where shp.servicios_codigoServicio = codServicio group by p.NombrePlato;
	end$$
        
delimiter $$
create procedure sp_ListarSubReportePlatos(in codServicio int)
	begin
		select * from servicios s inner join servicios_has_platos shp on s.CodigoServicios = shp.servicios_codigoServicio
        where s.CodigoServicios = codServicio group by s.DescripcionServicios;
	end$$
################################################################
delimiter $$
create procedure sp_BuscarServicios_has_platos(in sCod int,in pCod int)
	begin
		select servicios_has_platos.servicios_codigoServicio,
        servicios_has_platos.plato_codigoPlato
        from servicios_has_platos
            where servicios_has_platos.servicios_codigoServicio= sCod and
            servicios_has_platos.plato_codigoPlato= pCod;
	end$$

delimiter $$
create procedure sp_ListarServicios_has_platos()
	begin
		select servicios_has_platos.servicios_codigoServicio,
        servicios_has_platos.plato_codigoPlato
        from servicios_has_platos;
	end$$        

delimiter $$
create procedure sp_ListarServicios_has_empleados()
	begin
		select servicios_has_empleados.codigoSHE,
        servicios_has_empleados.servicios_CodigoServicios,
        servicios_has_empleados.empleados_CodigoEmpleados,
        servicios_has_empleados.fechaEvento,
        servicios_has_empleados.horaEvento,
        servicios_has_empleados.lugarEvento
        from servicios_has_empleados;
	end$$    

delimiter $$
create procedure sp_ListarProductos_has_platos()
	begin
		select productos_has_platos.productos_CodigoProducto,
        productos_has_platos.platos_CodigoPlato
        from productos_has_platos;
	end$$   
################################################################
delimiter $$
create procedure sp_AgregarServicios_has_empledos(in codSHE int,in codServ int,in codEmpl int, in fecha date,in hora time,
in lugar varchar(50))
	begin
		insert into servicios_has_empleados(codigoSHE,servicios_CodigoServicios,empleados_CodigoEmpleados,fechaEvento,horaEvento,lugarEvento) 
        values(codSHE,codServ,codEmpl,fecha,hora,lugar);
	end$$

delimiter $$
create procedure sp_EliminarServicios_has_empledos(in codEmp int)
	begin
		delete from servicios_has_empleados
		where servicios_has_empleados.codigoSHE = codEmp;
	end$$
    
delimiter $$
create procedure sp_ModificarServicios_has_empledos(in codSHE int,in fEvento date, in hEvento time,
in lEvento varchar(50))
	begin
		update servicios_has_empleados
			set servicios_has_empleados.codigoSHE=codSHE,
            servicios_has_empleados.fechaEvento=fEvento,
            servicios_has_empleados.horaEvento=hEvento,
            servicios_has_empleados.lugarEvento=lEvento
            where servicios_has_empleados.codigoSHE=codSHE;
	end$$
################################################################    
delimiter $$
create procedure ReportePlatos(in codPlato int)
	begin
		select * from productos_has_platos php inner join productos p on php.productos_CodigoProducto = p.CodigoProducto
		inner join tipoplato tp on 
        tp.CodigoTipoPlato = php.platos_CodigoPlato
        where php.platos_CodigoPlato = codPlato group by p.NombreProducto;
	end$$

delimiter $$
create procedure sp_SubReportePlatos(in codPlato int)
	begin
		select * from platos p inner join productos_has_platos php on p.CodigoPlato = php.platos_CodigoPlato
        where p.CodigoPlato = codPlato group by p.NombrePlato;
	end$$
    