package dds.monedero.model;

import java.time.LocalDate;

public class Movimiento {
  private LocalDate fecha;
  //En ningún lenguaje de programación usen jamás doubles para modelar dinero en el mundo real
  //siempre usen numeros de precision arbitraria, como BigDecimal en Java y similares


  private double monto;
  private boolean esDeposito;


  //NO ES NECESARIA ESTA FUNCION, YA QUE estamos dandole responsabilidad a el movimiento de agregarse en la cuenta,
  // cuando en realidad, la cuenta es la que tiene que agregar el movimiento
  //CODE SMELL agregateA(Cuenta cuenta):  posible LARGE CLASS ya que le estamos dando responsabilidad de mas a el movimiento?




  //EN VEZ DE TENER UNA VARIABLE BOOLEANA DE SI ES DEPOSITO, le daria mejor esa responsabilidad a una clase
  //que tenga la responsabilidad de realizar ciertas tareas
  //TODO CODE SMELL: PRIMITIVE OBSESSION? ya que estamos representando con booleano, cuando podria ser un obetjo con comportamiento

  public Movimiento(LocalDate fecha, double monto, boolean esDeposito) {
    this.fecha = fecha;
    this.monto = monto;
    this.esDeposito = esDeposito;
  }

  public double getMonto() {
    return monto;
  }

  public LocalDate getFecha() {
    return fecha;
  }




  //ACA HAY LOGICA REPETIDA, ya que estas analizan lo mismo solo que de formaContraria
  public boolean fueDepositado(LocalDate fecha) {
    return isDeposito() && esDeLaFecha(fecha);
  }

  public boolean fueExtraido(LocalDate fecha) {
    return isExtraccion() && esDeLaFecha(fecha);
  }
  //




  public boolean esDeLaFecha(LocalDate fecha) {
    return this.fecha.equals(fecha);
  }

  public boolean isDeposito() {
    return esDeposito;
  }

  public boolean isExtraccion() {
    return !esDeposito;
  }



  //ADEMAS NO ES CORRECTO PREGUNTAR SI SE TRATA DE UN DEPOSITO O NO: TODO CODE SMELL: TYPE TEST?
  public double calcularValor(Cuenta cuenta) {
    if (esDeposito) {
      return cuenta.getSaldo() + getMonto();
    } else {
      return cuenta.getSaldo() - getMonto();
    }
  }
}
