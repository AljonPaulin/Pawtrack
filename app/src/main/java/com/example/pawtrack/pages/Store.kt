package com.example.pawtrack.pages

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.util.*

@Composable
fun Store(modifier: Modifier = Modifier) {
    ImagePickerApp()
}

@Composable
fun ImagePickerApp() {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var latestImage by remember { mutableStateOf<Bitmap?>(null) }
    var latestImagePath by remember { mutableStateOf<String?>(null) }

    // Load latest image on startup
    LaunchedEffect(Unit) {
        latestImagePath = getLatestImagePath(context)
        latestImagePath?.let { path ->
            latestImage = BitmapFactory.decodeFile(path)
        }
    }

    // Create a temporary file for the captured image
    fun createImageFile(): Uri {
        val file = File(context.filesDir, "captured_image.jpg")
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
    }



    // Launcher to pick image from gallery
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                imageUri = it
                val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                latestImage = bitmap
            }
        }
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        latestImage?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Displayed Image",
                modifier = Modifier
                    .size(100.dp, 100.dp)
                    .padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { imagePickerLauncher.launch("image/*") }) {
            Text("Select from Gallery")
        }


        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            latestImage?.let { bitmap ->
                val filename = "latest_saved_image.jpg"
                val nameWithoutExtension = filename.substringBeforeLast(".")
                val savedPath = saveImageToInternalStorage(context, bitmap, filename)
                if (savedPath != null) {
                    latestImagePath = savedPath
                    Log.d("Image", "Path $latestImagePath")
                }
            }
        }) {
            Text("Save Image Locally")
        }
    }
}

fun saveImageToInternalStorage(context: Context, bitmap: Bitmap, filename: String): String? {
    return try {
        val file = File(context.filesDir, "$filename.jpg")
        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        }
        file.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun getLatestImagePath(context: Context): String? {
    val file = File(context.filesDir, "latest_saved_image.jpg")
    return if (file.exists()) file.absolutePath else null
}


