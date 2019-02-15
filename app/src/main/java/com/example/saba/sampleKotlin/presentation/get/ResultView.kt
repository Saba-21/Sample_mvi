package com.example.saba.sampleKotlin.presentation.get

import com.example.saba.sampleKotlin.domain.model.apiModels.RepoModel
import com.example.saba.sampleKotlin.mvi.view.BaseView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

interface ResultView: BaseView<ResultViewState> {

    fun onAddingNavigatorClickIntent(): Observable<Unit>

    fun onInitialIntent(): Observable<Unit>

    fun onDropClickIntent(): PublishSubject<RepoModel>


}
