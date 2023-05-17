package com.example.vahan2

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.icu.text.IDNA.Info
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vahan2.databinding.FragmentInformationBinding
import com.example.vahan2.models.info
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class InformationFragment : Fragment(), iInfoAdapter {

    private var _binding: FragmentInformationBinding? = null
    private val binding get() = _binding!!


    private lateinit var adapter: InformationAdapter

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentInformationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.progressInfo.visibility = View.VISIBLE

        setupRecyclerView()



        return root

    }

    private fun setupRecyclerView() {
        val db = FirebaseFirestore.getInstance()
        val infoCollections = db.collection("componentsInformation")
        val query = infoCollections.orderBy("itemName", Query.Direction.DESCENDING)
        val recylerViewOptions =
            FirestoreRecyclerOptions.Builder<info>().setQuery(query, info::class.java).build()

        adapter = InformationAdapter(recylerViewOptions, this)

        binding.informationRecyclerView.adapter = adapter
        binding.informationRecyclerView.layoutManager = LinearLayoutManager(this.context)

        binding.progressInfo.visibility = View.GONE


    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onContainerClicked(
        view: View,
        itemName: String,
        itemDescription: String,
        itemPricing: String,
        itemPosition: String,
        itemImage: String
    ) {

        val bundle = bundleOf(
            "iName" to itemName,
            "iDesp" to itemDescription,
            "iPrice" to itemPricing,
            "iPos" to itemPosition,
            "iImage" to itemImage
        )
        view.findNavController().navigate(R.id.action_navigation_information_to_infoClcickedFragment,bundle)
    }

}