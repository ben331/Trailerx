package com.globant.imdb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.globant.imdb.core.RetrofitHelper
import com.globant.imdb.databinding.ActivityMainBinding

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