package com.readingjournal.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProgressChart(
    modifier: Modifier = Modifier
) {
    // Placeholder for progress chart
    // In a real app, this would use a chart library like MPAndroidChart
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Text(
                text = "📊 Progress Chart",
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}
