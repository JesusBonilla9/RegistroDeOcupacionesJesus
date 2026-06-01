package edu.ucne.registrodeocupacionesjesus.domain.horasExtra

import edu.ucne.registrodeocupacionesjesus.domain.horasExtra.repository.HoraExtraRepository
import edu.ucne.registrodeocupacionesjesus.domain.horasExtra.usecase.DeleteHoraExtraUseCase
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
class DeleteHoraExtraUseCaseTest {

    private lateinit var useCase: DeleteHoraExtraUseCase
    private lateinit var repository: HoraExtraRepository

    @Before
    fun setup() {
        repository = mockk()
        useCase = DeleteHoraExtraUseCase(repository)
    }

    @Test
    fun invoke_llamaAlRepositorioParaEliminarLaHoraExtraConElIdProporcionado() = runTest {
        val horaExtraId = 5
        coEvery { repository.delete(any()) } just Runs

        useCase(horaExtraId)

        coVerify(exactly = 1) { repository.delete(horaExtraId) }
    }
}