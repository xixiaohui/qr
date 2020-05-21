package com.xixiaohui.scanner.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.xixiaohui.scanner.R
import com.xixiaohui.scanner.databinding.ActivityGenerateBinding
import com.xixiaohui.scanner.fragment.*

import kotlinx.android.synthetic.main.activity_generate.*
import kotlinx.android.synthetic.main.fragment_generate_main.*

class GenerateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGenerateBinding

    private lateinit var textFragment: GenerateTextFragment
    private lateinit var wifiFragment: GenerateWifiFragment
    private lateinit var urlFragment: GenerateUrlFragment
    private lateinit var contactFragment: GenerateContactFragment
    private lateinit var appFragment: GenerateAppFragment
    private lateinit var emailFragment: GenerateEmailFragment
    private lateinit var geoFragment: GenerateGeoFragment
    private lateinit var phoneFragment: GeneratePhoneFragment
    private lateinit var eventFragment: GenerateEventFragment
    private lateinit var smsFragment: GenerateSmsFragment
    private lateinit var myqrFragment: GenerateMyqrFragment
    private lateinit var productFragment: GenerateProductFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenerateBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        setSupportActionBar(toolbar)
//        this.supportActionBar.apply {
//            this!!.setDisplayHomeAsUpEnabled(true)
//        }

        textFragment = GenerateTextFragment()
        wifiFragment = GenerateWifiFragment()
        urlFragment = GenerateUrlFragment()
        contactFragment = GenerateContactFragment()
        appFragment = GenerateAppFragment()
        emailFragment = GenerateEmailFragment()
        geoFragment = GenerateGeoFragment()
        phoneFragment = GeneratePhoneFragment()
        eventFragment = GenerateEventFragment()
        smsFragment = GenerateSmsFragment()
        myqrFragment = GenerateMyqrFragment()
        productFragment = GenerateProductFragment()


        val fm = supportFragmentManager
        fm.beginTransaction().add(R.id.generate_container, GenerateMainFragment()).commit()


    }

    fun doNextFragment(view: View) {

        val id = view.id
        val fm = supportFragmentManager
        when(id){
            R.id.generate_text->fm.beginTransaction().replace(R.id.generate_container, textFragment).commit()
            R.id.generate_url ->fm.beginTransaction().replace(R.id.generate_container, urlFragment).commit()
            R.id.generate_wifi ->fm.beginTransaction().replace(R.id.generate_container, wifiFragment).commit()
            R.id.generate_contact ->fm.beginTransaction().replace(R.id.generate_container, contactFragment).commit()
            R.id.generate_app ->fm.beginTransaction().replace(R.id.generate_container, appFragment).commit()
            R.id.generate_email ->fm.beginTransaction().replace(R.id.generate_container, emailFragment).commit()
            R.id.generate_phone ->fm.beginTransaction().replace(R.id.generate_container, phoneFragment).commit()
            R.id.generate_event ->fm.beginTransaction().replace(R.id.generate_container, eventFragment).commit()
            R.id.generate_sms ->fm.beginTransaction().replace(R.id.generate_container, smsFragment).commit()
            R.id.generate_myqr ->fm.beginTransaction().replace(R.id.generate_container, myqrFragment).commit()
            R.id.generate_product ->fm.beginTransaction().replace(R.id.generate_container, productFragment).commit()
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