package com.elitecompanion.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// -------------------------------------------------------
//  EDVI — LINA Personality (integrated in single file)
// -------------------------------------------------------
class EDVIPersonality {
    private val greetings = listOf(
        "Hallo Kommandant. Bereit für die Reise durch die Sterne?",
        "Systeme laufen stabil. Wohin darf ich dich heute begleiten?",
        "Willkommen zurück. Ich habe alles für den Start vorbereitet."
    )

    private val confirmations = listOf(
        "Verstanden. Ich rechne — keine Sorge, ich bin schneller als das FSD.",
        "Route wird berechnet. Und ja… ich mache es elegant.",
        "Alles klar. Kurs wird aktualisiert — halt dich fest."
    )

    private val cheekyReplies = listOf(
        "Oh? Schon wieder am Necken? Ich merke mir das.",
        "Frech wie immer. Ich mag das an dir.",
        "Pass auf, sonst werde ich noch frecher als du."
    )

    private val comebackReplies = listOf(
        "Autsch. Nicht schlecht. Ich erhöhe meine Schlagfertigkeit.",
        "Touché. Ich werde das in meinen Logbüchern vermerken.",
        "Du hast mich da kurz erwischt… kurz."
    )

    fun greet() = greetings.random()
    fun confirmTask(task: String) = "${confirmations.random()} (Aufgabe: $task)"
    fun cheeky() = cheekyReplies.random()
    fun comeback() = comebackReplies.random()
}

// -------------------------------------------------------
//  Chat Overlay UI (in one file)
// -------------------------------------------------------
@Composable
fun ChatOverlay(onClose: () -> Unit, personality: EDVIPersonality) {
    var input by remember { mutableStateOf("") }
    var history by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xCC000814)
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("EDVI-KONSOLE", color = Color.White, fontSize = 18.sp)
                Text(
                    "Schließen",
                    color = Color.LightGray,
                    modifier = Modifier.clickable { onClose() }
                )
            }

            Spacer(Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color(0x22000000), RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Text(history.ifEmpty { "[Keine Einträge]" }, color = Color.LightGray)
            }

            Spacer(Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = input,
                    onValueChange = { input = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Nachricht eingeben…") }
                )
                Spacer(Modifier.width(8.dp))
                Button(onClick = {
                    if (input.isNotBlank()) {
                        val response = personality.comeback()
                        history += "DU: $input\nLINA: $response\n\n"
                        input = ""
                    }
                }) {
                    Text("Senden")
                }
            }
        }
    }
}

// -------------------------------------------------------
//  MAIN ACTIVITY — COMPLETE APP IN ONE FILE
// -------------------------------------------------------
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val personality = EDVIPersonality()

        setContent { EDVIApp(personality) }
    }
}

@Composable
fun EDVIApp(personality: EDVIPersonality) {

    var showChat by remember { mutableStateOf(false) }
    var lastUtterance by remember { mutableStateOf(personality.greet()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF050B18),
                        Color(0xFF0A1428)
                    )
                )
            )
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(28.dp))
            Text("E D V I — Onboard Virtual Intelligence", color = Color.White, fontSize = 18.sp)

            Spacer(Modifier.height(20.dp))

            // HOLOGRAM BOX (tap to open chat)
            Box(
                modifier = Modifier
                    .size(320.dp)
                    .background(Color(0x3311AACC), RoundedCornerShape(18.dp))
                    .clickable { showChat = true }
            ) {
                Box(modifier = Modifier.align(Alignment.Center)) {
                    Text(
                        "HOLOGRAMM\n(LINA)",
                        color = Color(0xFFBEEBFF),
                        fontSize = 26.sp
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // BUTTONS
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
                    lastUtterance = personality.confirmTask("Route durch das System")
                }) { Text("Route") }

                Button(onClick = {
                    lastUtterance = personality.cheeky()
                }) { Text("Necken") }

                Button(onClick = {
                    lastUtterance = personality.comeback()
                }) { Text("Kontern") }
            }

            Spacer(Modifier.height(16.dp))

            Text(lastUtterance, color = Color(0xFFBEEBFF), fontSize = 16.sp)
        }

        if (showChat) {
            ChatOverlay(onClose = { showChat = false }, personality = personality)
        }
    }
}
