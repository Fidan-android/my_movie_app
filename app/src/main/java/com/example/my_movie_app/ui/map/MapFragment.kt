package com.example.my_movie_app.ui.map

import android.os.Bundle
import android.os.StrictMode
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.my_movie_app.api.ApiManager
import com.example.my_movie_app.api.IInternetConnected
import com.example.my_movie_app.databinding.FragmentMapBinding
import com.google.android.material.snackbar.Snackbar
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import java.io.File


class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private val viewModel by lazy {
        ViewModelProvider(this)[MapViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())
        Configuration.getInstance().apply {
            load(requireContext(), PreferenceManager.getDefaultSharedPreferences(requireContext()))
            userAgentValue = requireActivity().packageName
            osmdroidBasePath = File(requireActivity().cacheDir.absolutePath, "osmdroid")
            osmdroidTileCache = File(osmdroidBasePath.absolutePath, "tile")
        }
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapView.apply {
            setUseDataConnection(true)
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            controller.setZoom(13.0)
            controller.animateTo(GeoPoint(55.030204, 82.920430))
        }
    }

    override fun onStart() {
        ApiManager.setConnectCallback(requireContext(), object : IInternetConnected {
            override fun onConnect() {
            }

            override fun onLost() {
                Snackbar.make(
                    binding.root,
                    "Отсутствует интернет соединение",
                    Snackbar.LENGTH_SHORT
                ).show()
            }

        })
        super.onStart()
        viewModel.onGetErrorMessage().observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
        viewModel.onGetData().observe(viewLifecycleOwner) {
            it.map { cinema ->
                binding.mapView.overlays.add(
                    Marker(binding.mapView).apply {
                        position = GeoPoint(cinema.latitude!!, cinema.longitude!!)
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    }
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}