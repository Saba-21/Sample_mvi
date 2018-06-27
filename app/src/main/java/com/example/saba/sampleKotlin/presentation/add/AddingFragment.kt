package com.example.saba.sampleKotlin.presentation.add

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.saba.sampleKotlin.R
import com.example.saba.sampleKotlin.adapter.RepoListRenderer
import com.example.saba.sampleKotlin.domain.model.apiModels.RepoModel
import com.example.saba.sampleKotlin.mvi.fragment.BaseFragment
import com.jakewharton.rxbinding2.view.clicks
import com.zuluft.autoadapter.SortedAutoAdapter
import com.zuluft.generated.AutoAdapterFactory
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_adding.*

class AddingFragment : BaseFragment<AddingViewState, AddingPresenter>(), AddingView {

    private val listAdapter: SortedAutoAdapter = AutoAdapterFactory.createSortedAutoAdapter()

    companion object {
        @JvmStatic
        fun newInstance(): AddingFragment = AddingFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_adding, container, false)
    }

    override fun reflectState(state: AddingViewState) {
        when (state.state) {
            ADDING_VIEW_INITIAL_STATE -> initialStateActions()
            ADDING_VIEW_LOADING_STATE -> loadingStateActions()
            ADDING_VIEW_SUCCESS_STATE -> successStateActions(state)
            ADDING_VIEW_ERROR_STATE -> errorStateActions(state)
        }
    }

    private fun initialStateActions() {
        rvGlobalRepos.layoutManager = LinearLayoutManager(context)
        rvGlobalRepos.adapter = listAdapter
    }

    private fun loadingStateActions() {
        listAdapter.removeAll()
        startLoadingAnimation()
    }

    private fun successStateActions(viewState: AddingViewState) {
        listAdapter.updateAll(viewState.response?.map { RepoListRenderer(it) }!!)
        stopLoadingAnimation()
    }

    private fun errorStateActions(viewState: AddingViewState) {
        tv_adding_error.visibility = View.VISIBLE
        tv_adding_error.text = viewState.exception
        stopLoadingAnimation()
    }

    private fun startLoadingAnimation() {
        animLoading.playAnimation()
        animLoading.visibility = View.VISIBLE
    }

    private fun stopLoadingAnimation() {
        animLoading.cancelAnimation()
        animLoading.visibility = View.GONE
    }

    override fun renderView(view: View?, savedInstanceState: Bundle?) {}

    override fun onPresenterReady(presenter: AddingPresenter) {
        presenter.attach(this)
    }

    override fun onResultNavigatorClickIntent():
            Observable<Unit> = butDrawResult.clicks()

    override fun onSearchClickIntent():
            Observable<String> = butSearch.clicks().map { tvUsername.text.toString().trim() }

    override fun onAddClickIntent():
            Observable<RepoModel> = listAdapter
            .clicks(RepoListRenderer::class.java)
            .map { itemInfo -> itemInfo.renderer }
            .map { renderer -> renderer.repoModel }
}
