package com.vasha.workhrstracker.features.scanner

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
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

    //private val scannerDialogArgs: ScannerDialogArgs by navArgs()
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

     /*binding.scannerMessage.text = scannerDialogArgs.message
     binding.scannerTitle.text = scannerDialogArgs.title.name
     if(scannerDialogArgs.title == TitleType.APPROVED) {
         binding.image.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_success, null))
     }
     else {
         binding.image.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_error, null))
     }*/
    binding.closeButton.setOnClickListener {
        findNavController().navigateUp()
        viewModel.onCloseButtonClicked()
    }
 }
}