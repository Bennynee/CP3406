package com.readingjournal.app.data.barcode

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BarcodeScanner @Inject constructor() {
    
    fun createScanOptions(): ScanOptions {
        return ScanOptions().apply {
            setDesiredBarcodeFormats(listOf(
                BarcodeFormat.EAN_13.name,
                BarcodeFormat.EAN_8.name,
                BarcodeFormat.UPC_A.name,
                BarcodeFormat.UPC_E.name,
                BarcodeFormat.CODE_128.name,
                BarcodeFormat.CODE_39.name,
                BarcodeFormat.QR_CODE.name
            ))
            setPrompt("Scan a book's barcode")
            setCameraId(0)
            setBeepEnabled(false)
            setBarcodeImageEnabled(true)
            setOrientationLocked(false)
        }
    }
    
    fun createScanLauncher(
        activity: ComponentActivity,
        onResult: (String?) -> Unit
    ): ActivityResultLauncher<ScanOptions> {
        return activity.registerForActivityResult(ScanContract()) { result ->
            if (result.contents != null) {
                onResult(result.contents)
            } else {
                onResult(null)
            }
        }
    }
}
