# Diagnóstico del ZIP de CrediYa y cambios realizados

## Estado observado en el ZIP recibido

| Área | Estado al recibir el ZIP | Acción realizada |
|---|---|---|
| Maven, JDBC y MySQL | Configuración inicial presente; conexión previamente probada en el equipo del estudiante | Se conservó la configuración JDBC y la dependencia Connector/J. |
| Modelos POO | Persona, Cliente, Empleado, Prestamo, Pago y EstadoPrestamo presentes | Se conservaron sin reescritura funcional. |
| DAO | CRUD de clientes, empleados y préstamos; pagos transaccionales | Se conservaron. Se añadió `PrestamoDAO.listarPorCliente(int)`. |
| Archivos TXT | ArchivoService generaba y leía cuatro reportes | Se conservó y se usa desde el controlador para refrescar reportes después de cambios. |
| MVC | Vista y controlador existentes; el controlador solo tenía CRUD real de clientes y otras secciones provisionales | Se completaron empleados, préstamos, pagos, reportes y navegación. |
| Reportes Lambda/Stream | No existía implementación real | Se añadió `ReporteService` con filtros para activos, vencidos y clientes morosos. |
| Clase principal | `CrediYa.java` solo verificaba conexión y tablas | Ahora verifica conexión y arranca `MenuControlador`. |
| Documentación | No había README ni UML completos | Se agregaron `README.md` y el diagrama PlantUML. |
| Seguridad y empaquetado | El ZIP recibido incluía `.git`, `target` y `db.properties` local | Se excluyeron esos elementos del ZIP final. |

## Funcionalidades completadas en el código

- Menús funcionales para clientes, empleados, préstamos y pagos.
- CRUD de clientes y empleados conectado a sus DAO.
- Registro, búsqueda, listado, ajuste de saldo y eliminación de préstamos sujeto a las claves foráneas.
- Registro transaccional de pagos, búsqueda y consulta de historial.
- Sincronización de los cuatro reportes TXT después de operaciones de escritura exitosas desde el menú.
- Reportes de préstamos activos, vencidos y clientes morosos utilizando Lambda y Stream API.
- Inicio de la aplicación final desde `com.crediya.CrediYa`.

## Verificación realizada en este entorno

- Se compilaron todos los archivos fuente Java con `javac --release 21 -encoding UTF-8` sin errores.
- Se revisó la estructura y el contenido del ZIP final para comprobar que no incluya `.git/`, `target/`, archivos `.class` ni `src/main/resources/db.properties`.
- No se ejecutó una prueba contra la instancia MySQL local del estudiante, porque esa instancia no está disponible desde este entorno. La conexión y las operaciones JDBC deben volver a comprobarse en el computador del estudiante después de configurar su archivo local `db.properties`.
