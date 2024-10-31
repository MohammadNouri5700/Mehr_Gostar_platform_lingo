package com.android.platform.ui.report

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.android.platform.PlatformApplication
import com.android.platform.R
import com.android.platform.databinding.FragmentReportBinding
import com.android.platform.utils.extension.getLastSevenDays
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class ReportFragment : Fragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    private lateinit var viewModel: ReportViewModel

    private lateinit var binding: FragmentReportBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_report, container, false)
        (requireActivity().application as PlatformApplication).appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[ReportViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.liveDayReport.observe(viewLifecycleOwner, Observer { report ->
            when(report.third){
                0->{
                    binding.prsix.setProgress(report.first)
                    lifecycleScope.launch {
                        withContext(Dispatchers.Main){
                            binding.inter7.text = report.second
                        }
                    }
                    viewModel.getReportDayOftWeek(1)
                }
                1->{
                    binding.prfive.setProgress(report.first)
                    lifecycleScope.launch {
                        withContext(Dispatchers.Main){
                            binding.inter6.text = report.second
                        }
                    }
                    viewModel.getReportDayOftWeek(2)
                }
                2->{
                    binding.prfour.setProgress(report.first)
                    lifecycleScope.launch {
                        withContext(Dispatchers.Main){
                            binding.inter5.text = report.second
                        }
                    }
                    viewModel.getReportDayOftWeek(3)
                }
                3->{
                    binding.prthree.setProgress(report.first)
                    lifecycleScope.launch {
                        withContext(Dispatchers.Main){
                            binding.inter4.text = report.second
                        }
                    }
                    viewModel.getReportDayOftWeek(4)
                }
                4->{
                    binding.prtwo.setProgress(report.first)
                    lifecycleScope.launch {
                        withContext(Dispatchers.Main){
                            binding.inter3.text = report.second
                        }
                    }
                    viewModel.getReportDayOftWeek(5)
                }
                5->{
                    binding.prone.setProgress(report.first)
                    lifecycleScope.launch {
                        withContext(Dispatchers.Main){
                            binding.inter2.text = report.second
                        }
                    }
                }

            }
        })

        lifecycleScope.launch {
            val data = withContext(Dispatchers.IO){
                viewModel.getReportTotalWeek()
            }
            withContext(Dispatchers.Main){
                binding.txttotalweek.text=data
            }
        }

        viewModel.getReportDayOftWeek(0)

    }

    override fun onResume() {
        super.onResume()
    }


}