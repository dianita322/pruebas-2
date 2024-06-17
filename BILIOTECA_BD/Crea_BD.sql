-- =============================================
-- Creación de la Base de Datos
-- =============================================

USE master;
GO

IF EXISTS (SELECT name FROM master.sys.databases WHERE name = 'BIBLIOTECA')
BEGIN
    DROP DATABASE BIBLIOTECA;
END;
GO

CREATE DATABASE BIBLIOTECA;
GO

-- =============================================
-- Seleccionar la Base de Datos
-- =============================================

USE BIBLIOTECA;
GO

-- =============================================
-- Creación de los Objetos de la Base de Datos
-- =============================================

CREATE TABLE Libros (
    LibroID varchar(9),
    Título varchar(100) NOT NULL,
    Autor varchar(100) NOT NULL,
    Categoria varchar(20) NOT NULL,
    CDU int NOT NULL,
    AñoPublicacion int,
    Editorial varchar(50),
    ISBN varchar(30),
    Descripcion varchar(100),
    Cantidad int,
    CONSTRAINT PK_Libros PRIMARY KEY (LibroID)
);
GO

CREATE TABLE Alumnos (
    CodigoAlumno varchar(9),
    Nombres varchar(30) NOT NULL,
    Apellidos varchar(30) NOT NULL,
    Correo varchar(50) NOT NULL,
    Usuario varchar(30) NOT NULL,
    Contraseña varchar(8) NOT NULL,
    FechaRegistro varchar(10) NOT NULL,
    NumeroFaltas int,
    Estado varchar(10),
    CONSTRAINT PK_Alumnos PRIMARY KEY (CodigoAlumno),
    CONSTRAINT U_Alumnos_usuario UNIQUE (Usuario),
    CONSTRAINT chk_Alumnos_Estado CHECK (Estado IN ('ACTIVO', 'BLOQUEADO'))
);
GO

CREATE TABLE Ejemplares (
    EjemplarID varchar(12),
    LibroID varchar(9) NOT NULL,
    Estado varchar(20) NOT NULL,
    CONSTRAINT PK_Ejemplares PRIMARY KEY (EjemplarID),
    CONSTRAINT FK_Ejemplares_LibroID FOREIGN KEY (LibroID) REFERENCES Libros (LibroID),
    CONSTRAINT chk_Ejemplares_Estado CHECK (Estado IN ('DISPONIBLE', 'PRESTADO', 'EN REPARACION', 'PERDIDO'))
);
GO

CREATE TABLE Empleados (
    EmpleadoID INT IDENTITY(1,1),
    Tipo varchar(20) NOT NULL,
    Nombres varchar(30) NOT NULL,
    Apellidos varchar(30) NOT NULL,
    Usuario varchar(30) NOT NULL,
    Contraseña varchar(8) NOT NULL,
    FechaContratacion varchar(10) NOT NULL,
    Estado varchar(10) NOT NULL,
    CONSTRAINT PK_Empleados PRIMARY KEY (EmpleadoID),
    CONSTRAINT chk_Empleados_Tipo CHECK (Tipo IN ('BIBLIOTECARIO', 'ADMINISTRADOR')),
    CONSTRAINT chk_Empleados_Estado CHECK (Estado IN ('EN LABOR', 'NO LABORA')),
    CONSTRAINT U_Empleados_usuario UNIQUE (Usuario)
);
GO

CREATE TABLE Prestamos (
    PrestamoID int IDENTITY(1,1),
    CodigoAlumno varchar(9) NOT NULL,
    EjemplarID varchar(12) NOT NULL,
    EmpleadoID int NOT NULL,
    FechaPrestamo varchar(10) NOT NULL,
    FechaDevolucion varchar(10) NOT NULL,
    TipoPrestamo varchar(10) NOT NULL,
    Estado varchar(20) NOT NULL,
    CONSTRAINT PK_Prestamos PRIMARY KEY (PrestamoID),
    CONSTRAINT FK_Prestamos_CodigoAlumno FOREIGN KEY (CodigoAlumno) REFERENCES Alumnos (CodigoAlumno),
    CONSTRAINT FK_Prestamos_EjemplarID FOREIGN KEY (EjemplarID) REFERENCES Ejemplares (EjemplarID),
    CONSTRAINT FK_Prestamos_EmpleadoID FOREIGN KEY (EmpleadoID) REFERENCES Empleados (EmpleadoID),
    CONSTRAINT chk_Prestamos_Tipo CHECK (TipoPrestamo IN ('SALA', 'LLEVAR')),
    CONSTRAINT chk_Prestamos_Estado CHECK (Estado IN ('DEVUELTO', 'NO DEVUELTO'))
);
GO

CREATE TABLE Devoluciones (
    ID INT IDENTITY(1,1),
    PrestamoID INT NOT NULL,
    CodigoAlumno varchar(9) NOT NULL,
    EmpleadoID INT NOT NULL,
    EjemplarID varchar(12) NOT NULL,
    FechaPrestamo varchar(10) NOT NULL,
    FechaDevolucion varchar(10) NOT NULL,
    CONSTRAINT PK_Devoluciones PRIMARY KEY (ID),
    CONSTRAINT FK_Devoluciones_CodigoAlumno FOREIGN KEY (CodigoAlumno) REFERENCES Alumnos (CodigoAlumno),
    CONSTRAINT FK_Devoluciones_PrestamoID FOREIGN KEY (PrestamoID) REFERENCES Prestamos (PrestamoID),
    CONSTRAINT FK_Devoluciones_EjemplarID FOREIGN KEY (EjemplarID) REFERENCES Ejemplares (EjemplarID),
    CONSTRAINT FK_Devoluciones_EmpleadoID FOREIGN KEY (EmpleadoID) REFERENCES Empleados (EmpleadoID)
);
GO

CREATE TABLE Penalizaciones (
    PenalizacionID INT IDENTITY(1,1),
    CodigoAlumno varchar(9) NOT NULL,
    Tipo varchar(50) NOT NULL,
	EmpleadoID int not null,
    FechaPenalizacion varchar(10) NOT NULL,
    Monto money NOT NULL,
    Estado varchar(20) NOT NULL,
    CONSTRAINT PK_Penalizaciones PRIMARY KEY (PenalizacionID),
    CONSTRAINT FK_Penalizaciones_CodigoAlumno FOREIGN KEY (CodigoAlumno) REFERENCES Alumnos (CodigoAlumno),
	CONSTRAINT FK_Penalizaciones_EmpleadoID FOREIGN KEY (EmpleadoID) REFERENCES Empleados (EmpleadoID),
    CONSTRAINT chk_Penalizaciones_Estado CHECK (Estado IN ('PAGADO', 'NO PAGADO')),
    CONSTRAINT chk_Penalizaciones_Tipo CHECK (Tipo IN ('LIMITE DE FALTAS', 'PERDIDA DE LIBRO', 'LIBRO DAÑADO'))
);
GO

CREATE TABLE TarifaPenalizaciones (
    Tipo varchar(30) NOT NULL,
    Descripcion varchar(100) NOT NULL,
    Monto money NOT NULL,
);
GO

CREATE TABLE Faltas (
    MinDias int not null,
	MaxDias int not null,
    PesoFalta int NOT NULL,
    Descripcion varchar(100) NOT NULL,
);
GO

CREATE TABLE Guia (
    Parametro varchar(30) NOT NULL,
    Valor int NOT NULL,
);
GO







