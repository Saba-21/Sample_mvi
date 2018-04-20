package com.example.saba.sampleKotlin.mvi.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.example.saba.sampleKotlin.mvi.presenter.BasePresenter
import com.example.saba.sampleKotlin.mvi.view.BaseView
import dagger.Lazy
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

abstract class BaseActivity<ViewState: Any, P : BasePresenter<ViewState, out BaseView<ViewState>>>:
        AppCompatActivity(), HasSupportFragmentInjector {

    private var presenter: P? = null
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        renderView(savedInstanceState)
        AndroidInjection.inject(this)
    }

    override fun onDestroy() {
        presenter!!.detach(isFinishing)
        compositeDisposable.clear()
        super.onDestroy()
    }

    @Inject
    fun setPresenter(lazy: Lazy<P>) {
        if (lastNonConfigurationInstance == null) presenter = lazy.get()
        onPresenterReady(presenter!!)
    }

    fun subscribe(continuousViewStateObservable: Observable<ViewState>,
                  viewStateObservable: Observable<ViewState>) {
        compositeDisposable.add(viewStateObservable.subscribe(this::reflectState))
        compositeDisposable.add(continuousViewStateObservable.subscribe(this::reflectState))
    }

    protected abstract fun reflectState(state: ViewState)

    protected abstract fun onPresenterReady(presenter: P)

    protected abstract fun renderView(savedInstanceState: Bundle?)

    @Inject lateinit var mDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = mDispatchingAndroidInjector

}
