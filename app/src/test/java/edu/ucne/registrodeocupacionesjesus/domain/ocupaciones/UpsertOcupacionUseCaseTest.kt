package edu.ucne.registrodeocupacionesjesus.domain.ocupaciones
import edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.model.Ocupacion
import edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.repository.OcupacionRepository
import edu.ucne.registrodeocupacionesjesus.domain.ocupaciones.usecase.UpsertOcupacionUseCase
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class UpsertOcupacionUseCaseTest {

    private lateinit var useCase: UpsertOcupacionUseCase
    private lateinit var repository: OcupacionRepository

    @Before
    fun setup() {
        repository = mockk()
        useCase = UpsertOcupacionUseCase(repository)
    }

    @Test
    fun invoke_guardaOcupacionConDatosValidos() = runTest {
        val ocupacion = Ocupacion(ocupacionId = 0, descripcion = "Desarrollador", sueldo = 50000.0)

        coEvery { repository.observeOcupaciones() } returns flowOf(emptyList())
        coEvery { repository.upsert(ocupacion) } returns 1

        val result = useCase(ocupacion)

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull())
        coVerify(exactly = 1) { repository.observeOcupaciones() }
        coVerify(exactly = 1) { repository.upsert(ocupacion) }
    }

    @Test
    fun invoke_fallaConDescripcionVacia() = runTest {
        val ocupacion = Ocupacion(ocupacionId = 0, descripcion = "   ", sueldo = 50000.0)
        coEvery { repository.observeOcupaciones() } returns flowOf(emptyList())

        val result = useCase(ocupacion)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
        assertEquals("La descripcion no puede estar vacia", result.exceptionOrNull()?.message)
        coVerify(exactly = 0) { repository.upsert(any()) }
    }

    @Test
    fun invoke_fallaConDescripcionMenorA3Caracteres() = runTest {
        val ocupacion = Ocupacion(ocupacionId = 0, descripcion = "QA", sueldo = 50000.0)
        coEvery { repository.observeOcupaciones() } returns flowOf(emptyList())

        val result = useCase(ocupacion)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
        assertEquals("La descripcion debe tener al menos 3 caracteres", result.exceptionOrNull()?.message)
    }

    @Test
    fun invoke_fallaSiLaDescripcionYaExiste() = runTest {
        val ocupacionExistente = Ocupacion(ocupacionId = 0, descripcion = "Ingeniero", sueldo = 40000.0)

        val nuevaOcupacion = Ocupacion(ocupacionId = 1, descripcion = "ingeniero", sueldo = 60000.0)

        coEvery { repository.observeOcupaciones() } returns flowOf(listOf(ocupacionExistente))

        val result = useCase(nuevaOcupacion)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
        assertEquals("Ya existe una ocupacion con esa descripcion", result.exceptionOrNull()?.message)
    }

    @Test
    fun invoke_fallaConSueldoInvalidoMenorOIgualACero() = runTest {
        val ocupacion = Ocupacion(ocupacionId = 0, descripcion = "Desarrollador", sueldo = 0.0)
        coEvery { repository.observeOcupaciones() } returns flowOf(emptyList())

        val result = useCase(ocupacion)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
        assertEquals("El sueldo debe ser mayor que 0", result.exceptionOrNull()?.message)
        coVerify(exactly = 0) { repository.upsert(any()) }
    }
}