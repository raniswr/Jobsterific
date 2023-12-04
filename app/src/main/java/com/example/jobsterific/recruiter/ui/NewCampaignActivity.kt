package com.example.jobsterific.recruiter.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.jobsterific.databinding.ActivityNewCampaignBinding
import com.google.android.material.textfield.TextInputLayout
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale


class NewCampaignActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewCampaignBinding
    private var isFormatting: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewCampaignBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar: androidx.appcompat.widget.Toolbar =
            findViewById(com.example.jobsterific.R.id.toolbar)

        // Set the toolbar as the action bar
        setSupportActionBar(toolbar)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.campaignEnd.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                addSlashAutomatically(s)
                validateDateFormat(s)
            }
        })
        binding.campaignStart.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                addSlashAutomatically(s)
                validateDateFormat2(s)
            }
        })


        binding.description.post {
            binding.description.scrollTo(0, binding.description.bottom)
        }

        binding.buttonSave.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this@NewCampaignActivity).apply {
                setTitle("Success")
                setMessage("Your Upload has been successful")

                setPositiveButton("Back") { _, _ ->
                    val intent = Intent(context, DashboardRecruiterActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
            }.create()

            alertDialog.setOnShowListener {
                val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                positiveButton?.let {
                    it.setTextColor(
                        ContextCompat.getColor(
                            this@NewCampaignActivity,
                            com.example.jobsterific.R.color.black
                        )
                    )
                }
            }
            alertDialog.show()
        }
    }

    private fun addSlashAutomatically(s: Editable?) {
        if (s != null && s.length == 2 && s[1] != '/' && !s.toString().endsWith("/")) {
            s.insert(2, "/")
        } else if (s != null && s.length == 5 && s[4] != '/' && !s.toString().endsWith("/")) {
            s.insert(5, "/")
        } else if (s != null && s.length == 5 && s[4] == '/' && s.endsWith("/")) {
            // Handle deletion of the last '/'
            s.delete(4, 5)
        } else if (s != null && s.length == 2 && s[1] == '/' && s.endsWith("/")) {
            // Handle deletion of the last '/'
            s.delete(1, 2)
        }
    }

    private fun validateDateFormat(s: Editable?) {
        try {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            dateFormat.isLenient = false
            val date = dateFormat.parse(s.toString())

            binding.LayoutCampaignEnd.endIconMode = TextInputLayout.END_ICON_CUSTOM
            binding.campaignEnd.error = null
        } catch (e: ParseException) {
            binding.campaignEnd.error = "Invalid date format use dd/mm/yyyy"
            binding.LayoutCampaignEnd.endIconMode = TextInputLayout.END_ICON_NONE
        }
    }

    private fun validateDateFormat2(s: Editable?) {
        try {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            dateFormat.isLenient = false
            val date = dateFormat.parse(s.toString())

            binding.LayoutCampaignStart.endIconMode = TextInputLayout.END_ICON_CUSTOM
            binding.campaignStart.error = null
        } catch (e: ParseException) {
            binding.campaignStart.error = "Invalid date format use dd/mm/yyyy"
            binding.LayoutCampaignStart.endIconMode = TextInputLayout.END_ICON_NONE
        }
    }
}

