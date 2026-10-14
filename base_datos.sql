CREATE DATABASE IF NOT EXISTS sistema_alquiler_carros;

USE sistema_alquiler_carros;


CREATE TABLE IF NOT EXISTS cliente (

    cedula VARCHAR(30) PRIMARY KEY,
    nombre_completo VARCHAR(100) NOT NULL,
    telefono VARCHAR(30) NOT NULL,
    correo VARCHAR(100) NOT NULL

    );


CREATE TABLE IF NOT EXISTS tipo_vehiculo (

    nombre VARCHAR(50) PRIMARY KEY,
    categoria VARCHAR(20) NOT NULL,
    requiere_pasajeros BOOLEAN NOT NULL,
    requiere_traccion BOOLEAN NOT NULL,
    requiere_capacidad_carga BOOLEAN NOT NULL

    );


CREATE TABLE IF NOT EXISTS vehiculo (

    placa VARCHAR(20) PRIMARY KEY,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    anio INT NOT NULL,
    tarifa_diaria DECIMAL(12,2) NOT NULL,
    estado VARCHAR(20) NOT NULL,

    tipo_nombre VARCHAR(50) NOT NULL,

    cantidad_pasajeros INT,
    tipo_traccion VARCHAR(20),
    capacidad_carga DECIMAL(12,2),

    FOREIGN KEY (tipo_nombre)
    REFERENCES tipo_vehiculo(nombre)

    );


CREATE TABLE IF NOT EXISTS alquiler (

    numero_alquiler INT AUTO_INCREMENT PRIMARY KEY,

    cedula_cliente VARCHAR(30) NOT NULL,
    placa_vehiculo VARCHAR(20) NOT NULL,

    fecha_inicio DATE NOT NULL,
    cantidad_dias INT NOT NULL,
    fecha_devolucion_real DATE,

    tarifa_diaria_aplicada DECIMAL(12,2) NOT NULL,
    deposito_garantia DECIMAL(12,2) NOT NULL,

    estado VARCHAR(20) NOT NULL,

    FOREIGN KEY (cedula_cliente)
    REFERENCES cliente(cedula),

    FOREIGN KEY (placa_vehiculo)
    REFERENCES vehiculo(placa)

    );


INSERT IGNORE INTO tipo_vehiculo
(
    nombre,
    categoria,
    requiere_pasajeros,
    requiere_traccion,
    requiere_capacidad_carga
)

VALUES

('Sedán', 'PASAJEROS', TRUE, FALSE, FALSE),
('SUV', 'PASAJEROS', TRUE, TRUE, FALSE),
('Minivan', 'PASAJEROS', TRUE, TRUE, FALSE),
('CUV', 'PASAJEROS', TRUE, TRUE, FALSE),
('Deportivo', 'PASAJEROS', TRUE, TRUE, FALSE),
('Pickup', 'CARGA', FALSE, TRUE, TRUE);