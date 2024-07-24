package com.example.travelillay

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.travelillay.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            // Lógica para iniciar sesión
        }
    }
}
