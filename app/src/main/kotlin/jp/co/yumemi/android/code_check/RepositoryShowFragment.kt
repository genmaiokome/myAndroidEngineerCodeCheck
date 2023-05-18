/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import jp.co.yumemi.android.code_check.TopActivity.Companion.lastSearchDate
import jp.co.yumemi.android.code_check.databinding.FragmentRepositoryShowBinding

/*
 * RepositoryShowFragmentは、リポジトリの詳細を表示するためのフラグメントです。
 */
class RepositoryShowFragment : Fragment(R.layout.fragment_repository_show) {

    private val args: RepositoryShowFragmentArgs by navArgs()

    private var binding: FragmentRepositoryShowBinding? = null
    private val _binding get() = binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        Log.d("検索した日時", lastSearchDate.toString())

        binding = FragmentRepositoryShowBinding.bind(view)

        val repositoryItem: RepositoryItem = args.repositoryItem
        setupViews(repositoryItem)
    }

    private fun setupViews(item: RepositoryItem) {
        //表示内容を設定
        with(_binding) {
            ownerIconView.load(item.ownerIconUrl)
            nameView.text = item.name
            languageView.text = getString(R.string.txtWrittenLanguage, item.language)
            starsView.text = getString(R.string.txtStars, item.stargazersCount)
            watchersView.text = getString(R.string.txtWatchers, item.watchersCount)
            forksView.text = getString(R.string.txtForks, item.forksCount)
            openIssuesView.text = getString(R.string.txtOpenIssues, item.openIssuesCount)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //Viewが破棄された際に、メモリを解放する
        binding = null
    }
}