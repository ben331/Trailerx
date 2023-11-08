package com.globant.imdb.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.globant.imdb.R
import com.globant.imdb.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding:ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_IMDb)
        setContentView(binding.root)
    }
}