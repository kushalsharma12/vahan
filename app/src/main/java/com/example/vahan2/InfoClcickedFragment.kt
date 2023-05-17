package com.example.vahan2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.vahan2.databinding.FragmentInfoClcickedBinding

class InfoClcickedFragment : Fragment() {

    private var _binding: FragmentInfoClcickedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentInfoClcickedBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val iName = arguments?.getString("iName")
        val iDesp = arguments?.getString("iDesp")
        val iPrice = arguments?.getString("iPrice")
        val iPos = arguments?.getString("iPos")
        val iImage = arguments?.getString("iImage")

        Glide.with(binding.infoClickedImg.context).load(iImage)
            .transform(CenterCrop(), RoundedCorners(12))
            .into(binding.infoClickedImg)

        binding.infoClickedNametv.text = iName.toString()

        binding.infoClickedPosTv.text = buildString {
        append(getString(R.string.positionText))
        append(iPos.toString())
    }

        binding.infoClickedPricetv.text = buildString {
            append(getString(R.string.price))
            append(iPrice.toString())
        }
        binding.infoClickedDesp.text = buildString {
            append("WORKING-")
            append(iDesp.toString())
        }



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}