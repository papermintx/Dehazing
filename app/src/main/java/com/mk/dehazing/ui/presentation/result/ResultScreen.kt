package com.mk.dehazing.ui.presentation.result

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

@Composable
fun ResultScreen(
    modifier: Modifier = Modifier,
    bitmap: Bitmap
) {
    Scaffold { innerpadding ->
        Box(
            modifier = modifier.padding(innerpadding)
                .fillMaxSize()
        ){
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Hasil Dehaze",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
                    .border(2.dp, Color.Gray, RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }

}