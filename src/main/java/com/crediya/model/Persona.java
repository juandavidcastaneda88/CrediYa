package com.crediya.model;

/*
 * CLASE: Persona
 *
 * Representa los datos generales que comparten las personas
 * dentro del sistema CrediYa S.A.S.
 *
 * Es una clase abstracta porque no queremos crear personas
 * directamente. En su lugar, utilizamos sus clases hijas:
 * Cliente y Empleado.
 *
 * Aplica el concepto de ABSTRACCIÓN de la programación orientada
 * a objetos.
    
Una clase como Persona se crea como abstracta porque representa un 
concepto genérico que no debe existir por sí mismo en la realidad 
del sistema, sino que sirve exclusivamente como una plantilla base 
para otras clases más específicas.
 */
public abstract class Persona {

    /*
     * ATRIBUTOS
     *
     * Se utiliza private para aplicar ENCAPSULAMIENTO.
     * Esto significa que los atributos no pueden modificarse
     * directamente desde otras clases.
     *
     * Para consultar o modificar sus valores utilizamos
     * los métodos getters y setters.
     */
    private int id;
    private String nombre;
    private String documento;
    private String correo;


    /*
     * PRIMER CONSTRUCTOR
     *
     * Permite crear una persona sin indicar un identificador.
     *
     * Se utiliza protected porque este constructor está pensado
     * para ser utilizado por las clases que heredan de Persona.
     *
     * El valor 0 se asigna inicialmente al identificador porque
     * la base de datos puede asignarlo automáticamente.
     *
     * this(...) llama al segundo constructor de esta misma clase.
     */
    protected Persona(String nombre, String documento, String correo) {
        this(0, nombre, documento, correo);
    }


    /*
     * SEGUNDO CONSTRUCTOR
     *
     * Permite crear una persona indicando todos sus datos,
     * incluido el identificador.
     *
     * También puede ser utilizado por las clases hijas
     * cuando necesiten recuperar una persona desde la base de datos.
     */
    protected Persona(int id, String nombre, String documento, String correo) {

        // this hace referencia al objeto actual.
        // Asigna los valores recibidos a los atributos de la clase.
        this.id = id;
        this.nombre = nombre;
        this.documento = documento;
        this.correo = correo;
    }


    /*
     * MÉTODOS GETTERS Y SETTERS
     *
     * Los getters permiten consultar los valores de los atributos.
     * Los setters permiten modificar dichos valores.
     *
     * Estos métodos permiten acceder a los atributos privados
     * sin modificar directamente su visibilidad.
     */


    // GETTER Y SETTER DEL IDENTIFICADOR.

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    // GETTER Y SETTER DEL NOMBRE.

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    // GETTER Y SETTER DEL DOCUMENTO.

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }


    // GETTER Y SETTER DEL CORREO ELECTRÓNICO.

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }


    /*
     * MÉTODO ABSTRACTO
     *
     * Declara un método que debe ser implementado por las clases
     * que hereden de Persona.
     *
     * Cliente y Empleado implementan este método para indicar
     * qué tipo de persona representa cada una.
     *
     * Se utiliza el concepto de ABSTRACCIÓN y se relaciona
     * con el POLIMORFISMO.
     */
    public abstract String obtenerTipoPersona();


    /*
     * =====================================================
     * PREGUNTAS 
     * =====================================================
     *
     * 1. ¿Para qué sirve la clase Persona?
     * Respuesta: Para representar los datos generales que
     * comparten los clientes y los empleados.
     *
     * 2. ¿Por qué Persona es abstract?
     * Respuesta: Porque es una clase base y no queremos
     * crear objetos directamente de ella.
     *
     * 3. ¿Qué es la abstracción?
     * Respuesta: Es un concepto de programación orientada
     * a objetos que permite representar características
     * generales y dejar detalles para las clases hijas.
     *
     * 4. ¿Para qué sirve private?
     * Respuesta: Para aplicar encapsulamiento y proteger
     * los atributos de la clase.
     *
     * 5. ¿Para qué sirven los getters y setters?
     * Respuesta: Los getters permiten consultar los atributos
     * y los setters permiten modificarlos.
     *
     * 6. ¿Por qué los constructores son protected?
     * Respuesta: Para permitir que las clases hijas puedan
     * utilizar los constructores de Persona.
     *
     * 7. ¿Para qué sirve this?
     * Respuesta: Para hacer referencia al objeto actual.
     *
     * 8. ¿Qué significa this(0, nombre, documento, correo)?
     * Respuesta: Llama a otro constructor de la misma clase
     * para reutilizar la asignación de los atributos.
     *
     * 9. ¿Para qué sirve el método abstracto
     * obtenerTipoPersona()?
     * Respuesta: Para obligar a las clases hijas a definir
     * qué tipo de persona representan.
     *
     * 10. ¿Qué relación tiene Persona con Cliente y Empleado?
     * Respuesta: Cliente y Empleado heredan de Persona
     * y comparten sus atributos y métodos.
     *
     * =====================================================
     */
}