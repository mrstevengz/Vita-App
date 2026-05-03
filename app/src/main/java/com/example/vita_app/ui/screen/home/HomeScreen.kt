package com.example.vita_app.ui.screen.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vita_app.ui.components.HomeTopBar
import com.example.vita_app.ui.theme.BackgroundLight
import com.example.vita_app.ui.theme.MutedOlive
import com.example.vita_app.ui.theme.PineBlue
import com.example.vita_app.ui.theme.White

@Composable
fun HomeScreen(nombre: String) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {


        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(x = (-120).dp, y = (-100).dp)
                .background(
                    color = PineBlue.copy(alpha = 0.08f),
                    shape = CircleShape
                )
        )


        Box(
            modifier = Modifier
                .size(250.dp)
                .offset(x = 220.dp, y = 500.dp)
                .background(
                    color = MutedOlive.copy(alpha = 0.08f),
                    shape = CircleShape
                )
        )

        //Contenido principal de la APP
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp)
        ) {

            HomeTopBar(nombre) //

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            ) {

                // CARD PRINCIPAL (Calorías)
                Text("Today", fontWeight = FontWeight.Bold, fontSize = 24.sp,
                    modifier = Modifier.padding(bottom = 8.dp))

                Card(
                    shape = RoundedCornerShape(28.dp),
                    modifier = Modifier.fillMaxWidth().height(250.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        Text("Calories", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Remaining = Goal - Food", fontSize = 12.sp, color = Color.Gray)

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            // Circulo de progreso (hard-coded)
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.size(120.dp)
                            ) {

                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    drawArc(
                                        color = Color.LightGray,
                                        startAngle = 0f,
                                        sweepAngle = 360f,
                                        useCenter = false,
                                        style = Stroke(width = 20f)
                                    )

                                    drawArc(
                                        color = PineBlue,
                                        startAngle = -90f,
                                        sweepAngle = 220f,
                                        useCenter = false,
                                        style = Stroke(width = 20f)
                                    )
                                }

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("959", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                                    Text("Remaining", fontSize = 12.sp)
                                }
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column {

                                Text("Base Goal", color = Color.Gray, fontSize = 12.sp)
                                Text("1,600", fontWeight = FontWeight.Bold)

                                Spacer(modifier = Modifier.height(8.dp))

                                Text("Food", color = PineBlue, fontSize = 12.sp)
                                Text("641", fontWeight = FontWeight.Bold)

                                Spacer(modifier = Modifier.height(8.dp))

                                Text("Exercise", color = Color.Gray, fontSize = 12.sp)
                                Text("235", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // INDICADOR
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(2) {
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .padding(2.dp)
                                .clip(CircleShape)
                                .background(if (it == 0) PineBlue else Color.LightGray)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // CARDS INFERIORES
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    // STEPS
                    Card(
                        modifier = Modifier.weight(1f).height(160.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {

                            Text("Steps", fontWeight = FontWeight.Bold)

                            Spacer(modifier = Modifier.height(8.dp))

                            Text("7,456", fontSize = 22.sp, fontWeight = FontWeight.Bold)

                            Text("Goal: 10,000 steps", fontSize = 12.sp, color = Color.Gray)

                            Spacer(modifier = Modifier.height(8.dp))

                            LinearProgressIndicator(
                                progress = 0.7f,
                                modifier = Modifier.fillMaxWidth(),
                                color = PineBlue
                            )
                        }
                    }

                    // EXERCISE
                    Card(
                        modifier = Modifier.weight(1f).height(160.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {

                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Exercise", fontWeight = FontWeight.Bold)
                                Text("+", fontWeight = FontWeight.Bold)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text("235 cal", fontWeight = FontWeight.Bold)

                            Spacer(modifier = Modifier.height(8.dp))

                            Text("0:45 hr", color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}