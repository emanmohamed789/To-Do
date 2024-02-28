package com.example.todoapp.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentSettingsBinding
import java.util.Locale

class SettingsFragment : Fragment() {

    //private var settingsSP: SharedPreferences? = null
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        settingsSP = activity?.getSharedPreferences("com.example.todoapp.settings",Context.MODE_PRIVATE)
//
//        checkAndSetSettingsPreferences()

        setupExposedDropDownMenus()

        binding.autoCompleteTVModes.doOnTextChanged { text, _, _, _ ->
            activeDarkMode(
                when (text.toString()) {
                    resources.getString(R.string.dark) -> {
                        //settingsSP!!.edit().putBoolean("MODE",true).apply()
                        true
                    }

                    else -> {
//                        settingsSP!!.edit().putBoolean("MODE",false).apply()
                        false
                    }
                }
            )
        }

        binding.autoCompleteTVLanguages.doOnTextChanged { text, _, _, _ ->
            activateArabicLang(
                when (text.toString()) {
                    resources.getString(R.string.arabic) -> {
                        true
                    }

                    else -> {
                        false
                    }
                }
            )
        }
    }

//    private fun checkAndSetSettingsPreferences() {
//        activeDarkMode(settingsSP?.getBoolean("com.example.todoapp.settings",false) ?: false)
//    }

    private fun activateArabicLang(activate: Boolean) {
        setLocale(if (activate) "ar" else "en")
        activity?.let { ActivityCompat.recreate(it) }
    }


    private fun activeDarkMode(activate: Boolean) {
        //activity?.let { ActivityCompat.recreate(it)}
        AppCompatDelegate.setDefaultNightMode(
            if (activate) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
        activity?.let { ActivityCompat.recreate(it) }
    }

    private fun populateMenusWithOptions() {
        binding.autoCompleteTVLanguages.setText(
            when (isArabicLangActive()) {
                true -> resources.getString(R.string.arabic)
                false -> resources.getString(R.string.english)
            }, false
        )

        binding.autoCompleteTVModes.setText(
            when (isDarkModeActive()) {
                true -> resources.getString(R.string.dark)
                false -> resources.getString(R.string.light)
            }, false
        )
    }

    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        activity?.let {
            @Suppress("DEPRECATION")
            requireActivity().baseContext.resources.updateConfiguration(
                config,
                requireActivity().baseContext.resources.displayMetrics
            )
        }
    }

    private fun isDarkModeActive(): Boolean {

        return when (
            context?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
    }


    //RequiresApi(Build.VERSION_CODES.N)
    private fun isArabicLangActive(): Boolean {
        return when (context?.resources?.configuration?.locales!![0].language) {
            "ar" -> true
            else -> false
        }
    }


    //RequiresApi(Build.VERSION_CODES.N)
    private fun setupExposedDropDownMenus() {

        val langItems = listOf(
            resources.getString(R.string.english),
            resources.getString(R.string.arabic)
        )
        val langAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item, langItems)
        binding.autoCompleteTVLanguages.setAdapter(langAdapter)

        val modeItems = listOf(
            resources.getString(R.string.light),
            resources.getString(R.string.dark)
        )
        val modeAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item, modeItems)
        binding.autoCompleteTVModes.setAdapter(modeAdapter)


        populateMenusWithOptions()

    }
}

