package com.example.myapplication.ui.sell

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.myapplication.BaseApplication
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentSellBinding
import com.example.myapplication.model.CarLogo
import com.example.myapplication.ui.addCar.UriImage
import com.example.myapplication.ui.addCar.kw
import com.example.myapplication.ui.addCar.price
import com.google.android.material.bottomnavigation.BottomNavigationView

class SellFragment : Fragment() {

    private val sellViewModel: SellViewModel by viewModels {
        SellViewModelFactory(
            (activity?.application as BaseApplication).database.CarDao()
        )
    }

    private var _binding: FragmentSellBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSellBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            addCarFab.setOnClickListener {
                val action = SellFragmentDirections
                    .actionNavigationSellToAddNewCarFragment()
                findNavController().navigate(action)
                UriImage = null
                price = 0.0
                kw = 0
            }
        }
        val adapter = SellListAdapter ({ car ->
            val action = SellFragmentDirections
                .actionNavigationSellToDetailCarFragment(car.id, true)
            findNavController().navigate(action)
        }, sellViewModel.carLogos)
        val observer = Observer<List<CarLogo>> {
            binding.recyclerView.adapter = adapter
        }
        sellViewModel.carLogos.observe(viewLifecycleOwner,observer)
        sellViewModel.allCars.observe(this.viewLifecycleOwner) { carSelected ->
            carSelected.let {
                adapter.submitList(it)
            }
        }
        binding.apply {
            recyclerView.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}