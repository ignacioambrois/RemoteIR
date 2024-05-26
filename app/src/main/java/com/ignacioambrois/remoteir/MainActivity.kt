package com.ignacioambrois.remoteir

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ignacioambrois.remoteir.ui.theme.RemoteIRTheme

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*
        enableEdgeToEdge()
        setContent {
            RemoteIRTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                            name = "Android",
                            modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
        */
        // Start Ktor server
        embeddedServer(Netty, port = 8080) {
            install(Authentication) {
                basic("auth") {
                    realm = "Ktor Server"
                    validate { credentials ->
                        if (credentials.name == "user" && credentials.password == "password") {
                            UserIdPrincipal(credentials.name)
                        } else {
                            null
                        }
                    }
                }
            }
            routing {
                authenticate("auth") {
                    get("/") {
                        call.respondText("Hello, ${call.principal<UserIdPrincipal>()?.name}")
                    }
                }
            }
        }.start(wait = false)

        //Log ipv4 address to console
        println("Server running at http:// ${Utils.getIPAddress(true)}:8080/")
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
            text = "Hello $name!",
            modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RemoteIRTheme {
        Greeting("Android")
    }
}