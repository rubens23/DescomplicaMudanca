package com.rubens.appajudamudanca

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.Toast
import com.rubens.appajudamudanca.databinding.ActivityTestBottomSheetBinding

class ActivityTestBottomSheet : AppCompatActivity() {

    private lateinit var binding: ActivityTestBottomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBottomSheetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomSheet.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottomsheetlayout)

//        val editLayout = dialog.findViewById<LinearLayout>(R.id.layoutEdit)
//        val shareLayout = dialog.findViewById<LinearLayout>(R.id.layoutShare)
//        val uploadLayout = dialog.findViewById<LinearLayout>(R.id.layoutUpload)
//        val printLayout = dialog.findViewById<LinearLayout>(R.id.layoutPrint)

//        editLayout.setOnClickListener {
//            Toast.makeText(this@ActivityTestBottomSheet, "edit", Toast.LENGTH_SHORT).show()
//        }
//        shareLayout.setOnClickListener {
//            Toast.makeText(this@ActivityTestBottomSheet, "share", Toast.LENGTH_SHORT).show()
//
//        }
//        uploadLayout.setOnClickListener {
//            Toast.makeText(this@ActivityTestBottomSheet, "upload", Toast.LENGTH_SHORT).show()
//
//        }
//        printLayout.setOnClickListener {
//            Toast.makeText(this@ActivityTestBottomSheet, "print", Toast.LENGTH_SHORT).show()
//
//        }

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)


    }
}