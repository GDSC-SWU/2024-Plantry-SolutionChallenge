package com.plantry.coreui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.plantry.presentation.MainActivity


// 의존성 분리를 위한 interface 선언
abstract class BindingFragment<T : ViewDataBinding>(
    @LayoutRes private val layoutRes: Int,
) : Fragment() {
    private var _binding: T? = null
    protected val binding
        get() = requireNotNull(_binding) {

        }

    protected open var bnvVisibility = View.VISIBLE

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setBnvVisibility()
    }

    protected abstract fun initView()

    private fun setBnvVisibility(){
        if (activity is MainActivity) {
            val  mainActivity = activity as MainActivity
            mainActivity.setBottomNavigationVisibility(bnvVisibility)
        }
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}