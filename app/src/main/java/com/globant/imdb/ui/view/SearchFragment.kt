package com.globant.imdb.ui.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.globant.imdb.databinding.FragmentSearchBinding
import com.globant.imdb.ui.view.adapters.MovieResultAdapter
import com.globant.imdb.ui.view.adapters.MovieResultViewHolder
import com.globant.imdb.ui.viewmodel.SearchMovieViewModel
import com.squareup.picasso.Picasso

class SearchFragment : Fragment(), MovieResultAdapter.ImageRenderListener, MovieResultViewHolder.MovieResultListener {

    private val searchMovieViewModel: SearchMovieViewModel by viewModels()

    private val binding: FragmentSearchBinding by lazy {
        FragmentSearchBinding.inflate(layoutInflater)
    }

    private lateinit var moviesResultAdapter: MovieResultAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLiveData()
        setupRecyclerView()
        setupWatcher()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupLiveData(){
        searchMovieViewModel.resultMovies.observe(viewLifecycleOwner){
            moviesResultAdapter.notifyDataSetChanged()
        }
    }

    private fun setupRecyclerView(){
        moviesResultAdapter = MovieResultAdapter()
        moviesResultAdapter.movieList = searchMovieViewModel.resultMovies
        moviesResultAdapter.moviesListener = this
        searchMovieViewModel.adapter = moviesResultAdapter
        with(binding.recyclerMoviesResult){
            adapter = moviesResultAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun setupWatcher(){
        val watcher = object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
            override fun afterTextChanged(s: Editable?) {
                val query = binding.searchTextField.text.toString()
                if(query.isNotBlank()){
                    searchMovieViewModel.search(query)
                }else{
                    searchMovieViewModel.resultMovies.postValue(emptyList())
                }
            }
        }
        binding.searchTextField.addTextChangedListener(watcher)
    }

    override fun renderImage(url: String, image: ImageView) {
       Picasso.with(requireContext())
           .load(url)
           .fit()
           .centerCrop()
           .into(image)
    }

    override fun showDetails(id: Int) {
        val action = SearchFragmentDirections.actionSearchFragmentToMovieFragment(id)
        findNavController().navigate(action)
    }
}