package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

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
  //los setters de movimientos y saldos no son necesarios ya que no los vamos a utilizar fuera

  public List<Movimiento> getMovimientos() {
    return movimientos;
  }

  public double getSaldo() {
    return saldo;
  }




  //CODE SMELL: Duplicated code en poner y sacar ya que hacen cosas de codigo muy parecidas
  //otros dos posibles code smells sean estos dos que analiza diferentes condiciones y que luego ejecuta una excecpion diferente por cada uno
  //proposicion
  private void validarMontoPositivo(double cuanto) {
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }
  }
  //otro code smell, ya que la funciones de agregateA(Cuenta cuenta) en monedero esta generando acoplamiento de mas entre las clases, ya que no deberia encargarse el movimiento de agregarse a la lista, sino que deberia agregarlo directamente la cuenta
  //CODE SMELL: MISPLACED METHOD
  //PROPOSICION:
  public void agregarMovimiento(LocalDate fecha, double cuanto, boolean esDeposito) {
    Movimiento movimiento = new Movimiento(fecha, cuanto, esDeposito);
    movimientos.add(movimiento);
  }

  public boolean masMovimientosQueLimiteEstablecido(Stream<Movimiento> movimientos, int limite){
    return movimientos.count() >= limite;
  }




  public void poner(double cuanto) {

    validarMontoPositivo(cuanto);

    //LE ESTAMOS DANDO MUCHA RESPONSABILIDAD A LA CLASE NUESTRA PARA CALCULAR LA CANTIDAD DE MOVIMIENTOS SI ES UN DEPOSITO,
    //CODE SMELL: FEATURE ENVY
    //PROPOSICION:
    if (this.masMovimientosQueLimiteEstablecido(movimientos.stream().filter(movimiento -> movimiento.isDeposito()),3)) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }


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



  public double getMontoExtraidoA(LocalDate fecha) {
    //TODO CODE SMELL: FEATURE ENVY porque aca se le estan enviando demasiados mensajes a la clase de movimiento, esta funcion deberia resolverla en la clase de movmiento
    return movimientos.stream()
        .filter(movimiento -> !movimiento.isDeposito() && movimiento.getFecha().equals(fecha))
        .mapToDouble(movimiento -> movimiento.getMonto())
        .sum();
  }


}


