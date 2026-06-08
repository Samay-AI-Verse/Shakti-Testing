package com.example.ui

import android.os.Bundle
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// -------------------------------------------------------------
// Design Palette: "Immersive UI" Theme
// -------------------------------------------------------------
object ShaktiTheme {
    val DeepObsidian = Color(0xFF0F1115) // Deep Slate Black background
    val CardObsidian = Color(0xFF1C1F26) // Sleek floating cards
    val AmberGold = Color(0xFF818CF8)    // Indigo-400 as the prominent interactive highlight
    val SaffronRose = Color(0xFFA78BFA)  // Violet-400 as the highlight complement
    val TextMain = Color(0xFFE2E2E6)     // High-readability crisp light text
    val TextMuted = Color(0xFF94A3B8)    // Slate-400 muted descriptions
    val BorderDark = Color(0xFF2E323D)   // Subtle slate border outline
    val SafetyRed = Color(0xFFF87171)    // Red-400
    val SafeGreen = Color(0xFF4ADE80)    // Green-400
    val InfoBlue = Color(0xFF60A5FA)     // Blue-400

    val GradientBrand = Brush.linearGradient(
        colors = listOf(Color(0xFF4F46E5), Color(0xFFA78BFA)) // Indigo-600 to Violet-400 shadow-gradient
    )
    val GradientBackground = Brush.verticalGradient(
        colors = listOf(DeepObsidian, Color(0xFF16181D))     // Dynamic visual fade to deep-grey
    )
}

// Model Classes for Demos
data class ComparativeRow(
    val option: String,
    val latency: String,
    val quality: String,
    val cost: String,
    val recommended: Boolean = false
)

data class NotificationItem(
    val id: String,
    val app: String,
    val sender: String,
    val content: String,
    val timestamp: String,
    val urgency: String // "Urgent", "Normal", "Spam"
)

data class LiveLog(
    val timestamp: String,
    val category: String, // "SOS", "INTELLIGENCE", "DEVICE", "AGENT"
    val text: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShaktiDashboardScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()
    
    // Safety SOS states
    var isSosTriggered by remember { mutableStateOf(false) }
    var sosCountdown by remember { mutableStateOf(5) }
    val safetyLogs = remember { mutableStateListOf<LiveLog>() }
    var isRecordingEnvironment by remember { mutableStateOf(false) }
    
    // Fake call scheduling
    var fakeCallScheduled by remember { mutableStateOf(false) }
    var fakeCallCountdown by remember { mutableStateOf(0) }
    var runningFakeCall by remember { mutableStateOf(false) }
    var activeFakeCallContact by remember { mutableStateOf("Dad (Emergency Coordinator)") }
    var callAccepted by remember { mutableStateOf(false) }
    var callTimerSec by remember { mutableStateOf(0) }

    // Operator and unknown caller screener states
    var screenerActive by remember { mutableStateOf(false) }
    var screenerStep by remember { mutableStateOf(0) }
    val screenerScripts = listOf(
        "Incoming Call from +1-800-459-2911 (Unknown - Spam Indicator 48%)... Initiating Shakti Screening System.",
        "[Shakti AI Operator]: Greetings. I am Shakti, an autonomous voice assistant answering on behalf of Samay. Please state your name and precise reason for calling.",
        "[Spam Caller]: Hi there, I am calling from Premium Health Life regarding a special health insurance discount package waiting for you...",
        "[Shakti AI Operator]: Thank you. Based on your prompt, our algorithms classify this interaction as non-essential solicitor solicitation. I am ending this call. Have a good day.",
        "System: Call successfully cataloged, recorded, and categorized: 'Spam, Health Robocall'. Rejection index: 100%."
    )

    // Log initializer
    LaunchedEffect(Unit) {
        safetyLogs.add(LiveLog("14:46:50", "AGENT", "Shakti Always-Listening Core initialized in low-power Background Service."))
        safetyLogs.add(LiveLog("14:46:52", "AGENT", "Accessibility Nodes cached: 212 layouts mapped across active system viewport."))
        safetyLogs.add(LiveLog("14:46:53", "SOS", "Shake-sensor registered with telemetry filter: Sensitivity 12.5m/s²."))
    }

    // SOS Countdown coroutine
    LaunchedEffect(isSosTriggered) {
        if (isSosTriggered) {
            isRecordingEnvironment = true
            safetyLogs.add(LiveLog("14:47:01", "SOS", "⚠️ EMERGENCY TRIGGER ACTIVATE! Starting continuous audio/video evidence storage."))
            safetyLogs.add(LiveLog("14:47:02", "SOS", "📍 GPS Capture: 19.0760° N, 72.8777° E. Broadcasting live coordinates to family contacts."))
            safetyLogs.add(LiveLog("14:47:03", "SOS", "📡 Uplink check: Dual LTE-WiFi emergency tunnels established."))
            
            sosCountdown = 5
            while (sosCountdown > 0) {
                delay(1000)
                sosCountdown--
            }
            safetyLogs.add(LiveLog("14:47:08", "SOS", "📞 Dialing Emergency Services & coordinating Dispatch Center automatically."))
        } else {
            isRecordingEnvironment = false
        }
    }

    // Fake call Countdown
    LaunchedEffect(fakeCallScheduled) {
        if (fakeCallScheduled && fakeCallCountdown > 0) {
            while (fakeCallCountdown > 0) {
                delay(1000)
                fakeCallCountdown--
            }
            fakeCallScheduled = false
            runningFakeCall = true
            callAccepted = false
            callTimerSec = 0
        }
    }

    // Fake call active timer
    LaunchedEffect(callAccepted) {
        if (callAccepted) {
            while (runningFakeCall && callAccepted) {
                delay(1000)
                callTimerSec++
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(ShaktiTheme.GradientBrand),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "S",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                ),
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Shakti AI",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    lineHeight = 16.sp
                                ),
                                color = ShaktiTheme.TextMain
                            )
                            Text(
                                text = "NEURAL AGENT v2.5",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.5.sp
                                ),
                                color = ShaktiTheme.AmberGold
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ShaktiTheme.DeepObsidian,
                    titleContentColor = ShaktiTheme.TextMain
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF16181D),
                tonalElevation = 8.dp,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)), RoundedCornerShape(24.dp)),
                windowInsets = WindowInsets.navigationBars
            ) {
                val items = listOf(
                    Triple("Blueprint", Icons.Default.Menu, 0),
                    Triple("Sandbox", Icons.Default.PlayArrow, 1),
                    Triple("Safety Suite", Icons.Default.Warning, 2),
                    Triple("Screener", Icons.Default.Notifications, 3),
                    Triple("Scale & Sec", Icons.Default.Settings, 4)
                )
                items.forEach { (label, icon, tabIndex) ->
                    NavigationBarItem(
                        selected = selectedTab == tabIndex,
                        onClick = { selectedTab = tabIndex },
                        icon = { Icon(imageVector = icon, contentDescription = label) },
                        label = { Text(label, style = MaterialTheme.typography.labelMedium) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = ShaktiTheme.DeepObsidian,
                            selectedTextColor = ShaktiTheme.AmberGold,
                            indicatorColor = ShaktiTheme.AmberGold.copy(alpha = 0.2f),
                            unselectedIconColor = ShaktiTheme.TextMuted,
                            unselectedTextColor = ShaktiTheme.TextMuted
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        // Render fake call full-screen overlays first if running
        if (runningFakeCall) {
            FakeCallOverlay(
                callerName = activeFakeCallContact,
                isAccepted = callAccepted,
                durationSeconds = callTimerSec,
                onAccept = { callAccepted = true },
                onDecline = {
                    runningFakeCall = false
                    callAccepted = false
                }
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ShaktiTheme.GradientBackground)
                    .padding(innerPadding)
            ) {
                // If safety alert countdown is active, show the prominent global warning banner
                if (isSosTriggered) {
                    SosAlertBanner(
                        countdown = sosCountdown,
                        onCancel = { isSosTriggered = false }
                    )
                }

                // Main navigation view routing
                Box(modifier = Modifier.weight(1f)) {
                    when (selectedTab) {
                        0 -> BlueprintScreen()
                        1 -> AgentSandboxScreen(safetyLogs)
                        2 -> SafetySuiteScreen(
                            isSosTriggered = isSosTriggered,
                            onTriggerSos = { isSosTriggered = !isSosTriggered },
                            fakeCallScheduled = fakeCallScheduled,
                            fakeCallCountdown = fakeCallCountdown,
                            activeFakeCallContact = activeFakeCallContact,
                            onScheduleFakeCall = { contact, secs ->
                                activeFakeCallContact = contact
                                fakeCallCountdown = secs
                                fakeCallScheduled = true
                                safetyLogs.add(LiveLog("14:47:15", "SOS", "Fake Call Scheduled in $secs secs (Caller: $contact)."))
                            },
                            safetyLogs = safetyLogs
                        )
                        3 -> ScreenerNotifScreen(
                            screenerActive = screenerActive,
                            screenerStep = screenerStep,
                            screenerScripts = screenerScripts,
                            onNextScreenerStep = {
                                if (screenerStep < screenerScripts.size - 1) {
                                    screenerStep++
                                } else {
                                        screenerActive = false
                                        screenerStep = 0
                                    }
                            },
                            onStartScreener = {
                                screenerActive = true
                                screenerStep = 0
                            }
                        )
                        4 -> InfrastructureSecurityScreen()
                    }
                }
            }
        }
    }
}

// =============================================================
// GLOBAL SAFETY HOOKS
// =============================================================
@Composable
fun SosAlertBanner(countdown: Int, onCancel: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = ShaktiTheme.SafetyRed),
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "🚨 SOS DISPATCH ACTIVE",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
                Text(
                    text = if (countdown > 0) "Emergency dispatch and GPS broadcast in $countdown seconds..." else "Emergency services and dispatch centers have been messaged with your threat live coordinates.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
            Button(
                onClick = onCancel,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(4.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text("CANCEL", color = ShaktiTheme.SafetyRed, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// -------------------------------------------------------------
// TAB 1: VISION & ARCHITECTURE BLUEPRINT
// -------------------------------------------------------------
@Composable
fun BlueprintScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Shakti AI Architectural Blueprint",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                color = ShaktiTheme.AmberGold
            )
            Text(
                text = "This document presents the complete production architectural design and engineering layout for launching Shakti AI as an offline-capable, autonomous smartphone operator companion on Android.",
                style = MaterialTheme.typography.bodyMedium,
                color = ShaktiTheme.TextMuted,
                modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
            )
        }

        // Section: Vision Statement
        item {
            BlueprintCard(title = "1. Brand Vision & Scope", icon = Icons.Default.Info) {
                Text(
                    text = "Establishing a natural, fully-integrated conversational operating intelligence. Users communicate naturally with Shakti AI through continuous voice pipelines while the model reasons, synthesizes layout content, constructs action plans, and operates any mobile application autonomously via secure Accessibility automation hooks.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ShaktiTheme.TextMain
                )
            }
        }

        // Section: Comparative Pipeline Tables
        item {
            BlueprintCard(title = "2. Speech-To-Text STT Pipeline Options", icon = Icons.Default.Mic) {
                CompareTable(
                    headers = listOf("Technology", "Avg Latency", "Accuracy", "Unit Cost / Rating"),
                    rows = listOf(
                        ComparativeRow("Gemini Live Integration", "150ms", "98%", "Included in API (Highly Rec.)", recommended = true),
                        ComparativeRow("OpenAI Realtime API", "250ms", "98%", "$0.06 / min (Heavy)"),
                        ComparativeRow("Deepgram Nova-2", "120ms", "97%", "$0.004 / min (STT Only)"),
                        ComparativeRow("Whisper (Local C++ compiled)", "180ms", "95%", "0$ Host (No Network required)")
                    )
                )
                Text(
                    text = "Architectural Recommendation: Deploy Gemini Live for streaming verbal intents. Use On-Device compiled Whisper models as fallback offline buffers to process emergency SOS actions without network.",
                    style = MaterialTheme.typography.bodySmall,
                    color = ShaktiTheme.AmberGold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        item {
            BlueprintCard(title = "3. Text-To-Speech TTS Pipeline Options", icon = Icons.Default.VolumeUp) {
                CompareTable(
                    headers = listOf("TTS Engine", "Server Latency", "Naturalness", "Unit Cost / Rating"),
                    rows = listOf(
                        ComparativeRow("ElevenLabs Multilingual v2", "320ms", "99% (Elite)", "High ($0.15k - $0.30k chars)"),
                        ComparativeRow("Gemini Built-in TTS Voices", "140ms", "95% (High)", "Included in API Framework", recommended = true),
                        ComparativeRow("OpenAI TTS HD Engine", "240ms", "96% (High)", "$15.00 / million chars"),
                        ComparativeRow("Azure Cognitive Neural TTS", "210ms", "93% (Good)", "$16.00 / million chars")
                    )
                )
                Text(
                    text = "Synthesis Verdict: For continuous dialog interfaces, Gemini's integrated TTS ensures low latency without high third-party routing expenses, supporting custom speech rates.",
                    style = MaterialTheme.typography.bodySmall,
                    color = ShaktiTheme.AmberGold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        // Section: AI Multi-Agent Choreography Schema
        item {
            BlueprintCard(title = "4. Modular Multi-Agent Orchestration Schema", icon = Icons.Default.Layers) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    AgentVisualBubble("1. Core Intent Parser", "Gemini 2.5 Flash models identify system intents, resolving calling, messaging, routing, notes, and navigation goals with 96% confidence.", ShaktiTheme.AmberGold)
                    AgentArrow()
                    AgentVisualBubble("2. Context Memory Binder", "Retrieves context from local Room caches and vectors stored in the remote cluster (Qdrant). Translates conversational labels (like 'Mother' or 'Presentation') to direct IDs.", ShaktiTheme.SafeGreen)
                    AgentArrow()
                    AgentVisualBubble("3. Task planner agent", "Gemini 2.5 Pro deconstructs complex actions (e.g. 'whatsapp presentation') to complete step instructions, compiling error-handling checks.", ShaktiTheme.InfoBlue)
                    AgentArrow()
                    AgentVisualBubble("4. Sandbox Execution Worker", "Android Accessibility Service operates the layout programmatically, mapping screens, emitting clicks, typing inputs, and verifying responses.", ShaktiTheme.SaffronRose)
                }
            }
        }

        // Section: Flutter Structure
        item {
            BlueprintCard(title = "5. Production Flutter App Structure", icon = Icons.Default.Android) {
                Text(
                    text = "Complete Clean Architecture visual organization incorporating Riverpod for state binding, Hive for offline caching, and Go-Router routes:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ShaktiTheme.TextMuted,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                FlutterTreeViewer()
            }
        }

        // Section: Backend Stack
        item {
            BlueprintCard(title = "6. Scalable Backend Integration Engine", icon = Icons.Default.Dns) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    BackendComponentRow("FastAPI Gateway", "Serving ASGI asynchronous webhooks with <5ms network routing times.")
                    BackendComponentRow("Qdrant Vector DB", "Storing deep semantic user memory fragments using secure embedding indices.")
                    BackendComponentRow("Redis Memory Store", "Caching active agent orchestration configurations and temporary telemetry coordinates.")
                    BackendComponentRow("Kubernetes Clustered Nodes", "Orchestrating microservices to scale seamlessly with automatic traffic routing.")
                }
            }
        }
    }
}

@Composable
fun BlueprintCard(
    title: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = ShaktiTheme.CardObsidian),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, ShaktiTheme.BorderDark),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = ShaktiTheme.AmberGold,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = ShaktiTheme.TextMain
                )
            }
            content()
        }
    }
}

@Composable
fun CompareTable(headers: List<String>, rows: List<ComparativeRow>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, ShaktiTheme.BorderDark, RoundedCornerShape(8.dp))
            .background(ShaktiTheme.DeepObsidian)
    ) {
        // Headers
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ShaktiTheme.BorderDark.copy(alpha = 0.5f))
                .padding(8.dp)
        ) {
            headers.forEach { h ->
                Text(
                    text = h,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = ShaktiTheme.TextMain,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        // Rows
        rows.forEach { r ->
            val bg = if (r.recommended) ShaktiTheme.AmberGold.copy(alpha = 0.08f) else Color.Transparent
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(bg)
                    .border(0.5.dp, ShaktiTheme.BorderDark.copy(alpha = 0.3f))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = r.option + (if (r.recommended) " ★" else ""),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = if (r.recommended) FontWeight.Bold else FontWeight.Normal
                    ),
                    color = if (r.recommended) ShaktiTheme.AmberGold else ShaktiTheme.TextMain,
                    modifier = Modifier.weight(1f)
                )
                Text(text = r.latency, style = MaterialTheme.typography.bodySmall, color = ShaktiTheme.TextMuted, modifier = Modifier.weight(1f))
                Text(text = r.quality, style = MaterialTheme.typography.bodySmall, color = ShaktiTheme.TextMuted, modifier = Modifier.weight(1f))
                Text(
                    text = r.cost,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = if (r.recommended) ShaktiTheme.AmberGold else ShaktiTheme.TextMain,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun AgentVisualBubble(title: String, desc: String, color: Color) {
    Card(
        colors = CardDefaults.cardColors(containerColor = ShaktiTheme.DeepObsidian),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, color.copy(alpha = 0.6f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = color)
            Spacer(modifier = Modifier.height(4.dp))
            Text(desc, style = MaterialTheme.typography.bodySmall, color = ShaktiTheme.TextMain)
        }
    }
}

@Composable
fun AgentArrow() {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = null,
            tint = ShaktiTheme.TextMuted,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun ColumnScope.BackendComponentRow(title: String, description: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Default.Dns,
            contentDescription = null,
            tint = ShaktiTheme.InfoBlue,
            modifier = Modifier
                .size(16.dp)
                .offset(y = 2.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = ShaktiTheme.TextMain)
            Text(text = description, style = MaterialTheme.typography.bodySmall, color = ShaktiTheme.TextMuted)
        }
    }
}

@Composable
fun FlutterTreeViewer() {
    val tree = listOf(
        "lib/" to "Root Code directory",
        "  ├── main.dart" to "Global Flutter application bootstraps",
        "  ├── core/" to "Cross-cutting safety utilities",
        "  │   ├── services/" to "Accessibility & Background service platform integrations",
        "  │   ├── security/" to "Standard AES encryption and Keystore bindings",
        "  │   └── theme/" to "Material 3 Sovereign themes & typography templates",
        "  └── features/" to "Decoupled domain business logic feature modules",
        "      ├── assistant/" to "Core live voice stream module",
        "      │   ├── data/" to "Gemini local api repositories & cache databases",
        "      │   ├── domain/" to "Assistant intent validation use-cases",
        "      │   └── presentation/" to "Assistant waves and sandbox logs viewports",
        "      ├── safety/" to "SOS triggers, route divergence monitoring & mock call screens",
        "      └── operator/" to "Notification interception registries and incoming call managers"
    )
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black, RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        tree.forEach { (path, desc) ->
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = path,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = if (path.endsWith("/")) ShaktiTheme.AmberGold else ShaktiTheme.TextMain,
                    modifier = Modifier.weight(1.3f)
                )
                Text(
                    text = " -> $desc",
                    style = MaterialTheme.typography.labelSmall,
                    color = ShaktiTheme.TextMuted,
                    modifier = Modifier.weight(1.7f)
                )
            }
        }
    }
}

@Composable
fun AiCoreVisualizer(isSimulating: Boolean, activePrompt: String) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    
    // Pulse animation for outer glowing radius
    val glowScale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowScale"
    )
    
    // Wave heights animations targeting natural speech motion
    val wave1 by infiniteTransition.animateFloat(
        initialValue = if (isSimulating) 12f else 6f,
        targetValue = if (isSimulating) 38f else 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (isSimulating) 280 else 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wave1"
    )
    val wave2 by infiniteTransition.animateFloat(
        initialValue = if (isSimulating) 18f else 8f,
        targetValue = if (isSimulating) 46f else 18f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (isSimulating) 430 else 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wave2"
    )
    val wave3 by infiniteTransition.animateFloat(
        initialValue = if (isSimulating) 10f else 5f,
        targetValue = if (isSimulating) 42f else 14f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (isSimulating) 360 else 1100, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wave3"
    )
    val wave4 by infiniteTransition.animateFloat(
        initialValue = if (isSimulating) 14f else 7f,
        targetValue = if (isSimulating) 34f else 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (isSimulating) 310 else 900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wave4"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Orb layers stack
        Box(
            modifier = Modifier.size(190.dp),
            contentAlignment = Alignment.Center
        ) {
            // Blurred glowing back halo layer
            Box(
                modifier = Modifier
                    .size((130 * glowScale).dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF6366F1).copy(alpha = 0.25f),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
            )

            // Inner styling circle ring
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .border(1.dp, Color(0xFF6366F1).copy(alpha = 0.2f), CircleShape)
            )

            // Dynamic Core Orb from Indigo to Violet
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF818CF8),
                                Color(0xFF4F46E5)
                            )
                        ),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                // High contrast mask
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .background(Color(0xFF0F1115), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    // Sonic voicepeaks visualization bars
                    Row(
                        modifier = Modifier.height(48.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .width(5.dp)
                                .height(wave1.dp)
                                .background(Color(0xFF818CF8), RoundedCornerShape(10.dp))
                        )
                        Box(
                            modifier = Modifier
                                .width(5.dp)
                                .height(wave2.dp)
                                .background(Color(0xFFA78BFA), RoundedCornerShape(10.dp))
                        )
                        Box(
                            modifier = Modifier
                                .width(5.dp)
                                .height(wave3.dp)
                                .background(Color(0xFF818CF8), RoundedCornerShape(10.dp))
                        )
                        Box(
                            modifier = Modifier
                                .width(5.dp)
                                .height(wave4.dp)
                                .background(Color(0xFFA78BFA), RoundedCornerShape(10.dp))
                        )
                    }
                }
            }
        }

        // Translation/Prompt visual area
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = if (isSimulating) "SYSTEM ACTIVE" else "SYSTEM READY",
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            ),
            color = if (isSimulating) Color(0xFFC7D2FE) else Color(0xFF94A3B8)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = if (activePrompt.isNotBlank()) "\"$activePrompt\"" else "\"Shakti, send Rahul the latest presentation from Drive.\"",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Light,
                lineHeight = 22.sp,
                textAlign = TextAlign.Center
            ),
            color = Color.White,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        // Status Indicators Row
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .background(Color(0xFF6366F1).copy(alpha = 0.1f), RoundedCornerShape(50))
                    .border(1.dp, Color(0xFF6366F1).copy(alpha = 0.2f), RoundedCornerShape(50))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(if (isSimulating) Color(0xFF4ADE80) else Color(0xFF94A3B8).copy(alpha = 0.4f), CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isSimulating) "INTENT ENGINE ACTIVE" else "INTENT ENGINE IDLE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp
                    ),
                    color = Color.White
                )
            }

            Row(
                modifier = Modifier
                    .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(50))
                    .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(50))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(Color.White.copy(alpha = 0.4f), CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "VISION IDLE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp
                    ),
                    color = Color.White.copy(alpha = 0.6f)
                )
            }
        }
    }
}

// -------------------------------------------------------------
// TAB 2: MULTI-AGENT SIMULATOR PLAYGROUND
// -------------------------------------------------------------
@Composable
fun AgentSandboxScreen(logs: MutableList<LiveLog>) {
    val scope = rememberCoroutineScope()
    var inputQuery by remember { mutableStateOf("") }
    
    // Core stages of simulated execution
    var activeStage by remember { mutableStateOf(-1) }
    var intentDetails by remember { mutableStateOf<String?>(null) }
    var retrievedMemory by remember { mutableStateOf<String?>(null) }
    var taskPlanList by remember { mutableStateOf<List<String>>(emptyList()) }
    var activePlanStepIndex by remember { mutableStateOf(-1) }
    var executionCodeOutput by remember { mutableStateOf<String?>(null) }
    var isSimulating by remember { mutableStateOf(false) }

    val mockPrompts = listOf(
        "Shakti, call my mom.",
        "Send Dad the latest slide presentation on WhatsApp.",
        "Check notes, then remind me to email presentation tomorrow morning.",
        "Read what's on my screen."
    )

    fun runAgentSimulation(prompt: String) {
        if (isSimulating) return
        scope.launch {
            isSimulating = true
            inputQuery = prompt
            activeStage = 0
            intentDetails = null
            retrievedMemory = null
            taskPlanList = emptyList()
            activePlanStepIndex = -1
            executionCodeOutput = null
            
            logs.add(LiveLog("14:47:20", "AGENT", "Simulation started for prompt: \"$prompt\""))
            delay(1200)

            // Stage 1: Intent Recognition
            activeStage = 1
            val resolvedIntent = when {
                prompt.contains("call", ignoreCase = true) -> "TELEPHONY_DIAL_CALL"
                prompt.contains("WhatsApp", ignoreCase = true) -> "MESSAG_WHATSAPP_SEND"
                prompt.contains("remind", ignoreCase = true) -> "ALARM_SCHED_REMINDER"
                else -> "ACCESSIBILITY_OCR_SCREEN"
            }
            intentDetails = "Intent: $resolvedIntent | Confidence: 99.2% | IntentAgent resolved via Gemini 2.5 Flash."
            logs.add(LiveLog("14:47:21", "AGENT", "IntentAgent: Resolved parsed intent to \"$resolvedIntent\"."))
            delay(1500)

            // Stage 2: Memory Retrieval
            activeStage = 2
            retrievedMemory = when {
                prompt.contains("mom", ignoreCase = true) -> "Memory Search: 'Mom' matched with Emergency Contact ID: +1-555-0199."
                prompt.contains("Dad", ignoreCase = true) -> "Memory Search: 'Dad' matched with Primary Household Contact ID: +1-555-0144. 'presentation' matched with recent file URI: '/sdcard/Downloads/Presentation.pdf'."
                else -> "Memory Search: Standard local context parsed. No custom entity mapping requested."
            }
            logs.add(LiveLog("14:47:23", "AGENT", "MemoryAgent: Parsed bindings retrieved from local caches."))
            delay(1500)

            // Stage 3: Planning Suite
            activeStage = 3
            taskPlanList = when {
                prompt.contains("call", ignoreCase = true) -> listOf(
                    "Initiate device system dial request with target number +1-555-0199.",
                    "Verify audio interface switching to speaker automatically.",
                    "Log active dial event to Local Activity Cache in Room."
                )
                prompt.contains("WhatsApp", ignoreCase = true) -> listOf(
                    "Launch messaging action intent targeting com.whatsapp.",
                    "Parse accessibility screen structure to find matching chat bubble recipient.",
                    "Emulate programmatic tap on file selection window, uploading recent document URI.",
                    "Construct accessibility node dispatch event triggering send button."
                )
                else -> listOf(
                    "Invoke Accessibility OCR payload to scrape current window node text.",
                    "Deliver scraped structured nodes directly to Planning agent.",
                    "Simulate conversational visual layout synthesis to identify action buttons."
                )
            }
            logs.add(LiveLog("14:47:24", "AGENT", "PlanningAgent: 4-step execution graph constructed."))
            
            for (i in taskPlanList.indices) {
                activePlanStepIndex = i
                delay(1200)
            }

            // Stage 4: Robotic Execution
            activeStage = 4
            executionCodeOutput = when {
                prompt.contains("call", ignoreCase = true) -> """
                    // Executing system telephone dial call 
                    val intent = Intent(Intent.ACTION_CALL).apply {
                        data = Uri.parse("tel:+15550199")
                    }
                    startActivity(intent)
                    Log.d("ShaktiExecution", "Call Tunnel Activated.")
                """.trimIndent()
                else -> """
                    // Scraping accessibility tree and clicking target node element 
                    val root = accessibilityService.rootInActiveWindow
                    val sendBtn = root?.findAccessibilityNodeInfosByViewId(
                        "com.whatsapp:id/send_button"
                    )?.firstOrNull()
                    
                    if (sendBtn != null) {
                        sendBtn.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                        Log.d("ShaktiAgent", "Autonomous dispatch success.")
                    }
                """.trimIndent()
            }
            logs.add(LiveLog("14:47:30", "DEVICE", "ExecutionAgent: Device Action dispatched successfully via accessibility framework."))
            
            delay(1500)
            logs.add(LiveLog("14:47:32", "AGENT", "Task Completed successfully in local sandbox."))
            isSimulating = false
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Shakti AI Agent Sandbox",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                color = ShaktiTheme.AmberGold
            )
            Text(
                text = "Interactive emulation platform to monitor intent resolution, contextual database matching, step synthesis, and Accessibility code-dispatching.",
                style = MaterialTheme.typography.bodyMedium,
                color = ShaktiTheme.TextMuted,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
            )
        }

        item {
            AiCoreVisualizer(isSimulating = isSimulating, activePrompt = inputQuery)
        }

        // Section: Prompt Templates
        item {
            Text(
                text = "Select Sample Prompt To Emulate",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = ShaktiTheme.TextMain
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                mockPrompts.forEach { prompt ->
                    Button(
                        onClick = { runAgentSimulation(prompt) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (inputQuery == prompt) ShaktiTheme.AmberGold else ShaktiTheme.CardObsidian,
                            contentColor = if (inputQuery == prompt) Color.Black else ShaktiTheme.TextMain
                        ),
                        border = BorderStroke(1.dp, ShaktiTheme.BorderDark),
                        enabled = !isSimulating
                    ) {
                        Text(prompt, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }

        // Section: Custom Query Inputs
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = inputQuery,
                    onValueChange = { inputQuery = it },
                    placeholder = { Text("Alternatively, type custom query...", color = ShaktiTheme.TextMuted) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = ShaktiTheme.CardObsidian,
                        unfocusedContainerColor = ShaktiTheme.CardObsidian,
                        focusedTextColor = ShaktiTheme.TextMain,
                        unfocusedTextColor = ShaktiTheme.TextMain,
                        focusedIndicatorColor = ShaktiTheme.AmberGold,
                        unfocusedIndicatorColor = ShaktiTheme.BorderDark
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("agent_query_input"),
                    enabled = !isSimulating
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { runAgentSimulation(inputQuery) },
                    colors = ButtonDefaults.buttonColors(containerColor = ShaktiTheme.AmberGold),
                    modifier = Modifier
                        .testTag("run_agent_simulation_button")
                        .align(Alignment.CenterVertically),
                    enabled = !isSimulating && inputQuery.isNotBlank()
                ) {
                    if (isSimulating) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.Black)
                    } else {
                        Text("SIMULATE", fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                }
            }
        }

        // Section: Interactive Agent Pipeline Nodes
        item {
            Text(
                text = "Live Execution Stack",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = ShaktiTheme.TextMain
            )
        }

        // Stage 1 Component
        item {
            StageCard(
                stageNum = "1",
                title = "Intent Agent Parser",
                isActive = activeStage == 1,
                isCompleted = activeStage > 1,
                content = intentDetails
            )
        }

        // Stage 2 Component
        item {
            StageCard(
                stageNum = "2",
                title = "Memory Agent Linker",
                isActive = activeStage == 2,
                isCompleted = activeStage > 2,
                content = retrievedMemory
            )
        }

        // Stage 3 Component
        item {
            StageCard(
                stageNum = "3",
                title = "Planning Agent Graph",
                isActive = activeStage == 3,
                isCompleted = activeStage > 3,
                content = if (taskPlanList.isEmpty()) null else "Planning graph decomposed successfully."
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    taskPlanList.forEachIndexed { idx, step ->
                        val isStepActive = activePlanStepIndex == idx
                        val isStepDone = activePlanStepIndex > idx
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isStepDone) Icons.Default.CheckCircle else if (isStepActive) Icons.Default.PlayArrow else Icons.Default.RadioButtonUnchecked,
                                contentDescription = null,
                                tint = if (isStepDone) ShaktiTheme.SafeGreen else if (isStepActive) ShaktiTheme.AmberGold else ShaktiTheme.TextMuted,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Step ${idx + 1}: $step",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = if (isStepActive) FontWeight.Bold else FontWeight.Normal
                                ),
                                color = if (isStepActive) ShaktiTheme.AmberGold else if (isStepDone) ShaktiTheme.TextMain else ShaktiTheme.TextMuted
                            )
                        }
                    }
                }
            }
        }

        // Stage 4 Component
        item {
            StageCard(
                stageNum = "4",
                title = "Accessibility Execution Service",
                isActive = activeStage == 4,
                isCompleted = activeStage > 4,
                content = if (executionCodeOutput != null) "Synthesizing Android accessibility click nodes:" else null
            ) {
                if (executionCodeOutput != null) {
                    Text(
                        text = executionCodeOutput!!,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        color = ShaktiTheme.SafeGreen,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black, RoundedCornerShape(4.dp))
                            .border(1.dp, ShaktiTheme.BorderDark, RoundedCornerShape(4.dp))
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun StageCard(
    stageNum: String,
    title: String,
    isActive: Boolean,
    isCompleted: Boolean,
    content: String?,
    extraContent: @Composable (ColumnScope.() -> Unit)? = null
) {
    val borderColor = if (isActive) ShaktiTheme.AmberGold else if (isCompleted) ShaktiTheme.SafeGreen else ShaktiTheme.BorderDark
    val iconColor = if (isActive) ShaktiTheme.AmberGold else if (isCompleted) ShaktiTheme.SafeGreen else ShaktiTheme.TextMuted
    val containerColor = if (isActive) ShaktiTheme.CardObsidian else ShaktiTheme.DeepObsidian

    Card(
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, borderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            color = if (isCompleted) ShaktiTheme.SafeGreen else if (isActive) ShaktiTheme.AmberGold else ShaktiTheme.BorderDark,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stageNum,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.Black
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = ShaktiTheme.TextMain
                )
                Spacer(modifier = Modifier.weight(1f))
                if (isCompleted) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Done", tint = ShaktiTheme.SafeGreen, modifier = Modifier.size(18.dp))
                } else if (isActive) {
                    CircularProgressIndicator(modifier = Modifier.size(14.dp), color = ShaktiTheme.AmberGold, strokeWidth = 2.dp)
                }
            }

            if (content != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodySmall,
                    color = ShaktiTheme.TextMain
                )
            }
            if (extraContent != null) {
                extraContent()
            }
        }
    }
}

// -------------------------------------------------------------
// TAB 3: WOMEN SAFETY SOS SUITE & REALISTIC FAKE CALL GENERATOR
// -------------------------------------------------------------
@Composable
fun SafetySuiteScreen(
    isSosTriggered: Boolean,
    onTriggerSos: () -> Unit,
    fakeCallScheduled: Boolean,
    fakeCallCountdown: Int,
    activeFakeCallContact: String,
    onScheduleFakeCall: (String, Int) -> Unit,
    safetyLogs: List<LiveLog>
) {
    var selectedContactName by remember { mutableStateOf("Dad (Emergency Coordinator)") }
    var selectedDelaySecs by remember { mutableStateOf(5) }
    var activeRiskTelemetry by remember { mutableStateOf(false) }

    val contactOptions = listOf(
        "Dad (Emergency Coordinator)",
        "Police Control Desk",
        "Elder Brother (Advocate)",
        "SafeRide Support Center"
    )
    val delayOptions = listOf(5, 10, 20)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Shakti Women Safety Suite",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                color = ShaktiTheme.AmberGold
            )
            Text(
                text = "Named after Shakti (divine strength), this suite couples physical gesture heuristics (shake sensors), automated location broadcasting protocols, live evidence uploading, and protective fake calls.",
                style = MaterialTheme.typography.bodyMedium,
                color = ShaktiTheme.TextMuted,
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
            )
        }

        // Features row: Emergency Trigger SOS & Route Deviation Guard
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Interactive Shake & Dial SOS Button
                Card(
                    colors = CardDefaults.cardColors(containerColor = if (isSosTriggered) ShaktiTheme.SafetyRed.copy(alpha = 0.15f) else ShaktiTheme.CardObsidian),
                    border = BorderStroke(1.dp, if (isSosTriggered) ShaktiTheme.SafetyRed else ShaktiTheme.BorderDark),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onTriggerSos() }
                ) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = if (isSosTriggered) ShaktiTheme.SafetyRed else ShaktiTheme.AmberGold,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (isSosTriggered) "ACTIVE: STOP SOS" else "MOCK SOS TRIGGER",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.ExtraBold),
                            color = if (isSosTriggered) ShaktiTheme.SafetyRed else ShaktiTheme.TextMain,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Simulates shake activation, records sound, and alerts emergency contacts.",
                            style = MaterialTheme.typography.bodySmall,
                            color = ShaktiTheme.TextMuted,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Interactive Route Deviation Simulator Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = if (activeRiskTelemetry) ShaktiTheme.SaffronRose.copy(alpha = 0.12f) else ShaktiTheme.CardObsidian),
                    border = BorderStroke(1.dp, if (activeRiskTelemetry) ShaktiTheme.SaffronRose else ShaktiTheme.BorderDark),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { activeRiskTelemetry = !activeRiskTelemetry }
                ) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Map,
                            contentDescription = null,
                            tint = if (activeRiskTelemetry) ShaktiTheme.SaffronRose else ShaktiTheme.SafeGreen,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (activeRiskTelemetry) "PATH DEVIATED!" else "SAFE ROUTE MONITOR",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.ExtraBold),
                            color = if (activeRiskTelemetry) ShaktiTheme.SaffronRose else ShaktiTheme.TextMain,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Simulates real-time map route tracking and deviation triggers.",
                            style = MaterialTheme.typography.bodySmall,
                            color = ShaktiTheme.TextMuted,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        // Active State of Route Deviation Monitor
        if (activeRiskTelemetry) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = ShaktiTheme.DeepObsidian),
                    border = BorderStroke(1.dp, ShaktiTheme.SaffronRose),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = ShaktiTheme.SaffronRose)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Critical Risk Alert: Confirmed Route Deviation Unresolved",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = ShaktiTheme.SaffronRose
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Shakti AI analyzed your active taxi route coordinates. Telemetry suggests a 45-degree deviation into an unlit route sector. Action Required",
                            style = MaterialTheme.typography.bodySmall,
                            color = ShaktiTheme.TextMain
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { activeRiskTelemetry = false },
                                colors = ButtonDefaults.buttonColors(containerColor = ShaktiTheme.SafeGreen),
                                shape = RoundedCornerShape(4.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("APPROVE ROUTE", color = Color.Black, fontWeight = FontWeight.Bold)
                            }
                            Button(
                                onClick = { onTriggerSos() },
                                colors = ButtonDefaults.buttonColors(containerColor = ShaktiTheme.SafetyRed),
                                shape = RoundedCornerShape(4.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("TRIGGER SOS ALERT", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Defensive Tool Panel: Realistic Fake Call Generator
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = ShaktiTheme.CardObsidian),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, ShaktiTheme.BorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Call, contentDescription = null, tint = ShaktiTheme.AmberGold, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Fake Call Generator (Defensive Safety)",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = ShaktiTheme.TextMain
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Activates a highly realistic incoming phone call screen. Accepting this call streams an auditory script (using Mom or Dad prompts) to confuse harassers or excuse yourself comfortably.",
                        style = MaterialTheme.typography.bodySmall,
                        color = ShaktiTheme.TextMuted
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Selector: Contact Name
                    Text("Select Caller identity:", style = MaterialTheme.typography.labelMedium, color = ShaktiTheme.TextMain)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        contactOptions.forEach { name ->
                            val isSelected = selectedContactName == name
                            Button(
                                onClick = { selectedContactName = name },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSelected) ShaktiTheme.AmberGold else ShaktiTheme.DeepObsidian,
                                    contentColor = if (isSelected) Color.Black else ShaktiTheme.TextMain
                                ),
                                border = BorderStroke(1.dp, ShaktiTheme.BorderDark)
                            ) {
                                Text(name, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    // Selector: Delay
                    Text("Trigger Timer Delay:", style = MaterialTheme.typography.labelMedium, color = ShaktiTheme.TextMain)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        delayOptions.forEach { secs ->
                            val isSelected = selectedDelaySecs == secs
                            Button(
                                onClick = { selectedDelaySecs = secs },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSelected) ShaktiTheme.AmberGold else ShaktiTheme.DeepObsidian,
                                    contentColor = if (isSelected) Color.Black else ShaktiTheme.TextMain
                                ),
                                border = BorderStroke(1.dp, ShaktiTheme.BorderDark),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("$secs seconds", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { onScheduleFakeCall(selectedContactName, selectedDelaySecs) },
                        colors = ButtonDefaults.buttonColors(containerColor = ShaktiTheme.AmberGold),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("schedule_fake_call_button"),
                        enabled = !fakeCallScheduled
                    ) {
                        Text(
                            text = if (fakeCallScheduled) "CALL INITIATION BINDED ($fakeCallCountdown sec)" else "ACTIVATE DEFENSIVE FAKE CALL",
                            fontWeight = FontWeight.Black,
                            color = Color.Black
                        )
                    }
                }
            }
        }

        // Live Log viewer (Focused purely on Women Safety and telemetry)
        item {
            Text(
                "Shakti Telemetry Safety Log Stream",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = ShaktiTheme.TextMain,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.Black),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, ShaktiTheme.BorderDark),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(safetyLogs.reversed()) { log ->
                        val logColor = when (log.category) {
                            "SOS" -> ShaktiTheme.SafetyRed
                            "DEVICE" -> ShaktiTheme.AccentColor
                            "INTELLIGENCE" -> ShaktiTheme.InfoBlue
                            else -> ShaktiTheme.AmberGold
                        }
                        
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "[${log.timestamp}]",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                color = ShaktiTheme.TextMuted
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "[${log.category}]",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                color = logColor,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.width(105.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = log.text,
                                style = MaterialTheme.typography.bodySmall,
                                color = ShaktiTheme.TextMain,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

// Accent Color Placeholder Helper
val ShaktiTheme.AccentColor: Color
    get() = Color(0xFF64FFDA)

// -------------------------------------------------------------
// TAB 3 SUB-VIEW: HIGH-FIDELITY OVERLAY FOR INCOMING PHONE CALL SCREEN
// -------------------------------------------------------------
@Composable
fun FakeCallOverlay(
    callerName: String,
    isAccepted: Boolean,
    durationSeconds: Int,
    onAccept: () -> Unit,
    onDecline: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF070709))
            .clickable(enabled = false) {}, // Intercept touch events
        contentAlignment = Alignment.Center
    ) {
        if (!isAccepted) {
            // Incoming Screen Layout
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Caller Header
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 64.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color(0xFF1E1E24), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(54.dp))
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = callerName,
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Incoming Safety Dial...",
                        style = MaterialTheme.typography.bodyLarge,
                        color = ShaktiTheme.AmberGold
                    )
                }

                // Answer / Decline Options
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 64.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Decline
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        IconButton(
                            onClick = onDecline,
                            modifier = Modifier
                                .size(72.dp)
                                .background(ShaktiTheme.SafetyRed, CircleShape)
                        ) {
                            Icon(imageVector = Icons.Default.CallEnd, contentDescription = "Decline", tint = Color.White, modifier = Modifier.size(32.dp))
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Decline", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.8f))
                    }

                    // Accept
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        IconButton(
                            onClick = onAccept,
                            modifier = Modifier
                                .size(72.dp)
                                .background(ShaktiTheme.SafeGreen, CircleShape)
                        ) {
                            Icon(imageVector = Icons.Default.Call, contentDescription = "Accept", tint = Color.White, modifier = Modifier.size(32.dp))
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Accept", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.8f))
                    }
                }
            }
        } else {
            // Active Conversation Layout
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Header
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 48.dp)
                ) {
                    Text(
                        text = callerName,
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    val mins = durationSeconds / 60
                    val secs = durationSeconds % 60
                    Text(
                        text = String.format("%02d:%02d", mins, secs),
                        fontFamily = FontFamily.Monospace,
                        style = MaterialTheme.typography.titleMedium,
                        color = ShaktiTheme.AmberGold
                    )
                }

                // Transcription Audio Screen Simulation Box
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF131318)),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, ShaktiTheme.BorderDark),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Mic, contentDescription = null, tint = ShaktiTheme.SafeGreen, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Safety Tele-Auditory Script Streaming",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = ShaktiTheme.SafeGreen
                            )
                        }
                        Divider(modifier = Modifier.padding(vertical = 12.dp), color = ShaktiTheme.BorderDark)
                        Text(
                            text = if (durationSeconds < 4) {
                                "[Voice Protocol Active]\nCaller Dad: \"Hello Samay? Where are you right now? I am tracking your live transit maps system...\""
                            } else if (durationSeconds < 8) {
                                "Caller Dad: \"...I have your route deviations map open. Stay in populated brightly lit locations...\""
                            } else {
                                "Caller Dad: \"...I am dialing safety dispatched patrols just in case. Do you need immediate dispatcher checkups? Answer loud and clear.\""
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White,
                            lineHeight = 22.sp
                        )
                    }
                }

                // Decline Button
                IconButton(
                    onClick = onDecline,
                    modifier = Modifier
                        .size(64.dp)
                        .background(ShaktiTheme.SafetyRed, CircleShape)
                        .padding(bottom = 16.dp)
                ) {
                    Icon(imageVector = Icons.Default.CallEnd, contentDescription = "End Call", tint = Color.White, modifier = Modifier.size(28.dp))
                }
            }
        }
    }
}

// -------------------------------------------------------------
// TAB 4: OPERATOR SCREENER & NOTIFICATION INTELLIGENCE
// -------------------------------------------------------------
@Composable
fun ScreenerNotifScreen(
    screenerActive: Boolean,
    screenerStep: Int,
    screenerScripts: List<String>,
    onNextScreenerStep: () -> Unit,
    onStartScreener: () -> Unit
) {
    // Simulated Inbox Alerts
    val mockNotifications = listOf(
        NotificationItem("n1", "Slack", "Rahul S.", "Hey Samay, is the design blueprint file finalized? Clients want the Kubernetes scale setup logs in 10 minutes", "14:45", "Urgent"),
        NotificationItem("n2", "WhatsApp", "Grandmother", "Have your warm lunch, take care of your throat! Bless you.", "14:41", "Normal"),
        NotificationItem("n3", "SMS Promo", "DiscountCity", "GET 90% DISCOUNT! Use coupon SPAMCREDIT NOW!", "14:38", "Spam"),
        NotificationItem("n4", "Gmail Alert", "Server Node", "[CRITICAL] Memory database threshold exceeded peak 95% on container Node-B", "14:30", "Urgent")
    )

    var notificationSummarized by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Shakti Operator & Notification Intel",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                color = ShaktiTheme.AmberGold
            )
            Text(
                text = "Acts as an automated intelligence buffer on top of device telephony and incoming notification trees, safeguarding against spam and compiling dense data streams into rapid bulletins.",
                style = MaterialTheme.typography.bodyMedium,
                color = ShaktiTheme.TextMuted,
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
            )
        }

        // Feature 1: AI Telephone Operator Section
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = ShaktiTheme.CardObsidian),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, ShaktiTheme.BorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.RingVolume, contentDescription = null, tint = ShaktiTheme.AmberGold, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "AI Call Screening Engine",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = ShaktiTheme.TextMain
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Allows Shakti to intercept anonymous calls, prompt the caller, and transcribe in real-time. Shakti acts as a robotic safety receptionist, routing or rejecting.",
                        style = MaterialTheme.typography.bodySmall,
                        color = ShaktiTheme.TextMuted
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    if (!screenerActive) {
                        Button(
                            onClick = onStartScreener,
                            colors = ButtonDefaults.buttonColors(containerColor = ShaktiTheme.AmberGold),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("start_screener_button")
                        ) {
                            Text("SIMULATE SPAM TELEMARKETER INTERCEPT", fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                    } else {
                        // Incremental active screener panel
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.Black),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, ShaktiTheme.BorderDark),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "Shakti Screener Live Transcript:",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = ShaktiTheme.AmberGold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                // Print scripts up to current step
                                for (i in 0..screenerStep) {
                                    val logText = screenerScripts[i]
                                    val colorText = if (logText.contains("Shakti")) ShaktiTheme.AmberGold else if (logText.contains("System")) ShaktiTheme.SafeGreen else ShaktiTheme.TextMain
                                    Text(
                                        text = logText,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = colorText,
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = onNextScreenerStep,
                                    colors = ButtonDefaults.buttonColors(containerColor = ShaktiTheme.AmberGold),
                                    modifier = Modifier.align(Alignment.End)
                                ) {
                                    Text(
                                        text = if (screenerStep == screenerScripts.size - 1) "DISMISS INTERCEPT" else "NEXT CONVERSATION TURN",
                                        color = Color.Black,
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Feature 2: Passive Notification Interception Hub
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = ShaktiTheme.CardObsidian),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, ShaktiTheme.BorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.NotificationsActive, contentDescription = null, tint = ShaktiTheme.AmberGold, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Notification Intelligence center",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = ShaktiTheme.TextMain
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Rather than shifting contexts for multiple push notifications, the notification intercept service compiles alerts, extracts priorities, blocks noise, and renders aggregated feeds.",
                        style = MaterialTheme.typography.bodySmall,
                        color = ShaktiTheme.TextMuted
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Simulated Incoming Alert Stream", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = ShaktiTheme.TextMain)
                        Button(
                            onClick = { notificationSummarized = !notificationSummarized },
                            colors = ButtonDefaults.buttonColors(containerColor = if (notificationSummarized) ShaktiTheme.SafeGreen else ShaktiTheme.AmberGold),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = if (notificationSummarized) "SHOW UNRESTRICTED FEED" else "COMPILE AI SUMMARY",
                                color = Color.Black,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    if (notificationSummarized) {
                        // Render AI Aggregated Bulletin
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.Black),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, ShaktiTheme.SafeGreen.copy(alpha = 0.5f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    "✨ Shakti AI Intercept Bulletin Summary:",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = ShaktiTheme.SafeGreen
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "• 🔴 [URGENT] Infrastructure alert: Critical memory DB threshold exceeded Node-B (Gmail - 14:30).\n\n" +
                                            "• 🔴 [URGENT] Work context: Rahul requested Kubernetes deployment task feedback within 10 minutes (Slack - 14:45).\n\n" +
                                            "• 🟢 [NORMAL] Social contact: Warm family blessing from Grandmother (WhatsApp - 14:41).\n\n" +
                                            "• 🚫 [NOISE BLOCKED] Suppressed promotional SMS offering high discount coupons.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = ShaktiTheme.TextMain,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    } else {
                        // Render raw notifications list
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            mockNotifications.forEach { item ->
                                val tagColor = when (item.urgency) {
                                    "Urgent" -> ShaktiTheme.SafetyRed
                                    "Spam" -> ShaktiTheme.BorderDark
                                    else -> ShaktiTheme.SafeGreen
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(1.dp, ShaktiTheme.BorderDark, RoundedCornerShape(6.dp))
                                        .background(Color.Black)
                                        .padding(8.dp)
                                ) {
                                    Column {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Card(
                                                    colors = CardDefaults.cardColors(containerColor = ShaktiTheme.BorderDark),
                                                    shape = RoundedCornerShape(4.dp)
                                                ) {
                                                    Text(
                                                        item.app,
                                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                        color = ShaktiTheme.AmberGold,
                                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                    )
                                                }
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(item.sender, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = ShaktiTheme.TextMain)
                                            }
                                            
                                            // Urgency badge
                                            Card(
                                                colors = CardDefaults.cardColors(containerColor = tagColor.copy(alpha = 0.2f)),
                                                shape = RoundedCornerShape(4.dp)
                                            ) {
                                                Text(
                                                    item.urgency.uppercase(),
                                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                    color = if (item.urgency == "Spam") ShaktiTheme.TextMuted else tagColor,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(item.content, style = MaterialTheme.typography.bodySmall, color = ShaktiTheme.TextMuted)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// TAB 5: SECURITY, INFRASTRUCTURE & MVP ROADMAP
// -------------------------------------------------------------
@Composable
fun InfrastructureSecurityScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Security Standard & MVP Roadmap",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                color = ShaktiTheme.AmberGold
            )
            Text(
                text = "Comprehensive specification covering system permission architectures, cryptography boundaries, localized biometric verification flow, and 5-stage progressive MVP launching details.",
                style = MaterialTheme.typography.bodyMedium,
                color = ShaktiTheme.TextMuted,
                modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
            )
        }

        // Section: Cryptography & Security Architecture
        item {
            BlueprintCard(title = "1. Security & Device Permission Layer", icon = Icons.Outlined.Security) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Operating as a system-level agent warrants extreme compliance standards. Shakti secures user trust via strict cryptographic boundaries:",
                        style = MaterialTheme.typography.bodySmall,
                        color = ShaktiTheme.TextMuted
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    SecurityFeatureRow("Local Device Encryption", "Audio files and conversational transcripts are serialized using AES-256 encryption. Storing decryption vectors occurs within the hardware-backed Android Keystore.")
                    SecurityFeatureRow("Interactive Permission Consents", "Accessibility services are strictly scoped. Telemetry screen scrapes are processed on-device (via local neural networks) or through hashed token payloads.")
                    SecurityFeatureRow("Voice Biometrics Authentication", "Crucial operations like banking approvals or launching security applications are guarded by user-specific deep speech footprint verification.")
                }
            }
        }

        // Section: Scaling 10 Million Users Infrastructure
        item {
            BlueprintCard(title = "2. Scalability Architecture Roadmap", icon = Icons.Outlined.TrendingUp) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ScalingStageRow("100,000 active users", "Single-region API router, direct Redis state synchronization, clustered PostgreSQL instances with secondary backup read pools.")
                    ScalingStageRow("1,000,000 active users", "Deploy secondary endpoints via Cloud CDN. Distribute high semantic user vector memories across isolated Qdrant vector storage clusters.")
                    ScalingStageRow("10,000,000 active users", "Establish multi-region Kubernetes routing clusters. Offload STT processing to regional Edge networks to guarantee latency under 150ms globally.")
                }
            }
        }

        // Section: Monetization Strategy Models
        item {
            BlueprintCard(title = "3. Product Monetization Packages", icon = Icons.Outlined.MonetizationOn) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PricingTierCard("FREE TIER", "$0 / Mo", "Standard chat agent assistance, offline emergency safety SOS alerts, 100 on-device intents daily.", modifier = Modifier.weight(1f))
                    PricingTierCard("PRO TIER", "$12 / Mo", "Real-time streaming (Gemini Live), full autonomous accessibility-driven application control, custom safety features.", modifier = Modifier.weight(1.1f), highlighted = true)
                    PricingTierCard("BUSINESS", "Custom", "Exclusive API integration pipelines, secure hardware infrastructure nodes, audit registries.", modifier = Modifier.weight(1f))
                }
            }
        }

        // Section: Interactive 5-Phase Roadmap Review
        item {
            BlueprintCard(title = "4. Detailed MVP Delivery Phases", icon = Icons.Outlined.Event) {
                Text(
                    text = "A structured view of our engineering deployment journey from a voice sandbox to a fully independent device operator:",
                    style = MaterialTheme.typography.bodySmall,
                    color = ShaktiTheme.TextMuted,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                
                RoadmapMilestone("Phase 1: Deep Voice Pipeline (Month 1-2)", "Setup streaming STT/TTS channels via Gemini Live services, implement robust foreground noise filters, and test offline SOS audio alerts.", completed = true)
                RoadmapMilestone("Phase 2: Android Accessibility SDK Integration (Month 3-4)", "Create custom background services to parse viewport nodes in real-time, register biometric-pin-verification boundaries, and test click automation mocks.", completed = true)
                RoadmapMilestone("Phase 3: Multi-Agent Intent Parsing & Execution (Month 5-6)", "Coordinate intent mapping algorithms leveraging Gemini 2.5 Flash, construct planning graph frameworks, and serialize entity binding lists.", completed = false)
                RoadmapMilestone("Phase 4: Screen OCR & Spatial Vision Interface (Month 7-8)", "Formulate computer vision filters, enable screenshot analytics streams via Gemini 2.5 Pro representation nodes, and verify spatial button boundaries.", completed = false)
                RoadmapMilestone("Phase 5: Fully Autonomous AI Mobile Operator (Month 9-10)", "Perform exhaustive system stress runs, establish distributed Kubernetes gateways, launch Pro packages, and initiate public rollout.", completed = false)
            }
        }
    }
}

@Composable
fun SecurityFeatureRow(title: String, desc: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = ShaktiTheme.SafeGreen, modifier = Modifier.size(16.dp).offset(y = 2.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(title, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = ShaktiTheme.TextMain)
            Text(desc, style = MaterialTheme.typography.labelSmall, color = ShaktiTheme.TextMuted)
        }
    }
}

@Composable
fun ScalingStageRow(users: String, details: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Icon(imageVector = Icons.Default.TrendingUp, contentDescription = null, tint = ShaktiTheme.AmberGold, modifier = Modifier.size(16.dp).offset(y = 2.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(users, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = ShaktiTheme.AmberGold)
            Text(details, style = MaterialTheme.typography.labelSmall, color = ShaktiTheme.TextMuted)
        }
    }
}

@Composable
fun PricingTierCard(
    tier: String,
    price: String,
    features: String,
    modifier: Modifier = Modifier,
    highlighted: Boolean = false
) {
    val borderColor = if (highlighted) ShaktiTheme.AmberGold else ShaktiTheme.BorderDark
    val bg = if (highlighted) ShaktiTheme.AmberGold.copy(alpha = 0.05f) else Color.Transparent
    
    Card(
        colors = CardDefaults.cardColors(containerColor = ShaktiTheme.DeepObsidian),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, borderColor),
        modifier = modifier.height(180.dp)
    ) {
        Column(
            modifier = Modifier
                .background(bg)
                .padding(8.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = tier,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.ExtraBold),
                    color = if (highlighted) ShaktiTheme.AmberGold else ShaktiTheme.TextMain
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = price,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                    color = ShaktiTheme.TextMain
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = features,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    color = ShaktiTheme.TextMuted,
                    lineHeight = 13.sp
                )
            }
        }
    }
}

@Composable
fun RoadmapMilestone(
    period: String,
    details: String,
    completed: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = if (completed) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
            contentDescription = null,
            tint = if (completed) ShaktiTheme.SafeGreen else ShaktiTheme.TextMuted,
            modifier = Modifier.size(16.dp).offset(y = 2.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = period,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = if (completed) FontWeight.Bold else FontWeight.Normal
                ),
                color = if (completed) ShaktiTheme.SafeGreen else ShaktiTheme.TextMain
            )
            Text(
                text = details,
                style = MaterialTheme.typography.labelSmall,
                color = ShaktiTheme.TextMuted
            )
        }
    }
}
