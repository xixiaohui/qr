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

        val title = this.getString(R.string.generate)
        this.supportActionBar.apply {
            this!!.setDisplayHomeAsUpEnabled(true)
            this.title = title
        }

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
        fm.beginTransaction().replace(R.id.generate_container, GenerateMainFragment()).commit()


    }

    fun doNextFragment(view: View) {
        val id = view.id
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
            .setCustomAnimations(R.animator.slide_right_in, R.animator.slide_left_out)
        when (id) {
            R.id.generate_text ->
                ft.replace(R.id.generate_container, textFragment).addToBackStack(null).commit()
            R.id.generate_url -> ft.replace(R.id.generate_container, urlFragment)
                .addToBackStack(null).commit()
            R.id.generate_wifi -> ft
                .replace(R.id.generate_container, wifiFragment).addToBackStack(null).commit()
            R.id.generate_contact -> ft
                .replace(R.id.generate_container, contactFragment).addToBackStack(null).commit()
            R.id.generate_app -> ft.replace(R.id.generate_container, appFragment)
                .addToBackStack(null).commit()
            R.id.generate_email -> ft
                .replace(R.id.generate_container, emailFragment).addToBackStack(null).commit()
            R.id.generate_phone -> ft
                .replace(R.id.generate_container, phoneFragment).addToBackStack(null).commit()
            R.id.generate_event -> ft
                .replace(R.id.generate_container, eventFragment).addToBackStack(null).commit()
            R.id.generate_sms -> ft.replace(R.id.generate_container, smsFragment)
                .addToBackStack(null).commit()
            R.id.generate_myqr -> ft
                .replace(R.id.generate_container, myqrFragment).addToBackStack(null).commit()
            R.id.generate_product -> ft
                .replace(R.id.generate_container, productFragment).addToBackStack(null).commit()
            R.id.generate_geo -> ft.replace(R.id.generate_container, geoFragment)
                .addToBackStack(null).commit()
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (supportFragmentManager.backStackEntryCount == 0)
                    finish()
                else{
                    supportFragmentManager.popBackStack()
                }


                true
            }
            else -> false
        }
        return super.onOptionsItemSelected(item)
    }
}