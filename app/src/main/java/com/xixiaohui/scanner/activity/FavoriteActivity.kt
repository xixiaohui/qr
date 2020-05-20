package com.xixiaohui.scanner.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.xixiaohui.scanner.R
import com.xixiaohui.scanner.databinding.ActivityFavoriteBinding

import com.xixiaohui.scanner.fragment.HistoryFragment
import com.xixiaohui.scanner.resultList


const val FavoriteActivityTag = "FavoriteActivityTag"
class FavoriteActivity : AppCompatActivity() {

    lateinit var binding:ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_favorite)

        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = this.getString(R.string.favorite)
        this.supportActionBar.apply {
            this!!.setDisplayHomeAsUpEnabled(true)
            this.title = title
        }

        val trans = supportFragmentManager.beginTransaction()
        trans.replace(R.id.favorite_fragment, HistoryFragment.newInstance(1, resultList,FavoriteActivityTag), "FAVORITE")
        trans.commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                finish()
                true
            }
            else->false
        }
        return super.onOptionsItemSelected(item)
    }
}