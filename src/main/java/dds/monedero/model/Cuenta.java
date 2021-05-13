package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private double saldo;
  private List<Movimiento> movimientos = new ArrayList<>();



  //CODE SMELL: posible Duplicated Code ya que se repite la logica
  //Aca estas haciendo repeticion de logica, ya que antes establecimos que el saldo tenia el valor = 0, por lo tanto no tendria sentido reasignarle el mismo valor
  //PROPOSICION

  public Cuenta(double montoInicial ) {
    saldo = montoInicial;
  }






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



  //CODE SMELL: Duplicated code en poner y sacar ya que hacen cosas de codigo muy parecidas
  //otros dos posibles code smells sean estos dos que analiza diferentes condiciones y que luego ejecuta una excecpion diferente por cada uno
  //proposicion
  private void validarMontoPositivo(double cuanto) {
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }
  }

















  public void poner(double cuanto) {

    validarMontoPositivo(cuanto);

    //LE ESTAMOS DANDO MUCHA RESPONSABILIDAD A LA CLASE NUESTRA PARA CALCULAR LA CANTIDAD DE MOVIMIENTOS SI ES UN DEPOSITO,
    //TODO CODE SMELL: MISPLACED METHOD
    if (getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count() >= 3) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }



    //otro code smell, ya que esta generando acoplamiento de mas entre las clases, ya que no deberia encargarse el movimiento de agregarse a la lista, sino que deberia agregarlo directamente la cuenta
    //TODO CODE SMELL: ?????
    //new Movimiento(LocalDate.now(), cuanto, true).agregateA(this);
    //PROPOSICION:
    this.agregarMovimiento(LocalDate.now(), cuanto, true);
    this.saldo += cuanto;
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
    this.saldo -= cuanto;
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


