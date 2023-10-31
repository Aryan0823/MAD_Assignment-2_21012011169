package com.example.foodcalculator
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

import org.json.JSONObject
import java.io.IOException
import java.io.InputStream


class MainActivity : AppCompatActivity() {
    // Define your UI elements
    private lateinit var categoryDropdown: Spinner
    private lateinit var foodItemDropdown: Spinner
    private lateinit var gramInput: EditText
    private lateinit var calculateButton: Button
    private lateinit var totalButton: Button
    private lateinit var clearButton: Button
    private lateinit var resultsTable: TableLayout
    private lateinit var totalTable: TableLayout

    // Define variables to store calculated results
    private val calculatedResults = mutableListOf<JSONObject>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI elements
        categoryDropdown = findViewById(R.id.categorySpinner)
        foodItemDropdown = findViewById(R.id.foodItemSpinner)
        gramInput = findViewById(R.id.gramInputEditText)
        calculateButton = findViewById(R.id.calculateButton)
        totalButton = findViewById(R.id.totalButton)
        clearButton = findViewById(R.id.clearButton)
        resultsTable = findViewById(R.id.resultsTable)
        totalTable = findViewById(R.id.totalTable)

        // Load data from JSON file
        loadJSONFromAsset("data.json")?.let { database ->
            val categories = database.getJSONObject("Categories")
            val categoryNames = categories.keys().asSequence().toList()

            // Initialize category dropdown
            val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categoryNames)
            categoryDropdown.adapter = categoryAdapter

            // Set a listener to update the food item dropdown when a category is selected
            categoryDropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val categoryName = categoryNames[position]
                    val foodItems = categories.getJSONObject(categoryName).keys().asSequence().toList()

                    // Initialize food item dropdown based on the selected category
                    val foodItemAdapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_dropdown_item, foodItems)
                    foodItemDropdown.adapter = foodItemAdapter
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do nothing
                }
            }
        }





        // Calculate button click event
        calculateButton.setOnClickListener {
            val selectedCategory = categoryDropdown.selectedItem.toString()
            val selectedFoodItem = foodItemDropdown.selectedItem.toString()
            val grams = gramInput.text.toString().toFloatOrNull() ?: 0.0f

            loadJSONFromAsset("data.json")?.let { database ->
                val foodData = database.getJSONObject("Categories").getJSONObject(selectedCategory).getJSONObject(selectedFoodItem)
                val calculatedParameters = JSONObject()

                calculatedParameters.put("Grams", grams)
                calculatedParameters.put("Energy", (foodData.getDouble("Energy") / 100.0) * grams)
                calculatedParameters.put("Protein", (foodData.getDouble("Protein") / 100.0) * grams)
                calculatedParameters.put("Fat", (foodData.getDouble("Fat") / 100.0) * grams)
                calculatedParameters.put("Minerals", (foodData.getDouble("Minerals") / 100.0) * grams)
                calculatedParameters.put("Fibre", (foodData.getDouble("Fibre") / 100.0) * grams)
                calculatedParameters.put("Carbos", (foodData.getDouble("Carbos") / 100.0) * grams)
                calculatedParameters.put("Calcium", (foodData.getDouble("Calcium") / 100.0) * grams)
                calculatedParameters.put("Phosphorous", (foodData.getDouble("Phosphorous") / 100.0) * grams)
                calculatedParameters.put("Iron", (foodData.getDouble("Iron") / 100.0) * grams)

                // Create a new row and add it to the table
                val newRow = TableRow(this)

                // Create TextViews for each cell in the row
                val categoryCell = TextView(this)
                categoryCell.text = selectedCategory
                categoryCell.gravity = Gravity.CENTER

                val foodItemCell = TextView(this)
                foodItemCell.text = selectedFoodItem
                categoryCell.gravity = Gravity.CENTER

                val gramsCell = TextView(this)
                gramsCell.text = grams.toString()
                categoryCell.gravity = Gravity.CENTER

                val energyCell = TextView(this)
                energyCell.text = String.format("%.2f", calculatedParameters.getDouble("Energy"))
                categoryCell.gravity = Gravity.CENTER

                val proteinCell = TextView(this)
                proteinCell.text = String.format("%.2f", calculatedParameters.getDouble("Protein"))
                categoryCell.gravity = Gravity.CENTER

                val fatCell = TextView(this)
                fatCell.text = String.format("%.2f", calculatedParameters.getDouble("Fat"))
                categoryCell.gravity = Gravity.CENTER

                val mineralsCell = TextView(this)
                mineralsCell.text = String.format("%.2f", calculatedParameters.getDouble("Minerals"))
                categoryCell.gravity = Gravity.CENTER

                val fibreCell = TextView(this)
                fibreCell.text = String.format("%.2f", calculatedParameters.getDouble("Fibre"))
                categoryCell.gravity = Gravity.CENTER

                val carbosCell = TextView(this)
                carbosCell.text = String.format("%.2f", calculatedParameters.getDouble("Carbos"))
                categoryCell.gravity = Gravity.CENTER

                val calciumCell = TextView(this)
                calciumCell.text = String.format("%.2f", calculatedParameters.getDouble("Calcium"))


                val phosphorousCell = TextView(this)
                phosphorousCell.text = String.format("%.2f", calculatedParameters.getDouble("Phosphorous"))


                val ironCell = TextView(this)
                ironCell.text = String.format("%.2f", calculatedParameters.getDouble("Iron"))


                // Add the TextViews to the row in the desired order
                newRow.addView(categoryCell)
                newRow.addView(foodItemCell)
                newRow.addView(gramsCell)
                newRow.addView(energyCell)
                newRow.addView(proteinCell)
                newRow.addView(fatCell)
                newRow.addView(mineralsCell)
                newRow.addView(fibreCell)
                newRow.addView(carbosCell)
                newRow.addView(calciumCell)
                newRow.addView(phosphorousCell)
                newRow.addView(ironCell)

                // Add the row to the table
                resultsTable.addView(newRow)

                // Add calculated results to the array
                calculatedResults.add(calculatedParameters)

                // Clear the input field
                gramInput.text.clear()
            }
        }


// Total button click event
        totalButton.setOnClickListener {
            val totalParameters = JSONObject()
            totalParameters.put("Grams", 0.0)
            totalParameters.put("Energy", 0.0)
            totalParameters.put("Protein", 0.0)
            totalParameters.put("Fat", 0.0)
            totalParameters.put("Minerals", 0.0)
            totalParameters.put("Fibre", 0.0)
            totalParameters.put("Carbos", 0.0)
            totalParameters.put("Calcium", 0.0)
            totalParameters.put("Phosphorous", 0.0)
            totalParameters.put("Iron", 0.0)

            for (result in calculatedResults) {
                for (key in result.keys()) {
                    totalParameters.put(key, totalParameters.getDouble(key) + result.getDouble(key))
                }
            }

            // Create a new table row for the total and add it to the table
            val newRow = TableRow(this)
            val parameterCell = TextView(this)
            parameterCell.text = "Total"
            newRow.addView(parameterCell)

            val categoryTotalCell = TextView(this)
            categoryTotalCell.text = "Total"
            newRow.addView(categoryTotalCell)

            for (key in totalParameters.keys()) {
                val nutrientCell = TextView(this)
                nutrientCell.text = String.format("%.2f", totalParameters.getDouble(key))
                newRow.addView(nutrientCell)
            }

            totalTable.addView(newRow)
        }


// Inside the "Clear" button click event
        // Inside the "Clear" button click event
        clearButton.setOnClickListener {
            // Clear the calculatedResults list
            calculatedResults.clear()

            // Clear rows in resultsTable starting from the second row (index 1)
            for (i in resultsTable.childCount - 1 downTo 1) {
                resultsTable.removeViewAt(i)
            }

            // Clear rows in totalTable starting from the second row (index 1)
            for (i in totalTable.childCount - 1 downTo 1) {
                totalTable.removeViewAt(i)
            }
        }



    }

    private fun loadJSONFromAsset(fileName: String): JSONObject? {
        var json: String? = null
        try {
            val inputStream: InputStream = assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, Charsets.UTF_8)
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return JSONObject(json)
    }
}