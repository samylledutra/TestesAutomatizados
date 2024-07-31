import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


public class FuncionarioTest {

    // Testa o cálculo do pagamento de um funcionário com horas trabalhadas e valor da hora válidos
    @Test
    public void testCalcularPagamentoFuncionarioValorHoraPermitido() {
        double salarioMinimo = 1320.00;
        Funcionario funcionario = new Funcionario("Jose", 40, salarioMinimo * 0.05);
        assertEquals(2640.00, funcionario.calcularPagamento(), 0.01);
    }


    // Testa o cálculo do pagamento de um funcionário com valor da hora abaixo do mínimo permitido
    @Test
    public void testCalcularPagamentoFuncionarioValorHoraAbaixoPermitido() {
        double salarioMinimo = 1320.00;
        Funcionario funcionario = new Funcionario("Mary", 40, salarioMinimo * 0.03);
        assertEquals(salarioMinimo * 0.04 * 40, funcionario.calcularPagamento(), 0.01);
    }

    // Testa o cálculo do pagamento de um funcionário com valor da hora acima do máximo permitido
    @Test
    public void testCalcularPagamentoFuncionarioValorHoraAcimaPermitido() {
        double salarioMinimo = 1320.00;
        Funcionario funcionario = new Funcionario("Carlos", 40, salarioMinimo * 0.12);
        assertEquals(salarioMinimo * 0.10 * 40, funcionario.calcularPagamento(), 0.01);
    }

    // Testa o cálculo do pagamento de um funcionário com horas trabalhadas acima do máximo permitido
    @Test
    public void testHorasTrabalhadasAcimaPermitido() {
        double salarioMinimo = 1320.00;
        Funcionario funcionario = new Funcionario("Julia", 50, salarioMinimo * 0.05);
        assertEquals(salarioMinimo * 0.05 * 40, funcionario.calcularPagamento(), 0.01);
    }

    // Testa o cálculo do pagamento de um funcionário terceirizado com despesas adicionais válidas
    @Test
    public void testCalcularPagamentoFuncionarioTerceirizado() {
        double salarioMinimo = 1320.00;
        double despesasAdicionais = 500.00;
        FuncionarioTerceirizado funcionarioTerceirizado = new FuncionarioTerceirizado("Jonas", 40, salarioMinimo * 0.08, despesasAdicionais);
        assertEquals((salarioMinimo * 0.08 * 40) + (despesasAdicionais * 1.1), funcionarioTerceirizado.calcularPagamento(), 0.01);
    }

    // Testa o cálculo do pagamento de um funcionário terceirizado com despesas adicionais acima do limite
    @Test
    public void testDespesasAdicionaisAcimaDoLimite() {
        double salarioMinimo = 1320.00;
        double despesasAdicionais = 1500.00; // acima do limite
        FuncionarioTerceirizado funcionarioTerceirizado = new FuncionarioTerceirizado("Julia", 40, salarioMinimo * 0.05, despesasAdicionais);
        assertEquals((salarioMinimo * 0.05 * 40) + (1000.00 * 1.1), funcionarioTerceirizado.calcularPagamento(), 0.01);
    }

    // Testa a criação de um funcionário com valor da hora abaixo do mínimo permitido
    @Test
    public void testCriarFuncionarioValorHoraAbaixoPermitido() {
        double salarioMinimo = 1320.00;
        Funcionario funcionario = new Funcionario("Mary", 40, salarioMinimo * 0.03);
        assertEquals(salarioMinimo * 0.04, funcionario.getValorHora(), 0.01);
    }

    // Testa a criação de um funcionário com valor da hora acima do máximo permitido
    @Test
    public void testCriarFuncionarioValorHoraAcimaPermitido() {
        double salarioMinimo = 1320.00;
        Funcionario funcionario = new Funcionario("Pedro", 40, salarioMinimo * 0.12);
        assertEquals(salarioMinimo * 0.10, funcionario.getValorHora(), 0.01);
    }

    // Testa o ajuste do valor da hora no setter
    @Test
    public void testSetValorHoraInvalido() {
        double salarioMinimo = 1320.00;
        Funcionario funcionario = new Funcionario("Jonas", 40, salarioMinimo * 0.05);
        funcionario.setValorHora(salarioMinimo * 0.12);
        assertEquals(salarioMinimo * 0.10, funcionario.getValorHora(), 0.01);
    }

    // Testa a criação de um funcionário terceirizado com despesas adicionais válidas
    @Test
    public void testConstrutorFuncionarioTerceirizadoDespesasValidas() {
        double salarioMinimo = 1320.00;
        double despesasAdicionais = 500.00;
        FuncionarioTerceirizado funcionarioTerceirizado = new FuncionarioTerceirizado("Jona", 40, salarioMinimo * 0.05, despesasAdicionais);
        assertEquals("João", funcionarioTerceirizado.getNome());
        assertEquals(40, funcionarioTerceirizado.getHorasTrabalhadas());
        assertEquals(salarioMinimo * 0.05, funcionarioTerceirizado.getValorHora(), 0.001);
        assertEquals(despesasAdicionais, funcionarioTerceirizado.getDespesasAdicionais(), 0.001);
    }

    // Testa a modificação das despesas adicionais acima do limite
    @Test
    public void testSetDespesasAdicionaisAcimaDoLimite() {
        FuncionarioTerceirizado funcionarioTerceirizado = new FuncionarioTerceirizado("Julia", 40, 50.00, 500.00);
        funcionarioTerceirizado.setDespesasAdicionais(1500.00);
        assertEquals(1000.00, funcionarioTerceirizado.getDespesasAdicionais(), 0.01);
    }

    // Testa o cálculo do pagamento de um funcionário terceirizado com despesas adicionais e ajuste no valor da hora
    @Test
    public void testCalcularPagamentoFuncionarioTerceirizadoDespesasValidas() {
        double salarioMinimo = 1320.00;
        double despesasAdicionais = 200.00;
        FuncionarioTerceirizado funcionarioTerceirizado = new FuncionarioTerceirizado("Carlos", 40, salarioMinimo * 0.07, despesasAdicionais);
        assertEquals((salarioMinimo * 0.07 * 40) + (despesasAdicionais * 1.1), funcionarioTerceirizado.calcularPagamento(), 0.01);
    }

    // Testa a exceção quando o nome do funcionário é vazio
    @Test
    public void testNomeInvalido() {
        String nome = "";
        double valorHora = 30.0;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Funcionario(nome, 30, valorHora);
        });
        assertEquals("Nome inválido", exception.getMessage());
    }

    // Testa a exceção quando o valor da hora é menor que 4% do salário mínimo
    @Test
    public void testValorHoraMenorQueMinimo() {
        double salarioMinimo = 1320.00;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Funcionario("Jonas", 30, salarioMinimo * 0.03);
        });
        assertEquals("Valor da hora inválido", exception.getMessage());
    }

    // Testa a exceção quando o valor da hora é maior que 10% do salário mínimo
    @Test
    public void testValorHoraMaiorQueMaximo() {
        double salarioMinimo = 1320.00;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Funcionario("Jonas", 30, salarioMinimo * 0.11);
        });
        assertEquals("Valor da hora inválido", exception.getMessage());
    }

    // Testa o ajuste do número de horas trabalhadas acima do limite
    @Test
    public void testHorasTrabalhadasAjustadas() {
        Funcionario funcionario = new Funcionario("Jonas", 50, 20.0);
        assertEquals(40, funcionario.getHorasTrabalhadas());
    }
}
