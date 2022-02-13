package com.vasha.workhrstracker.features.scanner

import android.animation.Animator
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.vasha.workhrstracker.R
import com.vasha.workhrstracker.databinding.ScannerDialogBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by ivasil on 1/26/2022
 */

class ScannerDialog : DialogFragment() {

    private val scannerDialogArgs: ScannerDialogArgs by navArgs()
    private val binding by viewBinding(ScannerDialogBinding::bind)
    private val viewModel: ScannerViewModel by sharedViewModel()

     override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
     ): View? {
         return inflater.inflate(R.layout.scanner_dialog, container, false);
     }

 override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
  super.onViewCreated(view, savedInstanceState)

     setFullScreen()
     if(scannerDialogArgs.title == TitleType.APPROVED) {
         binding.animationView.setAnimation(R.raw.success_animiation)
         binding.errorMessage.text = scannerDialogArgs.message
         binding.close.isVisible = false

         binding.animationView.addAnimatorListener(object : Animator.AnimatorListener {
             override fun onAnimationStart(p0: Animator?) {
                 //do nothing
             }

             override fun onAnimationEnd(p0: Animator?) {
                 findNavController().navigateUp()
                 viewModel.onCloseButtonClicked()
             }

             override fun onAnimationCancel(p0: Animator?) {
                 //do nothing
             }

             override fun onAnimationRepeat(p0: Animator?) {
                 //do nothing
             }
         })
     }
     else {
         binding.animationView.setAnimation(R.raw.failure_animation)
         binding.animationView.removeAllAnimatorListeners()
         binding.errorMessage.text = scannerDialogArgs.message
         binding.close.isVisible = true
     }
    binding.close.setOnClickListener {
        findNavController().navigateUp()
        viewModel.onCloseButtonClicked()
    }
 }
    private fun DialogFragment.setFullScreen() {
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}