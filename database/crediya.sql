/*-- 1. CREAR Y SELECCIONAR LA BASE DE DATOS*/
CREATE DATABASE IF NOT EXISTS crediya_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_0900_ai_ci;

USE crediya_db;

-- 2. TABLA DE EMPLEADOS
CREATE TABLE IF NOT EXISTS empleados (
    id          INT NOT NULL AUTO_INCREMENT,
    nombre      VARCHAR(80) NOT NULL,
    documento   VARCHAR(30) NOT NULL,
    rol         VARCHAR(30) NOT NULL,
    correo      VARCHAR(80) NULL,
    salario     DECIMAL(12,2) NOT NULL,

    CONSTRAINT pk_empleados PRIMARY KEY (id),
    CONSTRAINT uq_empleados_documento UNIQUE (documento),
    CONSTRAINT chk_salario CHECK (salario >= 0)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;

-- 3. TABLA DE CLIENTES
CREATE TABLE IF NOT EXISTS clientes (
    id          INT NOT NULL AUTO_INCREMENT,
    nombre      VARCHAR(80) NOT NULL,
    documento   VARCHAR(30) NOT NULL,
    correo      VARCHAR(80) NULL,
    telefono    VARCHAR(20) NULL,

    CONSTRAINT pk_clientes PRIMARY KEY (id),
    CONSTRAINT uq_clientes_documento UNIQUE (documento)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;

-- 4. TABLA DE PRESTAMOS
CREATE TABLE IF NOT EXISTS prestamos (
    id                  INT NOT NULL AUTO_INCREMENT,
    cliente_id          INT NOT NULL,
    empleado_id         INT NOT NULL,
    monto               DECIMAL(12,2) NOT NULL,
    interes             DECIMAL(5,2) NOT NULL,
    cuotas              INT NOT NULL,
    fecha_inicio        DATE NOT NULL,
    fecha_vencimiento   DATE NOT NULL,
    monto_total         DECIMAL(12,2) NOT NULL,
    valor_cuota         DECIMAL(12,2) NOT NULL,
    saldo_pendiente     DECIMAL(12,2) NOT NULL,
    estado              VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',

    CONSTRAINT pk_prestamos PRIMARY KEY (id),
    INDEX idx_prestamos_cliente (cliente_id),
    INDEX idx_prestamos_empleado (empleado_id),
    INDEX idx_prestamos_estado_vencimiento (estado, fecha_vencimiento),

    CONSTRAINT fk_prestamo_cliente
        FOREIGN KEY (cliente_id)
        REFERENCES clientes (id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_prestamo_empleado
        FOREIGN KEY (empleado_id)
        REFERENCES empleados (id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT chk_prestamo_monto CHECK (monto > 0),
    CONSTRAINT chk_prestamo_interes CHECK (interes >= 0),
    CONSTRAINT chk_prestamo_cuotas CHECK (cuotas > 0),
    CONSTRAINT chk_prestamo_fechas CHECK (fecha_vencimiento >= fecha_inicio),
    CONSTRAINT chk_prestamo_estado
        CHECK (estado IN ('PENDIENTE', 'PAGADO')),
    CONSTRAINT chk_prestamo_saldos
        CHECK (
            monto_total >= monto
            AND valor_cuota > 0
            AND saldo_pendiente >= 0
            AND saldo_pendiente <= monto_total
        )
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;

-- 5. TABLA DE PAGOS
CREATE TABLE IF NOT EXISTS pagos (
    id          INT NOT NULL AUTO_INCREMENT,
    prestamo_id INT NOT NULL,
    fecha_pago  DATE NOT NULL,
    monto       DECIMAL(12,2) NOT NULL,

    CONSTRAINT pk_pagos PRIMARY KEY (id),
    INDEX idx_pagos_prestamo_fecha (prestamo_id, fecha_pago),

    CONSTRAINT fk_pago_prestamo
        FOREIGN KEY (prestamo_id)
        REFERENCES prestamos (id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT chk_pago_monto CHECK (monto > 0)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;

-- 6. VERIFICACION: BASE DE DATOS ACTUAL
SELECT DATABASE() AS base_de_datos_seleccionada;

-- 7. VERIFICACION: TABLAS CREADAS
SHOW TABLES;

-- 8. VERIFICACION: CANTIDAD DE COLUMNAS POR TABLA
SELECT
    TABLE_NAME AS tabla,
    COUNT(*) AS total_columnas
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA = 'crediya_db'
  AND TABLE_NAME IN ('empleados', 'clientes', 'prestamos', 'pagos')
GROUP BY TABLE_NAME
ORDER BY TABLE_NAME;

-- Cantidades esperadas:
-- clientes = 5, empleados = 6, pagos = 4, prestamos = 12.

-- 9. VERIFICACION: CLAVES FORANEAS Y RELACIONES
SELECT
    TABLE_NAME AS tabla,
    COLUMN_NAME AS columna,
    CONSTRAINT_NAME AS nombre_clave_foranea,
    REFERENCED_TABLE_NAME AS tabla_referenciada,
    REFERENCED_COLUMN_NAME AS columna_referenciada
FROM information_schema.KEY_COLUMN_USAGE
WHERE TABLE_SCHEMA = 'crediya_db'
  AND REFERENCED_TABLE_NAME IS NOT NULL
ORDER BY TABLE_NAME, COLUMN_NAME;

-- Relaciones esperadas:
-- prestamos.cliente_id  -> clientes.id
-- prestamos.empleado_id -> empleados.id
-- pagos.prestamo_id     -> prestamos.id

-- 10. VERIFICACION: DETALLE DE COLUMNAS
SELECT
    TABLE_NAME AS tabla,
    COLUMN_NAME AS columna,
    COLUMN_TYPE AS tipo,
    IS_NULLABLE AS permite_nulos,
    COLUMN_KEY AS tipo_clave,
    EXTRA AS atributo_adicional
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA = 'crediya_db'
  AND TABLE_NAME IN ('empleados', 'clientes', 'prestamos', 'pagos')
ORDER BY TABLE_NAME, ORDINAL_POSITION;

-- FIN DEL SCRIPT CREDIYA
