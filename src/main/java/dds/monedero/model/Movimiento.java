package dds.monedero.model;

import java.time.LocalDate;



//AL HABER SOLO DOS TIPOS DE MOVIME
abstract public class Movimiento {
  private LocalDate fecha;
  private double monto;

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

  //ACA HAY LOGICA REPETIDA, ya que estas analizan lo mismo solo que de formaContraria
  abstract public double calcularValor(double saldo);
}





class Deposito extends Movimiento{
  public Deposito(LocalDate fecha, double monto) {
    super(fecha, monto);
  }
  public double calcularValor(double saldo){
    return saldo + this.getMonto();
  }
  public boolean fueDepositado(LocalDate fecha) {
    return  this.esDeLaFecha(fecha);
  }
}


class Extraccion extends Movimiento{
  public Extraccion(LocalDate fecha, double monto) {
    super(fecha, monto);
  }
  public double calcularValor(double saldo){
    return saldo - this.getMonto();
  }
  public boolean fueExtraido(LocalDate fecha) {
    return this.esDeLaFecha(fecha);
  }





}

