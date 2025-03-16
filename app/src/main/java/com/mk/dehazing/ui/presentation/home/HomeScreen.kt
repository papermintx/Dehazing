package com.mk.dehazing.ui.presentation.home

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.mk.core.domain.model.StateApp
import com.mk.dehazing.ui.presentation.components.ErrorDialog
import com.mk.dehazing.ui.presentation.components.LoadingDialog
import com.mk.dehazing.ui.presentation.components.OpenGallery
import com.mk.dehazing.ui.utilities.toBitMap

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewmodel: HomeViewModel = hiltViewModel(),
) {

    val state by viewmodel.state.collectAsStateWithLifecycle()
    var uriImage by remember { mutableStateOf<Uri?>(null) }
    var bitmapImage by remember { mutableStateOf<Bitmap?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Dehazing Image") })
        }
    ) { innerPadding ->

        Box(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (state) {
                is StateApp.Error -> {
                    val stateError = state as StateApp.Error
                    ErrorDialog(isVisible = true, errorMessage = stateError.message) {
                        viewmodel.resetState()
                    }
                }
                is StateApp.Loading -> {
                    LoadingDialog(isVisible = true)
                }
                is StateApp.Success -> {
                    val stateSuccess = state as StateApp.Success
                    bitmapImage = stateSuccess.data
                    showDialog = true
                }
                else -> {}
            }

            Column(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(2f)
                        .height(250.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    if (uriImage != null) {
                        Image(
                            painter = rememberAsyncImagePainter(uriImage),
                            contentDescription = "Captured Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.LightGray),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No Image Captured", color = Color.Gray)
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))

                OpenGallery(
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    uriImage = it
                }

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        uriImage?.let {
                            val bitmap = uriImage!!.toBitMap(context)
                            viewmodel.dehazeImage(bitmap)
                        }
                    }
                ) {
                    Text(text = "Remove Haze")
                }
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = {
                        showDialog = false
                        viewmodel.resetState()
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showDialog = false
                                viewmodel.resetState()
                            }
                        ) {
                            Text("Close")
                        }
                    },
                    title = { Text("Hasil Remove Haze") },
                    text = {
                        bitmapImage?.let {
                            Image(
                                bitmap = it.asImageBitmap(),
                                contentDescription = "Hasil Dehaze",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .border(2.dp, Color.Gray, RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Crop
                            )
                        } ?: Text("Gagal memuat gambar.")
                    }
                )
            }
        }
    }
}
