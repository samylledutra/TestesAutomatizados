package testes;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.Calculadora;


public class CalculadoraTest {
	private static Calculadora calc;
	
	@BeforeAll
    static void inicializarTeste(){
        System.out.println("Começou o teste!!!!!!!!");
        calc = new Calculadora();
    }

    @BeforeEach
    void instanciarObjetos(){
        System.out.println("Inicializando caso de teste");        
    }
    
	
	@Test
	public void testConstrutorSemParametro() throws Exception{
		
		calc = new Calculadora();
		assertEquals(0, calc.getMemoria());
		
	}

	@Test
	public void testContrutorComParametro() throws Exception{

		assertEquals(3, calc.getMemoria()); 

	}

	
	@Test
	public void testConstrutorSomarNegativo() throws Exception{
		
		calc.somar(-5);
		assertEquals(-2, calc.getMemoria());
	}

	@Test
	public void testSubtrairPositivo() throws Exception{
		
		calc.subtrair(3);
		assertEquals(0, calc.getMemoria());
	}


	@Test
	public void testConstrutorMultiplicarPositivo() {
		calc.multiplicar(5);
		assertEquals(2, calc.getMemoria());
	}

	@Test
	public void testDividirPositivo() throws Exception {
	
		calc.dividir(3);
		Assertions.assertEquals(1, calc.getMemoria());
	}


@Test
public void testDividirPorZero() {
    Assertions.assertThrows(Exception.class, () -> calc.dividir(0));
}


	
	@Test
	public void testExponenciarPor1() throws Exception {
	
		calc.exponenciar(1);
		assertEquals(3, calc.getMemoria());
	}

	
	@Test
	public void testExponenciarPor10() throws Exception {
	
		calc.exponenciar(10);
		assertEquals(59049, calc.getMemoria());
	}

	@Test
	public void testZerarMemoria() throws Exception{
		
		calc.zerarMemoria();
		assertEquals(0, calc.getMemoria());
	}


	@AfterEach
    void finalizarCadaMetodoTeste(){
        System.out.println("Finalizando caso de teste");        
    }

    @AfterAll
    static void finalizarTeste(){
        System.out.println("Fim do teste!!!!!");
    }



}




