package dds.monedero.model;

import java.time.LocalDate;



//AL HABER SOLO DOS TIPOS DE MOVIME
abstract public class Movimiento {
  private final LocalDate fecha;
  private final double monto;

  //NO ES NECESARIA ESTA FUNCION, YA QUE estamos dandole responsabilidad a el movimiento de agregarse en la cuenta,
  // cuando en realidad, la cuenta es la que tiene que agregar el movimiento
  //CODE SMELL agregateA(Cuenta cuenta):  posible LARGE CLASS ya que le estamos dando responsabilidad de mas a el movimiento?

  //EN VEZ DE TENER UNA VARIABLE BOOLEANA DE SI ES DEPOSITO, le daria mejor esa responsabilidad a una clase
  //que tenga la responsabilidad de realizar ciertas tareas
  //CODE SMELL: PRIMITIVE OBSESSION? ya que estamos representando con booleano, cuando podria ser un obetjo con comportamiento

  public Movimiento(LocalDate fecha, double monto) {
    this.fecha = fecha;
    this.monto = monto;
  }

  public double getMonto() {
    return monto;
  }
  public boolean esDeLaFecha(LocalDate fecha) {
    return this.fecha.equals(fecha);
  }
  //este metodo nose si va a servir de algo
  abstract public double calcularValor(double saldo);
}





class Deposito extends Movimiento{
  public Deposito(LocalDate fecha, double monto) {
    super(fecha, monto);
  }
  //TODO public double calcularValor(double saldo): analizar este metodo ya que no se utiliza para nada
  public double calcularValor(double saldo){
    return saldo + this.getMonto();
  }

}


class Extraccion extends Movimiento{
  public Extraccion(LocalDate fecha, double monto) {
    super(fecha, monto);
  }
  public double calcularValor(double saldo){
    return saldo - this.getMonto();
  }

}

