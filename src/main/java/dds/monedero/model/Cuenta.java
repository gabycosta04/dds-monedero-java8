package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private double saldo = 0;
  private List<Movimiento> movimientos = new ArrayList<>();

  //aca estas haciendo repeticion de logica, ya que antes establecimos que el saldo tenia el valor = 0, por lo tanto no tendria sentido reasignarle el mismo valor
  //TODO CODE SMELL: posible Duplicated Code ya que se repite la logica

  public Cuenta() {
    saldo = 0;
  }
  public Cuenta(double montoInicial) {
    saldo = montoInicial;
  }


  //PROPOSICION
  /*
  public Cuenta(double montoInicial) {
    //al no ingresar nada queda el valor predeterminado que en el caso de los double 0
    saldo = montoInicial;
  }
  */

  //GETTERS Y SETTERS, habria que analizar si es ncesario ver todos, ya que si no tienen funcionalidad para otras clases, no tendria logica declararlos todos

  public void setMovimientos(List<Movimiento> movimientos) {
    this.movimientos = movimientos;
  }

  public List<Movimiento> getMovimientos() {
    return movimientos;
  }

  public double getSaldo() {
    return saldo;
  }

  public void setSaldo(double saldo) {
    this.saldo = saldo;
  }


  private void validarMontoPositivo(double cuanto) {
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }
  }


  //otros dos posibles code smells sean estos dos que analiza diferentes condiciones y que luego ejecuta una excecpion diferente por cada uno
  //TODO CODE SMELL: Duplicated code en poner y sacar ya que hacen cosas de codigo muy parecidas
  public void poner(double cuanto) {

    //ADEMAS ESTA EXCEPCION LA REPITE EN LOS METODOS DE PONER Y SACAR
    //TODO CODE SMELL: ??
    //PROPOSICION:
    validarMontoPositivo(cuanto);


    if (getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count() >= 3) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }
    //otro code smell, ya que esta generando acoplamiento de mas entre las clases, ya que no deberia encargarse el movimiento de agregarse a la lista, sino que deberia agregarlo directamente la cuenta
    //TODO CODE SMELL: ?????
    //new Movimiento(LocalDate.now(), cuanto, true).agregateA(this);
    //PROPOSICION:
    this.agregarMovimiento(LocalDate.now(), cuanto, true);
  }


  public void sacar(double cuanto) {


    validarMontoPositivo(cuanto);

    if (getSaldo() - cuanto < 0) {
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }
    double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
    double limite = 1000 - montoExtraidoHoy;
    if (cuanto > limite) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000
          + " diarios, límite: " + limite);
    }
    this.agregarMovimiento(LocalDate.now(), cuanto, false);
  }






  public void agregarMovimiento(LocalDate fecha, double cuanto, boolean esDeposito) {
    Movimiento movimiento = new Movimiento(fecha, cuanto, esDeposito);
    movimientos.add(movimiento);
  }


  public double getMontoExtraidoA(LocalDate fecha) {
    //TODO CODE SMELL: FEATURE ENVY porque aca se le estan enviando demasiados mensajes a la clase de movimiento, esta funcion deberia resolverla en la clase de movmiento
    return getMovimientos().stream()
        .filter(movimiento -> !movimiento.isDeposito() && movimiento.getFecha().equals(fecha))
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }


}


