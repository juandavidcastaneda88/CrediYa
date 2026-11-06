package com.crediya.model;

/*
 * CLASE: Empleado
 *
 * Representa a los trabajadores de CrediYa S.A.S.
 *
 * Esta clase HEREDA de Persona, por lo tanto, reutiliza
 * los atributos y métodos generales de la clase padre:
 *
 * id, nombre, documento y correo.
 *
 * También incorpora sus propios atributos:
 * rol y salario.
 *
 * Se aplica el concepto de HERENCIA de la programación
 * orientada a objetos.
 */
public class Empleado extends Persona {

    /*
     * ATRIBUTOS PROPIOS DE EMPLEADO
     *
     * rol: representa el cargo o función del empleado.
     *
     * salario: representa el salario del trabajador.
     *
     * Se utiliza private para aplicar ENCAPSULAMIENTO
     * y controlar el acceso mediante getters y setters.
     *
     * double permite almacenar valores numéricos decimales.
     */
    private String rol;
    private double salario;


    /*
     * PRIMER CONSTRUCTOR
     *
     * Permite crear un empleado nuevo sin indicar su ID.
     *
     * El valor 0 indica que todavía no tiene un identificador
     * asignado por la base de datos.
     *
     * this(...) llama al segundo constructor de esta misma clase
     * para reutilizar la inicialización de los atributos.
     */
    public Empleado(
            String nombre,
            String documento,
            String correo,
            String rol,
            double salario) {

        this(0, nombre, documento, correo, rol, salario);
    }


    /*
     * SEGUNDO CONSTRUCTOR
     *
     * Permite crear un empleado indicando todos sus datos,
     * incluido el identificador.
     *
     * Se puede utilizar cuando recuperamos un empleado
     * que ya existe en la base de datos.
     */
    public Empleado(
            int id,
            String nombre,
            String documento,
            String correo,
            String rol,
            double salario) {

        /*
         * super(...) llama al constructor de Persona.
         *
         * Permite inicializar los atributos heredados:
         * id, nombre, documento y correo.
         */
        super(id, nombre, documento, correo);

        // Utilizamos los setters para asignar y validar los datos.
        setRol(rol);
        setSalario(salario);
    }


    /*
     * GETTER Y SETTER DEL ROL
     *
     * El getter permite consultar el cargo del empleado.
     * El setter permite modificarlo.
     */

    public String getRol() {
        return rol;
    }

    /*
     * SETTER DEL ROL
     *
     * Comprueba que el rol no sea null ni esté vacío.
     *
     * trim() elimina los espacios al principio y al final
     * del texto para guardar el valor limpio.
     */
    public void setRol(String rol) {

        if (rol == null || rol.isBlank()) {
            throw new IllegalArgumentException(
                    "El rol del empleado es obligatorio."
            );
        }

        this.rol = rol.trim();
    }


    /*
     * GETTER Y SETTER DEL SALARIO
     *
     * El getter permite consultar el salario.
     * El setter permite modificarlo.
     */

    public double getSalario() {
        return salario;
    }

    /*
     * SETTER DEL SALARIO
     *
     * Comprueba que el salario no sea negativo.
     *
     * Si el valor es menor que cero, se genera
     * una excepción para impedir que se almacene.
     */
    public void setSalario(double salario) {

        if (salario < 0) {
            throw new IllegalArgumentException(
                    "El salario no puede ser negativo."
            );
        }

        this.salario = salario;
    }


    /*
     * MÉTODO obtenerTipoPersona()
     *
     * Este método fue declarado como abstracto en Persona.
     *
     * Aquí lo implementamos para indicar que esta clase
     * representa a un EMPLEADO.
     *
     * @Override indica que estamos sobrescribiendo
     * un método de la clase padre.
     */
    @Override
    public String obtenerTipoPersona() {
        return "EMPLEADO";
    }


    /*
     * MÉTODO toString()
     *
     * Permite representar los datos del empleado como texto.
     *
     * Utilizamos los getters heredados de Persona para consultar
     * el ID, nombre, documento y correo.
     *
     * También mostramos el rol y el salario, que pertenecen
     * directamente a esta clase.
     */
    @Override
    public String toString() {

        return "Empleado{"
                + "id=" + getId()
                + ", nombre='" + getNombre() + '\''
                + ", documento='" + getDocumento() + '\''
                + ", correo='" + getCorreo() + '\''
                + ", rol='" + rol + '\''
                + ", salario=" + salario
                + '}';
    }


    /*
     * =====================================================
     * PREGUNTAS 
     * =====================================================
     *
     * 1. ¿Para qué sirve la clase Empleado?
     * Respuesta: Para representar a los trabajadores
     * de CrediYa S.A.S.
     *
     * 2. ¿Por qué utilizas extends Persona?
     * Respuesta: Para aplicar herencia y reutilizar
     * los atributos y métodos de Persona.
     *
     * 3. ¿Qué significa super()?
     * Respuesta: Permite llamar al constructor de la clase
     * padre para inicializar los atributos heredados.
     *
     * 4. ¿Para qué sirve this()?
     * Respuesta: Para llamar a otro constructor de la
     * misma clase y reutilizar su código.
     *
     * 5. ¿Por qué utilizas private?
     * Respuesta: Para aplicar encapsulamiento y controlar
     * el acceso a los atributos.
     *
     * 6. ¿Para qué sirve double?
     * Respuesta: Es un tipo de dato que permite almacenar
     * números con valores decimales, como el salario.
     *
     * 7. ¿Para qué sirve setSalario()?
     * Respuesta: Para asignar un salario al empleado
     * y comprobar que no sea negativo.
     *
     * 8. ¿Para qué sirve trim()?
     * Respuesta: Para eliminar los espacios al principio
     * y al final de un texto.
     *
     * 9. ¿Qué significa @Override?
     * Respuesta: Indica que estamos sobrescribiendo
     * un método heredado.
     *
     * 10. ¿Para qué sirve obtenerTipoPersona()?
     * Respuesta: Para indicar que el tipo de persona
     * representado por esta clase es EMPLEADO.
     *
     * 11. ¿Para qué sirve toString()?
     * Respuesta: Para mostrar los datos del empleado
     * en forma de texto.
     *
     * 12. ¿Por qué utilizamos los getters de Persona?
     * Respuesta: Porque los atributos de Persona son
     * privados y debemos acceder a ellos mediante
     * sus métodos públicos.
     *
     * =====================================================
     */
}