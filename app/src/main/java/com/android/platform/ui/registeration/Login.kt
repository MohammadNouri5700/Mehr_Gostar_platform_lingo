package com.android.platform.ui.registeration

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.platform.PlatformApplication
import com.android.platform.R
import com.android.platform.databinding.ActivityLoginBinding
import com.android.platform.databinding.ActivityMainBinding
import com.android.platform.di.factory.LoadingView
import com.android.platform.ui.home.story.PodcastAdapter
import com.android.platform.ui.home.story.StoryAdapter
import com.android.platform.ui.main.MainActivity
import com.android.platform.ui.main.MainViewModel
import com.android.platform.utils.extension.getAndroidId
import com.android.platform.utils.extension.hideLoading
import com.android.platform.utils.extension.isValidPhoneNumber
import com.android.platform.utils.extension.showLoading
import com.android.platform.utils.extension.showToast
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Login : DaggerAppCompatActivity() {

    private lateinit var loadingView: LoadingView

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: LoginViewModel by viewModels { viewModelFactory }
    private lateinit var binding: ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as PlatformApplication).appComponent.inject(this)
        if (viewModel.isRegistered()){
            viewModel.call.enqueueIoTask {
                viewModel.preferences.getString("PHONE","NULL")?.let { Firebase.crashlytics.setUserId(it) }
            }
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewModel = viewModel
        setContentView(binding.root)



        viewModel.event.observe(this, Observer { data ->
            when (data) {
                "Login" -> {
                    viewModel.call.enqueueMainTask {
                        hideLoading()
                        startActivity(Intent(this,MainActivity::class.java))
                        finish()
                    }
                }
                "Loading"->{
                    if (!isFinishing) {
                        loadingView = showLoading()

                    }
                }
                "Error" -> {
                    viewModel.call.enqueueMainTask {
                        hideLoading()
                        showToast("Error on registration")
                        binding.edtPhone.setText("")
                        binding.edtPhone.isEnabled = true
                    }
                }

            }
        })

    }

    override fun onResume() {
        super.onResume()
        binding.edtPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().length >= 10 && isValidPhoneNumber(s.toString())) {
                    binding.edtPhone.isEnabled = false

                    viewModel.call.enqueueIoTask {
                        viewModel.registerUser(s.toString(), getAndroidId(this@Login))
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

    }
}