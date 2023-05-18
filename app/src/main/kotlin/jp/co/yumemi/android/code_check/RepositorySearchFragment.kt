package jp.co.yumemi.android.code_check

import ProgressBarUtils
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jp.co.yumemi.android.code_check.databinding.FragmentRepositorySearchBinding


class RepositorySearchFragment : Fragment(R.layout.fragment_repository_search) {

    private var binding: FragmentRepositorySearchBinding? = null
    private val _binding get() = binding!!
    private val viewModel: RepositorySearchViewModel by viewModels()

    // 初回起動フラグ
    private var isFirstLaunch = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRepositorySearchBinding.bind(view)
        setupRecyclerView()
        setupSearchInputListener()
        setUpObservation()
    }

    private fun setupRecyclerView() {
        // RecyclerViewの設定
        val layoutManager = LinearLayoutManager(requireContext())

        val adapter = CustomAdapter(object : CustomAdapter.OnItemClickListener {
            override fun itemClick(item: RepositoryItem) {
                gotoRepositoryFragment(item)
            }
        })

        _binding.recyclerView.apply {
            this.layoutManager = layoutManager
            this.isVerticalFadingEdgeEnabled = true
            addItemDecoration(createDividerItemDecoration())
            setAdapter(adapter)
        }
    }

    private fun setUpObservation(){
        observeSearchResults()
        observeIsLoading()
    }

    private fun createDividerItemDecoration(): DividerItemDecoration {
        // リストの区切り線の設定
        return DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
    }

    private fun setupSearchInputListener() {
        //検索ボタンクリック時のリスナーを設定
        _binding.searchInputText.setOnEditorActionListener { editText, action, _ ->
            if (action == EditorInfo.IME_ACTION_SEARCH) {
                if (editText.text.isBlank()) {
                    SnackBarUtils.showSnackBar(requireView(), getString(R.string.strBlankMessage))
                }else{
                    viewModel.performSearch(editText.text.toString())
                }
                true
            } else {
                false
            }
        }
    }

    private fun observeSearchResults() {
        viewModel.searchResults.observe(viewLifecycleOwner) { results ->
            val adapter = _binding.recyclerView.adapter as? CustomAdapter
            adapter?.submitList(results)
            KeyboardUtils.hideKeyboard(requireView())

            if (results.isEmpty()) {
                SnackBarUtils.showSnackBar(requireView(), getString(R.string.strNoResultMessage))
            }
        }
    }

    private fun observeIsLoading() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if(isLoading){
                ProgressBarUtils.showProgressBar(requireContext())
            }else{
                ProgressBarUtils.hideProgressBar()
            }
        }
    }

    private fun gotoRepositoryFragment(repositoryItem: RepositoryItem) {
        // リポジトリの詳細画面に遷移する
        val action = RepositorySearchFragmentDirections
            .repositorySearchFragmentToRepositoryShowFragment(repositoryItem = repositoryItem)
        findNavController().navigate(action)
    }


    override fun onResume() {
        super.onResume()
        if (isFirstLaunch) {
            // 初回起動時にフラグを更新する
            isFirstLaunch = false
        }else {
            val adapter = _binding.recyclerView.adapter as? CustomAdapter
            adapter?.submitList(viewModel.searchResults.value)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //Viewが破棄された際に、メモリを解放する
        binding?.searchInputText?.setOnEditorActionListener(null)
        binding = null
    }

}