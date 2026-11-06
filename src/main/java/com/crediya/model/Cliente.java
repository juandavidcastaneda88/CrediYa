package com.crediya.model;

/*
 * CLASE: Cliente
 *
 * Representa a los clientes que utilizan los servicios
 * de crédito de CrediYa S.A.S.
 *
 * Esta clase HEREDA de Persona, por lo tanto, reutiliza
 * sus atributos y métodos generales:
 *
 * id, nombre, documento y correo.
 *
 * Se utiliza el concepto de HERENCIA de la programación
 * orientada a objetos.
 */
public class Cliente extends Persona {

    /*
     * ATRIBUTO PROPIO DE CLIENTE
     *
     * Representa el número telefónico del cliente.
     *
     * Se declara private para aplicar ENCAPSULAMIENTO
     * y controlar el acceso mediante un getter y un setter.
     */
    private String telefono;


    /*
     * PRIMER CONSTRUCTOR
     *
     * Permite crear un cliente nuevo sin indicar su ID.
     *
     * El valor 0 representa que todavía no tiene un
     * identificador asignado por la base de datos.
     *
     * this(...) llama al segundo constructor de esta clase
     * para reutilizar la inicialización de los atributos.
     */
    public Cliente(
            String nombre,
            String documento,
            String correo,
            String telefono) {

        this(0, nombre, documento, correo, telefono);
    }


    /*
     * SEGUNDO CONSTRUCTOR
     *
     * Permite crear un cliente indicando todos sus datos,
     * incluido el identificador.
     *
     * Se utiliza, por ejemplo, cuando recuperamos un cliente
     * que ya existe en la base de datos.
     */
    public Cliente(
            int id,
            String nombre,
            String documento,
            String correo,
            String telefono) {

        /*
         * super(...) llama al constructor de la clase padre:
         * Persona.
         *
         * De esta manera reutilizamos la asignación de los
         * atributos id, nombre, documento y correo.
         */
        super(id, nombre, documento, correo);

        // Utilizamos el setter para asignar y validar el teléfono.
        setTelefono(telefono);
    }


    /*
     * GETTER Y SETTER
     *
     * El getter permite consultar el teléfono.
     * El setter permite modificarlo.
     */


    // GETTER: devuelve el teléfono del cliente.
    public String getTelefono() {
        return telefono;
    }


    /*
     * SETTER: permite modificar el teléfono.
     *
     * Se verifica que, si el teléfono tiene un valor,
     * no sea una cadena vacía o compuesta por espacios.
     *
     * Se permite null porque el atributo no es obligatorio
     * en nuestra implementación.
     */
    public void setTelefono(String telefono) {

        if (telefono != null && telefono.isBlank()) {
            throw new IllegalArgumentException(
                    "El teléfono no puede estar vacío."
            );
        }

        // this.telefono representa el atributo del objeto actual.
        this.telefono = telefono;
    }


    /*
     * MÉTODO obtenerTipoPersona()
     *
     * Este método fue declarado como abstracto en Persona.
     *
     * Aquí lo implementamos para indicar que esta clase
     * representa a un CLIENTE.
     *
     * @Override indica que estamos sobrescribiendo
     * un método heredado de la clase padre.
     */
    @Override
    public String obtenerTipoPersona() {
        return "CLIENTE";
    }


    /*
     * MÉTODO toString()
     *
     * Sobrescribe el método toString() heredado de Object.
     *
     * Permite obtener una representación en texto del cliente
     * y mostrar sus datos de manera organizada.
     *
     * Utilizamos los getters heredados de Persona para consultar
     * el ID, nombre, documento y correo.
     */
    @Override
    public String toString() {

        return "Cliente{"
                + "id=" + getId()
                + ", nombre='" + getNombre() + '\''
                + ", documento='" + getDocumento() + '\''
                + ", correo='" + getCorreo() + '\''
                + ", telefono='" + telefono + '\''
                + '}';
    }


    /*
     * =====================================================
     * PREGUNTAS 
     * =====================================================
     *
     * 1. ¿Para qué sirve la clase Cliente?
     * Respuesta: Para representar a los clientes que
     * utilizan los servicios de crédito de CrediYa.
     *
     * 2. ¿Por qué utilizas extends Persona?
     * Respuesta: Para aplicar herencia y reutilizar los
     * atributos y métodos de la clase Persona.
     *
     * 3. ¿Qué significa herencia?
     * Respuesta: Es un concepto de programación orientada
     * a objetos que permite que una clase hija reutilice
     * características de una clase padre.
     *
     * 4. ¿Para qué sirve super()?
     * Respuesta: Para llamar al constructor de la clase padre
     * y utilizar sus atributos y métodos de inicialización.
     *
     * 5. ¿Cuál es la diferencia entre this() y super()?
     * Respuesta: this() llama a otro constructor de la misma
     * clase, mientras que super() llama al constructor
     * de la clase padre.
     *
     * 6. ¿Para qué sirve private String telefono?
     * Respuesta: Para almacenar el teléfono del cliente
     * y proteger el atributo mediante encapsulamiento.
     *
     * 7. ¿Para qué sirve setTelefono()?
     * Respuesta: Para modificar el teléfono y comprobar
     * que no sea una cadena vacía cuando tiene un valor.
     *
     * 8. ¿Qué significa @Override?
     * Respuesta: Indica que estamos sobrescribiendo
     * un método heredado de otra clase.
     *
     * 9. ¿Por qué se sobrescribe obtenerTipoPersona()?
     * Respuesta: Para que Cliente indique que el tipo
     * de persona que representa es CLIENTE.
     *
     * 10. ¿Para qué sirve toString()?
     * Respuesta: Para mostrar los datos del cliente
     * en forma de texto.
     *
     * 11. ¿Por qué utilizamos los getters de Persona?
     * Respuesta: Porque los atributos de Persona son privados
     * y debemos acceder a ellos mediante sus métodos públicos.
     *
     * 12. ¿Qué relación existe entre Cliente y Persona?
     * Respuesta: Cliente es una clase hija de Persona
     * y hereda sus características generales.
     *
     * =====================================================
     */
}