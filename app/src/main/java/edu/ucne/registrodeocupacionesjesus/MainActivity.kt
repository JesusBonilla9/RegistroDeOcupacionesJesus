package edu.ucne.registrodeocupacionesjesus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import edu.ucne.registrodeocupacionesjesus.ui.theme.RegistroDeOcupacionesJesusTheme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegistroDeOcupacionesJesusTheme {
               RegistroOcupacionesAppUI()
            }
        }
    }
}


