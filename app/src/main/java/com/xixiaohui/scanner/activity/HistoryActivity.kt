package com.xixiaohui.scanner.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.xixiaohui.scanner.MainActivity
import com.xixiaohui.scanner.R
import com.xixiaohui.scanner.databinding.ActivityFavoriteBinding
import com.xixiaohui.scanner.databinding.ActivityHistoryBinding
import com.xixiaohui.scanner.fragment.HistoryFragment
import com.xixiaohui.scanner.fragment.MAIN
import com.xixiaohui.scanner.fragment.MainFragment
import com.xixiaohui.scanner.resultList

const val HistoryActivityTag = "HistoryActivityTag"
class HistoryActivity : AppCompatActivity() {

    lateinit var binding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_history)

        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = this.getString(R.string.history)

        this.supportActionBar.apply {
            this!!.setDisplayHomeAsUpEnabled(true)
            this.title = "$title"
        }

        val trans = supportFragmentManager.beginTransaction()
        trans.replace(R.id.history_fragment, HistoryFragment.newInstance(1, resultList,HistoryActivityTag), "HISTORY")
        trans.commit()

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