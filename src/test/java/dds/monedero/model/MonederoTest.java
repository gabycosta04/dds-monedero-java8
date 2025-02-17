package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MonederoTest {
  private Cuenta cuenta;

  @BeforeEach
  void init() {
    //convendria que en una cuenta en vez de no ingresar un monto, que en caso de que se quiera comenzar se arranque con 0
    cuenta = new Cuenta(0);
  }



  @Test
  void Poner() {
    cuenta.poner(1500);
    assertEquals(cuenta.getSaldo(),1500);
  }

  @Test
  void PonerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.poner(-1500));
  }

  @Test
  void TresDepositosEstanPermitidos() {
    cuenta.poner(1500);
    cuenta.poner(456);
    cuenta.poner(1900);
    assertEquals(cuenta.getSaldo(),1500+456+1900);
  }

  @Test
  void MasDeTresDepositos() {
    assertThrows(MaximaCantidadDepositosException.class, () -> {
          cuenta.poner(1500);
          cuenta.poner(456);
          cuenta.poner(1900);
          cuenta.poner(245);
    });
  }





  @Test
  void ExtraerMasQueElSaldo() {
    assertThrows(SaldoMenorException.class, () -> {
          //aca decidimos que lo indicado no era setear ya que sino no respetariamos lo del encapsulamiento,
          // ademas para agregar saldo, lo indicado seria agregarselo en el constructor o al realizar un deposito
          Cuenta nueva = nuevaCuenta(90);
          nueva.sacar(100);
    });
  }

  @Test
  public void ExtraerMasDe1000() {
    assertThrows(MaximoExtraccionDiarioException.class, () -> {
      Cuenta nueva = nuevaCuenta(5000);
      nueva.sacar(1001);
    });
  }

  @Test
  public void ExtraerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.sacar(-500));
  }



  //TEST AGREGADOS-------------------------------------------------------------------
  @Test
  public void SePuedeExtraer1000SiNoSeRetiroNadaAntes() {
      Cuenta nueva= nuevaCuenta(1500);
      nueva.sacar(1000);
      assertEquals(nueva.getSaldo(),500);
  }


  @Test
  public void ExtraerMasDe1000yExtraerMasQueElSaldo() {
    //como las excepciones van en orden, primero deberia tirar la del sueldo antes que la de extraccionLimite
    assertThrows(SaldoMenorException.class, () -> {
      Cuenta nueva= nuevaCuenta(100);
      nueva.sacar(1001);
    });
  }
  @Test
  public void RealizarDepositoNegativoYaHabiendoRealizado3Depositos() {
    //aca al igual que el test anterior, va a analizar la primera excepcion del monto negativo antes que la de la cantidad de depositos realizados
    assertThrows(MontoNegativoException.class, () -> {
      cuenta.poner(1500);
      cuenta.poner(1000);
      cuenta.poner(500);
      cuenta.poner(-500);
    });
  }
  @Test
  public void Realizar5depositosYElUltimoQueSeaNegativo() {
    //a diferencia de los anteriores aca primero va a saltar la excepcion de el 4to poner primero,x lo tanto
    // a pesar de que en el ultimo poner se genere una excepcion tambien, este no se tendra en cuenta
    // debido a la anterior excepcion que para el flujo
    assertThrows(MaximaCantidadDepositosException.class, () -> {
      cuenta.poner(1500);
      cuenta.poner(1000);
      cuenta.poner(500);
      cuenta.poner(500);
      cuenta.poner(-500);
    });
  }





  //permite crear nuestras propias cuentas, sin tener que andar seteando el valor de la cuenta creada en el inicio
  public Cuenta nuevaCuenta(int montoInicial){
    return new Cuenta(montoInicial);
  }

}