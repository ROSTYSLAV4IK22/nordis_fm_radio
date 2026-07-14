package com.nordisapps.fmradio.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SavedStationsSection(
    stations: List<Double>,
    onStationSelected: (Double) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        Text(
            text = "Сохранённые станции",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (stations.isEmpty()) {
            Text(
                text = "Пока здесь пусто. Запустите сканирование, чтобы найти станции",
                modifier = Modifier.padding(vertical = 16.dp)
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(stations) { freq ->
                    TextButton(
                        onClick = { onStationSelected(freq) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "$freq MHz",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    HorizontalDivider()
                }
            }

        }
    }
}