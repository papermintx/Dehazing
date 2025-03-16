package com.mk.dehazing.ui.presentation.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mk.dehazing.R

@Composable
fun OpenGallery(
    modifier: Modifier = Modifier,
    onImageSelected: (Uri?) -> Unit
) {
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        onImageSelected(uri)
    }

    Button(
        modifier = modifier,
        onClick = { galleryLauncher.launch("image/*") }
    ) {
        Text(stringResource(R.string.open_gallery))
    }
}