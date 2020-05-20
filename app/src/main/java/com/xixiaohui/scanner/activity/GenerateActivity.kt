package com.xixiaohui.scanner.activity

import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.xixiaohui.scanner.R
import com.xixiaohui.scanner.databinding.ActivityGenerateBinding
import kotlinx.android.synthetic.main.activity_generate.*

class GenerateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGenerateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenerateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(toolbar)
        toolbar_layout.title = "Generate"

        this.supportActionBar.apply {
            this!!.setDisplayHomeAsUpEnabled(true)
        }


    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> false
        }
        return super.onOptionsItemSelected(item)
    }
}