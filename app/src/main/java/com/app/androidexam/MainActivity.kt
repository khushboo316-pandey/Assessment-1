package com.app.androidexam

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var listAdapter: ListAdapter
    private var originalDataList = listOf("Apple", "Banana", "Orange", "Blueberry", "Mango")
    private var filteredDataList = originalDataList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.listRecyclerView)
        val bannerViewPager = findViewById<ViewPager2>(R.id.bannerViewPager)
        val fab = findViewById<FloatingActionButton>(R.id.fab)

        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.queryHint = "Search here..."
        searchView.isIconified = false
        searchView.clearFocus()
        val searchEditText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchEditText.setHintTextColor(ContextCompat.getColor(this, R.color.gray)) // or any color you want

        // Initialize the adapter with the data list
        listAdapter = ListAdapter(filteredDataList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = listAdapter

        // Handle the search functionality
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Optionally handle submit if needed
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Filter the list based on query
                filteredDataList = if (newText.isNullOrEmpty()) {
                    originalDataList
                } else {
                    originalDataList.filter { it.contains(newText, ignoreCase = true) }
                }
                // Update the adapter with the filtered data
                listAdapter.updateData(filteredDataList)
                return true
            }
        })

        // Floating Action Button action (optional)
        fab.setOnClickListener {
            showStatisticsBottomSheet(filteredDataList)
        }

        // Set up the banner with ViewPager2 (you already have this part)
        val imageList = listOf(R.drawable.image1, R.drawable.image2, R.drawable.image3)
        val bannerAdapter = BannerAdapter(imageList)
        bannerViewPager.adapter = bannerAdapter
        setupIndicatorDots(imageList.size, 0)

        bannerViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setupIndicatorDots(imageList.size, position)
            }
        })


    }

    // Method to update the indicator dots (already present in your code)
    private fun setupIndicatorDots(count: Int, selectedPosition: Int) {
        val dotsLayout = findViewById<LinearLayout>(R.id.dotsIndicator)
        dotsLayout.removeAllViews()

        for (i in 0 until count) {
            val dot = ImageView(this)
            dot.setImageResource(
                if (i == selectedPosition) R.drawable.active_dot
                else R.drawable.inactive_dot
            )

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(8, 0, 8, 0)
            }

            dotsLayout.addView(dot, params)
        }

    }

    fun showStatisticsBottomSheet(items: List<String>) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_statistics, null)

        val itemCountTextView = view.findViewById<TextView>(R.id.itemCountTextView)
        val topCharsTextView = view.findViewById<TextView>(R.id.topCharsTextView)

        // Set total items
        itemCountTextView.text = "List 1 (${items.size} items)"

        // Count character frequencies
        val charCounts = mutableMapOf<Char, Int>()
        items.joinToString("").forEach { char ->
            if (char.isLetter()) {
                val lowerChar = char.lowercaseChar()
                charCounts[lowerChar] = charCounts.getOrDefault(lowerChar, 0) + 1
            }
        }

        // Get top 3 characters
        val top3 = charCounts.entries.sortedByDescending { it.value }.take(3)

        val resultText = top3.joinToString("\n") { "${it.key} = ${it.value}" }
        topCharsTextView.text = resultText

        dialog.setContentView(view)
        dialog.show()
    }

}
