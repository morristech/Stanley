package fr.xgouchet.packageexplorer.applist

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import fr.xgouchet.packageexplorer.R
import fr.xgouchet.packageexplorer.about.AboutActivity
import fr.xgouchet.packageexplorer.applist.sort.AppSort
import fr.xgouchet.packageexplorer.ui.adapter.BaseAdapter
import fr.xgouchet.packageexplorer.ui.mvp.list.BaseListFragment


class AppListFragment : BaseListFragment<AppViewModel, AppListPresenter>() {

    override val adapter: BaseAdapter<AppViewModel> = AppAdapter(this)
    override val isFabVisible: Boolean = false
    override val fabIconOverride: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.app_list, menu)

        menu?.findItem(R.id.action_search)?.let {
            val searchView = it.actionView as SearchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = false

                override fun onQueryTextChange(newText: String?): Boolean {
                    presenter.setFilter(newText ?: "")
                    return true
                }
            })
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        val systemAppsVisible = presenter.areSystemAppsVisible()
        menu?.apply {
            findItem(R.id.hide_system_apps).isVisible = systemAppsVisible
            findItem(R.id.show_system_apps).isVisible = !systemAppsVisible
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.sort_by_title -> presenter.setSort(AppSort.TITLE)
            R.id.sort_by_package_name -> presenter.setSort(AppSort.PACKAGE_NAME)
            R.id.sort_by_install_time -> presenter.setSort(AppSort.INSTALL_TIME)
            R.id.sort_by_update_time -> presenter.setSort(AppSort.UPDATE_TIME)
            R.id.hide_system_apps -> presenter.setSystemAppsVisible(false)
            R.id.show_system_apps -> presenter.setSystemAppsVisible(true)
            R.id.about -> {
                startActivity(Intent(activity, AboutActivity::class.java))
            }
            R.id.licenses -> {
                OssLicensesMenuActivity.setActivityTitle(getString(R.string.activity_title_licenses))
                startActivity(Intent(activity, OssLicensesMenuActivity::class.java))
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }


}