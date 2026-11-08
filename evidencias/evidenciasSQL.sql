-- ============================================================================
-- PROYECTO: CrediYa S.A.S.
-- ARCHIVO: consultas_verificacion.sql
-- OBJETIVO: Generar consultas de solo lectura para las evidencias de MySQL.
-- BASE DE DATOS: crediya_db
--
-- INSTRUCCIONES:
--   1. Abrir este archivo en MySQL Workbench.
--   2. Ejecutar una sección a la vez usando el rayo.
--   3. Ejecutar en orden para que no falle. 
--
-- IMPORTANTE:
--   Este archivo solo contiene consultas de lectura y metadatos.
--   No inserta, actualiza ni elimina registros.
-- ============================================================================

USE crediya_db;

-- ============================================================================
-- EVIDENCIA 01: ESQUEMA DE LA BASE DE DATOS
-- ARCHIVO DE CAPTURA: 01_esquema_crediya.png
-- Objetivo: confirmar que crediya_db existe y está seleccionada.
-- ============================================================================

SELECT DATABASE() AS base_de_datos_seleccionada;

SELECT SCHEMA_NAME AS esquema_existente
FROM information_schema.SCHEMATA
WHERE SCHEMA_NAME = 'crediya_db';


-- ============================================================================
-- EVIDENCIA 02: TABLAS Y RELACIONES
-- ARCHIVO DE CAPTURA: 02_tablas_mysql.png
-- Objetivo: mostrar las cuatro tablas y sus claves foráneas.
-- Además de ejecutar esta sección, expandir Tables en el panel SCHEMAS.
-- ============================================================================

SHOW TABLES;

-- Cantidad de columnas de cada tabla.
SELECT
    TABLE_NAME AS tabla,
    COUNT(*) AS total_columnas
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA = 'crediya_db'
  AND TABLE_NAME IN ('clientes', 'empleados', 'prestamos', 'pagos')
GROUP BY TABLE_NAME
ORDER BY TABLE_NAME;

-- Relaciones entre las tablas (claves foráneas).
SELECT
    TABLE_NAME AS tabla,
    COLUMN_NAME AS columna,
    REFERENCED_TABLE_NAME AS tabla_referenciada,
    REFERENCED_COLUMN_NAME AS columna_referenciada
FROM information_schema.KEY_COLUMN_USAGE
WHERE TABLE_SCHEMA = 'crediya_db'
  AND REFERENCED_TABLE_NAME IS NOT NULL
ORDER BY TABLE_NAME, COLUMN_NAME;


-- ============================================================================
-- EVIDENCIA 03: CLIENTES REGISTRADOS
-- ARCHIVO DE CAPTURA: 03_clientes_mysql.png
-- Objetivo: visualizar los clientes almacenados desde Java.
-- ============================================================================

SELECT
    id,
    nombre,
    documento,
    correo,
    telefono
FROM clientes
ORDER BY id;


-- ============================================================================
-- EVIDENCIA 04: EMPLEADOS REGISTRADOS
-- ARCHIVO DE CAPTURA: 04_empleados_mysql.png
-- Objetivo: visualizar los empleados almacenados desde Java.
-- ============================================================================

SELECT
    id,
    nombre,
    documento,
    rol,
    correo,
    salario
FROM empleados
ORDER BY id;


-- ============================================================================
-- EVIDENCIA 05: PRESTAMOS Y RELACIONES
-- ARCHIVO DE CAPTURA: 05_prestamos_relaciones_mysql.png
-- Objetivo: mostrar cada préstamo junto con su cliente y empleado.
-- ============================================================================

SELECT
    p.id AS prestamo_id,
    c.id AS cliente_id,
    c.nombre AS cliente,
    e.id AS empleado_id,
    e.nombre AS empleado,
    p.monto,
    p.interes,
    p.cuotas,
    p.monto_total,
    p.saldo_pendiente,
    p.estado
FROM prestamos p
INNER JOIN clientes c
    ON p.cliente_id = c.id
INNER JOIN empleados e
    ON p.empleado_id = e.id
ORDER BY p.id;


-- ============================================================================
-- EVIDENCIA 06: HISTORIAL DE PAGOS
-- ARCHIVO DE CAPTURA: 06_historial_pagos_mysql.png
-- Objetivo: mostrar los pagos registrados y el préstamo relacionado.
-- ============================================================================

SELECT
    pg.id AS pago_id,
    pg.prestamo_id,
    pg.fecha_pago,
    pg.monto AS monto_pagado,
    p.estado AS estado_prestamo
FROM pagos pg
INNER JOIN prestamos p
    ON pg.prestamo_id = p.id
ORDER BY pg.prestamo_id, pg.id;

-- Resumen de pagos por préstamo y comparación con el saldo guardado.
SELECT
    p.id AS prestamo_id,
    p.monto_total,
    COALESCE(SUM(pg.monto), 0) AS total_pagado,
    p.saldo_pendiente,
    p.estado
FROM prestamos p
LEFT JOIN pagos pg
    ON p.id = pg.prestamo_id
GROUP BY
    p.id,
    p.monto_total,
    p.saldo_pendiente,
    p.estado
ORDER BY p.id;



