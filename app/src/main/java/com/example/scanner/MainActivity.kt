package com.example.scanner

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode

class MainActivity : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),123)
        }else{
            startSanner()
        }
    }

    private fun startSanner() {

        val scannerView : CodeScannerView = findViewById(R.id.scaanerview)
        codeScanner = CodeScanner(this,scannerView)
        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS

        codeScanner.autoFocusMode =    AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = true

        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                Toast.makeText(this,"scan result :  ${it.text}",Toast.LENGTH_SHORT).show()
            }
        }

        codeScanner.errorCallback = ErrorCallback {
            runOnUiThread {
                Toast.makeText(this,"Camera initialization error : ${it.message}",Toast.LENGTH_SHORT).show()
            }
        }


        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 123){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera permission Granted", Toast.LENGTH_SHORT).show()
                 startSanner()
            }
        }else{
            Toast.makeText(this,"Camera permission denied",Toast.LENGTH_SHORT).show()
        }
    }


    override fun onResume() {
        super.onResume()
        if(::codeScanner.isInitialized){
            codeScanner?.startPreview()
        }
    }


    override fun onPause() {

        if(::codeScanner.isInitialized){
            codeScanner?.releaseResources()
        }

        super.onPause()


    }
}