# Funcionamiento del Examen #1

<img width="401" height="382" alt="Captura de pantalla 2026-11-13 205728" src="https://github.com/user-attachments/assets/10738f34-9b44-4904-b5ca-1fefc0b01ba9" />

# Funcionamiento del Examen#1
<img width="377" height="248" alt="Captura de pantalla 2026-11-13 205709" src="https://github.com/user-attachments/assets/67ca9285-9776-4588-bf51-0e1b53939a68" />


# Desarrollando del Examen#1

<img width="401" height="382" alt="Captura de pantalla 2026-11-13 205728" src="https://github.com/user-attachments/assets/10738f34-9b44-4904-b5ca-1fefc0b01ba9" />


# Sistema de Cobros de Cartera CrediYa S.A.S.

Aplicación de consola desarrollada en Java 21 para administrar empleados, clientes, préstamos y pagos de CrediYa S.A.S. El proyecto utiliza programación orientada a objetos, JDBC para conectarse a MySQL, archivos de texto para generar reportes y una organización MVC sencilla.

## 1. Tecnologías

- Java 21.
- Maven. /herramienta de software para la gestión, automatización y construcción (build) de proyectos.
- MySQL Server 8.x.
- MySQL Workbench (para ejecutar y revisar el script SQL).
- Apache NetBeans 25 (IDE recomendado; también puede utilizarse otro IDE Java).
- MySQL Connector/J 9.7.0.



## 2. Funcionalidades

### Clientes

- Registrar, buscar por ID, listar, actualizar y eliminar clientes.
- Consultar los préstamos asociados a un cliente.
- No permite eliminar un cliente que tenga préstamos asociados, debido a la clave foránea.

### Empleados

- Registrar, buscar por ID, listar, actualizar y eliminar empleados.
- No permite eliminar un empleado que tenga préstamos asociados, debido a la clave foránea.

### Préstamos

- Crear un préstamo relacionado con un cliente existente y un empleado existente.
- Calcular el interés simple, el monto total, el valor de cuota y el saldo inicial.
- Buscar y listar préstamos.
- Consultar préstamos por cliente.
- Ajustar el saldo pendiente desde el menú. El modelo valida que el saldo no sea negativo ni supere el monto total; al quedar en cero, el estado pasa a `PAGADO`.
- Eliminar un préstamo solo cuando MySQL lo permita. Si tiene pagos asociados, la clave foránea impide eliminarlo.

Fórmula académica utilizada:

```text
interes = monto * porcentajeInteres / 100
montoTotal = monto + interes
valorCuota = montoTotal / numeroCuotas
```

Los valores calculados se redondean a dos decimales. Se utiliza `double` porque es el tipo trabajado en esta asignatura; para sistemas financieros de producción se suele elegir un tipo decimal de precisión exacta.

### Pagos

- Registrar un abono a un préstamo existente.
- Buscar un pago por ID.
- Consultar el historial de pagos de un préstamo.
- Rechazar montos iguales a cero, negativos o superiores al saldo pendiente.
- Actualizar el saldo y el estado del préstamo dentro de una transacción JDBC.
- Conservar el historial de pagos; el menú no ofrece borrar o modificar pagos.

### Reportes

- Generar cuatro archivos de texto: clientes, empleados, préstamos y pagos.
- Consultar los archivos generados desde el menú.
- Consultar préstamos activos, préstamos vencidos y clientes morosos usando Lambda y Stream API.

**Definición académica de cliente moroso:** cliente que tiene al menos un préstamo cuya fecha general de vencimiento ya pasó y cuyo saldo pendiente sigue siendo mayor que cero. El esquema actual no contiene un calendario de vencimiento por cuota, por lo que el sistema no calcula mora individual por cuota.

## 3. Estructura del proyecto

```text
CrediYa/
├── pom.xml
├── .gitignore
├── README.md
├── database/
│   └── crediya.sql
├── docs/
│   ├── DIAGNOSTICO_FINAL.md
│   └── diagrama_crediya.puml
├── evidencias/
│   ├── 01_esquema_crediya.png
│   ├── 02_tablas_mysql.png
│   ├── 03_clientes_mysql.png
│   ├── 04_empleados_mysql.png
│   ├── 05_prestamos_relaciones_mysql.png
│   ├── 06_historial_pagos_mysql.png
│   ├── 07_crud_clientes_java.png
│   ├── 08_resumen_evidencias_java.png
│   └── evidenciasSQL.sql
├── reportes/
│   ├── clientes.txt
│   ├── empleados.txt
│   ├── prestamos.txt
│   └── pagos.txt
└── src/main/
    ├── java/com/crediya/
    │   ├── CrediYa.java
    │   ├── Prueba*.java
    │   ├── controller/MenuControlador.java
    │   ├── dao/ClienteDAO.java
    │   ├── dao/EmpleadoDAO.java
    │   ├── dao/PrestamoDAO.java
    │   ├── dao/PagoDAO.java
    │   ├── database/ConexionDB.java
    │   ├── model/Persona.java
    │   ├── model/Cliente.java
    │   ├── model/Empleado.java
    │   ├── model/Prestamo.java
    │   ├── model/Pago.java
    │   ├── model/EstadoPrestamo.java
    │   ├── service/ArchivoService.java
    │   ├── service/ReporteService.java
    │   └── view/MenuVista.java
    └── resources/
        └── db.properties.example
```

Se conservan los paquetes y rutas Maven que ya existían en el proyecto. No se trasladó `ConexionDB` ni se cambió el paquete de la clase principal.

## 4. Crear la base de datos

1. Abrir MySQL Workbench y conectarse a la instancia local de MySQL.
2. Abrir el archivo `database/crediya.sql`.
3. Ejecutar el script.
4. Comprobar que exista `crediya_db` y que contenga las tablas `clientes`, `empleados`, `prestamos` y `pagos`.

El script utiliza `CREATE DATABASE IF NOT EXISTS` y `CREATE TABLE IF NOT EXISTS`. No contiene instrucciones `DROP` y no elimina registros existentes. Si una tabla ya existe, `CREATE TABLE IF NOT EXISTS` no modifica su estructura; por tanto, los cambios futuros de estructura deben revisarse manualmente.

Las relaciones principales son:

- `prestamos.cliente_id` → `clientes.id`.
- `prestamos.empleado_id` → `empleados.id`.
- `pagos.prestamo_id` → `prestamos.id`.

Para consultas de verificación y las evidencias se conserva `evidencias/evidenciasSQL.sql`.

## 5. Configurar la conexión JDBC

El proyecto incluye `src/main/resources/db.properties.example`, sin credenciales reales.

1. Dentro de `src/main/resources`, copia `db.properties.example`.
2. Renombra la copia como `db.properties`.
3. Abre el nuevo `db.properties` y coloca el usuario y la contraseña local de tu instancia MySQL.
4. No compartas ese archivo ni lo agregues a Git. Está excluido por `.gitignore`.

Ejemplo de configuración (utiliza tus datos locales):

```properties
db.url=jdbc:mysql://127.0.0.1:3306/crediya_db?serverTimezone=America/Bogota&characterEncoding=UTF-8
db.username=root
db.password=CAMBIAR_POR_TU_PASSWORD
```

El marcador de ejemplo debe sustituirse únicamente en el archivo local `db.properties`. No pongas la contraseña real en Java, SQL, README ni capturas.

## 6. Abrir, compilar y ejecutar en NetBeans 25

1. Extraer `CrediYa_Final.zip` en una carpeta local.
2. Configurar `db.properties` como se indica en la sección anterior.
3. En NetBeans seleccionar **File → Open Project** y elegir la carpeta `CrediYa` que contiene `pom.xml`.
4. Esperar a que Maven cargue la dependencia MySQL Connector/J.
5. Ejecutar **Clean and Build**. El resultado esperado es `BUILD SUCCESS`.
6. Abrir `src/main/java/com/crediya/CrediYa.java`.
7. Hacer clic derecho y seleccionar **Run File**, o ejecutar el proyecto si `CrediYa` está configurada como clase principal.

La aplicación comprueba la conexión JDBC al iniciar y después muestra el menú principal.

## 7. Ejecutar con Maven desde una terminal

Con JDK 21 y Maven instalados, desde la carpeta raíz del proyecto:

```bash
mvn clean package
```

Para iniciar directamente con el complemento Exec de Maven, puede ejecutarse:

```bash
mvn org.codehaus.mojo:exec-maven-plugin:3.1.0:java -Dexec.mainClass=com.crediya.CrediYa
```

El archivo JAR por sí solo no contiene el driver JDBC. Para ejecutarlo directamente con `java`, hay que incluir el JAR del conector MySQL en el classpath además del JAR de CrediYa y haber empaquetado el recurso local `db.properties`.

## 8. Uso del menú

El menú principal tiene estas opciones:

1. **Gestión de clientes:** registrar, buscar, listar, actualizar, eliminar y consultar préstamos de un cliente.
2. **Gestión de empleados:** registrar, buscar, listar, actualizar y eliminar.
3. **Gestión de préstamos:** registrar, buscar, listar, ajustar saldo pendiente y eliminar cuando las relaciones lo permitan.
4. **Gestión de pagos:** registrar pagos, buscar por ID y consultar el historial de un préstamo.
5. **Generar reportes TXT:** vuelve a consultar MySQL y genera los cuatro reportes.
6. **Consultar reportes y cartera:** permite leer los TXT y consultar activos, vencidos y morosos con Stream API.
0. **Salir:** termina el menú.

En cada submenú, la opción `0` regresa al menú principal.

Para registrar un préstamo primero deben existir el cliente y el empleado. Las fechas deben introducirse con el formato `AAAA-MM-DD`. Los porcentajes de interés se escriben como número, por ejemplo `10` para diez por ciento. Los montos aceptan punto o coma decimal.

El ajuste de saldo de un préstamo es una operación administrativa de corrección. Los abonos normales deben registrarse desde el módulo de pagos para conservar su historial.

## 9. Archivos TXT

Los archivos se crean o actualizan dentro de `reportes/`:

- `reportes/clientes.txt`
- `reportes/empleados.txt`
- `reportes/prestamos.txt`
- `reportes/pagos.txt`

La base de datos MySQL es la fuente principal de información. Después de una operación de escritura exitosa desde el menú, el controlador vuelve a consultar MySQL y actualiza los reportes. Si la escritura TXT falla después de guardar en MySQL, el programa informa que el cambio quedó en la base de datos pero el reporte no pudo actualizarse.

Los archivos TXT son reportes de consulta; no sustituyen a MySQL. Se pueden regenerar en cualquier momento desde la opción 5 del menú.

## 10. Pruebas independientes incluidas

Se conservan las clases de prueba del proyecto:

- `PruebaClienteDAO.java`
- `PruebaEmpleadoDAO.java`
- `PruebaPrestamoDAO.java`
- `PruebaPagoDAO.java`
- `PruebaValidacionesPagoDAO.java`
- `PruebaArchivoService.java`
- `PruebaMenuControlador.java`

Se ejecutan desde NetBeans con clic derecho sobre la clase → **Run File**. Estas pruebas se conectan a la base MySQL configurada y algunas insertan, actualizan o eliminan registros de prueba. Antes de ejecutarlas en una base con información importante, realiza un respaldo y revisa el código de la prueba.

La ejecución normal de la aplicación debe iniciarse desde `CrediYa.java`.

