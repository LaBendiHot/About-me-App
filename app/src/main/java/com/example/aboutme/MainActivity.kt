package com.example.aboutme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.animation.*
import androidx.compose.foundation.border
import kotlinx.coroutines.delay
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.animation.core.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import android.content.Intent
import android.widget.Toast
import androidx.core.net.toUri
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.ui.text.font.FontStyle



private val DarkColors = darkColorScheme(
    primary = Color(0xFF2196F3),
    onPrimary = Color.White,
    surface = Color(0xFF121212),
    onSurface = Color.White,
    background = Color(0xFF121212),
    onBackground = Color.White,
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = DarkColors
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AboutMeApp()
                }
            }
        }
    }
}

@Composable
fun AboutMeApp() {
    var selectedSection by remember { mutableStateOf("") }
    var visible by remember { mutableStateOf(false) }
    var targetSection by remember { mutableStateOf("") }

    LaunchedEffect(targetSection) {
        if (targetSection == selectedSection) {
            // Presionó el mismo → oculta
            visible = false
            delay(300)
            selectedSection = ""
        } else {
            // Presionó otro → oculta y luego muestra
            if (visible) {
                visible = false
                delay(300)
            }
            selectedSection = targetSection
            visible = true
        }
    }



    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF121212)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(16.dp)
        ) {
            // 🔷 Contenedor Superior (Foto + Datos)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF1E1E1E))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val infiniteTransition = rememberInfiniteTransition()
                val angle by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 4000, easing = LinearEasing)
                    )
                )

                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .drawBehind {
                            val strokeWidth = 6.dp.toPx()
                          //  val radius = size.minDimension / 2
                            val center = this.center

                            drawArc(
                                brush = Brush.sweepGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color(0xFF2196F3),
                                        Color.Transparent
                                    ),
                                    center = center
                                ),
                                startAngle = angle,
                                sweepAngle = 90f,
                                useCenter = false,
                                style = Stroke(width = strokeWidth)
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.foto),
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        "Carlos Barajas",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text("Estudiante de Ingeniería", fontSize = 16.sp, color = Color.LightGray)
                    Text(
                        "Especialidad en tecnologias en la nube",
                        fontSize = 14.sp,
                        color = Color.LightGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 🔘 Botones circulares
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                CircleButton("D") { targetSection = "datos" }
                CircleButton("S") { targetSection = "skills" }
                CircleButton("H") { targetSection = "hobbies" }
                CircleButton("E") { targetSection = "expectativas" }
            }

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(
                color = Color.Gray.copy(alpha = 0.3f),
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            // 🔽 Animación dinámica
            AnimatedContent(
                targetState = selectedSection,
                transitionSpec = {
                    slideInHorizontally { fullWidth -> fullWidth } + fadeIn() togetherWith
                            slideOutHorizontally { fullWidth -> -fullWidth } + fadeOut()
                },
                label = "Seccion"
            ) { section ->
                val scrollState = rememberScrollState()

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                ) {
                    if (section.isNotEmpty()) {
                        Text(
                            text = when (section) {
                                "datos" -> "Datos Personales"
                                "skills" -> "Habilidades"
                                "hobbies" -> "Hobbies"
                                "expectativas" -> "Expectativas"
                                else -> ""
                            },
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        when (section) {
                            "datos" ->{
                                DatosPersonalesCard()
                                ContactIconsRow()
                            }
                            "skills" -> {
                                SkillsCard()

                                // 🔹 Barra divisora visual entre tarjeta y íconos
                                HorizontalDivider(
                                    color = Color.Gray.copy(alpha = 0.3f),
                                    thickness = 1.dp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                )

                                SkillIconsGrouped()
                            }

                            "hobbies" -> HobbiesCard()
                            "expectativas" -> ExpectativasCard()
                        }
                    }
                }
            }
        }
    }
}


            @Composable
fun CircleButton(letter: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .border(width = 2.dp, color = Color.White, shape = CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letter,
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun DatosPersonalesCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawRoundRect(
                    color = Color(0xFF2196F3).copy(alpha = 0.25f),
                    topLeft = Offset(-6f, -6f),
                    size = Size(size.width + 12f, size.height + 12f),
                    cornerRadius = CornerRadius(24f, 24f)
                )
            }
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF9F9F9)) // blanco suave
            .padding(16.dp)
    ) {
        // 👤 Info principal
        Text("Carlos Barajas Sánchez", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Text("Carrera: Ingeniería en Sistemas Computacionales", fontSize = 16.sp, color = Color.DarkGray)
        Text("Especialidad: Tecnologías en la Nube", fontSize = 16.sp, color = Color.DarkGray)

        // 🔻 Divider para separar secciones
        HorizontalDivider(
            modifier = Modifier
                .padding(vertical = 12.dp),
            color = Color.Gray.copy(alpha = 0.3f),
            thickness = 1.dp
        )

        // 🗣️ Descripción personal
        Text(
            text = "\uD83D\uDC68\u200D\uD83D\uDCBBSoy un estudiante apasionado por el desarrollo full stack y la ciencia de datos. " +
                    "Me gusta crear soluciones reales y enfocarme en proyectos que generen impacto. \uD83D\uDCCA",
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}



@Composable
fun ContactIcon(iconId: Int, contentDescription: String, onClick: () -> Unit) {
    Image(
        painter = painterResource(id = iconId),
        contentDescription = contentDescription,
        colorFilter = ColorFilter.tint(Color.White),
        modifier = Modifier
            .size(32.dp)
            .clickable(onClick = onClick)
    )
}
@Composable
fun ContactIconsRow() {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ContactIcon(R.drawable.email, "Correo") {
            // Implementacion de email
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = ("mailto:carlosbarajas75@hotmail.com").toUri()
            }
            context.startActivity(intent)
        }
        ContactIcon(R.drawable.linkedin, "LinkedIn") {
            val url = "https://www.linkedin.com".toUri()
            val intent = Intent(Intent.ACTION_VIEW, url)

            // ⚠️ Envolver en un chooser para que el usuario elija la app
            val chooser = Intent.createChooser(intent, "Abrir con...")

            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(chooser)
            } else {
                Toast.makeText(context, "No se encontró una app compatible", Toast.LENGTH_SHORT).show()
            }
        }


        ContactIcon(R.drawable.github, "GitHub") {
            // Puedes abrir tu GitHub también
            val intent = Intent(Intent.ACTION_VIEW, "https://github.com/LaBendiHot".toUri())
            context.startActivity(intent)
        }
    }
}



@Composable
fun SkillsCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawRoundRect(
                    color = Color(0xFF2196F3).copy(alpha = 0.25f),
                    topLeft = Offset(-6f, -6f),
                    size = Size(size.width + 12f, size.height + 12f),
                    cornerRadius = CornerRadius(24f, 24f)
                )
            }
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF9F9F9))
            .padding(16.dp)
    ) {
        Text(
            "Habilidades Técnicas",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text("Lenguajes y Frameworks:", fontWeight = FontWeight.SemiBold, color = Color.Black)
        Text("• Python\n• Laravel\n• Node.js\n• React", color = Color.DarkGray)

        Spacer(modifier = Modifier.height(8.dp))

        Text("Bases de Datos:", fontWeight = FontWeight.SemiBold, color = Color.Black)
        Text("• PostgreSQL", color = Color.DarkGray)

        Spacer(modifier = Modifier.height(8.dp))

        Text("Ciencia de Datos:", fontWeight = FontWeight.SemiBold, color = Color.Black)
        Text("• Análisis y visualización de datos con Python", color = Color.DarkGray)
    }
}

@Composable
fun HobbyTag(label: String, emoji: String, backgroundColor: Color) {
    Surface(
        shape = RoundedCornerShape(50),
        color = backgroundColor,
        shadowElevation = 2.dp,
        tonalElevation = 2.dp,
        modifier = Modifier.padding(end = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = emoji,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                fontSize = 14.sp,
                color = Color.White,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


@Composable
fun HobbiesCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawRoundRect(
                    color = Color(0xFF2196F3).copy(alpha = 0.25f),
                    topLeft = Offset(-6f, -6f),
                    size = Size(size.width + 12f, size.height + 12f),
                    cornerRadius = CornerRadius(24f, 24f)
                )
            }
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF9F9F9))
            .padding(16.dp)
    ) {
        Text(
            "Mis Hobbies",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            "Me gusta equilibrar mi vida académica con hobbies que me relajan y desarrollan mi creatividad.",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // 🟢 Hobbies Activos
        Text("🎧 Hobbies Activos:", fontWeight = FontWeight.SemiBold, color = Color.Black)
        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            HobbyTag("Escuchar música", "🎧", Color(0xFF42A5F5))
            HobbyTag("Jugar videojuegos", "🕹️", Color(0xFF42A5F5))
        }

        // 🎨 Hobbies Creativos
        Text("🧊 Hobbies Creativos:", fontWeight = FontWeight.SemiBold, color = Color.Black)
        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            HobbyTag("Modelado 3D (Blender)", "🧊", Color(0xFF7E57C2))

        }

        // 🏐 Hobbies Deportivos
        Text("🏐 Hobbies Deportivos:", fontWeight = FontWeight.SemiBold, color = Color.Black)
        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            HobbyTag("Volleyball (en pausa)", "🏐", Color(0xFF66BB6A))        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            "Algunos de estos hobbies también fortalecen mi creatividad, enfoque y pensamiento lógico.",
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun ExpectationChip(text: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(50),
        color = color,
        shadowElevation = 2.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = text,
                fontSize = 14.sp,
                color = Color.White
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExpectativasCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawRoundRect(
                    color = Color(0xFF2196F3).copy(alpha = 0.25f),
                    topLeft = Offset(-6f, -6f),
                    size = Size(size.width + 12f, size.height + 12f),
                    cornerRadius = CornerRadius(24f, 24f)
                )
            }
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF9F9F9))
            .padding(16.dp)
    ) {
        Text(
            "Expectativas Profesionales",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            "Actualmente me encuentro en el último año de mi carrera. Mis expectativas están enfocadas en consolidar mis habilidades, enfrentar nuevos retos y seguir aprendiendo constantemente.",
            fontSize = 14.sp,
            color = Color.DarkGray,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // 🎯 Chips de expectativas
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            ExpectationChip("🎓 Titularme", Color(0xFF64B5F6))
            ExpectationChip("💼 Realizar residencias profesionales", Color(0xFF81C784))
            ExpectationChip("🔐 Enfocarme en ciberseguridad", Color(0xFFBA68C8))
            ExpectationChip("📊 Profundizar en ciencia de datos", Color(0xFFFF8A65))
            ExpectationChip("🚀 Participar en proyectos reales", Color(0xFFFFD54F))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            "Mi meta es seguir creciendo como profesionista, aprendiendo tanto de la teoría como de la práctica.",
            fontSize = 14.sp,
            fontStyle = FontStyle.Italic,
            color = Color.Gray
        )
    }
}



    data class SkillItem(
        val icon: Int,
        val name: String,
        val description: String
)

@Composable
fun SkillIconsGrouped() {
    val groupedSkills = mapOf(
        "Lenguajes y Frameworks" to listOf(
            SkillItem(R.drawable.python, "Python", "Lenguaje versátil para desarrollo y ciencia de datos."),
            SkillItem(R.drawable.laravel, "Laravel", "Framework de backend moderno en PHP."),
            SkillItem(R.drawable.nodejs, "Node.js", "Entorno de ejecución para JavaScript en el servidor."),
            SkillItem(R.drawable.react, "React", "Librería para construir interfaces interactivas.")
        ),
        "Bases de Datos" to listOf(
            SkillItem(R.drawable.postgresql, "PostgreSQL", "Base de datos relacional avanzada y robusta.")
        ),
        "Ciencia de Datos" to listOf(
            SkillItem(R.drawable.datascience, "Data Science", "Procesamiento, análisis y visualización de datos.")
        )
    )

    var selectedSkill by remember { mutableStateOf<SkillItem?>(null) }

    Column(modifier = Modifier.fillMaxWidth()) {
        groupedSkills.forEach { (category, skills) ->
            Text(
                text = category,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                items(skills) { skill ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { selectedSkill = skill }
                    ) {
                        Image(
                            painter = painterResource(id = skill.icon),
                            contentDescription = skill.name,
                            colorFilter = ColorFilter.tint(Color.White),
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )
                        Text(
                            text = skill.name,
                            fontSize = 12.sp,
                            color = Color.White,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }

    // Dialog tipo tooltip al presionar ícono
    if (selectedSkill != null) {
        AlertDialog(
            onDismissRequest = { selectedSkill = null },
            title = { Text(selectedSkill!!.name) },
            text = { Text(selectedSkill!!.description) },
            confirmButton = {
                TextButton(onClick = { selectedSkill = null }) {
                    Text("Cerrar")
                }
            }
        )
    }
}



@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun AboutMePreview() {
    MaterialTheme(
        colorScheme = DarkColors
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AboutMeApp()
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun HobbiesCardPreview() {
    MaterialTheme(colorScheme = DarkColors) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            HobbiesCard()
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun ExpectativasCardPreview() {
    MaterialTheme(colorScheme = DarkColors) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            color = MaterialTheme.colorScheme.background

        ) {
            ExpectativasCard()
        }
    }
}


