package edu.ucne.registrodeocupacionesjesus.domain.empleados
import edu.ucne.registrodeocupacionesjesus.domain.empleados.model.Empleado
import edu.ucne.registrodeocupacionesjesus.domain.empleados.repository.EmpleadoRepository
import edu.ucne.registrodeocupacionesjesus.domain.empleados.usecase.UpsertEmpleadoUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

@ExperimentalCoroutinesApi
class UpsertEmpleadoUseCaseTest {

    private lateinit var useCase: UpsertEmpleadoUseCase
    private lateinit var repository: EmpleadoRepository

    @Before
    fun setup() {
        repository = mockk()
        useCase = UpsertEmpleadoUseCase(repository)
    }

    @Test
    fun invoke_guardaEmpleadoConDatosValidos() = runTest {
        val empleado = Empleado(
            empleadoId = 0,
            nombres = "Juan Perez",
            sueldo = 50000.0,
            fechaIngreso = LocalDate.now(),
            sexo = "Masculino"
        )
        coEvery { repository.upsert(empleado) } returns 1

        val result = useCase(empleado)

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull())
        coVerify(exactly = 1) { repository.upsert(empleado) }
    }

    @Test
    fun invoke_fallaConNombresVacios() = runTest {

        val empleado = Empleado(
            empleadoId = 0,
            nombres = "   ",
            sueldo = 50000.0,
            fechaIngreso = LocalDate.now(),
            sexo = "Masculino"
        )

        val result = useCase(empleado)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
        assertEquals("El nombre no puede estar vacio", result.exceptionOrNull()?.message)
        coVerify(exactly = 0) { repository.upsert(any()) }
    }

    @Test
    fun invoke_fallaConNombresMenorA3Caracteres() = runTest {

        val empleado = Empleado(
            empleadoId = 0,
            nombres = "Al",
            sueldo = 50000.0,
            fechaIngreso = LocalDate.now(),
            sexo = "Masculino"
        )

        val result = useCase(empleado)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
        assertEquals("El nombre debe tener al menos 3 letras", result.exceptionOrNull()?.message)
        coVerify(exactly = 0) { repository.upsert(any()) }
    }

    @Test
    fun invoke_fallaConNombresQueContienenNumerosOSimbolos() = runTest {

        val empleado = Empleado(
            empleadoId = 0,
            nombres = "Juan123",
            sueldo = 50000.0,
            fechaIngreso = LocalDate.now(),
            sexo = "Masculino"
        )

        val result = useCase(empleado)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
        assertEquals("El nombre solo debe contener letras", result.exceptionOrNull()?.message)
        coVerify(exactly = 0) { repository.upsert(any()) }
    }

    @Test
    fun invoke_fallaConSueldoInvalidoMenorOIgualACero() = runTest {

        val empleado = Empleado(
            empleadoId = 0,
            nombres = "Juan Perez",
            sueldo = 0.0,
            fechaIngreso = LocalDate.now(),
            sexo = "Masculino"
        )

        val result = useCase(empleado)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
        assertEquals("El sueldo debe ser mayor que 0", result.exceptionOrNull()?.message)
        coVerify(exactly = 0) { repository.upsert(any()) }
    }

    @Test
    fun invoke_fallaConFechaDeIngresoFutura() = runTest {

        val fechaFutura = LocalDate.now().plusDays(1)
        val empleado = Empleado(
            empleadoId = 0,
            nombres = "Juan Perez",
            sueldo = 50000.0,
            fechaIngreso = fechaFutura,
            sexo = "Masculino"
        )

        val result = useCase(empleado)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
        assertEquals("La fecha de ingreso no puede ser futura", result.exceptionOrNull()?.message)
        coVerify(exactly = 0) { repository.upsert(any()) }
    }

    @Test
    fun invoke_fallaConSexoVacio() = runTest {

        val empleado = Empleado(
            empleadoId = 0,
            nombres = "Juan Perez",
            sueldo = 50000.0,
            fechaIngreso = LocalDate.now(),
            sexo = "   "
        )

        val result = useCase(empleado)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
        assertEquals("Debe seleccionar el sexo del empleado", result.exceptionOrNull()?.message)
        coVerify(exactly = 0) { repository.upsert(any()) }
    }
}