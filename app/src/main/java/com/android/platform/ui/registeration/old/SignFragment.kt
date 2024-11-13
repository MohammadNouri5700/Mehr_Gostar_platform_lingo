package com.android.platform.ui.registeration.old

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.platform.PlatformApplication
import com.android.platform.R
import com.android.platform.databinding.FragmentSignBinding
import com.android.platform.di.factory.CallQueueManager
import com.android.platform.utils.extension.getAndroidId
import com.android.platform.utils.extension.isValidPhoneNumber
import javax.inject.Inject


class SignFragment @Inject constructor() : Fragment() {

    @Inject
    lateinit var call: CallQueueManager


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    private lateinit var viewModel: SignViewModel

    private lateinit var binding: FragmentSignBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign, container, false)
        (requireActivity().application as PlatformApplication).appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[SignViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val motionLayout = view.findViewById<MotionLayout>(R.id.constraintLayout7)

        binding.editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                motionLayout.setTransition(R.id.start, R.id.end)
                motionLayout.transitionToEnd()
            }
        }
        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().length >= 10 && isValidPhoneNumber(s.toString())) {
                    binding.editText.isEnabled = false

                    call.enqueueIoTask {
                        viewModel.registerUser(s.toString(), getAndroidId(requireContext()))
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })


    }

    override fun onResume() {
        super.onResume()
    }


}