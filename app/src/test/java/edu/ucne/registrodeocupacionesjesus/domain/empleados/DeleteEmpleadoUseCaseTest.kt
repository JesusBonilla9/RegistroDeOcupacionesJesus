package edu.ucne.registrodeocupacionesjesus.domain.empleados
import edu.ucne.registrodeocupacionesjesus.domain.empleados.repository.EmpleadoRepository
import edu.ucne.registrodeocupacionesjesus.domain.empleados.usecase.DeleteEmpleadoUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class DeleteEmpleadoUseCaseTest {

    private lateinit var useCase: DeleteEmpleadoUseCase
    private lateinit var repository: EmpleadoRepository

    @Before
    fun setup() {
        repository = mockk()
        useCase = DeleteEmpleadoUseCase(repository)
    }

    @Test
    fun invoke_llamaAlRepositorioParaEliminarElEmpleadoConElIdProporcionado() = runTest {

        val empleadoId = 5
        coEvery { repository.delete(any()) } just Runs

        useCase(empleadoId)

        coVerify(exactly = 1) { repository.delete(empleadoId) }
    }
}