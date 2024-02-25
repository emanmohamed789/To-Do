package com.example.todoapp.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentSettingsBinding
import java.util.Locale

@Suppress("DEPRECATION")
class SettingsFragment : Fragment() {

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

        val languages = resources.getStringArray(R.array.languages)
        val languageAdapter =
            ArrayAdapter(requireContext(), R.layout.drop_down_item, languages)
        binding.autoCompleteTVLanguages.setAdapter(languageAdapter)

        val modes = resources.getStringArray(R.array.modes)
        val modeAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item, modes)
        binding.autoCompleteTVModes.setAdapter(modeAdapter)

        binding.autoCompleteTVLanguages.setOnItemClickListener { _, _, position, _ ->
            val selectedLanguage = languages[position]
            changeLanguage(selectedLanguage)
        }

        binding.autoCompleteTVModes.setOnItemClickListener { _, _, position, _ ->
            val selectedMode = modes[position]
            changeMode(selectedMode)
        }
    }

    private fun changeLanguage(selectedLanguage: String?) {
        when (selectedLanguage) {
            "Arabic", "العربيه" -> setLanguage("ar")
            "English", "الانجليزيه" -> setLanguage("en")
        }
    }

    private fun changeMode(mode: String) {
        when (mode) {
            "Light", "الوضع الضوء" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            "Dark", "الوضع المظلم" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    private fun setLanguage(langCode: String) {
        val resources = resources
        val configuration = Configuration(resources.configuration)
        val locale = Locale(langCode)
        configuration.setLocale(locale)

        val displayMetrics = resources.displayMetrics

        resources.updateConfiguration(configuration, displayMetrics)

        val editor = requireActivity().getPreferences(0).edit()
        editor.putString("selected_language", langCode)
        editor.apply()

        requireActivity().recreate()
    }
}